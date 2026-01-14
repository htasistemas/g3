package br.com.g3.gerenciamentodados.dto;

public class GerenciamentoDadosConfiguracaoRequest {
  private String frequencia;
  private String horarioExecucao;
  private String horarioIncremental;
  private Integer retencaoDias;
  private Boolean automacaoAtiva;
  private Integer limiteBanda;
  private Boolean pausarHorarioComercial;
  private String provedor;
  private String caminhoDestino;
  private String bucketNome;
  private Boolean criptografia;
  private String compressao;
  private Boolean verificarIntegridade;
  private String emailNotificacao;
  private Boolean copiaExterna;
  private Boolean alertas;
  private Boolean deteccaoAnomalia;
  private Boolean autoVerificacao;
  private String integracoes;
  private Boolean simulacaoRetencao;

  public String getFrequencia() {
    return frequencia;
  }

  public void setFrequencia(String frequencia) {
    this.frequencia = frequencia;
  }

  public String getHorarioExecucao() {
    return horarioExecucao;
  }

  public void setHorarioExecucao(String horarioExecucao) {
    this.horarioExecucao = horarioExecucao;
  }

  public String getHorarioIncremental() {
    return horarioIncremental;
  }

  public void setHorarioIncremental(String horarioIncremental) {
    this.horarioIncremental = horarioIncremental;
  }

  public Integer getRetencaoDias() {
    return retencaoDias;
  }

  public void setRetencaoDias(Integer retencaoDias) {
    this.retencaoDias = retencaoDias;
  }

  public Boolean getAutomacaoAtiva() {
    return automacaoAtiva;
  }

  public void setAutomacaoAtiva(Boolean automacaoAtiva) {
    this.automacaoAtiva = automacaoAtiva;
  }

  public Integer getLimiteBanda() {
    return limiteBanda;
  }

  public void setLimiteBanda(Integer limiteBanda) {
    this.limiteBanda = limiteBanda;
  }

  public Boolean getPausarHorarioComercial() {
    return pausarHorarioComercial;
  }

  public void setPausarHorarioComercial(Boolean pausarHorarioComercial) {
    this.pausarHorarioComercial = pausarHorarioComercial;
  }

  public String getProvedor() {
    return provedor;
  }

  public void setProvedor(String provedor) {
    this.provedor = provedor;
  }

  public String getCaminhoDestino() {
    return caminhoDestino;
  }

  public void setCaminhoDestino(String caminhoDestino) {
    this.caminhoDestino = caminhoDestino;
  }

  public String getBucketNome() {
    return bucketNome;
  }

  public void setBucketNome(String bucketNome) {
    this.bucketNome = bucketNome;
  }

  public Boolean getCriptografia() {
    return criptografia;
  }

  public void setCriptografia(Boolean criptografia) {
    this.criptografia = criptografia;
  }

  public String getCompressao() {
    return compressao;
  }

  public void setCompressao(String compressao) {
    this.compressao = compressao;
  }

  public Boolean getVerificarIntegridade() {
    return verificarIntegridade;
  }

  public void setVerificarIntegridade(Boolean verificarIntegridade) {
    this.verificarIntegridade = verificarIntegridade;
  }

  public String getEmailNotificacao() {
    return emailNotificacao;
  }

  public void setEmailNotificacao(String emailNotificacao) {
    this.emailNotificacao = emailNotificacao;
  }

  public Boolean getCopiaExterna() {
    return copiaExterna;
  }

  public void setCopiaExterna(Boolean copiaExterna) {
    this.copiaExterna = copiaExterna;
  }

  public Boolean getAlertas() {
    return alertas;
  }

  public void setAlertas(Boolean alertas) {
    this.alertas = alertas;
  }

  public Boolean getDeteccaoAnomalia() {
    return deteccaoAnomalia;
  }

  public void setDeteccaoAnomalia(Boolean deteccaoAnomalia) {
    this.deteccaoAnomalia = deteccaoAnomalia;
  }

  public Boolean getAutoVerificacao() {
    return autoVerificacao;
  }

  public void setAutoVerificacao(Boolean autoVerificacao) {
    this.autoVerificacao = autoVerificacao;
  }

  public String getIntegracoes() {
    return integracoes;
  }

  public void setIntegracoes(String integracoes) {
    this.integracoes = integracoes;
  }

  public Boolean getSimulacaoRetencao() {
    return simulacaoRetencao;
  }

  public void setSimulacaoRetencao(Boolean simulacaoRetencao) {
    this.simulacaoRetencao = simulacaoRetencao;
  }
}
