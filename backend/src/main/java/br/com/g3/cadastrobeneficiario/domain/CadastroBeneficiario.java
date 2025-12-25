package br.com.g3.cadastrobeneficiario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import br.com.g3.unidadeassistencial.domain.Endereco;

@Entity
@Table(name = "cadastro_beneficiario")
public class CadastroBeneficiario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "nome_completo", length = 200, nullable = false)
  private String nomeCompleto;

  @Column(name = "nome_social", length = 200)
  private String nomeSocial;

  @Column(name = "apelido", length = 120)
  private String apelido;

  @Column(name = "data_nascimento", nullable = false)
  private LocalDate dataNascimento;

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

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "endereco_id")
  private Endereco endereco;

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

  public Endereco getEndereco() {
    return endereco;
  }

  public void setEndereco(Endereco endereco) {
    this.endereco = endereco;
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
