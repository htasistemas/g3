package br.com.g3.cursosatendimentos.repositoryimpl;

import br.com.g3.cursosatendimentos.domain.CursoAtendimentoPresencaAnexo;
import br.com.g3.cursosatendimentos.repository.CursoAtendimentoPresencaAnexoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CursoAtendimentoPresencaAnexoRepositoryImpl
    implements CursoAtendimentoPresencaAnexoRepository {
  private final CursoAtendimentoPresencaAnexoJpaRepository jpaRepository;

  public CursoAtendimentoPresencaAnexoRepositoryImpl(
      CursoAtendimentoPresencaAnexoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public CursoAtendimentoPresencaAnexo salvar(CursoAtendimentoPresencaAnexo anexo) {
    return jpaRepository.save(anexo);
  }

  @Override
  public List<CursoAtendimentoPresencaAnexo> listarPorPresencaData(Long presencaDataId) {
    return jpaRepository.findAllByPresencaDataIdOrderByDataUploadDescIdDesc(presencaDataId);
  }

  @Override
  public Optional<CursoAtendimentoPresencaAnexo> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }
}
