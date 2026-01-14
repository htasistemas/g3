package br.com.g3.ocorrenciacrianca.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "ocorrencia_crianca")
public class OcorrenciaCrianca {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "data_preenchimento", nullable = false)
  private LocalDate dataPreenchimento;

  @Column(name = "local_violencia", length = 60)
  private String localViolencia;

  @Column(name = "local_violencia_outro", length = 200)
  private String localViolenciaOutro;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "violencia_motivada_por", columnDefinition = "jsonb")
  private String violenciaMotivadaPorJson;

  @Column(name = "violencia_motivada_outro", length = 200)
  private String violenciaMotivadaOutro;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "violencia_praticada_por", columnDefinition = "jsonb")
  private String violenciaPraticadaPorJson;

  @Column(name = "violencia_praticada_outro", length = 200)
  private String violenciaPraticadaOutro;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "outras_violacoes", columnDefinition = "jsonb")
  private String outrasViolacoesJson;

  @Column(name = "vitima_nome", length = 200, nullable = false)
  private String vitimaNome;

  @Column(name = "vitima_idade", nullable = false)
  private Integer vitimaIdade;

  @Column(name = "vitima_raca_cor", length = 40)
  private String vitimaRacaCor;

  @Column(name = "vitima_identidade_genero", length = 60)
  private String vitimaIdentidadeGenero;

  @Column(name = "vitima_orientacao_sexual", length = 40)
  private String vitimaOrientacaoSexual;

  @Column(name = "vitima_orientacao_outro", length = 120)
  private String vitimaOrientacaoOutro;

  @Column(name = "vitima_escolaridade", length = 40)
  private String vitimaEscolaridade;

  @Column(name = "vitima_responsavel_tipo", length = 20)
  private String vitimaResponsavelTipo;

  @Column(name = "vitima_responsavel_nome", length = 200)
  private String vitimaResponsavelNome;

  @Column(name = "vitima_telefone_responsavel", length = 40)
  private String vitimaTelefoneResponsavel;

  @Column(name = "vitima_endereco_logradouro", length = 200)
  private String vitimaEnderecoLogradouro;

  @Column(name = "vitima_endereco_complemento", length = 200)
  private String vitimaEnderecoComplemento;

  @Column(name = "vitima_endereco_bairro", length = 120)
  private String vitimaEnderecoBairro;

  @Column(name = "vitima_endereco_municipio", length = 120)
  private String vitimaEnderecoMunicipio;

  @Column(name = "autor_nome", length = 200)
  private String autorNome;

  @Column(name = "autor_idade")
  private Integer autorIdade;

  @Column(name = "autor_nao_consta")
  private Boolean autorNaoConsta;

  @Column(name = "autor_parentesco", length = 20)
  private String autorParentesco;

  @Column(name = "autor_parentesco_grau", length = 200)
  private String autorParentescoGrau;

  @Column(name = "autor_responsavel_tipo", length = 20)
  private String autorResponsavelTipo;

  @Column(name = "autor_responsavel_nome", length = 200)
  private String autorResponsavelNome;

  @Column(name = "autor_responsavel_telefone", length = 40)
  private String autorResponsavelTelefone;

  @Column(name = "autor_responsavel_nao_consta")
  private Boolean autorResponsavelNaoConsta;

  @Column(name = "autor_endereco_logradouro", length = 200)
  private String autorEnderecoLogradouro;

  @Column(name = "autor_endereco_complemento", length = 200)
  private String autorEnderecoComplemento;

  @Column(name = "autor_endereco_bairro", length = 120)
  private String autorEnderecoBairro;

  @Column(name = "autor_endereco_municipio", length = 120)
  private String autorEnderecoMunicipio;

  @Column(name = "autor_endereco_nao_consta")
  private Boolean autorEnderecoNaoConsta;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "tipificacao_violencia", columnDefinition = "jsonb")
  private String tipificacaoViolenciaJson;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "tipificacao_psicologica", columnDefinition = "jsonb")
  private String tipificacaoPsicologicaJson;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "tipificacao_sexual", columnDefinition = "jsonb")
  private String tipificacaoSexualJson;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "violencia_autoprovocada", columnDefinition = "jsonb")
  private String violenciaAutoprovocadaJson;

  @Column(name = "outro_tipo_violencia", columnDefinition = "text")
  private String outroTipoViolenciaDescricao;

  @Column(name = "resumo_violencia", columnDefinition = "text", nullable = false)
  private String resumoViolencia;

  @Column(name = "encaminhar_conselho")
  private Boolean encaminharConselho;

  @Column(name = "encaminhar_motivo", columnDefinition = "text")
  private String encaminharMotivo;

  @Column(name = "data_envio_conselho")
  private LocalDate dataEnvioConselho;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "denuncia_origem", columnDefinition = "jsonb")
  private String denunciaOrigemJson;

  @Column(name = "denuncia_origem_outro", length = 200)
  private String denunciaOrigemOutro;

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

  public String getViolenciaMotivadaPorJson() {
    return violenciaMotivadaPorJson;
  }

  public void setViolenciaMotivadaPorJson(String violenciaMotivadaPorJson) {
    this.violenciaMotivadaPorJson = violenciaMotivadaPorJson;
  }

  public String getViolenciaMotivadaOutro() {
    return violenciaMotivadaOutro;
  }

  public void setViolenciaMotivadaOutro(String violenciaMotivadaOutro) {
    this.violenciaMotivadaOutro = violenciaMotivadaOutro;
  }

  public String getViolenciaPraticadaPorJson() {
    return violenciaPraticadaPorJson;
  }

  public void setViolenciaPraticadaPorJson(String violenciaPraticadaPorJson) {
    this.violenciaPraticadaPorJson = violenciaPraticadaPorJson;
  }

  public String getViolenciaPraticadaOutro() {
    return violenciaPraticadaOutro;
  }

  public void setViolenciaPraticadaOutro(String violenciaPraticadaOutro) {
    this.violenciaPraticadaOutro = violenciaPraticadaOutro;
  }

  public String getOutrasViolacoesJson() {
    return outrasViolacoesJson;
  }

  public void setOutrasViolacoesJson(String outrasViolacoesJson) {
    this.outrasViolacoesJson = outrasViolacoesJson;
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

  public String getTipificacaoViolenciaJson() {
    return tipificacaoViolenciaJson;
  }

  public void setTipificacaoViolenciaJson(String tipificacaoViolenciaJson) {
    this.tipificacaoViolenciaJson = tipificacaoViolenciaJson;
  }

  public String getTipificacaoPsicologicaJson() {
    return tipificacaoPsicologicaJson;
  }

  public void setTipificacaoPsicologicaJson(String tipificacaoPsicologicaJson) {
    this.tipificacaoPsicologicaJson = tipificacaoPsicologicaJson;
  }

  public String getTipificacaoSexualJson() {
    return tipificacaoSexualJson;
  }

  public void setTipificacaoSexualJson(String tipificacaoSexualJson) {
    this.tipificacaoSexualJson = tipificacaoSexualJson;
  }

  public String getViolenciaAutoprovocadaJson() {
    return violenciaAutoprovocadaJson;
  }

  public void setViolenciaAutoprovocadaJson(String violenciaAutoprovocadaJson) {
    this.violenciaAutoprovocadaJson = violenciaAutoprovocadaJson;
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

  public String getDenunciaOrigemJson() {
    return denunciaOrigemJson;
  }

  public void setDenunciaOrigemJson(String denunciaOrigemJson) {
    this.denunciaOrigemJson = denunciaOrigemJson;
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
