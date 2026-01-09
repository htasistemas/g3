package br.com.g3.auditoria.controller;

import br.com.g3.auditoria.dto.AuditoriaEventoResponse;
import br.com.g3.auditoria.service.AuditoriaService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auditoria-eventos")
public class AuditoriaEventoController {
  private final AuditoriaService auditoriaService;

  public AuditoriaEventoController(AuditoriaService auditoriaService) {
    this.auditoriaService = auditoriaService;
  }

  @GetMapping
  public List<AuditoriaEventoResponse> listar(
      @RequestParam(value = "data_inicio", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate dataInicio,
      @RequestParam(value = "data_fim", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate dataFim,
      @RequestParam(value = "usuario_id", required = false) String usuarioId,
      @RequestParam(value = "entidade", required = false) String entidade,
      @RequestParam(value = "texto", required = false) String texto) {
    return auditoriaService.listar(dataInicio, dataFim, usuarioId, entidade, texto);
  }
}
