package br.com.g3.usuario.controller;

import br.com.g3.usuario.dto.PermissaoResponse;
import br.com.g3.usuario.service.PermissaoService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios/permissoes")
public class PermissaoController {
  private final PermissaoService service;

  public PermissaoController(PermissaoService service) {
    this.service = service;
  }

  @GetMapping
  public List<PermissaoResponse> listar() {
    return service.listar().stream()
        .map(permissao -> new PermissaoResponse(permissao.getId(), permissao.getNome()))
        .collect(Collectors.toList());
  }
}
