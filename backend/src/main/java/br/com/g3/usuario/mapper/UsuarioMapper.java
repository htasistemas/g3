package br.com.g3.usuario.mapper;

import br.com.g3.usuario.domain.Permissao;
import br.com.g3.usuario.domain.Usuario;
import br.com.g3.usuario.dto.UsuarioResponse;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioMapper {
  private UsuarioMapper() {}

  public static UsuarioResponse toResponse(Usuario usuario) {
    List<String> permissoes =
        usuario.getPermissoes().stream().map(Permissao::getNome).collect(Collectors.toList());
    return new UsuarioResponse(
        usuario.getId(),
        usuario.getNomeUsuario(),
        usuario.getNome(),
        usuario.getEmail(),
        usuario.getCriadoEm(),
        usuario.getAtualizadoEm(),
        permissoes);
  }
}
