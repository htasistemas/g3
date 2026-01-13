package br.com.g3.oficios.service;

import br.com.g3.oficios.dto.OficioListaResponse;
import br.com.g3.oficios.dto.OficioImagemRequest;
import br.com.g3.oficios.dto.OficioImagemResponse;
import br.com.g3.oficios.dto.OficioPdfAssinadoRequest;
import br.com.g3.oficios.dto.OficioRequest;
import br.com.g3.oficios.dto.OficioResponse;
import java.util.List;

public interface OficioService {
  OficioListaResponse listar();

  OficioResponse obter(Long id);

  OficioResponse criar(OficioRequest request);

  OficioResponse atualizar(Long id, OficioRequest request);

  void excluir(Long id);

  OficioResponse salvarPdfAssinado(Long id, OficioPdfAssinadoRequest request);

  void removerPdfAssinado(Long id);

  byte[] obterPdfAssinado(Long id);

  String obterPdfAssinadoTipo(Long id);

  String obterPdfAssinadoNome(Long id);

  List<OficioImagemResponse> listarImagens(Long oficioId);

  OficioImagemResponse adicionarImagem(Long oficioId, OficioImagemRequest request);

  void removerImagem(Long oficioId, Long imagemId);
}
