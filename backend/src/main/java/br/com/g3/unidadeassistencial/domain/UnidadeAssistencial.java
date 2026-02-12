package br.com.g3.unidadeassistencial.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "unidade_assistencial")
public class UnidadeAssistencial {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "nome_fantasia", length = 200)
  private String nomeFantasia;

  @Column(name = "razao_social", length = 200)
  private String razaoSocial;

  @Column(name = "cnpj", length = 20)
  private String cnpj;

  @Column(name = "email", length = 150)
  private String email;

  @Column(name = "site", length = 200)
  private String site;

  @Column(name = "telefone", length = 30)
  private String telefone;

  @Column(name = "horario_funcionamento", length = 120)
  private String horarioFuncionamento;

  @Column(name = "observacoes")
  private String observacoes;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "endereco_id")
  private Endereco endereco;

  @OneToOne(mappedBy = "unidadeAssistencial", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private ImagemUnidade imagemUnidade;

  @OneToMany(mappedBy = "unidadeAssistencial", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private List<DiretoriaUnidade> diretoria = new ArrayList<>();

  @Column(name = "unidade_principal", nullable = false)
  private boolean unidadePrincipal;

  @Column(name = "raio_ponto_metros", nullable = false)
  private Integer raioPontoMetros;

  @Column(name = "accuracy_max_ponto_metros", nullable = false)
  private Integer accuracyMaxPontoMetros;

  @Column(name = "ip_validacao_ponto", length = 45)
  private String ipValidacaoPonto;

  @Column(name = "ping_timeout_ms", nullable = false)
  private Integer pingTimeoutMs;
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

  public String getNomeFantasia() {
    return nomeFantasia;
  }

  public void setNomeFantasia(String nomeFantasia) {
    this.nomeFantasia = nomeFantasia;
  }

  public String getRazaoSocial() {
    return razaoSocial;
  }

  public void setRazaoSocial(String razaoSocial) {
    this.razaoSocial = razaoSocial;
  }

  public String getCnpj() {
    return cnpj;
  }

  public void setCnpj(String cnpj) {
    this.cnpj = cnpj;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getSite() {
    return site;
  }

  public void setSite(String site) {
    this.site = site;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public String getHorarioFuncionamento() {
    return horarioFuncionamento;
  }

  public void setHorarioFuncionamento(String horarioFuncionamento) {
    this.horarioFuncionamento = horarioFuncionamento;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public Endereco getEndereco() {
    return endereco;
  }

  public void setEndereco(Endereco endereco) {
    this.endereco = endereco;
  }

  public ImagemUnidade getImagemUnidade() {
    return imagemUnidade;
  }

  public void setImagemUnidade(ImagemUnidade imagemUnidade) {
    this.imagemUnidade = imagemUnidade;
  }

  public List<DiretoriaUnidade> getDiretoria() {
    return diretoria;
  }

  public void setDiretoria(List<DiretoriaUnidade> diretoria) {
    this.diretoria = diretoria;
  }

  public boolean isUnidadePrincipal() {
    return unidadePrincipal;
  }

  public void setUnidadePrincipal(boolean unidadePrincipal) {
    this.unidadePrincipal = unidadePrincipal;
  }

  public Integer getRaioPontoMetros() {
    return raioPontoMetros;
  }

  public void setRaioPontoMetros(Integer raioPontoMetros) {
    this.raioPontoMetros = raioPontoMetros;
  }

  public Integer getAccuracyMaxPontoMetros() {
    return accuracyMaxPontoMetros;
  }

  public void setAccuracyMaxPontoMetros(Integer accuracyMaxPontoMetros) {
    this.accuracyMaxPontoMetros = accuracyMaxPontoMetros;
  }

  public String getIpValidacaoPonto() {
    return ipValidacaoPonto;
  }

  public void setIpValidacaoPonto(String ipValidacaoPonto) {
    this.ipValidacaoPonto = ipValidacaoPonto;
  }

  public Integer getPingTimeoutMs() {
    return pingTimeoutMs;
  }

  public void setPingTimeoutMs(Integer pingTimeoutMs) {
    this.pingTimeoutMs = pingTimeoutMs;
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
