package br.com.g3.auditoria.service;

import br.com.g3.auditoria.dto.AuditoriaEventoResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AuditoriaService {
  AuditoriaEventoResponse registrarEvento(
      String acao, String entidade, String entidadeId, String dadosJson, Long usuarioId);

  AuditoriaEventoResponse buscarPorId(UUID id);

  List<AuditoriaEventoResponse> listar(
      LocalDate dataInicio,
      LocalDate dataFim,
      String usuarioId,
      String entidade,
      String texto);
}
