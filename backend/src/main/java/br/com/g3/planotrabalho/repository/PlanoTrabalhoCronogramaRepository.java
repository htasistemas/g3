package br.com.g3.planotrabalho.repository;

import br.com.g3.planotrabalho.domain.PlanoTrabalhoCronograma;
import java.util.List;

public interface PlanoTrabalhoCronogramaRepository {
  PlanoTrabalhoCronograma salvar(PlanoTrabalhoCronograma cronograma);

  List<PlanoTrabalhoCronograma> listarPorPlano(Long planoId);

  void removerPorPlano(Long planoId);
}
