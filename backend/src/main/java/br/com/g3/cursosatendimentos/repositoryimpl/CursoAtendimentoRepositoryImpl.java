package br.com.g3.cursosatendimentos.repositoryimpl;

import br.com.g3.cursosatendimentos.domain.CursoAtendimento;
import br.com.g3.cursosatendimentos.repository.CursoAtendimentoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CursoAtendimentoRepositoryImpl implements CursoAtendimentoRepository {
  private final CursoAtendimentoJpaRepository jpaRepository;

  public CursoAtendimentoRepositoryImpl(CursoAtendimentoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public CursoAtendimento salvar(CursoAtendimento curso) {
    return jpaRepository.save(curso);
  }

  @Override
  public List<CursoAtendimento> listar() {
    return jpaRepository.findAll();
  }

  @Override
  public Optional<CursoAtendimento> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public void remover(CursoAtendimento curso) {
    jpaRepository.delete(curso);
  }
}
