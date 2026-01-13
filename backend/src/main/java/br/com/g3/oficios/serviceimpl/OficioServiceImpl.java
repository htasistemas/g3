package br.com.g3.oficios.serviceimpl;

import br.com.g3.oficios.domain.Oficio;
import br.com.g3.oficios.domain.OficioImagem;
import br.com.g3.oficios.domain.OficioTramite;
import br.com.g3.oficios.dto.OficioConteudoRequest;
import br.com.g3.oficios.dto.OficioConteudoResponse;
import br.com.g3.oficios.dto.OficioIdentificacaoRequest;
import br.com.g3.oficios.dto.OficioIdentificacaoResponse;
import br.com.g3.oficios.dto.OficioImagemRequest;
import br.com.g3.oficios.dto.OficioImagemResponse;
import br.com.g3.oficios.dto.OficioListaResponse;
import br.com.g3.oficios.dto.OficioPdfAssinadoRequest;
import br.com.g3.oficios.dto.OficioProtocoloRequest;
import br.com.g3.oficios.dto.OficioProtocoloResponse;
import br.com.g3.oficios.dto.OficioRequest;
import br.com.g3.oficios.dto.OficioResponse;
import br.com.g3.oficios.dto.OficioTramiteRequest;
import br.com.g3.oficios.dto.OficioTramiteResponse;
import br.com.g3.oficios.repository.OficioImagemRepository;
import br.com.g3.oficios.repository.OficioRepository;
import br.com.g3.oficios.repository.OficioTramiteRepository;
import br.com.g3.oficios.service.OficioService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
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
  private final OficioImagemRepository imagemRepository;

  public OficioServiceImpl(
      OficioRepository repository,
      OficioTramiteRepository tramiteRepository,
      OficioImagemRepository imagemRepository) {
    this.repository = repository;
    this.tramiteRepository = tramiteRepository;
    this.imagemRepository = imagemRepository;
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
    aplicarNumeroAutomaticoSeNecessario(request);
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
    aplicarNumeroExistenteSeAusente(oficio, request);
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
    imagemRepository.removerPorOficioId(oficio.getId());
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
    String destinatario = identificacao.getDestinatario();
    if (destinatario == null || destinatario.isBlank()) {
      destinatario = conteudo.getRazaoSocial();
    }
    if (destinatario == null || destinatario.isBlank()) {
      destinatario = conteudo.getPara();
    }
    oficio.setDestinatario(destinatario == null ? "" : destinatario);
    oficio.setMeioEnvio(identificacao.getMeioEnvio());
    oficio.setPrazoResposta(identificacao.getPrazoResposta());
    oficio.setClassificacao(identificacao.getClassificacao());

    oficio.setRazaoSocial(conteudo.getRazaoSocial());
    oficio.setLogoUrl(conteudo.getLogoUrl());
    oficio.setTitulo(conteudo.getTitulo());
    oficio.setSaudacao(conteudo.getSaudacao());
    oficio.setPara(conteudo.getPara());
    oficio.setCargoPara(conteudo.getCargoPara());
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
            oficio.getPara(),
            oficio.getCargoPara(),
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
    String pdfAssinadoUrl =
        oficio.getPdfAssinadoConteudo() == null || oficio.getPdfAssinadoConteudo().isBlank()
            ? null
            : "/api/oficios/" + oficio.getId() + "/pdf-assinado";
    return new OficioResponse(
        oficio.getId(),
        identificacao,
        conteudo,
        protocolo,
        tramites,
        oficio.getPdfAssinadoNome(),
        pdfAssinadoUrl);
  }

  @Override
  @Transactional
  public OficioResponse salvarPdfAssinado(Long id, OficioPdfAssinadoRequest request) {
    Oficio oficio = buscarOficio(id);
    if (request == null
        || request.getConteudoBase64() == null
        || request.getConteudoBase64().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Envie o PDF assinado.");
    }
    String tipo = request.getTipoMime() == null ? "" : request.getTipoMime().toLowerCase();
    String nome = request.getNomeArquivo() == null ? "" : request.getNomeArquivo().toLowerCase();
    if (!tipo.contains("pdf") && !nome.endsWith(".pdf")) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Envie apenas arquivo PDF.");
    }
    oficio.setPdfAssinadoNome(request.getNomeArquivo());
    oficio.setPdfAssinadoTipo(request.getTipoMime());
    oficio.setPdfAssinadoConteudo(request.getConteudoBase64());
    oficio.setAtualizadoEm(LocalDateTime.now());
    repository.salvar(oficio);
    return mapOficio(oficio);
  }

  @Override
  @Transactional
  public void removerPdfAssinado(Long id) {
    Oficio oficio = buscarOficio(id);
    oficio.setPdfAssinadoNome(null);
    oficio.setPdfAssinadoTipo(null);
    oficio.setPdfAssinadoConteudo(null);
    oficio.setAtualizadoEm(LocalDateTime.now());
    repository.salvar(oficio);
  }

  @Override
  public byte[] obterPdfAssinado(Long id) {
    Oficio oficio = buscarOficio(id);
    if (oficio.getPdfAssinadoConteudo() == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PDF assinado nao encontrado.");
    }
    return Base64.getDecoder().decode(oficio.getPdfAssinadoConteudo());
  }

  @Override
  public String obterPdfAssinadoTipo(Long id) {
    Oficio oficio = buscarOficio(id);
    if (oficio.getPdfAssinadoTipo() == null || oficio.getPdfAssinadoTipo().isBlank()) {
      return "application/pdf";
    }
    return oficio.getPdfAssinadoTipo();
  }

  @Override
  public String obterPdfAssinadoNome(Long id) {
    Oficio oficio = buscarOficio(id);
    if (oficio.getPdfAssinadoNome() == null || oficio.getPdfAssinadoNome().isBlank()) {
      return "oficio-assinado.pdf";
    }
    return oficio.getPdfAssinadoNome();
  }

  @Override
  public List<OficioImagemResponse> listarImagens(Long oficioId) {
    Oficio oficio = buscarOficio(oficioId);
    List<OficioImagem> imagens = imagemRepository.listarPorOficioId(oficio.getId());
    List<OficioImagemResponse> resposta = new ArrayList<>();
    for (OficioImagem imagem : imagens) {
      resposta.add(mapImagem(imagem));
    }
    return resposta;
  }

  @Override
  @Transactional
  public OficioImagemResponse adicionarImagem(Long oficioId, OficioImagemRequest request) {
    Oficio oficio = buscarOficio(oficioId);
    validarImagem(request, oficio.getId());
    LocalDateTime agora = LocalDateTime.now();
    OficioImagem imagem = new OficioImagem();
    imagem.setOficioId(oficio.getId());
    imagem.setNomeArquivo(request.getNomeArquivo());
    imagem.setTipoMime(request.getTipoMime());
    imagem.setConteudoBase64(request.getConteudoBase64());
    imagem.setOrdem(request.getOrdem());
    imagem.setCriadoEm(agora);
    imagem.setAtualizadoEm(agora);
    OficioImagem salva = imagemRepository.salvar(imagem);
    return mapImagem(salva);
  }

  @Override
  @Transactional
  public void removerImagem(Long oficioId, Long imagemId) {
    Oficio oficio = buscarOficio(oficioId);
    List<OficioImagem> imagens = imagemRepository.listarPorOficioId(oficio.getId());
    boolean pertence = imagens.stream().anyMatch(imagem -> imagem.getId().equals(imagemId));
    if (!pertence) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagem nao encontrada.");
    }
    imagemRepository.removerPorId(imagemId);
  }

  private void aplicarNumeroAutomaticoSeNecessario(OficioRequest request) {
    if (request == null || request.getIdentificacao() == null) {
      return;
    }
    String numeroAtual = request.getIdentificacao().getNumero();
    if (numeroAtual != null && !numeroAtual.trim().isEmpty()) {
      return;
    }
    LocalDate data = request.getIdentificacao().getData();
    int ano = data != null ? data.getYear() : LocalDate.now().getYear();
    request.getIdentificacao().setNumero(gerarNumeroPorAno(ano));
  }

  private void aplicarNumeroExistenteSeAusente(Oficio oficio, OficioRequest request) {
    if (request == null || request.getIdentificacao() == null) {
      return;
    }
    String numeroAtual = request.getIdentificacao().getNumero();
    if (numeroAtual != null && !numeroAtual.trim().isEmpty()) {
      return;
    }
    request.getIdentificacao().setNumero(oficio.getNumero());
  }

  private String gerarNumeroPorAno(int ano) {
    String ultimoNumero = repository.buscarUltimoNumeroPorAno(ano);
    int proximo = 1;
    if (ultimoNumero != null && ultimoNumero.contains("/")) {
      String parteNumerica = ultimoNumero.split("/")[0];
      try {
        proximo = Integer.parseInt(parteNumerica) + 1;
      } catch (NumberFormatException ignored) {
        proximo = 1;
      }
    }
    return String.format("%04d/%d", proximo, ano);
  }

  private void validarImagem(OficioImagemRequest request, Long oficioId) {
    List<OficioImagem> existentes = imagemRepository.listarPorOficioId(oficioId);
    if (existentes.size() >= 10) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Limite maximo de 10 imagens por oficio.");
    }
    String tipo = request.getTipoMime() == null ? "" : request.getTipoMime().toLowerCase();
    if (!tipo.equals("image/jpeg") && !tipo.equals("image/png")) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Tipo de imagem invalido. Use JPG ou PNG.");
    }
    byte[] bytes;
    try {
      bytes = Base64.getDecoder().decode(request.getConteudoBase64());
    } catch (IllegalArgumentException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conteudo da imagem invalido.");
    }
    if (bytes.length > 10 * 1024 * 1024) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Imagem excede o limite de 10MB.");
    }
  }

  private OficioImagemResponse mapImagem(OficioImagem imagem) {
    OficioImagemResponse response = new OficioImagemResponse();
    response.setId(imagem.getId());
    response.setOficioId(imagem.getOficioId());
    response.setNomeArquivo(imagem.getNomeArquivo());
    response.setTipoMime(imagem.getTipoMime());
    response.setConteudoBase64(imagem.getConteudoBase64());
    response.setOrdem(imagem.getOrdem());
    response.setCriadoEm(imagem.getCriadoEm());
    response.setAtualizadoEm(imagem.getAtualizadoEm());
    return response;
  }
}
