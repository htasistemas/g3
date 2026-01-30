package br.com.g3.cadastrobeneficiario.serviceimpl;

import br.com.g3.cadastrobeneficiario.domain.CadastroBeneficiario;
import br.com.g3.cadastrobeneficiario.domain.DocumentoBeneficiario;
import br.com.g3.cadastrobeneficiario.dto.AptidaoCestaBasicaRequest;
import br.com.g3.cadastrobeneficiario.dto.CadastroBeneficiarioCriacaoRequest;
import br.com.g3.cadastrobeneficiario.dto.CadastroBeneficiarioResponse;
import br.com.g3.cadastrobeneficiario.dto.CadastroBeneficiarioResumoResponse;
import br.com.g3.cadastrobeneficiario.dto.DocumentoUploadRequest;
import br.com.g3.cadastrobeneficiario.mapper.CadastroBeneficiarioMapper;
import br.com.g3.cadastrobeneficiario.repository.CadastroBeneficiarioRepository;
import br.com.g3.cadastrobeneficiario.repositoryimpl.DocumentoBeneficiarioJpaRepository;
import br.com.g3.cadastrobeneficiario.service.ArmazenamentoDocumentoService;
import br.com.g3.cadastrobeneficiario.service.CadastroBeneficiarioService;
import br.com.g3.unidadeassistencial.domain.Endereco;
import br.com.g3.unidadeassistencial.service.GeocodificacaoService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CadastroBeneficiarioServiceImpl implements CadastroBeneficiarioService {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(CadastroBeneficiarioServiceImpl.class);
  private final CadastroBeneficiarioRepository repository;
  private final ArmazenamentoDocumentoService armazenamentoDocumentoService;
  private final DocumentoBeneficiarioJpaRepository documentoRepository;
  private final GeocodificacaoService geocodificacaoService;
  private final br.com.g3.shared.service.EmailService emailService;

  public CadastroBeneficiarioServiceImpl(
      CadastroBeneficiarioRepository repository,
      ArmazenamentoDocumentoService armazenamentoDocumentoService,
      DocumentoBeneficiarioJpaRepository documentoRepository,
      GeocodificacaoService geocodificacaoService,
      br.com.g3.shared.service.EmailService emailService) {
    this.repository = repository;
    this.armazenamentoDocumentoService = armazenamentoDocumentoService;
    this.documentoRepository = documentoRepository;
    this.geocodificacaoService = geocodificacaoService;
    this.emailService = emailService;
  }

  @Override
  @Transactional
  public CadastroBeneficiarioResponse criar(CadastroBeneficiarioCriacaoRequest request) {
    CadastroBeneficiario cadastro = CadastroBeneficiarioMapper.toDomain(request);
    if (cadastro.getCodigo() == null || cadastro.getCodigo().trim().isEmpty()) {
      cadastro.setCodigo(gerarCodigoSequencial());
    }
    CadastroBeneficiario salvo = repository.salvar(cadastro);
    adicionarDocumentosUpload(salvo, request);
    CadastroBeneficiario atualizado = repository.salvar(salvo);
    CadastroBeneficiarioResponse response = CadastroBeneficiarioMapper.toResponse(atualizado);
    enviarEmailCadastro(response);
    return response;
  }

  @Override
  @Transactional
  public CadastroBeneficiarioResponse atualizar(Long id, CadastroBeneficiarioCriacaoRequest request) {
    CadastroBeneficiario cadastro =
        repository.buscarPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    CadastroBeneficiarioMapper.aplicarAtualizacao(cadastro, request);
    CadastroBeneficiario salvo = repository.salvar(cadastro);
    adicionarDocumentosUpload(salvo, request);
    CadastroBeneficiario atualizado = repository.salvar(salvo);
    CadastroBeneficiarioResponse response = CadastroBeneficiarioMapper.toResponse(atualizado);
    enviarEmailAtualizacao(response);
    return response;
  }

  @Override
  public CadastroBeneficiarioResponse buscarPorId(Long id) {
    CadastroBeneficiario cadastro =
        repository.buscarPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    return CadastroBeneficiarioMapper.toResponse(cadastro);
  }

  @Override
  public List<CadastroBeneficiarioResponse> listar(String nome, String status) {
    boolean temNome = nome != null && !nome.trim().isEmpty();
    boolean temStatus = status != null && !status.trim().isEmpty();

    List<CadastroBeneficiario> cadastros;
    if (temNome && temStatus) {
      cadastros = repository.listarPorNomeEStatus(nome, status);
    } else if (temNome) {
      cadastros = repository.buscarPorNome(nome);
    } else if (temStatus) {
      cadastros = repository.listar().stream()
          .filter(cadastro -> status.equalsIgnoreCase(cadastro.getStatus()))
          .collect(Collectors.toList());
    } else {
      cadastros = repository.listar();
    }
    return cadastros.stream()
        .collect(Collectors.toMap(CadastroBeneficiario::getId, cadastro -> cadastro, (a, b) -> a, java.util.LinkedHashMap::new))
        .values()
        .stream()
        .map(CadastroBeneficiarioMapper::toResponse)
        .collect(Collectors.toList());
  }

  @Override
  public List<CadastroBeneficiarioResumoResponse> listarResumo(String nome, String status) {
    boolean temNome = nome != null && !nome.trim().isEmpty();
    boolean temStatus = status != null && !status.trim().isEmpty();

    List<CadastroBeneficiario> cadastros;
    if (temNome && temStatus) {
      cadastros = repository.listarPorNomeEStatus(nome, status);
    } else if (temNome) {
      cadastros = repository.buscarPorNome(nome);
    } else if (temStatus) {
      cadastros = repository.listar().stream()
          .filter(cadastro -> status.equalsIgnoreCase(cadastro.getStatus()))
          .collect(Collectors.toList());
    } else {
      cadastros = repository.listar();
    }

    return cadastros.stream()
        .collect(Collectors.toMap(CadastroBeneficiario::getId, cadastro -> cadastro, (a, b) -> a, java.util.LinkedHashMap::new))
        .values()
        .stream()
        .map(cadastro -> new CadastroBeneficiarioResumoResponse(cadastro.getId(), cadastro.getNomeCompleto()))
        .collect(Collectors.toList());
  }

  @Override
  public void remover(Long id) {
    CadastroBeneficiario cadastro =
        repository.buscarPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    repository.remover(cadastro);
  }

  @Override
  public DocumentoBeneficiario obterDocumento(Long beneficiarioId, Long documentoId) {
    DocumentoBeneficiario documento =
        documentoRepository
            .findById(documentoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    if (documento.getBeneficiario() == null
        || documento.getBeneficiario().getId() == null
        || !documento.getBeneficiario().getId().equals(beneficiarioId)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return documento;
  }

  @Override
  @Transactional
  public CadastroBeneficiarioResponse geocodificarEndereco(Long id, boolean forcar) {
    CadastroBeneficiario cadastro =
        repository.buscarPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    List<String> camposFaltantes = obterCamposEnderecoFaltantes(cadastro.getEndereco());
    if (!camposFaltantes.isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Endereco incompleto: " + String.join(", ", camposFaltantes) + ".");
    }
    CadastroBeneficiario atualizado = tentarGeocodificarEndereco(cadastro, forcar);
    if (atualizado == cadastro) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Nao foi possivel geocodificar o endereco informado.");
    }
    return CadastroBeneficiarioMapper.toResponse(atualizado);
  }

  @Override
  public CadastroBeneficiarioResponse atualizarAptidaoCestaBasica(
      Long id, AptidaoCestaBasicaRequest request) {
    CadastroBeneficiario cadastro =
        repository
            .buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Beneficiario nao encontrado."));

    Boolean opta = request.getOptaReceberCestaBasica();
    Boolean apto = request.getAptoReceberCestaBasica();

    cadastro.setOptaReceberCestaBasica(opta);
    cadastro.setAptoReceberCestaBasica(Boolean.FALSE.equals(opta) ? null : apto);

    CadastroBeneficiario salvo = repository.salvar(cadastro);
    return CadastroBeneficiarioMapper.toResponse(salvo);
  }

  private void adicionarDocumentosUpload(CadastroBeneficiario cadastro, CadastroBeneficiarioCriacaoRequest request) {
    List<DocumentoUploadRequest> documentos = request.getDocumentosObrigatorios();
    if (documentos == null || documentos.isEmpty()) {
      return;
    }

    LocalDateTime agora = LocalDateTime.now();
    for (DocumentoUploadRequest doc : documentos) {
      if (doc == null || doc.getConteudo() == null || doc.getConteudo().trim().isEmpty()) {
        continue;
      }

      String caminho = armazenamentoDocumentoService.salvarArquivo(cadastro.getId(), doc);
      DocumentoBeneficiario documento = new DocumentoBeneficiario();
      documento.setBeneficiario(cadastro);
      documento.setNomeDocumento(doc.getNome());
      documento.setNomeArquivo(doc.getNomeArquivo());
      documento.setContentType(doc.getContentType());
      documento.setObrigatorio(doc.getObrigatorio());
      documento.setCaminhoArquivo(caminho);
      documento.setCriadoEm(agora);
      documento.setAtualizadoEm(agora);
      cadastro.getDocumentos().add(documento);
    }
  }

  private String gerarCodigoSequencial() {
    Integer maiorCodigo = repository.buscarMaiorCodigo();
    int proximoCodigo = (maiorCodigo == null ? 0 : maiorCodigo) + 1;
    return String.format("%04d", proximoCodigo);
  }

  private CadastroBeneficiario tentarGeocodificarEndereco(CadastroBeneficiario cadastro, boolean forcar) {
    Endereco endereco = cadastro.getEndereco();
    if (endereco == null) {
      return cadastro;
    }
    if (!obterCamposEnderecoFaltantes(endereco).isEmpty()) {
      return cadastro;
    }
    if (!forcar && endereco.getLatitude() != null && endereco.getLongitude() != null) {
      return cadastro;
    }
    return geocodificacaoService
        .geocodificar(endereco)
        .map(
            coordenadas -> {
              endereco.setLatitude(coordenadas.getLatitude());
              endereco.setLongitude(coordenadas.getLongitude());
              endereco.setAtualizadoEm(LocalDateTime.now());
              cadastro.setAtualizadoEm(LocalDateTime.now());
              return repository.salvar(cadastro);
            })
        .orElse(cadastro);
  }

  private List<String> obterCamposEnderecoFaltantes(Endereco endereco) {
    if (endereco == null) {
      return List.of("endereco");
    }
    List<String> faltantes = new java.util.ArrayList<>();
    if (endereco.getLogradouro() == null || endereco.getLogradouro().trim().isEmpty()) {
      faltantes.add("logradouro");
    }
    if (endereco.getNumero() == null || endereco.getNumero().trim().isEmpty()) {
      faltantes.add("numero");
    }
    if (endereco.getCidade() == null || endereco.getCidade().trim().isEmpty()) {
      faltantes.add("cidade");
    }
    if (endereco.getEstado() == null || endereco.getEstado().trim().isEmpty()) {
      faltantes.add("estado");
    }
    return faltantes;
  }

  private void enviarEmailCadastro(CadastroBeneficiarioResponse beneficiario) {
    if (!podeEnviarEmail(beneficiario)) return;
    try {
      emailService.enviarCadastroBeneficiario(
          beneficiario.getEmail(),
          beneficiario.getNomeCompleto(),
          beneficiario.getCodigo());
    } catch (Exception ex) {
      LOGGER.warn("Envio de email de cadastro de beneficiario ignorado.", ex);
    }
  }

  private void enviarEmailAtualizacao(CadastroBeneficiarioResponse beneficiario) {
    if (!podeEnviarEmail(beneficiario)) return;
    try {
      emailService.enviarAtualizacaoBeneficiario(
          beneficiario.getEmail(),
          beneficiario.getNomeCompleto(),
          beneficiario.getCodigo());
    } catch (Exception ex) {
      LOGGER.warn("Envio de email de atualizacao de beneficiario ignorado.", ex);
    }
  }

  private boolean podeEnviarEmail(CadastroBeneficiarioResponse beneficiario) {
    if (beneficiario == null) return false;
    if (beneficiario.getPermiteContatoEmail() == null || !beneficiario.getPermiteContatoEmail()) {
      return false;
    }
    String email = beneficiario.getEmail();
    return email != null && !email.trim().isEmpty();
  }
}
