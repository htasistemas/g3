package br.com.g3.almoxarifado.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "produtos_kit_composicao")
public class ProdutoKitComposicao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "produto_kit_id", nullable = false)
  private Long produtoKitId;

  @Column(name = "produto_item_id", nullable = false)
  private Long produtoItemId;

  @Column(name = "quantidade_item", nullable = false, precision = 12, scale = 4)
  private BigDecimal quantidadeItem;

  @Column(name = "ativo", nullable = false)
  private Boolean ativo;

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

  public Long getProdutoKitId() {
    return produtoKitId;
  }

  public void setProdutoKitId(Long produtoKitId) {
    this.produtoKitId = produtoKitId;
  }

  public Long getProdutoItemId() {
    return produtoItemId;
  }

  public void setProdutoItemId(Long produtoItemId) {
    this.produtoItemId = produtoItemId;
  }

  public BigDecimal getQuantidadeItem() {
    return quantidadeItem;
  }

  public void setQuantidadeItem(BigDecimal quantidadeItem) {
    this.quantidadeItem = quantidadeItem;
  }

  public Boolean getAtivo() {
    return ativo;
  }

  public void setAtivo(Boolean ativo) {
    this.ativo = ativo;
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
