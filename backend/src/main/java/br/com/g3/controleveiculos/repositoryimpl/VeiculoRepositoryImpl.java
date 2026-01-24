package br.com.g3.controleveiculos.repositoryimpl;

import br.com.g3.controleveiculos.domain.Veiculo;
import br.com.g3.controleveiculos.repository.VeiculoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class VeiculoRepositoryImpl implements VeiculoRepository {
  private final VeiculoJpaRepository repositorioJpa;

  public VeiculoRepositoryImpl(VeiculoJpaRepository repositorioJpa) {
    this.repositorioJpa = repositorioJpa;
  }

  @Override
  public Veiculo salvar(Veiculo veiculo) {
    return repositorioJpa.save(veiculo);
  }

  @Override
  public Optional<Veiculo> buscarPorId(Long id) {
    return repositorioJpa.findById(id);
  }

  @Override
  public Optional<Veiculo> buscarPorPlaca(String placa) {
    return repositorioJpa.findByPlacaIgnoreCase(placa);
  }

  @Override
  public List<Veiculo> listar() {
    return repositorioJpa.findAllByOrderByPlacaAscIdAsc();
  }

  @Override
  public void remover(Veiculo veiculo) {
    repositorioJpa.delete(veiculo);
  }
}
