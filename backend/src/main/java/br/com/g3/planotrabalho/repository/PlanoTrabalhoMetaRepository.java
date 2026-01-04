package br.com.g3.planotrabalho.repository;

import br.com.g3.planotrabalho.domain.PlanoTrabalhoMeta;
import java.util.List;

public interface PlanoTrabalhoMetaRepository {
  PlanoTrabalhoMeta salvar(PlanoTrabalhoMeta meta);

  List<PlanoTrabalhoMeta> listarPorPlano(Long planoId);

  void removerPorPlano(Long planoId);
}
