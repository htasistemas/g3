package br.com.g3.cadastrovoluntario.repositoryimpl;

import br.com.g3.cadastrovoluntario.domain.CadastroVoluntario;
import br.com.g3.cadastrovoluntario.repository.CadastroVoluntarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CadastroVoluntarioRepositoryImpl implements CadastroVoluntarioRepository {
  private final CadastroVoluntarioJpaRepository jpaRepository;

  public CadastroVoluntarioRepositoryImpl(CadastroVoluntarioJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public CadastroVoluntario salvar(CadastroVoluntario cadastro) {
    return jpaRepository.save(cadastro);
  }

  @Override
  public List<CadastroVoluntario> listar() {
    return jpaRepository.findAll();
  }

  @Override
  public List<CadastroVoluntario> buscarPorNome(String nome) {
    return jpaRepository.findByNomeCompletoContainingIgnoreCase(nome);
  }

  @Override
  public Optional<CadastroVoluntario> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public void remover(CadastroVoluntario cadastro) {
    jpaRepository.delete(cadastro);
  }
}
