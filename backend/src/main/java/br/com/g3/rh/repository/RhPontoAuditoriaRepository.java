package br.com.g3.rh.repository;

import br.com.g3.rh.domain.RhPontoAuditoria;
import java.time.LocalDateTime;
import java.util.List;

public interface RhPontoAuditoriaRepository {
  RhPontoAuditoria salvar(RhPontoAuditoria auditoria);

  List<RhPontoAuditoria> buscarAuditoria(
      Long funcionarioId,
      Long unidadeId,
      String resultado,
      LocalDateTime inicio,
      LocalDateTime fim,
      int limite);
}
