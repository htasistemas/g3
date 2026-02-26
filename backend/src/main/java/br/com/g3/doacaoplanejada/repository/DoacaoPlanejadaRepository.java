package br.com.g3.doacaoplanejada.repository;

import br.com.g3.doacaoplanejada.domain.DoacaoPlanejada;
import java.util.List;
import java.util.Optional;

public interface DoacaoPlanejadaRepository {
  DoacaoPlanejada salvar(DoacaoPlanejada doacao);

  List<DoacaoPlanejada> listar();

  List<DoacaoPlanejada> listarPorBeneficiario(Long beneficiarioId);

  List<DoacaoPlanejada> listarPorVinculoFamiliar(Long vinculoFamiliarId);

  List<DoacaoPlanejada> listarPendentes();

  Optional<DoacaoPlanejada> buscarPorId(Long id);

  void remover(DoacaoPlanejada doacao);
}
