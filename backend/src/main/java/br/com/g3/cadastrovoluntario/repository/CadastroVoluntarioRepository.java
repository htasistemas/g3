package br.com.g3.cadastrovoluntario.repository;

import br.com.g3.cadastrovoluntario.domain.CadastroVoluntario;
import java.util.List;
import java.util.Optional;

public interface CadastroVoluntarioRepository {
  CadastroVoluntario salvar(CadastroVoluntario cadastro);

  List<CadastroVoluntario> listar();

  Optional<CadastroVoluntario> buscarPorId(Long id);

  void remover(CadastroVoluntario cadastro);
}
