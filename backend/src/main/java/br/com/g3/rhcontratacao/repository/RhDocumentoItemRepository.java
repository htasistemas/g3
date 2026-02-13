package br.com.g3.rhcontratacao.repository;

import br.com.g3.rhcontratacao.domain.RhDocumentoItem;
import java.util.List;
import java.util.Optional;

public interface RhDocumentoItemRepository {
  RhDocumentoItem salvar(RhDocumentoItem item);
  Optional<RhDocumentoItem> buscarPorId(Long id);
  Optional<RhDocumentoItem> buscarPorProcessoETipo(Long processoId, String tipoDocumento);
  List<RhDocumentoItem> listarPorProcesso(Long processoId);
}
