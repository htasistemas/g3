package br.com.g3.fotoseventos.repositoryimpl;

import br.com.g3.fotoseventos.domain.FotoEvento;
import br.com.g3.fotoseventos.repository.FotoEventoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class FotoEventoRepositoryImpl implements FotoEventoRepository {
  private final FotoEventoJpaRepository jpaRepository;

  public FotoEventoRepositoryImpl(FotoEventoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public FotoEvento salvar(FotoEvento evento) {
    return jpaRepository.save(evento);
  }

  @Override
  public Optional<FotoEvento> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public Page<FotoEvento> listarComFiltros(
      String busca,
      LocalDate dataInicio,
      LocalDate dataFim,
      Long unidadeId,
      br.com.g3.fotoseventos.domain.StatusFotoEvento status,
      List<String> tags,
      Pageable pageable) {
    return jpaRepository.buscarComFiltros(busca, dataInicio, dataFim, unidadeId, status, tags, pageable);
  }

  @Override
  public Page<FotoEvento> listarComFiltrosOrdenadoPorFotos(
      String busca,
      LocalDate dataInicio,
      LocalDate dataFim,
      Long unidadeId,
      br.com.g3.fotoseventos.domain.StatusFotoEvento status,
      List<String> tags,
      Pageable pageable) {
    return jpaRepository.buscarComFiltrosOrdenadoPorFotos(
        busca, dataInicio, dataFim, unidadeId, status, tags, pageable);
  }

  @Override
  public void remover(FotoEvento evento) {
    jpaRepository.delete(evento);
  }
}
