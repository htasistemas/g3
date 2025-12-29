package br.com.g3.almoxarifado.serviceimpl;

import br.com.g3.almoxarifado.domain.AlmoxarifadoItem;
import br.com.g3.almoxarifado.domain.AlmoxarifadoMovimentacao;
import br.com.g3.almoxarifado.dto.AlmoxarifadoItemCriacaoRequest;
import br.com.g3.almoxarifado.dto.AlmoxarifadoItemResponse;
import br.com.g3.almoxarifado.dto.AlmoxarifadoMovimentacaoCadastroResponse;
import br.com.g3.almoxarifado.dto.AlmoxarifadoMovimentacaoRequest;
import br.com.g3.almoxarifado.dto.AlmoxarifadoMovimentacaoResponse;
import br.com.g3.almoxarifado.mapper.AlmoxarifadoMapper;
import br.com.g3.almoxarifado.repository.AlmoxarifadoRepository;
import br.com.g3.almoxarifado.service.AlmoxarifadoService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

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
  public List<AlmoxarifadoMovimentacaoResponse> listarMovimentacoes() {
    return repository.listarMovimentacoes().stream()
        .map(AlmoxarifadoMapper::toMovimentacaoResponse)
        .collect(Collectors.toList());
  }

  @Override
  public AlmoxarifadoMovimentacaoCadastroResponse registrarMovimentacao(
      AlmoxarifadoMovimentacaoRequest request) {
    AlmoxarifadoItem item =
        repository
            .buscarItemPorCodigo(request.getCodigoItem())
            .orElseThrow(() -> new IllegalArgumentException("Item de almoxarifado nao encontrado."));

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
}
