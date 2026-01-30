package br.com.g3.senhas.domain;

import br.com.g3.cadastrobeneficiario.domain.CadastroBeneficiario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "senha_fila")
public class SenhaFila {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "beneficiario_id", nullable = false)
  private CadastroBeneficiario beneficiario;

  @Column(name = "status", nullable = false, length = 20)
  private String status;

  @Column(name = "prioridade", nullable = false)
  private Integer prioridade;

  @Column(name = "data_hora_entrada", nullable = false)
  private LocalDateTime dataHoraEntrada;

  @Column(name = "data_hora_atualizacao", nullable = false)
  private LocalDateTime dataHoraAtualizacao;

  @Column(name = "unidade_id")
  private Long unidadeId;

  @Column(name = "usuario_id")
  private Long usuarioId;

  @Column(name = "sala_atendimento", length = 120)
  private String salaAtendimento;

  public Long getId() {
    return id;
  }

  public CadastroBeneficiario getBeneficiario() {
    return beneficiario;
  }

  public void setBeneficiario(CadastroBeneficiario beneficiario) {
    this.beneficiario = beneficiario;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Integer getPrioridade() {
    return prioridade;
  }

  public void setPrioridade(Integer prioridade) {
    this.prioridade = prioridade;
  }

  public LocalDateTime getDataHoraEntrada() {
    return dataHoraEntrada;
  }

  public void setDataHoraEntrada(LocalDateTime dataHoraEntrada) {
    this.dataHoraEntrada = dataHoraEntrada;
  }

  public LocalDateTime getDataHoraAtualizacao() {
    return dataHoraAtualizacao;
  }

  public void setDataHoraAtualizacao(LocalDateTime dataHoraAtualizacao) {
    this.dataHoraAtualizacao = dataHoraAtualizacao;
  }

  public Long getUnidadeId() {
    return unidadeId;
  }

  public void setUnidadeId(Long unidadeId) {
    this.unidadeId = unidadeId;
  }

  public Long getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(Long usuarioId) {
    this.usuarioId = usuarioId;
  }

  public String getSalaAtendimento() {
    return salaAtendimento;
  }

  public void setSalaAtendimento(String salaAtendimento) {
    this.salaAtendimento = salaAtendimento;
  }
}
