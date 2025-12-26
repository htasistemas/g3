package br.com.g3.cadastroprofissionais.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "cadastro_profissionais")
public class CadastroProfissional {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "nome_completo", length = 200, nullable = false)
  private String nomeCompleto;

  @Column(name = "categoria", length = 120, nullable = false)
  private String categoria;

  @Column(name = "registro_conselho", length = 120)
  private String registroConselho;

  @Column(name = "especialidade", length = 120)
  private String especialidade;

  @Column(name = "email", length = 150)
  private String email;

  @Column(name = "telefone", length = 30)
  private String telefone;

  @Column(name = "unidade", length = 200)
  private String unidade;

  @Column(name = "carga_horaria")
  private Integer cargaHoraria;

  @Column(name = "disponibilidade")
  private String disponibilidade;

  @Column(name = "canais_atendimento")
  private String canaisAtendimento;

  @Column(name = "status", length = 60)
  private String status;

  @Column(name = "tags")
  private String tags;

  @Column(name = "resumo")
  private String resumo;

  @Column(name = "observacoes")
  private String observacoes;

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

  public String getNomeCompleto() {
    return nomeCompleto;
  }

  public void setNomeCompleto(String nomeCompleto) {
    this.nomeCompleto = nomeCompleto;
  }

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public String getRegistroConselho() {
    return registroConselho;
  }

  public void setRegistroConselho(String registroConselho) {
    this.registroConselho = registroConselho;
  }

  public String getEspecialidade() {
    return especialidade;
  }

  public void setEspecialidade(String especialidade) {
    this.especialidade = especialidade;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public String getUnidade() {
    return unidade;
  }

  public void setUnidade(String unidade) {
    this.unidade = unidade;
  }

  public Integer getCargaHoraria() {
    return cargaHoraria;
  }

  public void setCargaHoraria(Integer cargaHoraria) {
    this.cargaHoraria = cargaHoraria;
  }

  public String getDisponibilidade() {
    return disponibilidade;
  }

  public void setDisponibilidade(String disponibilidade) {
    this.disponibilidade = disponibilidade;
  }

  public String getCanaisAtendimento() {
    return canaisAtendimento;
  }

  public void setCanaisAtendimento(String canaisAtendimento) {
    this.canaisAtendimento = canaisAtendimento;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  public String getResumo() {
    return resumo;
  }

  public void setResumo(String resumo) {
    this.resumo = resumo;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
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
