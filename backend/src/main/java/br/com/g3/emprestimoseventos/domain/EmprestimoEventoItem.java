package br.com.g3.emprestimoseventos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "emprestimos_eventos_itens")
public class EmprestimoEventoItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "emprestimo_id", nullable = false)
  private EmprestimoEvento emprestimo;

  @Column(name = "item_id", nullable = false)
  private Long itemId;

  @Column(name = "tipo_item", length = 20, nullable = false)
  private String tipoItem;

  @Column(name = "quantidade", nullable = false)
  private Integer quantidade;

  @Column(name = "status_item", length = 20, nullable = false)
  private String statusItem;

  @Column(name = "observacao_item")
  private String observacaoItem;

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

  public EmprestimoEvento getEmprestimo() {
    return emprestimo;
  }

  public void setEmprestimo(EmprestimoEvento emprestimo) {
    this.emprestimo = emprestimo;
  }

  public Long getItemId() {
    return itemId;
  }

  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }

  public String getTipoItem() {
    return tipoItem;
  }

  public void setTipoItem(String tipoItem) {
    this.tipoItem = tipoItem;
  }

  public Integer getQuantidade() {
    return quantidade;
  }

  public void setQuantidade(Integer quantidade) {
    this.quantidade = quantidade;
  }

  public String getStatusItem() {
    return statusItem;
  }

  public void setStatusItem(String statusItem) {
    this.statusItem = statusItem;
  }

  public String getObservacaoItem() {
    return observacaoItem;
  }

  public void setObservacaoItem(String observacaoItem) {
    this.observacaoItem = observacaoItem;
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
