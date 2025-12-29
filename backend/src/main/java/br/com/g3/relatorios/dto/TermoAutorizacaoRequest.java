package br.com.g3.relatorios.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TermoAutorizacaoRequest {
  @JsonProperty("beneficiaryName")
  private String beneficiaryName;

  @JsonProperty("birthDate")
  private String birthDate;

  @JsonProperty("motherName")
  private String motherName;

  @JsonProperty("cpf")
  private String cpf;

  @JsonProperty("rg")
  private String rg;

  @JsonProperty("nis")
  private String nis;

  @JsonProperty("address")
  private String address;

  @JsonProperty("contact")
  private String contact;

  @JsonProperty("issueDate")
  private String issueDate;

  @JsonProperty("unit")
  private UnidadeAssistencialRequest unit;

  public String getBeneficiaryName() {
    return beneficiaryName;
  }

  public void setBeneficiaryName(String beneficiaryName) {
    this.beneficiaryName = beneficiaryName;
  }

  public String getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(String birthDate) {
    this.birthDate = birthDate;
  }

  public String getMotherName() {
    return motherName;
  }

  public void setMotherName(String motherName) {
    this.motherName = motherName;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getRg() {
    return rg;
  }

  public void setRg(String rg) {
    this.rg = rg;
  }

  public String getNis() {
    return nis;
  }

  public void setNis(String nis) {
    this.nis = nis;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public String getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(String issueDate) {
    this.issueDate = issueDate;
  }

  public UnidadeAssistencialRequest getUnit() {
    return unit;
  }

  public void setUnit(UnidadeAssistencialRequest unit) {
    this.unit = unit;
  }

  public static class UnidadeAssistencialRequest {
    @JsonProperty("nomeFantasia")
    private String nomeFantasia;

    @JsonProperty("razaoSocial")
    private String razaoSocial;

    @JsonProperty("cnpj")
    private String cnpj;

    @JsonProperty("telefone")
    private String telefone;

    @JsonProperty("email")
    private String email;

    @JsonProperty("endereco")
    private String endereco;

    @JsonProperty("numeroEndereco")
    private String numeroEndereco;

    @JsonProperty("complemento")
    private String complemento;

    @JsonProperty("bairro")
    private String bairro;

    @JsonProperty("pontoReferencia")
    private String pontoReferencia;

    @JsonProperty("cidade")
    private String cidade;

    @JsonProperty("estado")
    private String estado;

    public String getNomeFantasia() {
      return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
      this.nomeFantasia = nomeFantasia;
    }

    public String getRazaoSocial() {
      return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
      this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
      return cnpj;
    }

    public void setCnpj(String cnpj) {
      this.cnpj = cnpj;
    }

    public String getTelefone() {
      return telefone;
    }

    public void setTelefone(String telefone) {
      this.telefone = telefone;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getEndereco() {
      return endereco;
    }

    public void setEndereco(String endereco) {
      this.endereco = endereco;
    }

    public String getNumeroEndereco() {
      return numeroEndereco;
    }

    public void setNumeroEndereco(String numeroEndereco) {
      this.numeroEndereco = numeroEndereco;
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

    public String getCidade() {
      return cidade;
    }

    public void setCidade(String cidade) {
      this.cidade = cidade;
    }

    public String getEstado() {
      return estado;
    }

    public void setEstado(String estado) {
      this.estado = estado;
    }
  }
}
