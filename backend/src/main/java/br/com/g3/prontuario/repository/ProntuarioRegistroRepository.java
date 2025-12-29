package br.com.g3.prontuario.repository;

import br.com.g3.prontuario.domain.ProntuarioRegistro;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

public interface ProntuarioRegistroRepository {
  ProntuarioRegistro salvar(ProntuarioRegistro registro);

  Optional<ProntuarioRegistro> buscarPorId(Long id);

  void remover(ProntuarioRegistro registro);

  ProntuarioRegistroConsultaResultado listarRegistros(
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

  Map<String, Long> contarPorTipo(Long beneficiarioId);

  LocalDateTime buscarUltimaAtualizacao(Long beneficiarioId);
}
