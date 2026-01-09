package br.com.g3.chamadotecnico.dto;

import br.com.g3.chamadotecnico.domain.ChamadoImpacto;
import br.com.g3.chamadotecnico.domain.ChamadoPrioridade;
import br.com.g3.chamadotecnico.domain.ChamadoStatus;
import br.com.g3.chamadotecnico.domain.ChamadoTipo;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ChamadoTecnicoCriacaoRequest {
  @NotBlank
  private String titulo;

  @NotBlank
  private String descricao;

  @NotNull
  private ChamadoTipo tipo;

  private ChamadoStatus status;

  @NotNull
  private ChamadoPrioridade prioridade;

  @NotNull
  private ChamadoImpacto impacto;

  @NotBlank
  private String modulo;

  @NotBlank
  private String menu;

  @NotBlank
  private String cliente;

  private String ambiente;

  @JsonProperty("versao_sistema")
  private String versaoSistema;

  @JsonProperty("passos_reproducao")
  private String passosReproducao;

  @JsonProperty("resultado_atual")
  private String resultadoAtual;

  @JsonProperty("resultado_esperado")
  private String resultadoEsperado;

  @JsonProperty("usuarios_teste")
  private String usuariosTeste;

  @JsonProperty("prazo_sla_em_horas")
  private Integer prazoSlaEmHoras;

  @JsonProperty("responsavel_usuario_id")
  private Long responsavelUsuarioId;

  @JsonProperty("criado_por_usuario_id")
  private Long criadoPorUsuarioId;

  private String tags;

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public ChamadoTipo getTipo() {
    return tipo;
  }

  public void setTipo(ChamadoTipo tipo) {
    this.tipo = tipo;
  }

  public ChamadoStatus getStatus() {
    return status;
  }

  public void setStatus(ChamadoStatus status) {
    this.status = status;
  }

  public ChamadoPrioridade getPrioridade() {
    return prioridade;
  }

  public void setPrioridade(ChamadoPrioridade prioridade) {
    this.prioridade = prioridade;
  }

  public ChamadoImpacto getImpacto() {
    return impacto;
  }

  public void setImpacto(ChamadoImpacto impacto) {
    this.impacto = impacto;
  }

  public String getModulo() {
    return modulo;
  }

  public void setModulo(String modulo) {
    this.modulo = modulo;
  }

  public String getMenu() {
    return menu;
  }

  public void setMenu(String menu) {
    this.menu = menu;
  }

  public String getCliente() {
    return cliente;
  }

  public void setCliente(String cliente) {
    this.cliente = cliente;
  }

  public String getAmbiente() {
    return ambiente;
  }

  public void setAmbiente(String ambiente) {
    this.ambiente = ambiente;
  }

  public String getVersaoSistema() {
    return versaoSistema;
  }

  public void setVersaoSistema(String versaoSistema) {
    this.versaoSistema = versaoSistema;
  }

  public String getPassosReproducao() {
    return passosReproducao;
  }

  public void setPassosReproducao(String passosReproducao) {
    this.passosReproducao = passosReproducao;
  }

  public String getResultadoAtual() {
    return resultadoAtual;
  }

  public void setResultadoAtual(String resultadoAtual) {
    this.resultadoAtual = resultadoAtual;
  }

  public String getResultadoEsperado() {
    return resultadoEsperado;
  }

  public void setResultadoEsperado(String resultadoEsperado) {
    this.resultadoEsperado = resultadoEsperado;
  }

  public String getUsuariosTeste() {
    return usuariosTeste;
  }

  public void setUsuariosTeste(String usuariosTeste) {
    this.usuariosTeste = usuariosTeste;
  }

  public Integer getPrazoSlaEmHoras() {
    return prazoSlaEmHoras;
  }

  public void setPrazoSlaEmHoras(Integer prazoSlaEmHoras) {
    this.prazoSlaEmHoras = prazoSlaEmHoras;
  }

  public Long getResponsavelUsuarioId() {
    return responsavelUsuarioId;
  }

  public void setResponsavelUsuarioId(Long responsavelUsuarioId) {
    this.responsavelUsuarioId = responsavelUsuarioId;
  }

  public Long getCriadoPorUsuarioId() {
    return criadoPorUsuarioId;
  }

  public void setCriadoPorUsuarioId(Long criadoPorUsuarioId) {
    this.criadoPorUsuarioId = criadoPorUsuarioId;
  }

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }
}
