package br.com.g3.patrimonio.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "patrimonio_movimentacao")
public class PatrimonioMovimentacao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "patrimonio_id", nullable = false)
  private PatrimonioItem patrimonio;

  @Column(nullable = false, length = 30)
  private String tipo;

  private String destino;
  private String responsavel;
  private String observacao;

  @Column(name = "data_movimento", nullable = false)
  private LocalDate dataMovimento;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  @PrePersist
  public void prePersist() {
    criadoEm = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PatrimonioItem getPatrimonio() {
    return patrimonio;
  }

  public void setPatrimonio(PatrimonioItem patrimonio) {
    this.patrimonio = patrimonio;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getDestino() {
    return destino;
  }

  public void setDestino(String destino) {
    this.destino = destino;
  }

  public String getResponsavel() {
    return responsavel;
  }

  public void setResponsavel(String responsavel) {
    this.responsavel = responsavel;
  }

  public String getObservacao() {
    return observacao;
  }

  public void setObservacao(String observacao) {
    this.observacao = observacao;
  }

  public LocalDate getDataMovimento() {
    return dataMovimento;
  }

  public void setDataMovimento(LocalDate dataMovimento) {
    this.dataMovimento = dataMovimento;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
