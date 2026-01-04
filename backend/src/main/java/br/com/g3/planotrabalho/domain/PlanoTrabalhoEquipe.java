package br.com.g3.planotrabalho.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "plano_trabalho_equipe")
public class PlanoTrabalhoEquipe {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "plano_trabalho_id", nullable = false)
  private Long planoTrabalhoId;

  @Column(name = "nome", nullable = false, length = 200)
  private String nome;

  @Column(name = "funcao", length = 200)
  private String funcao;

  @Column(name = "cpf", length = 20)
  private String cpf;

  @Column(name = "carga_horaria", length = 80)
  private String cargaHoraria;

  @Column(name = "tipo_vinculo", length = 120)
  private String tipoVinculo;

  @Column(name = "contato", length = 200)
  private String contato;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPlanoTrabalhoId() {
    return planoTrabalhoId;
  }

  public void setPlanoTrabalhoId(Long planoTrabalhoId) {
    this.planoTrabalhoId = planoTrabalhoId;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getFuncao() {
    return funcao;
  }

  public void setFuncao(String funcao) {
    this.funcao = funcao;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getCargaHoraria() {
    return cargaHoraria;
  }

  public void setCargaHoraria(String cargaHoraria) {
    this.cargaHoraria = cargaHoraria;
  }

  public String getTipoVinculo() {
    return tipoVinculo;
  }

  public void setTipoVinculo(String tipoVinculo) {
    this.tipoVinculo = tipoVinculo;
  }

  public String getContato() {
    return contato;
  }

  public void setContato(String contato) {
    this.contato = contato;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
