package br.com.g3.bancoempregos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "banco_empregos")
public class BancoEmprego {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "titulo", length = 200, nullable = false)
  private String titulo;

  @Column(name = "area", length = 120)
  private String area;

  @Column(name = "tipo", length = 80)
  private String tipo;

  @Column(name = "nivel", length = 80)
  private String nivel;

  @Column(name = "modelo", length = 80)
  private String modelo;

  @Column(name = "status", length = 20, nullable = false)
  private String status;

  @Column(name = "data_abertura")
  private LocalDate dataAbertura;

  @Column(name = "data_encerramento")
  private LocalDate dataEncerramento;

  @Column(name = "tipo_contrato", length = 80)
  private String tipoContrato;

  @Column(name = "carga_horaria", length = 80)
  private String cargaHoraria;

  @Column(name = "salario", length = 80)
  private String salario;

  @Column(name = "beneficios", columnDefinition = "TEXT")
  private String beneficios;

  @Column(name = "nome_empresa", length = 200)
  private String nomeEmpresa;

  @Column(name = "cnpj", length = 20)
  private String cnpj;

  @Column(name = "responsavel", length = 150)
  private String responsavel;

  @Column(name = "telefone", length = 60)
  private String telefone;

  @Column(name = "email", length = 150)
  private String email;

  @Column(name = "endereco", length = 200)
  private String endereco;

  @Column(name = "bairro", length = 120)
  private String bairro;

  @Column(name = "cidade", length = 120)
  private String cidade;

  @Column(name = "uf", length = 10)
  private String uf;

  @Column(name = "escolaridade", length = 120)
  private String escolaridade;

  @Column(name = "experiencia", length = 200)
  private String experiencia;

  @Column(name = "habilidades", columnDefinition = "TEXT")
  private String habilidades;

  @Column(name = "requisitos", columnDefinition = "TEXT")
  private String requisitos;

  @Column(name = "descricao", columnDefinition = "TEXT")
  private String descricao;

  @Column(name = "observacoes", columnDefinition = "TEXT")
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

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getNivel() {
    return nivel;
  }

  public void setNivel(String nivel) {
    this.nivel = nivel;
  }

  public String getModelo() {
    return modelo;
  }

  public void setModelo(String modelo) {
    this.modelo = modelo;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDate getDataAbertura() {
    return dataAbertura;
  }

  public void setDataAbertura(LocalDate dataAbertura) {
    this.dataAbertura = dataAbertura;
  }

  public LocalDate getDataEncerramento() {
    return dataEncerramento;
  }

  public void setDataEncerramento(LocalDate dataEncerramento) {
    this.dataEncerramento = dataEncerramento;
  }

  public String getTipoContrato() {
    return tipoContrato;
  }

  public void setTipoContrato(String tipoContrato) {
    this.tipoContrato = tipoContrato;
  }

  public String getCargaHoraria() {
    return cargaHoraria;
  }

  public void setCargaHoraria(String cargaHoraria) {
    this.cargaHoraria = cargaHoraria;
  }

  public String getSalario() {
    return salario;
  }

  public void setSalario(String salario) {
    this.salario = salario;
  }

  public String getBeneficios() {
    return beneficios;
  }

  public void setBeneficios(String beneficios) {
    this.beneficios = beneficios;
  }

  public String getNomeEmpresa() {
    return nomeEmpresa;
  }

  public void setNomeEmpresa(String nomeEmpresa) {
    this.nomeEmpresa = nomeEmpresa;
  }

  public String getCnpj() {
    return cnpj;
  }

  public void setCnpj(String cnpj) {
    this.cnpj = cnpj;
  }

  public String getResponsavel() {
    return responsavel;
  }

  public void setResponsavel(String responsavel) {
    this.responsavel = responsavel;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEndereco() {
    return endereco;
  }

  public void setEndereco(String endereco) {
    this.endereco = endereco;
  }

  public String getBairro() {
    return bairro;
  }

  public void setBairro(String bairro) {
    this.bairro = bairro;
  }

  public String getCidade() {
    return cidade;
  }

  public void setCidade(String cidade) {
    this.cidade = cidade;
  }

  public String getUf() {
    return uf;
  }

  public void setUf(String uf) {
    this.uf = uf;
  }

  public String getEscolaridade() {
    return escolaridade;
  }

  public void setEscolaridade(String escolaridade) {
    this.escolaridade = escolaridade;
  }

  public String getExperiencia() {
    return experiencia;
  }

  public void setExperiencia(String experiencia) {
    this.experiencia = experiencia;
  }

  public String getHabilidades() {
    return habilidades;
  }

  public void setHabilidades(String habilidades) {
    this.habilidades = habilidades;
  }

  public String getRequisitos() {
    return requisitos;
  }

  public void setRequisitos(String requisitos) {
    this.requisitos = requisitos;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
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
