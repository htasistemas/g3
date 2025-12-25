package br.com.g3.usuario.service;

import br.com.g3.usuario.domain.Permissao;
import java.util.Collection;
import java.util.List;

public interface PermissaoService {
  List<String> listarNomes();

  List<Permissao> buscarPorNomes(Collection<String> nomes);

  List<Permissao> listar();
}
