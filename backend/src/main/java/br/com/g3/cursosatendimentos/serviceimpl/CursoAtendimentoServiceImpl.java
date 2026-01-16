package br.com.g3.cursosatendimentos.serviceimpl;

import br.com.g3.cursosatendimentos.domain.CursoAtendimento;
import br.com.g3.cursosatendimentos.domain.CursoAtendimentoMatricula;
import br.com.g3.cursosatendimentos.domain.CursoAtendimentoPresenca;
import br.com.g3.cursosatendimentos.domain.CursoAtendimentoStatus;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaItemRequest;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaItemResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaRequest;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoRequest;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoStatusRequest;
import br.com.g3.cursosatendimentos.mapper.CursosAtendimentosMapper;
import br.com.g3.cursosatendimentos.repository.CursoAtendimentoPresencaRepository;
import br.com.g3.cursosatendimentos.repository.CursoAtendimentoRepository;
import br.com.g3.cursosatendimentos.service.CursoAtendimentoService;
import br.com.g3.unidadeassistencial.domain.SalaUnidade;
import br.com.g3.unidadeassistencial.repository.SalaUnidadeRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CursoAtendimentoServiceImpl implements CursoAtendimentoService {
  private final CursoAtendimentoRepository repository;
  private final CursoAtendimentoPresencaRepository presencaRepository;
  private final SalaUnidadeRepository salaRepository;

  public CursoAtendimentoServiceImpl(
      CursoAtendimentoRepository repository,
      CursoAtendimentoPresencaRepository presencaRepository,
      SalaUnidadeRepository salaRepository) {
    this.repository = repository;
    this.presencaRepository = presencaRepository;
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
    CursoAtendimento curso =
        repository.buscarPorId(cursoId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
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
    CursoAtendimento curso =
        repository.buscarPorId(cursoId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
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
    return listarPresencas(curso.getId(), request.getDataAula());
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
}
