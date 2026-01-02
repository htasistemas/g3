package br.com.g3.visitadomiciliar.dto;

import java.time.LocalDateTime;

public class VisitaDomiciliarAnexoResponse {
  private Long id;
  private Long visitaId;
  private String nome;
  private String tipo;
  private String tamanho;
  private LocalDateTime criadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getVisitaId() {
    return visitaId;
  }

  public void setVisitaId(Long visitaId) {
    this.visitaId = visitaId;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getTamanho() {
    return tamanho;
  }

  public void setTamanho(String tamanho) {
    this.tamanho = tamanho;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
