package br.com.g3.recebimentodoacao.repositoryimpl;

import br.com.g3.recebimentodoacao.domain.RecebimentoDoacao;
import br.com.g3.recebimentodoacao.repository.RecebimentoDoacaoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class RecebimentoDoacaoRepositoryImpl implements RecebimentoDoacaoRepository {
  private final RecebimentoDoacaoJpaRepository jpaRepository;

  public RecebimentoDoacaoRepositoryImpl(RecebimentoDoacaoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public RecebimentoDoacao salvar(RecebimentoDoacao recebimento) {
    return jpaRepository.save(recebimento);
  }

  @Override
  public List<RecebimentoDoacao> listar() {
    return jpaRepository.findAll();
  }

  @Override
  public Optional<RecebimentoDoacao> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public void remover(RecebimentoDoacao recebimento) {
    jpaRepository.delete(recebimento);
  }
}
