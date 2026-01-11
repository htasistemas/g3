package br.com.g3.alertasistema.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "alertas_sistema")
public class AlertaSistema {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "tipo_alerta", length = 80, nullable = false)
  private String tipoAlerta;

  @Column(name = "frequencia_envio", length = 30, nullable = false)
  private String frequenciaEnvio;

  @Column(name = "ativo", nullable = false)
  private boolean ativo;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  @Column(name = "atualizado_em", nullable = false)
  private LocalDateTime atualizadoEm;

  @PrePersist
  void onCreate() {
    LocalDateTime agora = LocalDateTime.now();
    this.criadoEm = agora;
    this.atualizadoEm = agora;
  }

  @PreUpdate
  void onUpdate() {
    this.atualizadoEm = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTipoAlerta() {
    return tipoAlerta;
  }

  public void setTipoAlerta(String tipoAlerta) {
    this.tipoAlerta = tipoAlerta;
  }

  public String getFrequenciaEnvio() {
    return frequenciaEnvio;
  }

  public void setFrequenciaEnvio(String frequenciaEnvio) {
    this.frequenciaEnvio = frequenciaEnvio;
  }

  public boolean isAtivo() {
    return ativo;
  }

  public void setAtivo(boolean ativo) {
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
