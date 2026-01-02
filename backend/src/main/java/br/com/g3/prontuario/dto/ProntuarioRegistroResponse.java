package br.com.g3.prontuario.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class ProntuarioRegistroResponse {
  private Long id;
  private Long beneficiarioId;
  private String tipo;
  private LocalDateTime dataRegistro;
  private Long profissionalId;
  private Long unidadeId;
  private Long familiaId;
  private String titulo;
  private String descricao;
  private Map<String, Object> dadosExtra;
  private String status;
  private String referenciaOrigemTipo;
  private Long referenciaOrigemId;
  private String nivelSigilo;
  private LocalDateTime criadoEm;
  private Long criadoPor;
  private LocalDateTime atualizadoEm;
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

  public Long getFamiliaId() {
    return familiaId;
  }

  public void setFamiliaId(Long familiaId) {
    this.familiaId = familiaId;
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

  public Map<String, Object> getDadosExtra() {
    return dadosExtra;
  }

  public void setDadosExtra(Map<String, Object> dadosExtra) {
    this.dadosExtra = dadosExtra;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getReferenciaOrigemTipo() {
    return referenciaOrigemTipo;
  }

  public void setReferenciaOrigemTipo(String referenciaOrigemTipo) {
    this.referenciaOrigemTipo = referenciaOrigemTipo;
  }

  public Long getReferenciaOrigemId() {
    return referenciaOrigemId;
  }

  public void setReferenciaOrigemId(Long referenciaOrigemId) {
    this.referenciaOrigemId = referenciaOrigemId;
  }

  public String getNivelSigilo() {
    return nivelSigilo;
  }

  public void setNivelSigilo(String nivelSigilo) {
    this.nivelSigilo = nivelSigilo;
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
