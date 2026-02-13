package br.com.g3.rhcontratacao.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "rh_candidato")
public class RhCandidato {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "nome_completo", nullable = false, length = 200)
  private String nomeCompleto;

  @Column(name = "cpf", length = 20)
  private String cpf;

  @Column(name = "rg", length = 40)
  private String rg;

  @Column(name = "pis", length = 40)
  private String pis;

  @Column(name = "data_nascimento")
  private LocalDate dataNascimento;

  @Column(name = "naturalidade", length = 120)
  private String naturalidade;

  @Column(name = "estado_civil", length = 60)
  private String estadoCivil;

  @Column(name = "nome_mae", length = 200)
  private String nomeMae;

  @Column(name = "nome_conjuge", length = 200)
  private String nomeConjuge;

  @Column(name = "vaga_pretendida", length = 160)
  private String vagaPretendida;

  @Column(name = "data_preenchimento")
  private LocalDate dataPreenchimento;

  @Column(name = "filhos_possui", nullable = false)
  private Boolean filhosPossui;

  @Column(name = "filhos_json", columnDefinition = "TEXT")
  private String filhosJson;

  @Column(name = "deficiencia_possui", nullable = false)
  private Boolean deficienciaPossui;

  @Column(name = "deficiencia_tipo", length = 80)
  private String deficienciaTipo;

  @Column(name = "deficiencia_descricao", columnDefinition = "TEXT")
  private String deficienciaDescricao;

  @Column(name = "endereco_json", columnDefinition = "TEXT")
  private String enderecoJson;

  @Column(name = "telefone", length = 40)
  private String telefone;

  @Column(name = "whatsapp", length = 40)
  private String whatsapp;

  @Column(name = "anexos_json", columnDefinition = "TEXT")
  private String anexosJson;

  @Column(name = "ativo", nullable = false)
  private Boolean ativo;

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

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getRg() {
    return rg;
  }

  public void setRg(String rg) {
    this.rg = rg;
  }

  public String getPis() {
    return pis;
  }

  public void setPis(String pis) {
    this.pis = pis;
  }

  public LocalDate getDataNascimento() {
    return dataNascimento;
  }

  public void setDataNascimento(LocalDate dataNascimento) {
    this.dataNascimento = dataNascimento;
  }

  public String getNaturalidade() {
    return naturalidade;
  }

  public void setNaturalidade(String naturalidade) {
    this.naturalidade = naturalidade;
  }

  public String getEstadoCivil() {
    return estadoCivil;
  }

  public void setEstadoCivil(String estadoCivil) {
    this.estadoCivil = estadoCivil;
  }

  public String getNomeMae() {
    return nomeMae;
  }

  public void setNomeMae(String nomeMae) {
    this.nomeMae = nomeMae;
  }

  public String getNomeConjuge() {
    return nomeConjuge;
  }

  public void setNomeConjuge(String nomeConjuge) {
    this.nomeConjuge = nomeConjuge;
  }

  public String getVagaPretendida() {
    return vagaPretendida;
  }

  public void setVagaPretendida(String vagaPretendida) {
    this.vagaPretendida = vagaPretendida;
  }

  public LocalDate getDataPreenchimento() {
    return dataPreenchimento;
  }

  public void setDataPreenchimento(LocalDate dataPreenchimento) {
    this.dataPreenchimento = dataPreenchimento;
  }

  public Boolean getFilhosPossui() {
    return filhosPossui;
  }

  public void setFilhosPossui(Boolean filhosPossui) {
    this.filhosPossui = filhosPossui;
  }

  public String getFilhosJson() {
    return filhosJson;
  }

  public void setFilhosJson(String filhosJson) {
    this.filhosJson = filhosJson;
  }

  public Boolean getDeficienciaPossui() {
    return deficienciaPossui;
  }

  public void setDeficienciaPossui(Boolean deficienciaPossui) {
    this.deficienciaPossui = deficienciaPossui;
  }

  public String getDeficienciaTipo() {
    return deficienciaTipo;
  }

  public void setDeficienciaTipo(String deficienciaTipo) {
    this.deficienciaTipo = deficienciaTipo;
  }

  public String getDeficienciaDescricao() {
    return deficienciaDescricao;
  }

  public void setDeficienciaDescricao(String deficienciaDescricao) {
    this.deficienciaDescricao = deficienciaDescricao;
  }

  public String getEnderecoJson() {
    return enderecoJson;
  }

  public void setEnderecoJson(String enderecoJson) {
    this.enderecoJson = enderecoJson;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public String getWhatsapp() {
    return whatsapp;
  }

  public void setWhatsapp(String whatsapp) {
    this.whatsapp = whatsapp;
  }

  public String getAnexosJson() {
    return anexosJson;
  }

  public void setAnexosJson(String anexosJson) {
    this.anexosJson = anexosJson;
  }

  public Boolean getAtivo() {
    return ativo;
  }

  public void setAtivo(Boolean ativo) {
    this.ativo = ativo;
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
