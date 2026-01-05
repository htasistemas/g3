package br.com.g3.oficios.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "oficios")
public class Oficio {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "tipo", nullable = false, length = 20)
  private String tipo;

  @Column(name = "numero", nullable = false, length = 120)
  private String numero;

  @Column(name = "data", nullable = false)
  private LocalDate data;

  @Column(name = "setor_origem", nullable = false, length = 200)
  private String setorOrigem;

  @Column(name = "responsavel", nullable = false, length = 200)
  private String responsavel;

  @Column(name = "destinatario", nullable = false, length = 200)
  private String destinatario;

  @Column(name = "meio_envio", nullable = false, length = 120)
  private String meioEnvio;

  @Column(name = "prazo_resposta", length = 120)
  private String prazoResposta;

  @Column(name = "classificacao", length = 120)
  private String classificacao;

  @Column(name = "razao_social", nullable = false, length = 200)
  private String razaoSocial;

  @Column(name = "logo_url", length = 500)
  private String logoUrl;

  @Column(name = "titulo", length = 200)
  private String titulo;

  @Column(name = "saudacao", length = 200)
  private String saudacao;

  @Column(name = "assunto", nullable = false, length = 300)
  private String assunto;

  @Column(name = "corpo", nullable = false, columnDefinition = "TEXT")
  private String corpo;

  @Column(name = "finalizacao", columnDefinition = "TEXT")
  private String finalizacao;

  @Column(name = "assinatura_nome", length = 200)
  private String assinaturaNome;

  @Column(name = "assinatura_cargo", length = 200)
  private String assinaturaCargo;

  @Column(name = "rodape", columnDefinition = "TEXT")
  private String rodape;

  @Column(name = "status", nullable = false, length = 30)
  private String status;

  @Column(name = "protocolo_envio", length = 120)
  private String protocoloEnvio;

  @Column(name = "data_envio")
  private LocalDate dataEnvio;

  @Column(name = "protocolo_recebimento", length = 120)
  private String protocoloRecebimento;

  @Column(name = "data_recebimento")
  private LocalDate dataRecebimento;

  @Column(name = "proximo_destino", length = 200)
  private String proximoDestino;

  @Column(name = "observacoes", columnDefinition = "TEXT")
  private String observacoes;

  @Column(name = "unidade_id")
  private Long unidadeId;

  @Column(name = "criado_por")
  private Long criadoPor;

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

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public LocalDate getData() {
    return data;
  }

  public void setData(LocalDate data) {
    this.data = data;
  }

  public String getSetorOrigem() {
    return setorOrigem;
  }

  public void setSetorOrigem(String setorOrigem) {
    this.setorOrigem = setorOrigem;
  }

  public String getResponsavel() {
    return responsavel;
  }

  public void setResponsavel(String responsavel) {
    this.responsavel = responsavel;
  }

  public String getDestinatario() {
    return destinatario;
  }

  public void setDestinatario(String destinatario) {
    this.destinatario = destinatario;
  }

  public String getMeioEnvio() {
    return meioEnvio;
  }

  public void setMeioEnvio(String meioEnvio) {
    this.meioEnvio = meioEnvio;
  }

  public String getPrazoResposta() {
    return prazoResposta;
  }

  public void setPrazoResposta(String prazoResposta) {
    this.prazoResposta = prazoResposta;
  }

  public String getClassificacao() {
    return classificacao;
  }

  public void setClassificacao(String classificacao) {
    this.classificacao = classificacao;
  }

  public String getRazaoSocial() {
    return razaoSocial;
  }

  public void setRazaoSocial(String razaoSocial) {
    this.razaoSocial = razaoSocial;
  }

  public String getLogoUrl() {
    return logoUrl;
  }

  public void setLogoUrl(String logoUrl) {
    this.logoUrl = logoUrl;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getSaudacao() {
    return saudacao;
  }

  public void setSaudacao(String saudacao) {
    this.saudacao = saudacao;
  }

  public String getAssunto() {
    return assunto;
  }

  public void setAssunto(String assunto) {
    this.assunto = assunto;
  }

  public String getCorpo() {
    return corpo;
  }

  public void setCorpo(String corpo) {
    this.corpo = corpo;
  }

  public String getFinalizacao() {
    return finalizacao;
  }

  public void setFinalizacao(String finalizacao) {
    this.finalizacao = finalizacao;
  }

  public String getAssinaturaNome() {
    return assinaturaNome;
  }

  public void setAssinaturaNome(String assinaturaNome) {
    this.assinaturaNome = assinaturaNome;
  }

  public String getAssinaturaCargo() {
    return assinaturaCargo;
  }

  public void setAssinaturaCargo(String assinaturaCargo) {
    this.assinaturaCargo = assinaturaCargo;
  }

  public String getRodape() {
    return rodape;
  }

  public void setRodape(String rodape) {
    this.rodape = rodape;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getProtocoloEnvio() {
    return protocoloEnvio;
  }

  public void setProtocoloEnvio(String protocoloEnvio) {
    this.protocoloEnvio = protocoloEnvio;
  }

  public LocalDate getDataEnvio() {
    return dataEnvio;
  }

  public void setDataEnvio(LocalDate dataEnvio) {
    this.dataEnvio = dataEnvio;
  }

  public String getProtocoloRecebimento() {
    return protocoloRecebimento;
  }

  public void setProtocoloRecebimento(String protocoloRecebimento) {
    this.protocoloRecebimento = protocoloRecebimento;
  }

  public LocalDate getDataRecebimento() {
    return dataRecebimento;
  }

  public void setDataRecebimento(LocalDate dataRecebimento) {
    this.dataRecebimento = dataRecebimento;
  }

  public String getProximoDestino() {
    return proximoDestino;
  }

  public void setProximoDestino(String proximoDestino) {
    this.proximoDestino = proximoDestino;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public Long getUnidadeId() {
    return unidadeId;
  }

  public void setUnidadeId(Long unidadeId) {
    this.unidadeId = unidadeId;
  }

  public Long getCriadoPor() {
    return criadoPor;
  }

  public void setCriadoPor(Long criadoPor) {
    this.criadoPor = criadoPor;
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
