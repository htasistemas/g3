package br.com.g3.doacaoplanejada.serviceimpl;

import br.com.g3.almoxarifado.domain.AlmoxarifadoItem;
import br.com.g3.almoxarifado.repository.AlmoxarifadoRepository;
import br.com.g3.cadastrobeneficiario.domain.CadastroBeneficiario;
import br.com.g3.cadastrobeneficiario.repository.CadastroBeneficiarioRepository;
import br.com.g3.doacaoplanejada.domain.DoacaoPlanejada;
import br.com.g3.doacaoplanejada.dto.DoacaoPlanejadaRequest;
import br.com.g3.doacaoplanejada.dto.DoacaoPlanejadaResponse;
import br.com.g3.doacaoplanejada.mapper.DoacaoPlanejadaMapper;
import br.com.g3.doacaoplanejada.repository.DoacaoPlanejadaRepository;
import br.com.g3.doacaoplanejada.service.DoacaoPlanejadaService;
import br.com.g3.vinculofamiliar.domain.VinculoFamiliar;
import br.com.g3.vinculofamiliar.repository.VinculoFamiliarRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DoacaoPlanejadaServiceImpl implements DoacaoPlanejadaService {
  private final DoacaoPlanejadaRepository repository;
  private final CadastroBeneficiarioRepository beneficiarioRepository;
  private final VinculoFamiliarRepository vinculoFamiliarRepository;
  private final AlmoxarifadoRepository almoxarifadoRepository;

  public DoacaoPlanejadaServiceImpl(
      DoacaoPlanejadaRepository repository,
      CadastroBeneficiarioRepository beneficiarioRepository,
      VinculoFamiliarRepository vinculoFamiliarRepository,
      AlmoxarifadoRepository almoxarifadoRepository) {
    this.repository = repository;
    this.beneficiarioRepository = beneficiarioRepository;
    this.vinculoFamiliarRepository = vinculoFamiliarRepository;
    this.almoxarifadoRepository = almoxarifadoRepository;
  }

  @Override
  public DoacaoPlanejadaResponse criar(DoacaoPlanejadaRequest request) {
    validarRequest(request);
    CadastroBeneficiario beneficiario = obterBeneficiario(request.getBeneficiarioId());
    VinculoFamiliar vinculoFamiliar = obterVinculoFamiliar(request.getVinculoFamiliarId());
    AlmoxarifadoItem item = obterItem(request.getItemCodigo());
    DoacaoPlanejada doacao = DoacaoPlanejadaMapper.toDomain(request, beneficiario, vinculoFamiliar, item);
    return DoacaoPlanejadaMapper.toResponse(repository.salvar(doacao));
  }

  @Override
  public DoacaoPlanejadaResponse atualizar(Long id, DoacaoPlanejadaRequest request) {
    validarRequest(request);
    DoacaoPlanejada doacao =
        repository
            .buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Doa��o planejada n�o encontrada."));
    CadastroBeneficiario beneficiario = obterBeneficiario(request.getBeneficiarioId());
    VinculoFamiliar vinculoFamiliar = obterVinculoFamiliar(request.getVinculoFamiliarId());
    AlmoxarifadoItem item = obterItem(request.getItemCodigo());
    DoacaoPlanejadaMapper.aplicarDados(doacao, request, beneficiario, vinculoFamiliar, item);
    return DoacaoPlanejadaMapper.toResponse(repository.salvar(doacao));
  }

  @Override
  public DoacaoPlanejadaResponse buscarPorId(Long id) {
    DoacaoPlanejada doacao =
        repository
            .buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Doa��o planejada n�o encontrada."));
    return DoacaoPlanejadaMapper.toResponse(doacao);
  }

  @Override
  public List<DoacaoPlanejadaResponse> listar() {
    return repository.listar().stream().map(DoacaoPlanejadaMapper::toResponse).toList();
  }

  @Override
  public List<DoacaoPlanejadaResponse> listarPorBeneficiario(Long beneficiarioId) {
    return repository.listarPorBeneficiario(beneficiarioId).stream()
        .map(DoacaoPlanejadaMapper::toResponse)
        .toList();
  }

  @Override
  public List<DoacaoPlanejadaResponse> listarPorVinculoFamiliar(Long vinculoFamiliarId) {
    return repository.listarPorVinculoFamiliar(vinculoFamiliarId).stream()
        .map(DoacaoPlanejadaMapper::toResponse)
        .toList();
  }

  @Override
  public void remover(Long id) {
    DoacaoPlanejada doacao =
        repository
            .buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Doa��o planejada n�o encontrada."));
    repository.remover(doacao);
  }

  private CadastroBeneficiario obterBeneficiario(Long id) {
    if (id == null) {
      return null;
    }
    return beneficiarioRepository
        .buscarPorId(id)
        .orElseThrow(() -> new IllegalArgumentException("Benefici�rio n�o encontrado."));
  }

  private VinculoFamiliar obterVinculoFamiliar(Long id) {
    if (id == null) {
      return null;
    }
    return vinculoFamiliarRepository
        .buscarPorId(id)
        .orElseThrow(() -> new IllegalArgumentException("Fam�lia n�o encontrada."));
  }

  private AlmoxarifadoItem obterItem(String codigo) {
    String codigoNormalizado = codigo != null ? codigo.trim() : null;
    if (codigoNormalizado == null || codigoNormalizado.isEmpty()) {
      throw new IllegalArgumentException("Informe o item do almoxarifado.");
    }
    return almoxarifadoRepository
        .buscarItemPorCodigo(codigoNormalizado)
        .orElseThrow(() -> new IllegalArgumentException("Item do almoxarifado n�o encontrado."));
  }

  private void validarRequest(DoacaoPlanejadaRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("Dados da doa��o planejada n�o informados.");
    }
    if (request.getBeneficiarioId() == null && request.getVinculoFamiliarId() == null) {
      throw new IllegalArgumentException("Informe o benefici�rio ou a fam�lia da doa��o planejada.");
    }
    if (request.getQuantidade() == null || request.getQuantidade() <= 0) {
      throw new IllegalArgumentException("Informe a quantidade da doa��o planejada.");
    }
    if (request.getDataPrevista() == null) {
      throw new IllegalArgumentException("Informe a data prevista da doa��o planejada.");
    }
    if (request.getPrioridade() == null || request.getPrioridade().trim().isEmpty()) {
      request.setPrioridade("media");
    }
    if (request.getStatus() == null || request.getStatus().trim().isEmpty()) {
      request.setStatus("pendente");
    }
  }
}
