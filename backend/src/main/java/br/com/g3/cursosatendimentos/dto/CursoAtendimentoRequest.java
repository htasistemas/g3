package br.com.g3.cursosatendimentos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CursoAtendimentoRequest {
  private String tipo;
  private String nome;
  private String descricao;
  private String imagem;
  private Integer vagasTotais;
  private Integer vagasDisponiveis;
  private Integer cargaHoraria;
  private String horarioInicial;
  private Integer duracaoHoras;
  private List<String> diasSemana = new ArrayList<>();
  private List<String> faixasEtarias = new ArrayList<>();
  private Boolean vagaPreferencialIdosos;
  private String sexoPermitido;
  private String restricoes;
  private String profissional;
  private Long salaId;
  private String status;
  private LocalDate dataTriagem;
  private LocalDate dataEncaminhamento;
  private LocalDate dataConclusao;

  @JsonProperty("enrollments")
  private List<CursoAtendimentoMatriculaRequest> matriculas = new ArrayList<>();

  @JsonProperty("waitlist")
  private List<CursoAtendimentoFilaEsperaRequest> filaEspera = new ArrayList<>();

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public String getImagem() {
    return imagem;
  }

  public void setImagem(String imagem) {
    this.imagem = imagem;
  }

  public Integer getVagasTotais() {
    return vagasTotais;
  }

  public void setVagasTotais(Integer vagasTotais) {
    this.vagasTotais = vagasTotais;
  }

  public Integer getVagasDisponiveis() {
    return vagasDisponiveis;
  }

  public void setVagasDisponiveis(Integer vagasDisponiveis) {
    this.vagasDisponiveis = vagasDisponiveis;
  }

  public Integer getCargaHoraria() {
    return cargaHoraria;
  }

  public void setCargaHoraria(Integer cargaHoraria) {
    this.cargaHoraria = cargaHoraria;
  }

  public String getHorarioInicial() {
    return horarioInicial;
  }

  public void setHorarioInicial(String horarioInicial) {
    this.horarioInicial = horarioInicial;
  }

  public Integer getDuracaoHoras() {
    return duracaoHoras;
  }

  public void setDuracaoHoras(Integer duracaoHoras) {
    this.duracaoHoras = duracaoHoras;
  }

  public List<String> getDiasSemana() {
    return diasSemana;
  }

  public void setDiasSemana(List<String> diasSemana) {
    this.diasSemana = diasSemana;
  }

  public List<String> getFaixasEtarias() {
    return faixasEtarias;
  }

  public void setFaixasEtarias(List<String> faixasEtarias) {
    this.faixasEtarias = faixasEtarias;
  }

  public Boolean getVagaPreferencialIdosos() {
    return vagaPreferencialIdosos;
  }

  public void setVagaPreferencialIdosos(Boolean vagaPreferencialIdosos) {
    this.vagaPreferencialIdosos = vagaPreferencialIdosos;
  }

  public String getSexoPermitido() {
    return sexoPermitido;
  }

  public void setSexoPermitido(String sexoPermitido) {
    this.sexoPermitido = sexoPermitido;
  }

  public String getRestricoes() {
    return restricoes;
  }

  public void setRestricoes(String restricoes) {
    this.restricoes = restricoes;
  }

  public String getProfissional() {
    return profissional;
  }

  public void setProfissional(String profissional) {
    this.profissional = profissional;
  }

  public Long getSalaId() {
    return salaId;
  }

  public void setSalaId(Long salaId) {
    this.salaId = salaId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDate getDataTriagem() {
    return dataTriagem;
  }

  public void setDataTriagem(LocalDate dataTriagem) {
    this.dataTriagem = dataTriagem;
  }

  public LocalDate getDataEncaminhamento() {
    return dataEncaminhamento;
  }

  public void setDataEncaminhamento(LocalDate dataEncaminhamento) {
    this.dataEncaminhamento = dataEncaminhamento;
  }

  public LocalDate getDataConclusao() {
    return dataConclusao;
  }

  public void setDataConclusao(LocalDate dataConclusao) {
    this.dataConclusao = dataConclusao;
  }

  public List<CursoAtendimentoMatriculaRequest> getMatriculas() {
    return matriculas;
  }

  public void setMatriculas(List<CursoAtendimentoMatriculaRequest> matriculas) {
    this.matriculas = matriculas;
  }

  public List<CursoAtendimentoFilaEsperaRequest> getFilaEspera() {
    return filaEspera;
  }

  public void setFilaEspera(List<CursoAtendimentoFilaEsperaRequest> filaEspera) {
    this.filaEspera = filaEspera;
  }
}
