package br.com.g3.prontuario.dto;

public class BeneficiarioResumoResponse {
  private Long id;
  private String nomeCompleto;
  private String cpf;
  private String nis;
  private String dataNascimento;
  private String nomeMae;
  private String whatsapp;
  private String telefone;
  private String endereco;
  private String status;
  private String foto3x4;
  private java.util.List<String> vulnerabilidades;
  private String familiaReferencia;

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

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getNis() {
    return nis;
  }

  public void setNis(String nis) {
    this.nis = nis;
  }

  public String getDataNascimento() {
    return dataNascimento;
  }

  public void setDataNascimento(String dataNascimento) {
    this.dataNascimento = dataNascimento;
  }

  public String getNomeMae() {
    return nomeMae;
  }

  public void setNomeMae(String nomeMae) {
    this.nomeMae = nomeMae;
  }

  public String getWhatsapp() {
    return whatsapp;
  }

  public void setWhatsapp(String whatsapp) {
    this.whatsapp = whatsapp;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public String getEndereco() {
    return endereco;
  }

  public void setEndereco(String endereco) {
    this.endereco = endereco;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getFoto3x4() {
    return foto3x4;
  }

  public void setFoto3x4(String foto3x4) {
    this.foto3x4 = foto3x4;
  }

  public String getFamiliaReferencia() {
    return familiaReferencia;
  }

  public void setFamiliaReferencia(String familiaReferencia) {
    this.familiaReferencia = familiaReferencia;
  }

  public java.util.List<String> getVulnerabilidades() {
    return vulnerabilidades;
  }

  public void setVulnerabilidades(java.util.List<String> vulnerabilidades) {
    this.vulnerabilidades = vulnerabilidades;
  }
}
