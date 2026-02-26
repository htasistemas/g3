package br.com.g3.doacaoplanejada.repositoryimpl;

import br.com.g3.doacaoplanejada.domain.DoacaoPlanejada;
import br.com.g3.doacaoplanejada.repository.DoacaoPlanejadaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class DoacaoPlanejadaRepositoryImpl implements DoacaoPlanejadaRepository {
  private final DoacaoPlanejadaJpaRepository jpaRepository;

  public DoacaoPlanejadaRepositoryImpl(DoacaoPlanejadaJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public DoacaoPlanejada salvar(DoacaoPlanejada doacao) {
    return jpaRepository.save(doacao);
  }

  @Override
  public List<DoacaoPlanejada> listar() {
    return jpaRepository.listarTodasComItem();
  }

  @Override
  public List<DoacaoPlanejada> listarPorBeneficiario(Long beneficiarioId) {
    return jpaRepository.listarPorBeneficiarioComItem(beneficiarioId);
  }

  @Override
  public List<DoacaoPlanejada> listarPorVinculoFamiliar(Long vinculoFamiliarId) {
    return jpaRepository.listarPorVinculoFamiliarComItem(vinculoFamiliarId);
  }

  @Override
  public List<DoacaoPlanejada> listarPendentes() {
    return jpaRepository.listarPendentesComRelacionamentos("pendente");
  }

  @Override
  public Optional<DoacaoPlanejada> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public void remover(DoacaoPlanejada doacao) {
    jpaRepository.delete(doacao);
  }
}
