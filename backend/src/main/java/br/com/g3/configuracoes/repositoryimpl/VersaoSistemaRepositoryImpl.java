package br.com.g3.configuracoes.repositoryimpl;

import br.com.g3.configuracoes.domain.VersaoSistema;
import br.com.g3.configuracoes.repository.VersaoSistemaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class VersaoSistemaRepositoryImpl implements VersaoSistemaRepository {
  private final VersaoSistemaJpaRepository jpaRepository;

  public VersaoSistemaRepositoryImpl(VersaoSistemaJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Optional<VersaoSistema> buscarAtual() {
    return jpaRepository.findTopByOrderByAtualizadoEmDesc();
  }

  @Override
  public VersaoSistema salvar(VersaoSistema versaoSistema) {
    return jpaRepository.save(versaoSistema);
  }
}
