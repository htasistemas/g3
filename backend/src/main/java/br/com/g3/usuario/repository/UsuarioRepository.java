package br.com.g3.usuario.repository;

import br.com.g3.usuario.domain.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
  Usuario salvar(Usuario usuario);

  List<Usuario> listar();

  Optional<Usuario> buscarPorId(Long id);

  Optional<Usuario> buscarPorNomeUsuarioIgnoreCase(String nomeUsuario);

  Optional<Usuario> buscarPorEmailIgnoreCase(String email);

  void remover(Usuario usuario);
}
