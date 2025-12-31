package br.com.g3.recebimentodoacao.service;

import br.com.g3.recebimentodoacao.dto.DoadorRequest;
import br.com.g3.recebimentodoacao.dto.DoadorResponse;
import br.com.g3.recebimentodoacao.dto.RecebimentoDoacaoListaResponse;
import br.com.g3.recebimentodoacao.dto.RecebimentoDoacaoRequest;
import br.com.g3.recebimentodoacao.dto.RecebimentoDoacaoResponse;
import java.util.List;

public interface RecebimentoDoacaoService {
  DoadorResponse criarDoador(DoadorRequest request);

  List<DoadorResponse> listarDoadores();

  RecebimentoDoacaoResponse criarRecebimento(RecebimentoDoacaoRequest request);

  RecebimentoDoacaoListaResponse listarRecebimentos();

  RecebimentoDoacaoResponse atualizarRecebimento(Long id, RecebimentoDoacaoRequest request);

  void excluirRecebimento(Long id);
}
