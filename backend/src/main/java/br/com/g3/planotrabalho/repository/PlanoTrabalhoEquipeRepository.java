package br.com.g3.planotrabalho.repository;

import br.com.g3.planotrabalho.domain.PlanoTrabalhoEquipe;
import java.util.List;

public interface PlanoTrabalhoEquipeRepository {
  PlanoTrabalhoEquipe salvar(PlanoTrabalhoEquipe equipe);

  List<PlanoTrabalhoEquipe> listarPorPlano(Long planoId);

  void removerPorPlano(Long planoId);
}
