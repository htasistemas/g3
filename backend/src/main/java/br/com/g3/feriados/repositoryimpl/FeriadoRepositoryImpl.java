package br.com.g3.feriados.repositoryimpl;

import br.com.g3.feriados.domain.Feriado;
import br.com.g3.feriados.repository.FeriadoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class FeriadoRepositoryImpl implements FeriadoRepository {
  private final FeriadoJpaRepository jpaRepository;

  public FeriadoRepositoryImpl(FeriadoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Feriado salvar(Feriado feriado) {
    return jpaRepository.save(feriado);
  }

  @Override
  public List<Feriado> listar() {
    return jpaRepository.findAllByOrderByDataAscIdAsc();
  }

  @Override
  public Optional<Feriado> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public void remover(Feriado feriado) {
    jpaRepository.delete(feriado);
  }
}
