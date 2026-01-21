package br.com.g3.documentosinstituicao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class DocumentoInstituicaoRequest {
  @NotBlank(message = "Tipo de documento e obrigatorio.")
  private String tipoDocumento;

  @NotBlank(message = "Orgao emissor e obrigatorio.")
  private String orgaoEmissor;

  private String descricao;
  private String categoria;

  @NotNull(message = "Data de emissao e obrigatoria.")
  private LocalDate emissao;

  private LocalDate validade;
  private String responsavelInterno;
  private String modoRenovacao;
  private String observacaoRenovacao;
  private Boolean gerarAlerta;
  private List<Integer> diasAntecedencia;
  private String formaAlerta;
  private Boolean emRenovacao;
  private Boolean semVencimento;
  private Boolean vencimentoIndeterminado;

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
}
