package br.com.g3.vinculofamiliar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public class VinculoFamiliarCriacaoRequest {
  @NotBlank
  @Size(max = 200)
  @JsonProperty("nome_familia")
  private String nomeFamilia;

  @JsonProperty("id_referencia_familiar")
  private Long idReferenciaFamiliar;

  @Size(max = 60)
  @JsonProperty("status")
  private String status;

  @JsonProperty("cep")
  private String cep;

  @JsonProperty("logradouro")
  private String logradouro;

  @JsonProperty("numero")
  private String numero;

  @JsonProperty("complemento")
  private String complemento;

  @JsonProperty("bairro")
  private String bairro;

  @JsonProperty("ponto_referencia")
  private String pontoReferencia;

  @JsonProperty("municipio")
  private String municipio;

  @JsonProperty("uf")
  private String uf;

  @JsonProperty("zona")
  private String zona;

  @JsonProperty("situacao_imovel")
  private String situacaoImovel;

  @JsonProperty("tipo_moradia")
  private String tipoMoradia;

  @JsonProperty("agua_encanada")
  private Boolean aguaEncanada;

  @JsonProperty("esgoto_tipo")
  private String esgotoTipo;

  @JsonProperty("coleta_lixo")
  private String coletaLixo;

  @JsonProperty("energia_eletrica")
  private Boolean energiaEletrica;

  @JsonProperty("internet")
  private Boolean internet;

  @JsonProperty("arranjo_familiar")
  private String arranjoFamiliar;

  @JsonProperty("qtd_membros")
  private Integer qtdMembros;

  @JsonProperty("qtd_criancas")
  private Integer qtdCriancas;

  @JsonProperty("qtd_adolescentes")
  private Integer qtdAdolescentes;

  @JsonProperty("qtd_idosos")
  private Integer qtdIdosos;

  @JsonProperty("qtd_pessoas_deficiencia")
  private Integer qtdPessoasDeficiencia;

  @JsonProperty("renda_familiar_total")
  private String rendaFamiliarTotal;

  @JsonProperty("renda_per_capita")
  private String rendaPerCapita;

  @JsonProperty("faixa_renda_per_capita")
  private String faixaRendaPerCapita;

  @JsonProperty("principais_fontes_renda")
  private String principaisFontesRenda;

  @JsonProperty("situacao_inseguranca_alimentar")
  private String situacaoInsegurancaAlimentar;

  @JsonProperty("possui_dividas_relevantes")
  private Boolean possuiDividasRelevantes;

  @JsonProperty("descricao_dividas")
  private String descricaoDividas;

  @JsonProperty("vulnerabilidades_familia")
  private String vulnerabilidadesFamilia;

  @JsonProperty("servicos_acompanhamento")
  private String servicosAcompanhamento;

  @JsonProperty("tecnico_responsavel")
  private String tecnicoResponsavel;

  @JsonProperty("periodicidade_atendimento")
  private String periodicidadeAtendimento;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonProperty("proxima_visita_prevista")
  private LocalDate proximaVisitaPrevista;

  @JsonProperty("observacoes")
  private String observacoes;

  @Valid
  @JsonProperty("membros")
  private List<VinculoFamiliarMembroRequest> membros;

  public String getNomeFamilia() {
    return nomeFamilia;
  }

  public void setNomeFamilia(String nomeFamilia) {
    this.nomeFamilia = nomeFamilia;
  }

  public Long getIdReferenciaFamiliar() {
    return idReferenciaFamiliar;
  }

  public void setIdReferenciaFamiliar(Long idReferenciaFamiliar) {
    this.idReferenciaFamiliar = idReferenciaFamiliar;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getCep() {
    return cep;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

  public String getLogradouro() {
    return logradouro;
  }

  public void setLogradouro(String logradouro) {
    this.logradouro = logradouro;
  }

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public String getComplemento() {
    return complemento;
  }

  public void setComplemento(String complemento) {
    this.complemento = complemento;
  }

  public String getBairro() {
    return bairro;
  }

  public void setBairro(String bairro) {
    this.bairro = bairro;
  }

  public String getPontoReferencia() {
    return pontoReferencia;
  }

  public void setPontoReferencia(String pontoReferencia) {
    this.pontoReferencia = pontoReferencia;
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

  public String getZona() {
    return zona;
  }

  public void setZona(String zona) {
    this.zona = zona;
  }

  public String getSituacaoImovel() {
    return situacaoImovel;
  }

  public void setSituacaoImovel(String situacaoImovel) {
    this.situacaoImovel = situacaoImovel;
  }

  public String getTipoMoradia() {
    return tipoMoradia;
  }

  public void setTipoMoradia(String tipoMoradia) {
    this.tipoMoradia = tipoMoradia;
  }

  public Boolean getAguaEncanada() {
    return aguaEncanada;
  }

  public void setAguaEncanada(Boolean aguaEncanada) {
    this.aguaEncanada = aguaEncanada;
  }

  public String getEsgotoTipo() {
    return esgotoTipo;
  }

  public void setEsgotoTipo(String esgotoTipo) {
    this.esgotoTipo = esgotoTipo;
  }

  public String getColetaLixo() {
    return coletaLixo;
  }

  public void setColetaLixo(String coletaLixo) {
    this.coletaLixo = coletaLixo;
  }

  public Boolean getEnergiaEletrica() {
    return energiaEletrica;
  }

  public void setEnergiaEletrica(Boolean energiaEletrica) {
    this.energiaEletrica = energiaEletrica;
  }

  public Boolean getInternet() {
    return internet;
  }

  public void setInternet(Boolean internet) {
    this.internet = internet;
  }

  public String getArranjoFamiliar() {
    return arranjoFamiliar;
  }

  public void setArranjoFamiliar(String arranjoFamiliar) {
    this.arranjoFamiliar = arranjoFamiliar;
  }

  public Integer getQtdMembros() {
    return qtdMembros;
  }

  public void setQtdMembros(Integer qtdMembros) {
    this.qtdMembros = qtdMembros;
  }

  public Integer getQtdCriancas() {
    return qtdCriancas;
  }

  public void setQtdCriancas(Integer qtdCriancas) {
    this.qtdCriancas = qtdCriancas;
  }

  public Integer getQtdAdolescentes() {
    return qtdAdolescentes;
  }

  public void setQtdAdolescentes(Integer qtdAdolescentes) {
    this.qtdAdolescentes = qtdAdolescentes;
  }

  public Integer getQtdIdosos() {
    return qtdIdosos;
  }

  public void setQtdIdosos(Integer qtdIdosos) {
    this.qtdIdosos = qtdIdosos;
  }

  public Integer getQtdPessoasDeficiencia() {
    return qtdPessoasDeficiencia;
  }

  public void setQtdPessoasDeficiencia(Integer qtdPessoasDeficiencia) {
    this.qtdPessoasDeficiencia = qtdPessoasDeficiencia;
  }

  public String getRendaFamiliarTotal() {
    return rendaFamiliarTotal;
  }

  public void setRendaFamiliarTotal(String rendaFamiliarTotal) {
    this.rendaFamiliarTotal = rendaFamiliarTotal;
  }

  public String getRendaPerCapita() {
    return rendaPerCapita;
  }

  public void setRendaPerCapita(String rendaPerCapita) {
    this.rendaPerCapita = rendaPerCapita;
  }

  public String getFaixaRendaPerCapita() {
    return faixaRendaPerCapita;
  }

  public void setFaixaRendaPerCapita(String faixaRendaPerCapita) {
    this.faixaRendaPerCapita = faixaRendaPerCapita;
  }

  public String getPrincipaisFontesRenda() {
    return principaisFontesRenda;
  }

  public void setPrincipaisFontesRenda(String principaisFontesRenda) {
    this.principaisFontesRenda = principaisFontesRenda;
  }

  public String getSituacaoInsegurancaAlimentar() {
    return situacaoInsegurancaAlimentar;
  }

  public void setSituacaoInsegurancaAlimentar(String situacaoInsegurancaAlimentar) {
    this.situacaoInsegurancaAlimentar = situacaoInsegurancaAlimentar;
  }

  public Boolean getPossuiDividasRelevantes() {
    return possuiDividasRelevantes;
  }

  public void setPossuiDividasRelevantes(Boolean possuiDividasRelevantes) {
    this.possuiDividasRelevantes = possuiDividasRelevantes;
  }

  public String getDescricaoDividas() {
    return descricaoDividas;
  }

  public void setDescricaoDividas(String descricaoDividas) {
    this.descricaoDividas = descricaoDividas;
  }

  public String getVulnerabilidadesFamilia() {
    return vulnerabilidadesFamilia;
  }

  public void setVulnerabilidadesFamilia(String vulnerabilidadesFamilia) {
    this.vulnerabilidadesFamilia = vulnerabilidadesFamilia;
  }

  public String getServicosAcompanhamento() {
    return servicosAcompanhamento;
  }

  public void setServicosAcompanhamento(String servicosAcompanhamento) {
    this.servicosAcompanhamento = servicosAcompanhamento;
  }

  public String getTecnicoResponsavel() {
    return tecnicoResponsavel;
  }

  public void setTecnicoResponsavel(String tecnicoResponsavel) {
    this.tecnicoResponsavel = tecnicoResponsavel;
  }

  public String getPeriodicidadeAtendimento() {
    return periodicidadeAtendimento;
  }

  public void setPeriodicidadeAtendimento(String periodicidadeAtendimento) {
    this.periodicidadeAtendimento = periodicidadeAtendimento;
  }

  public LocalDate getProximaVisitaPrevista() {
    return proximaVisitaPrevista;
  }

  public void setProximaVisitaPrevista(LocalDate proximaVisitaPrevista) {
    this.proximaVisitaPrevista = proximaVisitaPrevista;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public List<VinculoFamiliarMembroRequest> getMembros() {
    return membros;
  }

  public void setMembros(List<VinculoFamiliarMembroRequest> membros) {
    this.membros = membros;
  }
}
