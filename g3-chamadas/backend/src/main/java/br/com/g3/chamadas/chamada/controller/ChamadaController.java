package br.com.g3.chamadas.chamada.controller;

import br.com.g3.chamadas.chamada.dto.ChamadaCriarDto;
import br.com.g3.chamadas.chamada.dto.ChamadaRespostaDto;
import br.com.g3.chamadas.chamada.service.ChamadaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chamadas")
public class ChamadaController {
  private final ChamadaService chamadaService;

  public ChamadaController(ChamadaService chamadaService) {
    this.chamadaService = chamadaService;
  }

  @PostMapping("/chamar")
  public ChamadaRespostaDto chamar(
      @Valid @RequestBody ChamadaCriarDto dto,
      @RequestHeader(value = "X-Usuario", required = false) String usuario) {
    String chamadoPor = usuario != null && !usuario.trim().isEmpty() ? usuario.trim() : "Sistema";
    return chamadaService.chamar(dto, chamadoPor);
  }

  @GetMapping("/ultimas")
  public List<ChamadaRespostaDto> listarUltimas(@RequestParam(value = "limite", required = false) Integer limite) {
    return chamadaService.listarUltimas(limite);
  }

  @GetMapping("/ultima")
  public ChamadaRespostaDto ultima() {
    return chamadaService.ultimaChamada();
  }
}
