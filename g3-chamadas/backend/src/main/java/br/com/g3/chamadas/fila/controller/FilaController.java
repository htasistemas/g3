package br.com.g3.chamadas.fila.controller;

import br.com.g3.chamadas.fila.dto.FilaAtualizacaoDto;
import br.com.g3.chamadas.fila.dto.FilaRespostaDto;
import br.com.g3.chamadas.fila.service.FilaAtendimentoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fila")
public class FilaController {
  private final FilaAtendimentoService filaService;

  public FilaController(FilaAtendimentoService filaService) {
    this.filaService = filaService;
  }

  @GetMapping("/aguardando")
  public List<FilaRespostaDto> listarAguardando() {
    return filaService.listarAguardando();
  }

  @PostMapping("/entrar")
  public FilaRespostaDto entrar(@Valid @RequestBody FilaAtualizacaoDto dto) {
    return filaService.entrarFila(dto);
  }
}
