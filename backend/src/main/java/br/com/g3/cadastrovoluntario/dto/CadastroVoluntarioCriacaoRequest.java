package br.com.g3.cadastrovoluntario.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public class CadastroVoluntarioCriacaoRequest {
  @JsonProperty("profissional_id")
  private Long profissionalId;

  @NotBlank
  @Size(max = 200)
  @JsonProperty("nome_completo")
  private String nomeCompleto;

  @NotBlank
  @Size(max = 20)
  @JsonProperty("cpf")
  private String cpf;

  @Size(max = 20)
  @JsonProperty("rg")
  private String rg;

  @JsonProperty("foto_3x4")
  private String foto3x4;

  @Size(max = 20)
  @JsonProperty("cep")
  private String cep;

  @Size(max = 200)
  @JsonProperty("logradouro")
  private String logradouro;

  @Size(max = 20)
  @JsonProperty("numero")
  private String numero;

  @Size(max = 150)
  @JsonProperty("complemento")
  private String complemento;

  @Size(max = 150)
  @JsonProperty("bairro")
  private String bairro;

  @Size(max = 200)
  @JsonProperty("ponto_referencia")
  private String pontoReferencia;

  @Size(max = 150)
  @JsonProperty("municipio")
  private String municipio;

  @Size(max = 60)
  @JsonProperty("zona")
  private String zona;

  @Size(max = 2)
  @JsonProperty("uf")
  private String uf;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonProperty("data_nascimento")
  private LocalDate dataNascimento;

  @Size(max = 60)
  @JsonProperty("genero")
  private String genero;

  @Size(max = 120)
  @JsonProperty("profissao")
  private String profissao;

  @JsonProperty("motivacao")
  private String motivacao;

  @Size(max = 30)
  @JsonProperty("telefone")
  private String telefone;

  @NotBlank
  @Size(max = 150)
  @JsonProperty("email")
  private String email;

  @Size(max = 150)
  @JsonProperty("cidade")
  private String cidade;

  @Size(max = 2)
  @JsonProperty("estado")
  private String estado;

  @Size(max = 150)
  @JsonProperty("area_interesse")
  private String areaInteresse;

  @JsonProperty("habilidades")
  private String habilidades;

  @JsonProperty("idiomas")
  private String idiomas;

  @Size(max = 200)
  @JsonProperty("linkedin")
  private String linkedin;

  @Size(max = 60)
  @JsonProperty("status")
  private String status;

  @JsonProperty("disponibilidade_dias")
  private List<String> disponibilidadeDias;

  @JsonProperty("disponibilidade_periodos")
  private List<String> disponibilidadePeriodos;

  @Size(max = 50)
  @JsonProperty("carga_horaria_semanal")
  private String cargaHorariaSemanal;

  @JsonProperty("presencial")
  private Boolean presencial;

  @JsonProperty("remoto")
  private Boolean remoto;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonProperty("inicio_previsto")
  private LocalDate inicioPrevisto;

  @JsonProperty("observacoes")
  private String observacoes;

  @JsonProperty("documento_identificacao")
  private String documentoIdentificacao;

  @JsonProperty("comprovante_endereco")
  private String comprovanteEndereco;

  @NotNull
  @JsonProperty("aceite_voluntariado")
  private Boolean aceiteVoluntariado;

  @NotNull
  @JsonProperty("aceite_imagem")
  private Boolean aceiteImagem;

  @JsonProperty("assinatura_digital")
  private String assinaturaDigital;

  public Long getProfissionalId() {
    return profissionalId;
  }

  public void setProfissionalId(Long profissionalId) {
    this.profissionalId = profissionalId;
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

  public String getCep() {
    return cep;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

  public String getLogradouro() {
    return logradouro;
  }

  public void setLogradouro(String logradouro) {
    this.logradouro = logradouro;
  }

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public String getComplemento() {
    return complemento;
  }

  public void setComplemento(String complemento) {
    this.complemento = complemento;
  }

  public String getBairro() {
    return bairro;
  }

  public void setBairro(String bairro) {
    this.bairro = bairro;
  }

  public String getPontoReferencia() {
    return pontoReferencia;
  }

  public void setPontoReferencia(String pontoReferencia) {
    this.pontoReferencia = pontoReferencia;
  }

  public String getMunicipio() {
    return municipio;
  }

  public void setMunicipio(String municipio) {
    this.municipio = municipio;
  }

  public String getZona() {
    return zona;
  }

  public void setZona(String zona) {
    this.zona = zona;
  }

  public String getUf() {
    return uf;
  }

  public void setUf(String uf) {
    this.uf = uf;
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

  public List<String> getDisponibilidadeDias() {
    return disponibilidadeDias;
  }

  public void setDisponibilidadeDias(List<String> disponibilidadeDias) {
    this.disponibilidadeDias = disponibilidadeDias;
  }

  public List<String> getDisponibilidadePeriodos() {
    return disponibilidadePeriodos;
  }

  public void setDisponibilidadePeriodos(List<String> disponibilidadePeriodos) {
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

  public Boolean getAceiteVoluntariado() {
    return aceiteVoluntariado;
  }

  public void setAceiteVoluntariado(Boolean aceiteVoluntariado) {
    this.aceiteVoluntariado = aceiteVoluntariado;
  }

  public Boolean getAceiteImagem() {
    return aceiteImagem;
  }

  public void setAceiteImagem(Boolean aceiteImagem) {
    this.aceiteImagem = aceiteImagem;
  }

  public String getAssinaturaDigital() {
    return assinaturaDigital;
  }

  public void setAssinaturaDigital(String assinaturaDigital) {
    this.assinaturaDigital = assinaturaDigital;
  }
}
