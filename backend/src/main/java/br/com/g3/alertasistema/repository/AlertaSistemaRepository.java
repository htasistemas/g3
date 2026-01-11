package br.com.g3.alertasistema.repository;

import br.com.g3.alertasistema.domain.AlertaSistema;
import java.util.List;

public interface AlertaSistemaRepository {
  List<AlertaSistema> listar();

  AlertaSistema salvar(AlertaSistema alerta);

  void removerTodos();
}
