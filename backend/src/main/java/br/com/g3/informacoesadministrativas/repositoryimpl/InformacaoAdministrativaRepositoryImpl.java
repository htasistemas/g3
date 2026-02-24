package br.com.g3.informacoesadministrativas.repositoryimpl;

import br.com.g3.informacoesadministrativas.domain.InformacaoAdministrativa;
import br.com.g3.informacoesadministrativas.repository.InformacaoAdministrativaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class InformacaoAdministrativaRepositoryImpl implements InformacaoAdministrativaRepository {
  private final InformacaoAdministrativaJpaRepository jpaRepository;

  public InformacaoAdministrativaRepositoryImpl(InformacaoAdministrativaJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public InformacaoAdministrativa salvar(InformacaoAdministrativa info) {
    return jpaRepository.save(info);
  }

  @Override
  public Optional<InformacaoAdministrativa> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public List<InformacaoAdministrativa> listar() {
    return jpaRepository.findAll();
  }

  @Override
  public void remover(InformacaoAdministrativa info) {
    jpaRepository.delete(info);
  }
}
