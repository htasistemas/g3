package br.com.g3.alertasistema.service;

import br.com.g3.alertasistema.dto.AlertaSistemaRequest;
import br.com.g3.alertasistema.dto.AlertaSistemaResponse;

public interface AlertaSistemaService {
  AlertaSistemaResponse obterConfiguracao();

  AlertaSistemaResponse salvarConfiguracao(AlertaSistemaRequest request);
}
