package br.com.g3.cadastrovoluntario.domain;

import br.com.g3.cadastroprofissionais.domain.CadastroProfissional;
import br.com.g3.unidadeassistencial.domain.Endereco;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cadastro_voluntario")
public class CadastroVoluntario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "profissional_id")
  private CadastroProfissional profissional;

  @Column(name = "nome_completo", length = 200, nullable = false)
  private String nomeCompleto;

  @Column(name = "cpf", length = 20, nullable = false)
  private String cpf;

  @Column(name = "rg", length = 20)
  private String rg;

  @Column(name = "foto_3x4")
  private String foto3x4;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "endereco_id")
  private Endereco endereco;

  @Column(name = "data_nascimento")
  private LocalDate dataNascimento;

  @Column(name = "genero", length = 60)
  private String genero;

  @Column(name = "profissao", length = 120)
  private String profissao;

  @Column(name = "motivacao")
  private String motivacao;

  @Column(name = "telefone", length = 30)
  private String telefone;

  @Column(name = "email", length = 150, nullable = false)
  private String email;

  @Column(name = "cidade", length = 150)
  private String cidade;

  @Column(name = "estado", length = 2)
  private String estado;

  @Column(name = "area_interesse", length = 150)
  private String areaInteresse;

  @Column(name = "habilidades")
  private String habilidades;

  @Column(name = "idiomas")
  private String idiomas;

  @Column(name = "linkedin", length = 200)
  private String linkedin;

  @Column(name = "status", length = 60, nullable = false)
  private String status;

  @Column(name = "disponibilidade_dias")
  private String disponibilidadeDias;

  @Column(name = "disponibilidade_periodos")
  private String disponibilidadePeriodos;

  @Column(name = "carga_horaria_semanal", length = 50)
  private String cargaHorariaSemanal;

  @Column(name = "presencial")
  private Boolean presencial;

  @Column(name = "remoto")
  private Boolean remoto;

  @Column(name = "inicio_previsto")
  private LocalDate inicioPrevisto;

  @Column(name = "observacoes")
  private String observacoes;

  @Column(name = "documento_identificacao")
  private String documentoIdentificacao;

  @Column(name = "comprovante_endereco")
  private String comprovanteEndereco;

  @Column(name = "aceite_voluntariado", nullable = false)
  private boolean aceiteVoluntariado;

  @Column(name = "aceite_imagem", nullable = false)
  private boolean aceiteImagem;

  @Column(name = "assinatura_digital")
  private String assinaturaDigital;

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

  public CadastroProfissional getProfissional() {
    return profissional;
  }

  public void setProfissional(CadastroProfissional profissional) {
    this.profissional = profissional;
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

  public String getFoto3x4() {
    return foto3x4;
  }

  public void setFoto3x4(String foto3x4) {
    this.foto3x4 = foto3x4;
  }

  public Endereco getEndereco() {
    return endereco;
  }

  public void setEndereco(Endereco endereco) {
    this.endereco = endereco;
  }

  public LocalDate getDataNascimento() {
    return dataNascimento;
  }

  public void setDataNascimento(LocalDate dataNascimento) {
    this.dataNascimento = dataNascimento;
  }

  public String getGenero() {
    return genero;
  }

  public void setGenero(String genero) {
    this.genero = genero;
  }

  public String getProfissao() {
    return profissao;
  }

  public void setProfissao(String profissao) {
    this.profissao = profissao;
  }

  public String getMotivacao() {
    return motivacao;
  }

  public void setMotivacao(String motivacao) {
    this.motivacao = motivacao;
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

  public String getCidade() {
    return cidade;
  }

  public void setCidade(String cidade) {
    this.cidade = cidade;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public String getAreaInteresse() {
    return areaInteresse;
  }

  public void setAreaInteresse(String areaInteresse) {
    this.areaInteresse = areaInteresse;
  }

  public String getHabilidades() {
    return habilidades;
  }

  public void setHabilidades(String habilidades) {
    this.habilidades = habilidades;
  }

  public String getIdiomas() {
    return idiomas;
  }

  public void setIdiomas(String idiomas) {
    this.idiomas = idiomas;
  }

  public String getLinkedin() {
    return linkedin;
  }

  public void setLinkedin(String linkedin) {
    this.linkedin = linkedin;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getDisponibilidadeDias() {
    return disponibilidadeDias;
  }

  public void setDisponibilidadeDias(String disponibilidadeDias) {
    this.disponibilidadeDias = disponibilidadeDias;
  }

  public String getDisponibilidadePeriodos() {
    return disponibilidadePeriodos;
  }

  public void setDisponibilidadePeriodos(String disponibilidadePeriodos) {
    this.disponibilidadePeriodos = disponibilidadePeriodos;
  }

  public String getCargaHorariaSemanal() {
    return cargaHorariaSemanal;
  }

  public void setCargaHorariaSemanal(String cargaHorariaSemanal) {
    this.cargaHorariaSemanal = cargaHorariaSemanal;
  }

  public Boolean getPresencial() {
    return presencial;
  }

  public void setPresencial(Boolean presencial) {
    this.presencial = presencial;
  }

  public Boolean getRemoto() {
    return remoto;
  }

  public void setRemoto(Boolean remoto) {
    this.remoto = remoto;
  }

  public LocalDate getInicioPrevisto() {
    return inicioPrevisto;
  }

  public void setInicioPrevisto(LocalDate inicioPrevisto) {
    this.inicioPrevisto = inicioPrevisto;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public String getDocumentoIdentificacao() {
    return documentoIdentificacao;
  }

  public void setDocumentoIdentificacao(String documentoIdentificacao) {
    this.documentoIdentificacao = documentoIdentificacao;
  }

  public String getComprovanteEndereco() {
    return comprovanteEndereco;
  }

  public void setComprovanteEndereco(String comprovanteEndereco) {
    this.comprovanteEndereco = comprovanteEndereco;
  }

  public boolean isAceiteVoluntariado() {
    return aceiteVoluntariado;
  }

  public void setAceiteVoluntariado(boolean aceiteVoluntariado) {
    this.aceiteVoluntariado = aceiteVoluntariado;
  }

  public boolean isAceiteImagem() {
    return aceiteImagem;
  }

  public void setAceiteImagem(boolean aceiteImagem) {
    this.aceiteImagem = aceiteImagem;
  }

  public String getAssinaturaDigital() {
    return assinaturaDigital;
  }

  public void setAssinaturaDigital(String assinaturaDigital) {
    this.assinaturaDigital = assinaturaDigital;
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
