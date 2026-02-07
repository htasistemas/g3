package br.com.g3.lembretesdiarios.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "lembretes_diarios")
public class LembreteDiario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "titulo", length = 200, nullable = false)
  private String titulo;

  @Column(name = "descricao")
  private String descricao;

  @Column(name = "usuario_id")
  private Long usuarioId;

  @Column(name = "todos_usuarios", nullable = false)
  private boolean todosUsuarios;

  @Column(name = "data_inicial", nullable = false)
  private LocalDate dataInicial;

  @Column(name = "recorrencia", length = 20, nullable = false)
  private String recorrencia;

  @Column(name = "hora_aviso")
  private LocalTime horaAviso;

  @Column(name = "status", length = 20, nullable = false)
  private String status;

  @Column(name = "proxima_execucao_em", nullable = false)
  private LocalDateTime proximaExecucaoEm;

  @Column(name = "adiado_ate")
  private LocalDateTime adiadoAte;

  @Column(name = "concluido_em")
  private LocalDateTime concluidoEm;

  @Column(name = "deletado_em")
  private LocalDateTime deletadoEm;

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

  public Long getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(Long usuarioId) {
    this.usuarioId = usuarioId;
  }

  public boolean isTodosUsuarios() {
    return todosUsuarios;
  }

  public void setTodosUsuarios(boolean todosUsuarios) {
    this.todosUsuarios = todosUsuarios;
  }

  public LocalDate getDataInicial() {
    return dataInicial;
  }

  public void setDataInicial(LocalDate dataInicial) {
    this.dataInicial = dataInicial;
  }

  public String getRecorrencia() {
    return recorrencia;
  }

  public void setRecorrencia(String recorrencia) {
    this.recorrencia = recorrencia;
  }

  public LocalTime getHoraAviso() {
    return horaAviso;
  }

  public void setHoraAviso(LocalTime horaAviso) {
    this.horaAviso = horaAviso;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDateTime getProximaExecucaoEm() {
    return proximaExecucaoEm;
  }

  public void setProximaExecucaoEm(LocalDateTime proximaExecucaoEm) {
    this.proximaExecucaoEm = proximaExecucaoEm;
  }

  public LocalDateTime getAdiadoAte() {
    return adiadoAte;
  }

  public void setAdiadoAte(LocalDateTime adiadoAte) {
    this.adiadoAte = adiadoAte;
  }

  public LocalDateTime getConcluidoEm() {
    return concluidoEm;
  }

  public void setConcluidoEm(LocalDateTime concluidoEm) {
    this.concluidoEm = concluidoEm;
  }

  public LocalDateTime getDeletadoEm() {
    return deletadoEm;
  }

  public void setDeletadoEm(LocalDateTime deletadoEm) {
    this.deletadoEm = deletadoEm;
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
