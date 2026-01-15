package br.com.g3.bancoempregos.repositoryimpl;

import br.com.g3.bancoempregos.domain.BancoEmpregoEncaminhamento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BancoEmpregoEncaminhamentoJpaRepository
    extends JpaRepository<BancoEmpregoEncaminhamento, Long> {
  List<BancoEmpregoEncaminhamento> findByEmpregoId(Long empregoId);

  void deleteByEmpregoId(Long empregoId);
}
