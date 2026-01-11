package br.com.g3.bancoempregos.repository;

import br.com.g3.bancoempregos.domain.BancoEmpregoCandidato;
import java.util.List;
import java.util.Optional;

public interface BancoEmpregoCandidatoRepository {
  List<BancoEmpregoCandidato> listarPorEmprego(Long empregoId);

  BancoEmpregoCandidato salvar(BancoEmpregoCandidato candidato);

  Optional<BancoEmpregoCandidato> buscarPorId(Long id);

  void remover(BancoEmpregoCandidato candidato);
}
