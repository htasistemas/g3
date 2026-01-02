package br.com.g3.visitadomiciliar.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "visita_domiciliar")
public class VisitaDomiciliar {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "beneficiario_id", nullable = false)
  private Long beneficiarioId;

  @Column(name = "unidade", length = 200, nullable = false)
  private String unidade;

  @Column(name = "responsavel", length = 150, nullable = false)
  private String responsavel;

  @Column(name = "data_visita", nullable = false)
  private LocalDate dataVisita;

  @Column(name = "horario_inicial", nullable = false)
  private LocalTime horarioInicial;

  @Column(name = "horario_final")
  private LocalTime horarioFinal;

  @Column(name = "tipo_visita", length = 80)
  private String tipoVisita;

  @Column(name = "situacao", length = 30, nullable = false)
  private String situacao;

  @Column(name = "usar_endereco_beneficiario", nullable = false)
  private Boolean usarEnderecoBeneficiario;

  @Column(name = "endereco", columnDefinition = "JSONB")
  @JdbcTypeCode(SqlTypes.JSON)
  private Map<String, Object> endereco;

  @Column(name = "observacoes_iniciais", columnDefinition = "TEXT")
  private String observacoesIniciais;

  @Column(name = "condicoes", columnDefinition = "JSONB")
  @JdbcTypeCode(SqlTypes.JSON)
  private Map<String, Object> condicoes;

  @Column(name = "situacao_social", columnDefinition = "JSONB")
  @JdbcTypeCode(SqlTypes.JSON)
  private Map<String, Object> situacaoSocial;

  @Column(name = "registro", columnDefinition = "JSONB")
  @JdbcTypeCode(SqlTypes.JSON)
  private Map<String, Object> registro;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  @Column(name = "atualizado_em", nullable = false)
  private LocalDateTime atualizadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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
