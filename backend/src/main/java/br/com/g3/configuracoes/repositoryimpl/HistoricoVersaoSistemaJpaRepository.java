package br.com.g3.configuracoes.repositoryimpl;

import br.com.g3.configuracoes.domain.HistoricoVersaoSistema;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoVersaoSistemaJpaRepository extends JpaRepository<HistoricoVersaoSistema, Long> {
  List<HistoricoVersaoSistema> findAllByOrderByCriadoEmDesc();
}
