package br.com.g3.manualsistema.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "manual_sistema_mudancas")
public class ManualSistemaMudanca {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "data_mudanca", nullable = false)
  private LocalDateTime dataMudanca;

  @Column(name = "autor", length = 120)
  private String autor;

  @Column(name = "modulo", length = 120)
  private String modulo;

  @Column(name = "tela", length = 120)
  private String tela;

  @Column(name = "tipo", length = 30)
  private String tipo;

  @Column(name = "descricao_curta", length = 200)
  private String descricaoCurta;

  @Column(name = "descricao_detalhada", columnDefinition = "TEXT")
  private String descricaoDetalhada;

  @Column(name = "versao_build", length = 40)
  private String versaoBuild;

  @Column(name = "links", columnDefinition = "TEXT")
  private String links;

  @Column(name = "criado_em")
  private LocalDateTime criadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getDataMudanca() {
    return dataMudanca;
  }

  public void setDataMudanca(LocalDateTime dataMudanca) {
    this.dataMudanca = dataMudanca;
  }

  public String getAutor() {
    return autor;
  }

  public void setAutor(String autor) {
    this.autor = autor;
  }

  public String getModulo() {
    return modulo;
  }

  public void setModulo(String modulo) {
    this.modulo = modulo;
  }

  public String getTela() {
    return tela;
  }

  public void setTela(String tela) {
    this.tela = tela;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getDescricaoCurta() {
    return descricaoCurta;
  }

  public void setDescricaoCurta(String descricaoCurta) {
    this.descricaoCurta = descricaoCurta;
  }

  public String getDescricaoDetalhada() {
    return descricaoDetalhada;
  }

  public void setDescricaoDetalhada(String descricaoDetalhada) {
    this.descricaoDetalhada = descricaoDetalhada;
  }

  public String getVersaoBuild() {
    return versaoBuild;
  }

  public void setVersaoBuild(String versaoBuild) {
    this.versaoBuild = versaoBuild;
  }

  public String getLinks() {
    return links;
  }

  public void setLinks(String links) {
    this.links = links;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
