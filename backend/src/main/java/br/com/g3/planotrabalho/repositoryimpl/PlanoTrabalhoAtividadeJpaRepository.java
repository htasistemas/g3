package br.com.g3.planotrabalho.repositoryimpl;

import br.com.g3.planotrabalho.domain.PlanoTrabalhoAtividade;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface PlanoTrabalhoAtividadeJpaRepository
    extends JpaRepository<PlanoTrabalhoAtividade, Long> {
  List<PlanoTrabalhoAtividade> findAllByMetaId(Long metaId);

  @Modifying
  @Transactional
  void deleteByMetaId(Long metaId);
}
