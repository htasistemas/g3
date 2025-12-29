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
@Table(name = "beneficios_beneficiario")
public class BeneficiosBeneficiario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "beneficiario_id", nullable = false)
  private CadastroBeneficiario beneficiario;

  @Column(name = "recebe_beneficio")
  private Boolean recebeBeneficio;

  @Column(name = "beneficios_descricao")
  private String beneficiosDescricao;

  @Column(name = "valor_total_beneficios", length = 60)
  private String valorTotalBeneficios;

  @Column(name = "beneficios_recebidos")
  private String beneficiosRecebidos;

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

  public Boolean getRecebeBeneficio() {
    return recebeBeneficio;
  }

  public void setRecebeBeneficio(Boolean recebeBeneficio) {
    this.recebeBeneficio = recebeBeneficio;
  }

  public String getBeneficiosDescricao() {
    return beneficiosDescricao;
  }

  public void setBeneficiosDescricao(String beneficiosDescricao) {
    this.beneficiosDescricao = beneficiosDescricao;
  }

  public String getValorTotalBeneficios() {
    return valorTotalBeneficios;
  }

  public void setValorTotalBeneficios(String valorTotalBeneficios) {
    this.valorTotalBeneficios = valorTotalBeneficios;
  }

  public String getBeneficiosRecebidos() {
    return beneficiosRecebidos;
  }

  public void setBeneficiosRecebidos(String beneficiosRecebidos) {
    this.beneficiosRecebidos = beneficiosRecebidos;
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
