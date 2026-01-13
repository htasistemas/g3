package br.com.g3.oficios.repositoryimpl;

import br.com.g3.oficios.domain.OficioImagem;
import br.com.g3.oficios.repository.OficioImagemRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class OficioImagemRepositoryImpl implements OficioImagemRepository {
  private final OficioImagemJpaRepository jpaRepository;

  public OficioImagemRepositoryImpl(OficioImagemJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public OficioImagem salvar(OficioImagem imagem) {
    return jpaRepository.save(imagem);
  }

  @Override
  public List<OficioImagem> listarPorOficioId(Long oficioId) {
    return jpaRepository.findByOficioIdOrderByOrdemAscIdAsc(oficioId);
  }

  @Override
  public void removerPorId(Long id) {
    jpaRepository.deleteById(id);
  }

  @Override
  public void removerPorOficioId(Long oficioId) {
    List<OficioImagem> imagens = listarPorOficioId(oficioId);
    if (imagens.isEmpty()) {
      return;
    }
    jpaRepository.deleteAll(imagens);
  }
}
