package br.com.g3.relatorios.dto;

public class BeneficiarioRelacaoRequest {
  private String nome;
  private String cpf;
  private String codigo;
  private String status;
  private String dataNascimento;
  private String ordenarPor;
  private String ordem;
  private String usuarioEmissor;

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getDataNascimento() {
    return dataNascimento;
  }

  public void setDataNascimento(String dataNascimento) {
    this.dataNascimento = dataNascimento;
  }

  public String getOrdenarPor() {
    return ordenarPor;
  }

  public void setOrdenarPor(String ordenarPor) {
    this.ordenarPor = ordenarPor;
  }

  public String getOrdem() {
    return ordem;
  }

  public void setOrdem(String ordem) {
    this.ordem = ordem;
  }

  public String getUsuarioEmissor() {
    return usuarioEmissor;
  }

  public void setUsuarioEmissor(String usuarioEmissor) {
    this.usuarioEmissor = usuarioEmissor;
  }
}
