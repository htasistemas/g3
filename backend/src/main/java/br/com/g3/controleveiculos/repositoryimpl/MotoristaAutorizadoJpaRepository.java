package br.com.g3.controleveiculos.repositoryimpl;

import br.com.g3.controleveiculos.domain.MotoristaAutorizado;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotoristaAutorizadoJpaRepository extends JpaRepository<MotoristaAutorizado, Long> {
  List<MotoristaAutorizado> findByVeiculoIdOrderByNomeMotoristaAscIdAsc(Long veiculoId);

  List<MotoristaAutorizado> findAllByOrderByNomeMotoristaAscIdAsc();

  Optional<MotoristaAutorizado>
      findByVeiculoIdAndTipoOrigemAndProfissionalIdAndVoluntarioId(
          Long veiculoId, String tipoOrigem, Long profissionalId, Long voluntarioId);
}
