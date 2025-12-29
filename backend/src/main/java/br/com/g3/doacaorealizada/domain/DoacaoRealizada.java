package br.com.g3.doacaorealizada.domain;

import br.com.g3.cadastrobeneficiario.domain.CadastroBeneficiario;
import br.com.g3.vinculofamiliar.domain.VinculoFamiliar;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doacao_realizada")
public class DoacaoRealizada {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "beneficiario_id")
  private CadastroBeneficiario beneficiario;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "vinculo_familiar_id")
  private VinculoFamiliar vinculoFamiliar;

  @Column(name = "tipo_doacao", length = 120, nullable = false)
  private String tipoDoacao;

  @Column(name = "situacao", length = 60, nullable = false)
  private String situacao;

  @Column(name = "responsavel", length = 150)
  private String responsavel;

  @Column(name = "observacoes")
  private String observacoes;

  @Column(name = "data_doacao", nullable = false)
  private LocalDate dataDoacao;

  @OneToMany(
      mappedBy = "doacaoRealizada",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private List<DoacaoRealizadaItem> itens = new ArrayList<>();

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

  public VinculoFamiliar getVinculoFamiliar() {
    return vinculoFamiliar;
  }

  public void setVinculoFamiliar(VinculoFamiliar vinculoFamiliar) {
    this.vinculoFamiliar = vinculoFamiliar;
  }

  public String getTipoDoacao() {
    return tipoDoacao;
  }

  public void setTipoDoacao(String tipoDoacao) {
    this.tipoDoacao = tipoDoacao;
  }

  public String getSituacao() {
    return situacao;
  }

  public void setSituacao(String situacao) {
    this.situacao = situacao;
  }

  public String getResponsavel() {
    return responsavel;
  }

  public void setResponsavel(String responsavel) {
    this.responsavel = responsavel;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public LocalDate getDataDoacao() {
    return dataDoacao;
  }

  public void setDataDoacao(LocalDate dataDoacao) {
    this.dataDoacao = dataDoacao;
  }

  public List<DoacaoRealizadaItem> getItens() {
    return itens;
  }

  public void setItens(List<DoacaoRealizadaItem> itens) {
    this.itens = itens;
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
