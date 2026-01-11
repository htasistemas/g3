package br.com.g3.bancoempregos.dto;

import java.time.LocalDateTime;

public class BancoEmpregoCandidatoResponse {
  private final Long id;
  private final Long empregoId;
  private final Long beneficiarioId;
  private final String beneficiarioNome;
  private final String necessidadesProfissionais;
  private final String status;
  private final String curriculoNome;
  private final String curriculoTipo;
  private final String curriculoConteudo;
  private final LocalDateTime criadoEm;

  public BancoEmpregoCandidatoResponse(
      Long id,
      Long empregoId,
      Long beneficiarioId,
      String beneficiarioNome,
      String necessidadesProfissionais,
      String status,
      String curriculoNome,
      String curriculoTipo,
      String curriculoConteudo,
      LocalDateTime criadoEm) {
    this.id = id;
    this.empregoId = empregoId;
    this.beneficiarioId = beneficiarioId;
    this.beneficiarioNome = beneficiarioNome;
    this.necessidadesProfissionais = necessidadesProfissionais;
    this.status = status;
    this.curriculoNome = curriculoNome;
    this.curriculoTipo = curriculoTipo;
    this.curriculoConteudo = curriculoConteudo;
    this.criadoEm = criadoEm;
  }

  public Long getId() {
    return id;
  }

  public Long getEmpregoId() {
    return empregoId;
  }

  public Long getBeneficiarioId() {
    return beneficiarioId;
  }

  public String getBeneficiarioNome() {
    return beneficiarioNome;
  }

  public String getNecessidadesProfissionais() {
    return necessidadesProfissionais;
  }

  public String getStatus() {
    return status;
  }

  public String getCurriculoNome() {
    return curriculoNome;
  }

  public String getCurriculoTipo() {
    return curriculoTipo;
  }

  public String getCurriculoConteudo() {
    return curriculoConteudo;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }
}
