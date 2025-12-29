package br.com.g3.vinculofamiliar.domain;

import br.com.g3.cadastrobeneficiario.domain.CadastroBeneficiario;
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
@Table(name = "vinculo_familiar_membro")
public class VinculoFamiliarMembro {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "vinculo_familiar_id", nullable = false)
  private VinculoFamiliar vinculoFamiliar;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "beneficiario_id", nullable = false)
  private CadastroBeneficiario beneficiario;

  @Column(name = "parentesco", length = 120)
  private String parentesco;

  @Column(name = "responsavel_familiar")
  private Boolean responsavelFamiliar;

  @Column(name = "contribui_renda")
  private Boolean contribuiRenda;

  @Column(name = "renda_individual", length = 60)
  private String rendaIndividual;

  @Column(name = "participa_servicos")
  private Boolean participaServicos;

  @Column(name = "observacoes")
  private String observacoes;

  @Column(name = "usa_endereco_familia")
  private Boolean usaEnderecoFamilia;

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

  public VinculoFamiliar getVinculoFamiliar() {
    return vinculoFamiliar;
  }

  public void setVinculoFamiliar(VinculoFamiliar vinculoFamiliar) {
    this.vinculoFamiliar = vinculoFamiliar;
  }

  public CadastroBeneficiario getBeneficiario() {
    return beneficiario;
  }

  public void setBeneficiario(CadastroBeneficiario beneficiario) {
    this.beneficiario = beneficiario;
  }

  public String getParentesco() {
    return parentesco;
  }

  public void setParentesco(String parentesco) {
    this.parentesco = parentesco;
  }

  public Boolean getResponsavelFamiliar() {
    return responsavelFamiliar;
  }

  public void setResponsavelFamiliar(Boolean responsavelFamiliar) {
    this.responsavelFamiliar = responsavelFamiliar;
  }

  public Boolean getContribuiRenda() {
    return contribuiRenda;
  }

  public void setContribuiRenda(Boolean contribuiRenda) {
    this.contribuiRenda = contribuiRenda;
  }

  public String getRendaIndividual() {
    return rendaIndividual;
  }

  public void setRendaIndividual(String rendaIndividual) {
    this.rendaIndividual = rendaIndividual;
  }

  public Boolean getParticipaServicos() {
    return participaServicos;
  }

  public void setParticipaServicos(Boolean participaServicos) {
    this.participaServicos = participaServicos;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public Boolean getUsaEnderecoFamilia() {
    return usaEnderecoFamilia;
  }

  public void setUsaEnderecoFamilia(Boolean usaEnderecoFamilia) {
    this.usaEnderecoFamilia = usaEnderecoFamilia;
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
