package br.com.g3.controleveiculos.repositoryimpl;

import br.com.g3.controleveiculos.domain.MotoristaAutorizado;
import br.com.g3.controleveiculos.repository.MotoristaAutorizadoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MotoristaAutorizadoRepositoryImpl implements MotoristaAutorizadoRepository {
  private final MotoristaAutorizadoJpaRepository repositorioJpa;

  public MotoristaAutorizadoRepositoryImpl(MotoristaAutorizadoJpaRepository repositorioJpa) {
    this.repositorioJpa = repositorioJpa;
  }

  @Override
  public MotoristaAutorizado salvar(MotoristaAutorizado motorista) {
    return repositorioJpa.save(motorista);
  }

  @Override
  public Optional<MotoristaAutorizado> buscarPorId(Long id) {
    return repositorioJpa.findById(id);
  }

  @Override
  public Optional<MotoristaAutorizado> buscarPorDados(
      Long veiculoId, String tipoOrigem, Long profissionalId, Long voluntarioId) {
    return repositorioJpa.findByVeiculoIdAndTipoOrigemAndProfissionalIdAndVoluntarioId(
        veiculoId, tipoOrigem, profissionalId, voluntarioId);
  }

  @Override
  public List<MotoristaAutorizado> listar() {
    return repositorioJpa.findAllByOrderByNomeMotoristaAscIdAsc();
  }

  @Override
  public List<MotoristaAutorizado> listarPorVeiculo(Long veiculoId) {
    return repositorioJpa.findByVeiculoIdOrderByNomeMotoristaAscIdAsc(veiculoId);
  }

  @Override
  public void remover(MotoristaAutorizado motorista) {
    repositorioJpa.delete(motorista);
  }
}
