package br.com.g3.cadastrobeneficiario.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CadastroBeneficiarioCriacaoRequest {
  @NotBlank
  @Size(max = 200)
  @JsonProperty("nome_completo")
  private String nomeCompleto;

  @JsonProperty("codigo")
  private String codigo;

  @Size(max = 200)
  @JsonProperty("nome_social")
  private String nomeSocial;

  @Size(max = 120)
  @JsonProperty("apelido")
  private String apelido;

  @NotNull
  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonProperty("data_nascimento")
  private LocalDate dataNascimento;

  @JsonProperty("foto_3x4")
  private String foto3x4;

  @Size(max = 60)
  @JsonProperty("sexo_biologico")
  private String sexoBiologico;

  @Size(max = 120)
  @JsonProperty("identidade_genero")
  private String identidadeGenero;

  @Size(max = 60)
  @JsonProperty("cor_raca")
  private String corRaca;

  @Size(max = 60)
  @JsonProperty("estado_civil")
  private String estadoCivil;

  @Size(max = 120)
  @JsonProperty("nacionalidade")
  private String nacionalidade;

  @Size(max = 150)
  @JsonProperty("naturalidade_cidade")
  private String naturalidadeCidade;

  @Size(max = 2)
  @JsonProperty("naturalidade_uf")
  private String naturalidadeUf;

  @NotBlank
  @Size(max = 200)
  @JsonProperty("nome_mae")
  private String nomeMae;

  @Size(max = 200)
  @JsonProperty("nome_pai")
  private String nomePai;

  @Size(max = 60)
  @JsonProperty("status")
  private String status;

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

  @Size(max = 60)
  @JsonProperty("subzona")
  private String subzona;

  @Size(max = 2)
  @JsonProperty("uf")
  private String uf;

  @JsonProperty("latitude")
  private String latitude;

  @JsonProperty("longitude")
  private String longitude;

  @Size(max = 30)
  @JsonProperty("telefone_principal")
  private String telefonePrincipal;

  @JsonProperty("telefone_principal_whatsapp")
  private Boolean telefonePrincipalWhatsapp;

  @Size(max = 30)
  @JsonProperty("telefone_secundario")
  private String telefoneSecundario;

  @Size(max = 150)
  @JsonProperty("telefone_recado_nome")
  private String telefoneRecadoNome;

  @Size(max = 30)
  @JsonProperty("telefone_recado_numero")
  private String telefoneRecadoNumero;

  @Size(max = 150)
  @JsonProperty("email")
  private String email;

  @JsonProperty("permite_contato_tel")
  private Boolean permiteContatoTel;

  @JsonProperty("permite_contato_whatsapp")
  private Boolean permiteContatoWhatsapp;

  @JsonProperty("permite_contato_sms")
  private Boolean permiteContatoSms;

  @JsonProperty("permite_contato_email")
  private Boolean permiteContatoEmail;

  @Size(max = 80)
  @JsonProperty("horario_preferencial_contato")
  private String horarioPreferencialContato;

  @Size(max = 60)
  @JsonProperty("cpf")
  private String cpf;

  @Size(max = 60)
  @JsonProperty("rg_numero")
  private String rgNumero;

  @Size(max = 120)
  @JsonProperty("rg_orgao_emissor")
  private String rgOrgaoEmissor;

  @Size(max = 2)
  @JsonProperty("rg_uf")
  private String rgUf;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonProperty("rg_data_emissao")
  private LocalDate rgDataEmissao;

  @Size(max = 60)
  @JsonProperty("nis")
  private String nis;

  @Size(max = 60)
  @JsonProperty("certidao_tipo")
  private String certidaoTipo;

  @Size(max = 60)
  @JsonProperty("certidao_livro")
  private String certidaoLivro;

  @Size(max = 60)
  @JsonProperty("certidao_folha")
  private String certidaoFolha;

  @Size(max = 60)
  @JsonProperty("certidao_termo")
  private String certidaoTermo;

  @Size(max = 150)
  @JsonProperty("certidao_cartorio")
  private String certidaoCartorio;

  @Size(max = 150)
  @JsonProperty("certidao_municipio")
  private String certidaoMunicipio;

  @Size(max = 2)
  @JsonProperty("certidao_uf")
  private String certidaoUf;

  @Size(max = 60)
  @JsonProperty("titulo_eleitor")
  private String tituloEleitor;

  @Size(max = 60)
  @JsonProperty("cnh")
  private String cnh;

  @Size(max = 60)
  @JsonProperty("cartao_sus")
  private String cartaoSus;

  @JsonProperty("mora_com_familia")
  private Boolean moraComFamilia;

  @JsonProperty("responsavel_legal")
  private Boolean responsavelLegal;

  @Size(max = 150)
  @JsonProperty("vinculo_familiar")
  private String vinculoFamiliar;

  @JsonProperty("situacao_vulnerabilidade")
  private String situacaoVulnerabilidade;

  @JsonProperty("composicao_familiar")
  private String composicaoFamiliar;

  @JsonProperty("criancas_adolescentes")
  private Integer criancasAdolescentes;

  @JsonProperty("idosos")
  private Integer idosos;

  @JsonProperty("acompanhamento_cras")
  private Boolean acompanhamentoCras;

  @JsonProperty("acompanhamento_saude")
  private Boolean acompanhamentoSaude;

  @Size(max = 200)
  @JsonProperty("participa_comunidade")
  private String participaComunidade;

  @JsonProperty("rede_apoio")
  private String redeApoio;

  @JsonProperty("sabe_ler_escrever")
  private Boolean sabeLerEscrever;

  @Size(max = 150)
  @JsonProperty("nivel_escolaridade")
  private String nivelEscolaridade;

  @JsonProperty("estuda_atualmente")
  private Boolean estudaAtualmente;

  @Size(max = 150)
  @JsonProperty("ocupacao")
  private String ocupacao;

  @Size(max = 150)
  @JsonProperty("situacao_trabalho")
  private String situacaoTrabalho;

  @Size(max = 150)
  @JsonProperty("local_trabalho")
  private String localTrabalho;

  @Size(max = 60)
  @JsonProperty("renda_mensal")
  private String rendaMensal;

  @Size(max = 150)
  @JsonProperty("fonte_renda")
  private String fonteRenda;

  @JsonProperty("possui_deficiencia")
  private Boolean possuiDeficiencia;

  @Size(max = 200)
  @JsonProperty("tipo_deficiencia")
  private String tipoDeficiencia;

  @Size(max = 60)
  @JsonProperty("cid_principal")
  private String cidPrincipal;

  @JsonProperty("usa_medicacao_continua")
  private Boolean usaMedicacaoContinua;

  @JsonProperty("descricao_medicacao")
  private String descricaoMedicacao;

  @Size(max = 200)
  @JsonProperty("servico_saude_referencia")
  private String servicoSaudeReferencia;

  @JsonProperty("recebe_beneficio")
  private Boolean recebeBeneficio;

  @JsonProperty("beneficios_descricao")
  private String beneficiosDescricao;

  @Size(max = 60)
  @JsonProperty("valor_total_beneficios")
  private String valorTotalBeneficios;

  @JsonProperty("beneficios_recebidos")
  private List<String> beneficiosRecebidos;

  @JsonProperty("aceite_lgpd")
  private Boolean aceiteLgpd;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonProperty("data_aceite_lgpd")
  private LocalDate dataAceiteLgpd;

  @JsonProperty("observacoes")
  private String observacoes;

  @JsonProperty("documentos_obrigatorios")
  @JsonAlias("documentosObrigatorios")
  private List<DocumentoUploadRequest> documentosObrigatorios;
  public String getNomeCompleto() {
    return nomeCompleto;
  }

  public void setNomeCompleto(String nomeCompleto) {
    this.nomeCompleto = nomeCompleto;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getNomeSocial() {
    return nomeSocial;
  }

  public void setNomeSocial(String nomeSocial) {
    this.nomeSocial = nomeSocial;
  }

  public String getApelido() {
    return apelido;
  }

  public void setApelido(String apelido) {
    this.apelido = apelido;
  }

  public LocalDate getDataNascimento() {
    return dataNascimento;
  }

  public void setDataNascimento(LocalDate dataNascimento) {
    this.dataNascimento = dataNascimento;
  }

  public String getFoto3x4() {
    return foto3x4;
  }

  public void setFoto3x4(String foto3x4) {
    this.foto3x4 = foto3x4;
  }

  public String getSexoBiologico() {
    return sexoBiologico;
  }

  public void setSexoBiologico(String sexoBiologico) {
    this.sexoBiologico = sexoBiologico;
  }

  public String getIdentidadeGenero() {
    return identidadeGenero;
  }

  public void setIdentidadeGenero(String identidadeGenero) {
    this.identidadeGenero = identidadeGenero;
  }

  public String getCorRaca() {
    return corRaca;
  }

  public void setCorRaca(String corRaca) {
    this.corRaca = corRaca;
  }

  public String getEstadoCivil() {
    return estadoCivil;
  }

  public void setEstadoCivil(String estadoCivil) {
    this.estadoCivil = estadoCivil;
  }

  public String getNacionalidade() {
    return nacionalidade;
  }

  public void setNacionalidade(String nacionalidade) {
    this.nacionalidade = nacionalidade;
  }

  public String getNaturalidadeCidade() {
    return naturalidadeCidade;
  }

  public void setNaturalidadeCidade(String naturalidadeCidade) {
    this.naturalidadeCidade = naturalidadeCidade;
  }

  public String getNaturalidadeUf() {
    return naturalidadeUf;
  }

  public void setNaturalidadeUf(String naturalidadeUf) {
    this.naturalidadeUf = naturalidadeUf;
  }

  public String getNomeMae() {
    return nomeMae;
  }

  public void setNomeMae(String nomeMae) {
    this.nomeMae = nomeMae;
  }

  public String getNomePai() {
    return nomePai;
  }

  public void setNomePai(String nomePai) {
    this.nomePai = nomePai;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
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

  public String getSubzona() {
    return subzona;
  }

  public void setSubzona(String subzona) {
    this.subzona = subzona;
  }

  public String getUf() {
    return uf;
  }

  public void setUf(String uf) {
    this.uf = uf;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  public String getTelefonePrincipal() {
    return telefonePrincipal;
  }

  public void setTelefonePrincipal(String telefonePrincipal) {
    this.telefonePrincipal = telefonePrincipal;
  }

  public Boolean getTelefonePrincipalWhatsapp() {
    return telefonePrincipalWhatsapp;
  }

  public void setTelefonePrincipalWhatsapp(Boolean telefonePrincipalWhatsapp) {
    this.telefonePrincipalWhatsapp = telefonePrincipalWhatsapp;
  }

  public String getTelefoneSecundario() {
    return telefoneSecundario;
  }

  public void setTelefoneSecundario(String telefoneSecundario) {
    this.telefoneSecundario = telefoneSecundario;
  }

  public String getTelefoneRecadoNome() {
    return telefoneRecadoNome;
  }

  public void setTelefoneRecadoNome(String telefoneRecadoNome) {
    this.telefoneRecadoNome = telefoneRecadoNome;
  }

  public String getTelefoneRecadoNumero() {
    return telefoneRecadoNumero;
  }

  public void setTelefoneRecadoNumero(String telefoneRecadoNumero) {
    this.telefoneRecadoNumero = telefoneRecadoNumero;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Boolean getPermiteContatoTel() {
    return permiteContatoTel;
  }

  public void setPermiteContatoTel(Boolean permiteContatoTel) {
    this.permiteContatoTel = permiteContatoTel;
  }

  public Boolean getPermiteContatoWhatsapp() {
    return permiteContatoWhatsapp;
  }

  public void setPermiteContatoWhatsapp(Boolean permiteContatoWhatsapp) {
    this.permiteContatoWhatsapp = permiteContatoWhatsapp;
  }

  public Boolean getPermiteContatoSms() {
    return permiteContatoSms;
  }

  public void setPermiteContatoSms(Boolean permiteContatoSms) {
    this.permiteContatoSms = permiteContatoSms;
  }

  public Boolean getPermiteContatoEmail() {
    return permiteContatoEmail;
  }

  public void setPermiteContatoEmail(Boolean permiteContatoEmail) {
    this.permiteContatoEmail = permiteContatoEmail;
  }

  public String getHorarioPreferencialContato() {
    return horarioPreferencialContato;
  }

  public void setHorarioPreferencialContato(String horarioPreferencialContato) {
    this.horarioPreferencialContato = horarioPreferencialContato;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getRgNumero() {
    return rgNumero;
  }

  public void setRgNumero(String rgNumero) {
    this.rgNumero = rgNumero;
  }

  public String getRgOrgaoEmissor() {
    return rgOrgaoEmissor;
  }

  public void setRgOrgaoEmissor(String rgOrgaoEmissor) {
    this.rgOrgaoEmissor = rgOrgaoEmissor;
  }

  public String getRgUf() {
    return rgUf;
  }

  public void setRgUf(String rgUf) {
    this.rgUf = rgUf;
  }

  public LocalDate getRgDataEmissao() {
    return rgDataEmissao;
  }

  public void setRgDataEmissao(LocalDate rgDataEmissao) {
    this.rgDataEmissao = rgDataEmissao;
  }

  public String getNis() {
    return nis;
  }

  public void setNis(String nis) {
    this.nis = nis;
  }

  public String getCertidaoTipo() {
    return certidaoTipo;
  }

  public void setCertidaoTipo(String certidaoTipo) {
    this.certidaoTipo = certidaoTipo;
  }

  public String getCertidaoLivro() {
    return certidaoLivro;
  }

  public void setCertidaoLivro(String certidaoLivro) {
    this.certidaoLivro = certidaoLivro;
  }

  public String getCertidaoFolha() {
    return certidaoFolha;
  }

  public void setCertidaoFolha(String certidaoFolha) {
    this.certidaoFolha = certidaoFolha;
  }

  public String getCertidaoTermo() {
    return certidaoTermo;
  }

  public void setCertidaoTermo(String certidaoTermo) {
    this.certidaoTermo = certidaoTermo;
  }

  public String getCertidaoCartorio() {
    return certidaoCartorio;
  }

  public void setCertidaoCartorio(String certidaoCartorio) {
    this.certidaoCartorio = certidaoCartorio;
  }

  public String getCertidaoMunicipio() {
    return certidaoMunicipio;
  }

  public void setCertidaoMunicipio(String certidaoMunicipio) {
    this.certidaoMunicipio = certidaoMunicipio;
  }

  public String getCertidaoUf() {
    return certidaoUf;
  }

  public void setCertidaoUf(String certidaoUf) {
    this.certidaoUf = certidaoUf;
  }

  public String getTituloEleitor() {
    return tituloEleitor;
  }

  public void setTituloEleitor(String tituloEleitor) {
    this.tituloEleitor = tituloEleitor;
  }

  public String getCnh() {
    return cnh;
  }

  public void setCnh(String cnh) {
    this.cnh = cnh;
  }

  public String getCartaoSus() {
    return cartaoSus;
  }

  public void setCartaoSus(String cartaoSus) {
    this.cartaoSus = cartaoSus;
  }

  public Boolean getMoraComFamilia() {
    return moraComFamilia;
  }

  public void setMoraComFamilia(Boolean moraComFamilia) {
    this.moraComFamilia = moraComFamilia;
  }

  public Boolean getResponsavelLegal() {
    return responsavelLegal;
  }

  public void setResponsavelLegal(Boolean responsavelLegal) {
    this.responsavelLegal = responsavelLegal;
  }

  public String getVinculoFamiliar() {
    return vinculoFamiliar;
  }

  public void setVinculoFamiliar(String vinculoFamiliar) {
    this.vinculoFamiliar = vinculoFamiliar;
  }

  public String getSituacaoVulnerabilidade() {
    return situacaoVulnerabilidade;
  }

  public void setSituacaoVulnerabilidade(String situacaoVulnerabilidade) {
    this.situacaoVulnerabilidade = situacaoVulnerabilidade;
  }

  public String getComposicaoFamiliar() {
    return composicaoFamiliar;
  }

  public void setComposicaoFamiliar(String composicaoFamiliar) {
    this.composicaoFamiliar = composicaoFamiliar;
  }

  public Integer getCriancasAdolescentes() {
    return criancasAdolescentes;
  }

  public void setCriancasAdolescentes(Integer criancasAdolescentes) {
    this.criancasAdolescentes = criancasAdolescentes;
  }

  public Integer getIdosos() {
    return idosos;
  }

  public void setIdosos(Integer idosos) {
    this.idosos = idosos;
  }

  public Boolean getAcompanhamentoCras() {
    return acompanhamentoCras;
  }

  public void setAcompanhamentoCras(Boolean acompanhamentoCras) {
    this.acompanhamentoCras = acompanhamentoCras;
  }

  public Boolean getAcompanhamentoSaude() {
    return acompanhamentoSaude;
  }

  public void setAcompanhamentoSaude(Boolean acompanhamentoSaude) {
    this.acompanhamentoSaude = acompanhamentoSaude;
  }

  public String getParticipaComunidade() {
    return participaComunidade;
  }

  public void setParticipaComunidade(String participaComunidade) {
    this.participaComunidade = participaComunidade;
  }

  public String getRedeApoio() {
    return redeApoio;
  }

  public void setRedeApoio(String redeApoio) {
    this.redeApoio = redeApoio;
  }

  public Boolean getSabeLerEscrever() {
    return sabeLerEscrever;
  }

  public void setSabeLerEscrever(Boolean sabeLerEscrever) {
    this.sabeLerEscrever = sabeLerEscrever;
  }

  public String getNivelEscolaridade() {
    return nivelEscolaridade;
  }

  public void setNivelEscolaridade(String nivelEscolaridade) {
    this.nivelEscolaridade = nivelEscolaridade;
  }

  public Boolean getEstudaAtualmente() {
    return estudaAtualmente;
  }

  public void setEstudaAtualmente(Boolean estudaAtualmente) {
    this.estudaAtualmente = estudaAtualmente;
  }

  public String getOcupacao() {
    return ocupacao;
  }

  public void setOcupacao(String ocupacao) {
    this.ocupacao = ocupacao;
  }

  public String getSituacaoTrabalho() {
    return situacaoTrabalho;
  }

  public void setSituacaoTrabalho(String situacaoTrabalho) {
    this.situacaoTrabalho = situacaoTrabalho;
  }

  public String getLocalTrabalho() {
    return localTrabalho;
  }

  public void setLocalTrabalho(String localTrabalho) {
    this.localTrabalho = localTrabalho;
  }

  public String getRendaMensal() {
    return rendaMensal;
  }

  public void setRendaMensal(String rendaMensal) {
    this.rendaMensal = rendaMensal;
  }

  public String getFonteRenda() {
    return fonteRenda;
  }

  public void setFonteRenda(String fonteRenda) {
    this.fonteRenda = fonteRenda;
  }

  public Boolean getPossuiDeficiencia() {
    return possuiDeficiencia;
  }

  public void setPossuiDeficiencia(Boolean possuiDeficiencia) {
    this.possuiDeficiencia = possuiDeficiencia;
  }

  public String getTipoDeficiencia() {
    return tipoDeficiencia;
  }

  public void setTipoDeficiencia(String tipoDeficiencia) {
    this.tipoDeficiencia = tipoDeficiencia;
  }

  public String getCidPrincipal() {
    return cidPrincipal;
  }

  public void setCidPrincipal(String cidPrincipal) {
    this.cidPrincipal = cidPrincipal;
  }

  public Boolean getUsaMedicacaoContinua() {
    return usaMedicacaoContinua;
  }

  public void setUsaMedicacaoContinua(Boolean usaMedicacaoContinua) {
    this.usaMedicacaoContinua = usaMedicacaoContinua;
  }

  public String getDescricaoMedicacao() {
    return descricaoMedicacao;
  }

  public void setDescricaoMedicacao(String descricaoMedicacao) {
    this.descricaoMedicacao = descricaoMedicacao;
  }

  public String getServicoSaudeReferencia() {
    return servicoSaudeReferencia;
  }

  public void setServicoSaudeReferencia(String servicoSaudeReferencia) {
    this.servicoSaudeReferencia = servicoSaudeReferencia;
  }

  public Boolean getRecebeBeneficio() {
    return recebeBeneficio;
  }

  public void setRecebeBeneficio(Boolean recebeBeneficio) {
    this.recebeBeneficio = recebeBeneficio;
  }

  public String getBeneficiosDescricao() {
    return beneficiosDescricao;
  }

  public void setBeneficiosDescricao(String beneficiosDescricao) {
    this.beneficiosDescricao = beneficiosDescricao;
  }

  public String getValorTotalBeneficios() {
    return valorTotalBeneficios;
  }

  public void setValorTotalBeneficios(String valorTotalBeneficios) {
    this.valorTotalBeneficios = valorTotalBeneficios;
  }

  public List<String> getBeneficiosRecebidos() {
    return beneficiosRecebidos;
  }

  public void setBeneficiosRecebidos(List<String> beneficiosRecebidos) {
    this.beneficiosRecebidos = beneficiosRecebidos;
  }

  public Boolean getAceiteLgpd() {
    return aceiteLgpd;
  }

  public void setAceiteLgpd(Boolean aceiteLgpd) {
    this.aceiteLgpd = aceiteLgpd;
  }

  public LocalDate getDataAceiteLgpd() {
    return dataAceiteLgpd;
  }

  public void setDataAceiteLgpd(LocalDate dataAceiteLgpd) {
    this.dataAceiteLgpd = dataAceiteLgpd;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public List<DocumentoUploadRequest> getDocumentosObrigatorios() {
    return documentosObrigatorios;
  }

  public void setDocumentosObrigatorios(List<DocumentoUploadRequest> documentosObrigatorios) {
    this.documentosObrigatorios = documentosObrigatorios;
  }
}
