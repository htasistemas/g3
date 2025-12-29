package br.com.g3.prontuario.serviceimpl;

import br.com.g3.cadastrobeneficiario.domain.CadastroBeneficiario;
import br.com.g3.cadastrobeneficiario.domain.ContatoBeneficiario;
import br.com.g3.cadastrobeneficiario.domain.SituacaoSocialBeneficiario;
import br.com.g3.cadastrobeneficiario.repository.CadastroBeneficiarioRepository;
import br.com.g3.prontuario.domain.ProntuarioAnexo;
import br.com.g3.prontuario.domain.ProntuarioRegistro;
import br.com.g3.prontuario.dto.BeneficiarioResumoResponse;
import br.com.g3.prontuario.dto.ProntuarioAnexoRequest;
import br.com.g3.prontuario.dto.ProntuarioAnexoResponse;
import br.com.g3.prontuario.dto.ProntuarioRegistroListaResponse;
import br.com.g3.prontuario.dto.ProntuarioRegistroRequest;
import br.com.g3.prontuario.dto.ProntuarioRegistroResponse;
import br.com.g3.prontuario.dto.ProntuarioResumoResponse;
import br.com.g3.prontuario.repository.ProntuarioAnexoRepository;
import br.com.g3.prontuario.repository.ProntuarioRegistroConsultaResultado;
import br.com.g3.prontuario.repository.ProntuarioRegistroRepository;
import br.com.g3.prontuario.service.ProntuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
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
public class ProntuarioServiceImpl implements ProntuarioService {
  private final CadastroBeneficiarioRepository beneficiarioRepository;
  private final ProntuarioRegistroRepository registroRepository;
  private final ProntuarioAnexoRepository anexoRepository;
  private final ObjectMapper objectMapper;

  public ProntuarioServiceImpl(
      CadastroBeneficiarioRepository beneficiarioRepository,
      ProntuarioRegistroRepository registroRepository,
      ProntuarioAnexoRepository anexoRepository,
      ObjectMapper objectMapper) {
    this.beneficiarioRepository = beneficiarioRepository;
    this.registroRepository = registroRepository;
    this.anexoRepository = anexoRepository;
    this.objectMapper = objectMapper;
  }

  @Override
  @Transactional(readOnly = true)
  public ProntuarioResumoResponse obterResumo(Long beneficiarioId) {
    CadastroBeneficiario beneficiario =
        beneficiarioRepository
            .buscarPorId(beneficiarioId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Beneficiario nao encontrado."));

    BeneficiarioResumoResponse resumo = new BeneficiarioResumoResponse();
    resumo.setId(beneficiario.getId());
    resumo.setNomeCompleto(beneficiario.getNomeCompleto());
    resumo.setCpf(extrairCpf(beneficiario));
    LocalDate dataNascimento = beneficiario.getDataNascimento();
    resumo.setDataNascimento(dataNascimento != null ? dataNascimento.toString() : null);
    resumo.setNomeMae(beneficiario.getNomeMae());
    resumo.setFoto3x4(beneficiario.getFoto3x4());
    resumo.setStatus(beneficiario.getStatus());
    resumo.setEndereco(formatarEndereco(beneficiario));

    Optional<ContatoBeneficiario> contatoPrincipal =
        beneficiario.getContatos().stream().findFirst();
    contatoPrincipal.ifPresent(
        contato -> {
          resumo.setTelefone(contato.getTelefonePrincipal());
          resumo.setWhatsapp(Boolean.TRUE.equals(contato.getTelefonePrincipalWhatsapp()) ? contato.getTelefonePrincipal() : null);
        });

    String vulnerabilidades = extrairVulnerabilidades(beneficiario);
    if (vulnerabilidades != null && !vulnerabilidades.trim().isEmpty()) {
      List<String> lista = Arrays.asList(vulnerabilidades.split(";"));
      resumo.setVulnerabilidades(
          lista.stream().map(String::trim).filter(item -> !item.isEmpty()).collect(Collectors.toList()));
    }

    Map<String, Long> contagens = new HashMap<>(registroRepository.contarPorTipo(beneficiarioId));
    LocalDateTime ultimaAtualizacao = registroRepository.buscarUltimaAtualizacao(beneficiarioId);

    return new ProntuarioResumoResponse(resumo, contagens, ultimaAtualizacao);
  }

  @Override
  @Transactional(readOnly = true)
  public ProntuarioRegistroListaResponse listarRegistros(
      Long beneficiarioId,
      String tipo,
      LocalDateTime dataInicio,
      LocalDateTime dataFim,
      Long profissionalId,
      Long unidadeId,
      String status,
      String texto,
      int pagina,
      int tamanhoPagina) {
    ProntuarioRegistroConsultaResultado resultado =
        registroRepository.listarRegistros(
            beneficiarioId, tipo, dataInicio, dataFim, profissionalId, unidadeId, status, texto, pagina, tamanhoPagina);
    return new ProntuarioRegistroListaResponse(
        resultado.getRegistros(), resultado.getTotal(), pagina, tamanhoPagina);
  }

  @Override
  @Transactional
  public ProntuarioRegistroResponse criarRegistro(Long beneficiarioId, ProntuarioRegistroRequest request) {
    validarBeneficiario(beneficiarioId);
    ProntuarioRegistro registro = new ProntuarioRegistro();
    registro.setBeneficiarioId(beneficiarioId);
    aplicarRequest(request, registro);
    LocalDateTime agora = LocalDateTime.now();
    registro.setCriadoEm(agora);
    registro.setAtualizadoEm(agora);
    registro.setCriadoPor(request.getCriadoPor());
    registro.setAtualizadoPor(request.getAtualizadoPor());
    return mapRegistro(registroRepository.salvar(registro));
  }

  @Override
  @Transactional
  public ProntuarioRegistroResponse atualizarRegistro(Long registroId, ProntuarioRegistroRequest request) {
    ProntuarioRegistro registro =
        registroRepository
            .buscarPorId(registroId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro nao encontrado."));
    aplicarRequest(request, registro);
    registro.setAtualizadoEm(LocalDateTime.now());
    registro.setAtualizadoPor(request.getAtualizadoPor());
    return mapRegistro(registroRepository.salvar(registro));
  }

  @Override
  @Transactional
  public void removerRegistro(Long registroId) {
    ProntuarioRegistro registro =
        registroRepository
            .buscarPorId(registroId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro nao encontrado."));
    registroRepository.remover(registro);
  }

  @Override
  @Transactional
  public ProntuarioAnexoResponse adicionarAnexo(Long registroId, ProntuarioAnexoRequest request) {
    registroRepository
        .buscarPorId(registroId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro nao encontrado."));
    ProntuarioAnexo anexo = new ProntuarioAnexo();
    anexo.setRegistroId(registroId);
    anexo.setNomeArquivo(request.getNomeArquivo());
    anexo.setTipoMime(request.getTipoMime());
    anexo.setUrlArquivo(request.getUrlArquivo());
    anexo.setCriadoEm(LocalDateTime.now());
    return mapAnexo(anexoRepository.salvar(anexo));
  }

  private void aplicarRequest(ProntuarioRegistroRequest request, ProntuarioRegistro registro) {
    registro.setTipo(request.getTipo());
    registro.setDataRegistro(request.getDataRegistro());
    registro.setProfissionalId(request.getProfissionalId());
    registro.setUnidadeId(request.getUnidadeId());
    registro.setTitulo(request.getTitulo());
    registro.setDescricao(request.getDescricao());
    registro.setStatus(request.getStatus());
    registro.setDadosExtra(serializeExtra(request.getDadosExtra()));
  }

  private ProntuarioRegistroResponse mapRegistro(ProntuarioRegistro registro) {
    ProntuarioRegistroResponse response = new ProntuarioRegistroResponse();
    response.setId(registro.getId());
    response.setBeneficiarioId(registro.getBeneficiarioId());
    response.setTipo(registro.getTipo());
    response.setDataRegistro(registro.getDataRegistro());
    response.setProfissionalId(registro.getProfissionalId());
    response.setUnidadeId(registro.getUnidadeId());
    response.setTitulo(registro.getTitulo());
    response.setDescricao(registro.getDescricao());
    response.setStatus(registro.getStatus());
    response.setDadosExtra(deserializeExtra(registro.getDadosExtra()));
    response.setCriadoEm(registro.getCriadoEm());
    response.setCriadoPor(registro.getCriadoPor());
    response.setAtualizadoEm(registro.getAtualizadoEm());
    response.setAtualizadoPor(registro.getAtualizadoPor());
    return response;
  }

  private ProntuarioAnexoResponse mapAnexo(ProntuarioAnexo anexo) {
    ProntuarioAnexoResponse response = new ProntuarioAnexoResponse();
    response.setId(anexo.getId());
    response.setRegistroId(anexo.getRegistroId());
    response.setNomeArquivo(anexo.getNomeArquivo());
    response.setTipoMime(anexo.getTipoMime());
    response.setUrlArquivo(anexo.getUrlArquivo());
    response.setCriadoEm(anexo.getCriadoEm());
    return response;
  }

  private String serializeExtra(Map<String, Object> extra) {
    if (extra == null || extra.isEmpty()) {
      return null;
    }
    try {
      return objectMapper.writeValueAsString(extra);
    } catch (Exception ex) {
      return null;
    }
  }

  private Map<String, Object> deserializeExtra(String extra) {
    if (extra == null || extra.trim().isEmpty()) {
      return Collections.emptyMap();
    }
    try {
      return objectMapper.readValue(extra, Map.class);
    } catch (Exception ex) {
      return Collections.emptyMap();
    }
  }

  private void validarBeneficiario(Long beneficiarioId) {
    if (beneficiarioId == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Beneficiario nao informado.");
    }
    beneficiarioRepository
        .buscarPorId(beneficiarioId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Beneficiario nao encontrado."));
  }

  private String formatarEndereco(CadastroBeneficiario beneficiario) {
    if (beneficiario.getEndereco() == null) {
      return null;
    }
    List<String> partes = new java.util.ArrayList<>();
    partes.add(beneficiario.getEndereco().getLogradouro());
    partes.add(beneficiario.getEndereco().getNumero());
    partes.add(beneficiario.getEndereco().getBairro());
    partes.add(beneficiario.getEndereco().getCidade());
    partes.add(beneficiario.getEndereco().getEstado());
    return partes.stream().filter(item -> item != null && !item.trim().isEmpty()).collect(Collectors.joining(", "));
  }

  private String extrairCpf(CadastroBeneficiario beneficiario) {
    return beneficiario.getDocumentos().stream()
        .filter(doc -> "CPF".equalsIgnoreCase(doc.getTipoDocumento()))
        .map(doc -> doc.getNumeroDocumento())
        .filter(numero -> numero != null && !numero.trim().isEmpty())
        .findFirst()
        .orElse(null);
  }

  private String extrairVulnerabilidades(CadastroBeneficiario beneficiario) {
    return beneficiario.getSituacoesSociais().stream()
        .map(SituacaoSocialBeneficiario::getSituacaoVulnerabilidade)
        .filter(item -> item != null && !item.trim().isEmpty())
        .findFirst()
        .orElse(null);
  }
}
