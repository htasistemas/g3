package br.com.g3.oficios.serviceimpl;

import br.com.g3.oficios.domain.Oficio;
import br.com.g3.oficios.domain.OficioTramite;
import br.com.g3.oficios.dto.OficioConteudoRequest;
import br.com.g3.oficios.dto.OficioConteudoResponse;
import br.com.g3.oficios.dto.OficioIdentificacaoRequest;
import br.com.g3.oficios.dto.OficioIdentificacaoResponse;
import br.com.g3.oficios.dto.OficioListaResponse;
import br.com.g3.oficios.dto.OficioProtocoloRequest;
import br.com.g3.oficios.dto.OficioProtocoloResponse;
import br.com.g3.oficios.dto.OficioRequest;
import br.com.g3.oficios.dto.OficioResponse;
import br.com.g3.oficios.dto.OficioTramiteRequest;
import br.com.g3.oficios.dto.OficioTramiteResponse;
import br.com.g3.oficios.repository.OficioRepository;
import br.com.g3.oficios.repository.OficioTramiteRepository;
import br.com.g3.oficios.service.OficioService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OficioServiceImpl implements OficioService {
  private final OficioRepository repository;
  private final OficioTramiteRepository tramiteRepository;

  public OficioServiceImpl(
      OficioRepository repository,
      OficioTramiteRepository tramiteRepository) {
    this.repository = repository;
    this.tramiteRepository = tramiteRepository;
  }

  @Override
  public OficioListaResponse listar() {
    List<OficioResponse> resposta = new ArrayList<>();
    for (Oficio oficio : repository.listar()) {
      resposta.add(mapOficio(oficio));
    }
    return new OficioListaResponse(resposta);
  }

  @Override
  public OficioResponse obter(Long id) {
    return mapOficio(buscarOficio(id));
  }

  @Override
  @Transactional
  public OficioResponse criar(OficioRequest request) {
    Oficio oficio = mapOficioRequest(new Oficio(), request);
    LocalDateTime agora = LocalDateTime.now();
    oficio.setCriadoEm(agora);
    oficio.setAtualizadoEm(agora);
    Oficio salvo = repository.salvar(oficio);
    salvarTramites(salvo.getId(), request.getTramites(), agora);
    return mapOficio(buscarOficio(salvo.getId()));
  }

  @Override
  @Transactional
  public OficioResponse atualizar(Long id, OficioRequest request) {
    Oficio oficio = buscarOficio(id);
    mapOficioRequest(oficio, request);
    oficio.setAtualizadoEm(LocalDateTime.now());
    Oficio salvo = repository.salvar(oficio);
    tramiteRepository.removerPorOficio(salvo.getId());
    salvarTramites(salvo.getId(), request.getTramites(), LocalDateTime.now());
    return mapOficio(buscarOficio(salvo.getId()));
  }

  @Override
  @Transactional
  public void excluir(Long id) {
    Oficio oficio = buscarOficio(id);
    tramiteRepository.removerPorOficio(oficio.getId());
    repository.remover(oficio);
  }

  private Oficio buscarOficio(Long id) {
    Optional<Oficio> oficio = repository.buscarPorId(id);
    if (!oficio.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Oficio nao encontrado.");
    }
    return oficio.get();
  }

  private void salvarTramites(Long oficioId, List<OficioTramiteRequest> tramites, LocalDateTime agora) {
    if (tramites == null) {
      return;
    }
    for (OficioTramiteRequest tramiteRequest : tramites) {
      OficioTramite tramite = new OficioTramite();
      tramite.setOficioId(oficioId);
      tramite.setData(tramiteRequest.getData());
      tramite.setOrigem(tramiteRequest.getOrigem());
      tramite.setDestino(tramiteRequest.getDestino());
      tramite.setResponsavel(tramiteRequest.getResponsavel());
      tramite.setAcao(tramiteRequest.getAcao());
      tramite.setObservacoes(tramiteRequest.getObservacoes());
      tramite.setCriadoEm(agora);
      tramite.setAtualizadoEm(agora);
      tramiteRepository.salvar(tramite);
    }
  }

  private Oficio mapOficioRequest(Oficio oficio, OficioRequest request) {
    OficioIdentificacaoRequest identificacao = request.getIdentificacao();
    OficioConteudoRequest conteudo = request.getConteudo();
    OficioProtocoloRequest protocolo = request.getProtocolo();

    oficio.setTipo(identificacao.getTipo());
    oficio.setNumero(identificacao.getNumero());
    oficio.setData(identificacao.getData());
    oficio.setSetorOrigem(identificacao.getSetorOrigem());
    oficio.setResponsavel(identificacao.getResponsavel());
    oficio.setDestinatario(identificacao.getDestinatario());
    oficio.setMeioEnvio(identificacao.getMeioEnvio());
    oficio.setPrazoResposta(identificacao.getPrazoResposta());
    oficio.setClassificacao(identificacao.getClassificacao());

    oficio.setRazaoSocial(conteudo.getRazaoSocial());
    oficio.setLogoUrl(conteudo.getLogoUrl());
    oficio.setTitulo(conteudo.getTitulo());
    oficio.setSaudacao(conteudo.getSaudacao());
    oficio.setAssunto(conteudo.getAssunto());
    oficio.setCorpo(conteudo.getCorpo());
    oficio.setFinalizacao(conteudo.getFinalizacao());
    oficio.setAssinaturaNome(conteudo.getAssinaturaNome());
    oficio.setAssinaturaCargo(conteudo.getAssinaturaCargo());
    oficio.setRodape(conteudo.getRodape());

    oficio.setStatus(protocolo.getStatus());
    oficio.setProtocoloEnvio(protocolo.getProtocoloEnvio());
    oficio.setDataEnvio(protocolo.getDataEnvio());
    oficio.setProtocoloRecebimento(protocolo.getProtocoloRecebimento());
    oficio.setDataRecebimento(protocolo.getDataRecebimento());
    oficio.setProximoDestino(protocolo.getProximoDestino());
    oficio.setObservacoes(protocolo.getObservacoes());
    return oficio;
  }

  private OficioResponse mapOficio(Oficio oficio) {
    List<OficioTramiteResponse> tramites = new ArrayList<>();
    for (OficioTramite tramite : tramiteRepository.listarPorOficio(oficio.getId())) {
      tramites.add(
          new OficioTramiteResponse(
              tramite.getId(),
              tramite.getData(),
              tramite.getOrigem(),
              tramite.getDestino(),
              tramite.getResponsavel(),
              tramite.getAcao(),
              tramite.getObservacoes()));
    }
    OficioIdentificacaoResponse identificacao =
        new OficioIdentificacaoResponse(
            oficio.getId(),
            oficio.getTipo(),
            oficio.getNumero(),
            oficio.getData(),
            oficio.getSetorOrigem(),
            oficio.getResponsavel(),
            oficio.getDestinatario(),
            oficio.getMeioEnvio(),
            oficio.getPrazoResposta(),
            oficio.getClassificacao());
    OficioConteudoResponse conteudo =
        new OficioConteudoResponse(
            oficio.getRazaoSocial(),
            oficio.getLogoUrl(),
            oficio.getTitulo(),
            oficio.getSaudacao(),
            oficio.getAssunto(),
            oficio.getCorpo(),
            oficio.getFinalizacao(),
            oficio.getAssinaturaNome(),
            oficio.getAssinaturaCargo(),
            oficio.getRodape());
    OficioProtocoloResponse protocolo =
        new OficioProtocoloResponse(
            oficio.getStatus(),
            oficio.getProtocoloEnvio(),
            oficio.getDataEnvio(),
            oficio.getProtocoloRecebimento(),
            oficio.getDataRecebimento(),
            oficio.getProximoDestino(),
            oficio.getObservacoes());
    return new OficioResponse(oficio.getId(), identificacao, conteudo, protocolo, tramites);
  }
}
