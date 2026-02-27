package br.com.g3.cursosatendimentos.repository;

import br.com.g3.cursosatendimentos.domain.CursoAtendimentoPresencaAnexo;
import java.util.List;
import java.util.Optional;

public interface CursoAtendimentoPresencaAnexoRepository {
  CursoAtendimentoPresencaAnexo salvar(CursoAtendimentoPresencaAnexo anexo);

  List<CursoAtendimentoPresencaAnexo> listarPorPresencaData(Long presencaDataId);

  Optional<CursoAtendimentoPresencaAnexo> buscarPorId(Long id);
}
