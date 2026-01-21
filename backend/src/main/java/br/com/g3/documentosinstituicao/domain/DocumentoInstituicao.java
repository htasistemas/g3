package br.com.g3.documentosinstituicao.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "documentos_instituicao")
public class DocumentoInstituicao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "tipo_documento", nullable = false, length = 200)
  private String tipoDocumento;

  @Column(name = "orgao_emissor", nullable = false, length = 200)
  private String orgaoEmissor;


  @Column(name = "descricao", columnDefinition = "TEXT")
  private String descricao;

  @Column(name = "categoria", length = 120)
  private String categoria;

  @Column(name = "emissao", nullable = false)
  private LocalDate emissao;

  @Column(name = "validade")
  private LocalDate validade;

  @Column(name = "responsavel_interno", length = 200)
  private String responsavelInterno;

  @Column(name = "modo_renovacao", length = 30)
  private String modoRenovacao;

  @Column(name = "observacao_renovacao", columnDefinition = "TEXT")
  private String observacaoRenovacao;

  @Column(name = "gerar_alerta", nullable = false)
  private Boolean gerarAlerta;

  @Column(name = "dias_antecedencia", columnDefinition = "JSONB")
  @JdbcTypeCode(SqlTypes.JSON)
  private List<Integer> diasAntecedencia;

  @Column(name = "forma_alerta", length = 60)
  private String formaAlerta;

  @Column(name = "em_renovacao", nullable = false)
  private Boolean emRenovacao;

  @Column(name = "sem_vencimento", nullable = false)
  private Boolean semVencimento;

  @Column(name = "vencimento_indeterminado", nullable = false)
  private Boolean vencimentoIndeterminado;

  @Column(name = "situacao", length = 30)
  private String situacao;

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

  public String getTipoDocumento() {
    return tipoDocumento;
  }

  public void setTipoDocumento(String tipoDocumento) {
    this.tipoDocumento = tipoDocumento;
  }

  public String getOrgaoEmissor() {
    return orgaoEmissor;
  }

  public void setOrgaoEmissor(String orgaoEmissor) {
    this.orgaoEmissor = orgaoEmissor;
  }


  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public LocalDate getEmissao() {
    return emissao;
  }

  public void setEmissao(LocalDate emissao) {
    this.emissao = emissao;
  }

  public LocalDate getValidade() {
    return validade;
  }

  public void setValidade(LocalDate validade) {
    this.validade = validade;
  }

  public String getResponsavelInterno() {
    return responsavelInterno;
  }

  public void setResponsavelInterno(String responsavelInterno) {
    this.responsavelInterno = responsavelInterno;
  }

  public String getModoRenovacao() {
    return modoRenovacao;
  }

  public void setModoRenovacao(String modoRenovacao) {
    this.modoRenovacao = modoRenovacao;
  }

  public String getObservacaoRenovacao() {
    return observacaoRenovacao;
  }

  public void setObservacaoRenovacao(String observacaoRenovacao) {
    this.observacaoRenovacao = observacaoRenovacao;
  }

  public Boolean getGerarAlerta() {
    return gerarAlerta;
  }

  public void setGerarAlerta(Boolean gerarAlerta) {
    this.gerarAlerta = gerarAlerta;
  }

  public List<Integer> getDiasAntecedencia() {
    return diasAntecedencia;
  }

  public void setDiasAntecedencia(List<Integer> diasAntecedencia) {
    this.diasAntecedencia = diasAntecedencia;
  }

  public String getFormaAlerta() {
    return formaAlerta;
  }

  public void setFormaAlerta(String formaAlerta) {
    this.formaAlerta = formaAlerta;
  }

  public Boolean getEmRenovacao() {
    return emRenovacao;
  }

  public void setEmRenovacao(Boolean emRenovacao) {
    this.emRenovacao = emRenovacao;
  }

  public Boolean getSemVencimento() {
    return semVencimento;
  }

  public void setSemVencimento(Boolean semVencimento) {
    this.semVencimento = semVencimento;
  }

  public Boolean getVencimentoIndeterminado() {
    return vencimentoIndeterminado;
  }

  public void setVencimentoIndeterminado(Boolean vencimentoIndeterminado) {
    this.vencimentoIndeterminado = vencimentoIndeterminado;
  }

  public String getSituacao() {
    return situacao;
  }

  public void setSituacao(String situacao) {
    this.situacao = situacao;
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
