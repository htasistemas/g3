package br.com.g3.planotrabalho.repository;

import br.com.g3.planotrabalho.domain.PlanoTrabalhoAtividade;
import java.util.List;

public interface PlanoTrabalhoAtividadeRepository {
  PlanoTrabalhoAtividade salvar(PlanoTrabalhoAtividade atividade);

  List<PlanoTrabalhoAtividade> listarPorMeta(Long metaId);

  void removerPorMeta(Long metaId);
}
