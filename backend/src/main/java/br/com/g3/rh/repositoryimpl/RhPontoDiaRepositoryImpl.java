package br.com.g3.rh.repositoryimpl;

import br.com.g3.rh.domain.RhPontoDia;
import br.com.g3.rh.repository.RhPontoDiaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class RhPontoDiaRepositoryImpl implements RhPontoDiaRepository {
  private final RhPontoDiaJpaRepository repository;

  public RhPontoDiaRepositoryImpl(RhPontoDiaJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public Optional<RhPontoDia> buscarPorId(Long id) {
    return repository.findById(id);
  }

  @Override
  public Optional<RhPontoDia> buscarPorFuncionarioEData(Long funcionarioId, LocalDate data) {
    return repository.findByFuncionarioIdAndData(funcionarioId, data);
  }

  @Override
  public List<RhPontoDia> listarPorFuncionarioEntreDatas(Long funcionarioId, LocalDate inicio, LocalDate fim) {
    return repository.findAllByFuncionarioIdAndDataBetweenOrderByDataAsc(funcionarioId, inicio, fim);
  }

  @Override
  public RhPontoDia salvar(RhPontoDia pontoDia) {
    return repository.save(pontoDia);
  }
}
