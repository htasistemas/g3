package br.com.g3.unidadeassistencial.repositoryimpl;

import br.com.g3.unidadeassistencial.domain.UnidadeAssistencial;
import br.com.g3.unidadeassistencial.repository.UnidadeAssistencialRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UnidadeAssistencialRepositoryImpl implements UnidadeAssistencialRepository {
  private final UnidadeAssistencialJpaRepository jpaRepository;

  public UnidadeAssistencialRepositoryImpl(UnidadeAssistencialJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public UnidadeAssistencial salvar(UnidadeAssistencial unidade) {
    return jpaRepository.save(unidade);
  }

  @Override
  public List<UnidadeAssistencial> listar() {
    return jpaRepository.findAll();
  }

  @Override
  public Optional<UnidadeAssistencial> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public Optional<UnidadeAssistencial> buscarAtual() {
    return jpaRepository.findTopByOrderByAtualizadoEmDesc();
  }

  @Override
  public void limparUnidadePrincipal() {
    jpaRepository.limparUnidadePrincipal();
  }

  @Override
  public void remover(UnidadeAssistencial unidade) {
    jpaRepository.delete(unidade);
  }
}
