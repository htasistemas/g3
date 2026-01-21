package br.com.g3.documentosinstituicao.repositoryimpl;

import br.com.g3.documentosinstituicao.domain.DocumentoInstituicao;
import br.com.g3.documentosinstituicao.repository.DocumentoInstituicaoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class DocumentoInstituicaoRepositoryImpl implements DocumentoInstituicaoRepository {
  private final DocumentoInstituicaoJpaRepository jpaRepository;

  public DocumentoInstituicaoRepositoryImpl(DocumentoInstituicaoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public DocumentoInstituicao salvar(DocumentoInstituicao documento) {
    return jpaRepository.save(documento);
  }

  @Override
  public List<DocumentoInstituicao> listar() {
    return jpaRepository.findAllByOrderByAtualizadoEmDescIdDesc();
  }

  @Override
  public Optional<DocumentoInstituicao> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public void remover(DocumentoInstituicao documento) {
    jpaRepository.delete(documento);
  }
}
