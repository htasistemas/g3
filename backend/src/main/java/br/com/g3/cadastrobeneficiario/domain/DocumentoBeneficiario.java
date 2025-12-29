package br.com.g3.cadastrobeneficiario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "documentos")
public class DocumentoBeneficiario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "beneficiario_id", nullable = false)
  private CadastroBeneficiario beneficiario;

  @Column(name = "tipo_documento", length = 80)
  private String tipoDocumento;

  @Column(name = "numero_documento", length = 80)
  private String numeroDocumento;

  @Column(name = "orgao_emissor", length = 120)
  private String orgaoEmissor;

  @Column(name = "uf_emissor", length = 2)
  private String ufEmissor;

  @Column(name = "data_emissao")
  private LocalDate dataEmissao;

  @Column(length = 40)
  private String livro;

  @Column(length = 40)
  private String folha;

  @Column(length = 60)
  private String termo;

  @Column(length = 150)
  private String cartorio;

  @Column(length = 150)
  private String municipio;

  @Column(length = 2)
  private String uf;

  @Column(name = "nome_documento", length = 200)
  private String nomeDocumento;

  @Column(name = "nome_arquivo", length = 200)
  private String nomeArquivo;

  @Column(name = "caminho_arquivo", length = 400)
  private String caminhoArquivo;

  @Column(name = "content_type", length = 120)
  private String contentType;

  @Column
  private Boolean obrigatorio;

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

  public String getTipoDocumento() {
    return tipoDocumento;
  }

  public void setTipoDocumento(String tipoDocumento) {
    this.tipoDocumento = tipoDocumento;
  }

  public String getNumeroDocumento() {
    return numeroDocumento;
  }

  public void setNumeroDocumento(String numeroDocumento) {
    this.numeroDocumento = numeroDocumento;
  }

  public String getOrgaoEmissor() {
    return orgaoEmissor;
  }

  public void setOrgaoEmissor(String orgaoEmissor) {
    this.orgaoEmissor = orgaoEmissor;
  }

  public String getUfEmissor() {
    return ufEmissor;
  }

  public void setUfEmissor(String ufEmissor) {
    this.ufEmissor = ufEmissor;
  }

  public LocalDate getDataEmissao() {
    return dataEmissao;
  }

  public void setDataEmissao(LocalDate dataEmissao) {
    this.dataEmissao = dataEmissao;
  }

  public String getLivro() {
    return livro;
  }

  public void setLivro(String livro) {
    this.livro = livro;
  }

  public String getFolha() {
    return folha;
  }

  public void setFolha(String folha) {
    this.folha = folha;
  }

  public String getTermo() {
    return termo;
  }

  public void setTermo(String termo) {
    this.termo = termo;
  }

  public String getCartorio() {
    return cartorio;
  }

  public void setCartorio(String cartorio) {
    this.cartorio = cartorio;
  }

  public String getMunicipio() {
    return municipio;
  }

  public void setMunicipio(String municipio) {
    this.municipio = municipio;
  }

  public String getUf() {
    return uf;
  }

  public void setUf(String uf) {
    this.uf = uf;
  }

  public String getNomeDocumento() {
    return nomeDocumento;
  }

  public void setNomeDocumento(String nomeDocumento) {
    this.nomeDocumento = nomeDocumento;
  }

  public String getNomeArquivo() {
    return nomeArquivo;
  }

  public void setNomeArquivo(String nomeArquivo) {
    this.nomeArquivo = nomeArquivo;
  }

  public String getCaminhoArquivo() {
    return caminhoArquivo;
  }

  public void setCaminhoArquivo(String caminhoArquivo) {
    this.caminhoArquivo = caminhoArquivo;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public Boolean getObrigatorio() {
    return obrigatorio;
  }

  public void setObrigatorio(Boolean obrigatorio) {
    this.obrigatorio = obrigatorio;
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
