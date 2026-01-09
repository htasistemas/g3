package br.com.g3.chamadotecnico.dto;

import br.com.g3.chamadotecnico.domain.ChamadoImpacto;
import br.com.g3.chamadotecnico.domain.ChamadoPrioridade;
import br.com.g3.chamadotecnico.domain.ChamadoStatus;
import br.com.g3.chamadotecnico.domain.ChamadoTipo;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;

public class ChamadoTecnicoResponse {
  private UUID id;
  private String codigo;
  private String titulo;
  private String descricao;
  private ChamadoTipo tipo;
  private ChamadoStatus status;
  private ChamadoPrioridade prioridade;
  private ChamadoImpacto impacto;
  private String modulo;
  private String menu;
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
  @JsonProperty("data_limite_sla")
  private LocalDateTime dataLimiteSla;
  @JsonProperty("resposta_desenvolvedor")
  private String respostaDesenvolvedor;
  @JsonProperty("respondido_em")
  private LocalDateTime respondidoEm;
  @JsonProperty("respondido_por_usuario_id")
  private Long respondidoPorUsuarioId;
  @JsonProperty("responsavel_usuario_id")
  private Long responsavelUsuarioId;
  @JsonProperty("criado_por_usuario_id")
  private Long criadoPorUsuarioId;
  private String tags;
  @JsonProperty("criado_em")
  private LocalDateTime criadoEm;
  @JsonProperty("atualizado_em")
  private LocalDateTime atualizadoEm;
  @JsonProperty("sla_atrasado")
  private Boolean slaAtrasado;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

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

  public LocalDateTime getDataLimiteSla() {
    return dataLimiteSla;
  }

  public void setDataLimiteSla(LocalDateTime dataLimiteSla) {
    this.dataLimiteSla = dataLimiteSla;
  }

  public String getRespostaDesenvolvedor() {
    return respostaDesenvolvedor;
  }

  public void setRespostaDesenvolvedor(String respostaDesenvolvedor) {
    this.respostaDesenvolvedor = respostaDesenvolvedor;
  }

  public LocalDateTime getRespondidoEm() {
    return respondidoEm;
  }

  public void setRespondidoEm(LocalDateTime respondidoEm) {
    this.respondidoEm = respondidoEm;
  }

  public Long getRespondidoPorUsuarioId() {
    return respondidoPorUsuarioId;
  }

  public void setRespondidoPorUsuarioId(Long respondidoPorUsuarioId) {
    this.respondidoPorUsuarioId = respondidoPorUsuarioId;
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

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }

  public LocalDateTime getAtualizadoEm() {
    return atualizadoEm;
  }

  public void setAtualizadoEm(LocalDateTime atualizadoEm) {
    this.atualizadoEm = atualizadoEm;
  }

  public Boolean getSlaAtrasado() {
    return slaAtrasado;
  }

  public void setSlaAtrasado(Boolean slaAtrasado) {
    this.slaAtrasado = slaAtrasado;
  }
}
