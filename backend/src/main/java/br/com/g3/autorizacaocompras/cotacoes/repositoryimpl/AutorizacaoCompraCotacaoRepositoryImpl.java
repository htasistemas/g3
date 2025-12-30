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

  @Override
  public void remover(AutorizacaoCompraCotacao cotacao) {
    jpaRepository.delete(cotacao);
  }

  @Override
  public boolean existePorIdECompraId(Long cotacaoId, Long compraId) {
    return jpaRepository.existsByIdAndAutorizacaoCompraId(cotacaoId, compraId);
  }

  @Override
  public AutorizacaoCompraCotacao buscarPorId(Long cotacaoId) {
    return jpaRepository.findById(cotacaoId).orElse(null);
  }

  @Override
  public AutorizacaoCompraCotacao buscarUltimaPorCnpj(String cnpj) {
    return jpaRepository.findUltimaPorCnpj(cnpj).orElse(null);
  }
}
