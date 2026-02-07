package br.com.g3.lembretesdiarios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class LembreteDiarioRequest {
  @NotBlank(message = "Titulo e obrigatorio")
  private String titulo;

  private String descricao;

  @NotNull(message = "Data inicial e obrigatoria")
  private LocalDate dataInicial;

  private Long usuarioId;

  private Boolean todosUsuarios;

  private LocalTime horaAviso;

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

  public LocalDate getDataInicial() {
    return dataInicial;
  }

  public void setDataInicial(LocalDate dataInicial) {
    this.dataInicial = dataInicial;
  }

  public Long getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(Long usuarioId) {
    this.usuarioId = usuarioId;
  }

  public Boolean getTodosUsuarios() {
    return todosUsuarios;
  }

  public void setTodosUsuarios(Boolean todosUsuarios) {
    this.todosUsuarios = todosUsuarios;
  }

  public LocalTime getHoraAviso() {
    return horaAviso;
  }

  public void setHoraAviso(LocalTime horaAviso) {
    this.horaAviso = horaAviso;
  }
}
