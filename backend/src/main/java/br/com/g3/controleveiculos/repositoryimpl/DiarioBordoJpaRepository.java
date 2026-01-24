package br.com.g3.controleveiculos.repositoryimpl;

import br.com.g3.controleveiculos.domain.DiarioBordo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiarioBordoJpaRepository extends JpaRepository<DiarioBordo, Long> {
  List<DiarioBordo> findAllByOrderByDataDescIdDesc();
}
