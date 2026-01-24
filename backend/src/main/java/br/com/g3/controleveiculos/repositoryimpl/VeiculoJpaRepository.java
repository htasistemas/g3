package br.com.g3.controleveiculos.repositoryimpl;

import br.com.g3.controleveiculos.domain.Veiculo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeiculoJpaRepository extends JpaRepository<Veiculo, Long> {
  List<Veiculo> findAllByOrderByPlacaAscIdAsc();

  Optional<Veiculo> findByPlacaIgnoreCase(String placa);
}
