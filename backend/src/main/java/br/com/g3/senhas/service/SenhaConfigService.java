package br.com.g3.senhas.service;

import br.com.g3.senhas.dto.SenhaConfigRequest;
import br.com.g3.senhas.dto.SenhaConfigResponse;

public interface SenhaConfigService {
  SenhaConfigResponse obterConfiguracao();

  SenhaConfigResponse atualizarConfiguracao(SenhaConfigRequest request);
}
