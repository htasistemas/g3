package br.com.g3.planotrabalho.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "plano_trabalho_etapas")
public class PlanoTrabalhoEtapa {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "atividade_id", nullable = false)
  private Long atividadeId;

  @Column(name = "descricao", columnDefinition = "TEXT", nullable = false)
  private String descricao;

  @Column(name = "status", length = 30)
  private String status;

  @Column(name = "data_inicio_prevista")
  private LocalDate dataInicioPrevista;

  @Column(name = "data_fim_prevista")
  private LocalDate dataFimPrevista;

  @Column(name = "data_conclusao")
  private LocalDate dataConclusao;

  @Column(name = "responsavel", length = 200)
  private String responsavel;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getAtividadeId() {
    return atividadeId;
  }

  public void setAtividadeId(Long atividadeId) {
    this.atividadeId = atividadeId;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDate getDataInicioPrevista() {
    return dataInicioPrevista;
  }

  public void setDataInicioPrevista(LocalDate dataInicioPrevista) {
    this.dataInicioPrevista = dataInicioPrevista;
  }

  public LocalDate getDataFimPrevista() {
    return dataFimPrevista;
  }

  public void setDataFimPrevista(LocalDate dataFimPrevista) {
    this.dataFimPrevista = dataFimPrevista;
  }

  public LocalDate getDataConclusao() {
    return dataConclusao;
  }

  public void setDataConclusao(LocalDate dataConclusao) {
    this.dataConclusao = dataConclusao;
  }

  public String getResponsavel() {
    return responsavel;
  }

  public void setResponsavel(String responsavel) {
    this.responsavel = responsavel;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
