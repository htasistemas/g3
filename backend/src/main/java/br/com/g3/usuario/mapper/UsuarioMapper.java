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
        usuario.getHorarioEntrada1(),
        usuario.getHorarioSaida1(),
        usuario.getHorarioEntrada2(),
        usuario.getHorarioSaida2(),
        usuario.getHorarioSegundaEntrada1(),
        usuario.getHorarioSegundaSaida1(),
        usuario.getHorarioSegundaEntrada2(),
        usuario.getHorarioSegundaSaida2(),
        usuario.getHorarioTercaEntrada1(),
        usuario.getHorarioTercaSaida1(),
        usuario.getHorarioTercaEntrada2(),
        usuario.getHorarioTercaSaida2(),
        usuario.getHorarioQuartaEntrada1(),
        usuario.getHorarioQuartaSaida1(),
        usuario.getHorarioQuartaEntrada2(),
        usuario.getHorarioQuartaSaida2(),
        usuario.getHorarioQuintaEntrada1(),
        usuario.getHorarioQuintaSaida1(),
        usuario.getHorarioQuintaEntrada2(),
        usuario.getHorarioQuintaSaida2(),
        usuario.getHorarioSextaEntrada1(),
        usuario.getHorarioSextaSaida1(),
        usuario.getHorarioSextaEntrada2(),
        usuario.getHorarioSextaSaida2(),
        usuario.getHorarioSabadoEntrada1(),
        usuario.getHorarioSabadoSaida1(),
        usuario.getHorarioSabadoEntrada2(),
        usuario.getHorarioSabadoSaida2(),
        usuario.getHorarioDomingoEntrada1(),
        usuario.getHorarioDomingoSaida1(),
        usuario.getHorarioDomingoEntrada2(),
        usuario.getHorarioDomingoSaida2(),
        usuario.getCriadoEm(),
        usuario.getAtualizadoEm(),
        permissoes);
  }
}
