package br.com.g3.recebimentodoacao.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "doador")
public class Doador {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "nome", length = 200, nullable = false)
  private String nome;

  @Column(name = "tipo_pessoa", length = 20)
  private String tipoPessoa;

  @Column(name = "documento", length = 30)
  private String documento;

  @Column(name = "responsavel_empresa", length = 200)
  private String responsavelEmpresa;

  @Column(name = "email", length = 150)
  private String email;

  @Column(name = "telefone", length = 30)
  private String telefone;

  @Column(name = "logradouro", length = 200)
  private String logradouro;

  @Column(name = "numero", length = 40)
  private String numero;

  @Column(name = "complemento", length = 120)
  private String complemento;

  @Column(name = "bairro", length = 120)
  private String bairro;

  @Column(name = "cidade", length = 120)
  private String cidade;

  @Column(name = "uf", length = 2)
  private String uf;

  @Column(name = "cep", length = 12)
  private String cep;

  @Column(name = "observacoes")
  private String observacoes;

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

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getTipoPessoa() {
    return tipoPessoa;
  }

  public void setTipoPessoa(String tipoPessoa) {
    this.tipoPessoa = tipoPessoa;
  }

  public String getDocumento() {
    return documento;
  }

  public void setDocumento(String documento) {
    this.documento = documento;
  }

  public String getResponsavelEmpresa() {
    return responsavelEmpresa;
  }

  public void setResponsavelEmpresa(String responsavelEmpresa) {
    this.responsavelEmpresa = responsavelEmpresa;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
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

  public String getCidade() {
    return cidade;
  }

  public void setCidade(String cidade) {
    this.cidade = cidade;
  }

  public String getUf() {
    return uf;
  }

  public void setUf(String uf) {
    this.uf = uf;
  }

  public String getCep() {
    return cep;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
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
