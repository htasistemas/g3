package br.com.g3.cursosatendimentos.domain;

import br.com.g3.unidadeassistencial.domain.SalaUnidade;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cursos_atendimentos")
public class CursoAtendimento {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "tipo", nullable = false, length = 20)
  private String tipo;

  @Column(name = "nome", nullable = false, length = 200)
  private String nome;

  @Column(name = "descricao", columnDefinition = "TEXT")
  private String descricao;

  @Column(name = "imagem")
  private String imagem;

  @Column(name = "vagas_totais", nullable = false)
  private Integer vagasTotais;

  @Column(name = "vagas_disponiveis", nullable = false)
  private Integer vagasDisponiveis;

  @Column(name = "carga_horaria")
  private Integer cargaHoraria;

  @Column(name = "horario_inicial")
  private LocalTime horarioInicial;

  @Column(name = "duracao_horas", nullable = false)
  private Integer duracaoHoras;

  @Column(name = "dias_semana")
  private String diasSemana;

  @Column(name = "restricoes", columnDefinition = "TEXT")
  private String restricoes;

  @Column(name = "profissional", length = 150)
  private String profissional;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sala_id")
  private SalaUnidade sala;

  @Column(name = "status", nullable = false, length = 30)
  private String status;

  @Column(name = "data_triagem")
  private LocalDate dataTriagem;

  @Column(name = "data_encaminhamento")
  private LocalDate dataEncaminhamento;

  @Column(name = "data_conclusao")
  private LocalDate dataConclusao;

  @OneToMany(
      mappedBy = "cursoAtendimento",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  private List<CursoAtendimentoMatricula> matriculas = new ArrayList<>();

  @OneToMany(
      mappedBy = "cursoAtendimento",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  private List<CursoAtendimentoFilaEspera> filaEspera = new ArrayList<>();

  @OneToMany(
      mappedBy = "cursoAtendimento",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  private List<CursoAtendimentoStatus> historicoStatus = new ArrayList<>();

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

  public LocalTime getHorarioInicial() {
    return horarioInicial;
  }

  public void setHorarioInicial(LocalTime horarioInicial) {
    this.horarioInicial = horarioInicial;
  }

  public Integer getDuracaoHoras() {
    return duracaoHoras;
  }

  public void setDuracaoHoras(Integer duracaoHoras) {
    this.duracaoHoras = duracaoHoras;
  }

  public String getDiasSemana() {
    return diasSemana;
  }

  public void setDiasSemana(String diasSemana) {
    this.diasSemana = diasSemana;
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

  public SalaUnidade getSala() {
    return sala;
  }

  public void setSala(SalaUnidade sala) {
    this.sala = sala;
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

  public List<CursoAtendimentoMatricula> getMatriculas() {
    return matriculas;
  }

  public void setMatriculas(List<CursoAtendimentoMatricula> matriculas) {
    this.matriculas = matriculas;
  }

  public List<CursoAtendimentoFilaEspera> getFilaEspera() {
    return filaEspera;
  }

  public void setFilaEspera(List<CursoAtendimentoFilaEspera> filaEspera) {
    this.filaEspera = filaEspera;
  }

  public List<CursoAtendimentoStatus> getHistoricoStatus() {
    return historicoStatus;
  }

  public void setHistoricoStatus(List<CursoAtendimentoStatus> historicoStatus) {
    this.historicoStatus = historicoStatus;
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
