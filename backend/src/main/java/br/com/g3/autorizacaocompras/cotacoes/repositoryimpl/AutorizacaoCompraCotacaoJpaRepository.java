package br.com.g3.autorizacaocompras.cotacoes.repositoryimpl;

import br.com.g3.autorizacaocompras.cotacoes.domain.AutorizacaoCompraCotacao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorizacaoCompraCotacaoJpaRepository
    extends JpaRepository<AutorizacaoCompraCotacao, Long> {
  List<AutorizacaoCompraCotacao> findByAutorizacaoCompraIdOrderByCriadoEmDesc(Long compraId);
}
