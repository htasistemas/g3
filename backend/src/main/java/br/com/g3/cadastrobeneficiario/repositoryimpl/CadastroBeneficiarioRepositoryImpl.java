package br.com.g3.cadastrobeneficiario.repositoryimpl;

import br.com.g3.cadastrobeneficiario.domain.CadastroBeneficiario;
import br.com.g3.cadastrobeneficiario.repository.CadastroBeneficiarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CadastroBeneficiarioRepositoryImpl implements CadastroBeneficiarioRepository {
  private final CadastroBeneficiarioJpaRepository jpaRepository;

  public CadastroBeneficiarioRepositoryImpl(CadastroBeneficiarioJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public CadastroBeneficiario salvar(CadastroBeneficiario cadastro) {
    return jpaRepository.save(cadastro);
  }

  @Override
  public List<CadastroBeneficiario> listar() {
    return jpaRepository.findAll();
  }

  @Override
  public List<CadastroBeneficiario> buscarPorNome(String nome) {
    return jpaRepository.findByNomeCompletoContainingIgnoreCase(nome);
  }

  @Override
  public List<CadastroBeneficiario> listarPorNomeEStatus(String nome, String status) {
    return jpaRepository.findByNomeCompletoContainingIgnoreCaseAndStatus(nome, status);
  }

  @Override
  public Optional<CadastroBeneficiario> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public Integer buscarMaiorCodigo() {
    return jpaRepository.buscarMaiorCodigo();
  }

  @Override
  public void remover(CadastroBeneficiario cadastro) {
    jpaRepository.delete(cadastro);
  }
}
