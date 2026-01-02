package br.com.g3.visitadomiciliar.dto;

public class VisitaDomiciliarAnexoRequest {
  private String nome;
  private String tipo;
  private String tamanho;

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
}
