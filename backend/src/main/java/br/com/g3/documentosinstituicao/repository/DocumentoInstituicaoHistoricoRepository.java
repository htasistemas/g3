package br.com.g3.documentosinstituicao.repository;

import br.com.g3.documentosinstituicao.domain.DocumentoInstituicaoHistorico;
import java.util.List;

public interface DocumentoInstituicaoHistoricoRepository {
  DocumentoInstituicaoHistorico salvar(DocumentoInstituicaoHistorico historico);

  List<DocumentoInstituicaoHistorico> listarPorDocumento(Long documentoId);

  void removerPorDocumento(Long documentoId);
}
