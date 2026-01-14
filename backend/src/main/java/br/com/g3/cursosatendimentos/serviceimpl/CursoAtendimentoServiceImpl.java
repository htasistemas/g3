package br.com.g3.cursosatendimentos.serviceimpl;

import br.com.g3.cursosatendimentos.domain.CursoAtendimento;
import br.com.g3.cursosatendimentos.domain.CursoAtendimentoMatricula;
import br.com.g3.cursosatendimentos.domain.CursoAtendimentoStatus;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoRequest;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoStatusRequest;
import br.com.g3.cursosatendimentos.mapper.CursosAtendimentosMapper;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CursoAtendimentoServiceImpl implements CursoAtendimentoService {
  private final CursoAtendimentoRepository repository;
  private final SalaUnidadeRepository salaRepository;

  public CursoAtendimentoServiceImpl(
      CursoAtendimentoRepository repository, SalaUnidadeRepository salaRepository) {
    this.repository = repository;
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
}
