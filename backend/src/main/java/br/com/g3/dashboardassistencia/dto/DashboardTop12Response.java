package br.com.g3.dashboardassistencia.dto;

import java.util.Map;

public class DashboardTop12Response {
  private final long beneficiariosAtendidosPeriodo;
  private final long familiasExtremaPobreza;
  private final double rendaMediaFamiliar;
  private final long cursosAtivos;
  private final double taxaMediaOcupacaoCursos;
  private final long certificadosEmitidos;
  private final double doacoesPeriodo;
  private final Map<String, Long> itensDoadoResumo;
  private final long visitasDomiciliares;
  private final long termosVencendo;
  private final double execucaoFinanceira;
  private final double absenteismo;

  public DashboardTop12Response(
      long beneficiariosAtendidosPeriodo,
      long familiasExtremaPobreza,
      double rendaMediaFamiliar,
      long cursosAtivos,
      double taxaMediaOcupacaoCursos,
      long certificadosEmitidos,
      double doacoesPeriodo,
      Map<String, Long> itensDoadoResumo,
      long visitasDomiciliares,
      long termosVencendo,
      double execucaoFinanceira,
      double absenteismo) {
    this.beneficiariosAtendidosPeriodo = beneficiariosAtendidosPeriodo;
    this.familiasExtremaPobreza = familiasExtremaPobreza;
    this.rendaMediaFamiliar = rendaMediaFamiliar;
    this.cursosAtivos = cursosAtivos;
    this.taxaMediaOcupacaoCursos = taxaMediaOcupacaoCursos;
    this.certificadosEmitidos = certificadosEmitidos;
    this.doacoesPeriodo = doacoesPeriodo;
    this.itensDoadoResumo = itensDoadoResumo;
    this.visitasDomiciliares = visitasDomiciliares;
    this.termosVencendo = termosVencendo;
    this.execucaoFinanceira = execucaoFinanceira;
    this.absenteismo = absenteismo;
  }

  public long getBeneficiariosAtendidosPeriodo() {
    return beneficiariosAtendidosPeriodo;
  }

  public long getFamiliasExtremaPobreza() {
    return familiasExtremaPobreza;
  }

  public double getRendaMediaFamiliar() {
    return rendaMediaFamiliar;
  }

  public long getCursosAtivos() {
    return cursosAtivos;
  }

  public double getTaxaMediaOcupacaoCursos() {
    return taxaMediaOcupacaoCursos;
  }

  public long getCertificadosEmitidos() {
    return certificadosEmitidos;
  }

  public double getDoacoesPeriodo() {
    return doacoesPeriodo;
  }

  public Map<String, Long> getItensDoadoResumo() {
    return itensDoadoResumo;
  }

  public long getVisitasDomiciliares() {
    return visitasDomiciliares;
  }

  public long getTermosVencendo() {
    return termosVencendo;
  }

  public double getExecucaoFinanceira() {
    return execucaoFinanceira;
  }

  public double getAbsenteismo() {
    return absenteismo;
  }
}
