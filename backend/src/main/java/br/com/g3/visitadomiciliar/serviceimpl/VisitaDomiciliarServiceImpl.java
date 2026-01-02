package br.com.g3.visitadomiciliar.serviceimpl;

import br.com.g3.cadastrobeneficiario.domain.CadastroBeneficiario;
import br.com.g3.cadastrobeneficiario.repository.CadastroBeneficiarioRepository;
import br.com.g3.visitadomiciliar.domain.VisitaDomiciliar;
import br.com.g3.visitadomiciliar.domain.VisitaDomiciliarAnexo;
import br.com.g3.visitadomiciliar.dto.VisitaDomiciliarAnexoRequest;
import br.com.g3.visitadomiciliar.dto.VisitaDomiciliarAnexoResponse;
import br.com.g3.visitadomiciliar.dto.VisitaDomiciliarListaResponse;
import br.com.g3.visitadomiciliar.dto.VisitaDomiciliarRequest;
import br.com.g3.visitadomiciliar.dto.VisitaDomiciliarResponse;
import br.com.g3.visitadomiciliar.repository.VisitaDomiciliarAnexoRepository;
import br.com.g3.visitadomiciliar.repository.VisitaDomiciliarRepository;
import br.com.g3.visitadomiciliar.service.VisitaDomiciliarService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class VisitaDomiciliarServiceImpl implements VisitaDomiciliarService {
  private final VisitaDomiciliarRepository visitaRepository;
  private final VisitaDomiciliarAnexoRepository anexoRepository;
  private final CadastroBeneficiarioRepository beneficiarioRepository;

  public VisitaDomiciliarServiceImpl(
      VisitaDomiciliarRepository visitaRepository,
      VisitaDomiciliarAnexoRepository anexoRepository,
      CadastroBeneficiarioRepository beneficiarioRepository) {
    this.visitaRepository = visitaRepository;
    this.anexoRepository = anexoRepository;
    this.beneficiarioRepository = beneficiarioRepository;
  }

  @Override
  @Transactional
  public VisitaDomiciliarResponse criar(VisitaDomiciliarRequest request) {
    CadastroBeneficiario beneficiario = obterBeneficiario(request.getBeneficiarioId());
    VisitaDomiciliar visita = new VisitaDomiciliar();
    visita.setBeneficiarioId(beneficiario.getId());
    aplicarRequest(request, visita);
    LocalDateTime agora = LocalDateTime.now();
    visita.setCriadoEm(agora);
    visita.setAtualizadoEm(agora);
    VisitaDomiciliar salvo = visitaRepository.salvar(visita);
    salvarAnexos(salvo.getId(), request.getAnexos());
    return mapResponse(salvo, beneficiario.getNomeCompleto());
  }

  @Override
  @Transactional
  public VisitaDomiciliarResponse atualizar(Long id, VisitaDomiciliarRequest request) {
    VisitaDomiciliar visita =
        visitaRepository
            .buscarPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Visita nao encontrada."));
    CadastroBeneficiario beneficiario = obterBeneficiario(request.getBeneficiarioId());
    visita.setBeneficiarioId(beneficiario.getId());
    aplicarRequest(request, visita);
    visita.setAtualizadoEm(LocalDateTime.now());
    VisitaDomiciliar salvo = visitaRepository.salvar(visita);
    anexoRepository.removerPorVisitaId(salvo.getId());
    salvarAnexos(salvo.getId(), request.getAnexos());
    return mapResponse(salvo, beneficiario.getNomeCompleto());
  }

  @Override
  @Transactional
  public void excluir(Long id) {
    VisitaDomiciliar visita =
        visitaRepository
            .buscarPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Visita nao encontrada."));
    visitaRepository.remover(visita);
  }

  @Override
  @Transactional(readOnly = true)
  public VisitaDomiciliarListaResponse listar() {
    List<VisitaDomiciliar> visitas = visitaRepository.listar();
    Map<Long, String> beneficiarios = carregarBeneficiarios(visitas);
    Map<Long, List<VisitaDomiciliarAnexoResponse>> anexosPorVisita =
        carregarAnexos(visitas);
    List<VisitaDomiciliarResponse> responses =
        visitas.stream()
            .map(
                visita -> {
                  VisitaDomiciliarResponse response =
                      mapResponse(visita, beneficiarios.get(visita.getBeneficiarioId()));
                  response.setAnexos(
                      anexosPorVisita.getOrDefault(visita.getId(), new ArrayList<>()));
                  return response;
                })
            .collect(Collectors.toList());
    return new VisitaDomiciliarListaResponse(responses);
  }

  private CadastroBeneficiario obterBeneficiario(Long beneficiarioId) {
    return beneficiarioRepository
        .buscarPorId(beneficiarioId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Beneficiario nao encontrado."));
  }

  private void aplicarRequest(VisitaDomiciliarRequest request, VisitaDomiciliar visita) {
    visita.setUnidade(request.getUnidade());
    visita.setResponsavel(request.getResponsavel());
    visita.setDataVisita(request.getDataVisita());
    visita.setHorarioInicial(request.getHorarioInicial());
    visita.setHorarioFinal(request.getHorarioFinal());
    visita.setTipoVisita(request.getTipoVisita());
    visita.setSituacao(request.getSituacao());
    visita.setUsarEnderecoBeneficiario(
        request.getUsarEnderecoBeneficiario() != null
            ? request.getUsarEnderecoBeneficiario()
            : Boolean.TRUE);
    visita.setEndereco(request.getEndereco());
    visita.setObservacoesIniciais(request.getObservacoesIniciais());
    visita.setCondicoes(request.getCondicoes());
    visita.setSituacaoSocial(request.getSituacaoSocial());
    visita.setRegistro(request.getRegistro());
  }

  private void salvarAnexos(Long visitaId, List<VisitaDomiciliarAnexoRequest> anexos) {
    if (anexos == null || anexos.isEmpty()) {
      return;
    }
    LocalDateTime agora = LocalDateTime.now();
    for (VisitaDomiciliarAnexoRequest anexo : anexos) {
      if (anexo.getNome() == null || anexo.getTipo() == null) {
        continue;
      }
      VisitaDomiciliarAnexo entidade = new VisitaDomiciliarAnexo();
      entidade.setVisitaId(visitaId);
      entidade.setNome(anexo.getNome());
      entidade.setTipo(anexo.getTipo());
      entidade.setTamanho(anexo.getTamanho());
      entidade.setCriadoEm(agora);
      anexoRepository.salvar(entidade);
    }
  }

  private VisitaDomiciliarResponse mapResponse(VisitaDomiciliar visita, String beneficiarioNome) {
    VisitaDomiciliarResponse response = new VisitaDomiciliarResponse();
    response.setId(visita.getId());
    response.setBeneficiarioId(visita.getBeneficiarioId());
    response.setBeneficiarioNome(beneficiarioNome);
    response.setUnidade(visita.getUnidade());
    response.setResponsavel(visita.getResponsavel());
    response.setDataVisita(visita.getDataVisita());
    response.setHorarioInicial(visita.getHorarioInicial());
    response.setHorarioFinal(visita.getHorarioFinal());
    response.setTipoVisita(visita.getTipoVisita());
    response.setSituacao(visita.getSituacao());
    response.setUsarEnderecoBeneficiario(visita.getUsarEnderecoBeneficiario());
    response.setEndereco(visita.getEndereco());
    response.setObservacoesIniciais(visita.getObservacoesIniciais());
    response.setCondicoes(visita.getCondicoes());
    response.setSituacaoSocial(visita.getSituacaoSocial());
    response.setRegistro(visita.getRegistro());
    response.setCriadoEm(visita.getCriadoEm());
    response.setAtualizadoEm(visita.getAtualizadoEm());
    return response;
  }

  private Map<Long, String> carregarBeneficiarios(List<VisitaDomiciliar> visitas) {
    List<Long> ids =
        visitas.stream()
            .map(VisitaDomiciliar::getBeneficiarioId)
            .distinct()
            .collect(Collectors.toList());
    Map<Long, String> resultado = new HashMap<>();
    if (ids.isEmpty()) {
      return resultado;
    }
    for (Long id : ids) {
      Optional<CadastroBeneficiario> beneficiario = beneficiarioRepository.buscarPorId(id);
      beneficiario.ifPresent(item -> resultado.put(id, item.getNomeCompleto()));
    }
    return resultado;
  }

  private Map<Long, List<VisitaDomiciliarAnexoResponse>> carregarAnexos(List<VisitaDomiciliar> visitas) {
    List<Long> ids =
        visitas.stream().map(VisitaDomiciliar::getId).filter(id -> id != null).collect(Collectors.toList());
    List<VisitaDomiciliarAnexoResponse> anexos =
        anexoRepository.listarPorVisitaIds(ids).stream()
            .map(this::mapAnexo)
            .collect(Collectors.toList());
    Map<Long, List<VisitaDomiciliarAnexoResponse>> agrupado = new HashMap<>();
    for (VisitaDomiciliarAnexoResponse anexo : anexos) {
      agrupado.computeIfAbsent(anexo.getVisitaId(), key -> new ArrayList<>()).add(anexo);
    }
    return agrupado;
  }

  private VisitaDomiciliarAnexoResponse mapAnexo(VisitaDomiciliarAnexo anexo) {
    VisitaDomiciliarAnexoResponse response = new VisitaDomiciliarAnexoResponse();
    response.setId(anexo.getId());
    response.setVisitaId(anexo.getVisitaId());
    response.setNome(anexo.getNome());
    response.setTipo(anexo.getTipo());
    response.setTamanho(anexo.getTamanho());
    response.setCriadoEm(anexo.getCriadoEm());
    return response;
  }

}
