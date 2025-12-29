package br.com.g3.configuracoes.repositoryimpl;

import br.com.g3.configuracoes.domain.HistoricoVersaoSistema;
import br.com.g3.configuracoes.repository.HistoricoVersaoSistemaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class HistoricoVersaoSistemaRepositoryImpl implements HistoricoVersaoSistemaRepository {
  private final HistoricoVersaoSistemaJpaRepository jpaRepository;

  public HistoricoVersaoSistemaRepositoryImpl(HistoricoVersaoSistemaJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public HistoricoVersaoSistema salvar(HistoricoVersaoSistema historico) {
    return jpaRepository.save(historico);
  }

  @Override
  public List<HistoricoVersaoSistema> listar() {
    return jpaRepository.findAllByOrderByCriadoEmDesc();
  }
}
