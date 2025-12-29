package br.com.g3.cadastrovoluntario.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CadastroVoluntarioResponse {
  @JsonProperty("id_voluntario")
  private final Long id;

  @JsonProperty("profissional_id")
  private final Long profissionalId;

  @JsonProperty("nome_completo")
  private final String nomeCompleto;

  @JsonProperty("cpf")
  private final String cpf;

  @JsonProperty("rg")
  private final String rg;

  @JsonProperty("foto_3x4")
  private final String foto3x4;

  @JsonProperty("cep")
  private final String cep;

  @JsonProperty("logradouro")
  private final String logradouro;

  @JsonProperty("numero")
  private final String numero;

  @JsonProperty("complemento")
  private final String complemento;

  @JsonProperty("bairro")
  private final String bairro;

  @JsonProperty("ponto_referencia")
  private final String pontoReferencia;

  @JsonProperty("municipio")
  private final String municipio;

  @JsonProperty("zona")
  private final String zona;

  @JsonProperty("uf")
  private final String uf;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonProperty("data_nascimento")
  private final LocalDate dataNascimento;

  @JsonProperty("genero")
  private final String genero;

  @JsonProperty("profissao")
  private final String profissao;

  @JsonProperty("motivacao")
  private final String motivacao;

  @JsonProperty("telefone")
  private final String telefone;

  @JsonProperty("email")
  private final String email;

  @JsonProperty("cidade")
  private final String cidade;

  @JsonProperty("estado")
  private final String estado;

  @JsonProperty("area_interesse")
  private final String areaInteresse;

  @JsonProperty("habilidades")
  private final String habilidades;

  @JsonProperty("idiomas")
  private final String idiomas;

  @JsonProperty("linkedin")
  private final String linkedin;

  @JsonProperty("status")
  private final String status;

  @JsonProperty("disponibilidade_dias")
  private final List<String> disponibilidadeDias;

  @JsonProperty("disponibilidade_periodos")
  private final List<String> disponibilidadePeriodos;

  @JsonProperty("carga_horaria_semanal")
  private final String cargaHorariaSemanal;

  @JsonProperty("presencial")
  private final Boolean presencial;

  @JsonProperty("remoto")
  private final Boolean remoto;

  @JsonProperty("inicio_previsto")
  private final LocalDate inicioPrevisto;

  @JsonProperty("observacoes")
  private final String observacoes;

  @JsonProperty("documento_identificacao")
  private final String documentoIdentificacao;

  @JsonProperty("comprovante_endereco")
  private final String comprovanteEndereco;

  @JsonProperty("aceite_voluntariado")
  private final Boolean aceiteVoluntariado;

  @JsonProperty("aceite_imagem")
  private final Boolean aceiteImagem;

  @JsonProperty("assinatura_digital")
  private final String assinaturaDigital;

  @JsonProperty("data_cadastro")
  private final LocalDateTime dataCadastro;

  @JsonProperty("data_atualizacao")
  private final LocalDateTime dataAtualizacao;

  public CadastroVoluntarioResponse(
      Long id,
      Long profissionalId,
      String nomeCompleto,
      String cpf,
      String rg,
      String foto3x4,
      String cep,
      String logradouro,
      String numero,
      String complemento,
      String bairro,
      String pontoReferencia,
      String municipio,
      String zona,
      String uf,
      LocalDate dataNascimento,
      String genero,
      String profissao,
      String motivacao,
      String telefone,
      String email,
      String cidade,
      String estado,
      String areaInteresse,
      String habilidades,
      String idiomas,
      String linkedin,
      String status,
      List<String> disponibilidadeDias,
      List<String> disponibilidadePeriodos,
      String cargaHorariaSemanal,
      Boolean presencial,
      Boolean remoto,
      LocalDate inicioPrevisto,
      String observacoes,
      String documentoIdentificacao,
      String comprovanteEndereco,
      Boolean aceiteVoluntariado,
      Boolean aceiteImagem,
      String assinaturaDigital,
      LocalDateTime dataCadastro,
      LocalDateTime dataAtualizacao) {
    this.id = id;
    this.profissionalId = profissionalId;
    this.nomeCompleto = nomeCompleto;
    this.cpf = cpf;
    this.rg = rg;
    this.foto3x4 = foto3x4;
    this.cep = cep;
    this.logradouro = logradouro;
    this.numero = numero;
    this.complemento = complemento;
    this.bairro = bairro;
    this.pontoReferencia = pontoReferencia;
    this.municipio = municipio;
    this.zona = zona;
    this.uf = uf;
    this.dataNascimento = dataNascimento;
    this.genero = genero;
    this.profissao = profissao;
    this.motivacao = motivacao;
    this.telefone = telefone;
    this.email = email;
    this.cidade = cidade;
    this.estado = estado;
    this.areaInteresse = areaInteresse;
    this.habilidades = habilidades;
    this.idiomas = idiomas;
    this.linkedin = linkedin;
    this.status = status;
    this.disponibilidadeDias = disponibilidadeDias;
    this.disponibilidadePeriodos = disponibilidadePeriodos;
    this.cargaHorariaSemanal = cargaHorariaSemanal;
    this.presencial = presencial;
    this.remoto = remoto;
    this.inicioPrevisto = inicioPrevisto;
    this.observacoes = observacoes;
    this.documentoIdentificacao = documentoIdentificacao;
    this.comprovanteEndereco = comprovanteEndereco;
    this.aceiteVoluntariado = aceiteVoluntariado;
    this.aceiteImagem = aceiteImagem;
    this.assinaturaDigital = assinaturaDigital;
    this.dataCadastro = dataCadastro;
    this.dataAtualizacao = dataAtualizacao;
  }

  public Long getId() {
    return id;
  }

  public Long getProfissionalId() {
    return profissionalId;
  }

  public String getNomeCompleto() {
    return nomeCompleto;
  }

  public String getCpf() {
    return cpf;
  }

  public String getRg() {
    return rg;
  }

  public String getFoto3x4() {
    return foto3x4;
  }

  public String getCep() {
    return cep;
  }

  public String getLogradouro() {
    return logradouro;
  }

  public String getNumero() {
    return numero;
  }

  public String getComplemento() {
    return complemento;
  }

  public String getBairro() {
    return bairro;
  }

  public String getPontoReferencia() {
    return pontoReferencia;
  }

  public String getMunicipio() {
    return municipio;
  }

  public String getZona() {
    return zona;
  }

  public String getUf() {
    return uf;
  }

  public LocalDate getDataNascimento() {
    return dataNascimento;
  }

  public String getGenero() {
    return genero;
  }

  public String getProfissao() {
    return profissao;
  }

  public String getMotivacao() {
    return motivacao;
  }

  public String getTelefone() {
    return telefone;
  }

  public String getEmail() {
    return email;
  }

  public String getCidade() {
    return cidade;
  }

  public String getEstado() {
    return estado;
  }

  public String getAreaInteresse() {
    return areaInteresse;
  }

  public String getHabilidades() {
    return habilidades;
  }

  public String getIdiomas() {
    return idiomas;
  }

  public String getLinkedin() {
    return linkedin;
  }

  public String getStatus() {
    return status;
  }

  public List<String> getDisponibilidadeDias() {
    return disponibilidadeDias;
  }

  public List<String> getDisponibilidadePeriodos() {
    return disponibilidadePeriodos;
  }

  public String getCargaHorariaSemanal() {
    return cargaHorariaSemanal;
  }

  public Boolean getPresencial() {
    return presencial;
  }

  public Boolean getRemoto() {
    return remoto;
  }

  public LocalDate getInicioPrevisto() {
    return inicioPrevisto;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public String getDocumentoIdentificacao() {
    return documentoIdentificacao;
  }

  public String getComprovanteEndereco() {
    return comprovanteEndereco;
  }

  public Boolean getAceiteVoluntariado() {
    return aceiteVoluntariado;
  }

  public Boolean getAceiteImagem() {
    return aceiteImagem;
  }

  public String getAssinaturaDigital() {
    return assinaturaDigital;
  }

  public LocalDateTime getDataCadastro() {
    return dataCadastro;
  }

  public LocalDateTime getDataAtualizacao() {
    return dataAtualizacao;
  }
}
