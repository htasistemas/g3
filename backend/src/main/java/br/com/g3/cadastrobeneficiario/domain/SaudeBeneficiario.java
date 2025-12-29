package br.com.g3.cadastrobeneficiario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "saude_beneficiario")
public class SaudeBeneficiario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "beneficiario_id", nullable = false)
  private CadastroBeneficiario beneficiario;

  @Column(name = "possui_deficiencia")
  private Boolean possuiDeficiencia;

  @Column(name = "tipo_deficiencia", length = 200)
  private String tipoDeficiencia;

  @Column(name = "cid_principal", length = 60)
  private String cidPrincipal;

  @Column(name = "usa_medicacao_continua")
  private Boolean usaMedicacaoContinua;

  @Column(name = "descricao_medicacao")
  private String descricaoMedicacao;

  @Column(name = "servico_saude_referencia", length = 200)
  private String servicoSaudeReferencia;

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

  public CadastroBeneficiario getBeneficiario() {
    return beneficiario;
  }

  public void setBeneficiario(CadastroBeneficiario beneficiario) {
    this.beneficiario = beneficiario;
  }

  public Boolean getPossuiDeficiencia() {
    return possuiDeficiencia;
  }

  public void setPossuiDeficiencia(Boolean possuiDeficiencia) {
    this.possuiDeficiencia = possuiDeficiencia;
  }

  public String getTipoDeficiencia() {
    return tipoDeficiencia;
  }

  public void setTipoDeficiencia(String tipoDeficiencia) {
    this.tipoDeficiencia = tipoDeficiencia;
  }

  public String getCidPrincipal() {
    return cidPrincipal;
  }

  public void setCidPrincipal(String cidPrincipal) {
    this.cidPrincipal = cidPrincipal;
  }

  public Boolean getUsaMedicacaoContinua() {
    return usaMedicacaoContinua;
  }

  public void setUsaMedicacaoContinua(Boolean usaMedicacaoContinua) {
    this.usaMedicacaoContinua = usaMedicacaoContinua;
  }

  public String getDescricaoMedicacao() {
    return descricaoMedicacao;
  }

  public void setDescricaoMedicacao(String descricaoMedicacao) {
    this.descricaoMedicacao = descricaoMedicacao;
  }

  public String getServicoSaudeReferencia() {
    return servicoSaudeReferencia;
  }

  public void setServicoSaudeReferencia(String servicoSaudeReferencia) {
    this.servicoSaudeReferencia = servicoSaudeReferencia;
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
