package br.com.g3.cursosatendimentos.repositoryimpl;

import br.com.g3.cursosatendimentos.domain.CursoAtendimentoPresencaData;
import br.com.g3.cursosatendimentos.repository.CursoAtendimentoPresencaDataRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CursoAtendimentoPresencaDataRepositoryImpl implements CursoAtendimentoPresencaDataRepository {
  private final CursoAtendimentoPresencaDataJpaRepository jpaRepository;

  public CursoAtendimentoPresencaDataRepositoryImpl(
      CursoAtendimentoPresencaDataJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public CursoAtendimentoPresencaData salvar(CursoAtendimentoPresencaData presencaData) {
    return jpaRepository.save(presencaData);
  }

  @Override
  public Optional<CursoAtendimentoPresencaData> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public Optional<CursoAtendimentoPresencaData> buscarPorCursoEData(Long cursoId, LocalDate dataAula) {
    return jpaRepository.findByCursoAtendimentoIdAndDataAula(cursoId, dataAula);
  }

  @Override
  public List<CursoAtendimentoPresencaData> listarPorCurso(Long cursoId) {
    return jpaRepository.findAllByCursoAtendimentoIdOrderByDataAulaAscIdAsc(cursoId);
  }

  @Override
  public void remover(CursoAtendimentoPresencaData presencaData) {
    jpaRepository.delete(presencaData);
  }
}
