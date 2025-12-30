package br.com.g3.autorizacaocompras.cotacoes.repositoryimpl;

import br.com.g3.autorizacaocompras.cotacoes.domain.AutorizacaoCompraCotacao;
import br.com.g3.autorizacaocompras.cotacoes.repository.AutorizacaoCompraCotacaoRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class AutorizacaoCompraCotacaoRepositoryImpl implements AutorizacaoCompraCotacaoRepository {
  private final AutorizacaoCompraCotacaoJpaRepository jpaRepository;

  public AutorizacaoCompraCotacaoRepositoryImpl(AutorizacaoCompraCotacaoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public AutorizacaoCompraCotacao salvar(AutorizacaoCompraCotacao cotacao) {
    return jpaRepository.save(cotacao);
  }

  @Override
  public List<AutorizacaoCompraCotacao> listarPorCompraId(Long compraId) {
    return jpaRepository.findByAutorizacaoCompraIdOrderByCriadoEmDesc(compraId);
  }
}
