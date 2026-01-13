package br.com.g3.ocorrenciacrianca.repository;

import br.com.g3.ocorrenciacrianca.domain.OcorrenciaCrianca;
import java.util.List;
import java.util.Optional;

public interface OcorrenciaCriancaRepository {
  OcorrenciaCrianca salvar(OcorrenciaCrianca ocorrencia);

  Optional<OcorrenciaCrianca> buscarPorId(Long id);

  List<OcorrenciaCrianca> listar();

  void remover(Long id);
}
