package br.com.g3.doacaorealizada.domain;

import br.com.g3.almoxarifado.domain.AlmoxarifadoItem;
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
@Table(name = "doacao_realizada_item")
public class DoacaoRealizadaItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "doacao_realizada_id", nullable = false)
  private DoacaoRealizada doacaoRealizada;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "almoxarifado_item_id", nullable = false)
  private AlmoxarifadoItem item;

  @Column(name = "quantidade", nullable = false)
  private Integer quantidade;

  @Column(name = "observacoes")
  private String observacoes;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public DoacaoRealizada getDoacaoRealizada() {
    return doacaoRealizada;
  }

  public void setDoacaoRealizada(DoacaoRealizada doacaoRealizada) {
    this.doacaoRealizada = doacaoRealizada;
  }

  public AlmoxarifadoItem getItem() {
    return item;
  }

  public void setItem(AlmoxarifadoItem item) {
    this.item = item;
  }

  public Integer getQuantidade() {
    return quantidade;
  }

  public void setQuantidade(Integer quantidade) {
    this.quantidade = quantidade;
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
}
