package br.com.g3.gerenciamentodados.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "gerenciamento_dados")
public class GerenciamentoDadosConfiguracao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "frequencia", length = 20, nullable = false)
  private String frequencia;

  @Column(name = "horario_execucao", length = 8, nullable = false)
  private String horarioExecucao;

  @Column(name = "horario_incremental", length = 8, nullable = false)
  private String horarioIncremental;

  @Column(name = "retencao_dias", nullable = false)
  private Integer retencaoDias;

  @Column(name = "automacao_ativa", nullable = false)
  private Boolean automacaoAtiva;

  @Column(name = "limite_banda", nullable = false)
  private Integer limiteBanda;

  @Column(name = "pausar_horario_comercial", nullable = false)
  private Boolean pausarHorarioComercial;

  @Column(name = "provedor", length = 20, nullable = false)
  private String provedor;

  @Column(name = "caminho_destino", length = 200)
  private String caminhoDestino;

  @Column(name = "bucket_nome", length = 120)
  private String bucketNome;

  @Column(name = "criptografia", nullable = false)
  private Boolean criptografia;

  @Column(name = "compressao", length = 20, nullable = false)
  private String compressao;

  @Column(name = "verificar_integridade", nullable = false)
  private Boolean verificarIntegridade;

  @Column(name = "email_notificacao", length = 160)
  private String emailNotificacao;

  @Column(name = "copia_externa", nullable = false)
  private Boolean copiaExterna;

  @Column(name = "alertas", nullable = false)
  private Boolean alertas;

  @Column(name = "deteccao_anomalia", nullable = false)
  private Boolean deteccaoAnomalia;

  @Column(name = "auto_verificacao", nullable = false)
  private Boolean autoVerificacao;

  @Column(name = "integracoes", length = 40)
  private String integracoes;

  @Column(name = "simulacao_retencao", nullable = false)
  private Boolean simulacaoRetencao;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  @Column(name = "atualizado_em", nullable = false)
  private LocalDateTime atualizadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }

  public LocalDateTime getAtualizadoEm() {
    return atualizadoEm;
  }

  public void setAtualizadoEm(LocalDateTime atualizadoEm) {
    this.atualizadoEm = atualizadoEm;
  }
}
