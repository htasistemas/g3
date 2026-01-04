package br.com.g3.planotrabalho.repository;

import br.com.g3.planotrabalho.domain.PlanoTrabalhoEtapa;
import java.util.List;

public interface PlanoTrabalhoEtapaRepository {
  PlanoTrabalhoEtapa salvar(PlanoTrabalhoEtapa etapa);

  List<PlanoTrabalhoEtapa> listarPorAtividade(Long atividadeId);

  void removerPorAtividade(Long atividadeId);
}
