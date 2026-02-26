package br.com.g3.doacaoplanejada.domain;

import br.com.g3.almoxarifado.domain.AlmoxarifadoItem;
import br.com.g3.cadastrobeneficiario.domain.CadastroBeneficiario;
import br.com.g3.vinculofamiliar.domain.VinculoFamiliar;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "doacao_planejada")
public class DoacaoPlanejada {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "beneficiario_id")
  private CadastroBeneficiario beneficiario;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "vinculo_familiar_id")
  private VinculoFamiliar vinculoFamiliar;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "almoxarifado_item_id", nullable = false)
  private AlmoxarifadoItem item;

  @Column(name = "quantidade", nullable = false)
  private Integer quantidade;

  @Column(name = "data_prevista", nullable = false)
  private LocalDate dataPrevista;

  @Column(name = "prioridade", length = 20, nullable = false)
  private String prioridade;

  @Column(name = "status", length = 20, nullable = false)
  private String status;

  @Column(name = "observacoes")
  private String observacoes;

  @Column(name = "motivo_cancelamento")
  private String motivoCancelamento;

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

  public AlmoxarifadoItem getItem() {
    return item;
  }

  public void setItem(AlmoxarifadoItem item) {
    this.item = item;
  }

  public Integer getQuantidade() {
    return quantidade;
  }

  public void setQuantidade(Integer quantidade) {
    this.quantidade = quantidade;
  }

  public LocalDate getDataPrevista() {
    return dataPrevista;
  }

  public void setDataPrevista(LocalDate dataPrevista) {
    this.dataPrevista = dataPrevista;
  }

  public String getPrioridade() {
    return prioridade;
  }

  public void setPrioridade(String prioridade) {
    this.prioridade = prioridade;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public String getMotivoCancelamento() {
    return motivoCancelamento;
  }

  public void setMotivoCancelamento(String motivoCancelamento) {
    this.motivoCancelamento = motivoCancelamento;
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
