package br.com.g3.autenticacao.domain;

import br.com.g3.usuario.domain.Usuario;
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
@Table(name = "usuario_recuperacao_senha")
public class UsuarioRecuperacaoSenha {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

  @Column(name = "token", length = 120, nullable = false)
  private String token;

  @Column(name = "expira_em", nullable = false)
  private LocalDateTime expiraEm;

  @Column(name = "usado_em")
  private LocalDateTime usadoEm;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  public Long getId() {
    return id;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public LocalDateTime getExpiraEm() {
    return expiraEm;
  }

  public void setExpiraEm(LocalDateTime expiraEm) {
    this.expiraEm = expiraEm;
  }

  public LocalDateTime getUsadoEm() {
    return usadoEm;
  }

  public void setUsadoEm(LocalDateTime usadoEm) {
    this.usadoEm = usadoEm;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
