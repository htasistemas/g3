package br.com.g3.documentosinstituicao.repositoryimpl;

import br.com.g3.documentosinstituicao.domain.DocumentoInstituicaoHistorico;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentoInstituicaoHistoricoJpaRepository extends JpaRepository<DocumentoInstituicaoHistorico, Long> {
  List<DocumentoInstituicaoHistorico> findAllByDocumentoIdOrderByDataHoraDescIdDesc(Long documentoId);

  void deleteAllByDocumentoId(Long documentoId);
}
