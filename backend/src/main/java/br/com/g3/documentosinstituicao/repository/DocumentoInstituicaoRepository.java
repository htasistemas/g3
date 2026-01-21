package br.com.g3.documentosinstituicao.repository;

import br.com.g3.documentosinstituicao.domain.DocumentoInstituicao;
import java.util.List;
import java.util.Optional;

public interface DocumentoInstituicaoRepository {
  DocumentoInstituicao salvar(DocumentoInstituicao documento);

  List<DocumentoInstituicao> listar();

  Optional<DocumentoInstituicao> buscarPorId(Long id);

  void remover(DocumentoInstituicao documento);
}
