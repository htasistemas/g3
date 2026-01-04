package br.com.g3.planotrabalho.repositoryimpl;

import br.com.g3.planotrabalho.domain.PlanoTrabalhoMeta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface PlanoTrabalhoMetaJpaRepository extends JpaRepository<PlanoTrabalhoMeta, Long> {
  List<PlanoTrabalhoMeta> findAllByPlanoTrabalhoId(Long planoTrabalhoId);

  @Modifying
  @Transactional
  void deleteByPlanoTrabalhoId(Long planoTrabalhoId);
}
