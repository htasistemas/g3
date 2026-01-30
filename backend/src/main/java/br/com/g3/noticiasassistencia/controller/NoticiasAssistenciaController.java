package br.com.g3.noticiasassistencia.controller;

import br.com.g3.noticiasassistencia.dto.NoticiaAssistenciaResponse;
import br.com.g3.noticiasassistencia.service.NoticiasAssistenciaService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/noticias/assistencia")
public class NoticiasAssistenciaController {
  private final NoticiasAssistenciaService noticiasService;

  public NoticiasAssistenciaController(NoticiasAssistenciaService noticiasService) {
    this.noticiasService = noticiasService;
  }

  @GetMapping
  public ResponseEntity<List<NoticiaAssistenciaResponse>> listar(
      @RequestParam(value = "limite", required = false, defaultValue = "10") int limite,
      @RequestParam(value = "rss", required = false) String rss) {
    return ResponseEntity.ok(noticiasService.listarNoticias(limite, rss));
  }
}
