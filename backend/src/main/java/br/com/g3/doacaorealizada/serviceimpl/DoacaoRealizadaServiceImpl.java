package br.com.g3.doacaorealizada.serviceimpl;

import br.com.g3.almoxarifado.domain.AlmoxarifadoItem;
import br.com.g3.almoxarifado.repository.AlmoxarifadoRepository;
import br.com.g3.cadastrobeneficiario.domain.CadastroBeneficiario;
import br.com.g3.cadastrobeneficiario.repository.CadastroBeneficiarioRepository;
import br.com.g3.doacaorealizada.domain.DoacaoRealizada;
import br.com.g3.doacaorealizada.domain.DoacaoRealizadaItem;
import br.com.g3.doacaorealizada.dto.DoacaoRealizadaItemRequest;
import br.com.g3.doacaorealizada.dto.DoacaoRealizadaRequest;
import br.com.g3.doacaorealizada.dto.DoacaoRealizadaResponse;
import br.com.g3.doacaorealizada.mapper.DoacaoRealizadaMapper;
import br.com.g3.doacaorealizada.repository.DoacaoRealizadaRepository;
import br.com.g3.doacaorealizada.service.DoacaoRealizadaService;
import br.com.g3.vinculofamiliar.domain.VinculoFamiliar;
import br.com.g3.vinculofamiliar.repository.VinculoFamiliarRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoacaoRealizadaServiceImpl implements DoacaoRealizadaService {
  private final DoacaoRealizadaRepository repository;
  private final CadastroBeneficiarioRepository beneficiarioRepository;
  private final VinculoFamiliarRepository vinculoFamiliarRepository;
  private final AlmoxarifadoRepository almoxarifadoRepository;

  public DoacaoRealizadaServiceImpl(
      DoacaoRealizadaRepository repository,
      CadastroBeneficiarioRepository beneficiarioRepository,
      VinculoFamiliarRepository vinculoFamiliarRepository,
      AlmoxarifadoRepository almoxarifadoRepository) {
    this.repository = repository;
    this.beneficiarioRepository = beneficiarioRepository;
    this.vinculoFamiliarRepository = vinculoFamiliarRepository;
    this.almoxarifadoRepository = almoxarifadoRepository;
  }

  @Override
  @Transactional
  public DoacaoRealizadaResponse criar(DoacaoRealizadaRequest request) {
    validarReferencias(request);
    validarItens(request);
    DoacaoRealizada doacao = DoacaoRealizadaMapper.toDomain(request);
    aplicarBeneficiario(doacao, request.getBeneficiarioId());
    aplicarFamilia(doacao, request.getVinculoFamiliarId());
    aplicarItens(doacao, request.getItens());
    return DoacaoRealizadaMapper.toResponse(repository.salvar(doacao));
  }

  @Override
  @Transactional
  public DoacaoRealizadaResponse atualizar(Long id, DoacaoRealizadaRequest request) {
    validarReferencias(request);
    validarItens(request);
    DoacaoRealizada doacao =
        repository
            .buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Doacao realizada nao encontrada."));
    validarPrazoEdicao(doacao);
    restaurarEstoqueItens(doacao);
    DoacaoRealizadaMapper.aplicarDados(doacao, request);
    aplicarBeneficiario(doacao, request.getBeneficiarioId());
    aplicarFamilia(doacao, request.getVinculoFamiliarId());
    aplicarItens(doacao, request.getItens());
    return DoacaoRealizadaMapper.toResponse(repository.salvar(doacao));
  }

  @Override
  @Transactional(readOnly = true)
  public DoacaoRealizadaResponse buscarPorId(Long id) {
    DoacaoRealizada doacao =
        repository
            .buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Doacao realizada nao encontrada."));
    return DoacaoRealizadaMapper.toResponse(doacao);
  }

  @Override
  @Transactional(readOnly = true)
  public List<DoacaoRealizadaResponse> listar() {
    return repository.listar().stream()
        .map(DoacaoRealizadaMapper::toResponse)
        .collect(Collectors.toList());
  }

  private void aplicarBeneficiario(DoacaoRealizada doacao, Long beneficiarioId) {
    if (beneficiarioId == null) {
      doacao.setBeneficiario(null);
      return;
    }
    CadastroBeneficiario beneficiario =
        beneficiarioRepository
            .buscarPorId(beneficiarioId)
            .orElseThrow(() -> new IllegalArgumentException("Beneficiario nao encontrado."));
    doacao.setBeneficiario(beneficiario);
  }

  private void aplicarFamilia(DoacaoRealizada doacao, Long vinculoFamiliarId) {
    if (vinculoFamiliarId == null) {
      doacao.setVinculoFamiliar(null);
      return;
    }
    VinculoFamiliar vinculo =
        vinculoFamiliarRepository
            .buscarPorId(vinculoFamiliarId)
            .orElseThrow(() -> new IllegalArgumentException("Familia nao encontrada."));
    doacao.setVinculoFamiliar(vinculo);
  }

  private void aplicarItens(DoacaoRealizada doacao, List<DoacaoRealizadaItemRequest> itens) {
    doacao.getItens().clear();
    if (itens == null || itens.isEmpty()) {
      return;
    }
    List<DoacaoRealizadaItem> novosItens = new ArrayList<>();
    for (DoacaoRealizadaItemRequest itemRequest : itens) {
      AlmoxarifadoItem item =
          almoxarifadoRepository
              .buscarItemPorId(itemRequest.getItemId())
              .orElseThrow(() -> new IllegalArgumentException("Item do almoxarifado nao encontrado."));
      Integer estoqueAtual = item.getEstoqueAtual();
      int quantidade = itemRequest.getQuantidade() != null ? itemRequest.getQuantidade() : 0;
      if (estoqueAtual == null || estoqueAtual < quantidade) {
        throw new IllegalArgumentException("Estoque insuficiente para o item " + item.getDescricao() + ".");
      }
      item.setEstoqueAtual(estoqueAtual - quantidade);
      item.setAtualizadoEm(LocalDateTime.now());
      almoxarifadoRepository.salvarItem(item);
      novosItens.add(DoacaoRealizadaMapper.toItemDomain(itemRequest, doacao, item));
    }
    doacao.setItens(novosItens);
  }

  private void restaurarEstoqueItens(DoacaoRealizada doacao) {
    for (DoacaoRealizadaItem itemDoacao : doacao.getItens()) {
      AlmoxarifadoItem item = itemDoacao.getItem();
      if (item == null || itemDoacao.getQuantidade() == null) {
        continue;
      }
      Integer estoqueAtual = item.getEstoqueAtual();
      if (estoqueAtual == null) {
        continue;
      }
      item.setEstoqueAtual(estoqueAtual + itemDoacao.getQuantidade());
      item.setAtualizadoEm(LocalDateTime.now());
      almoxarifadoRepository.salvarItem(item);
    }
  }

  private void validarPrazoEdicao(DoacaoRealizada doacao) {
    LocalDate dataDoacao = doacao.getDataDoacao();
    if (dataDoacao == null) {
      return;
    }
    long dias = ChronoUnit.DAYS.between(dataDoacao, LocalDate.now());
    if (dias > 10) {
      throw new IllegalArgumentException("Doacao realizada ha mais de 10 dias nao pode ser alterada.");
    }
  }

  private void validarReferencias(DoacaoRealizadaRequest request) {
    if (request.getBeneficiarioId() == null && request.getVinculoFamiliarId() == null) {
      throw new IllegalArgumentException("Informe um beneficiario ou uma familia para registrar a doacao.");
    }
  }

  private void validarItens(DoacaoRealizadaRequest request) {
    if (request.getItens() == null || request.getItens().isEmpty()) {
      throw new IllegalArgumentException("Informe ao menos um item para registrar a doacao.");
    }
  }
}
