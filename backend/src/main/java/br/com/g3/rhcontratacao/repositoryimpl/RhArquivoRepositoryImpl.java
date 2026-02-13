package br.com.g3.rhcontratacao.repositoryimpl;

import br.com.g3.rhcontratacao.domain.RhArquivo;
import br.com.g3.rhcontratacao.repository.RhArquivoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class RhArquivoRepositoryImpl implements RhArquivoRepository {
  private final RhArquivoJpaRepository jpaRepository;

  public RhArquivoRepositoryImpl(RhArquivoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public RhArquivo salvar(RhArquivo arquivo) {
    return jpaRepository.save(arquivo);
  }

  @Override
  public Optional<RhArquivo> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public List<RhArquivo> listarPorProcesso(Long processoId) {
    return jpaRepository.findByProcessoIdOrderByCriadoEmDesc(processoId);
  }
}
