package br.com.g3.oficios.repositoryimpl;

import br.com.g3.oficios.domain.Oficio;
import br.com.g3.oficios.repository.OficioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class OficioRepositoryImpl implements OficioRepository {
  private final OficioJpaRepository jpaRepository;

  public OficioRepositoryImpl(OficioJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Oficio salvar(Oficio oficio) {
    return jpaRepository.save(oficio);
  }

  @Override
  public Optional<Oficio> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public List<Oficio> listar() {
    return jpaRepository.findAll(Sort.by(Sort.Direction.DESC, "data", "id"));   
  }

  @Override
  public String buscarUltimoNumeroPorAno(int ano) {
    return jpaRepository.buscarUltimoNumeroPorAno(String.valueOf(ano));
  }

  @Override
  public void remover(Oficio oficio) {
    jpaRepository.delete(oficio);
  }
}
