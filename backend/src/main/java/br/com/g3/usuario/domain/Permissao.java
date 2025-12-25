package br.com.g3.usuario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissao")
public class Permissao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "nome", length = 60, nullable = false, unique = true)
  private String nome;

  @ManyToMany(mappedBy = "permissoes")
  private Set<Usuario> usuarios = new HashSet<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public Set<Usuario> getUsuarios() {
    return usuarios;
  }

  public void setUsuarios(Set<Usuario> usuarios) {
    this.usuarios = usuarios;
  }
}
