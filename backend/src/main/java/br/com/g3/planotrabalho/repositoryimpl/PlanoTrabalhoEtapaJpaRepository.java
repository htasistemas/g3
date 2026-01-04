package br.com.g3.planotrabalho.repositoryimpl;

import br.com.g3.planotrabalho.domain.PlanoTrabalhoEtapa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface PlanoTrabalhoEtapaJpaRepository extends JpaRepository<PlanoTrabalhoEtapa, Long> {
  List<PlanoTrabalhoEtapa> findAllByAtividadeId(Long atividadeId);

  @Modifying
  @Transactional
  void deleteByAtividadeId(Long atividadeId);
}
