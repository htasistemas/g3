package br.com.g3.termofomento.serviceimpl;

import br.com.g3.termofomento.domain.TermoFomento;
import br.com.g3.termofomento.domain.TermoFomentoAditivo;
import br.com.g3.termofomento.domain.TermoFomentoDocumento;
import br.com.g3.termofomento.dto.TermoDocumentoRequest;
import br.com.g3.termofomento.dto.TermoDocumentoResponse;
import br.com.g3.termofomento.dto.TermoFomentoAditivoRequest;
import br.com.g3.termofomento.dto.TermoFomentoAditivoResponse;
import br.com.g3.termofomento.dto.TermoFomentoRequest;
import br.com.g3.termofomento.dto.TermoFomentoResponse;
import br.com.g3.termofomento.repository.TermoFomentoAditivoRepository;
import br.com.g3.termofomento.repository.TermoFomentoDocumentoRepository;
import br.com.g3.termofomento.repository.TermoFomentoRepository;
import br.com.g3.termofomento.service.TermoFomentoService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TermoFomentoServiceImpl implements TermoFomentoService {
  private final TermoFomentoRepository repository;
  private final TermoFomentoAditivoRepository aditivoRepository;
  private final TermoFomentoDocumentoRepository documentoRepository;

  public TermoFomentoServiceImpl(
      TermoFomentoRepository repository,
      TermoFomentoAditivoRepository aditivoRepository,
      TermoFomentoDocumentoRepository documentoRepository) {
    this.repository = repository;
    this.aditivoRepository = aditivoRepository;
    this.documentoRepository = documentoRepository;
  }

  @Override
  public List<TermoFomentoResponse> listar() {
    List<TermoFomento> termos = repository.listar();
    List<TermoFomentoResponse> resposta = new ArrayList<>();
    for (TermoFomento termo : termos) {
      resposta.add(mapTermo(termo));
    }
    return resposta;
  }

  @Override
  public TermoFomentoResponse obter(Long id) {
    return mapTermo(buscarTermo(id));
  }

  @Override
  @Transactional
  public TermoFomentoResponse criar(TermoFomentoRequest request) {
    TermoFomento termo = mapTermoRequest(new TermoFomento(), request);
    LocalDateTime agora = LocalDateTime.now();
    termo.setCriadoEm(agora);
    termo.setAtualizadoEm(agora);
    TermoFomento salvo = repository.salvar(termo);
    salvarDocumentos(salvo.getId(), null, request.getTermoDocumento(), request.getDocumentosRelacionados());
    salvarAditivos(salvo.getId(), request.getAditivos());
    return mapTermo(buscarTermo(salvo.getId()));
  }

  @Override
  @Transactional
  public TermoFomentoResponse atualizar(Long id, TermoFomentoRequest request) {
    TermoFomento termo = buscarTermo(id);
    mapTermoRequest(termo, request);
    termo.setAtualizadoEm(LocalDateTime.now());
    TermoFomento salvo = repository.salvar(termo);
    documentoRepository.removerPorTermo(salvo.getId());
    aditivoRepository.removerPorTermo(salvo.getId());
    salvarDocumentos(salvo.getId(), null, request.getTermoDocumento(), request.getDocumentosRelacionados());
    salvarAditivos(salvo.getId(), request.getAditivos());
    return mapTermo(buscarTermo(salvo.getId()));
  }

  @Override
  @Transactional
  public TermoFomentoResponse adicionarAditivo(Long id, TermoFomentoAditivoRequest request) {
    TermoFomento termo = buscarTermo(id);
    TermoFomentoAditivo aditivo = mapAditivoRequest(id, request);
    TermoFomentoAditivo salvo = aditivoRepository.salvar(aditivo);
    salvarDocumentoAditivo(id, salvo.getId(), request.getAnexo());
    termo.setAtualizadoEm(LocalDateTime.now());
    repository.salvar(termo);
    return mapTermo(buscarTermo(id));
  }

  @Override
  @Transactional
  public void excluir(Long id) {
    TermoFomento termo = buscarTermo(id);
    documentoRepository.removerPorTermo(id);
    aditivoRepository.removerPorTermo(id);
    repository.remover(termo);
  }

  private TermoFomento buscarTermo(Long id) {
    Optional<TermoFomento> termo = repository.buscarPorId(id);
    if (!termo.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Termo de fomento nao encontrado.");
    }
    return termo.get();
  }

  private TermoFomento mapTermoRequest(TermoFomento termo, TermoFomentoRequest request) {
    termo.setNumeroTermo(request.getNumeroTermo());
    termo.setTipoTermo(request.getTipoTermo());
    termo.setOrgaoConcedente(request.getOrgaoConcedente());
    termo.setDataAssinatura(request.getDataAssinatura());
    termo.setDataInicioVigencia(request.getDataInicioVigencia());
    termo.setDataFimVigencia(request.getDataFimVigencia());
    termo.setSituacao(request.getSituacao());
    termo.setDescricaoObjeto(request.getDescricaoObjeto());
    termo.setValorGlobal(request.getValorGlobal());
    termo.setResponsavelInterno(request.getResponsavelInterno());
    return termo;
  }

  private void salvarDocumentos(
      Long termoId,
      Long aditivoId,
      TermoDocumentoRequest termoDocumento,
      List<TermoDocumentoRequest> relacionados) {
    LocalDateTime agora = LocalDateTime.now();
    if (termoDocumento != null) {
      documentoRepository.salvar(
          criarDocumento(termoId, aditivoId, termoDocumento, agora, "TERMO"));
    }
    if (relacionados != null) {
      for (TermoDocumentoRequest doc : relacionados) {
        documentoRepository.salvar(
            criarDocumento(termoId, aditivoId, doc, agora, "RELACIONADO"));
      }
    }
  }

  private void salvarAditivos(Long termoId, List<TermoFomentoAditivoRequest> aditivos) {
    if (aditivos == null) {
      return;
    }
    for (TermoFomentoAditivoRequest request : aditivos) {
      TermoFomentoAditivo aditivo = mapAditivoRequest(termoId, request);
      TermoFomentoAditivo salvo = aditivoRepository.salvar(aditivo);
      salvarDocumentoAditivo(termoId, salvo.getId(), request.getAnexo());
    }
  }

  private void salvarDocumentoAditivo(Long termoId, Long aditivoId, TermoDocumentoRequest anexo) {
    if (anexo == null) {
      return;
    }
    LocalDateTime agora = LocalDateTime.now();
    documentoRepository.salvar(criarDocumento(termoId, aditivoId, anexo, agora, "ADITIVO"));
  }

  private TermoFomentoDocumento criarDocumento(
      Long termoId,
      Long aditivoId,
      TermoDocumentoRequest request,
      LocalDateTime agora,
      String tipoPadrao) {
    TermoFomentoDocumento documento = new TermoFomentoDocumento();
    documento.setTermoFomentoId(termoId);
    documento.setAditivoId(aditivoId);
    documento.setNome(request.getNome());
    documento.setDataUrl(request.getDataUrl());
    documento.setTipoDocumento(
        request.getTipo() != null && !request.getTipo().trim().isEmpty()
            ? request.getTipo()
            : tipoPadrao);
    documento.setCriadoEm(agora);
    return documento;
  }

  private TermoFomentoAditivo mapAditivoRequest(Long termoId, TermoFomentoAditivoRequest request) {
    TermoFomentoAditivo aditivo = new TermoFomentoAditivo();
    aditivo.setTermoFomentoId(termoId);
    aditivo.setTipoAditivo(request.getTipoAditivo());
    aditivo.setDataAditivo(request.getDataAditivo());
    aditivo.setNovaDataFim(request.getNovaDataFim());
    aditivo.setNovoValor(request.getNovoValor());
    aditivo.setObservacoes(request.getObservacoes());
    LocalDateTime agora = LocalDateTime.now();
    aditivo.setCriadoEm(agora);
    aditivo.setAtualizadoEm(agora);
    return aditivo;
  }

  private TermoFomentoResponse mapTermo(TermoFomento termo) {
    List<TermoFomentoDocumento> documentos = documentoRepository.listarPorTermo(termo.getId());
    List<TermoFomentoAditivo> aditivos = aditivoRepository.listarPorTermo(termo.getId());
    TermoDocumentoResponse termoDocumento = null;
    List<TermoDocumentoResponse> relacionados = new ArrayList<>();
    for (TermoFomentoDocumento documento : documentos) {
      TermoDocumentoResponse response =
          new TermoDocumentoResponse(
              documento.getId(), documento.getNome(), documento.getDataUrl(), documento.getTipoDocumento());
      if ("TERMO".equalsIgnoreCase(documento.getTipoDocumento())) {
        termoDocumento = response;
      } else if ("RELACIONADO".equalsIgnoreCase(documento.getTipoDocumento())) {
        relacionados.add(response);
      }
    }
    List<TermoFomentoAditivoResponse> aditivosResponse = new ArrayList<>();
    for (TermoFomentoAditivo aditivo : aditivos) {
      TermoDocumentoResponse anexo = null;
      for (TermoFomentoDocumento doc : documentoRepository.listarPorAditivo(aditivo.getId())) {
        if ("ADITIVO".equalsIgnoreCase(doc.getTipoDocumento())) {
          anexo = new TermoDocumentoResponse(doc.getId(), doc.getNome(), doc.getDataUrl(), doc.getTipoDocumento());
          break;
        }
      }
      aditivosResponse.add(
          new TermoFomentoAditivoResponse(
              aditivo.getId(),
              aditivo.getTipoAditivo(),
              aditivo.getDataAditivo(),
              aditivo.getNovaDataFim(),
              aditivo.getNovoValor(),
              aditivo.getObservacoes(),
              anexo));
    }
    return new TermoFomentoResponse(
        termo.getId(),
        termo.getNumeroTermo(),
        termo.getTipoTermo(),
        termo.getOrgaoConcedente(),
        termo.getDataAssinatura(),
        termo.getDataInicioVigencia(),
        termo.getDataFimVigencia(),
        termo.getSituacao(),
        termo.getDescricaoObjeto(),
        termo.getValorGlobal(),
        termo.getResponsavelInterno(),
        termoDocumento,
        relacionados,
        aditivosResponse,
        termo.getCriadoEm(),
        termo.getAtualizadoEm());
  }
}
