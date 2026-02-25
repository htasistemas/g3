package br.com.g3.rh.repositoryimpl;

import br.com.g3.rh.domain.RhPontoAuditoria;
import br.com.g3.rh.repository.RhPontoAuditoriaRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class RhPontoAuditoriaRepositoryImpl implements RhPontoAuditoriaRepository {
  private final RhPontoAuditoriaJpaRepository repository;

  public RhPontoAuditoriaRepositoryImpl(RhPontoAuditoriaJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public RhPontoAuditoria salvar(RhPontoAuditoria auditoria) {
    return repository.save(auditoria);
  }

  @Override
  public List<RhPontoAuditoria> buscarAuditoria(
      Long funcionarioId,
      Long unidadeId,
      String resultado,
      LocalDateTime inicio,
      LocalDateTime fim,
      int limite) {
    int tamanho = limite > 0 ? limite : 200;
    var pageable = PageRequest.of(0, tamanho, Sort.by(Sort.Direction.DESC, "dataHoraServidor"));
    return repository.buscarAuditoria(funcionarioId, unidadeId, resultado, inicio, fim, pageable).getContent();
  }
}
