package br.com.g3.unidadeassistencial.domain;

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
@Table(name = "diretoria_unidade")
public class DiretoriaUnidade {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "unidade_id", nullable = false)
  private UnidadeAssistencial unidadeAssistencial;

  @Column(name = "nome_completo", length = 200, nullable = false)
  private String nomeCompleto;

  @Column(name = "documento", length = 60, nullable = false)
  private String documento;

  @Column(name = "funcao", length = 120, nullable = false)
  private String funcao;

  @Column(name = "mandato_inicio", length = 9)
  private String mandatoInicio;

  @Column(name = "mandato_fim", length = 9)
  private String mandatoFim;

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

  public UnidadeAssistencial getUnidadeAssistencial() {
    return unidadeAssistencial;
  }

  public void setUnidadeAssistencial(UnidadeAssistencial unidadeAssistencial) {
    this.unidadeAssistencial = unidadeAssistencial;
  }

  public String getNomeCompleto() {
    return nomeCompleto;
  }

  public void setNomeCompleto(String nomeCompleto) {
    this.nomeCompleto = nomeCompleto;
  }

  public String getDocumento() {
    return documento;
  }

  public void setDocumento(String documento) {
    this.documento = documento;
  }

  public String getFuncao() {
    return funcao;
  }

  public void setFuncao(String funcao) {
    this.funcao = funcao;
  }

  public String getMandatoInicio() {
    return mandatoInicio;
  }

  public void setMandatoInicio(String mandatoInicio) {
    this.mandatoInicio = mandatoInicio;
  }

  public String getMandatoFim() {
    return mandatoFim;
  }

  public void setMandatoFim(String mandatoFim) {
    this.mandatoFim = mandatoFim;
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
