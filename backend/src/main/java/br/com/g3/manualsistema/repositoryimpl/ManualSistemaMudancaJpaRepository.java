package br.com.g3.manualsistema.repositoryimpl;

import br.com.g3.manualsistema.domain.ManualSistemaMudanca;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ManualSistemaMudancaJpaRepository extends JpaRepository<ManualSistemaMudanca, Long> {
  List<ManualSistemaMudanca> findAllByOrderByDataMudancaDesc(Pageable pageable);

  @Query("select max(m.dataMudanca) from ManualSistemaMudanca m")
  java.time.LocalDateTime buscarUltimaMudanca();
}
