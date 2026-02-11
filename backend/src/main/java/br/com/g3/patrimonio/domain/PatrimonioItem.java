package br.com.g3.patrimonio.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patrimonio_item")
public class PatrimonioItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "numero_patrimonio", nullable = false, length = 60)
  private String numeroPatrimonio;

  @Column(nullable = false, length = 200)
  private String nome;

  private String categoria;
  private String subcategoria;
  private String conservacao;
  private String status;

  @Column(name = "data_aquisicao")
  private LocalDate dataAquisicao;

  @Column(name = "valor_aquisicao")
  private BigDecimal valorAquisicao;

  @Column(name = "doacao_id")
  private Long doacaoId;

  private String origem;
  private String responsavel;
  private String unidade;
  private String sala;

  @Column(name = "taxa_depreciacao")
  private BigDecimal taxaDepreciacao;

  private String observacoes;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  @Column(name = "atualizado_em", nullable = false)
  private LocalDateTime atualizadoEm;

  @OneToMany(mappedBy = "patrimonio", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PatrimonioMovimentacao> movimentos = new ArrayList<>();

  @PrePersist
  public void prePersist() {
    LocalDateTime agora = LocalDateTime.now();
    criadoEm = agora;
    atualizadoEm = agora;
  }

  @PreUpdate
  public void preUpdate() {
    atualizadoEm = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNumeroPatrimonio() {
    return numeroPatrimonio;
  }

  public void setNumeroPatrimonio(String numeroPatrimonio) {
    this.numeroPatrimonio = numeroPatrimonio;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public String getSubcategoria() {
    return subcategoria;
  }

  public void setSubcategoria(String subcategoria) {
    this.subcategoria = subcategoria;
  }

  public String getConservacao() {
    return conservacao;
  }

  public void setConservacao(String conservacao) {
    this.conservacao = conservacao;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDate getDataAquisicao() {
    return dataAquisicao;
  }

  public void setDataAquisicao(LocalDate dataAquisicao) {
    this.dataAquisicao = dataAquisicao;
  }

  public BigDecimal getValorAquisicao() {
    return valorAquisicao;
  }

  public void setValorAquisicao(BigDecimal valorAquisicao) {
    this.valorAquisicao = valorAquisicao;
  }

  public Long getDoacaoId() {
    return doacaoId;
  }

  public void setDoacaoId(Long doacaoId) {
    this.doacaoId = doacaoId;
  }

  public String getOrigem() {
    return origem;
  }

  public void setOrigem(String origem) {
    this.origem = origem;
  }

  public String getResponsavel() {
    return responsavel;
  }

  public void setResponsavel(String responsavel) {
    this.responsavel = responsavel;
  }

  public String getUnidade() {
    return unidade;
  }

  public void setUnidade(String unidade) {
    this.unidade = unidade;
  }

  public String getSala() {
    return sala;
  }

  public void setSala(String sala) {
    this.sala = sala;
  }

  public BigDecimal getTaxaDepreciacao() {
    return taxaDepreciacao;
  }

  public void setTaxaDepreciacao(BigDecimal taxaDepreciacao) {
    this.taxaDepreciacao = taxaDepreciacao;
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

  public List<PatrimonioMovimentacao> getMovimentos() {
    return movimentos;
  }

  public void setMovimentos(List<PatrimonioMovimentacao> movimentos) {
    this.movimentos = movimentos;
  }

  public void adicionarMovimento(PatrimonioMovimentacao movimento) {
    movimentos.add(movimento);
    movimento.setPatrimonio(this);
  }
}
