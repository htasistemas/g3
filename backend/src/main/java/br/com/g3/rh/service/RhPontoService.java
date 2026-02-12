package br.com.g3.rh.service;

import br.com.g3.rh.dto.RhConfiguracaoPontoRequest;
import br.com.g3.rh.dto.RhConfiguracaoPontoResponse;
import br.com.g3.rh.dto.RhPontoBaterRequest;
import br.com.g3.rh.dto.RhPontoDiaAtualizacaoRequest;
import br.com.g3.rh.dto.RhPontoDiaResponse;
import br.com.g3.rh.dto.RhPontoEspelhoResponse;
public interface RhPontoService {
  RhConfiguracaoPontoResponse buscarConfiguracao();
  RhConfiguracaoPontoResponse atualizarConfiguracao(RhConfiguracaoPontoRequest request, Long usuarioId);

  RhPontoDiaResponse baterPonto(RhPontoBaterRequest request, Long usuarioId, String ip, String userAgent);
  RhPontoEspelhoResponse consultarEspelho(Integer mes, Integer ano, Long funcionarioId);
  RhPontoDiaResponse atualizarDia(Long id, RhPontoDiaAtualizacaoRequest request, Long usuarioId);
}
