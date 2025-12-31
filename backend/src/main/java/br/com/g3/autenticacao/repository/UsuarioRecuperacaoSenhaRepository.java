package br.com.g3.autenticacao.repository;

import br.com.g3.autenticacao.domain.UsuarioRecuperacaoSenha;
import br.com.g3.usuario.domain.Usuario;
import java.time.LocalDateTime;
import java.util.Optional;

public interface UsuarioRecuperacaoSenhaRepository {
  UsuarioRecuperacaoSenha salvar(UsuarioRecuperacaoSenha token);

  Optional<UsuarioRecuperacaoSenha> buscarPorToken(String token);

  Optional<UsuarioRecuperacaoSenha> buscarUltimoPorUsuario(Usuario usuario);

  void marcarTokensComoUsados(Usuario usuario, LocalDateTime usadoEm);
}
