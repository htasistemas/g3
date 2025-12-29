package br.com.g3.prontuario.service;

import br.com.g3.prontuario.dto.ProntuarioAnexoRequest;
import br.com.g3.prontuario.dto.ProntuarioAnexoResponse;
import br.com.g3.prontuario.dto.ProntuarioRegistroListaResponse;
import br.com.g3.prontuario.dto.ProntuarioRegistroRequest;
import br.com.g3.prontuario.dto.ProntuarioRegistroResponse;
import br.com.g3.prontuario.dto.ProntuarioResumoResponse;
import java.time.LocalDateTime;

public interface ProntuarioService {
  ProntuarioResumoResponse obterResumo(Long beneficiarioId);

  ProntuarioRegistroListaResponse listarRegistros(
      Long beneficiarioId,
      String tipo,
      LocalDateTime dataInicio,
      LocalDateTime dataFim,
      Long profissionalId,
      Long unidadeId,
      String status,
      String texto,
      int pagina,
      int tamanhoPagina);

  ProntuarioRegistroResponse criarRegistro(Long beneficiarioId, ProntuarioRegistroRequest request);

  ProntuarioRegistroResponse atualizarRegistro(Long registroId, ProntuarioRegistroRequest request);

  void removerRegistro(Long registroId);

  ProntuarioAnexoResponse adicionarAnexo(Long registroId, ProntuarioAnexoRequest request);
}
