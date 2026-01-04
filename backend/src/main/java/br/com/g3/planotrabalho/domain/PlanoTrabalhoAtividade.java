package br.com.g3.planotrabalho.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "plano_trabalho_atividades")
public class PlanoTrabalhoAtividade {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "meta_id", nullable = false)
  private Long metaId;

  @Column(name = "descricao", columnDefinition = "TEXT", nullable = false)
  private String descricao;

  @Column(name = "justificativa", columnDefinition = "TEXT")
  private String justificativa;

  @Column(name = "publico_alvo", columnDefinition = "TEXT")
  private String publicoAlvo;

  @Column(name = "local_execucao", columnDefinition = "TEXT")
  private String localExecucao;

  @Column(name = "produto_esperado", columnDefinition = "TEXT")
  private String produtoEsperado;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getMetaId() {
    return metaId;
  }

  public void setMetaId(Long metaId) {
    this.metaId = metaId;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public String getJustificativa() {
    return justificativa;
  }

  public void setJustificativa(String justificativa) {
    this.justificativa = justificativa;
  }

  public String getPublicoAlvo() {
    return publicoAlvo;
  }

  public void setPublicoAlvo(String publicoAlvo) {
    this.publicoAlvo = publicoAlvo;
  }

  public String getLocalExecucao() {
    return localExecucao;
  }

  public void setLocalExecucao(String localExecucao) {
    this.localExecucao = localExecucao;
  }

  public String getProdutoEsperado() {
    return produtoEsperado;
  }

  public void setProdutoEsperado(String produtoEsperado) {
    this.produtoEsperado = produtoEsperado;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
