package br.com.g3.rh.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rh_ponto_dia")
public class RhPontoDia {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "funcionario_id", nullable = false)
  private Long funcionarioId;

  @Column(name = "data", nullable = false)
  private LocalDate data;

  @Column(name = "ocorrencia", nullable = false, length = 30)
  private String ocorrencia;

  @Column(name = "observacoes")
  private String observacoes;

  @Column(name = "carga_prevista_minutos", nullable = false)
  private Integer cargaPrevistaMinutos;

  @Column(name = "tolerancia_minutos", nullable = false)
  private Integer toleranciaMinutos;

  @Column(name = "total_trabalhado_minutos", nullable = false)
  private Integer totalTrabalhadoMinutos;

  @Column(name = "extras_minutos", nullable = false)
  private Integer extrasMinutos;

  @Column(name = "faltas_atrasos_minutos", nullable = false)
  private Integer faltasAtrasosMinutos;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  @Column(name = "atualizado_em", nullable = false)
  private LocalDateTime atualizadoEm;

  @OneToMany(mappedBy = "pontoDia", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<RhPontoMarcacao> marcacoes = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getFuncionarioId() {
    return funcionarioId;
  }

  public void setFuncionarioId(Long funcionarioId) {
    this.funcionarioId = funcionarioId;
  }

  public LocalDate getData() {
    return data;
  }

  public void setData(LocalDate data) {
    this.data = data;
  }

  public String getOcorrencia() {
    return ocorrencia;
  }

  public void setOcorrencia(String ocorrencia) {
    this.ocorrencia = ocorrencia;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public Integer getCargaPrevistaMinutos() {
    return cargaPrevistaMinutos;
  }

  public void setCargaPrevistaMinutos(Integer cargaPrevistaMinutos) {
    this.cargaPrevistaMinutos = cargaPrevistaMinutos;
  }

  public Integer getToleranciaMinutos() {
    return toleranciaMinutos;
  }

  public void setToleranciaMinutos(Integer toleranciaMinutos) {
    this.toleranciaMinutos = toleranciaMinutos;
  }

  public Integer getTotalTrabalhadoMinutos() {
    return totalTrabalhadoMinutos;
  }

  public void setTotalTrabalhadoMinutos(Integer totalTrabalhadoMinutos) {
    this.totalTrabalhadoMinutos = totalTrabalhadoMinutos;
  }

  public Integer getExtrasMinutos() {
    return extrasMinutos;
  }

  public void setExtrasMinutos(Integer extrasMinutos) {
    this.extrasMinutos = extrasMinutos;
  }

  public Integer getFaltasAtrasosMinutos() {
    return faltasAtrasosMinutos;
  }

  public void setFaltasAtrasosMinutos(Integer faltasAtrasosMinutos) {
    this.faltasAtrasosMinutos = faltasAtrasosMinutos;
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

  public List<RhPontoMarcacao> getMarcacoes() {
    return marcacoes;
  }

  public void setMarcacoes(List<RhPontoMarcacao> marcacoes) {
    this.marcacoes = marcacoes;
  }
}
