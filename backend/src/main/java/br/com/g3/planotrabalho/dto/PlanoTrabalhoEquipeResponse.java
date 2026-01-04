package br.com.g3.planotrabalho.dto;

public class PlanoTrabalhoEquipeResponse {
  private final Long id;
  private final String nome;
  private final String funcao;
  private final String cpf;
  private final String cargaHoraria;
  private final String tipoVinculo;
  private final String contato;

  public PlanoTrabalhoEquipeResponse(
      Long id,
      String nome,
      String funcao,
      String cpf,
      String cargaHoraria,
      String tipoVinculo,
      String contato) {
    this.id = id;
    this.nome = nome;
    this.funcao = funcao;
    this.cpf = cpf;
    this.cargaHoraria = cargaHoraria;
    this.tipoVinculo = tipoVinculo;
    this.contato = contato;
  }

  public Long getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  public String getFuncao() {
    return funcao;
  }

  public String getCpf() {
    return cpf;
  }

  public String getCargaHoraria() {
    return cargaHoraria;
  }

  public String getTipoVinculo() {
    return tipoVinculo;
  }

  public String getContato() {
    return contato;
  }
}
