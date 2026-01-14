package br.com.g3.cursosatendimentos.dto;

import br.com.g3.unidadeassistencial.dto.SalaUnidadeResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CursoAtendimentoResponse {
  private String id;
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
  private String salaId;
  private SalaUnidadeResponse sala;

  @JsonProperty("createdAt")
  private LocalDateTime criadoEm;

  @JsonProperty("updatedAt")
  private LocalDateTime atualizadoEm;

  private String status;

  @JsonProperty("statusHistory")
  private List<CursoAtendimentoStatusResponse> historicoStatus = new ArrayList<>();

  private LocalDate dataTriagem;
  private LocalDate dataEncaminhamento;
  private LocalDate dataConclusao;

  @JsonProperty("enrollments")
  private List<CursoAtendimentoMatriculaResponse> matriculas = new ArrayList<>();

  @JsonProperty("waitlist")
  private List<CursoAtendimentoFilaEsperaResponse> filaEspera = new ArrayList<>();

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

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

  public String getSalaId() {
    return salaId;
  }

  public void setSalaId(String salaId) {
    this.salaId = salaId;
  }

  public SalaUnidadeResponse getSala() {
    return sala;
  }

  public void setSala(SalaUnidadeResponse sala) {
    this.sala = sala;
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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public List<CursoAtendimentoStatusResponse> getHistoricoStatus() {
    return historicoStatus;
  }

  public void setHistoricoStatus(List<CursoAtendimentoStatusResponse> historicoStatus) {
    this.historicoStatus = historicoStatus;
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

  public List<CursoAtendimentoMatriculaResponse> getMatriculas() {
    return matriculas;
  }

  public void setMatriculas(List<CursoAtendimentoMatriculaResponse> matriculas) {
    this.matriculas = matriculas;
  }

  public List<CursoAtendimentoFilaEsperaResponse> getFilaEspera() {
    return filaEspera;
  }

  public void setFilaEspera(List<CursoAtendimentoFilaEsperaResponse> filaEspera) {
    this.filaEspera = filaEspera;
  }
}
