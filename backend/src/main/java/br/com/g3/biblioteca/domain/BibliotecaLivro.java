package br.com.g3.biblioteca.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "biblioteca_livros")
public class BibliotecaLivro {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "codigo", length = 40, nullable = false, unique = true)
  private String codigo;

  @Column(name = "titulo", length = 220, nullable = false)
  private String titulo;

  @Column(name = "autor", length = 200, nullable = false)
  private String autor;

  @Column(name = "isbn", length = 40)
  private String isbn;

  @Column(name = "editora", length = 180)
  private String editora;

  @Column(name = "ano_publicacao")
  private Integer anoPublicacao;

  @Column(name = "categoria", length = 120)
  private String categoria;

  @Column(name = "quantidade_total", nullable = false)
  private Integer quantidadeTotal;

  @Column(name = "quantidade_disponivel", nullable = false)
  private Integer quantidadeDisponivel;

  @Column(name = "localizacao", length = 160)
  private String localizacao;

  @Column(name = "status", length = 20, nullable = false)
  private String status;

  @Column(name = "estado_livro", length = 30)
  private String estadoLivro;

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

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getAutor() {
    return autor;
  }

  public void setAutor(String autor) {
    this.autor = autor;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public String getEditora() {
    return editora;
  }

  public void setEditora(String editora) {
    this.editora = editora;
  }

  public Integer getAnoPublicacao() {
    return anoPublicacao;
  }

  public void setAnoPublicacao(Integer anoPublicacao) {
    this.anoPublicacao = anoPublicacao;
  }

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public Integer getQuantidadeTotal() {
    return quantidadeTotal;
  }

  public void setQuantidadeTotal(Integer quantidadeTotal) {
    this.quantidadeTotal = quantidadeTotal;
  }

  public Integer getQuantidadeDisponivel() {
    return quantidadeDisponivel;
  }

  public void setQuantidadeDisponivel(Integer quantidadeDisponivel) {
    this.quantidadeDisponivel = quantidadeDisponivel;
  }

  public String getLocalizacao() {
    return localizacao;
  }

  public void setLocalizacao(String localizacao) {
    this.localizacao = localizacao;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getEstadoLivro() {
    return estadoLivro;
  }

  public void setEstadoLivro(String estadoLivro) {
    this.estadoLivro = estadoLivro;
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
