package br.com.g3.almoxarifado.serviceimpl;

import br.com.g3.almoxarifado.domain.AlmoxarifadoItem;
import br.com.g3.almoxarifado.domain.AlmoxarifadoMovimentacao;
import br.com.g3.almoxarifado.domain.MovimentacaoVinculoKit;
import br.com.g3.almoxarifado.domain.ProdutoKitComposicao;
import br.com.g3.almoxarifado.dto.AlmoxarifadoItemCriacaoRequest;
import br.com.g3.almoxarifado.dto.AlmoxarifadoItemResponse;
import br.com.g3.almoxarifado.dto.AlmoxarifadoMovimentacaoCadastroResponse;
import br.com.g3.almoxarifado.dto.AlmoxarifadoMovimentacaoRequest;
import br.com.g3.almoxarifado.dto.AlmoxarifadoMovimentacaoResponse;
import br.com.g3.almoxarifado.dto.MovimentacaoKitVinculoResponse;
import br.com.g3.almoxarifado.dto.ProdutoKitComposicaoRequest;
import br.com.g3.almoxarifado.dto.ProdutoKitComposicaoResponse;
import br.com.g3.almoxarifado.mapper.AlmoxarifadoMapper;
import br.com.g3.almoxarifado.repository.AlmoxarifadoRepository;
import br.com.g3.almoxarifado.service.AlmoxarifadoService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlmoxarifadoServiceImpl implements AlmoxarifadoService {
  private final AlmoxarifadoRepository repository;

  public AlmoxarifadoServiceImpl(AlmoxarifadoRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<AlmoxarifadoItemResponse> listarItens() {
    return repository.listarItens().stream()
        .map(AlmoxarifadoMapper::toResponse)
        .collect(Collectors.toList());
  }

  @Override
  public String obterProximoCodigo() {
    int proximo = repository.obterProximoCodigo();
    return String.format("%04d", proximo);
  }

  @Override
  public AlmoxarifadoItemResponse criarItem(AlmoxarifadoItemCriacaoRequest request) {
    validarDuplicidade(null, request);
    validarCodigoBarrasDuplicado(null, request);
    AlmoxarifadoItem item = AlmoxarifadoMapper.toDomain(request);
    AlmoxarifadoItem salvo = repository.salvarItem(item);
    return AlmoxarifadoMapper.toResponse(salvo);
  }

  @Override
  public AlmoxarifadoItemResponse atualizarItem(Long id, AlmoxarifadoItemCriacaoRequest request) {
    validarDuplicidade(id, request);
    validarCodigoBarrasDuplicado(id, request);
    AlmoxarifadoItem item =
        repository
            .buscarItemPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Item de almoxarifado nao encontrado."));
    AlmoxarifadoMapper.aplicarItem(item, request);
    AlmoxarifadoItem salvo = repository.salvarItem(item);
    return AlmoxarifadoMapper.toResponse(salvo);
  }

  @Override
  @Transactional(readOnly = true)
  public List<AlmoxarifadoMovimentacaoResponse> listarMovimentacoes() {
    return repository.listarMovimentacoes().stream()
        .map(AlmoxarifadoMapper::toMovimentacaoResponse)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public AlmoxarifadoMovimentacaoCadastroResponse registrarMovimentacao(
      AlmoxarifadoMovimentacaoRequest request) {
    AlmoxarifadoItem item =
        repository
            .buscarItemPorCodigo(request.getCodigoItem())
            .orElseThrow(() -> new IllegalArgumentException("Item de almoxarifado nao encontrado."));

    boolean isKit = Boolean.TRUE.equals(item.getIsKit());
    String tipoMovimento = request.getTipo();
    boolean gerarItensKit = Boolean.TRUE.equals(request.getGerarItensKit());
    if (isKit && "Saida".equalsIgnoreCase(tipoMovimento)) {
      validarDisponibilidadeKit(item, request);
    }

    int saldoAtual = item.getEstoqueAtual() != null ? item.getEstoqueAtual() : 0;
    int quantidade = request.getQuantidade() != null ? request.getQuantidade() : 0;
    int saldoApos = calcularSaldo(request.getTipo(), request.getDirecaoAjuste(), saldoAtual, quantidade);

    if (saldoApos < 0) {
      throw new IllegalArgumentException("A movimentacao nao pode deixar o estoque negativo.");
    }

    item.setEstoqueAtual(saldoApos);
    AlmoxarifadoItem salvoItem = repository.salvarItem(item);

    AlmoxarifadoMovimentacao movimentacao =
        AlmoxarifadoMapper.toMovimentacao(request, salvoItem, saldoApos);
    AlmoxarifadoMovimentacao salvaMovimentacao = repository.salvarMovimentacao(movimentacao);

    if (isKit && "Saida".equalsIgnoreCase(tipoMovimento)) {
      gerarBaixaItensKit(item, request, salvaMovimentacao);
    }
    if (isKit && "Entrada".equalsIgnoreCase(tipoMovimento) && gerarItensKit) {
      gerarEntradaItensKit(item, request, salvaMovimentacao);
    }

    return new AlmoxarifadoMovimentacaoCadastroResponse(
        AlmoxarifadoMapper.toMovimentacaoResponse(salvaMovimentacao),
        AlmoxarifadoMapper.toResponse(salvoItem));
  }

  private int calcularSaldo(String tipo, String direcaoAjuste, int saldoAtual, int quantidade) {
    if ("Entrada".equalsIgnoreCase(tipo)) {
      return saldoAtual + quantidade;
    }
    if ("Saida".equalsIgnoreCase(tipo)) {
      return saldoAtual - quantidade;
    }
    if ("Ajuste".equalsIgnoreCase(tipo)) {
      boolean aumentar = "increase".equalsIgnoreCase(direcaoAjuste);
      return aumentar ? saldoAtual + quantidade : saldoAtual - quantidade;
    }
    return saldoAtual;
  }

  private void validarDuplicidade(Long idAtual, AlmoxarifadoItemCriacaoRequest request) {
    String descricao = normalizarTexto(request.getDescricao());
    String categoria = normalizarTexto(request.getCategoria());
    String unidade = normalizarTexto(request.getUnidade());
    String localizacao = normalizarTexto(request.getLocalizacao());
    String localizacaoInterna = normalizarTexto(request.getLocalizacaoInterna());

    repository
        .buscarItemDuplicado(descricao, categoria, unidade, localizacao, localizacaoInterna)
        .filter((item) -> idAtual == null || !item.getId().equals(idAtual))
        .ifPresent(
            (item) -> {
              throw new IllegalArgumentException(
                  "Ja existe um item cadastrado com a mesma descricao, categoria, unidade e localizacao.");
            });
  }

  private void validarCodigoBarrasDuplicado(Long idAtual, AlmoxarifadoItemCriacaoRequest request) {
    String codigoBarras = normalizarTexto(request.getCodigoBarras());
    if (codigoBarras == null) {
      return;
    }

    repository
        .buscarItemPorCodigoBarras(codigoBarras)
        .filter((item) -> idAtual == null || !item.getId().equals(idAtual))
        .ifPresent(
            (item) -> {
              throw new IllegalArgumentException("Codigo de barras ja cadastrado para outro item.");
            });
  }

  private String normalizarTexto(String valor) {
    if (valor == null) {
      return null;
    }
    String texto = valor.trim();
    return texto.isEmpty() ? null : texto;
  }

  @Override
  public List<ProdutoKitComposicaoResponse> listarComposicaoKit(Long produtoKitId) {
    AlmoxarifadoItem kit =
        repository
            .buscarItemPorId(produtoKitId)
            .orElseThrow(() -> new IllegalArgumentException("Produto nao encontrado."));
    if (!Boolean.TRUE.equals(kit.getIsKit())) {
      return Collections.emptyList();
    }
    return repository.listarComposicaoKit(produtoKitId).stream()
        .map(this::mapComposicao)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public List<ProdutoKitComposicaoResponse> atualizarComposicaoKit(
      Long produtoKitId, List<ProdutoKitComposicaoRequest> itens) {
    AlmoxarifadoItem kit =
        repository
            .buscarItemPorId(produtoKitId)
            .orElseThrow(() -> new IllegalArgumentException("Produto nao encontrado."));
    if (!Boolean.TRUE.equals(kit.getIsKit())) {
      throw new IllegalArgumentException("O produto informado nao esta marcado como kit.");
    }
    if (itens == null || itens.isEmpty()) {
      repository.substituirComposicaoKit(produtoKitId, Collections.emptyList());
      return Collections.emptyList();
    }
    Set<Long> itensUnicos = new HashSet<>();
    List<ProdutoKitComposicao> composicao = new ArrayList<>();
    LocalDateTime agora = LocalDateTime.now();
    for (ProdutoKitComposicaoRequest item : itens) {
      if (item.getProdutoItemId() == null) {
        throw new IllegalArgumentException("Informe o item da composicao.");
      }
      if (item.getProdutoItemId().equals(produtoKitId)) {
        throw new IllegalArgumentException("O kit nao pode conter ele mesmo na composicao.");
      }
      if (!itensUnicos.add(item.getProdutoItemId())) {
        throw new IllegalArgumentException("Nao e permitido repetir itens na composicao.");
      }
      BigDecimal quantidade = item.getQuantidadeItem();
      if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("Quantidade do item deve ser maior que zero.");
      }
      repository
          .buscarItemPorId(item.getProdutoItemId())
          .orElseThrow(() -> new IllegalArgumentException("Item da composicao nao encontrado."));
      ProdutoKitComposicao novo = new ProdutoKitComposicao();
      novo.setProdutoKitId(produtoKitId);
      novo.setProdutoItemId(item.getProdutoItemId());
      novo.setQuantidadeItem(quantidade);
      novo.setAtivo(true);
      novo.setCriadoEm(agora);
      novo.setAtualizadoEm(agora);
      composicao.add(novo);
    }
    repository.substituirComposicaoKit(produtoKitId, composicao);
    return composicao.stream().map(this::mapComposicao).collect(Collectors.toList());
  }

  @Override
  public List<MovimentacaoKitVinculoResponse> listarVinculosKit(Long movimentacaoPrincipalId) {
    return repository.listarVinculosKit(movimentacaoPrincipalId).stream()
        .map(
            vinculo ->
                repository
                    .buscarMovimentacaoPorId(vinculo.getMovimentacaoGeradaId())
                    .map(
                        movimento ->
                            new MovimentacaoKitVinculoResponse(
                                movimento.getId(),
                                movimento.getDataMovimentacao(),
                                movimento.getTipo(),
                                movimento.getItem() != null ? movimento.getItem().getCodigo() : null,
                                movimento.getItem() != null ? movimento.getItem().getDescricao() : null,
                                movimento.getQuantidade(),
                                movimento.getSaldoApos()))
                    .orElse(null))
        .filter((response) -> response != null)
        .collect(Collectors.toList());
  }

  private ProdutoKitComposicaoResponse mapComposicao(ProdutoKitComposicao item) {
    AlmoxarifadoItem produto =
        repository
            .buscarItemPorId(item.getProdutoItemId())
            .orElse(null);
    return new ProdutoKitComposicaoResponse(
        item.getId(),
        item.getProdutoItemId(),
        produto != null ? produto.getCodigo() : null,
        produto != null ? produto.getDescricao() : null,
        item.getQuantidadeItem());
  }

  private void validarDisponibilidadeKit(AlmoxarifadoItem kit, AlmoxarifadoMovimentacaoRequest request) {
    List<ProdutoKitComposicao> composicao = repository.listarComposicaoKit(kit.getId());
    if (composicao.isEmpty()) {
      throw new IllegalArgumentException("Composicao do kit nao definida.");
    }
    List<String> faltantes = new ArrayList<>();
    for (ProdutoKitComposicao item : composicao) {
      AlmoxarifadoItem itemEstoque =
          repository
              .buscarItemPorId(item.getProdutoItemId())
              .orElseThrow(() -> new IllegalArgumentException("Item da composicao nao encontrado."));
      int saldoAtual = itemEstoque.getEstoqueAtual() != null ? itemEstoque.getEstoqueAtual() : 0;
      int necessario = calcularQuantidadeComposicao(item.getQuantidadeItem(), request.getQuantidade());
      if (saldoAtual < necessario) {
        faltantes.add(
            itemEstoque.getDescricao()
                + ": saldo "
                + saldoAtual
                + ", necessario "
                + necessario);
      }
    }
    if (!faltantes.isEmpty()) {
      throw new IllegalArgumentException("Itens sem estoque suficiente: " + String.join("; ", faltantes));
    }
  }

  private void gerarBaixaItensKit(
      AlmoxarifadoItem kit, AlmoxarifadoMovimentacaoRequest request, AlmoxarifadoMovimentacao principal) {
    List<ProdutoKitComposicao> composicao = repository.listarComposicaoKit(kit.getId());
    for (ProdutoKitComposicao item : composicao) {
      AlmoxarifadoItem itemEstoque =
          repository
              .buscarItemPorId(item.getProdutoItemId())
              .orElseThrow(() -> new IllegalArgumentException("Item da composicao nao encontrado."));
      int saldoAtual = itemEstoque.getEstoqueAtual() != null ? itemEstoque.getEstoqueAtual() : 0;
      int necessario = calcularQuantidadeComposicao(item.getQuantidadeItem(), request.getQuantidade());
      int saldoApos = saldoAtual - necessario;
      itemEstoque.setEstoqueAtual(saldoApos);
      AlmoxarifadoItem salvoItem = repository.salvarItem(itemEstoque);
      AlmoxarifadoMovimentacao movimentacaoItem =
          AlmoxarifadoMapper.toMovimentacao(request, salvoItem, saldoApos);
      movimentacaoItem.setQuantidade(necessario);
      movimentacaoItem.setTipo("Saida");
      AlmoxarifadoMovimentacao salvoMovimento = repository.salvarMovimentacao(movimentacaoItem);
      MovimentacaoVinculoKit vinculo = new MovimentacaoVinculoKit();
      vinculo.setMovimentacaoPrincipalId(principal.getId());
      vinculo.setMovimentacaoGeradaId(salvoMovimento.getId());
      vinculo.setCriadoEm(LocalDateTime.now());
      repository.salvarVinculoKit(vinculo);
    }
  }

  private void gerarEntradaItensKit(
      AlmoxarifadoItem kit, AlmoxarifadoMovimentacaoRequest request, AlmoxarifadoMovimentacao principal) {
    List<ProdutoKitComposicao> composicao = repository.listarComposicaoKit(kit.getId());
    if (composicao.isEmpty()) {
      return;
    }
    for (ProdutoKitComposicao item : composicao) {
      AlmoxarifadoItem itemEstoque =
          repository
              .buscarItemPorId(item.getProdutoItemId())
              .orElseThrow(() -> new IllegalArgumentException("Item da composicao nao encontrado."));
      int saldoAtual = itemEstoque.getEstoqueAtual() != null ? itemEstoque.getEstoqueAtual() : 0;
      int entrada = calcularQuantidadeComposicao(item.getQuantidadeItem(), request.getQuantidade());
      int saldoApos = saldoAtual + entrada;
      itemEstoque.setEstoqueAtual(saldoApos);
      AlmoxarifadoItem salvoItem = repository.salvarItem(itemEstoque);
      AlmoxarifadoMovimentacao movimentacaoItem =
          AlmoxarifadoMapper.toMovimentacao(request, salvoItem, saldoApos);
      movimentacaoItem.setQuantidade(entrada);
      movimentacaoItem.setTipo("Entrada");
      AlmoxarifadoMovimentacao salvoMovimento = repository.salvarMovimentacao(movimentacaoItem);
      MovimentacaoVinculoKit vinculo = new MovimentacaoVinculoKit();
      vinculo.setMovimentacaoPrincipalId(principal.getId());
      vinculo.setMovimentacaoGeradaId(salvoMovimento.getId());
      vinculo.setCriadoEm(LocalDateTime.now());
      repository.salvarVinculoKit(vinculo);
    }
  }

  private int calcularQuantidadeComposicao(BigDecimal quantidadeItem, Integer quantidadeKit) {
    BigDecimal quantidadeKitDecimal =
        quantidadeKit != null ? BigDecimal.valueOf(quantidadeKit) : BigDecimal.ZERO;
    BigDecimal total = quantidadeItem.multiply(quantidadeKitDecimal);
    return total.intValueExact();
  }
}
