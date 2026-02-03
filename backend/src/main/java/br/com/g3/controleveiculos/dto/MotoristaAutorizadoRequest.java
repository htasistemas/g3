package br.com.g3.controleveiculos.dto;

public class MotoristaAutorizadoRequest {
  private Long veiculoId;
  private String tipoOrigem;
  private Long motoristaId;
  private String numeroCarteira;
  private String categoriaCarteira;
  private String vencimentoCarteira;
  private String arquivoCarteiraPdf;

  public Long getVeiculoId() {
    return veiculoId;
  }

  public void setVeiculoId(Long veiculoId) {
    this.veiculoId = veiculoId;
  }

  public String getTipoOrigem() {
    return tipoOrigem;
  }

  public void setTipoOrigem(String tipoOrigem) {
    this.tipoOrigem = tipoOrigem;
  }

  public Long getMotoristaId() {
    return motoristaId;
  }

  public void setMotoristaId(Long motoristaId) {
    this.motoristaId = motoristaId;
  }

  public String getNumeroCarteira() {
    return numeroCarteira;
  }

  public void setNumeroCarteira(String numeroCarteira) {
    this.numeroCarteira = numeroCarteira;
  }

  public String getCategoriaCarteira() {
    return categoriaCarteira;
  }

  public void setCategoriaCarteira(String categoriaCarteira) {
    this.categoriaCarteira = categoriaCarteira;
  }

  public String getVencimentoCarteira() {
    return vencimentoCarteira;
  }

  public void setVencimentoCarteira(String vencimentoCarteira) {
    this.vencimentoCarteira = vencimentoCarteira;
  }

  public String getArquivoCarteiraPdf() {
    return arquivoCarteiraPdf;
  }

  public void setArquivoCarteiraPdf(String arquivoCarteiraPdf) {
    this.arquivoCarteiraPdf = arquivoCarteiraPdf;
  }
}
