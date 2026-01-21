package br.com.g3.dashboardassistencia.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DashboardAssistenciaRepository {
  long contarBeneficiarios();

  Map<String, Long> contarBeneficiariosPorStatus();

  long contarBeneficiariosPeriodo(LocalDate inicio, LocalDate fim);

  long contarCadastroCompleto();

  List<LocalDate> listarDatasNascimento();

  long contarSituacaoSocialTotal();

  Double calcularMediaPessoas();

  List<String> listarRendasFamiliares();

  Map<String, Long> contarVulnerabilidades();

  long contarProfissionais();

  long contarVoluntarios();

  long contarFamilias();

  long contarBensPatrimonio();

  double somarValoresAReceber();

  double somarValoresEmCaixa();

  double somarValoresEmBanco();
}
