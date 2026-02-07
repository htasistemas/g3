package br.com.g3.lembretesdiarios.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class LembreteDiarioResponse {
  private Long id;
  private String titulo;
  private String descricao;
  private LocalDate dataInicial;
  private String recorrencia;
  private LocalTime horaAviso;
  private String status;
  private LocalDateTime proximaExecucaoEm;
  private LocalDateTime adiadoAte;
  private LocalDateTime concluidoEm;
  private LocalDateTime criadoEm;
  private LocalDateTime atualizadoEm;

  public LembreteDiarioResponse(
      Long id,
      String titulo,
      String descricao,
      LocalDate dataInicial,
      String recorrencia,
      LocalTime horaAviso,
      String status,
      LocalDateTime proximaExecucaoEm,
      LocalDateTime adiadoAte,
      LocalDateTime concluidoEm,
      LocalDateTime criadoEm,
      LocalDateTime atualizadoEm) {
    this.id = id;
    this.titulo = titulo;
    this.descricao = descricao;
    this.dataInicial = dataInicial;
    this.recorrencia = recorrencia;
    this.horaAviso = horaAviso;
    this.status = status;
    this.proximaExecucaoEm = proximaExecucaoEm;
    this.adiadoAte = adiadoAte;
    this.concluidoEm = concluidoEm;
    this.criadoEm = criadoEm;
    this.atualizadoEm = atualizadoEm;
  }

  public Long getId() {
    return id;
  }

  public String getTitulo() {
    return titulo;
  }

  public String getDescricao() {
    return descricao;
  }

  public LocalDate getDataInicial() {
    return dataInicial;
  }

  public String getRecorrencia() {
    return recorrencia;
  }

  public LocalTime getHoraAviso() {
    return horaAviso;
  }

  public String getStatus() {
    return status;
  }

  public LocalDateTime getProximaExecucaoEm() {
    return proximaExecucaoEm;
  }

  public LocalDateTime getAdiadoAte() {
    return adiadoAte;
  }

  public LocalDateTime getConcluidoEm() {
    return concluidoEm;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public LocalDateTime getAtualizadoEm() {
    return atualizadoEm;
  }
}
