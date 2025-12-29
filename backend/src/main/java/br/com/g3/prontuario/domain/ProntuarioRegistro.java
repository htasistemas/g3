package br.com.g3.prontuario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "prontuario_registros")
public class ProntuarioRegistro {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "beneficiario_id", nullable = false)
  private Long beneficiarioId;

  @Column(name = "tipo", nullable = false, length = 40)
  private String tipo;

  @Column(name = "data_registro", nullable = false)
  private LocalDateTime dataRegistro;

  @Column(name = "profissional_id")
  private Long profissionalId;

  @Column(name = "unidade_id")
  private Long unidadeId;

  @Column(name = "titulo", length = 200)
  private String titulo;

  @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
  private String descricao;

  @Column(name = "dados_extra", columnDefinition = "JSONB")
  private String dadosExtra;

  @Column(name = "status", nullable = false, length = 20)
  private String status;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  @Column(name = "criado_por")
  private Long criadoPor;

  @Column(name = "atualizado_em", nullable = false)
  private LocalDateTime atualizadoEm;

  @Column(name = "atualizado_por")
  private Long atualizadoPor;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getBeneficiarioId() {
    return beneficiarioId;
  }

  public void setBeneficiarioId(Long beneficiarioId) {
    this.beneficiarioId = beneficiarioId;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public LocalDateTime getDataRegistro() {
    return dataRegistro;
  }

  public void setDataRegistro(LocalDateTime dataRegistro) {
    this.dataRegistro = dataRegistro;
  }

  public Long getProfissionalId() {
    return profissionalId;
  }

  public void setProfissionalId(Long profissionalId) {
    this.profissionalId = profissionalId;
  }

  public Long getUnidadeId() {
    return unidadeId;
  }

  public void setUnidadeId(Long unidadeId) {
    this.unidadeId = unidadeId;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public String getDadosExtra() {
    return dadosExtra;
  }

  public void setDadosExtra(String dadosExtra) {
    this.dadosExtra = dadosExtra;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }

  public Long getCriadoPor() {
    return criadoPor;
  }

  public void setCriadoPor(Long criadoPor) {
    this.criadoPor = criadoPor;
  }

  public LocalDateTime getAtualizadoEm() {
    return atualizadoEm;
  }

  public void setAtualizadoEm(LocalDateTime atualizadoEm) {
    this.atualizadoEm = atualizadoEm;
  }

  public Long getAtualizadoPor() {
    return atualizadoPor;
  }

  public void setAtualizadoPor(Long atualizadoPor) {
    this.atualizadoPor = atualizadoPor;
  }
}
