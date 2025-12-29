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
@Table(name = "situacao_social")
public class SituacaoSocialBeneficiario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "beneficiario_id", nullable = false)
  private CadastroBeneficiario beneficiario;

  @Column(name = "mora_com_familia")
  private Boolean moraComFamilia;

  @Column(name = "responsavel_legal")
  private Boolean responsavelLegal;

  @Column(name = "vinculo_familiar", length = 150)
  private String vinculoFamiliar;

  @Column(name = "situacao_vulnerabilidade")
  private String situacaoVulnerabilidade;

  @Column(name = "composicao_familiar")
  private String composicaoFamiliar;

  @Column(name = "criancas_adolescentes")
  private Integer criancasAdolescentes;

  @Column(name = "idosos")
  private Integer idosos;

  @Column(name = "acompanhamento_cras")
  private Boolean acompanhamentoCras;

  @Column(name = "acompanhamento_saude")
  private Boolean acompanhamentoSaude;

  @Column(name = "participa_comunidade", length = 200)
  private String participaComunidade;

  @Column(name = "rede_apoio")
  private String redeApoio;

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

  public Boolean getMoraComFamilia() {
    return moraComFamilia;
  }

  public void setMoraComFamilia(Boolean moraComFamilia) {
    this.moraComFamilia = moraComFamilia;
  }

  public Boolean getResponsavelLegal() {
    return responsavelLegal;
  }

  public void setResponsavelLegal(Boolean responsavelLegal) {
    this.responsavelLegal = responsavelLegal;
  }

  public String getVinculoFamiliar() {
    return vinculoFamiliar;
  }

  public void setVinculoFamiliar(String vinculoFamiliar) {
    this.vinculoFamiliar = vinculoFamiliar;
  }

  public String getSituacaoVulnerabilidade() {
    return situacaoVulnerabilidade;
  }

  public void setSituacaoVulnerabilidade(String situacaoVulnerabilidade) {
    this.situacaoVulnerabilidade = situacaoVulnerabilidade;
  }

  public String getComposicaoFamiliar() {
    return composicaoFamiliar;
  }

  public void setComposicaoFamiliar(String composicaoFamiliar) {
    this.composicaoFamiliar = composicaoFamiliar;
  }

  public Integer getCriancasAdolescentes() {
    return criancasAdolescentes;
  }

  public void setCriancasAdolescentes(Integer criancasAdolescentes) {
    this.criancasAdolescentes = criancasAdolescentes;
  }

  public Integer getIdosos() {
    return idosos;
  }

  public void setIdosos(Integer idosos) {
    this.idosos = idosos;
  }

  public Boolean getAcompanhamentoCras() {
    return acompanhamentoCras;
  }

  public void setAcompanhamentoCras(Boolean acompanhamentoCras) {
    this.acompanhamentoCras = acompanhamentoCras;
  }

  public Boolean getAcompanhamentoSaude() {
    return acompanhamentoSaude;
  }

  public void setAcompanhamentoSaude(Boolean acompanhamentoSaude) {
    this.acompanhamentoSaude = acompanhamentoSaude;
  }

  public String getParticipaComunidade() {
    return participaComunidade;
  }

  public void setParticipaComunidade(String participaComunidade) {
    this.participaComunidade = participaComunidade;
  }

  public String getRedeApoio() {
    return redeApoio;
  }

  public void setRedeApoio(String redeApoio) {
    this.redeApoio = redeApoio;
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
