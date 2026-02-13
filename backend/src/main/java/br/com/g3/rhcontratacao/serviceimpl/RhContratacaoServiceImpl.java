package br.com.g3.rhcontratacao.serviceimpl;

import br.com.g3.rhcontratacao.domain.RhArquivo;
import br.com.g3.rhcontratacao.domain.RhAuditoriaContratacao;
import br.com.g3.rhcontratacao.domain.RhCartaBanco;
import br.com.g3.rhcontratacao.domain.RhCandidato;
import br.com.g3.rhcontratacao.domain.RhDocumentoItem;
import br.com.g3.rhcontratacao.domain.RhEntrevista;
import br.com.g3.rhcontratacao.domain.RhFichaAdmissao;
import br.com.g3.rhcontratacao.domain.RhPpd;
import br.com.g3.rhcontratacao.domain.RhProcessoContratacao;
import br.com.g3.rhcontratacao.domain.RhTermo;
import br.com.g3.rhcontratacao.dto.RhAtualizarStatusRequest;
import br.com.g3.rhcontratacao.dto.RhArquivoRequest;
import br.com.g3.rhcontratacao.dto.RhArquivoResponse;
import br.com.g3.rhcontratacao.dto.RhAuditoriaContratacaoResponse;
import br.com.g3.rhcontratacao.dto.RhCartaBancoRequest;
import br.com.g3.rhcontratacao.dto.RhCartaBancoResponse;
import br.com.g3.rhcontratacao.dto.RhCandidatoRequest;
import br.com.g3.rhcontratacao.dto.RhCandidatoResponse;
import br.com.g3.rhcontratacao.dto.RhCandidatoResumoResponse;
import br.com.g3.rhcontratacao.dto.RhDocumentoItemRequest;
import br.com.g3.rhcontratacao.dto.RhDocumentoItemResponse;
import br.com.g3.rhcontratacao.dto.RhEntrevistaRequest;
import br.com.g3.rhcontratacao.dto.RhEntrevistaResponse;
import br.com.g3.rhcontratacao.dto.RhFichaAdmissaoRequest;
import br.com.g3.rhcontratacao.dto.RhFichaAdmissaoResponse;
import br.com.g3.rhcontratacao.dto.RhPpdRequest;
import br.com.g3.rhcontratacao.dto.RhPpdResponse;
import br.com.g3.rhcontratacao.dto.RhProcessoContratacaoResponse;
import br.com.g3.rhcontratacao.dto.RhTermoRequest;
import br.com.g3.rhcontratacao.dto.RhTermoResponse;
import br.com.g3.rhcontratacao.repository.RhArquivoRepository;
import br.com.g3.rhcontratacao.repository.RhAuditoriaContratacaoRepository;
import br.com.g3.rhcontratacao.repository.RhCartaBancoRepository;
import br.com.g3.rhcontratacao.repository.RhCandidatoRepository;
import br.com.g3.rhcontratacao.repository.RhDocumentoItemRepository;
import br.com.g3.rhcontratacao.repository.RhEntrevistaRepository;
import br.com.g3.rhcontratacao.repository.RhFichaAdmissaoRepository;
import br.com.g3.rhcontratacao.repository.RhPpdRepository;
import br.com.g3.rhcontratacao.repository.RhProcessoContratacaoRepository;
import br.com.g3.rhcontratacao.repository.RhTermoRepository;
import br.com.g3.rhcontratacao.service.ArmazenamentoRhArquivoService;
import br.com.g3.rhcontratacao.service.RhContratacaoService;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RhContratacaoServiceImpl implements RhContratacaoService {
  private static final String STATUS_CADASTRADO = "Cadastrado";
  private static final String STATUS_EM_ENTREVISTA = "Em entrevista";
  private static final String STATUS_APROVADO = "Aprovado";
  private static final String STATUS_DOC_PENDENTE = "Documentos pendentes";
  private static final String STATUS_DOC_OK = "Documentos ok";
  private static final String STATUS_ADMITIDO = "Admitido";

  private final RhCandidatoRepository candidatoRepository;
  private final RhProcessoContratacaoRepository processoRepository;
  private final RhEntrevistaRepository entrevistaRepository;
  private final RhFichaAdmissaoRepository fichaRepository;
  private final RhDocumentoItemRepository documentoRepository;
  private final RhArquivoRepository arquivoRepository;
  private final RhTermoRepository termoRepository;
  private final RhPpdRepository ppdRepository;
  private final RhCartaBancoRepository cartaBancoRepository;
  private final RhAuditoriaContratacaoRepository auditoriaRepository;
  private final ArmazenamentoRhArquivoService armazenamentoService;

  public RhContratacaoServiceImpl(
      RhCandidatoRepository candidatoRepository,
      RhProcessoContratacaoRepository processoRepository,
      RhEntrevistaRepository entrevistaRepository,
      RhFichaAdmissaoRepository fichaRepository,
      RhDocumentoItemRepository documentoRepository,
      RhArquivoRepository arquivoRepository,
      RhTermoRepository termoRepository,
      RhPpdRepository ppdRepository,
      RhCartaBancoRepository cartaBancoRepository,
      RhAuditoriaContratacaoRepository auditoriaRepository,
      ArmazenamentoRhArquivoService armazenamentoService) {
    this.candidatoRepository = candidatoRepository;
    this.processoRepository = processoRepository;
    this.entrevistaRepository = entrevistaRepository;
    this.fichaRepository = fichaRepository;
    this.documentoRepository = documentoRepository;
    this.arquivoRepository = arquivoRepository;
    this.termoRepository = termoRepository;
    this.ppdRepository = ppdRepository;
    this.cartaBancoRepository = cartaBancoRepository;
    this.auditoriaRepository = auditoriaRepository;
    this.armazenamentoService = armazenamentoService;
  }

  @Override
  public List<RhCandidatoResumoResponse> listarCandidatos(String termo) {
    String termoBusca = normalizar(termo);
    List<RhCandidatoResumoResponse> resposta = new ArrayList<>();
    for (RhCandidato candidato : candidatoRepository.listar()) {
      RhProcessoContratacao processo = processoRepository.buscarPorCandidatoId(candidato.getId()).orElse(null);
      if (!StringUtils.hasText(termoBusca) || correspondeTermo(candidato, processo, termoBusca)) {
        resposta.add(mapResumo(candidato, processo));
      }
    }
    return resposta;
  }

  @Override
  public RhCandidatoResponse buscarCandidato(Long candidatoId) {
    RhCandidato candidato = obterCandidato(candidatoId);
    return mapCandidato(candidato);
  }

  @Override
  public RhProcessoContratacaoResponse buscarProcessoPorCandidato(Long candidatoId) {
    RhProcessoContratacao processo = processoRepository
        .buscarPorCandidatoId(candidatoId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Processo nao encontrado."));
    return mapProcesso(processo);
  }

  @Override
  @Transactional
  public RhCandidatoResponse criarCandidato(RhCandidatoRequest request, Long usuarioId) {
    validarCandidato(request);
    RhCandidato candidato = new RhCandidato();
    aplicarCandidato(candidato, request);
    candidato.setAtivo(Boolean.TRUE);
    LocalDateTime agora = LocalDateTime.now();
    candidato.setCriadoEm(agora);
    candidato.setAtualizadoEm(agora);
    RhCandidato salvo = candidatoRepository.salvar(candidato);

    RhProcessoContratacao processo = new RhProcessoContratacao();
    processo.setCandidatoId(salvo.getId());
    processo.setStatus(STATUS_CADASTRADO);
    processo.setResponsavelId(usuarioId);
    processo.setCriadoEm(agora);
    processo.setAtualizadoEm(agora);
    processo.setUltimaMovimentacaoEm(agora);
    processoRepository.salvar(processo);

    registrarAuditoria(processo.getId(), usuarioId, "Cadastro", "Candidato criado.");
    return mapCandidato(salvo);
  }

  @Override
  @Transactional
  public RhCandidatoResponse atualizarCandidato(Long candidatoId, RhCandidatoRequest request, Long usuarioId) {
    RhCandidato candidato = obterCandidato(candidatoId);
    validarCandidato(request);
    aplicarCandidato(candidato, request);
    candidato.setAtualizadoEm(LocalDateTime.now());
    RhCandidato salvo = candidatoRepository.salvar(candidato);
    RhProcessoContratacao processo = processoRepository.buscarPorCandidatoId(candidatoId).orElse(null);
    if (processo != null) {
      processo.setAtualizadoEm(LocalDateTime.now());
      processoRepository.salvar(processo);
      registrarAuditoria(processo.getId(), usuarioId, "Atualizacao", "Dados do candidato atualizados.");
    }
    return mapCandidato(salvo);
  }

  @Override
  @Transactional
  public void inativarCandidato(Long candidatoId, Long usuarioId) {
    RhCandidato candidato = obterCandidato(candidatoId);
    candidato.setAtivo(Boolean.FALSE);
    candidato.setAtualizadoEm(LocalDateTime.now());
    candidatoRepository.salvar(candidato);
    RhProcessoContratacao processo = processoRepository.buscarPorCandidatoId(candidatoId).orElse(null);
    if (processo != null) {
      registrarAuditoria(processo.getId(), usuarioId, "Inativacao", "Candidato inativado.");
    }
  }

  @Override
  @Transactional
  public RhProcessoContratacaoResponse atualizarStatus(
      Long processoId, RhAtualizarStatusRequest request, Long usuarioId) {
    RhProcessoContratacao processo = obterProcesso(processoId);
    String novoStatus = validarStatus(request != null ? request.getStatus() : null);
    if (STATUS_DOC_OK.equals(novoStatus) && !documentosAprovados(processoId)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Documentos obrigatorios pendentes.");
    }
    processo.setStatus(novoStatus);
    processo.setAtualizadoEm(LocalDateTime.now());
    processo.setUltimaMovimentacaoEm(LocalDateTime.now());
    RhProcessoContratacao salvo = processoRepository.salvar(processo);
    registrarAuditoria(processoId, usuarioId, "Status", "Status alterado para " + novoStatus + ".");
    return mapProcesso(salvo);
  }

  @Override
  public List<RhEntrevistaResponse> listarEntrevistas(Long processoId) {
    List<RhEntrevistaResponse> resposta = new ArrayList<>();
    for (RhEntrevista entrevista : entrevistaRepository.listarPorProcesso(processoId)) {
      resposta.add(mapEntrevista(entrevista));
    }
    return resposta;
  }

  @Override
  @Transactional
  public RhEntrevistaResponse salvarEntrevista(Long processoId, RhEntrevistaRequest request, Long usuarioId) {
    if (request == null || !StringUtils.hasText(request.getTipoRoteiro())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o roteiro da entrevista.");
    }
    RhEntrevista entrevista = new RhEntrevista();
    entrevista.setProcessoId(processoId);
    entrevista.setTipoRoteiro(request.getTipoRoteiro());
    entrevista.setPerguntasJson(request.getPerguntasJson());
    entrevista.setRespostasJson(request.getRespostasJson());
    entrevista.setParecer(request.getParecer());
    entrevista.setObservacoes(request.getObservacoes());
    entrevista.setDataEntrevista(request.getDataEntrevista());
    entrevista.setCriadoPor(usuarioId);
    entrevista.setCriadoEm(LocalDateTime.now());
    RhEntrevista salvo = entrevistaRepository.salvar(entrevista);
    registrarAuditoria(processoId, usuarioId, "Entrevista", "Entrevista registrada.");
    return mapEntrevista(salvo);
  }

  @Override
  public RhFichaAdmissaoResponse buscarFichaAdmissao(Long processoId) {
    return fichaRepository.buscarPorProcesso(processoId)
        .map(this::mapFicha)
        .orElse(null);
  }

  @Override
  @Transactional
  public RhFichaAdmissaoResponse salvarFichaAdmissao(Long processoId, RhFichaAdmissaoRequest request, Long usuarioId) {
    RhFichaAdmissao ficha = fichaRepository.buscarPorProcesso(processoId).orElse(new RhFichaAdmissao());
    ficha.setProcessoId(processoId);
    ficha.setDadosPessoaisJson(request != null ? request.getDadosPessoaisJson() : null);
    ficha.setDependentesJson(request != null ? request.getDependentesJson() : null);
    ficha.setDadosInternosJson(request != null ? request.getDadosInternosJson() : null);
    LocalDateTime agora = LocalDateTime.now();
    if (ficha.getId() == null) {
      ficha.setCriadoEm(agora);
    }
    ficha.setAtualizadoEm(agora);
    RhFichaAdmissao salvo = fichaRepository.salvar(ficha);
    registrarAuditoria(processoId, usuarioId, "Admissao", "Ficha de admissao atualizada.");
    return mapFicha(salvo);
  }

  @Override
  public List<RhDocumentoItemResponse> listarDocumentos(Long processoId) {
    garantirChecklist(processoId);
    List<RhDocumentoItemResponse> resposta = new ArrayList<>();
    for (RhDocumentoItem item : documentoRepository.listarPorProcesso(processoId)) {
      resposta.add(mapDocumento(item));
    }
    return resposta;
  }

  @Override
  @Transactional
  public RhDocumentoItemResponse atualizarDocumentoItem(Long itemId, RhDocumentoItemRequest request, Long usuarioId) {
    RhDocumentoItem item = documentoRepository
        .buscarPorId(itemId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento nao encontrado."));
    if (request != null) {
      if (StringUtils.hasText(request.getStatus())) {
        item.setStatus(request.getStatus());
      }
      if (request.getObservacao() != null) {
        item.setObservacao(request.getObservacao());
      }
    }
    item.setAtualizadoPor(usuarioId);
    item.setAtualizadoEm(LocalDateTime.now());
    RhDocumentoItem salvo = documentoRepository.salvar(item);
    registrarAuditoria(item.getProcessoId(), usuarioId, "Documento", "Documento atualizado: " + item.getTipoDocumento());
    return mapDocumento(salvo);
  }

  @Override
  public List<RhArquivoResponse> listarArquivos(Long processoId) {
    List<RhArquivoResponse> resposta = new ArrayList<>();
    for (RhArquivo arquivo : arquivoRepository.listarPorProcesso(processoId)) {
      resposta.add(mapArquivo(arquivo));
    }
    return resposta;
  }

  @Override
  @Transactional
  public RhArquivoResponse adicionarArquivo(Long processoId, RhArquivoRequest request, Long usuarioId) {
    if (request == null || !StringUtils.hasText(request.getCategoria())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe a categoria do arquivo.");
    }
    String caminho = armazenamentoService.salvarArquivo(processoId, request);
    int versao = 1;
    for (RhArquivo existente : arquivoRepository.listarPorProcesso(processoId)) {
      if (request.getCategoria().equalsIgnoreCase(existente.getCategoria())
          && equalText(request.getTipoDocumento(), existente.getTipoDocumento())) {
        versao = Math.max(versao, existente.getVersao() + 1);
      }
    }
    RhArquivo arquivo = new RhArquivo();
    arquivo.setProcessoId(processoId);
    arquivo.setCategoria(request.getCategoria());
    arquivo.setTipoDocumento(request.getTipoDocumento());
    arquivo.setNomeArquivo(request.getNomeArquivo());
    arquivo.setMimeType(request.getMimeType());
    arquivo.setTamanhoBytes(request.getTamanhoBytes() != null ? request.getTamanhoBytes() : 0L);
    arquivo.setCaminhoArquivo(caminho);
    arquivo.setVersao(versao);
    arquivo.setCriadoPor(usuarioId);
    arquivo.setCriadoEm(LocalDateTime.now());
    RhArquivo salvo = arquivoRepository.salvar(arquivo);
    registrarAuditoria(processoId, usuarioId, "Arquivo", "Arquivo enviado: " + arquivo.getNomeArquivo());
    return mapArquivo(salvo);
  }

  @Override
  public Resource obterArquivo(Long arquivoId) {
    RhArquivo arquivo = arquivoRepository
        .buscarPorId(arquivoId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo nao encontrado."));
    if (!StringUtils.hasText(arquivo.getCaminhoArquivo())) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo nao encontrado.");
    }
    try {
      Path caminho = Paths.get(arquivo.getCaminhoArquivo());
      Resource resource = new UrlResource(caminho.toUri());
      if (!resource.exists()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo nao encontrado.");
      }
      return resource;
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo nao encontrado.");
    }
  }

  @Override
  public List<RhTermoResponse> listarTermos(Long processoId) {
    List<RhTermoResponse> resposta = new ArrayList<>();
    for (RhTermo termo : termoRepository.listarPorProcesso(processoId)) {
      resposta.add(mapTermo(termo));
    }
    return resposta;
  }

  @Override
  @Transactional
  public RhTermoResponse salvarTermo(Long processoId, RhTermoRequest request, Long usuarioId) {
    if (request == null || !StringUtils.hasText(request.getTipo())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo do termo e obrigatorio.");
    }
    RhTermo termo = termoRepository
        .buscarPorProcessoETipo(processoId, request.getTipo())
        .orElse(new RhTermo());
    termo.setProcessoId(processoId);
    termo.setTipo(request.getTipo());
    termo.setDadosJson(request.getDadosJson());
    termo.setStatusAssinatura(request.getStatusAssinatura());
    termo.setDataAssinatura(request.getDataAssinatura());
    termo.setResponsavel(request.getResponsavel());
    LocalDateTime agora = LocalDateTime.now();
    if (termo.getId() == null) {
      termo.setCriadoEm(agora);
    }
    termo.setAtualizadoEm(agora);
    RhTermo salvo = termoRepository.salvar(termo);
    registrarAuditoria(processoId, usuarioId, "Termo", "Termo atualizado: " + request.getTipo());
    return mapTermo(salvo);
  }

  @Override
  public RhPpdResponse buscarPpd(Long processoId) {
    return ppdRepository.buscarPorProcesso(processoId).map(this::mapPpd).orElse(null);
  }

  @Override
  @Transactional
  public RhPpdResponse salvarPpd(Long processoId, RhPpdRequest request, Long usuarioId) {
    RhPpd ppd = ppdRepository.buscarPorProcesso(processoId).orElse(new RhPpd());
    ppd.setProcessoId(processoId);
    ppd.setCabecalhoJson(request != null ? request.getCabecalhoJson() : null);
    ppd.setLadoAJson(request != null ? request.getLadoAJson() : null);
    ppd.setLadoBJson(request != null ? request.getLadoBJson() : null);
    LocalDateTime agora = LocalDateTime.now();
    if (ppd.getId() == null) {
      ppd.setCriadoEm(agora);
    }
    ppd.setAtualizadoEm(agora);
    RhPpd salvo = ppdRepository.salvar(ppd);
    registrarAuditoria(processoId, usuarioId, "PPD", "PPD atualizado.");
    return mapPpd(salvo);
  }

  @Override
  public RhCartaBancoResponse buscarCartaBanco(Long processoId) {
    return cartaBancoRepository.buscarPorProcesso(processoId).map(this::mapCarta).orElse(null);
  }

  @Override
  @Transactional
  public RhCartaBancoResponse salvarCartaBanco(Long processoId, RhCartaBancoRequest request, Long usuarioId) {
    RhCartaBanco carta = cartaBancoRepository.buscarPorProcesso(processoId).orElse(new RhCartaBanco());
    carta.setProcessoId(processoId);
    carta.setDadosJson(request != null ? request.getDadosJson() : null);
    LocalDateTime agora = LocalDateTime.now();
    if (carta.getId() == null) {
      carta.setCriadoEm(agora);
    }
    carta.setAtualizadoEm(agora);
    RhCartaBanco salvo = cartaBancoRepository.salvar(carta);
    registrarAuditoria(processoId, usuarioId, "Carta", "Carta de abertura de conta atualizada.");
    return mapCarta(salvo);
  }

  @Override
  public List<RhAuditoriaContratacaoResponse> listarAuditoria(Long processoId) {
    List<RhAuditoriaContratacaoResponse> resposta = new ArrayList<>();
    for (RhAuditoriaContratacao auditoria : auditoriaRepository.listarPorProcesso(processoId)) {
      RhAuditoriaContratacaoResponse dto = new RhAuditoriaContratacaoResponse();
      dto.setId(auditoria.getId());
      dto.setProcessoId(auditoria.getProcessoId());
      dto.setAtorId(auditoria.getAtorId());
      dto.setAcao(auditoria.getAcao());
      dto.setDetalhes(auditoria.getDetalhes());
      dto.setCriadoEm(auditoria.getCriadoEm());
      resposta.add(dto);
    }
    return resposta;
  }

  private void validarCandidato(RhCandidatoRequest request) {
    if (request == null || !StringUtils.hasText(request.getNomeCompleto())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome do candidato e obrigatorio.");
    }
  }

  private RhCandidato obterCandidato(Long candidatoId) {
    return candidatoRepository
        .buscarPorId(candidatoId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidato nao encontrado."));
  }

  private RhProcessoContratacao obterProcesso(Long processoId) {
    return processoRepository
        .buscarPorId(processoId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Processo nao encontrado."));
  }

  private void aplicarCandidato(RhCandidato candidato, RhCandidatoRequest request) {
    candidato.setNomeCompleto(request.getNomeCompleto());
    candidato.setCpf(request.getCpf());
    candidato.setRg(request.getRg());
    candidato.setPis(request.getPis());
    candidato.setDataNascimento(request.getDataNascimento());
    candidato.setNaturalidade(request.getNaturalidade());
    candidato.setEstadoCivil(request.getEstadoCivil());
    candidato.setNomeMae(request.getNomeMae());
    candidato.setNomeConjuge(request.getNomeConjuge());
    candidato.setVagaPretendida(request.getVagaPretendida());
    candidato.setDataPreenchimento(request.getDataPreenchimento());
    candidato.setFilhosPossui(Boolean.TRUE.equals(request.getFilhosPossui()));
    candidato.setFilhosJson(request.getFilhosJson());
    candidato.setDeficienciaPossui(Boolean.TRUE.equals(request.getDeficienciaPossui()));
    candidato.setDeficienciaTipo(request.getDeficienciaTipo());
    candidato.setDeficienciaDescricao(request.getDeficienciaDescricao());
    candidato.setEnderecoJson(request.getEnderecoJson());
    candidato.setTelefone(request.getTelefone());
    candidato.setWhatsapp(request.getWhatsapp());
    candidato.setAnexosJson(request.getAnexosJson());
    if (request.getAtivo() != null) {
      candidato.setAtivo(request.getAtivo());
    }
  }

  private RhCandidatoResponse mapCandidato(RhCandidato candidato) {
    RhCandidatoResponse response = new RhCandidatoResponse();
    response.setId(candidato.getId());
    response.setNomeCompleto(candidato.getNomeCompleto());
    response.setCpf(candidato.getCpf());
    response.setRg(candidato.getRg());
    response.setPis(candidato.getPis());
    response.setDataNascimento(candidato.getDataNascimento());
    response.setNaturalidade(candidato.getNaturalidade());
    response.setEstadoCivil(candidato.getEstadoCivil());
    response.setNomeMae(candidato.getNomeMae());
    response.setNomeConjuge(candidato.getNomeConjuge());
    response.setVagaPretendida(candidato.getVagaPretendida());
    response.setDataPreenchimento(candidato.getDataPreenchimento());
    response.setFilhosPossui(candidato.getFilhosPossui());
    response.setFilhosJson(candidato.getFilhosJson());
    response.setDeficienciaPossui(candidato.getDeficienciaPossui());
    response.setDeficienciaTipo(candidato.getDeficienciaTipo());
    response.setDeficienciaDescricao(candidato.getDeficienciaDescricao());
    response.setEnderecoJson(candidato.getEnderecoJson());
    response.setTelefone(candidato.getTelefone());
    response.setWhatsapp(candidato.getWhatsapp());
    response.setAnexosJson(candidato.getAnexosJson());
    response.setAtivo(candidato.getAtivo());
    response.setCriadoEm(candidato.getCriadoEm());
    response.setAtualizadoEm(candidato.getAtualizadoEm());
    return response;
  }

  private RhProcessoContratacaoResponse mapProcesso(RhProcessoContratacao processo) {
    RhProcessoContratacaoResponse response = new RhProcessoContratacaoResponse();
    response.setId(processo.getId());
    response.setCandidatoId(processo.getCandidatoId());
    response.setStatus(processo.getStatus());
    response.setResponsavelId(processo.getResponsavelId());
    response.setGestorId(processo.getGestorId());
    response.setCriadoEm(processo.getCriadoEm());
    response.setAtualizadoEm(processo.getAtualizadoEm());
    response.setUltimaMovimentacaoEm(processo.getUltimaMovimentacaoEm());
    return response;
  }

  private RhCandidatoResumoResponse mapResumo(RhCandidato candidato, RhProcessoContratacao processo) {
    RhCandidatoResumoResponse response = new RhCandidatoResumoResponse();
    response.setCandidatoId(candidato.getId());
    response.setProcessoId(processo != null ? processo.getId() : null);
    response.setNomeCompleto(candidato.getNomeCompleto());
    response.setCpf(candidato.getCpf());
    response.setVagaPretendida(candidato.getVagaPretendida());
    response.setStatus(processo != null ? processo.getStatus() : null);
    response.setAtivo(candidato.getAtivo());
    return response;
  }

  private RhEntrevistaResponse mapEntrevista(RhEntrevista entrevista) {
    RhEntrevistaResponse response = new RhEntrevistaResponse();
    response.setId(entrevista.getId());
    response.setProcessoId(entrevista.getProcessoId());
    response.setTipoRoteiro(entrevista.getTipoRoteiro());
    response.setPerguntasJson(entrevista.getPerguntasJson());
    response.setRespostasJson(entrevista.getRespostasJson());
    response.setParecer(entrevista.getParecer());
    response.setObservacoes(entrevista.getObservacoes());
    response.setDataEntrevista(entrevista.getDataEntrevista());
    response.setCriadoPor(entrevista.getCriadoPor());
    response.setCriadoEm(entrevista.getCriadoEm());
    return response;
  }

  private RhFichaAdmissaoResponse mapFicha(RhFichaAdmissao ficha) {
    RhFichaAdmissaoResponse response = new RhFichaAdmissaoResponse();
    response.setId(ficha.getId());
    response.setProcessoId(ficha.getProcessoId());
    response.setDadosPessoaisJson(ficha.getDadosPessoaisJson());
    response.setDependentesJson(ficha.getDependentesJson());
    response.setDadosInternosJson(ficha.getDadosInternosJson());
    response.setCriadoEm(ficha.getCriadoEm());
    response.setAtualizadoEm(ficha.getAtualizadoEm());
    return response;
  }

  private RhDocumentoItemResponse mapDocumento(RhDocumentoItem item) {
    RhDocumentoItemResponse response = new RhDocumentoItemResponse();
    response.setId(item.getId());
    response.setProcessoId(item.getProcessoId());
    response.setTipoDocumento(item.getTipoDocumento());
    response.setObrigatorio(item.getObrigatorio());
    response.setStatus(item.getStatus());
    response.setObservacao(item.getObservacao());
    response.setAtualizadoPor(item.getAtualizadoPor());
    response.setCriadoEm(item.getCriadoEm());
    response.setAtualizadoEm(item.getAtualizadoEm());
    return response;
  }

  private RhArquivoResponse mapArquivo(RhArquivo arquivo) {
    RhArquivoResponse response = new RhArquivoResponse();
    response.setId(arquivo.getId());
    response.setProcessoId(arquivo.getProcessoId());
    response.setCategoria(arquivo.getCategoria());
    response.setTipoDocumento(arquivo.getTipoDocumento());
    response.setNomeArquivo(arquivo.getNomeArquivo());
    response.setMimeType(arquivo.getMimeType());
    response.setTamanhoBytes(arquivo.getTamanhoBytes());
    response.setVersao(arquivo.getVersao());
    response.setArquivoUrl(
        "/api/rh/contratacao/arquivos/" + arquivo.getId() + "/download");
    response.setCriadoPor(arquivo.getCriadoPor());
    response.setCriadoEm(arquivo.getCriadoEm());
    return response;
  }

  private RhTermoResponse mapTermo(RhTermo termo) {
    RhTermoResponse response = new RhTermoResponse();
    response.setId(termo.getId());
    response.setProcessoId(termo.getProcessoId());
    response.setTipo(termo.getTipo());
    response.setDadosJson(termo.getDadosJson());
    response.setStatusAssinatura(termo.getStatusAssinatura());
    response.setDataAssinatura(termo.getDataAssinatura());
    response.setResponsavel(termo.getResponsavel());
    response.setCriadoEm(termo.getCriadoEm());
    response.setAtualizadoEm(termo.getAtualizadoEm());
    return response;
  }

  private RhPpdResponse mapPpd(RhPpd ppd) {
    RhPpdResponse response = new RhPpdResponse();
    response.setId(ppd.getId());
    response.setProcessoId(ppd.getProcessoId());
    response.setCabecalhoJson(ppd.getCabecalhoJson());
    response.setLadoAJson(ppd.getLadoAJson());
    response.setLadoBJson(ppd.getLadoBJson());
    response.setCriadoEm(ppd.getCriadoEm());
    response.setAtualizadoEm(ppd.getAtualizadoEm());
    return response;
  }

  private RhCartaBancoResponse mapCarta(RhCartaBanco carta) {
    RhCartaBancoResponse response = new RhCartaBancoResponse();
    response.setId(carta.getId());
    response.setProcessoId(carta.getProcessoId());
    response.setDadosJson(carta.getDadosJson());
    response.setCriadoEm(carta.getCriadoEm());
    response.setAtualizadoEm(carta.getAtualizadoEm());
    return response;
  }

  private void registrarAuditoria(Long processoId, Long usuarioId, String acao, String detalhes) {
    if (processoId == null) {
      return;
    }
    RhAuditoriaContratacao auditoria = new RhAuditoriaContratacao();
    auditoria.setProcessoId(processoId);
    auditoria.setAtorId(usuarioId);
    auditoria.setAcao(acao);
    auditoria.setDetalhes(detalhes);
    auditoria.setCriadoEm(LocalDateTime.now());
    auditoriaRepository.salvar(auditoria);
  }

  private void garantirChecklist(Long processoId) {
    for (String item : itensObrigatorios()) {
      Optional<RhDocumentoItem> existente = documentoRepository.buscarPorProcessoETipo(processoId, item);
      if (existente.isEmpty()) {
        RhDocumentoItem novo = new RhDocumentoItem();
        novo.setProcessoId(processoId);
        novo.setTipoDocumento(item);
        novo.setObrigatorio(Boolean.TRUE);
        novo.setStatus("pendente");
        LocalDateTime agora = LocalDateTime.now();
        novo.setCriadoEm(agora);
        novo.setAtualizadoEm(agora);
        documentoRepository.salvar(novo);
      }
    }
  }

  private boolean documentosAprovados(Long processoId) {
    garantirChecklist(processoId);
    for (RhDocumentoItem item : documentoRepository.listarPorProcesso(processoId)) {
      if (Boolean.TRUE.equals(item.getObrigatorio()) && !"aprovado".equalsIgnoreCase(item.getStatus())) {
        return false;
      }
    }
    return true;
  }

  private String[] itensObrigatorios() {
    return new String[] {
      "Atestado medico admissional (ASO)",
      "RG (frente e verso)",
      "Foto 3x4",
      "CPF",
      "Titulo de eleitor",
      "PIS",
      "Reservista",
      "Comprovante de residencia",
      "Comprovante de escolaridade",
      "Certidao de casamento",
      "Certidao de casamento com averbacao",
      "Certidao de nascimento",
      "Certidao de nascimento filhos ate 21 anos",
      "Carteira de vacinacao filhos menores",
      "Comprovante frequencia escolar filhos",
      "Atestado invalidez filhos"
    };
  }

  private String validarStatus(String status) {
    if (!StringUtils.hasText(status)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status do processo e obrigatorio.");
    }
    String statusNormalizado = status.trim();
    if (!STATUS_CADASTRADO.equals(statusNormalizado)
        && !STATUS_EM_ENTREVISTA.equals(statusNormalizado)
        && !STATUS_APROVADO.equals(statusNormalizado)
        && !STATUS_DOC_PENDENTE.equals(statusNormalizado)
        && !STATUS_DOC_OK.equals(statusNormalizado)
        && !STATUS_ADMITIDO.equals(statusNormalizado)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status informado e invalido.");
    }
    return statusNormalizado;
  }

  private boolean correspondeTermo(RhCandidato candidato, RhProcessoContratacao processo, String termo) {
    if (candidato == null) {
      return false;
    }
    String nome = normalizar(candidato.getNomeCompleto());
    String cpf = normalizar(candidato.getCpf());
    String vaga = normalizar(candidato.getVagaPretendida());
    String status = processo != null ? normalizar(processo.getStatus()) : "";
    return nome.contains(termo) || cpf.contains(termo) || vaga.contains(termo) || status.contains(termo);
  }

  private String normalizar(String valor) {
    if (!StringUtils.hasText(valor)) {
      return "";
    }
    return valor.toLowerCase(Locale.ROOT)
        .replace("ã", "a")
        .replace("á", "a")
        .replace("à", "a")
        .replace("â", "a")
        .replace("é", "e")
        .replace("ê", "e")
        .replace("í", "i")
        .replace("ó", "o")
        .replace("ô", "o")
        .replace("õ", "o")
        .replace("ú", "u")
        .replace("ç", "c");
  }

  private boolean equalText(String valor, String outro) {
    if (!StringUtils.hasText(valor) && !StringUtils.hasText(outro)) {
      return true;
    }
    return StringUtils.hasText(valor) && StringUtils.hasText(outro) && valor.equalsIgnoreCase(outro);
  }
}
