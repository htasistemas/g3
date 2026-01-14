package br.com.g3.gerenciamentodados.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "gerenciamento_dados_backup")
public class GerenciamentoDadosBackup {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "codigo", length = 30, nullable = false)
  private String codigo;

  @Column(name = "rotulo", length = 200, nullable = false)
  private String rotulo;

  @Column(name = "tipo", length = 20, nullable = false)
  private String tipo;

  @Column(name = "status", length = 20, nullable = false)
  private String status;

  @Column(name = "iniciado_em", nullable = false)
  private LocalDateTime iniciadoEm;

  @Column(name = "armazenado_em", length = 60)
  private String armazenadoEm;

  @Column(name = "tamanho", length = 40)
  private String tamanho;

  @Column(name = "criptografado", nullable = false)
  private Boolean criptografado;

  @Column(name = "retencao_dias", nullable = false)
  private Integer retencaoDias;

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

  public String getRotulo() {
    return rotulo;
  }

  public void setRotulo(String rotulo) {
    this.rotulo = rotulo;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDateTime getIniciadoEm() {
    return iniciadoEm;
  }

  public void setIniciadoEm(LocalDateTime iniciadoEm) {
    this.iniciadoEm = iniciadoEm;
  }

  public String getArmazenadoEm() {
    return armazenadoEm;
  }

  public void setArmazenadoEm(String armazenadoEm) {
    this.armazenadoEm = armazenadoEm;
  }

  public String getTamanho() {
    return tamanho;
  }

  public void setTamanho(String tamanho) {
    this.tamanho = tamanho;
  }

  public Boolean getCriptografado() {
    return criptografado;
  }

  public void setCriptografado(Boolean criptografado) {
    this.criptografado = criptografado;
  }

  public Integer getRetencaoDias() {
    return retencaoDias;
  }

  public void setRetencaoDias(Integer retencaoDias) {
    this.retencaoDias = retencaoDias;
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
