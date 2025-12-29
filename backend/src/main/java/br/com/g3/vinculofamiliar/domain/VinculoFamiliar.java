package br.com.g3.vinculofamiliar.domain;

import br.com.g3.cadastrobeneficiario.domain.CadastroBeneficiario;
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
@Table(name = "vinculo_familiar")
public class VinculoFamiliar {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "nome_familia", length = 200, nullable = false)
  private String nomeFamilia;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_referencia_familiar")
  private CadastroBeneficiario referenciaFamiliar;

  @Column(name = "status", length = 60, nullable = false)
  private String status;

  @Column(name = "cep", length = 20)
  private String cep;

  @Column(name = "logradouro", length = 200)
  private String logradouro;

  @Column(name = "numero", length = 20)
  private String numero;

  @Column(name = "complemento", length = 150)
  private String complemento;

  @Column(name = "bairro", length = 150)
  private String bairro;

  @Column(name = "ponto_referencia", length = 200)
  private String pontoReferencia;

  @Column(name = "municipio", length = 150)
  private String municipio;

  @Column(name = "uf", length = 2)
  private String uf;

  @Column(name = "zona", length = 60)
  private String zona;

  @Column(name = "situacao_imovel", length = 120)
  private String situacaoImovel;

  @Column(name = "tipo_moradia", length = 120)
  private String tipoMoradia;

  @Column(name = "agua_encanada")
  private Boolean aguaEncanada;

  @Column(name = "esgoto_tipo", length = 120)
  private String esgotoTipo;

  @Column(name = "coleta_lixo", length = 120)
  private String coletaLixo;

  @Column(name = "energia_eletrica")
  private Boolean energiaEletrica;

  @Column(name = "internet")
  private Boolean internet;

  @Column(name = "arranjo_familiar", length = 200)
  private String arranjoFamiliar;

  @Column(name = "qtd_membros")
  private Integer qtdMembros;

  @Column(name = "qtd_criancas")
  private Integer qtdCriancas;

  @Column(name = "qtd_adolescentes")
  private Integer qtdAdolescentes;

  @Column(name = "qtd_idosos")
  private Integer qtdIdosos;

  @Column(name = "qtd_pessoas_deficiencia")
  private Integer qtdPessoasDeficiencia;

  @Column(name = "renda_familiar_total", length = 60)
  private String rendaFamiliarTotal;

  @Column(name = "renda_per_capita", length = 60)
  private String rendaPerCapita;

  @Column(name = "faixa_renda_per_capita", length = 120)
  private String faixaRendaPerCapita;

  @Column(name = "principais_fontes_renda")
  private String principaisFontesRenda;

  @Column(name = "situacao_inseguranca_alimentar")
  private String situacaoInsegurancaAlimentar;

  @Column(name = "possui_dividas_relevantes")
  private Boolean possuiDividasRelevantes;

  @Column(name = "descricao_dividas")
  private String descricaoDividas;

  @Column(name = "vulnerabilidades_familia")
  private String vulnerabilidadesFamilia;

  @Column(name = "servicos_acompanhamento")
  private String servicosAcompanhamento;

  @Column(name = "tecnico_responsavel", length = 150)
  private String tecnicoResponsavel;

  @Column(name = "periodicidade_atendimento", length = 120)
  private String periodicidadeAtendimento;

  @Column(name = "proxima_visita_prevista")
  private LocalDate proximaVisitaPrevista;

  @Column(name = "observacoes")
  private String observacoes;

  @OneToMany(
      mappedBy = "vinculoFamiliar",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private List<VinculoFamiliarMembro> membros = new ArrayList<>();

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

  public String getNomeFamilia() {
    return nomeFamilia;
  }

  public void setNomeFamilia(String nomeFamilia) {
    this.nomeFamilia = nomeFamilia;
  }

  public CadastroBeneficiario getReferenciaFamiliar() {
    return referenciaFamiliar;
  }

  public void setReferenciaFamiliar(CadastroBeneficiario referenciaFamiliar) {
    this.referenciaFamiliar = referenciaFamiliar;
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

  public List<VinculoFamiliarMembro> getMembros() {
    return membros;
  }

  public void setMembros(List<VinculoFamiliarMembro> membros) {
    this.membros = membros;
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
