package br.com.g3.cadastrobeneficiario.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AptidaoCestaBasicaRequest {

  @JsonProperty("opta_receber_cesta_basica")
  private Boolean optaReceberCestaBasica;

  @JsonProperty("apto_receber_cesta_basica")
  private Boolean aptoReceberCestaBasica;

  public Boolean getOptaReceberCestaBasica() {
    return optaReceberCestaBasica;
  }

  public void setOptaReceberCestaBasica(Boolean optaReceberCestaBasica) {
    this.optaReceberCestaBasica = optaReceberCestaBasica;
  }

  public Boolean getAptoReceberCestaBasica() {
    return aptoReceberCestaBasica;
  }

  public void setAptoReceberCestaBasica(Boolean aptoReceberCestaBasica) {
    this.aptoReceberCestaBasica = aptoReceberCestaBasica;
  }
}
