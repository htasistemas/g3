package br.com.g3.chamadotecnico.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "chamado_tecnico")
public class ChamadoTecnico {
  @Id
  @Column(name = "id", columnDefinition = "uuid")
  private UUID id;

  @Column(name = "codigo", length = 20, nullable = false)
  private String codigo;

  @Column(name = "titulo", length = 200, nullable = false)
  private String titulo;

  @Column(name = "descricao", columnDefinition = "TEXT", nullable = false)
  private String descricao;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(name = "tipo", columnDefinition = "chamado_tipo", nullable = false)
  private ChamadoTipo tipo;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(name = "status", columnDefinition = "chamado_status", nullable = false)
  private ChamadoStatus status;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(name = "prioridade", columnDefinition = "chamado_prioridade", nullable = false)
  private ChamadoPrioridade prioridade;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(name = "impacto", columnDefinition = "chamado_impacto", nullable = false)
  private ChamadoImpacto impacto;

  @Column(name = "modulo", length = 120, nullable = false)
  private String modulo;

  @Column(name = "menu", length = 160, nullable = false)
  private String menu;

  @Column(name = "cliente", length = 160, nullable = false)
  private String cliente;

  @Column(name = "ambiente", length = 40, nullable = false)
  private String ambiente;

  @Column(name = "versao_sistema", length = 40)
  private String versaoSistema;

  @Column(name = "passos_reproducao", columnDefinition = "TEXT")
  private String passosReproducao;

  @Column(name = "resultado_atual", columnDefinition = "TEXT")
  private String resultadoAtual;

  @Column(name = "resultado_esperado", columnDefinition = "TEXT")
  private String resultadoEsperado;

  @Column(name = "usuarios_teste", columnDefinition = "TEXT")
  private String usuariosTeste;

  @Column(name = "prazo_sla_em_horas")
  private Integer prazoSlaEmHoras;

  @Column(name = "data_limite_sla")
  private LocalDateTime dataLimiteSla;

  @Column(name = "resposta_desenvolvedor", columnDefinition = "TEXT")
  private String respostaDesenvolvedor;

  @Column(name = "respondido_em")
  private LocalDateTime respondidoEm;

  @Column(name = "respondido_por_usuario_id")
  private Long respondidoPorUsuarioId;

  @Column(name = "responsavel_usuario_id")
  private Long responsavelUsuarioId;

  @Column(name = "criado_por_usuario_id")
  private Long criadoPorUsuarioId;

  @Column(name = "tags", columnDefinition = "TEXT")
  private String tags;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  @Column(name = "atualizado_em", nullable = false)
  private LocalDateTime atualizadoEm;

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
}
