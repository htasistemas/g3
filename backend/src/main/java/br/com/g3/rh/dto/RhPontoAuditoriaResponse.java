package br.com.g3.rh.dto;

import java.time.LocalDateTime;

public class RhPontoAuditoriaResponse {
  private Long id;
  private Long funcionarioId;
  private Long unidadeId;
  private String tipoMarcacao;
  private String ipDetectado;
  private String userAgent;
  private LocalDateTime dataHoraServidor;
  private String resultado;
  private String motivo;
  private String acao;
  private String detalhes;
  private Long pontoMarcacaoId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getFuncionarioId() {
    return funcionarioId;
  }

  public void setFuncionarioId(Long funcionarioId) {
    this.funcionarioId = funcionarioId;
  }

  public Long getUnidadeId() {
    return unidadeId;
  }

  public void setUnidadeId(Long unidadeId) {
    this.unidadeId = unidadeId;
  }

  public String getTipoMarcacao() {
    return tipoMarcacao;
  }

  public void setTipoMarcacao(String tipoMarcacao) {
    this.tipoMarcacao = tipoMarcacao;
  }

  public String getIpDetectado() {
    return ipDetectado;
  }

  public void setIpDetectado(String ipDetectado) {
    this.ipDetectado = ipDetectado;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public LocalDateTime getDataHoraServidor() {
    return dataHoraServidor;
  }

  public void setDataHoraServidor(LocalDateTime dataHoraServidor) {
    this.dataHoraServidor = dataHoraServidor;
  }

  public String getResultado() {
    return resultado;
  }

  public void setResultado(String resultado) {
    this.resultado = resultado;
  }

  public String getMotivo() {
    return motivo;
  }

  public void setMotivo(String motivo) {
    this.motivo = motivo;
  }

  public String getAcao() {
    return acao;
  }

  public void setAcao(String acao) {
    this.acao = acao;
  }

  public String getDetalhes() {
    return detalhes;
  }

  public void setDetalhes(String detalhes) {
    this.detalhes = detalhes;
  }

  public Long getPontoMarcacaoId() {
    return pontoMarcacaoId;
  }

  public void setPontoMarcacaoId(Long pontoMarcacaoId) {
    this.pontoMarcacaoId = pontoMarcacaoId;
  }
}
