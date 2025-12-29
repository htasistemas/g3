package br.com.g3.doacaorealizada.repositoryimpl;

import br.com.g3.doacaorealizada.domain.DoacaoRealizada;
import br.com.g3.doacaorealizada.repository.DoacaoRealizadaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class DoacaoRealizadaRepositoryImpl implements DoacaoRealizadaRepository {
  private final DoacaoRealizadaJpaRepository jpaRepository;

  public DoacaoRealizadaRepositoryImpl(DoacaoRealizadaJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public DoacaoRealizada salvar(DoacaoRealizada doacao) {
    return jpaRepository.save(doacao);
  }

  @Override
  public List<DoacaoRealizada> listar() {
    return jpaRepository.findAll();
  }

  @Override
  public Optional<DoacaoRealizada> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }
}
