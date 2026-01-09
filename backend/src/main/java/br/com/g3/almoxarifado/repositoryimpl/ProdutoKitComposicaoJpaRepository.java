package br.com.g3.almoxarifado.repositoryimpl;

import br.com.g3.almoxarifado.domain.ProdutoKitComposicao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoKitComposicaoJpaRepository extends JpaRepository<ProdutoKitComposicao, Long> {
  List<ProdutoKitComposicao> findByProdutoKitIdAndAtivoTrueOrderByIdAsc(Long produtoKitId);

  void deleteByProdutoKitId(Long produtoKitId);
}
