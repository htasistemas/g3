package br.com.g3.rh.service;

import br.com.g3.rh.dto.RhConfiguracaoPontoRequest;
import br.com.g3.rh.dto.RhConfiguracaoPontoResponse;
import br.com.g3.rh.dto.RhLocalPontoRequest;
import br.com.g3.rh.dto.RhLocalPontoResponse;
import br.com.g3.rh.dto.RhPontoBaterRequest;
import br.com.g3.rh.dto.RhPontoDiaAtualizacaoRequest;
import br.com.g3.rh.dto.RhPontoDiaResponse;
import br.com.g3.rh.dto.RhPontoEspelhoResponse;
import java.util.List;

public interface RhPontoService {
  List<RhLocalPontoResponse> listarLocais();
  RhLocalPontoResponse criarLocal(RhLocalPontoRequest request);
  RhLocalPontoResponse atualizarLocal(Long id, RhLocalPontoRequest request);
  void removerLocal(Long id);
  RhLocalPontoResponse buscarLocalAtivo();

  RhConfiguracaoPontoResponse buscarConfiguracao();
  RhConfiguracaoPontoResponse atualizarConfiguracao(RhConfiguracaoPontoRequest request, Long usuarioId);

  RhPontoDiaResponse baterPonto(RhPontoBaterRequest request, Long usuarioId, String ip, String userAgent);
  RhPontoEspelhoResponse consultarEspelho(Integer mes, Integer ano, Long funcionarioId);
  RhPontoDiaResponse atualizarDia(Long id, RhPontoDiaAtualizacaoRequest request, Long usuarioId);
}
