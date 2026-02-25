package br.com.g3.manualsistema.repositoryimpl;

import br.com.g3.manualsistema.domain.ManualSistemaMudanca;
import br.com.g3.manualsistema.repository.ManualSistemaMudancaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
public class ManualSistemaMudancaRepositoryImpl implements ManualSistemaMudancaRepository {
  private final ManualSistemaMudancaJpaRepository jpaRepository;

  public ManualSistemaMudancaRepositoryImpl(ManualSistemaMudancaJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public ManualSistemaMudanca salvar(ManualSistemaMudanca mudanca) {
    return jpaRepository.save(mudanca);
  }

  @Override
  public List<ManualSistemaMudanca> listarRecentes(int limite) {
    return jpaRepository.findAllByOrderByDataMudancaDesc(PageRequest.of(0, limite));
  }

  @Override
  public Optional<LocalDateTime> buscarUltimaMudanca() {
    return Optional.ofNullable(jpaRepository.buscarUltimaMudanca());
  }
}
