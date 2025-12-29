package br.com.g3.dashboardassistencia.dto;

import java.util.Map;

public class DashboardFamiliasResponse {
  private final long total;
  private final double mediaPessoas;
  private final double rendaMediaFamiliar;
  private final double rendaPerCapitaMedia;
  private final Map<String, Long> insegurancaAlimentar;
  private final Map<String, Long> faixaRenda;

  public DashboardFamiliasResponse(
      long total,
      double mediaPessoas,
      double rendaMediaFamiliar,
      double rendaPerCapitaMedia,
      Map<String, Long> insegurancaAlimentar,
      Map<String, Long> faixaRenda) {
    this.total = total;
    this.mediaPessoas = mediaPessoas;
    this.rendaMediaFamiliar = rendaMediaFamiliar;
    this.rendaPerCapitaMedia = rendaPerCapitaMedia;
    this.insegurancaAlimentar = insegurancaAlimentar;
    this.faixaRenda = faixaRenda;
  }

  public long getTotal() {
    return total;
  }

  public double getMediaPessoas() {
    return mediaPessoas;
  }

  public double getRendaMediaFamiliar() {
    return rendaMediaFamiliar;
  }

  public double getRendaPerCapitaMedia() {
    return rendaPerCapitaMedia;
  }

  public Map<String, Long> getInsegurancaAlimentar() {
    return insegurancaAlimentar;
  }

  public Map<String, Long> getFaixaRenda() {
    return faixaRenda;
  }
}
