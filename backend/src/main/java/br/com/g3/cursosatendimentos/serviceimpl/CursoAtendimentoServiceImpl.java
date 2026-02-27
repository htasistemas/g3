package br.com.g3.cursosatendimentos.serviceimpl;

import br.com.g3.cursosatendimentos.domain.CursoAtendimento;
import br.com.g3.cursosatendimentos.domain.CursoAtendimentoMatricula;
import br.com.g3.cursosatendimentos.domain.CursoAtendimentoPresencaAnexo;
import br.com.g3.cursosatendimentos.domain.CursoAtendimentoPresencaData;
import br.com.g3.cursosatendimentos.domain.CursoAtendimentoPresenca;
import br.com.g3.cursosatendimentos.domain.CursoAtendimentoStatus;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaAnexoRequest;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaAnexoResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaDataListaResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaDataRequest;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaDataResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaItemRequest;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaItemResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaRequest;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoRequest;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoStatusRequest;
import br.com.g3.cursosatendimentos.mapper.CursosAtendimentosMapper;
import br.com.g3.cursosatendimentos.repository.CursoAtendimentoPresencaAnexoRepository;
import br.com.g3.cursosatendimentos.repository.CursoAtendimentoPresencaDataRepository;
import br.com.g3.cursosatendimentos.repository.CursoAtendimentoPresencaRepository;
import br.com.g3.cursosatendimentos.repository.CursoAtendimentoRepository;
import br.com.g3.cursosatendimentos.service.ArmazenamentoPresencaAnexoService;
import br.com.g3.cursosatendimentos.service.CursoAtendimentoService;
import br.com.g3.unidadeassistencial.domain.SalaUnidade;
import br.com.g3.unidadeassistencial.repository.SalaUnidadeRepository;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CursoAtendimentoServiceImpl implements CursoAtendimentoService {
  private static final String STATUS_PRESENCA_DATA_GERADA = "GERADA";
  private static final String STATUS_PRESENCA_DATA_PREENCHIDA = "PREENCHIDA";
  private static final String STATUS_PRESENCA_DATA_CANCELADA = "CANCELADA";
  private final CursoAtendimentoRepository repository;
  private final CursoAtendimentoPresencaRepository presencaRepository;
  private final CursoAtendimentoPresencaDataRepository presencaDataRepository;
  private final CursoAtendimentoPresencaAnexoRepository presencaAnexoRepository;
  private final ArmazenamentoPresencaAnexoService armazenamentoPresencaAnexoService;
  private final SalaUnidadeRepository salaRepository;

  public CursoAtendimentoServiceImpl(
      CursoAtendimentoRepository repository,
      CursoAtendimentoPresencaRepository presencaRepository,
      CursoAtendimentoPresencaDataRepository presencaDataRepository,
      CursoAtendimentoPresencaAnexoRepository presencaAnexoRepository,
      ArmazenamentoPresencaAnexoService armazenamentoPresencaAnexoService,
      SalaUnidadeRepository salaRepository) {
    this.repository = repository;
    this.presencaRepository = presencaRepository;
    this.presencaDataRepository = presencaDataRepository;
    this.presencaAnexoRepository = presencaAnexoRepository;
    this.armazenamentoPresencaAnexoService = armazenamentoPresencaAnexoService;
    this.salaRepository = salaRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<CursoAtendimentoResponse> listar() {
    return repository.listar().stream()
        .sorted(Comparator.comparing(CursoAtendimento::getCriadoEm).reversed())
        .map(CursosAtendimentosMapper::toResponse)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public CursoAtendimentoResponse criar(CursoAtendimentoRequest request) {
    SalaUnidade sala = buscarSala(request.getSalaId());
    CursoAtendimento curso = CursosAtendimentosMapper.toDomain(request, sala);
    if (curso.getStatus() == null || curso.getStatus().trim().isEmpty()) {
      curso.setStatus("TRIAGEM");
      if (curso.getDataTriagem() == null) {
        curso.setDataTriagem(LocalDate.now());
      }
    }
    LocalDateTime agora = LocalDateTime.now();
    curso.setCriadoEm(agora);
    curso.setAtualizadoEm(agora);
    curso.setVagasDisponiveis(calcularVagasDisponiveis(curso.getVagasTotais(), curso.getMatriculas()));
    CursoAtendimento salvo = repository.salvar(curso);
    return CursosAtendimentosMapper.toResponse(salvo);
  }

  @Override
  @Transactional
  public CursoAtendimentoResponse atualizar(Long id, CursoAtendimentoRequest request) {
    CursoAtendimento curso =
        repository.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Curso nao encontrado."));
    SalaUnidade sala = buscarSala(request.getSalaId());
    CursosAtendimentosMapper.apply(curso, request, sala);
    curso.setAtualizadoEm(LocalDateTime.now());
    curso.setVagasDisponiveis(calcularVagasDisponiveis(curso.getVagasTotais(), curso.getMatriculas()));
    CursoAtendimento salvo = repository.salvar(curso);
    return CursosAtendimentosMapper.toResponse(salvo);
  }

  @Override
  @Transactional
  public CursoAtendimentoResponse atualizarStatus(Long id, CursoAtendimentoStatusRequest request) {
    CursoAtendimento curso =
        repository.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Curso nao encontrado."));
    if (request.getStatus() == null || request.getStatus().trim().isEmpty()) {
      throw new IllegalArgumentException("Status nao informado.");
    }
    curso.setStatus(request.getStatus());
    atualizarDatasPorStatus(curso, request.getStatus());
    CursoAtendimentoStatus historico = new CursoAtendimentoStatus();
    historico.setCursoAtendimento(curso);
    historico.setStatus(request.getStatus());
    historico.setJustificativa(request.getJustificativa());
    historico.setDataAlteracao(LocalDateTime.now());
    curso.getHistoricoStatus().add(historico);
    curso.setAtualizadoEm(LocalDateTime.now());
    CursoAtendimento salvo = repository.salvar(curso);
    return CursosAtendimentosMapper.toResponse(salvo);
  }

  @Override
  public void remover(Long id) {
    CursoAtendimento curso =
        repository.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Curso nao encontrado."));
    repository.remover(curso);
  }

  @Override
  @Transactional(readOnly = true)
  public CursoAtendimentoPresencaResponse listarPresencas(Long cursoId, LocalDate dataAula) {
    if (dataAula == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data da aula nao informada.");
    }
    CursoAtendimento curso = buscarCurso(cursoId);
    List<CursoAtendimentoPresenca> registros =
        presencaRepository.listarPorCursoEData(curso.getId(), dataAula);
    List<CursoAtendimentoPresencaItemResponse> itens =
        registros.stream().map(this::mapearPresenca).collect(Collectors.toList());
    return new CursoAtendimentoPresencaResponse(dataAula, itens);
  }

  @Override
  @Transactional
  public CursoAtendimentoPresencaResponse salvarPresencas(
      Long cursoId, CursoAtendimentoPresencaRequest request) {
    if (request == null || request.getDataAula() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data da aula nao informada.");
    }
    CursoAtendimento curso = buscarCurso(cursoId);
    CursoAtendimentoPresencaData presencaData =
        presencaDataRepository
            .buscarPorCursoEData(curso.getId(), request.getDataAula())
            .orElseGet(() -> criarPresencaDataInterna(curso, request.getDataAula(), null));
    validarPresencaDataAtiva(presencaData);
    List<CursoAtendimentoMatricula> matriculas = curso.getMatriculas();
    for (CursoAtendimentoPresencaItemRequest item : request.getPresencas()) {
      if (item == null || item.getMatriculaId() == null) continue;
      Long matriculaId = parseId(item.getMatriculaId());
      CursoAtendimentoMatricula matricula =
          matriculas.stream()
              .filter((mat) -> mat.getId() != null && mat.getId().equals(matriculaId))
              .findFirst()
              .orElse(null);
      if (matricula == null) continue;
      String status = normalizarStatusPresenca(item.getStatus());
      CursoAtendimentoPresenca presenca =
          presencaRepository
              .buscarPorCursoMatriculaData(curso.getId(), matriculaId, request.getDataAula())
              .orElseGet(CursoAtendimentoPresenca::new);
      LocalDateTime agora = LocalDateTime.now();
      if (presenca.getId() == null) {
        presenca.setCriadoEm(agora);
        presenca.setCursoAtendimento(curso);
        presenca.setMatricula(matricula);
        presenca.setDataAula(request.getDataAula());
      }
      presenca.setStatus(status);
      presenca.setAtualizadoEm(agora);
      presencaRepository.salvar(presenca);
    }
    presencaData.setStatus(STATUS_PRESENCA_DATA_PREENCHIDA);
    presencaData.setAtualizadoEm(LocalDateTime.now());
    presencaDataRepository.salvar(presencaData);
    return listarPresencas(curso.getId(), request.getDataAula());
  }

  @Override
  @Transactional
  public CursoAtendimentoPresencaDataResponse criarPresencaData(
      Long cursoId, CursoAtendimentoPresencaDataRequest request) {
    if (request == null || request.getDataAula() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data da aula nao informada.");
    }
    CursoAtendimento curso = buscarCurso(cursoId);
    CursoAtendimentoPresencaData presencaData =
        presencaDataRepository
            .buscarPorCursoEData(curso.getId(), request.getDataAula())
            .orElseGet(() -> criarPresencaDataInterna(curso, request.getDataAula(), request.getObservacoes()));
    if (request.getObservacoes() != null) {
      presencaData.setObservacoes(request.getObservacoes());
      presencaData.setAtualizadoEm(LocalDateTime.now());
      presencaDataRepository.salvar(presencaData);
    }
    return mapearPresencaData(presencaData);
  }

  @Override
  @Transactional(readOnly = true)
  public CursoAtendimentoPresencaDataListaResponse listarPresencaDatas(
      Long cursoId, Boolean somentePendentes) {
    CursoAtendimento curso = buscarCurso(cursoId);
    List<CursoAtendimentoPresencaDataResponse> datas =
        presencaDataRepository.listarPorCurso(curso.getId()).stream()
            .filter(
                (registro) ->
                    somentePendentes == null
                        || !somentePendentes
                        || STATUS_PRESENCA_DATA_GERADA.equalsIgnoreCase(registro.getStatus()))
            .map(this::mapearPresencaData)
            .collect(Collectors.toList());
    return new CursoAtendimentoPresencaDataListaResponse(datas);
  }

  @Override
  @Transactional(readOnly = true)
  public CursoAtendimentoPresencaDataResponse obterPresencaData(Long cursoId, Long presencaDataId) {
    CursoAtendimentoPresencaData presencaData = buscarPresencaData(cursoId, presencaDataId);
    return mapearPresencaData(presencaData);
  }

  @Override
  @Transactional
  public CursoAtendimentoPresencaDataResponse atualizarPresencaData(
      Long cursoId, Long presencaDataId, CursoAtendimentoPresencaDataRequest request) {
    if (request == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados da presenca nao informados.");
    }
    CursoAtendimentoPresencaData presencaData = buscarPresencaData(cursoId, presencaDataId);
    if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
      String status = request.getStatus().trim().toUpperCase();
      if (!status.equals(STATUS_PRESENCA_DATA_GERADA)
          && !status.equals(STATUS_PRESENCA_DATA_PREENCHIDA)
          && !status.equals(STATUS_PRESENCA_DATA_CANCELADA)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status invalido para presenca.");
      }
      if (STATUS_PRESENCA_DATA_CANCELADA.equalsIgnoreCase(presencaData.getStatus())
          && !STATUS_PRESENCA_DATA_CANCELADA.equals(status)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data cancelada nao pode ser reaberta.");
      }
      presencaData.setStatus(status);
    }
    if (request.getObservacoes() != null) {
      presencaData.setObservacoes(request.getObservacoes());
    }
    presencaData.setAtualizadoEm(LocalDateTime.now());
    presencaDataRepository.salvar(presencaData);
    return mapearPresencaData(presencaData);
  }

  @Override
  @Transactional
  public void removerPresencaData(Long cursoId, Long presencaDataId) {
    CursoAtendimentoPresencaData presencaData = buscarPresencaData(cursoId, presencaDataId);
    presencaRepository.removerPorCursoEData(
        presencaData.getCursoAtendimento().getId(), presencaData.getDataAula());
    presencaDataRepository.remover(presencaData);
  }

  @Override
  @Transactional(readOnly = true)
  public CursoAtendimentoPresencaResponse listarPresencasPorDataId(
      Long cursoId, Long presencaDataId) {
    CursoAtendimentoPresencaData presencaData = buscarPresencaData(cursoId, presencaDataId);
    return listarPresencas(cursoId, presencaData.getDataAula());
  }

  @Override
  @Transactional
  public CursoAtendimentoPresencaResponse salvarPresencasPorDataId(
      Long cursoId, Long presencaDataId, CursoAtendimentoPresencaRequest request) {
    if (request == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados da presenca nao informados.");
    }
    CursoAtendimentoPresencaData presencaData = buscarPresencaData(cursoId, presencaDataId);
    validarPresencaDataAtiva(presencaData);
    request.setDataAula(presencaData.getDataAula());
    CursoAtendimentoPresencaResponse response = salvarPresencas(cursoId, request);
    presencaData.setStatus(STATUS_PRESENCA_DATA_PREENCHIDA);
    presencaData.setAtualizadoEm(LocalDateTime.now());
    presencaDataRepository.salvar(presencaData);
    return response;
  }

  @Override
  @Transactional(readOnly = true)
  public List<CursoAtendimentoPresencaAnexoResponse> listarPresencaAnexos(
      Long cursoId, Long presencaDataId) {
    CursoAtendimentoPresencaData presencaData = buscarPresencaData(cursoId, presencaDataId);
    return presencaAnexoRepository.listarPorPresencaData(presencaData.getId()).stream()
        .map(this::mapearPresencaAnexo)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public CursoAtendimentoPresencaAnexoResponse adicionarPresencaAnexo(
      Long cursoId, Long presencaDataId, CursoAtendimentoPresencaAnexoRequest request) {
    CursoAtendimentoPresencaData presencaData = buscarPresencaData(cursoId, presencaDataId);
    validarPresencaDataAtiva(presencaData);
    if (request == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do anexo nao informados.");
    }
    String caminhoArquivo = armazenamentoPresencaAnexoService.salvarArquivo(presencaData.getId(), request);
    CursoAtendimentoPresencaAnexo anexo = new CursoAtendimentoPresencaAnexo();
    anexo.setPresencaData(presencaData);
    anexo.setNomeArquivo(request.getNomeArquivo());
    anexo.setTipoMime(request.getTipoMime());
    anexo.setTamanho(request.getTamanho());
    anexo.setCaminhoArquivo(caminhoArquivo);
    anexo.setDataUpload(request.getDataUpload() != null ? request.getDataUpload() : LocalDate.now());
    anexo.setUsuario(request.getUsuario());
    anexo.setCriadoEm(LocalDateTime.now());
    CursoAtendimentoPresencaAnexo salvo = presencaAnexoRepository.salvar(anexo);
    presencaData.setAtualizadoEm(LocalDateTime.now());
    presencaDataRepository.salvar(presencaData);
    return mapearPresencaAnexo(salvo);
  }

  @Override
  public Resource obterPresencaAnexoArquivo(Long cursoId, Long presencaDataId, Long anexoId) {
    CursoAtendimentoPresencaData presencaData = buscarPresencaData(cursoId, presencaDataId);
    CursoAtendimentoPresencaAnexo anexo =
        presencaAnexoRepository
            .buscarPorId(anexoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anexo nao encontrado."));
    if (anexo.getPresencaData() == null
        || anexo.getPresencaData().getId() == null
        || !anexo.getPresencaData().getId().equals(presencaData.getId())) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Anexo nao encontrado.");
    }
    if (anexo.getCaminhoArquivo() == null || anexo.getCaminhoArquivo().trim().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo nao encontrado.");
    }
    try {
      Path caminho = Paths.get(anexo.getCaminhoArquivo());
      Resource resource = new UrlResource(caminho.toUri());
      if (!resource.exists()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo nao encontrado.");
      }
      return resource;
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo nao encontrado.");
    }
  }

  private SalaUnidade buscarSala(Long salaId) {
    if (salaId == null) return null;
    Optional<SalaUnidade> sala = salaRepository.buscarPorId(salaId);
    if (!sala.isPresent()) {
      throw new IllegalArgumentException("Sala nao encontrada.");
    }
    return sala.get();
  }

  private int calcularVagasDisponiveis(Integer vagasTotais, List<CursoAtendimentoMatricula> matriculas) {
    int total = vagasTotais == null ? 0 : vagasTotais;
    long ocupadas =
        matriculas == null
            ? 0
            : matriculas.stream()
                .filter(Objects::nonNull)
                .filter((matricula) -> "Ativo".equalsIgnoreCase(matricula.getStatus()))
                .count();
    int disponiveis = total - (int) ocupadas;
    return Math.max(disponiveis, 0);
  }

  private void atualizarDatasPorStatus(CursoAtendimento curso, String status) {
    if ("TRIAGEM".equalsIgnoreCase(status) && curso.getDataTriagem() == null) {
      curso.setDataTriagem(LocalDate.now());
    }
    if ("ENCAMINHADO".equalsIgnoreCase(status) && curso.getDataEncaminhamento() == null) {
      curso.setDataEncaminhamento(LocalDate.now());
    }
    if ("CONCLUIDO".equalsIgnoreCase(status) && curso.getDataConclusao() == null) {
      curso.setDataConclusao(LocalDate.now());
    }
  }

  private CursoAtendimentoPresencaItemResponse mapearPresenca(CursoAtendimentoPresenca presenca) {
    CursoAtendimentoPresencaItemResponse response = new CursoAtendimentoPresencaItemResponse();
    response.setMatriculaId(
        presenca.getMatricula() == null || presenca.getMatricula().getId() == null
            ? null
            : presenca.getMatricula().getId().toString());
    response.setStatus(presenca.getStatus());
    return response;
  }

  private CursoAtendimentoPresencaDataResponse mapearPresencaData(CursoAtendimentoPresencaData presencaData) {
    CursoAtendimentoPresencaDataResponse response = new CursoAtendimentoPresencaDataResponse();
    response.setId(presencaData.getId());
    response.setDataAula(presencaData.getDataAula());
    response.setStatus(presencaData.getStatus());
    response.setObservacoes(presencaData.getObservacoes());
    response.setCriadoEm(presencaData.getCriadoEm());
    response.setAtualizadoEm(presencaData.getAtualizadoEm());
    int totalPresencas =
        presencaRepository
            .listarPorCursoEData(presencaData.getCursoAtendimento().getId(), presencaData.getDataAula())
            .size();
    int totalAnexos = presencaAnexoRepository.listarPorPresencaData(presencaData.getId()).size();
    response.setTotalPresencas(totalPresencas);
    response.setTotalAnexos(totalAnexos);
    return response;
  }

  private CursoAtendimentoPresencaAnexoResponse mapearPresencaAnexo(CursoAtendimentoPresencaAnexo anexo) {
    CursoAtendimentoPresencaAnexoResponse response = new CursoAtendimentoPresencaAnexoResponse();
    response.setId(anexo.getId());
    response.setPresencaDataId(anexo.getPresencaData() != null ? anexo.getPresencaData().getId() : null);
    response.setNomeArquivo(anexo.getNomeArquivo());
    response.setTipoMime(anexo.getTipoMime());
    response.setTamanho(anexo.getTamanho());
    if (anexo.getId() != null
        && anexo.getPresencaData() != null
        && anexo.getPresencaData().getId() != null
        && anexo.getPresencaData().getCursoAtendimento() != null
        && anexo.getPresencaData().getCursoAtendimento().getId() != null
        && anexo.getCaminhoArquivo() != null
        && !anexo.getCaminhoArquivo().trim().isEmpty()) {
      response.setArquivoUrl(
          "/api/cursos-atendimentos/"
              + anexo.getPresencaData().getCursoAtendimento().getId()
              + "/presencas/datas/"
              + anexo.getPresencaData().getId()
              + "/anexos/"
              + anexo.getId()
              + "/arquivo");
    }
    response.setDataUpload(anexo.getDataUpload());
    response.setUsuario(anexo.getUsuario());
    response.setCriadoEm(anexo.getCriadoEm());
    return response;
  }

  private String normalizarStatusPresenca(String status) {
    if (status == null) return "AUSENTE";
    String upper = status.toUpperCase();
    return "PRESENTE".equals(upper) ? "PRESENTE" : "AUSENTE";
  }

  private Long parseId(String valor) {
    try {
      return Long.parseLong(valor);
    } catch (NumberFormatException ex) {
      return null;
    }
  }

  private CursoAtendimento buscarCurso(Long cursoId) {
    return repository
        .buscarPorId(cursoId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  private CursoAtendimentoPresencaData buscarPresencaData(Long cursoId, Long presencaDataId) {
    CursoAtendimentoPresencaData presencaData =
        presencaDataRepository
            .buscarPorId(presencaDataId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data nao encontrada."));
    if (presencaData.getCursoAtendimento() == null
        || presencaData.getCursoAtendimento().getId() == null
        || !presencaData.getCursoAtendimento().getId().equals(cursoId)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data nao encontrada.");
    }
    return presencaData;
  }

  private CursoAtendimentoPresencaData criarPresencaDataInterna(
      CursoAtendimento curso, LocalDate dataAula, String observacoes) {
    LocalDateTime agora = LocalDateTime.now();
    CursoAtendimentoPresencaData presencaData = new CursoAtendimentoPresencaData();
    presencaData.setCursoAtendimento(curso);
    presencaData.setDataAula(dataAula);
    presencaData.setStatus(STATUS_PRESENCA_DATA_GERADA);
    presencaData.setObservacoes(observacoes);
    presencaData.setCriadoEm(agora);
    presencaData.setAtualizadoEm(agora);
    return presencaDataRepository.salvar(presencaData);
  }

  private void validarPresencaDataAtiva(CursoAtendimentoPresencaData presencaData) {
    if (STATUS_PRESENCA_DATA_CANCELADA.equalsIgnoreCase(presencaData.getStatus())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data cancelada nao pode receber presenca.");
    }
  }
}
