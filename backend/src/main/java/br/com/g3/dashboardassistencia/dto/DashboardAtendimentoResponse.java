package br.com.g3.dashboardassistencia.dto;

import java.util.Map;

public class DashboardAtendimentoResponse {
  private final long totalBeneficiarios;
  private final long ativos;
  private final long pendentes;
  private final long bloqueados;
  private final long emAnalise;
  private final long desatualizados;
  private final double cadastroCompletoPercentual;
  private final long beneficiariosPeriodo;
  private final long novosBeneficiarios;
  private final long reincidentes;
  private final Map<String, Long> faixaEtaria;
  private final Map<String, Long> vulnerabilidades;

  public DashboardAtendimentoResponse(
      long totalBeneficiarios,
      long ativos,
      long pendentes,
      long bloqueados,
      long emAnalise,
      long desatualizados,
      double cadastroCompletoPercentual,
      long beneficiariosPeriodo,
      long novosBeneficiarios,
      long reincidentes,
      Map<String, Long> faixaEtaria,
      Map<String, Long> vulnerabilidades) {
    this.totalBeneficiarios = totalBeneficiarios;
    this.ativos = ativos;
    this.pendentes = pendentes;
    this.bloqueados = bloqueados;
    this.emAnalise = emAnalise;
    this.desatualizados = desatualizados;
    this.cadastroCompletoPercentual = cadastroCompletoPercentual;
    this.beneficiariosPeriodo = beneficiariosPeriodo;
    this.novosBeneficiarios = novosBeneficiarios;
    this.reincidentes = reincidentes;
    this.faixaEtaria = faixaEtaria;
    this.vulnerabilidades = vulnerabilidades;
  }

  public long getTotalBeneficiarios() {
    return totalBeneficiarios;
  }

  public long getAtivos() {
    return ativos;
  }

  public long getPendentes() {
    return pendentes;
  }

  public long getBloqueados() {
    return bloqueados;
  }

  public long getEmAnalise() {
    return emAnalise;
  }

  public long getDesatualizados() {
    return desatualizados;
  }

  public double getCadastroCompletoPercentual() {
    return cadastroCompletoPercentual;
  }

  public long getBeneficiariosPeriodo() {
    return beneficiariosPeriodo;
  }

  public long getNovosBeneficiarios() {
    return novosBeneficiarios;
  }

  public long getReincidentes() {
    return reincidentes;
  }

  public Map<String, Long> getFaixaEtaria() {
    return faixaEtaria;
  }

  public Map<String, Long> getVulnerabilidades() {
    return vulnerabilidades;
  }
}
