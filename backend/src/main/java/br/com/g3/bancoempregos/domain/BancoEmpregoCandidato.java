package br.com.g3.bancoempregos.domain;

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
@Table(name = "banco_empregos_candidatos")
public class BancoEmpregoCandidato {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "emprego_id", nullable = false)
  private Long empregoId;

  @Column(name = "beneficiario_id")
  private Long beneficiarioId;

  @Column(name = "beneficiario_nome", length = 200, nullable = false)
  private String beneficiarioNome;

  @Column(name = "necessidades_profissionais", columnDefinition = "TEXT")
  private String necessidadesProfissionais;

  @Column(name = "status", length = 40, nullable = false)
  private String status;

  @Column(name = "curriculo_nome", length = 255)
  private String curriculoNome;

  @Column(name = "curriculo_tipo", length = 120)
  private String curriculoTipo;

  @Column(name = "curriculo_conteudo", columnDefinition = "TEXT")
  private String curriculoConteudo;

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

  public Long getEmpregoId() {
    return empregoId;
  }

  public void setEmpregoId(Long empregoId) {
    this.empregoId = empregoId;
  }

  public Long getBeneficiarioId() {
    return beneficiarioId;
  }

  public void setBeneficiarioId(Long beneficiarioId) {
    this.beneficiarioId = beneficiarioId;
  }

  public String getBeneficiarioNome() {
    return beneficiarioNome;
  }

  public void setBeneficiarioNome(String beneficiarioNome) {
    this.beneficiarioNome = beneficiarioNome;
  }

  public String getNecessidadesProfissionais() {
    return necessidadesProfissionais;
  }

  public void setNecessidadesProfissionais(String necessidadesProfissionais) {
    this.necessidadesProfissionais = necessidadesProfissionais;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getCurriculoNome() {
    return curriculoNome;
  }

  public void setCurriculoNome(String curriculoNome) {
    this.curriculoNome = curriculoNome;
  }

  public String getCurriculoTipo() {
    return curriculoTipo;
  }

  public void setCurriculoTipo(String curriculoTipo) {
    this.curriculoTipo = curriculoTipo;
  }

  public String getCurriculoConteudo() {
    return curriculoConteudo;
  }

  public void setCurriculoConteudo(String curriculoConteudo) {
    this.curriculoConteudo = curriculoConteudo;
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
