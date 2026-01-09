package br.com.g3.almoxarifado.repositoryimpl;

import br.com.g3.almoxarifado.domain.MovimentacaoVinculoKit;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentacaoVinculoKitJpaRepository extends JpaRepository<MovimentacaoVinculoKit, Long> {
  List<MovimentacaoVinculoKit> findByMovimentacaoPrincipalId(Long movimentacaoPrincipalId);
}
