package br.com.g3.planotrabalho.repository;

import br.com.g3.planotrabalho.domain.PlanoTrabalho;
import java.util.List;
import java.util.Optional;

public interface PlanoTrabalhoRepository {
  PlanoTrabalho salvar(PlanoTrabalho plano);

  Optional<PlanoTrabalho> buscarPorId(Long id);

  List<PlanoTrabalho> listar();

  void remover(PlanoTrabalho plano);
}
