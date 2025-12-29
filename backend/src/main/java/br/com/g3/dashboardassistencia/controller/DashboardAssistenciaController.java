package br.com.g3.dashboardassistencia.controller;

import br.com.g3.dashboardassistencia.dto.DashboardAssistenciaResponse;
import br.com.g3.dashboardassistencia.service.DashboardAssistenciaService;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardAssistenciaController {
  private final DashboardAssistenciaService service;

  public DashboardAssistenciaController(DashboardAssistenciaService service) {
    this.service = service;
  }

  @GetMapping("/assistencia")
  public DashboardAssistenciaResponse obterDashboard(
      @RequestParam(value = "startDate", required = false) String startDate,
      @RequestParam(value = "endDate", required = false) String endDate) {
    LocalDate inicio = parseDate(startDate);
    LocalDate fim = parseDate(endDate);
    return service.obterDashboard(inicio, fim);
  }

  private LocalDate parseDate(String valor) {
    if (valor == null || valor.trim().isEmpty()) {
      return null;
    }
    try {
      return LocalDate.parse(valor);
    } catch (DateTimeParseException ex) {
      return null;
    }
  }
}
