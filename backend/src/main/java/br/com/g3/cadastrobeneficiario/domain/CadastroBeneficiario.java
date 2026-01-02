package br.com.g3.cadastrobeneficiario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import br.com.g3.unidadeassistencial.domain.Endereco;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cadastro_beneficiario")
public class CadastroBeneficiario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "codigo", length = 20)
  private String codigo;

  @Column(name = "nome_completo", length = 200, nullable = false)
  private String nomeCompleto;

  @Column(name = "nome_social", length = 200)
  private String nomeSocial;

  @Column(name = "apelido", length = 120)
  private String apelido;

  @Column(name = "data_nascimento", nullable = false)
  private LocalDate dataNascimento;

  @Column(name = "foto_3x4")
  private String foto3x4;

  @Column(name = "sexo_biologico", length = 60)
  private String sexoBiologico;

  @Column(name = "identidade_genero", length = 120)
  private String identidadeGenero;

  @Column(name = "cor_raca", length = 60)
  private String corRaca;

  @Column(name = "estado_civil", length = 60)
  private String estadoCivil;

  @Column(name = "nacionalidade", length = 120)
  private String nacionalidade;

  @Column(name = "naturalidade_cidade", length = 150)
  private String naturalidadeCidade;

  @Column(name = "naturalidade_uf", length = 2)
  private String naturalidadeUf;

  @Column(name = "nome_mae", length = 200, nullable = false)
  private String nomeMae;

  @Column(name = "nome_pai", length = 200)
  private String nomePai;

  @Column(name = "status", length = 60)
  private String status;

  @Column(name = "opta_receber_cesta_basica")
  private Boolean optaReceberCestaBasica;

  @Column(name = "apto_receber_cesta_basica")
  private Boolean aptoReceberCestaBasica;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "endereco_id")
  private Endereco endereco;

  @OneToMany(
      mappedBy = "beneficiario",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private List<ContatoBeneficiario> contatos = new ArrayList<>();

  @OneToMany(
      mappedBy = "beneficiario",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private List<DocumentoBeneficiario> documentos = new ArrayList<>();

  @OneToMany(
      mappedBy = "beneficiario",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private List<SituacaoSocialBeneficiario> situacoesSociais = new ArrayList<>();

  @OneToMany(
      mappedBy = "beneficiario",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private List<EscolaridadeBeneficiario> escolaridades = new ArrayList<>();

  @OneToMany(
      mappedBy = "beneficiario",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private List<SaudeBeneficiario> saudes = new ArrayList<>();

  @OneToMany(
      mappedBy = "beneficiario",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private List<BeneficiosBeneficiario> beneficios = new ArrayList<>();

  @OneToMany(
      mappedBy = "beneficiario",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private List<ObservacoesBeneficiario> observacoesRegistros = new ArrayList<>();

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

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getNomeCompleto() {
    return nomeCompleto;
  }

  public void setNomeCompleto(String nomeCompleto) {
    this.nomeCompleto = nomeCompleto;
  }

  public String getNomeSocial() {
    return nomeSocial;
  }

  public void setNomeSocial(String nomeSocial) {
    this.nomeSocial = nomeSocial;
  }

  public String getApelido() {
    return apelido;
  }

  public void setApelido(String apelido) {
    this.apelido = apelido;
  }

  public LocalDate getDataNascimento() {
    return dataNascimento;
  }

  public void setDataNascimento(LocalDate dataNascimento) {
    this.dataNascimento = dataNascimento;
  }

  public String getFoto3x4() {
    return foto3x4;
  }

  public void setFoto3x4(String foto3x4) {
    this.foto3x4 = foto3x4;
  }

  public String getSexoBiologico() {
    return sexoBiologico;
  }

  public void setSexoBiologico(String sexoBiologico) {
    this.sexoBiologico = sexoBiologico;
  }

  public String getIdentidadeGenero() {
    return identidadeGenero;
  }

  public void setIdentidadeGenero(String identidadeGenero) {
    this.identidadeGenero = identidadeGenero;
  }

  public String getCorRaca() {
    return corRaca;
  }

  public void setCorRaca(String corRaca) {
    this.corRaca = corRaca;
  }

  public String getEstadoCivil() {
    return estadoCivil;
  }

  public void setEstadoCivil(String estadoCivil) {
    this.estadoCivil = estadoCivil;
  }

  public String getNacionalidade() {
    return nacionalidade;
  }

  public void setNacionalidade(String nacionalidade) {
    this.nacionalidade = nacionalidade;
  }

  public String getNaturalidadeCidade() {
    return naturalidadeCidade;
  }

  public void setNaturalidadeCidade(String naturalidadeCidade) {
    this.naturalidadeCidade = naturalidadeCidade;
  }

  public String getNaturalidadeUf() {
    return naturalidadeUf;
  }

  public void setNaturalidadeUf(String naturalidadeUf) {
    this.naturalidadeUf = naturalidadeUf;
  }

  public String getNomeMae() {
    return nomeMae;
  }

  public void setNomeMae(String nomeMae) {
    this.nomeMae = nomeMae;
  }

  public String getNomePai() {
    return nomePai;
  }

  public void setNomePai(String nomePai) {
    this.nomePai = nomePai;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Boolean getOptaReceberCestaBasica() {
    return optaReceberCestaBasica;
  }

  public void setOptaReceberCestaBasica(Boolean optaReceberCestaBasica) {
    this.optaReceberCestaBasica = optaReceberCestaBasica;
  }

  public Boolean getAptoReceberCestaBasica() {
    return aptoReceberCestaBasica;
  }

  public void setAptoReceberCestaBasica(Boolean aptoReceberCestaBasica) {
    this.aptoReceberCestaBasica = aptoReceberCestaBasica;
  }

  public Endereco getEndereco() {
    return endereco;
  }

  public void setEndereco(Endereco endereco) {
    this.endereco = endereco;
  }

  public List<ContatoBeneficiario> getContatos() {
    return contatos;
  }

  public void setContatos(List<ContatoBeneficiario> contatos) {
    this.contatos = contatos;
  }

  public List<DocumentoBeneficiario> getDocumentos() {
    return documentos;
  }

  public void setDocumentos(List<DocumentoBeneficiario> documentos) {
    this.documentos = documentos;
  }

  public List<SituacaoSocialBeneficiario> getSituacoesSociais() {
    return situacoesSociais;
  }

  public void setSituacoesSociais(List<SituacaoSocialBeneficiario> situacoesSociais) {
    this.situacoesSociais = situacoesSociais;
  }

  public List<EscolaridadeBeneficiario> getEscolaridades() {
    return escolaridades;
  }

  public void setEscolaridades(List<EscolaridadeBeneficiario> escolaridades) {
    this.escolaridades = escolaridades;
  }

  public List<SaudeBeneficiario> getSaudes() {
    return saudes;
  }

  public void setSaudes(List<SaudeBeneficiario> saudes) {
    this.saudes = saudes;
  }

  public List<BeneficiosBeneficiario> getBeneficios() {
    return beneficios;
  }

  public void setBeneficios(List<BeneficiosBeneficiario> beneficios) {
    this.beneficios = beneficios;
  }

  public List<ObservacoesBeneficiario> getObservacoesRegistros() {
    return observacoesRegistros;
  }

  public void setObservacoesRegistros(List<ObservacoesBeneficiario> observacoesRegistros) {
    this.observacoesRegistros = observacoesRegistros;
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
