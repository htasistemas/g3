package br.com.g3.ocorrenciacrianca.repositoryimpl;

import br.com.g3.ocorrenciacrianca.domain.OcorrenciaCriancaAnexo;
import br.com.g3.ocorrenciacrianca.repository.OcorrenciaCriancaAnexoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class OcorrenciaCriancaAnexoRepositoryImpl implements OcorrenciaCriancaAnexoRepository {
  private final OcorrenciaCriancaAnexoJpaRepository jpaRepository;

  public OcorrenciaCriancaAnexoRepositoryImpl(OcorrenciaCriancaAnexoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public OcorrenciaCriancaAnexo salvar(OcorrenciaCriancaAnexo anexo) {
    return jpaRepository.save(anexo);
  }

  @Override
  public List<OcorrenciaCriancaAnexo> listarPorOcorrenciaId(Long ocorrenciaId) {
    return jpaRepository.findByOcorrenciaIdOrderByOrdemAscIdAsc(ocorrenciaId);
  }

  @Override
  public Optional<OcorrenciaCriancaAnexo> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public void remover(Long id) {
    jpaRepository.deleteById(id);
  }
}
