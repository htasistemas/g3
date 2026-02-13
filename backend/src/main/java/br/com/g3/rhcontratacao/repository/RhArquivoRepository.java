package br.com.g3.rhcontratacao.repository;

import br.com.g3.rhcontratacao.domain.RhArquivo;
import java.util.List;
import java.util.Optional;

public interface RhArquivoRepository {
  RhArquivo salvar(RhArquivo arquivo);
  Optional<RhArquivo> buscarPorId(Long id);
  List<RhArquivo> listarPorProcesso(Long processoId);
}
