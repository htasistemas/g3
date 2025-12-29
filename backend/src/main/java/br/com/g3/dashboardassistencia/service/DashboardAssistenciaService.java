package br.com.g3.dashboardassistencia.service;

import br.com.g3.dashboardassistencia.dto.DashboardAssistenciaResponse;
import java.time.LocalDate;

public interface DashboardAssistenciaService {
  DashboardAssistenciaResponse obterDashboard(LocalDate inicio, LocalDate fim);
}
