package br.com.g3.contabilidade.repositoryimpl;

import br.com.g3.contabilidade.domain.EmendaImpositiva;
import br.com.g3.contabilidade.repository.EmendaImpositivaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class EmendaImpositivaRepositoryImpl implements EmendaImpositivaRepository {
  private final EmendaImpositivaJpaRepository jpaRepository;

  public EmendaImpositivaRepositoryImpl(EmendaImpositivaJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public EmendaImpositiva salvar(EmendaImpositiva emenda) {
    return jpaRepository.save(emenda);
  }

  @Override
  public List<EmendaImpositiva> listar() {
    return jpaRepository.findAll();
  }

  @Override
  public Optional<EmendaImpositiva> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }
}
