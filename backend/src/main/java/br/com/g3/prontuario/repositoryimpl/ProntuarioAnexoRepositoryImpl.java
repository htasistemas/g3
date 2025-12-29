package br.com.g3.prontuario.repositoryimpl;

import br.com.g3.prontuario.domain.ProntuarioAnexo;
import br.com.g3.prontuario.repository.ProntuarioAnexoRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ProntuarioAnexoRepositoryImpl implements ProntuarioAnexoRepository {
  private final ProntuarioAnexoJpaRepository jpaRepository;

  public ProntuarioAnexoRepositoryImpl(ProntuarioAnexoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public ProntuarioAnexo salvar(ProntuarioAnexo anexo) {
    return jpaRepository.save(anexo);
  }
}
