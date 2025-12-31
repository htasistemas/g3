package br.com.g3.recebimentodoacao.dto;

public class DoadorResponse {
  private Long id;
  private String nome;
  private String tipoPessoa;
  private String documento;
  private String responsavelEmpresa;
  private String email;
  private String telefone;
  private String observacoes;

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

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }
}
