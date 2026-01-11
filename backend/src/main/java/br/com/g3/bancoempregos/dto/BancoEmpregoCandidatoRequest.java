package br.com.g3.bancoempregos.dto;

public class BancoEmpregoCandidatoRequest {
  private Long beneficiarioId;
  private String beneficiarioNome;
  private String necessidadesProfissionais;
  private String status;
  private String curriculoNome;
  private String curriculoTipo;
  private String curriculoConteudo;

  public Long getBeneficiarioId() {
    return beneficiarioId;
  }

  public void setBeneficiarioId(Long beneficiarioId) {
    this.beneficiarioId = beneficiarioId;
  }

  public String getBeneficiarioNome() {
    return beneficiarioNome;
  }

  public void setBeneficiarioNome(String beneficiarioNome) {
    this.beneficiarioNome = beneficiarioNome;
  }

  public String getNecessidadesProfissionais() {
    return necessidadesProfissionais;
  }

  public void setNecessidadesProfissionais(String necessidadesProfissionais) {
    this.necessidadesProfissionais = necessidadesProfissionais;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getCurriculoNome() {
    return curriculoNome;
  }

  public void setCurriculoNome(String curriculoNome) {
    this.curriculoNome = curriculoNome;
  }

  public String getCurriculoTipo() {
    return curriculoTipo;
  }

  public void setCurriculoTipo(String curriculoTipo) {
    this.curriculoTipo = curriculoTipo;
  }

  public String getCurriculoConteudo() {
    return curriculoConteudo;
  }

  public void setCurriculoConteudo(String curriculoConteudo) {
    this.curriculoConteudo = curriculoConteudo;
  }
}
