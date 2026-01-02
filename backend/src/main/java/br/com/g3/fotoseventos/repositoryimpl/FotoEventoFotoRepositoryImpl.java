package br.com.g3.fotoseventos.repositoryimpl;

import br.com.g3.fotoseventos.domain.FotoEventoFoto;
import br.com.g3.fotoseventos.repository.FotoEventoFotoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class FotoEventoFotoRepositoryImpl implements FotoEventoFotoRepository {
  private final FotoEventoFotoJpaRepository jpaRepository;

  public FotoEventoFotoRepositoryImpl(FotoEventoFotoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public FotoEventoFoto salvar(FotoEventoFoto foto) {
    return jpaRepository.save(foto);
  }

  @Override
  public List<FotoEventoFoto> listarPorEvento(Long eventoId) {
    return jpaRepository.findAllByEventoIdOrderByOrdemAscCriadoEmAsc(eventoId);
  }

  @Override
  public Optional<FotoEventoFoto> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public Optional<FotoEventoFoto> buscarPorEvento(Long eventoId, Long fotoId) {
    return jpaRepository.findByIdAndEventoId(fotoId, eventoId);
  }

  @Override
  public long contarPorEvento(Long eventoId) {
    return jpaRepository.countByEventoId(eventoId);
  }

  @Override
  public void remover(FotoEventoFoto foto) {
    jpaRepository.delete(foto);
  }
}
