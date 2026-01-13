package br.com.g3.ocorrenciacrianca.repository;

import br.com.g3.ocorrenciacrianca.domain.OcorrenciaCriancaAnexo;
import java.util.List;
import java.util.Optional;

public interface OcorrenciaCriancaAnexoRepository {
  OcorrenciaCriancaAnexo salvar(OcorrenciaCriancaAnexo anexo);

  List<OcorrenciaCriancaAnexo> listarPorOcorrenciaId(Long ocorrenciaId);

  Optional<OcorrenciaCriancaAnexo> buscarPorId(Long id);

  void remover(Long id);
}
