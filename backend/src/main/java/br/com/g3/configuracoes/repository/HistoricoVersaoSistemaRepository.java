package br.com.g3.configuracoes.repository;

import br.com.g3.configuracoes.domain.HistoricoVersaoSistema;
import java.util.List;

public interface HistoricoVersaoSistemaRepository {
  HistoricoVersaoSistema salvar(HistoricoVersaoSistema historico);

  List<HistoricoVersaoSistema> listar();
}
