package br.com.g3.documentosinstituicao.repositoryimpl;

import br.com.g3.documentosinstituicao.domain.DocumentoInstituicaoAnexo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentoInstituicaoAnexoJpaRepository extends JpaRepository<DocumentoInstituicaoAnexo, Long> {
  List<DocumentoInstituicaoAnexo> findAllByDocumentoIdOrderByDataUploadDescIdDesc(Long documentoId);

  void deleteAllByDocumentoId(Long documentoId);
}
