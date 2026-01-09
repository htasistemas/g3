package br.com.g3.almoxarifado.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao_vinculo_kit")
public class MovimentacaoVinculoKit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "movimentacao_principal_id", nullable = false)
  private Long movimentacaoPrincipalId;

  @Column(name = "movimentacao_gerada_id", nullable = false)
  private Long movimentacaoGeradaId;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getMovimentacaoPrincipalId() {
    return movimentacaoPrincipalId;
  }

  public void setMovimentacaoPrincipalId(Long movimentacaoPrincipalId) {
    this.movimentacaoPrincipalId = movimentacaoPrincipalId;
  }

  public Long getMovimentacaoGeradaId() {
    return movimentacaoGeradaId;
  }

  public void setMovimentacaoGeradaId(Long movimentacaoGeradaId) {
    this.movimentacaoGeradaId = movimentacaoGeradaId;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
