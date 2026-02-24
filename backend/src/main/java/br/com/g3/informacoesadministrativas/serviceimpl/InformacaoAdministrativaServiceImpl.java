package br.com.g3.informacoesadministrativas.serviceimpl;

import br.com.g3.informacoesadministrativas.crypto.CriptografiaSegredoService;
import br.com.g3.informacoesadministrativas.crypto.CriptografiaSegredoService.ResultadoCriptografia;
import br.com.g3.informacoesadministrativas.domain.InformacaoAdministrativa;
import br.com.g3.informacoesadministrativas.domain.InformacaoAdministrativaAuditoria;
import br.com.g3.informacoesadministrativas.dto.InformacaoAdministrativaRequest;
import br.com.g3.informacoesadministrativas.dto.InformacaoAdministrativaResponse;
import br.com.g3.informacoesadministrativas.dto.InformacaoAdministrativaRevealResponse;
import br.com.g3.informacoesadministrativas.repository.InformacaoAdministrativaAuditoriaRepository;
import br.com.g3.informacoesadministrativas.repository.InformacaoAdministrativaRepository;
import br.com.g3.informacoesadministrativas.service.InformacaoAdministrativaService;
import br.com.g3.usuario.domain.Permissao;
import br.com.g3.usuario.domain.Usuario;
import br.com.g3.usuario.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class InformacaoAdministrativaServiceImpl implements InformacaoAdministrativaService {
  private static final String PERMISSAO_ADMIN = "ADMINISTRADOR";
  private final InformacaoAdministrativaRepository repository;
  private final InformacaoAdministrativaAuditoriaRepository auditoriaRepository;
  private final UsuarioRepository usuarioRepository;
  private final CriptografiaSegredoService criptografiaService;

  public InformacaoAdministrativaServiceImpl(
      InformacaoAdministrativaRepository repository,
      InformacaoAdministrativaAuditoriaRepository auditoriaRepository,
      UsuarioRepository usuarioRepository,
      CriptografiaSegredoService criptografiaService) {
    this.repository = repository;
    this.auditoriaRepository = auditoriaRepository;
    this.usuarioRepository = usuarioRepository;
    this.criptografiaService = criptografiaService;
  }

  @Override
  public List<InformacaoAdministrativaResponse> listar(
      String tipo,
      String categoria,
      String titulo,
      String tags,
      Boolean status,
      Long usuarioId,
      String ip) {
    validarAdmin(usuarioId);
    List<InformacaoAdministrativaResponse> resposta = repository.listar().stream()
        .filter(info -> filtrarTexto(info.getTipo(), tipo))
        .filter(info -> filtrarTexto(info.getCategoria(), categoria))
        .filter(info -> filtrarTexto(info.getTitulo(), titulo))
        .filter(info -> filtrarTexto(info.getTags(), tags))
        .filter(info -> status == null || Objects.equals(info.getStatus(), status))
        .map(this::toResponse)
        .collect(Collectors.toList());
    registrarAuditoria(null, "SEARCH", usuarioId, ip, "listagem");
    return resposta;
  }

  @Override
  public InformacaoAdministrativaResponse buscarPorId(Long id, Long usuarioId, String ip) {
    validarAdmin(usuarioId);
    InformacaoAdministrativa info = buscarOuFalhar(id);
    registrarAuditoria(id, "VIEW", usuarioId, ip, "detalhe");
    return toResponse(info);
  }

  @Override
  @Transactional
  public InformacaoAdministrativaResponse criar(
      InformacaoAdministrativaRequest request,
      Long usuarioId,
      String ip) {
    validarAdmin(usuarioId);
    InformacaoAdministrativa info = new InformacaoAdministrativa();
    preencherCampos(info, request, usuarioId, true);
    InformacaoAdministrativa salvo = repository.salvar(info);
    registrarAuditoria(salvo.getId(), "CREATE", usuarioId, ip, montarDetalhesBasicos(salvo));
    return toResponse(salvo);
  }

  @Override
  @Transactional
  public InformacaoAdministrativaResponse atualizar(
      Long id,
      InformacaoAdministrativaRequest request,
      Long usuarioId,
      String ip) {
    validarAdmin(usuarioId);
    InformacaoAdministrativa info = buscarOuFalhar(id);
    preencherCampos(info, request, usuarioId, false);
    InformacaoAdministrativa salvo = repository.salvar(info);
    registrarAuditoria(id, "UPDATE", usuarioId, ip, montarDetalhesBasicos(salvo));
    return toResponse(salvo);
  }

  @Override
  @Transactional
  public void remover(Long id, Long usuarioId, String ip) {
    validarAdmin(usuarioId);
    InformacaoAdministrativa info = buscarOuFalhar(id);
    repository.remover(info);
    registrarAuditoria(id, "DELETE", usuarioId, ip, montarDetalhesBasicos(info));
  }

  @Override
  public InformacaoAdministrativaRevealResponse revelar(Long id, Long usuarioId, String ip) {
    validarAdmin(usuarioId);
    InformacaoAdministrativa info = buscarOuFalhar(id);
    if (info.getSegredoCiphertext() == null || info.getSegredoCiphertext().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Segredo nao informado.");
    }
    String segredo = criptografiaService.descriptografar(
        info.getSegredoCiphertext(), info.getSegredoIv(), info.getSegredoTag());
    registrarAuditoria(id, "REVEAL", usuarioId, ip, "segredo revelado");
    return new InformacaoAdministrativaRevealResponse(segredo, LocalDateTime.now().plusSeconds(15));
  }

  @Override
  public void registrarCopia(Long id, Long usuarioId, String ip) {
    validarAdmin(usuarioId);
    registrarAuditoria(id, "COPY", usuarioId, ip, "segredo copiado");
  }

  private InformacaoAdministrativa buscarOuFalhar(Long id) {
    return repository
        .buscarPorId(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro nao encontrado."));
  }

  private void preencherCampos(
      InformacaoAdministrativa info,
      InformacaoAdministrativaRequest request,
      Long usuarioId,
      boolean novo) {
    if (request == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe os dados da informacao.");
    }
    info.setTipo(normalizar(request.getTipo()));
    info.setCategoria(normalizar(request.getCategoria()));
    info.setTitulo(normalizar(request.getTitulo()));
    info.setDescricao(normalizarTexto(request.getDescricao()));
    info.setResponsavel(normalizarTexto(request.getResponsavel()));
    info.setHostUrl(normalizarTexto(request.getHostUrl()));
    info.setPorta(request.getPorta());
    info.setLogin(normalizarTexto(request.getLogin()));
    info.setTags(normalizarTexto(request.getTags()));
    info.setStatus(request.getStatus() != null ? request.getStatus() : Boolean.TRUE);

    if (request.getSegredo() != null && !request.getSegredo().isBlank()) {
      ResultadoCriptografia resultado = criptografiaService.criptografar(request.getSegredo());
      info.setSegredoCiphertext(resultado.getCiphertext());
      info.setSegredoIv(resultado.getIv());
      info.setSegredoTag(resultado.getTag());
    }

    LocalDateTime agora = LocalDateTime.now();
    String usuarioNome = obterUsuarioNome(usuarioId);
    if (novo) {
      info.setCriadoEm(agora);
      info.setCriadoPor(usuarioNome);
    }
    info.setAtualizadoEm(agora);
    info.setAtualizadoPor(usuarioNome);
  }

  private void validarAdmin(Long usuarioId) {
    if (usuarioId == null) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permissao negada.");
    }
    Usuario usuario =
        usuarioRepository
            .buscarPorId(usuarioId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Permissao negada."));
    boolean admin = usuario.getPermissoes().stream().map(Permissao::getNome)
        .anyMatch(nome -> PERMISSAO_ADMIN.equalsIgnoreCase(nome));
    if (!admin) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permissao negada.");
    }
  }

  private String obterUsuarioNome(Long usuarioId) {
    if (usuarioId == null) {
      return "Sistema";
    }
    Usuario usuario = usuarioRepository.buscarPorId(usuarioId).orElse(null);
    if (usuario == null) {
      return "Sistema";
    }
    if (usuario.getNome() != null && !usuario.getNome().isBlank()) {
      return usuario.getNome().trim();
    }
    return usuario.getNomeUsuario();
  }

  private void registrarAuditoria(Long id, String acao, Long usuarioId, String ip, String detalhes) {
    InformacaoAdministrativaAuditoria auditoria = new InformacaoAdministrativaAuditoria();
    auditoria.setInfoAdminId(id != null ? id : 0L);
    auditoria.setAcao(acao);
    auditoria.setUsuarioId(usuarioId);
    auditoria.setUsuarioNome(obterUsuarioNome(usuarioId));
    auditoria.setDataHora(LocalDateTime.now());
    auditoria.setIpOrigem(ip);
    auditoria.setDetalhes(detalhes);
    auditoriaRepository.salvar(auditoria);
  }

  private boolean filtrarTexto(String valor, String filtro) {
    if (filtro == null || filtro.isBlank()) {
      return true;
    }
    String normalizado = normalizarFiltro(valor);
    String filtroNormalizado = normalizarFiltro(filtro);
    return normalizado.contains(filtroNormalizado);
  }

  private String normalizar(String valor) {
    if (valor == null || valor.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campos obrigatorios nao preenchidos.");
    }
    return valor.trim();
  }

  private String normalizarTexto(String valor) {
    if (valor == null) {
      return null;
    }
    return valor.trim();
  }

  private String normalizarFiltro(String valor) {
    if (valor == null) {
      return "";
    }
    return valor.trim().toLowerCase(Locale.forLanguageTag("pt-BR"));
  }

  private String montarDetalhesBasicos(InformacaoAdministrativa info) {
    return "tipo=" + info.getTipo() + ", categoria=" + info.getCategoria() + ", titulo=" + info.getTitulo();
  }

  private InformacaoAdministrativaResponse toResponse(InformacaoAdministrativa info) {
    InformacaoAdministrativaResponse response = new InformacaoAdministrativaResponse();
    response.setId(info.getId());
    response.setTipo(info.getTipo());
    response.setCategoria(info.getCategoria());
    response.setTitulo(info.getTitulo());
    response.setDescricao(info.getDescricao());
    response.setResponsavel(info.getResponsavel());
    response.setHostUrl(info.getHostUrl());
    response.setPorta(info.getPorta());
    response.setLogin(info.getLogin());
    response.setTags(info.getTags());
    response.setStatus(info.getStatus());
    response.setCriadoEm(info.getCriadoEm());
    response.setCriadoPor(info.getCriadoPor());
    response.setAtualizadoEm(info.getAtualizadoEm());
    response.setAtualizadoPor(info.getAtualizadoPor());
    return response;
  }
}
