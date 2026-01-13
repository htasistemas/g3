package br.com.g3.oficios.repositoryimpl;

import br.com.g3.oficios.domain.OficioImagem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OficioImagemJpaRepository extends JpaRepository<OficioImagem, Long> {
  List<OficioImagem> findByOficioIdOrderByOrdemAscIdAsc(Long oficioId);
}
