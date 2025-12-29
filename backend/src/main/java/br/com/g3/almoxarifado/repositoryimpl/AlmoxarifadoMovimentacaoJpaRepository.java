package br.com.g3.almoxarifado.repositoryimpl;

import br.com.g3.almoxarifado.domain.AlmoxarifadoMovimentacao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlmoxarifadoMovimentacaoJpaRepository
    extends JpaRepository<AlmoxarifadoMovimentacao, Long> {
  List<AlmoxarifadoMovimentacao> findAllByOrderByDataMovimentacaoDescIdDesc();
}
