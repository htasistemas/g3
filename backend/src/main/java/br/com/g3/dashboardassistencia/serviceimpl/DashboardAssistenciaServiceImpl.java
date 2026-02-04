package br.com.g3.dashboardassistencia.serviceimpl;

import br.com.g3.dashboardassistencia.dto.DashboardAssistenciaResponse;
import br.com.g3.dashboardassistencia.dto.DashboardAtendimentoResponse;
import br.com.g3.dashboardassistencia.dto.DashboardCadastrosResponse;
import br.com.g3.dashboardassistencia.dto.DashboardFinanceiroResponse;
import br.com.g3.dashboardassistencia.dto.DashboardFamiliasResponse;
import br.com.g3.dashboardassistencia.dto.DashboardFiltrosResponse;
import br.com.g3.dashboardassistencia.dto.DashboardTermosResponse;
import br.com.g3.dashboardassistencia.dto.DashboardTop12Response;
import br.com.g3.dashboardassistencia.repository.DashboardAssistenciaRepository;
import br.com.g3.dashboardassistencia.service.DashboardAssistenciaService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class DashboardAssistenciaServiceImpl implements DashboardAssistenciaService {
  private final DashboardAssistenciaRepository repository;

  public DashboardAssistenciaServiceImpl(DashboardAssistenciaRepository repository) {
    this.repository = repository;
  }

  @Override
  public DashboardAssistenciaResponse obterDashboard(LocalDate inicio, LocalDate fim) {
    long totalBeneficiarios = repository.contarBeneficiarios();
    long totalProfissionais = repository.contarProfissionais();
    long totalVoluntarios = repository.contarVoluntarios();
    long totalFamiliasCadastradas = repository.contarFamilias();
    long totalBensPatrimonio = repository.contarBensPatrimonio();
    long beneficiariosPeriodo = repository.contarBeneficiariosPeriodo(inicio, fim);
    Map<String, Long> porStatus = repository.contarBeneficiariosPorStatus();

    long pendentes = porStatus.getOrDefault("INCOMPLETO", 0L);
    long bloqueados = porStatus.getOrDefault("BLOQUEADO", 0L);
    long emAnalise = porStatus.getOrDefault("EM_ANALISE", 0L);
    long desatualizados = porStatus.getOrDefault("DESATUALIZADO", 0L);
    long ativos = Math.max(0L, totalBeneficiarios - pendentes - bloqueados - emAnalise - desatualizados);

    long completos = repository.contarCadastroCompleto();
    double cadastroCompletoPercentual = totalBeneficiarios == 0
        ? 0.0
        : ((double) completos / totalBeneficiarios) * 100.0;

    List<LocalDate> datasNascimento = repository.listarDatasNascimento();
    Map<String, Long> faixaEtaria = calcularFaixaEtaria(datasNascimento);
    Map<String, Long> idades = calcularIdades(datasNascimento);
    Map<String, Long> vulnerabilidades = repository.contarVulnerabilidades();

    DashboardAtendimentoResponse atendimento =
        new DashboardAtendimentoResponse(
            totalBeneficiarios,
            ativos,
            pendentes,
            bloqueados,
            emAnalise,
            desatualizados,
            arredondar(cadastroCompletoPercentual),
            beneficiariosPeriodo,
            beneficiariosPeriodo,
            0L,
            faixaEtaria,
            idades,
            vulnerabilidades);

    long totalFamilias = repository.contarSituacaoSocialTotal();
    if (totalFamilias == 0) {
      totalFamilias = totalBeneficiarios;
    }

    Double mediaPessoasBanco = repository.calcularMediaPessoas();
    double mediaPessoas = mediaPessoasBanco != null ? mediaPessoasBanco : 0.0;
    List<BigDecimal> rendas = parseRendas(repository.listarRendasFamiliares());
    double rendaMedia = calcularMedia(rendas);
    double rendaPerCapita = mediaPessoas > 0 ? rendaMedia / mediaPessoas : 0.0;
    Map<String, Long> faixaRenda = calcularFaixaRenda(rendas);

    DashboardFamiliasResponse familias =
        new DashboardFamiliasResponse(
            totalFamilias,
            arredondar(mediaPessoas),
            arredondar(rendaMedia),
            arredondar(rendaPerCapita),
            Collections.emptyMap(),
            faixaRenda);

    long familiasExtremaPobreza = faixaRenda.getOrDefault("Ate 200", 0L);

    DashboardTop12Response top12 =
        new DashboardTop12Response(
            beneficiariosPeriodo,
            familiasExtremaPobreza,
            rendaMedia,
            0L,
            0.0,
            0L,
            0.0,
            Collections.emptyMap(),
            0L,
            0L,
            0.0,
            0.0);

    DashboardTermosResponse termos = new DashboardTermosResponse(0L, 0.0, Collections.emptyList());

    DashboardFinanceiroResponse financeiro =
        new DashboardFinanceiroResponse(
            repository.somarValoresAReceber(),
            repository.somarValoresEmCaixa(),
            repository.somarValoresEmBanco());

    DashboardFiltrosResponse filtros =
        new DashboardFiltrosResponse(inicio != null ? inicio.toString() : null, fim != null ? fim.toString() : null);

    DashboardCadastrosResponse cadastros =
        new DashboardCadastrosResponse(
            totalBeneficiarios,
            totalProfissionais,
            totalVoluntarios,
            totalFamiliasCadastradas,
            totalBensPatrimonio);

    return new DashboardAssistenciaResponse(filtros, cadastros, top12, atendimento, familias, termos, financeiro);
  }

  private Map<String, Long> calcularFaixaEtaria(List<LocalDate> datasNascimento) {
    Map<String, Long> faixas = new LinkedHashMap<>();
    faixas.put("0-12", 0L);
    faixas.put("13-17", 0L);
    faixas.put("18-29", 0L);
    faixas.put("30-59", 0L);
    faixas.put("60+", 0L);

    LocalDate hoje = LocalDate.now();
    for (LocalDate nascimento : datasNascimento) {
      int idade = Period.between(nascimento, hoje).getYears();
      if (idade <= 12) {
        faixas.compute("0-12", (k, v) -> v + 1);
      } else if (idade <= 17) {
        faixas.compute("13-17", (k, v) -> v + 1);
      } else if (idade <= 29) {
        faixas.compute("18-29", (k, v) -> v + 1);
      } else if (idade <= 59) {
        faixas.compute("30-59", (k, v) -> v + 1);
      } else {
        faixas.compute("60+", (k, v) -> v + 1);
      }
    }

    return faixas;
  }

  private Map<String, Long> calcularIdades(List<LocalDate> datasNascimento) {
    Map<Integer, Long> idadesContador = new LinkedHashMap<>();
    LocalDate hoje = LocalDate.now();

    for (LocalDate nascimento : datasNascimento) {
      if (nascimento == null) {
        continue;
      }
      int idade = Period.between(nascimento, hoje).getYears();
      if (idade < 0) {
        continue;
      }
      idadesContador.merge(idade, 1L, Long::sum);
    }

    if (idadesContador.isEmpty()) {
      return Collections.emptyMap();
    }

    Map<String, Long> idadesOrdenadas = new LinkedHashMap<>();
    idadesContador.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .forEach(entry -> idadesOrdenadas.put(String.valueOf(entry.getKey()), entry.getValue()));
    return idadesOrdenadas;
  }

  private List<BigDecimal> parseRendas(List<String> rendas) {
    List<BigDecimal> valores = new ArrayList<>();
    for (String renda : rendas) {
      BigDecimal valor = parseValorMonetario(renda);
      if (valor != null) {
        valores.add(valor);
      }
    }
    return valores;
  }

  private BigDecimal parseValorMonetario(String valor) {
    if (valor == null || valor.trim().isEmpty()) {
      return null;
    }
    String normalizado = valor.replace("R$", "").trim();
    normalizado = normalizado.replaceAll("[^0-9,\\.]", "");

    if (normalizado.contains(",")) {
      normalizado = normalizado.replace(".", "").replace(",", ".");
    } else if (normalizado.contains(".")) {
      int ultimaPosicao = normalizado.lastIndexOf('.');
      int casasDecimais = normalizado.length() - ultimaPosicao - 1;
      if (normalizado.chars().filter(ch -> ch == '.').count() > 1 || casasDecimais != 2) {
        normalizado = normalizado.replace(".", "");
      }
    }

    normalizado = normalizado.replaceAll("[^0-9.]", "");
    if (normalizado.isEmpty()) {
      return null;
    }
    try {
      return new BigDecimal(normalizado);
    } catch (NumberFormatException ex) {
      return null;
    }
  }

  private double calcularMedia(List<BigDecimal> valores) {
    if (valores.isEmpty()) {
      return 0.0;
    }
    BigDecimal soma = BigDecimal.ZERO;
    for (BigDecimal valor : valores) {
      soma = soma.add(valor);
    }
    return soma.divide(BigDecimal.valueOf(valores.size()), 2, RoundingMode.HALF_UP).doubleValue();
  }

  private Map<String, Long> calcularFaixaRenda(List<BigDecimal> rendas) {
    Map<String, Long> faixas = new LinkedHashMap<>();
    faixas.put("Ate 200", 0L);
    faixas.put("201-500", 0L);
    faixas.put("501-1000", 0L);
    faixas.put("Acima 1000", 0L);

    for (BigDecimal renda : rendas) {
      if (renda.compareTo(BigDecimal.valueOf(200)) <= 0) {
        faixas.compute("Ate 200", (k, v) -> v + 1);
      } else if (renda.compareTo(BigDecimal.valueOf(500)) <= 0) {
        faixas.compute("201-500", (k, v) -> v + 1);
      } else if (renda.compareTo(BigDecimal.valueOf(1000)) <= 0) {
        faixas.compute("501-1000", (k, v) -> v + 1);
      } else {
        faixas.compute("Acima 1000", (k, v) -> v + 1);
      }
    }

    return faixas;
  }

  private double arredondar(double valor) {
    return BigDecimal.valueOf(valor).setScale(1, RoundingMode.HALF_UP).doubleValue();
  }
}
