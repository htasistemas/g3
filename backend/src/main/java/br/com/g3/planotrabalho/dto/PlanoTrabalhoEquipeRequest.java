package br.com.g3.planotrabalho.dto;

public class PlanoTrabalhoEquipeRequest {
  private Long id;
  private String nome;
  private String funcao;
  private String cpf;
  private String cargaHoraria;
  private String tipoVinculo;
  private String contato;

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

  public String getFuncao() {
    return funcao;
  }

  public void setFuncao(String funcao) {
    this.funcao = funcao;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getCargaHoraria() {
    return cargaHoraria;
  }

  public void setCargaHoraria(String cargaHoraria) {
    this.cargaHoraria = cargaHoraria;
  }

  public String getTipoVinculo() {
    return tipoVinculo;
  }

  public void setTipoVinculo(String tipoVinculo) {
    this.tipoVinculo = tipoVinculo;
  }

  public String getContato() {
    return contato;
  }

  public void setContato(String contato) {
    this.contato = contato;
  }
}
