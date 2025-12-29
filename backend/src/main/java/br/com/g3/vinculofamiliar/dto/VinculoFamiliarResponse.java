package br.com.g3.vinculofamiliar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class VinculoFamiliarResponse {
  @JsonProperty("id_familia")
  private final Long idFamilia;

  @JsonProperty("nome_familia")
  private final String nomeFamilia;

  @JsonProperty("id_referencia_familiar")
  private final Long idReferenciaFamiliar;

  @JsonProperty("referencia_familiar")
  private final BeneficiarioResumoResponse referenciaFamiliar;

  @JsonProperty("status")
  private final String status;

  @JsonProperty("cep")
  private final String cep;

  @JsonProperty("logradouro")
  private final String logradouro;

  @JsonProperty("numero")
  private final String numero;

  @JsonProperty("complemento")
  private final String complemento;

  @JsonProperty("bairro")
  private final String bairro;

  @JsonProperty("ponto_referencia")
  private final String pontoReferencia;

  @JsonProperty("municipio")
  private final String municipio;

  @JsonProperty("uf")
  private final String uf;

  @JsonProperty("zona")
  private final String zona;

  @JsonProperty("situacao_imovel")
  private final String situacaoImovel;

  @JsonProperty("tipo_moradia")
  private final String tipoMoradia;

  @JsonProperty("agua_encanada")
  private final Boolean aguaEncanada;

  @JsonProperty("esgoto_tipo")
  private final String esgotoTipo;

  @JsonProperty("coleta_lixo")
  private final String coletaLixo;

  @JsonProperty("energia_eletrica")
  private final Boolean energiaEletrica;

  @JsonProperty("internet")
  private final Boolean internet;

  @JsonProperty("arranjo_familiar")
  private final String arranjoFamiliar;

  @JsonProperty("qtd_membros")
  private final Integer qtdMembros;

  @JsonProperty("qtd_criancas")
  private final Integer qtdCriancas;

  @JsonProperty("qtd_adolescentes")
  private final Integer qtdAdolescentes;

  @JsonProperty("qtd_idosos")
  private final Integer qtdIdosos;

  @JsonProperty("qtd_pessoas_deficiencia")
  private final Integer qtdPessoasDeficiencia;

  @JsonProperty("renda_familiar_total")
  private final String rendaFamiliarTotal;

  @JsonProperty("renda_per_capita")
  private final String rendaPerCapita;

  @JsonProperty("faixa_renda_per_capita")
  private final String faixaRendaPerCapita;

  @JsonProperty("principais_fontes_renda")
  private final String principaisFontesRenda;

  @JsonProperty("situacao_inseguranca_alimentar")
  private final String situacaoInsegurancaAlimentar;

  @JsonProperty("possui_dividas_relevantes")
  private final Boolean possuiDividasRelevantes;

  @JsonProperty("descricao_dividas")
  private final String descricaoDividas;

  @JsonProperty("vulnerabilidades_familia")
  private final String vulnerabilidadesFamilia;

  @JsonProperty("servicos_acompanhamento")
  private final String servicosAcompanhamento;

  @JsonProperty("tecnico_responsavel")
  private final String tecnicoResponsavel;

  @JsonProperty("periodicidade_atendimento")
  private final String periodicidadeAtendimento;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonProperty("proxima_visita_prevista")
  private final LocalDate proximaVisitaPrevista;

  @JsonProperty("observacoes")
  private final String observacoes;

  @JsonProperty("membros")
  private final List<VinculoFamiliarMembroResponse> membros;

  @JsonProperty("data_cadastro")
  private final LocalDateTime dataCadastro;

  @JsonProperty("data_atualizacao")
  private final LocalDateTime dataAtualizacao;

  public VinculoFamiliarResponse(
      Long idFamilia,
      String nomeFamilia,
      Long idReferenciaFamiliar,
      BeneficiarioResumoResponse referenciaFamiliar,
      String status,
      String cep,
      String logradouro,
      String numero,
      String complemento,
      String bairro,
      String pontoReferencia,
      String municipio,
      String uf,
      String zona,
      String situacaoImovel,
      String tipoMoradia,
      Boolean aguaEncanada,
      String esgotoTipo,
      String coletaLixo,
      Boolean energiaEletrica,
      Boolean internet,
      String arranjoFamiliar,
      Integer qtdMembros,
      Integer qtdCriancas,
      Integer qtdAdolescentes,
      Integer qtdIdosos,
      Integer qtdPessoasDeficiencia,
      String rendaFamiliarTotal,
      String rendaPerCapita,
      String faixaRendaPerCapita,
      String principaisFontesRenda,
      String situacaoInsegurancaAlimentar,
      Boolean possuiDividasRelevantes,
      String descricaoDividas,
      String vulnerabilidadesFamilia,
      String servicosAcompanhamento,
      String tecnicoResponsavel,
      String periodicidadeAtendimento,
      LocalDate proximaVisitaPrevista,
      String observacoes,
      List<VinculoFamiliarMembroResponse> membros,
      LocalDateTime dataCadastro,
      LocalDateTime dataAtualizacao) {
    this.idFamilia = idFamilia;
    this.nomeFamilia = nomeFamilia;
    this.idReferenciaFamiliar = idReferenciaFamiliar;
    this.referenciaFamiliar = referenciaFamiliar;
    this.status = status;
    this.cep = cep;
    this.logradouro = logradouro;
    this.numero = numero;
    this.complemento = complemento;
    this.bairro = bairro;
    this.pontoReferencia = pontoReferencia;
    this.municipio = municipio;
    this.uf = uf;
    this.zona = zona;
    this.situacaoImovel = situacaoImovel;
    this.tipoMoradia = tipoMoradia;
    this.aguaEncanada = aguaEncanada;
    this.esgotoTipo = esgotoTipo;
    this.coletaLixo = coletaLixo;
    this.energiaEletrica = energiaEletrica;
    this.internet = internet;
    this.arranjoFamiliar = arranjoFamiliar;
    this.qtdMembros = qtdMembros;
    this.qtdCriancas = qtdCriancas;
    this.qtdAdolescentes = qtdAdolescentes;
    this.qtdIdosos = qtdIdosos;
    this.qtdPessoasDeficiencia = qtdPessoasDeficiencia;
    this.rendaFamiliarTotal = rendaFamiliarTotal;
    this.rendaPerCapita = rendaPerCapita;
    this.faixaRendaPerCapita = faixaRendaPerCapita;
    this.principaisFontesRenda = principaisFontesRenda;
    this.situacaoInsegurancaAlimentar = situacaoInsegurancaAlimentar;
    this.possuiDividasRelevantes = possuiDividasRelevantes;
    this.descricaoDividas = descricaoDividas;
    this.vulnerabilidadesFamilia = vulnerabilidadesFamilia;
    this.servicosAcompanhamento = servicosAcompanhamento;
    this.tecnicoResponsavel = tecnicoResponsavel;
    this.periodicidadeAtendimento = periodicidadeAtendimento;
    this.proximaVisitaPrevista = proximaVisitaPrevista;
    this.observacoes = observacoes;
    this.membros = membros;
    this.dataCadastro = dataCadastro;
    this.dataAtualizacao = dataAtualizacao;
  }

  public Long getIdFamilia() {
    return idFamilia;
  }

  public String getNomeFamilia() {
    return nomeFamilia;
  }

  public Long getIdReferenciaFamiliar() {
    return idReferenciaFamiliar;
  }

  public BeneficiarioResumoResponse getReferenciaFamiliar() {
    return referenciaFamiliar;
  }

  public String getStatus() {
    return status;
  }

  public String getCep() {
    return cep;
  }

  public String getLogradouro() {
    return logradouro;
  }

  public String getNumero() {
    return numero;
  }

  public String getComplemento() {
    return complemento;
  }

  public String getBairro() {
    return bairro;
  }

  public String getPontoReferencia() {
    return pontoReferencia;
  }

  public String getMunicipio() {
    return municipio;
  }

  public String getUf() {
    return uf;
  }

  public String getZona() {
    return zona;
  }

  public String getSituacaoImovel() {
    return situacaoImovel;
  }

  public String getTipoMoradia() {
    return tipoMoradia;
  }

  public Boolean getAguaEncanada() {
    return aguaEncanada;
  }

  public String getEsgotoTipo() {
    return esgotoTipo;
  }

  public String getColetaLixo() {
    return coletaLixo;
  }

  public Boolean getEnergiaEletrica() {
    return energiaEletrica;
  }

  public Boolean getInternet() {
    return internet;
  }

  public String getArranjoFamiliar() {
    return arranjoFamiliar;
  }

  public Integer getQtdMembros() {
    return qtdMembros;
  }

  public Integer getQtdCriancas() {
    return qtdCriancas;
  }

  public Integer getQtdAdolescentes() {
    return qtdAdolescentes;
  }

  public Integer getQtdIdosos() {
    return qtdIdosos;
  }

  public Integer getQtdPessoasDeficiencia() {
    return qtdPessoasDeficiencia;
  }

  public String getRendaFamiliarTotal() {
    return rendaFamiliarTotal;
  }

  public String getRendaPerCapita() {
    return rendaPerCapita;
  }

  public String getFaixaRendaPerCapita() {
    return faixaRendaPerCapita;
  }

  public String getPrincipaisFontesRenda() {
    return principaisFontesRenda;
  }

  public String getSituacaoInsegurancaAlimentar() {
    return situacaoInsegurancaAlimentar;
  }

  public Boolean getPossuiDividasRelevantes() {
    return possuiDividasRelevantes;
  }

  public String getDescricaoDividas() {
    return descricaoDividas;
  }

  public String getVulnerabilidadesFamilia() {
    return vulnerabilidadesFamilia;
  }

  public String getServicosAcompanhamento() {
    return servicosAcompanhamento;
  }

  public String getTecnicoResponsavel() {
    return tecnicoResponsavel;
  }

  public String getPeriodicidadeAtendimento() {
    return periodicidadeAtendimento;
  }

  public LocalDate getProximaVisitaPrevista() {
    return proximaVisitaPrevista;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public List<VinculoFamiliarMembroResponse> getMembros() {
    return membros;
  }

  public LocalDateTime getDataCadastro() {
    return dataCadastro;
  }

  public LocalDateTime getDataAtualizacao() {
    return dataAtualizacao;
  }
}
