package br.com.g3.informacoesadministrativas.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "g3_auditoria_info_admin")
public class InformacaoAdministrativaAuditoria {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "info_admin_id", nullable = false)
  private Long infoAdminId;

  @Column(name = "acao", length = 40, nullable = false)
  private String acao;

  @Column(name = "usuario_id")
  private Long usuarioId;

  @Column(name = "usuario_nome", length = 160)
  private String usuarioNome;

  @Column(name = "data_hora", nullable = false)
  private LocalDateTime dataHora;

  @Column(name = "ip_origem", length = 120)
  private String ipOrigem;

  @Column(name = "detalhes", columnDefinition = "text")
  private String detalhes;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getInfoAdminId() {
    return infoAdminId;
  }

  public void setInfoAdminId(Long infoAdminId) {
    this.infoAdminId = infoAdminId;
  }

  public String getAcao() {
    return acao;
  }

  public void setAcao(String acao) {
    this.acao = acao;
  }

  public Long getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(Long usuarioId) {
    this.usuarioId = usuarioId;
  }

  public String getUsuarioNome() {
    return usuarioNome;
  }

  public void setUsuarioNome(String usuarioNome) {
    this.usuarioNome = usuarioNome;
  }

  public LocalDateTime getDataHora() {
    return dataHora;
  }

  public void setDataHora(LocalDateTime dataHora) {
    this.dataHora = dataHora;
  }

  public String getIpOrigem() {
    return ipOrigem;
  }

  public void setIpOrigem(String ipOrigem) {
    this.ipOrigem = ipOrigem;
  }

  public String getDetalhes() {
    return detalhes;
  }

  public void setDetalhes(String detalhes) {
    this.detalhes = detalhes;
  }
}
