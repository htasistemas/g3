package br.com.g3.controleveiculos.dto;

public class MotoristaAutorizadoResponse {
  private final Long id;
  private final Long veiculoId;
  private final String placaVeiculo;
  private final String modeloVeiculo;
  private final String tipoOrigem;
  private final Long motoristaId;
  private final String nomeMotorista;
  private final String numeroCarteira;
  private final String categoriaCarteira;
  private final String vencimentoCarteira;
  private final String arquivoCarteiraPdf;

  public MotoristaAutorizadoResponse(
      Long id,
      Long veiculoId,
      String placaVeiculo,
      String modeloVeiculo,
      String tipoOrigem,
      Long motoristaId,
      String nomeMotorista,
      String numeroCarteira,
      String categoriaCarteira,
      String vencimentoCarteira,
      String arquivoCarteiraPdf) {
    this.id = id;
    this.veiculoId = veiculoId;
    this.placaVeiculo = placaVeiculo;
    this.modeloVeiculo = modeloVeiculo;
    this.tipoOrigem = tipoOrigem;
    this.motoristaId = motoristaId;
    this.nomeMotorista = nomeMotorista;
    this.numeroCarteira = numeroCarteira;
    this.categoriaCarteira = categoriaCarteira;
    this.vencimentoCarteira = vencimentoCarteira;
    this.arquivoCarteiraPdf = arquivoCarteiraPdf;
  }

  public Long getId() {
    return id;
  }

  public Long getVeiculoId() {
    return veiculoId;
  }

  public String getPlacaVeiculo() {
    return placaVeiculo;
  }

  public String getModeloVeiculo() {
    return modeloVeiculo;
  }

  public String getTipoOrigem() {
    return tipoOrigem;
  }

  public Long getMotoristaId() {
    return motoristaId;
  }

  public String getNomeMotorista() {
    return nomeMotorista;
  }

  public String getNumeroCarteira() {
    return numeroCarteira;
  }

  public String getCategoriaCarteira() {
    return categoriaCarteira;
  }

  public String getVencimentoCarteira() {
    return vencimentoCarteira;
  }

  public String getArquivoCarteiraPdf() {
    return arquivoCarteiraPdf;
  }
}
