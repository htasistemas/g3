package br.com.g3.cursosatendimentos.repositoryimpl;

import br.com.g3.cursosatendimentos.domain.CursoAtendimentoPresenca;
import br.com.g3.cursosatendimentos.repository.CursoAtendimentoPresencaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CursoAtendimentoPresencaRepositoryImpl implements CursoAtendimentoPresencaRepository {
  private final CursoAtendimentoPresencaJpaRepository jpaRepository;

  public CursoAtendimentoPresencaRepositoryImpl(CursoAtendimentoPresencaJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public CursoAtendimentoPresenca salvar(CursoAtendimentoPresenca presenca) {
    return jpaRepository.save(presenca);
  }

  @Override
  public List<CursoAtendimentoPresenca> listarPorCursoEData(Long cursoId, LocalDate dataAula) {
    return jpaRepository.findByCursoAtendimentoIdAndDataAula(cursoId, dataAula);
  }

  @Override
  public Optional<CursoAtendimentoPresenca> buscarPorCursoMatriculaData(
      Long cursoId, Long matriculaId, LocalDate dataAula) {
    return jpaRepository.findByCursoAtendimentoIdAndMatriculaIdAndDataAula(
        cursoId, matriculaId, dataAula);
  }
}
