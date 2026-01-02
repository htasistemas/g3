package br.com.g3.cadastrobeneficiario.mapper;

import br.com.g3.cadastrobeneficiario.domain.BeneficiosBeneficiario;
import br.com.g3.cadastrobeneficiario.domain.CadastroBeneficiario;
import br.com.g3.cadastrobeneficiario.domain.ContatoBeneficiario;
import br.com.g3.cadastrobeneficiario.domain.DocumentoBeneficiario;
import br.com.g3.cadastrobeneficiario.domain.EscolaridadeBeneficiario;
import br.com.g3.cadastrobeneficiario.domain.ObservacoesBeneficiario;
import br.com.g3.cadastrobeneficiario.domain.SaudeBeneficiario;
import br.com.g3.cadastrobeneficiario.domain.SituacaoSocialBeneficiario;
import br.com.g3.cadastrobeneficiario.dto.CadastroBeneficiarioCriacaoRequest;
import br.com.g3.cadastrobeneficiario.dto.CadastroBeneficiarioResponse;
import br.com.g3.cadastrobeneficiario.dto.DocumentoBeneficiarioResponse;
import br.com.g3.unidadeassistencial.domain.Endereco;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CadastroBeneficiarioMapper {
  private static final String TIPO_CPF = "CPF";
  private static final String TIPO_RG = "RG";
  private static final String TIPO_NIS = "NIS";
  private static final String TIPO_CERTIDAO = "CERTIDAO";
  private static final String TIPO_TITULO_ELEITOR = "TITULO_ELEITOR";
  private static final String TIPO_CNH = "CNH";
  private static final String TIPO_CARTAO_SUS = "CARTAO_SUS";

  private CadastroBeneficiarioMapper() {}

  public static CadastroBeneficiario toDomain(CadastroBeneficiarioCriacaoRequest request) {
    CadastroBeneficiario cadastro = new CadastroBeneficiario();
    cadastro.setCodigo(request.getCodigo());
    cadastro.setNomeCompleto(request.getNomeCompleto());
    cadastro.setNomeSocial(request.getNomeSocial());
    cadastro.setApelido(request.getApelido());
    cadastro.setDataNascimento(request.getDataNascimento());
    cadastro.setFoto3x4(request.getFoto3x4());
    cadastro.setSexoBiologico(request.getSexoBiologico());
    cadastro.setIdentidadeGenero(request.getIdentidadeGenero());
    cadastro.setCorRaca(request.getCorRaca());
    cadastro.setEstadoCivil(request.getEstadoCivil());
    cadastro.setNacionalidade(request.getNacionalidade());
    cadastro.setNaturalidadeCidade(request.getNaturalidadeCidade());
    cadastro.setNaturalidadeUf(request.getNaturalidadeUf());
    cadastro.setNomeMae(request.getNomeMae());
    cadastro.setNomePai(request.getNomePai());
    cadastro.setStatus(normalizarStatus(request.getStatus()));
    cadastro.setOptaReceberCestaBasica(request.getOptaReceberCestaBasica());
    cadastro.setAptoReceberCestaBasica(request.getAptoReceberCestaBasica());
    LocalDateTime agora = LocalDateTime.now();
    cadastro.setCriadoEm(agora);
    cadastro.setAtualizadoEm(agora);
    cadastro.setEndereco(criarEndereco(request, agora));
    adicionarContato(cadastro, request, agora);
    adicionarSituacaoSocial(cadastro, request, agora);
    adicionarEscolaridade(cadastro, request, agora);
    adicionarSaude(cadastro, request, agora);
    adicionarBeneficios(cadastro, request, agora);
    adicionarObservacoes(cadastro, request, agora);
    cadastro.getDocumentos().addAll(criarDocumentosCadastro(request, cadastro, agora));
    return cadastro;
  }

  public static void aplicarAtualizacao(CadastroBeneficiario cadastro, CadastroBeneficiarioCriacaoRequest request) {
    cadastro.setCodigo(request.getCodigo());
    cadastro.setNomeCompleto(request.getNomeCompleto());
    cadastro.setNomeSocial(request.getNomeSocial());
    cadastro.setApelido(request.getApelido());
    cadastro.setDataNascimento(request.getDataNascimento());
    cadastro.setFoto3x4(request.getFoto3x4());
    cadastro.setSexoBiologico(request.getSexoBiologico());
    cadastro.setIdentidadeGenero(request.getIdentidadeGenero());
    cadastro.setCorRaca(request.getCorRaca());
    cadastro.setEstadoCivil(request.getEstadoCivil());
    cadastro.setNacionalidade(request.getNacionalidade());
    cadastro.setNaturalidadeCidade(request.getNaturalidadeCidade());
    cadastro.setNaturalidadeUf(request.getNaturalidadeUf());
    cadastro.setNomeMae(request.getNomeMae());
    cadastro.setNomePai(request.getNomePai());
    cadastro.setStatus(normalizarStatus(request.getStatus()));
    cadastro.setOptaReceberCestaBasica(request.getOptaReceberCestaBasica());
    cadastro.setAptoReceberCestaBasica(request.getAptoReceberCestaBasica());
    LocalDateTime agora = LocalDateTime.now();
    cadastro.setAtualizadoEm(agora);
    aplicarEndereco(cadastro, request);
    cadastro.getContatos().clear();
    cadastro.getSituacoesSociais().clear();
    cadastro.getEscolaridades().clear();
    cadastro.getSaudes().clear();
    cadastro.getBeneficios().clear();
    cadastro.getObservacoesRegistros().clear();
    cadastro.getDocumentos().clear();
    adicionarContato(cadastro, request, agora);
    adicionarSituacaoSocial(cadastro, request, agora);
    adicionarEscolaridade(cadastro, request, agora);
    adicionarSaude(cadastro, request, agora);
    adicionarBeneficios(cadastro, request, agora);
    adicionarObservacoes(cadastro, request, agora);
    cadastro.getDocumentos().addAll(criarDocumentosCadastro(request, cadastro, agora));
  }

  public static CadastroBeneficiarioResponse toResponse(CadastroBeneficiario cadastro) {
    Endereco endereco = cadastro.getEndereco();
    ContatoBeneficiario contato = obterUltimo(cadastro.getContatos(), ContatoBeneficiario::getAtualizadoEm);
    SituacaoSocialBeneficiario situacaoSocial =
        obterUltimo(cadastro.getSituacoesSociais(), SituacaoSocialBeneficiario::getAtualizadoEm);
    EscolaridadeBeneficiario escolaridade =
        obterUltimo(cadastro.getEscolaridades(), EscolaridadeBeneficiario::getAtualizadoEm);
    SaudeBeneficiario saude = obterUltimo(cadastro.getSaudes(), SaudeBeneficiario::getAtualizadoEm);
    BeneficiosBeneficiario beneficios =
        obterUltimo(cadastro.getBeneficios(), BeneficiosBeneficiario::getAtualizadoEm);
    ObservacoesBeneficiario observacoes =
        obterUltimo(cadastro.getObservacoesRegistros(), ObservacoesBeneficiario::getAtualizadoEm);

    List<DocumentoBeneficiario> documentos = cadastro.getDocumentos();
    DocumentoBeneficiario docCpf = buscarDocumentoPorTipo(documentos, TIPO_CPF);
    DocumentoBeneficiario docRg = buscarDocumentoPorTipo(documentos, TIPO_RG);
    DocumentoBeneficiario docNis = buscarDocumentoPorTipo(documentos, TIPO_NIS);
    DocumentoBeneficiario docCertidao = buscarDocumentoPorTipo(documentos, TIPO_CERTIDAO);
    DocumentoBeneficiario docTitulo = buscarDocumentoPorTipo(documentos, TIPO_TITULO_ELEITOR);
    DocumentoBeneficiario docCnh = buscarDocumentoPorTipo(documentos, TIPO_CNH);
    DocumentoBeneficiario docCartaoSus = buscarDocumentoPorTipo(documentos, TIPO_CARTAO_SUS);

    List<DocumentoBeneficiarioResponse> documentosObrigatorios =
        documentos.stream()
            .filter(doc -> temValor(doc.getNomeArquivo()) || temValor(doc.getCaminhoArquivo()))
            .map(
                doc ->
                    new DocumentoBeneficiarioResponse(
                        doc.getId(),
                        doc.getNomeDocumento(),
                        doc.getNomeArquivo(),
                        doc.getCaminhoArquivo(),
                        doc.getContentType(),
                        doc.getObrigatorio()))
            .collect(Collectors.toList());

    return new CadastroBeneficiarioResponse(
        cadastro.getId(),
        cadastro.getCodigo(),
        cadastro.getNomeCompleto(),
        cadastro.getNomeSocial(),
        cadastro.getApelido(),
        cadastro.getDataNascimento(),
        cadastro.getFoto3x4(),
        cadastro.getSexoBiologico(),
        cadastro.getIdentidadeGenero(),
        cadastro.getCorRaca(),
        cadastro.getEstadoCivil(),
        cadastro.getNacionalidade(),
        cadastro.getNaturalidadeCidade(),
        cadastro.getNaturalidadeUf(),
        cadastro.getNomeMae(),
        cadastro.getNomePai(),
        cadastro.getStatus(),
        cadastro.getOptaReceberCestaBasica(),
        cadastro.getAptoReceberCestaBasica(),
        endereco != null ? endereco.getCep() : null,
        endereco != null ? endereco.getLogradouro() : null,
        endereco != null ? endereco.getNumero() : null,
        endereco != null ? endereco.getComplemento() : null,
        endereco != null ? endereco.getBairro() : null,
        endereco != null ? endereco.getPontoReferencia() : null,
        endereco != null ? endereco.getCidade() : null,
        endereco != null ? endereco.getZona() : null,
        endereco != null ? endereco.getSubzona() : null,
        endereco != null ? endereco.getEstado() : null,
        endereco != null && endereco.getLatitude() != null ? endereco.getLatitude().toPlainString() : null,
        endereco != null && endereco.getLongitude() != null ? endereco.getLongitude().toPlainString() : null,
        contato != null ? contato.getTelefonePrincipal() : null,
        contato != null ? contato.getTelefonePrincipalWhatsapp() : null,
        contato != null ? contato.getTelefoneSecundario() : null,
        contato != null ? contato.getTelefoneRecadoNome() : null,
        contato != null ? contato.getTelefoneRecadoNumero() : null,
        contato != null ? contato.getEmail() : null,
        contato != null ? contato.getPermiteContatoTel() : null,
        contato != null ? contato.getPermiteContatoWhatsapp() : null,
        contato != null ? contato.getPermiteContatoSms() : null,
        contato != null ? contato.getPermiteContatoEmail() : null,
        contato != null ? contato.getHorarioPreferencialContato() : null,
        docCpf != null ? docCpf.getNumeroDocumento() : null,
        docRg != null ? docRg.getNumeroDocumento() : null,
        docRg != null ? docRg.getOrgaoEmissor() : null,
        docRg != null ? docRg.getUfEmissor() : null,
        docRg != null ? docRg.getDataEmissao() : null,
        docNis != null ? docNis.getNumeroDocumento() : null,
        docCertidao != null ? docCertidao.getNomeDocumento() : null,
        docCertidao != null ? docCertidao.getLivro() : null,
        docCertidao != null ? docCertidao.getFolha() : null,
        docCertidao != null ? docCertidao.getTermo() : null,
        docCertidao != null ? docCertidao.getCartorio() : null,
        docCertidao != null ? docCertidao.getMunicipio() : null,
        docCertidao != null ? docCertidao.getUf() : null,
        docTitulo != null ? docTitulo.getNumeroDocumento() : null,
        docCnh != null ? docCnh.getNumeroDocumento() : null,
        docCartaoSus != null ? docCartaoSus.getNumeroDocumento() : null,
        situacaoSocial != null ? situacaoSocial.getMoraComFamilia() : null,
        situacaoSocial != null ? situacaoSocial.getResponsavelLegal() : null,
        situacaoSocial != null ? situacaoSocial.getVinculoFamiliar() : null,
        situacaoSocial != null ? situacaoSocial.getSituacaoVulnerabilidade() : null,
        situacaoSocial != null ? situacaoSocial.getComposicaoFamiliar() : null,
        situacaoSocial != null ? situacaoSocial.getCriancasAdolescentes() : null,
        situacaoSocial != null ? situacaoSocial.getIdosos() : null,
        situacaoSocial != null ? situacaoSocial.getAcompanhamentoCras() : null,
        situacaoSocial != null ? situacaoSocial.getAcompanhamentoSaude() : null,
        situacaoSocial != null ? situacaoSocial.getParticipaComunidade() : null,
        situacaoSocial != null ? situacaoSocial.getRedeApoio() : null,
        escolaridade != null ? escolaridade.getSabeLerEscrever() : null,
        escolaridade != null ? escolaridade.getNivelEscolaridade() : null,
        escolaridade != null ? escolaridade.getEstudaAtualmente() : null,
        escolaridade != null ? escolaridade.getOcupacao() : null,
        escolaridade != null ? escolaridade.getSituacaoTrabalho() : null,
        escolaridade != null ? escolaridade.getLocalTrabalho() : null,
        escolaridade != null ? escolaridade.getRendaMensal() : null,
        escolaridade != null ? escolaridade.getFonteRenda() : null,
        saude != null ? saude.getPossuiDeficiencia() : null,
        saude != null ? saude.getTipoDeficiencia() : null,
        saude != null ? saude.getCidPrincipal() : null,
        saude != null ? saude.getUsaMedicacaoContinua() : null,
        saude != null ? saude.getDescricaoMedicacao() : null,
        saude != null ? saude.getServicoSaudeReferencia() : null,
        beneficios != null ? beneficios.getRecebeBeneficio() : null,
        beneficios != null ? beneficios.getBeneficiosDescricao() : null,
        beneficios != null ? beneficios.getValorTotalBeneficios() : null,
        beneficios != null ? separarLista(beneficios.getBeneficiosRecebidos()) : null,
        observacoes != null ? observacoes.getAceiteLgpd() : null,
        observacoes != null ? observacoes.getDataAceiteLgpd() : null,
        observacoes != null ? observacoes.getObservacoes() : null,
        documentosObrigatorios,
        cadastro.getCriadoEm(),
        cadastro.getAtualizadoEm());
  }

  private static Endereco criarEndereco(CadastroBeneficiarioCriacaoRequest request, LocalDateTime agora) {
    if (!possuiDadosEndereco(request)) {
      return null;
    }
    Endereco endereco = new Endereco();
    endereco.setCep(request.getCep());
    endereco.setLogradouro(request.getLogradouro());
    endereco.setNumero(request.getNumero());
    endereco.setComplemento(request.getComplemento());
    endereco.setBairro(request.getBairro());
    endereco.setPontoReferencia(request.getPontoReferencia());
    endereco.setCidade(request.getMunicipio());
    endereco.setZona(request.getZona());
    endereco.setSubzona(request.getSubzona());
    endereco.setEstado(request.getUf());
    endereco.setLatitude(parseDecimal(request.getLatitude()));
    endereco.setLongitude(parseDecimal(request.getLongitude()));
    endereco.setCriadoEm(agora);
    endereco.setAtualizadoEm(agora);
    return endereco;
  }

  private static String normalizarStatus(String status) {
    if (status == null || status.trim().isEmpty()) {
      return "EM_ANALISE";
    }
    return status;
  }

  private static void aplicarEndereco(CadastroBeneficiario cadastro, CadastroBeneficiarioCriacaoRequest request) {
    if (!possuiDadosEndereco(request)) {
      cadastro.setEndereco(null);
      return;
    }

    LocalDateTime agora = LocalDateTime.now();
    Endereco endereco = cadastro.getEndereco();

    if (endereco == null) {
      endereco = new Endereco();
      endereco.setCriadoEm(agora);
      cadastro.setEndereco(endereco);
    } else if (endereco.getCriadoEm() == null) {
      endereco.setCriadoEm(agora);
    }

    endereco.setCep(request.getCep());
    endereco.setLogradouro(request.getLogradouro());
    endereco.setNumero(request.getNumero());
    endereco.setComplemento(request.getComplemento());
    endereco.setBairro(request.getBairro());
    endereco.setPontoReferencia(request.getPontoReferencia());
    endereco.setCidade(request.getMunicipio());
    endereco.setZona(request.getZona());
    endereco.setSubzona(request.getSubzona());
    endereco.setEstado(request.getUf());
    endereco.setLatitude(parseDecimal(request.getLatitude()));
    endereco.setLongitude(parseDecimal(request.getLongitude()));
    endereco.setAtualizadoEm(agora);
  }

  private static void adicionarContato(
      CadastroBeneficiario cadastro, CadastroBeneficiarioCriacaoRequest request, LocalDateTime agora) {
    if (!possuiDadosContato(request)) {
      return;
    }

    ContatoBeneficiario contato = new ContatoBeneficiario();
    contato.setBeneficiario(cadastro);
    contato.setTelefonePrincipal(limparTexto(request.getTelefonePrincipal()));
    contato.setTelefonePrincipalWhatsapp(request.getTelefonePrincipalWhatsapp());
    contato.setTelefoneSecundario(limparTexto(request.getTelefoneSecundario()));
    contato.setTelefoneRecadoNome(limparTexto(request.getTelefoneRecadoNome()));
    contato.setTelefoneRecadoNumero(limparTexto(request.getTelefoneRecadoNumero()));
    contato.setEmail(limparTexto(request.getEmail()));
    contato.setPermiteContatoTel(request.getPermiteContatoTel());
    contato.setPermiteContatoWhatsapp(request.getPermiteContatoWhatsapp());
    contato.setPermiteContatoSms(request.getPermiteContatoSms());
    contato.setPermiteContatoEmail(request.getPermiteContatoEmail());
    contato.setHorarioPreferencialContato(limparTexto(request.getHorarioPreferencialContato()));
    contato.setCriadoEm(agora);
    contato.setAtualizadoEm(agora);
    cadastro.getContatos().add(contato);
  }

  private static void adicionarSituacaoSocial(
      CadastroBeneficiario cadastro, CadastroBeneficiarioCriacaoRequest request, LocalDateTime agora) {
    if (!possuiDadosSituacaoSocial(request)) {
      return;
    }

    SituacaoSocialBeneficiario situacao = new SituacaoSocialBeneficiario();
    situacao.setBeneficiario(cadastro);
    situacao.setMoraComFamilia(request.getMoraComFamilia());
    situacao.setResponsavelLegal(request.getResponsavelLegal());
    situacao.setVinculoFamiliar(limparTexto(request.getVinculoFamiliar()));
    situacao.setSituacaoVulnerabilidade(limparTexto(request.getSituacaoVulnerabilidade()));
    situacao.setComposicaoFamiliar(limparTexto(request.getComposicaoFamiliar()));
    situacao.setCriancasAdolescentes(request.getCriancasAdolescentes());
    situacao.setIdosos(request.getIdosos());
    situacao.setAcompanhamentoCras(request.getAcompanhamentoCras());
    situacao.setAcompanhamentoSaude(request.getAcompanhamentoSaude());
    situacao.setParticipaComunidade(limparTexto(request.getParticipaComunidade()));
    situacao.setRedeApoio(limparTexto(request.getRedeApoio()));
    situacao.setCriadoEm(agora);
    situacao.setAtualizadoEm(agora);
    cadastro.getSituacoesSociais().add(situacao);
  }

  private static void adicionarEscolaridade(
      CadastroBeneficiario cadastro, CadastroBeneficiarioCriacaoRequest request, LocalDateTime agora) {
    if (!possuiDadosEscolaridade(request)) {
      return;
    }

    EscolaridadeBeneficiario escolaridade = new EscolaridadeBeneficiario();
    escolaridade.setBeneficiario(cadastro);
    escolaridade.setSabeLerEscrever(request.getSabeLerEscrever());
    escolaridade.setNivelEscolaridade(limparTexto(request.getNivelEscolaridade()));
    escolaridade.setEstudaAtualmente(request.getEstudaAtualmente());
    escolaridade.setOcupacao(limparTexto(request.getOcupacao()));
    escolaridade.setSituacaoTrabalho(limparTexto(request.getSituacaoTrabalho()));
    escolaridade.setLocalTrabalho(limparTexto(request.getLocalTrabalho()));
    escolaridade.setRendaMensal(limparTexto(request.getRendaMensal()));
    escolaridade.setFonteRenda(limparTexto(request.getFonteRenda()));
    escolaridade.setCriadoEm(agora);
    escolaridade.setAtualizadoEm(agora);
    cadastro.getEscolaridades().add(escolaridade);
  }

  private static void adicionarSaude(
      CadastroBeneficiario cadastro, CadastroBeneficiarioCriacaoRequest request, LocalDateTime agora) {
    if (!possuiDadosSaude(request)) {
      return;
    }

    SaudeBeneficiario saude = new SaudeBeneficiario();
    saude.setBeneficiario(cadastro);
    saude.setPossuiDeficiencia(request.getPossuiDeficiencia());
    saude.setTipoDeficiencia(limparTexto(request.getTipoDeficiencia()));
    saude.setCidPrincipal(limparTexto(request.getCidPrincipal()));
    saude.setUsaMedicacaoContinua(request.getUsaMedicacaoContinua());
    saude.setDescricaoMedicacao(limparTexto(request.getDescricaoMedicacao()));
    saude.setServicoSaudeReferencia(limparTexto(request.getServicoSaudeReferencia()));
    saude.setCriadoEm(agora);
    saude.setAtualizadoEm(agora);
    cadastro.getSaudes().add(saude);
  }

  private static void adicionarBeneficios(
      CadastroBeneficiario cadastro, CadastroBeneficiarioCriacaoRequest request, LocalDateTime agora) {
    if (!possuiDadosBeneficios(request)) {
      return;
    }

    BeneficiosBeneficiario beneficios = new BeneficiosBeneficiario();
    beneficios.setBeneficiario(cadastro);
    beneficios.setRecebeBeneficio(request.getRecebeBeneficio());
    beneficios.setBeneficiosDescricao(limparTexto(request.getBeneficiosDescricao()));
    beneficios.setValorTotalBeneficios(limparTexto(request.getValorTotalBeneficios()));
    beneficios.setBeneficiosRecebidos(juntarLista(request.getBeneficiosRecebidos()));
    beneficios.setCriadoEm(agora);
    beneficios.setAtualizadoEm(agora);
    cadastro.getBeneficios().add(beneficios);
  }

  private static void adicionarObservacoes(
      CadastroBeneficiario cadastro, CadastroBeneficiarioCriacaoRequest request, LocalDateTime agora) {
    if (!possuiDadosObservacoes(request)) {
      return;
    }

    ObservacoesBeneficiario observacoes = new ObservacoesBeneficiario();
    observacoes.setBeneficiario(cadastro);
    observacoes.setAceiteLgpd(request.getAceiteLgpd());
    observacoes.setDataAceiteLgpd(request.getDataAceiteLgpd());
    observacoes.setObservacoes(limparTexto(request.getObservacoes()));
    observacoes.setCriadoEm(agora);
    observacoes.setAtualizadoEm(agora);
    cadastro.getObservacoesRegistros().add(observacoes);
  }

  private static List<DocumentoBeneficiario> criarDocumentosCadastro(
      CadastroBeneficiarioCriacaoRequest request, CadastroBeneficiario cadastro, LocalDateTime agora) {
    List<DocumentoBeneficiario> documentos = new ArrayList<>();

    adicionarDocumentoNumero(documentos, cadastro, agora, TIPO_CPF, request.getCpf());
    adicionarDocumentoNumero(documentos, cadastro, agora, TIPO_NIS, request.getNis());
    adicionarDocumentoNumero(documentos, cadastro, agora, TIPO_TITULO_ELEITOR, request.getTituloEleitor());
    adicionarDocumentoNumero(documentos, cadastro, agora, TIPO_CNH, request.getCnh());
    adicionarDocumentoNumero(documentos, cadastro, agora, TIPO_CARTAO_SUS, request.getCartaoSus());

    if (temValor(request.getRgNumero())) {
      DocumentoBeneficiario rg = new DocumentoBeneficiario();
      rg.setBeneficiario(cadastro);
      rg.setTipoDocumento(TIPO_RG);
      rg.setNumeroDocumento(limparTexto(request.getRgNumero()));
      rg.setOrgaoEmissor(limparTexto(request.getRgOrgaoEmissor()));
      rg.setUfEmissor(limparTexto(request.getRgUf()));
      rg.setDataEmissao(request.getRgDataEmissao());
      rg.setCriadoEm(agora);
      rg.setAtualizadoEm(agora);
      documentos.add(rg);
    }

    if (possuiDadosCertidao(request)) {
      DocumentoBeneficiario certidao = new DocumentoBeneficiario();
      certidao.setBeneficiario(cadastro);
      certidao.setTipoDocumento(TIPO_CERTIDAO);
      certidao.setLivro(limparTexto(request.getCertidaoLivro()));
      certidao.setFolha(limparTexto(request.getCertidaoFolha()));
      certidao.setTermo(limparTexto(request.getCertidaoTermo()));
      certidao.setCartorio(limparTexto(request.getCertidaoCartorio()));
      certidao.setMunicipio(limparTexto(request.getCertidaoMunicipio()));
      certidao.setUf(limparTexto(request.getCertidaoUf()));
      certidao.setNomeDocumento(limparTexto(request.getCertidaoTipo()));
      certidao.setCriadoEm(agora);
      certidao.setAtualizadoEm(agora);
      documentos.add(certidao);
    }

    return documentos;
  }

  private static void adicionarDocumentoNumero(
      List<DocumentoBeneficiario> documentos,
      CadastroBeneficiario cadastro,
      LocalDateTime agora,
      String tipo,
      String numero) {
    if (!temValor(numero)) {
      return;
    }

    DocumentoBeneficiario doc = new DocumentoBeneficiario();
    doc.setBeneficiario(cadastro);
    doc.setTipoDocumento(tipo);
    doc.setNumeroDocumento(limparTexto(numero));
    doc.setCriadoEm(agora);
    doc.setAtualizadoEm(agora);
    documentos.add(doc);
  }

  private static DocumentoBeneficiario buscarDocumentoPorTipo(List<DocumentoBeneficiario> documentos, String tipo) {
    if (documentos == null || documentos.isEmpty()) {
      return null;
    }

    return documentos.stream()
        .filter(doc -> Objects.equals(tipo, doc.getTipoDocumento()))
        .max(Comparator.comparing(DocumentoBeneficiario::getAtualizadoEm))
        .orElse(null);
  }

  private static boolean possuiDadosEndereco(CadastroBeneficiarioCriacaoRequest request) {
    return temValor(request.getCep())
        || temValor(request.getLogradouro())
        || temValor(request.getNumero())
        || temValor(request.getComplemento())
        || temValor(request.getBairro())
        || temValor(request.getPontoReferencia())
        || temValor(request.getMunicipio())
        || temValor(request.getZona())
        || temValor(request.getSubzona())
        || temValor(request.getUf());
  }

  private static boolean possuiDadosContato(CadastroBeneficiarioCriacaoRequest request) {
    return temValor(request.getTelefonePrincipal())
        || temValor(request.getTelefoneSecundario())
        || temValor(request.getTelefoneRecadoNome())
        || temValor(request.getTelefoneRecadoNumero())
        || temValor(request.getEmail())
        || Boolean.TRUE.equals(request.getPermiteContatoTel())
        || Boolean.TRUE.equals(request.getPermiteContatoWhatsapp())
        || Boolean.TRUE.equals(request.getPermiteContatoSms())
        || Boolean.TRUE.equals(request.getPermiteContatoEmail())
        || temValor(request.getHorarioPreferencialContato());
  }

  private static boolean possuiDadosSituacaoSocial(CadastroBeneficiarioCriacaoRequest request) {
    return Boolean.TRUE.equals(request.getMoraComFamilia())
        || Boolean.TRUE.equals(request.getResponsavelLegal())
        || temValor(request.getVinculoFamiliar())
        || temValor(request.getSituacaoVulnerabilidade())
        || temValor(request.getComposicaoFamiliar())
        || request.getCriancasAdolescentes() != null
        || request.getIdosos() != null
        || Boolean.TRUE.equals(request.getAcompanhamentoCras())
        || Boolean.TRUE.equals(request.getAcompanhamentoSaude())
        || temValor(request.getParticipaComunidade())
        || temValor(request.getRedeApoio());
  }

  private static boolean possuiDadosEscolaridade(CadastroBeneficiarioCriacaoRequest request) {
    return Boolean.TRUE.equals(request.getSabeLerEscrever())
        || temValor(request.getNivelEscolaridade())
        || Boolean.TRUE.equals(request.getEstudaAtualmente())
        || temValor(request.getOcupacao())
        || temValor(request.getSituacaoTrabalho())
        || temValor(request.getLocalTrabalho())
        || temValor(request.getRendaMensal())
        || temValor(request.getFonteRenda());
  }

  private static boolean possuiDadosSaude(CadastroBeneficiarioCriacaoRequest request) {
    return Boolean.TRUE.equals(request.getPossuiDeficiencia())
        || temValor(request.getTipoDeficiencia())
        || temValor(request.getCidPrincipal())
        || Boolean.TRUE.equals(request.getUsaMedicacaoContinua())
        || temValor(request.getDescricaoMedicacao())
        || temValor(request.getServicoSaudeReferencia());
  }

  private static boolean possuiDadosBeneficios(CadastroBeneficiarioCriacaoRequest request) {
    return Boolean.TRUE.equals(request.getRecebeBeneficio())
        || temValor(request.getBeneficiosDescricao())
        || temValor(request.getValorTotalBeneficios())
        || (request.getBeneficiosRecebidos() != null && !request.getBeneficiosRecebidos().isEmpty());
  }

  private static boolean possuiDadosObservacoes(CadastroBeneficiarioCriacaoRequest request) {
    return Boolean.TRUE.equals(request.getAceiteLgpd())
        || request.getDataAceiteLgpd() != null
        || temValor(request.getObservacoes());
  }

  private static boolean possuiDadosCertidao(CadastroBeneficiarioCriacaoRequest request) {
    return temValor(request.getCertidaoTipo())
        || temValor(request.getCertidaoLivro())
        || temValor(request.getCertidaoFolha())
        || temValor(request.getCertidaoTermo())
        || temValor(request.getCertidaoCartorio())
        || temValor(request.getCertidaoMunicipio())
        || temValor(request.getCertidaoUf());
  }

  private static <T> T obterUltimo(List<T> registros, Function<T, LocalDateTime> getAtualizado) {
    if (registros == null || registros.isEmpty()) {
      return null;
    }

    return registros.stream().filter(Objects::nonNull).max(Comparator.comparing(getAtualizado)).orElse(null);
  }

  private static String limparTexto(String valor) {
    if (!temValor(valor)) {
      return null;
    }
    return valor.trim();
  }

  private static boolean temValor(String valor) {
    return valor != null && !valor.trim().isEmpty();
  }

  private static java.math.BigDecimal parseDecimal(String valor) {
    if (!temValor(valor)) {
      return null;
    }
    try {
      return new java.math.BigDecimal(valor.trim());
    } catch (NumberFormatException ex) {
      return null;
    }
  }

  private static String juntarLista(List<String> valores) {
    if (valores == null || valores.isEmpty()) {
      return null;
    }
    return valores.stream().filter(Objects::nonNull).map(String::trim).filter(CadastroBeneficiarioMapper::temValor).collect(Collectors.joining(";"));
  }

  private static List<String> separarLista(String valor) {
    if (!temValor(valor)) {
      return null;
    }
    String[] partes = valor.split(";");
    List<String> lista = new ArrayList<>();
    for (String parte : partes) {
      if (temValor(parte)) {
        lista.add(parte.trim());
      }
    }
    return lista.isEmpty() ? null : lista;
  }
}
