package br.com.g3.ocorrenciacrianca.repositoryimpl;

import br.com.g3.ocorrenciacrianca.domain.OcorrenciaCrianca;
import br.com.g3.ocorrenciacrianca.repository.OcorrenciaCriancaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class OcorrenciaCriancaRepositoryImpl implements OcorrenciaCriancaRepository {
  private final OcorrenciaCriancaJpaRepository jpaRepository;

  public OcorrenciaCriancaRepositoryImpl(OcorrenciaCriancaJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public OcorrenciaCrianca salvar(OcorrenciaCrianca ocorrencia) {
    return jpaRepository.save(ocorrencia);
  }

  @Override
  public Optional<OcorrenciaCrianca> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public List<OcorrenciaCrianca> listar() {
    return jpaRepository.findAll();
  }

  @Override
  public void remover(Long id) {
    jpaRepository.deleteById(id);
  }
}
