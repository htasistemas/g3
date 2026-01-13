package br.com.g3.ocorrenciacrianca.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class OcorrenciaCriancaResponse {
  private Long id;
  private LocalDate dataPreenchimento;
  private String localViolencia;
  private String localViolenciaOutro;
  private List<String> violenciaMotivadaPor;
  private String violenciaMotivadaOutro;
  private List<String> violenciaPraticadaPor;
  private String violenciaPraticadaOutro;
  private List<String> outrasViolacoes;
  private String vitimaNome;
  private Integer vitimaIdade;
  private String vitimaRacaCor;
  private String vitimaIdentidadeGenero;
  private String vitimaOrientacaoSexual;
  private String vitimaOrientacaoOutro;
  private String vitimaEscolaridade;
  private String vitimaResponsavelTipo;
  private String vitimaResponsavelNome;
  private String vitimaTelefoneResponsavel;
  private String vitimaEnderecoLogradouro;
  private String vitimaEnderecoComplemento;
  private String vitimaEnderecoBairro;
  private String vitimaEnderecoMunicipio;
  private String autorNome;
  private Integer autorIdade;
  private Boolean autorNaoConsta;
  private String autorParentesco;
  private String autorParentescoGrau;
  private String autorResponsavelTipo;
  private String autorResponsavelNome;
  private String autorResponsavelTelefone;
  private Boolean autorResponsavelNaoConsta;
  private String autorEnderecoLogradouro;
  private String autorEnderecoComplemento;
  private String autorEnderecoBairro;
  private String autorEnderecoMunicipio;
  private Boolean autorEnderecoNaoConsta;
  private List<String> tipificacaoViolencia;
  private List<String> tipificacaoPsicologica;
  private List<String> tipificacaoSexual;
  private List<String> violenciaAutoprovocada;
  private String outroTipoViolenciaDescricao;
  private String resumoViolencia;
  private Boolean encaminharConselho;
  private String encaminharMotivo;
  private LocalDate dataEnvioConselho;
  private List<String> denunciaOrigem;
  private String denunciaOrigemOutro;
  private LocalDateTime criadoEm;
  private LocalDateTime atualizadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDate getDataPreenchimento() {
    return dataPreenchimento;
  }

  public void setDataPreenchimento(LocalDate dataPreenchimento) {
    this.dataPreenchimento = dataPreenchimento;
  }

  public String getLocalViolencia() {
    return localViolencia;
  }

  public void setLocalViolencia(String localViolencia) {
    this.localViolencia = localViolencia;
  }

  public String getLocalViolenciaOutro() {
    return localViolenciaOutro;
  }

  public void setLocalViolenciaOutro(String localViolenciaOutro) {
    this.localViolenciaOutro = localViolenciaOutro;
  }

  public List<String> getViolenciaMotivadaPor() {
    return violenciaMotivadaPor;
  }

  public void setViolenciaMotivadaPor(List<String> violenciaMotivadaPor) {
    this.violenciaMotivadaPor = violenciaMotivadaPor;
  }

  public String getViolenciaMotivadaOutro() {
    return violenciaMotivadaOutro;
  }

  public void setViolenciaMotivadaOutro(String violenciaMotivadaOutro) {
    this.violenciaMotivadaOutro = violenciaMotivadaOutro;
  }

  public List<String> getViolenciaPraticadaPor() {
    return violenciaPraticadaPor;
  }

  public void setViolenciaPraticadaPor(List<String> violenciaPraticadaPor) {
    this.violenciaPraticadaPor = violenciaPraticadaPor;
  }

  public String getViolenciaPraticadaOutro() {
    return violenciaPraticadaOutro;
  }

  public void setViolenciaPraticadaOutro(String violenciaPraticadaOutro) {
    this.violenciaPraticadaOutro = violenciaPraticadaOutro;
  }

  public List<String> getOutrasViolacoes() {
    return outrasViolacoes;
  }

  public void setOutrasViolacoes(List<String> outrasViolacoes) {
    this.outrasViolacoes = outrasViolacoes;
  }

  public String getVitimaNome() {
    return vitimaNome;
  }

  public void setVitimaNome(String vitimaNome) {
    this.vitimaNome = vitimaNome;
  }

  public Integer getVitimaIdade() {
    return vitimaIdade;
  }

  public void setVitimaIdade(Integer vitimaIdade) {
    this.vitimaIdade = vitimaIdade;
  }

  public String getVitimaRacaCor() {
    return vitimaRacaCor;
  }

  public void setVitimaRacaCor(String vitimaRacaCor) {
    this.vitimaRacaCor = vitimaRacaCor;
  }

  public String getVitimaIdentidadeGenero() {
    return vitimaIdentidadeGenero;
  }

  public void setVitimaIdentidadeGenero(String vitimaIdentidadeGenero) {
    this.vitimaIdentidadeGenero = vitimaIdentidadeGenero;
  }

  public String getVitimaOrientacaoSexual() {
    return vitimaOrientacaoSexual;
  }

  public void setVitimaOrientacaoSexual(String vitimaOrientacaoSexual) {
    this.vitimaOrientacaoSexual = vitimaOrientacaoSexual;
  }

  public String getVitimaOrientacaoOutro() {
    return vitimaOrientacaoOutro;
  }

  public void setVitimaOrientacaoOutro(String vitimaOrientacaoOutro) {
    this.vitimaOrientacaoOutro = vitimaOrientacaoOutro;
  }

  public String getVitimaEscolaridade() {
    return vitimaEscolaridade;
  }

  public void setVitimaEscolaridade(String vitimaEscolaridade) {
    this.vitimaEscolaridade = vitimaEscolaridade;
  }

  public String getVitimaResponsavelTipo() {
    return vitimaResponsavelTipo;
  }

  public void setVitimaResponsavelTipo(String vitimaResponsavelTipo) {
    this.vitimaResponsavelTipo = vitimaResponsavelTipo;
  }

  public String getVitimaResponsavelNome() {
    return vitimaResponsavelNome;
  }

  public void setVitimaResponsavelNome(String vitimaResponsavelNome) {
    this.vitimaResponsavelNome = vitimaResponsavelNome;
  }

  public String getVitimaTelefoneResponsavel() {
    return vitimaTelefoneResponsavel;
  }

  public void setVitimaTelefoneResponsavel(String vitimaTelefoneResponsavel) {
    this.vitimaTelefoneResponsavel = vitimaTelefoneResponsavel;
  }

  public String getVitimaEnderecoLogradouro() {
    return vitimaEnderecoLogradouro;
  }

  public void setVitimaEnderecoLogradouro(String vitimaEnderecoLogradouro) {
    this.vitimaEnderecoLogradouro = vitimaEnderecoLogradouro;
  }

  public String getVitimaEnderecoComplemento() {
    return vitimaEnderecoComplemento;
  }

  public void setVitimaEnderecoComplemento(String vitimaEnderecoComplemento) {
    this.vitimaEnderecoComplemento = vitimaEnderecoComplemento;
  }

  public String getVitimaEnderecoBairro() {
    return vitimaEnderecoBairro;
  }

  public void setVitimaEnderecoBairro(String vitimaEnderecoBairro) {
    this.vitimaEnderecoBairro = vitimaEnderecoBairro;
  }

  public String getVitimaEnderecoMunicipio() {
    return vitimaEnderecoMunicipio;
  }

  public void setVitimaEnderecoMunicipio(String vitimaEnderecoMunicipio) {
    this.vitimaEnderecoMunicipio = vitimaEnderecoMunicipio;
  }

  public String getAutorNome() {
    return autorNome;
  }

  public void setAutorNome(String autorNome) {
    this.autorNome = autorNome;
  }

  public Integer getAutorIdade() {
    return autorIdade;
  }

  public void setAutorIdade(Integer autorIdade) {
    this.autorIdade = autorIdade;
  }

  public Boolean getAutorNaoConsta() {
    return autorNaoConsta;
  }

  public void setAutorNaoConsta(Boolean autorNaoConsta) {
    this.autorNaoConsta = autorNaoConsta;
  }

  public String getAutorParentesco() {
    return autorParentesco;
  }

  public void setAutorParentesco(String autorParentesco) {
    this.autorParentesco = autorParentesco;
  }

  public String getAutorParentescoGrau() {
    return autorParentescoGrau;
  }

  public void setAutorParentescoGrau(String autorParentescoGrau) {
    this.autorParentescoGrau = autorParentescoGrau;
  }

  public String getAutorResponsavelTipo() {
    return autorResponsavelTipo;
  }

  public void setAutorResponsavelTipo(String autorResponsavelTipo) {
    this.autorResponsavelTipo = autorResponsavelTipo;
  }

  public String getAutorResponsavelNome() {
    return autorResponsavelNome;
  }

  public void setAutorResponsavelNome(String autorResponsavelNome) {
    this.autorResponsavelNome = autorResponsavelNome;
  }

  public String getAutorResponsavelTelefone() {
    return autorResponsavelTelefone;
  }

  public void setAutorResponsavelTelefone(String autorResponsavelTelefone) {
    this.autorResponsavelTelefone = autorResponsavelTelefone;
  }

  public Boolean getAutorResponsavelNaoConsta() {
    return autorResponsavelNaoConsta;
  }

  public void setAutorResponsavelNaoConsta(Boolean autorResponsavelNaoConsta) {
    this.autorResponsavelNaoConsta = autorResponsavelNaoConsta;
  }

  public String getAutorEnderecoLogradouro() {
    return autorEnderecoLogradouro;
  }

  public void setAutorEnderecoLogradouro(String autorEnderecoLogradouro) {
    this.autorEnderecoLogradouro = autorEnderecoLogradouro;
  }

  public String getAutorEnderecoComplemento() {
    return autorEnderecoComplemento;
  }

  public void setAutorEnderecoComplemento(String autorEnderecoComplemento) {
    this.autorEnderecoComplemento = autorEnderecoComplemento;
  }

  public String getAutorEnderecoBairro() {
    return autorEnderecoBairro;
  }

  public void setAutorEnderecoBairro(String autorEnderecoBairro) {
    this.autorEnderecoBairro = autorEnderecoBairro;
  }

  public String getAutorEnderecoMunicipio() {
    return autorEnderecoMunicipio;
  }

  public void setAutorEnderecoMunicipio(String autorEnderecoMunicipio) {
    this.autorEnderecoMunicipio = autorEnderecoMunicipio;
  }

  public Boolean getAutorEnderecoNaoConsta() {
    return autorEnderecoNaoConsta;
  }

  public void setAutorEnderecoNaoConsta(Boolean autorEnderecoNaoConsta) {
    this.autorEnderecoNaoConsta = autorEnderecoNaoConsta;
  }

  public List<String> getTipificacaoViolencia() {
    return tipificacaoViolencia;
  }

  public void setTipificacaoViolencia(List<String> tipificacaoViolencia) {
    this.tipificacaoViolencia = tipificacaoViolencia;
  }

  public List<String> getTipificacaoPsicologica() {
    return tipificacaoPsicologica;
  }

  public void setTipificacaoPsicologica(List<String> tipificacaoPsicologica) {
    this.tipificacaoPsicologica = tipificacaoPsicologica;
  }

  public List<String> getTipificacaoSexual() {
    return tipificacaoSexual;
  }

  public void setTipificacaoSexual(List<String> tipificacaoSexual) {
    this.tipificacaoSexual = tipificacaoSexual;
  }

  public List<String> getViolenciaAutoprovocada() {
    return violenciaAutoprovocada;
  }

  public void setViolenciaAutoprovocada(List<String> violenciaAutoprovocada) {
    this.violenciaAutoprovocada = violenciaAutoprovocada;
  }

  public String getOutroTipoViolenciaDescricao() {
    return outroTipoViolenciaDescricao;
  }

  public void setOutroTipoViolenciaDescricao(String outroTipoViolenciaDescricao) {
    this.outroTipoViolenciaDescricao = outroTipoViolenciaDescricao;
  }

  public String getResumoViolencia() {
    return resumoViolencia;
  }

  public void setResumoViolencia(String resumoViolencia) {
    this.resumoViolencia = resumoViolencia;
  }

  public Boolean getEncaminharConselho() {
    return encaminharConselho;
  }

  public void setEncaminharConselho(Boolean encaminharConselho) {
    this.encaminharConselho = encaminharConselho;
  }

  public String getEncaminharMotivo() {
    return encaminharMotivo;
  }

  public void setEncaminharMotivo(String encaminharMotivo) {
    this.encaminharMotivo = encaminharMotivo;
  }

  public LocalDate getDataEnvioConselho() {
    return dataEnvioConselho;
  }

  public void setDataEnvioConselho(LocalDate dataEnvioConselho) {
    this.dataEnvioConselho = dataEnvioConselho;
  }

  public List<String> getDenunciaOrigem() {
    return denunciaOrigem;
  }

  public void setDenunciaOrigem(List<String> denunciaOrigem) {
    this.denunciaOrigem = denunciaOrigem;
  }

  public String getDenunciaOrigemOutro() {
    return denunciaOrigemOutro;
  }

  public void setDenunciaOrigemOutro(String denunciaOrigemOutro) {
    this.denunciaOrigemOutro = denunciaOrigemOutro;
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
