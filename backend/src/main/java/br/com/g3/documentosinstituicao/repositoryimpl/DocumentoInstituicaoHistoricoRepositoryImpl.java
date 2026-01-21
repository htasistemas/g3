package br.com.g3.documentosinstituicao.repositoryimpl;

import br.com.g3.documentosinstituicao.domain.DocumentoInstituicaoHistorico;
import br.com.g3.documentosinstituicao.repository.DocumentoInstituicaoHistoricoRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class DocumentoInstituicaoHistoricoRepositoryImpl implements DocumentoInstituicaoHistoricoRepository {
  private final DocumentoInstituicaoHistoricoJpaRepository jpaRepository;

  public DocumentoInstituicaoHistoricoRepositoryImpl(DocumentoInstituicaoHistoricoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public DocumentoInstituicaoHistorico salvar(DocumentoInstituicaoHistorico historico) {
    return jpaRepository.save(historico);
  }

  @Override
  public List<DocumentoInstituicaoHistorico> listarPorDocumento(Long documentoId) {
    return jpaRepository.findAllByDocumentoIdOrderByDataHoraDescIdDesc(documentoId);
  }

  @Override
  public void removerPorDocumento(Long documentoId) {
    jpaRepository.deleteAllByDocumentoId(documentoId);
  }
}
