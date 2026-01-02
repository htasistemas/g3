package br.com.g3.visitadomiciliar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class VisitaDomiciliarRequest {
  @NotNull
  private Long beneficiarioId;

  @NotBlank
  private String unidade;

  @NotBlank
  private String responsavel;

  @NotNull
  private LocalDate dataVisita;

  @NotNull
  private LocalTime horarioInicial;

  private LocalTime horarioFinal;

  private String tipoVisita;

  @NotBlank
  private String situacao;

  private Boolean usarEnderecoBeneficiario;

  private Map<String, Object> endereco;

  private String observacoesIniciais;

  private Map<String, Object> condicoes;

  private Map<String, Object> situacaoSocial;

  private Map<String, Object> registro;

  private List<VisitaDomiciliarAnexoRequest> anexos;

  public Long getBeneficiarioId() {
    return beneficiarioId;
  }

  public void setBeneficiarioId(Long beneficiarioId) {
    this.beneficiarioId = beneficiarioId;
  }

  public String getUnidade() {
    return unidade;
  }

  public void setUnidade(String unidade) {
    this.unidade = unidade;
  }

  public String getResponsavel() {
    return responsavel;
  }

  public void setResponsavel(String responsavel) {
    this.responsavel = responsavel;
  }

  public LocalDate getDataVisita() {
    return dataVisita;
  }

  public void setDataVisita(LocalDate dataVisita) {
    this.dataVisita = dataVisita;
  }

  public LocalTime getHorarioInicial() {
    return horarioInicial;
  }

  public void setHorarioInicial(LocalTime horarioInicial) {
    this.horarioInicial = horarioInicial;
  }

  public LocalTime getHorarioFinal() {
    return horarioFinal;
  }

  public void setHorarioFinal(LocalTime horarioFinal) {
    this.horarioFinal = horarioFinal;
  }

  public String getTipoVisita() {
    return tipoVisita;
  }

  public void setTipoVisita(String tipoVisita) {
    this.tipoVisita = tipoVisita;
  }

  public String getSituacao() {
    return situacao;
  }

  public void setSituacao(String situacao) {
    this.situacao = situacao;
  }

  public Boolean getUsarEnderecoBeneficiario() {
    return usarEnderecoBeneficiario;
  }

  public void setUsarEnderecoBeneficiario(Boolean usarEnderecoBeneficiario) {
    this.usarEnderecoBeneficiario = usarEnderecoBeneficiario;
  }

  public Map<String, Object> getEndereco() {
    return endereco;
  }

  public void setEndereco(Map<String, Object> endereco) {
    this.endereco = endereco;
  }

  public String getObservacoesIniciais() {
    return observacoesIniciais;
  }

  public void setObservacoesIniciais(String observacoesIniciais) {
    this.observacoesIniciais = observacoesIniciais;
  }

  public Map<String, Object> getCondicoes() {
    return condicoes;
  }

  public void setCondicoes(Map<String, Object> condicoes) {
    this.condicoes = condicoes;
  }

  public Map<String, Object> getSituacaoSocial() {
    return situacaoSocial;
  }

  public void setSituacaoSocial(Map<String, Object> situacaoSocial) {
    this.situacaoSocial = situacaoSocial;
  }

  public Map<String, Object> getRegistro() {
    return registro;
  }

  public void setRegistro(Map<String, Object> registro) {
    this.registro = registro;
  }

  public List<VisitaDomiciliarAnexoRequest> getAnexos() {
    return anexos;
  }

  public void setAnexos(List<VisitaDomiciliarAnexoRequest> anexos) {
    this.anexos = anexos;
  }
}
