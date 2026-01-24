package br.com.g3.biblioteca.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "biblioteca_emprestimos")
public class BibliotecaEmprestimo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "livro_id", nullable = false)
  private BibliotecaLivro livro;

  @Column(name = "beneficiario_id")
  private Long beneficiarioId;

  @Column(name = "beneficiario_nome", length = 200)
  private String beneficiarioNome;

  @Column(name = "responsavel_id")
  private Long responsavelId;

  @Column(name = "responsavel_nome", length = 200)
  private String responsavelNome;

  @Column(name = "data_emprestimo", nullable = false)
  private LocalDate dataEmprestimo;

  @Column(name = "data_devolucao_prevista", nullable = false)
  private LocalDate dataDevolucaoPrevista;

  @Column(name = "data_devolucao_real")
  private LocalDate dataDevolucaoReal;

  @Column(name = "status", length = 20, nullable = false)
  private String status;

  @Column(name = "observacoes")
  private String observacoes;

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

  public BibliotecaLivro getLivro() {
    return livro;
  }

  public void setLivro(BibliotecaLivro livro) {
    this.livro = livro;
  }

  public Long getBeneficiarioId() {
    return beneficiarioId;
  }

  public void setBeneficiarioId(Long beneficiarioId) {
    this.beneficiarioId = beneficiarioId;
  }

  public String getBeneficiarioNome() {
    return beneficiarioNome;
  }

  public void setBeneficiarioNome(String beneficiarioNome) {
    this.beneficiarioNome = beneficiarioNome;
  }

  public Long getResponsavelId() {
    return responsavelId;
  }

  public void setResponsavelId(Long responsavelId) {
    this.responsavelId = responsavelId;
  }

  public String getResponsavelNome() {
    return responsavelNome;
  }

  public void setResponsavelNome(String responsavelNome) {
    this.responsavelNome = responsavelNome;
  }

  public LocalDate getDataEmprestimo() {
    return dataEmprestimo;
  }

  public void setDataEmprestimo(LocalDate dataEmprestimo) {
    this.dataEmprestimo = dataEmprestimo;
  }

  public LocalDate getDataDevolucaoPrevista() {
    return dataDevolucaoPrevista;
  }

  public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
    this.dataDevolucaoPrevista = dataDevolucaoPrevista;
  }

  public LocalDate getDataDevolucaoReal() {
    return dataDevolucaoReal;
  }

  public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) {
    this.dataDevolucaoReal = dataDevolucaoReal;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
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
