package br.com.g3.cadastrobeneficiario.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CadastroBeneficiarioResponse {
  @JsonProperty("id_beneficiario")
  private final Long id;

  @JsonProperty("codigo")
  private final String codigo;

  @JsonProperty("nome_completo")
  private final String nomeCompleto;

  @JsonProperty("nome_social")
  private final String nomeSocial;

  @JsonProperty("apelido")
  private final String apelido;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonProperty("data_nascimento")
  private final LocalDate dataNascimento;

  @JsonProperty("foto_3x4")
  private final String foto3x4;

  @JsonProperty("sexo_biologico")
  private final String sexoBiologico;

  @JsonProperty("identidade_genero")
  private final String identidadeGenero;

  @JsonProperty("cor_raca")
  private final String corRaca;

  @JsonProperty("estado_civil")
  private final String estadoCivil;

  @JsonProperty("nacionalidade")
  private final String nacionalidade;

  @JsonProperty("naturalidade_cidade")
  private final String naturalidadeCidade;

  @JsonProperty("naturalidade_uf")
  private final String naturalidadeUf;

  @JsonProperty("nome_mae")
  private final String nomeMae;

  @JsonProperty("nome_pai")
  private final String nomePai;

  @JsonProperty("status")
  private final String status;

  @JsonProperty("opta_receber_cesta_basica")
  private final Boolean optaReceberCestaBasica;

  @JsonProperty("apto_receber_cesta_basica")
  private final Boolean aptoReceberCestaBasica;

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

  @JsonProperty("subzona")
  private final String subzona;

  @JsonProperty("uf")
  private final String uf;

  @JsonProperty("latitude")
  private final String latitude;

  @JsonProperty("longitude")
  private final String longitude;

  @JsonProperty("telefone_principal")
  private final String telefonePrincipal;

  @JsonProperty("telefone_principal_whatsapp")
  private final Boolean telefonePrincipalWhatsapp;

  @JsonProperty("telefone_secundario")
  private final String telefoneSecundario;

  @JsonProperty("telefone_recado_nome")
  private final String telefoneRecadoNome;

  @JsonProperty("telefone_recado_numero")
  private final String telefoneRecadoNumero;

  @JsonProperty("email")
  private final String email;

  @JsonProperty("permite_contato_tel")
  private final Boolean permiteContatoTel;

  @JsonProperty("permite_contato_whatsapp")
  private final Boolean permiteContatoWhatsapp;

  @JsonProperty("permite_contato_sms")
  private final Boolean permiteContatoSms;

  @JsonProperty("permite_contato_email")
  private final Boolean permiteContatoEmail;

  @JsonProperty("horario_preferencial_contato")
  private final String horarioPreferencialContato;

  @JsonProperty("cpf")
  private final String cpf;

  @JsonProperty("rg_numero")
  private final String rgNumero;

  @JsonProperty("rg_orgao_emissor")
  private final String rgOrgaoEmissor;

  @JsonProperty("rg_uf")
  private final String rgUf;

  @JsonProperty("rg_data_emissao")
  private final LocalDate rgDataEmissao;

  @JsonProperty("nis")
  private final String nis;

  @JsonProperty("certidao_tipo")
  private final String certidaoTipo;

  @JsonProperty("certidao_livro")
  private final String certidaoLivro;

  @JsonProperty("certidao_folha")
  private final String certidaoFolha;

  @JsonProperty("certidao_termo")
  private final String certidaoTermo;

  @JsonProperty("certidao_cartorio")
  private final String certidaoCartorio;

  @JsonProperty("certidao_municipio")
  private final String certidaoMunicipio;

  @JsonProperty("certidao_uf")
  private final String certidaoUf;

  @JsonProperty("titulo_eleitor")
  private final String tituloEleitor;

  @JsonProperty("cnh")
  private final String cnh;

  @JsonProperty("cartao_sus")
  private final String cartaoSus;

  @JsonProperty("mora_com_familia")
  private final Boolean moraComFamilia;

  @JsonProperty("responsavel_legal")
  private final Boolean responsavelLegal;

  @JsonProperty("vinculo_familiar")
  private final String vinculoFamiliar;

  @JsonProperty("situacao_vulnerabilidade")
  private final String situacaoVulnerabilidade;

  @JsonProperty("composicao_familiar")
  private final String composicaoFamiliar;

  @JsonProperty("criancas_adolescentes")
  private final Integer criancasAdolescentes;

  @JsonProperty("idosos")
  private final Integer idosos;

  @JsonProperty("acompanhamento_cras")
  private final Boolean acompanhamentoCras;

  @JsonProperty("acompanhamento_saude")
  private final Boolean acompanhamentoSaude;

  @JsonProperty("participa_comunidade")
  private final String participaComunidade;

  @JsonProperty("rede_apoio")
  private final String redeApoio;

  @JsonProperty("sabe_ler_escrever")
  private final Boolean sabeLerEscrever;

  @JsonProperty("nivel_escolaridade")
  private final String nivelEscolaridade;

  @JsonProperty("estuda_atualmente")
  private final Boolean estudaAtualmente;

  @JsonProperty("ocupacao")
  private final String ocupacao;

  @JsonProperty("situacao_trabalho")
  private final String situacaoTrabalho;

  @JsonProperty("local_trabalho")
  private final String localTrabalho;

  @JsonProperty("renda_mensal")
  private final String rendaMensal;

  @JsonProperty("fonte_renda")
  private final String fonteRenda;

  @JsonProperty("possui_deficiencia")
  private final Boolean possuiDeficiencia;

  @JsonProperty("tipo_deficiencia")
  private final String tipoDeficiencia;

  @JsonProperty("cid_principal")
  private final String cidPrincipal;

  @JsonProperty("usa_medicacao_continua")
  private final Boolean usaMedicacaoContinua;

  @JsonProperty("descricao_medicacao")
  private final String descricaoMedicacao;

  @JsonProperty("servico_saude_referencia")
  private final String servicoSaudeReferencia;

  @JsonProperty("recebe_beneficio")
  private final Boolean recebeBeneficio;

  @JsonProperty("beneficios_descricao")
  private final String beneficiosDescricao;

  @JsonProperty("valor_total_beneficios")
  private final String valorTotalBeneficios;

  @JsonProperty("beneficios_recebidos")
  private final List<String> beneficiosRecebidos;

  @JsonProperty("aceite_lgpd")
  private final Boolean aceiteLgpd;

  @JsonProperty("data_aceite_lgpd")
  private final LocalDate dataAceiteLgpd;

  @JsonProperty("observacoes")
  private final String observacoes;

  @JsonProperty("documentos_obrigatorios")
  private final List<DocumentoBeneficiarioResponse> documentosObrigatorios;

  @JsonProperty("data_cadastro")
  private final LocalDateTime dataCadastro;

  @JsonProperty("data_atualizacao")
  private final LocalDateTime dataAtualizacao;

  public CadastroBeneficiarioResponse(
      Long id,
      String codigo,
      String nomeCompleto,
      String nomeSocial,
      String apelido,
      LocalDate dataNascimento,
      String foto3x4,
      String sexoBiologico,
      String identidadeGenero,
      String corRaca,
      String estadoCivil,
      String nacionalidade,
      String naturalidadeCidade,
      String naturalidadeUf,
      String nomeMae,
      String nomePai,
      String status,
      Boolean optaReceberCestaBasica,
      Boolean aptoReceberCestaBasica,
      String cep,
      String logradouro,
      String numero,
      String complemento,
      String bairro,
      String pontoReferencia,
      String municipio,
      String zona,
      String subzona,
      String uf,
      String latitude,
      String longitude,
      String telefonePrincipal,
      Boolean telefonePrincipalWhatsapp,
      String telefoneSecundario,
      String telefoneRecadoNome,
      String telefoneRecadoNumero,
      String email,
      Boolean permiteContatoTel,
      Boolean permiteContatoWhatsapp,
      Boolean permiteContatoSms,
      Boolean permiteContatoEmail,
      String horarioPreferencialContato,
      String cpf,
      String rgNumero,
      String rgOrgaoEmissor,
      String rgUf,
      LocalDate rgDataEmissao,
      String nis,
      String certidaoTipo,
      String certidaoLivro,
      String certidaoFolha,
      String certidaoTermo,
      String certidaoCartorio,
      String certidaoMunicipio,
      String certidaoUf,
      String tituloEleitor,
      String cnh,
      String cartaoSus,
      Boolean moraComFamilia,
      Boolean responsavelLegal,
      String vinculoFamiliar,
      String situacaoVulnerabilidade,
      String composicaoFamiliar,
      Integer criancasAdolescentes,
      Integer idosos,
      Boolean acompanhamentoCras,
      Boolean acompanhamentoSaude,
      String participaComunidade,
      String redeApoio,
      Boolean sabeLerEscrever,
      String nivelEscolaridade,
      Boolean estudaAtualmente,
      String ocupacao,
      String situacaoTrabalho,
      String localTrabalho,
      String rendaMensal,
      String fonteRenda,
      Boolean possuiDeficiencia,
      String tipoDeficiencia,
      String cidPrincipal,
      Boolean usaMedicacaoContinua,
      String descricaoMedicacao,
      String servicoSaudeReferencia,
      Boolean recebeBeneficio,
      String beneficiosDescricao,
      String valorTotalBeneficios,
      List<String> beneficiosRecebidos,
      Boolean aceiteLgpd,
      LocalDate dataAceiteLgpd,
      String observacoes,
      List<DocumentoBeneficiarioResponse> documentosObrigatorios,
      LocalDateTime dataCadastro,
      LocalDateTime dataAtualizacao) {
    this.id = id;
    this.codigo = codigo;
    this.nomeCompleto = nomeCompleto;
    this.nomeSocial = nomeSocial;
    this.apelido = apelido;
    this.dataNascimento = dataNascimento;
    this.foto3x4 = foto3x4;
    this.sexoBiologico = sexoBiologico;
    this.identidadeGenero = identidadeGenero;
    this.corRaca = corRaca;
    this.estadoCivil = estadoCivil;
    this.nacionalidade = nacionalidade;
    this.naturalidadeCidade = naturalidadeCidade;
    this.naturalidadeUf = naturalidadeUf;
    this.nomeMae = nomeMae;
    this.nomePai = nomePai;
    this.status = status;
    this.optaReceberCestaBasica = optaReceberCestaBasica;
    this.aptoReceberCestaBasica = aptoReceberCestaBasica;
    this.cep = cep;
    this.logradouro = logradouro;
    this.numero = numero;
    this.complemento = complemento;
    this.bairro = bairro;
    this.pontoReferencia = pontoReferencia;
    this.municipio = municipio;
    this.zona = zona;
    this.subzona = subzona;
    this.uf = uf;
    this.latitude = latitude;
    this.longitude = longitude;
    this.telefonePrincipal = telefonePrincipal;
    this.telefonePrincipalWhatsapp = telefonePrincipalWhatsapp;
    this.telefoneSecundario = telefoneSecundario;
    this.telefoneRecadoNome = telefoneRecadoNome;
    this.telefoneRecadoNumero = telefoneRecadoNumero;
    this.email = email;
    this.permiteContatoTel = permiteContatoTel;
    this.permiteContatoWhatsapp = permiteContatoWhatsapp;
    this.permiteContatoSms = permiteContatoSms;
    this.permiteContatoEmail = permiteContatoEmail;
    this.horarioPreferencialContato = horarioPreferencialContato;
    this.cpf = cpf;
    this.rgNumero = rgNumero;
    this.rgOrgaoEmissor = rgOrgaoEmissor;
    this.rgUf = rgUf;
    this.rgDataEmissao = rgDataEmissao;
    this.nis = nis;
    this.certidaoTipo = certidaoTipo;
    this.certidaoLivro = certidaoLivro;
    this.certidaoFolha = certidaoFolha;
    this.certidaoTermo = certidaoTermo;
    this.certidaoCartorio = certidaoCartorio;
    this.certidaoMunicipio = certidaoMunicipio;
    this.certidaoUf = certidaoUf;
    this.tituloEleitor = tituloEleitor;
    this.cnh = cnh;
    this.cartaoSus = cartaoSus;
    this.moraComFamilia = moraComFamilia;
    this.responsavelLegal = responsavelLegal;
    this.vinculoFamiliar = vinculoFamiliar;
    this.situacaoVulnerabilidade = situacaoVulnerabilidade;
    this.composicaoFamiliar = composicaoFamiliar;
    this.criancasAdolescentes = criancasAdolescentes;
    this.idosos = idosos;
    this.acompanhamentoCras = acompanhamentoCras;
    this.acompanhamentoSaude = acompanhamentoSaude;
    this.participaComunidade = participaComunidade;
    this.redeApoio = redeApoio;
    this.sabeLerEscrever = sabeLerEscrever;
    this.nivelEscolaridade = nivelEscolaridade;
    this.estudaAtualmente = estudaAtualmente;
    this.ocupacao = ocupacao;
    this.situacaoTrabalho = situacaoTrabalho;
    this.localTrabalho = localTrabalho;
    this.rendaMensal = rendaMensal;
    this.fonteRenda = fonteRenda;
    this.possuiDeficiencia = possuiDeficiencia;
    this.tipoDeficiencia = tipoDeficiencia;
    this.cidPrincipal = cidPrincipal;
    this.usaMedicacaoContinua = usaMedicacaoContinua;
    this.descricaoMedicacao = descricaoMedicacao;
    this.servicoSaudeReferencia = servicoSaudeReferencia;
    this.recebeBeneficio = recebeBeneficio;
    this.beneficiosDescricao = beneficiosDescricao;
    this.valorTotalBeneficios = valorTotalBeneficios;
    this.beneficiosRecebidos = beneficiosRecebidos;
    this.aceiteLgpd = aceiteLgpd;
    this.dataAceiteLgpd = dataAceiteLgpd;
    this.observacoes = observacoes;
    this.documentosObrigatorios = documentosObrigatorios;
    this.dataCadastro = dataCadastro;
    this.dataAtualizacao = dataAtualizacao;
  }

  public Long getId() {
    return id;
  }

  public String getCodigo() {
    return codigo;
  }

  public String getNomeCompleto() {
    return nomeCompleto;
  }

  public String getNomeSocial() {
    return nomeSocial;
  }

  public String getApelido() {
    return apelido;
  }

  public LocalDate getDataNascimento() {
    return dataNascimento;
  }

  public String getFoto3x4() {
    return foto3x4;
  }

  public String getSexoBiologico() {
    return sexoBiologico;
  }

  public String getIdentidadeGenero() {
    return identidadeGenero;
  }

  public String getCorRaca() {
    return corRaca;
  }

  public String getEstadoCivil() {
    return estadoCivil;
  }

  public String getNacionalidade() {
    return nacionalidade;
  }

  public String getNaturalidadeCidade() {
    return naturalidadeCidade;
  }

  public String getNaturalidadeUf() {
    return naturalidadeUf;
  }

  public String getNomeMae() {
    return nomeMae;
  }

  public String getNomePai() {
    return nomePai;
  }

  public String getStatus() {    
    return status;
  }

  public Boolean getOptaReceberCestaBasica() {
    return optaReceberCestaBasica;
  }

  public Boolean getAptoReceberCestaBasica() {
    return aptoReceberCestaBasica;
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

  public String getSubzona() {
    return subzona;
  }

  public String getUf() {
    return uf;
  }

  public String getLatitude() {
    return latitude;
  }

  public String getLongitude() {
    return longitude;
  }

  public String getTelefonePrincipal() {
    return telefonePrincipal;
  }

  public Boolean getTelefonePrincipalWhatsapp() {
    return telefonePrincipalWhatsapp;
  }

  public String getTelefoneSecundario() {
    return telefoneSecundario;
  }

  public String getTelefoneRecadoNome() {
    return telefoneRecadoNome;
  }

  public String getTelefoneRecadoNumero() {
    return telefoneRecadoNumero;
  }

  public String getEmail() {
    return email;
  }

  public Boolean getPermiteContatoTel() {
    return permiteContatoTel;
  }

  public Boolean getPermiteContatoWhatsapp() {
    return permiteContatoWhatsapp;
  }

  public Boolean getPermiteContatoSms() {
    return permiteContatoSms;
  }

  public Boolean getPermiteContatoEmail() {
    return permiteContatoEmail;
  }

  public String getHorarioPreferencialContato() {
    return horarioPreferencialContato;
  }

  public String getCpf() {
    return cpf;
  }

  public String getRgNumero() {
    return rgNumero;
  }

  public String getRgOrgaoEmissor() {
    return rgOrgaoEmissor;
  }

  public String getRgUf() {
    return rgUf;
  }

  public LocalDate getRgDataEmissao() {
    return rgDataEmissao;
  }

  public String getNis() {
    return nis;
  }

  public String getCertidaoTipo() {
    return certidaoTipo;
  }

  public String getCertidaoLivro() {
    return certidaoLivro;
  }

  public String getCertidaoFolha() {
    return certidaoFolha;
  }

  public String getCertidaoTermo() {
    return certidaoTermo;
  }

  public String getCertidaoCartorio() {
    return certidaoCartorio;
  }

  public String getCertidaoMunicipio() {
    return certidaoMunicipio;
  }

  public String getCertidaoUf() {
    return certidaoUf;
  }

  public String getTituloEleitor() {
    return tituloEleitor;
  }

  public String getCnh() {
    return cnh;
  }

  public String getCartaoSus() {
    return cartaoSus;
  }

  public Boolean getMoraComFamilia() {
    return moraComFamilia;
  }

  public Boolean getResponsavelLegal() {
    return responsavelLegal;
  }

  public String getVinculoFamiliar() {
    return vinculoFamiliar;
  }

  public String getSituacaoVulnerabilidade() {
    return situacaoVulnerabilidade;
  }

  public String getComposicaoFamiliar() {
    return composicaoFamiliar;
  }

  public Integer getCriancasAdolescentes() {
    return criancasAdolescentes;
  }

  public Integer getIdosos() {
    return idosos;
  }

  public Boolean getAcompanhamentoCras() {
    return acompanhamentoCras;
  }

  public Boolean getAcompanhamentoSaude() {
    return acompanhamentoSaude;
  }

  public String getParticipaComunidade() {
    return participaComunidade;
  }

  public String getRedeApoio() {
    return redeApoio;
  }

  public Boolean getSabeLerEscrever() {
    return sabeLerEscrever;
  }

  public String getNivelEscolaridade() {
    return nivelEscolaridade;
  }

  public Boolean getEstudaAtualmente() {
    return estudaAtualmente;
  }

  public String getOcupacao() {
    return ocupacao;
  }

  public String getSituacaoTrabalho() {
    return situacaoTrabalho;
  }

  public String getLocalTrabalho() {
    return localTrabalho;
  }

  public String getRendaMensal() {
    return rendaMensal;
  }

  public String getFonteRenda() {
    return fonteRenda;
  }

  public Boolean getPossuiDeficiencia() {
    return possuiDeficiencia;
  }

  public String getTipoDeficiencia() {
    return tipoDeficiencia;
  }

  public String getCidPrincipal() {
    return cidPrincipal;
  }

  public Boolean getUsaMedicacaoContinua() {
    return usaMedicacaoContinua;
  }

  public String getDescricaoMedicacao() {
    return descricaoMedicacao;
  }

  public String getServicoSaudeReferencia() {
    return servicoSaudeReferencia;
  }

  public Boolean getRecebeBeneficio() {
    return recebeBeneficio;
  }

  public String getBeneficiosDescricao() {
    return beneficiosDescricao;
  }

  public String getValorTotalBeneficios() {
    return valorTotalBeneficios;
  }

  public List<String> getBeneficiosRecebidos() {
    return beneficiosRecebidos;
  }

  public Boolean getAceiteLgpd() {
    return aceiteLgpd;
  }

  public LocalDate getDataAceiteLgpd() {
    return dataAceiteLgpd;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public List<DocumentoBeneficiarioResponse> getDocumentosObrigatorios() {
    return documentosObrigatorios;
  }

  public LocalDateTime getDataCadastro() {
    return dataCadastro;
  }

  public LocalDateTime getDataAtualizacao() {
    return dataAtualizacao;
  }
}
