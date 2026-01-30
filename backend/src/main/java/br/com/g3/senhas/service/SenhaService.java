package br.com.g3.senhas.service;

import br.com.g3.senhas.dto.SenhaChamadaResponse;
import br.com.g3.senhas.dto.SenhaChamarRequest;
import br.com.g3.senhas.dto.SenhaEmitirRequest;
import br.com.g3.senhas.dto.SenhaFilaResponse;
import java.util.List;

public interface SenhaService {
  List<SenhaFilaResponse> listarAguardando(Long unidadeId);

  SenhaFilaResponse emitirSenha(SenhaEmitirRequest request);

  SenhaChamadaResponse chamarSenha(SenhaChamarRequest request, String chamadoPor);

  void finalizarSenha(java.util.UUID chamadaId);

  void finalizarPorFila(Long filaId);

  SenhaChamadaResponse obterAtual(Long unidadeId);

  List<SenhaChamadaResponse> listarPainel(Long unidadeId, Integer limite);

  List<SenhaChamadaResponse> listarHistorico(Long unidadeId, Integer limite);
}
