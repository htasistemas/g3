package br.com.g3.ocorrenciacrianca.serviceimpl;

import br.com.g3.ocorrenciacrianca.domain.OcorrenciaCrianca;
import br.com.g3.ocorrenciacrianca.domain.OcorrenciaCriancaAnexo;
import br.com.g3.ocorrenciacrianca.dto.OcorrenciaCriancaAnexoRequest;
import br.com.g3.ocorrenciacrianca.dto.OcorrenciaCriancaAnexoResponse;
import br.com.g3.ocorrenciacrianca.dto.OcorrenciaCriancaRequest;
import br.com.g3.ocorrenciacrianca.dto.OcorrenciaCriancaResponse;
import br.com.g3.ocorrenciacrianca.repository.OcorrenciaCriancaAnexoRepository;
import br.com.g3.ocorrenciacrianca.repository.OcorrenciaCriancaRepository;
import br.com.g3.ocorrenciacrianca.service.OcorrenciaCriancaService;
import br.com.g3.relatorios.util.HtmlPdfRenderer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OcorrenciaCriancaServiceImpl implements OcorrenciaCriancaService {
  private static final int LIMITE_ANEXOS = 10;
  private static final int LIMITE_TAMANHO_ANEXO = 10 * 1024 * 1024;
  private static final DateTimeFormatter DATA_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  private final OcorrenciaCriancaRepository repository;
  private final OcorrenciaCriancaAnexoRepository anexoRepository;
  private final ObjectMapper objectMapper;

  public OcorrenciaCriancaServiceImpl(
      OcorrenciaCriancaRepository repository,
      OcorrenciaCriancaAnexoRepository anexoRepository,
      ObjectMapper objectMapper) {
    this.repository = repository;
    this.anexoRepository = anexoRepository;
    this.objectMapper = objectMapper;
  }

  @Override
  @Transactional
  public OcorrenciaCriancaResponse criar(OcorrenciaCriancaRequest request) {
    validarEncaminhamento(request);
    OcorrenciaCrianca ocorrencia = new OcorrenciaCrianca();
    aplicarRequest(ocorrencia, request);
    LocalDateTime agora = LocalDateTime.now();
    ocorrencia.setCriadoEm(agora);
    ocorrencia.setAtualizadoEm(agora);
    OcorrenciaCrianca salva = repository.salvar(ocorrencia);
    return mapResponse(buscarPorId(salva.getId()));
  }

  @Override
  @Transactional
  public OcorrenciaCriancaResponse atualizar(Long id, OcorrenciaCriancaRequest request) {
    validarEncaminhamento(request);
    OcorrenciaCrianca ocorrencia = buscarPorId(id);
    aplicarRequest(ocorrencia, request);
    ocorrencia.setAtualizadoEm(LocalDateTime.now());
    return mapResponse(repository.salvar(ocorrencia));
  }

  @Override
  @Transactional(readOnly = true)
  public OcorrenciaCriancaResponse buscar(Long id) {
    return mapResponse(buscarPorId(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<OcorrenciaCriancaResponse> listar() {
    List<OcorrenciaCriancaResponse> resposta = new ArrayList<>();
    for (OcorrenciaCrianca ocorrencia : repository.listar()) {
      resposta.add(mapResponse(ocorrencia));
    }
    return resposta;
  }

  @Override
  @Transactional
  public void remover(Long id) {
    OcorrenciaCrianca ocorrencia = buscarPorId(id);
    repository.remover(ocorrencia.getId());
  }

  @Override
  @Transactional(readOnly = true)
  public List<OcorrenciaCriancaAnexoResponse> listarAnexos(Long ocorrenciaId) {
    OcorrenciaCrianca ocorrencia = buscarPorId(ocorrenciaId);
    List<OcorrenciaCriancaAnexo> anexos =
        anexoRepository.listarPorOcorrenciaId(ocorrencia.getId());
    List<OcorrenciaCriancaAnexoResponse> resposta = new ArrayList<>();
    for (OcorrenciaCriancaAnexo anexo : anexos) {
      resposta.add(mapAnexo(anexo));
    }
    return resposta;
  }

  @Override
  @Transactional
  public OcorrenciaCriancaAnexoResponse adicionarAnexo(
      Long ocorrenciaId, OcorrenciaCriancaAnexoRequest request) {
    OcorrenciaCrianca ocorrencia = buscarPorId(ocorrenciaId);
    validarAnexo(ocorrencia.getId(), request);
    LocalDateTime agora = LocalDateTime.now();
    OcorrenciaCriancaAnexo anexo = new OcorrenciaCriancaAnexo();
    anexo.setOcorrenciaId(ocorrencia.getId());
    anexo.setNomeArquivo(request.getNomeArquivo());
    anexo.setTipoMime(request.getTipoMime());
    anexo.setConteudoBase64(request.getConteudoBase64());
    anexo.setOrdem(request.getOrdem());
    anexo.setCriadoEm(agora);
    anexo.setAtualizadoEm(agora);
    return mapAnexo(anexoRepository.salvar(anexo));
  }

  @Override
  @Transactional
  public void removerAnexo(Long ocorrenciaId, Long anexoId) {
    OcorrenciaCrianca ocorrencia = buscarPorId(ocorrenciaId);
    Optional<OcorrenciaCriancaAnexo> anexo = anexoRepository.buscarPorId(anexoId);
    if (!anexo.isPresent() || !anexo.get().getOcorrenciaId().equals(ocorrencia.getId())) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Anexo nao encontrado.");
    }
    anexoRepository.remover(anexoId);
  }

  @Override
  @Transactional(readOnly = true)
  public byte[] gerarPdfDenuncia(Long id) {
    OcorrenciaCrianca ocorrencia = buscarPorId(id);
    String html = gerarHtmlDenuncia(ocorrencia);
    return HtmlPdfRenderer.render(html);
  }

  @Override
  @Transactional(readOnly = true)
  public byte[] gerarPdfConselhoTutelar(Long id) {
    OcorrenciaCrianca ocorrencia = buscarPorId(id);
    String html = gerarHtmlConselhoTutelar(ocorrencia);
    return HtmlPdfRenderer.render(html);
  }

  private OcorrenciaCrianca buscarPorId(Long id) {
    Optional<OcorrenciaCrianca> ocorrencia = repository.buscarPorId(id);
    if (!ocorrencia.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ocorrencia nao encontrada.");
    }
    return ocorrencia.get();
  }

  private void aplicarRequest(OcorrenciaCrianca ocorrencia, OcorrenciaCriancaRequest request) {
    ocorrencia.setDataPreenchimento(request.getDataPreenchimento());
    ocorrencia.setLocalViolencia(request.getLocalViolencia());
    ocorrencia.setLocalViolenciaOutro(request.getLocalViolenciaOutro());
    ocorrencia.setViolenciaMotivadaPorJson(serializarLista(request.getViolenciaMotivadaPor()));
    ocorrencia.setViolenciaMotivadaOutro(request.getViolenciaMotivadaOutro());
    ocorrencia.setViolenciaPraticadaPorJson(serializarLista(request.getViolenciaPraticadaPor()));
    ocorrencia.setViolenciaPraticadaOutro(request.getViolenciaPraticadaOutro());
    ocorrencia.setOutrasViolacoesJson(serializarLista(request.getOutrasViolacoes()));
    ocorrencia.setVitimaNome(request.getVitimaNome());
    ocorrencia.setVitimaIdade(request.getVitimaIdade());
    ocorrencia.setVitimaRacaCor(request.getVitimaRacaCor());
    ocorrencia.setVitimaIdentidadeGenero(request.getVitimaIdentidadeGenero());
    ocorrencia.setVitimaOrientacaoSexual(request.getVitimaOrientacaoSexual());
    ocorrencia.setVitimaOrientacaoOutro(request.getVitimaOrientacaoOutro());
    ocorrencia.setVitimaEscolaridade(request.getVitimaEscolaridade());
    ocorrencia.setVitimaResponsavelTipo(request.getVitimaResponsavelTipo());
    ocorrencia.setVitimaResponsavelNome(request.getVitimaResponsavelNome());
    ocorrencia.setVitimaTelefoneResponsavel(request.getVitimaTelefoneResponsavel());
    ocorrencia.setVitimaEnderecoLogradouro(request.getVitimaEnderecoLogradouro());
    ocorrencia.setVitimaEnderecoComplemento(request.getVitimaEnderecoComplemento());
    ocorrencia.setVitimaEnderecoBairro(request.getVitimaEnderecoBairro());
    ocorrencia.setVitimaEnderecoMunicipio(request.getVitimaEnderecoMunicipio());
    ocorrencia.setAutorNome(request.getAutorNome());
    ocorrencia.setAutorIdade(request.getAutorIdade());
    ocorrencia.setAutorNaoConsta(request.getAutorNaoConsta());
    ocorrencia.setAutorParentesco(request.getAutorParentesco());
    ocorrencia.setAutorParentescoGrau(request.getAutorParentescoGrau());
    ocorrencia.setAutorResponsavelTipo(request.getAutorResponsavelTipo());
    ocorrencia.setAutorResponsavelNome(request.getAutorResponsavelNome());
    ocorrencia.setAutorResponsavelTelefone(request.getAutorResponsavelTelefone());
    ocorrencia.setAutorResponsavelNaoConsta(request.getAutorResponsavelNaoConsta());
    ocorrencia.setAutorEnderecoLogradouro(request.getAutorEnderecoLogradouro());
    ocorrencia.setAutorEnderecoComplemento(request.getAutorEnderecoComplemento());
    ocorrencia.setAutorEnderecoBairro(request.getAutorEnderecoBairro());
    ocorrencia.setAutorEnderecoMunicipio(request.getAutorEnderecoMunicipio());
    ocorrencia.setAutorEnderecoNaoConsta(request.getAutorEnderecoNaoConsta());
    ocorrencia.setTipificacaoViolenciaJson(serializarLista(request.getTipificacaoViolencia()));
    ocorrencia.setTipificacaoPsicologicaJson(serializarLista(request.getTipificacaoPsicologica()));
    ocorrencia.setTipificacaoSexualJson(serializarLista(request.getTipificacaoSexual()));
    ocorrencia.setViolenciaAutoprovocadaJson(serializarLista(request.getViolenciaAutoprovocada()));
    ocorrencia.setOutroTipoViolenciaDescricao(request.getOutroTipoViolenciaDescricao());
    ocorrencia.setResumoViolencia(request.getResumoViolencia());
    ocorrencia.setEncaminharConselho(request.getEncaminharConselho());
    ocorrencia.setEncaminharMotivo(request.getEncaminharMotivo());
    ocorrencia.setDataEnvioConselho(request.getDataEnvioConselho());
    ocorrencia.setDenunciaOrigemJson(serializarLista(request.getDenunciaOrigem()));
    ocorrencia.setDenunciaOrigemOutro(request.getDenunciaOrigemOutro());
  }

  private OcorrenciaCriancaResponse mapResponse(OcorrenciaCrianca ocorrencia) {
    OcorrenciaCriancaResponse response = new OcorrenciaCriancaResponse();
    response.setId(ocorrencia.getId());
    response.setDataPreenchimento(ocorrencia.getDataPreenchimento());
    response.setLocalViolencia(ocorrencia.getLocalViolencia());
    response.setLocalViolenciaOutro(ocorrencia.getLocalViolenciaOutro());
    response.setViolenciaMotivadaPor(desserializarLista(ocorrencia.getViolenciaMotivadaPorJson()));
    response.setViolenciaMotivadaOutro(ocorrencia.getViolenciaMotivadaOutro());
    response.setViolenciaPraticadaPor(desserializarLista(ocorrencia.getViolenciaPraticadaPorJson()));
    response.setViolenciaPraticadaOutro(ocorrencia.getViolenciaPraticadaOutro());
    response.setOutrasViolacoes(desserializarLista(ocorrencia.getOutrasViolacoesJson()));
    response.setVitimaNome(ocorrencia.getVitimaNome());
    response.setVitimaIdade(ocorrencia.getVitimaIdade());
    response.setVitimaRacaCor(ocorrencia.getVitimaRacaCor());
    response.setVitimaIdentidadeGenero(ocorrencia.getVitimaIdentidadeGenero());
    response.setVitimaOrientacaoSexual(ocorrencia.getVitimaOrientacaoSexual());
    response.setVitimaOrientacaoOutro(ocorrencia.getVitimaOrientacaoOutro());
    response.setVitimaEscolaridade(ocorrencia.getVitimaEscolaridade());
    response.setVitimaResponsavelTipo(ocorrencia.getVitimaResponsavelTipo());
    response.setVitimaResponsavelNome(ocorrencia.getVitimaResponsavelNome());
    response.setVitimaTelefoneResponsavel(ocorrencia.getVitimaTelefoneResponsavel());
    response.setVitimaEnderecoLogradouro(ocorrencia.getVitimaEnderecoLogradouro());
    response.setVitimaEnderecoComplemento(ocorrencia.getVitimaEnderecoComplemento());
    response.setVitimaEnderecoBairro(ocorrencia.getVitimaEnderecoBairro());
    response.setVitimaEnderecoMunicipio(ocorrencia.getVitimaEnderecoMunicipio());
    response.setAutorNome(ocorrencia.getAutorNome());
    response.setAutorIdade(ocorrencia.getAutorIdade());
    response.setAutorNaoConsta(ocorrencia.getAutorNaoConsta());
    response.setAutorParentesco(ocorrencia.getAutorParentesco());
    response.setAutorParentescoGrau(ocorrencia.getAutorParentescoGrau());
    response.setAutorResponsavelTipo(ocorrencia.getAutorResponsavelTipo());
    response.setAutorResponsavelNome(ocorrencia.getAutorResponsavelNome());
    response.setAutorResponsavelTelefone(ocorrencia.getAutorResponsavelTelefone());
    response.setAutorResponsavelNaoConsta(ocorrencia.getAutorResponsavelNaoConsta());
    response.setAutorEnderecoLogradouro(ocorrencia.getAutorEnderecoLogradouro());
    response.setAutorEnderecoComplemento(ocorrencia.getAutorEnderecoComplemento());
    response.setAutorEnderecoBairro(ocorrencia.getAutorEnderecoBairro());
    response.setAutorEnderecoMunicipio(ocorrencia.getAutorEnderecoMunicipio());
    response.setAutorEnderecoNaoConsta(ocorrencia.getAutorEnderecoNaoConsta());
    response.setTipificacaoViolencia(desserializarLista(ocorrencia.getTipificacaoViolenciaJson()));
    response.setTipificacaoPsicologica(
        desserializarLista(ocorrencia.getTipificacaoPsicologicaJson()));
    response.setTipificacaoSexual(desserializarLista(ocorrencia.getTipificacaoSexualJson()));
    response.setViolenciaAutoprovocada(
        desserializarLista(ocorrencia.getViolenciaAutoprovocadaJson()));
    response.setOutroTipoViolenciaDescricao(ocorrencia.getOutroTipoViolenciaDescricao());
    response.setResumoViolencia(ocorrencia.getResumoViolencia());
    response.setEncaminharConselho(ocorrencia.getEncaminharConselho());
    response.setEncaminharMotivo(ocorrencia.getEncaminharMotivo());
    response.setDataEnvioConselho(ocorrencia.getDataEnvioConselho());
    response.setDenunciaOrigem(desserializarLista(ocorrencia.getDenunciaOrigemJson()));
    response.setDenunciaOrigemOutro(ocorrencia.getDenunciaOrigemOutro());
    response.setCriadoEm(ocorrencia.getCriadoEm());
    response.setAtualizadoEm(ocorrencia.getAtualizadoEm());
    return response;
  }

  private OcorrenciaCriancaAnexoResponse mapAnexo(OcorrenciaCriancaAnexo anexo) {
    OcorrenciaCriancaAnexoResponse response = new OcorrenciaCriancaAnexoResponse();
    response.setId(anexo.getId());
    response.setOcorrenciaId(anexo.getOcorrenciaId());
    response.setNomeArquivo(anexo.getNomeArquivo());
    response.setTipoMime(anexo.getTipoMime());
    response.setConteudoBase64(anexo.getConteudoBase64());
    response.setOrdem(anexo.getOrdem());
    response.setCriadoEm(anexo.getCriadoEm());
    response.setAtualizadoEm(anexo.getAtualizadoEm());
    return response;
  }

  private void validarEncaminhamento(OcorrenciaCriancaRequest request) {
    if (request.getEncaminharConselho() == null) {
      return;
    }
    if (Boolean.TRUE.equals(request.getEncaminharConselho())) {
      if (request.getDataEnvioConselho() == null) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Informe a data de envio da ficha ao Conselho Tutelar.");
      }
    } else if (request.getEncaminharMotivo() == null
        || request.getEncaminharMotivo().trim().isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Informe o motivo do nao encaminhamento.");
    }
  }

  private void validarAnexo(Long ocorrenciaId, OcorrenciaCriancaAnexoRequest request) {
    List<OcorrenciaCriancaAnexo> existentes =
        anexoRepository.listarPorOcorrenciaId(ocorrenciaId);
    if (existentes.size() >= LIMITE_ANEXOS) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Limite maximo de 10 anexos por ocorrencia.");
    }
    String tipo = request.getTipoMime() == null ? "" : request.getTipoMime().toLowerCase();
    if (!tipo.equals("application/pdf")
        && !tipo.equals("image/jpeg")
        && !tipo.equals("image/png")) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Tipo de anexo invalido. Use PDF, JPG ou PNG.");
    }
    byte[] bytes;
    try {
      bytes = Base64.getDecoder().decode(request.getConteudoBase64());
    } catch (IllegalArgumentException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conteudo do anexo invalido.");
    }
    if (bytes.length > LIMITE_TAMANHO_ANEXO) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Anexo excede o limite de 10MB.");
    }
  }

  private String serializarLista(List<String> valores) {
    if (valores == null || valores.isEmpty()) {
      return null;
    }
    try {
      return objectMapper.writeValueAsString(valores);
    } catch (Exception ex) {
      return null;
    }
  }

  private List<String> desserializarLista(String json) {
    if (json == null || json.trim().isEmpty()) {
      return Collections.emptyList();
    }
    try {
      return objectMapper.readValue(json, new TypeReference<List<String>>() {});
    } catch (Exception ex) {
      return Collections.emptyList();
    }
  }

  private String checkbox(boolean marcado) {
    return marcado ? "(X)" : "( )";
  }

  private String radio(String atual, String opcao) {
    if (atual == null || opcao == null) {
      return checkbox(false);
    }
    return checkbox(atual.equalsIgnoreCase(opcao));
  }

  private boolean contem(List<String> valores, String valor) {
    if (valores == null || valores.isEmpty()) {
      return false;
    }
    for (String item : valores) {
      if (item != null && item.equalsIgnoreCase(valor)) {
        return true;
      }
    }
    return false;
  }

  private String valorOuLinha(String valor) {
    return valor == null || valor.trim().isEmpty() ? "____________________" : valor;
  }

  private String formatarData(LocalDate data) {
    if (data == null) {
      return "__/__/____";
    }
    return DATA_BR.format(data);
  }

  private String gerarHtmlDenuncia(OcorrenciaCrianca ocorrencia) {
    List<String> violenciaMotivada = desserializarLista(ocorrencia.getViolenciaMotivadaPorJson());
    List<String> violenciaPraticada = desserializarLista(ocorrencia.getViolenciaPraticadaPorJson());
    List<String> outrasViolacoes = desserializarLista(ocorrencia.getOutrasViolacoesJson());
    List<String> tipificacao = desserializarLista(ocorrencia.getTipificacaoViolenciaJson());
    List<String> tipificacaoPsicologica =
        desserializarLista(ocorrencia.getTipificacaoPsicologicaJson());
    List<String> tipificacaoSexual = desserializarLista(ocorrencia.getTipificacaoSexualJson());
    List<String> violenciaAutoprovocada =
        desserializarLista(ocorrencia.getViolenciaAutoprovocadaJson());
    List<String> denunciaOrigem = desserializarLista(ocorrencia.getDenunciaOrigemJson());

    StringBuilder html = new StringBuilder();
    html.append("<html><head><meta charset=\"")
        .append(StandardCharsets.UTF_8)
        .append("\">");
    html.append("<style>");
    html.append(
        "@page { size: A4; margin: 20mm 20mm 20mm 20mm; }"
            + "body { font-family: Arial, sans-serif; font-size: 12pt; color: #000; }"
            + ".titulo { text-align: center; font-weight: bold; margin-bottom: 8px; }"
            + ".linha { margin: 4px 0; }"
            + ".secao { margin-top: 10px; }"
            + ".campo { display: inline-block; min-width: 120px; }"
            + ".bloco { margin-left: 12px; }"
            + ".observacao { margin-top: 12px; font-size: 10pt; }");
    html.append("</style></head><body>");
    html.append("<div class=\"titulo\">FORMULARIO PARA REGISTRO DE SITUACOES DE VIOLENCIA CONTRA A CRIANCA E O ADOLESCENTE</div>");
    html.append("<div class=\"linha\">Data de preenchimento do formulario de registro: ")
        .append(formatarData(ocorrencia.getDataPreenchimento()))
        .append("</div>");

    html.append("<div class=\"secao\"><strong>1. VIOLENCIA PRATICADA CONTRA CRIANCA E ADOLESCENTE:</strong></div>");
    html.append("<div class=\"linha\">")
        .append(radio(ocorrencia.getLocalViolencia(), "Na escola"))
        .append(" Na escola ")
        .append(radio(ocorrencia.getLocalViolencia(), "No ambito familiar"))
        .append(" No ambito familiar ")
        .append(radio(ocorrencia.getLocalViolencia(), "Outros espacos"))
        .append(" Outros espacos: ")
        .append(valorOuLinha(ocorrencia.getLocalViolenciaOutro()))
        .append("</div>");

    html.append("<div class=\"secao\"><strong>2. DADOS DA VITIMA:</strong></div>");
    html.append("<div class=\"linha\">2.1 Nome: ")
        .append(valorOuLinha(ocorrencia.getVitimaNome()))
        .append("  Idade: ")
        .append(ocorrencia.getVitimaIdade() == null ? "__" : ocorrencia.getVitimaIdade())
        .append("</div>");
    html.append("<div class=\"linha\">2.2 Raca/cor: ")
        .append(radio(ocorrencia.getVitimaRacaCor(), "Branca"))
        .append(" Branca ")
        .append(radio(ocorrencia.getVitimaRacaCor(), "Preta"))
        .append(" Preta ")
        .append(radio(ocorrencia.getVitimaRacaCor(), "Parda"))
        .append(" Parda ")
        .append(radio(ocorrencia.getVitimaRacaCor(), "Indigena"))
        .append(" Indigena ")
        .append(radio(ocorrencia.getVitimaRacaCor(), "Amarela"))
        .append(" Amarela</div>");
    html.append("<div class=\"linha\">2.3 Identidade de genero: ")
        .append(radio(ocorrencia.getVitimaIdentidadeGenero(), "Masculino Cisgenero"))
        .append(" Masculino Cisgenero ")
        .append(radio(ocorrencia.getVitimaIdentidadeGenero(), "Feminino Cisgenero"))
        .append(" Feminino Cisgenero ")
        .append(radio(ocorrencia.getVitimaIdentidadeGenero(), "Masculino Transexual"))
        .append(" Masculino Transexual ")
        .append(radio(ocorrencia.getVitimaIdentidadeGenero(), "Feminino Transexual"))
        .append(" Feminino Transexual ")
        .append(radio(ocorrencia.getVitimaIdentidadeGenero(), "Nao binario"))
        .append(" Nao binario</div>");
    html.append("<div class=\"linha\">2.4 Orientacao sexual: ")
        .append(radio(ocorrencia.getVitimaOrientacaoSexual(), "Heterossexual"))
        .append(" Heterossexual ")
        .append(radio(ocorrencia.getVitimaOrientacaoSexual(), "Homossexual"))
        .append(" Homossexual ")
        .append(radio(ocorrencia.getVitimaOrientacaoSexual(), "Bissexual"))
        .append(" Bissexual ")
        .append(radio(ocorrencia.getVitimaOrientacaoSexual(), "Pansexual"))
        .append(" Pansexual ")
        .append(radio(ocorrencia.getVitimaOrientacaoSexual(), "Assexual"))
        .append(" Assexual ")
        .append(radio(ocorrencia.getVitimaOrientacaoSexual(), "Outro"))
        .append(" Outro: ")
        .append(valorOuLinha(ocorrencia.getVitimaOrientacaoOutro()))
        .append("</div>");
    html.append("<div class=\"linha\">2.5 Escolaridade: ")
        .append(valorOuLinha(ocorrencia.getVitimaEscolaridade()))
        .append("</div>");
    html.append("<div class=\"linha\">2.6 Nome do Responsavel: ")
        .append(radio(ocorrencia.getVitimaResponsavelTipo(), "Mae"))
        .append(" Mae: ")
        .append(valorOuLinha(ocorrencia.getVitimaResponsavelNome()))
        .append(" ")
        .append(radio(ocorrencia.getVitimaResponsavelTipo(), "Pai"))
        .append(" Pai: ")
        .append(valorOuLinha(ocorrencia.getVitimaResponsavelNome()))
        .append(" ")
        .append(radio(ocorrencia.getVitimaResponsavelTipo(), "Outro"))
        .append(" Outro: ")
        .append(valorOuLinha(ocorrencia.getVitimaResponsavelNome()))
        .append("</div>");
    html.append("<div class=\"linha\">2.7 Telefone do Responsavel: ")
        .append(valorOuLinha(ocorrencia.getVitimaTelefoneResponsavel()))
        .append("</div>");
    html.append("<div class=\"linha\">2.8 Dados da residencia da vitima:</div>");
    html.append("<div class=\"linha bloco\">Logradouro: ")
        .append(valorOuLinha(ocorrencia.getVitimaEnderecoLogradouro()))
        .append(" Complemento: ")
        .append(valorOuLinha(ocorrencia.getVitimaEnderecoComplemento()))
        .append("</div>");
    html.append("<div class=\"linha bloco\">Bairro: ")
        .append(valorOuLinha(ocorrencia.getVitimaEnderecoBairro()))
        .append(" Municipio: ")
        .append(valorOuLinha(ocorrencia.getVitimaEnderecoMunicipio()))
        .append("</div>");

    html.append("<div class=\"secao\"><strong>3. DADOS DO(A) POSSIVEL AUTOR(A) DA VIOLENCIA:</strong></div>");
    html.append("<div class=\"linha\">3.1 Identificacao: Nome: ")
        .append(valorOuLinha(ocorrencia.getAutorNome()))
        .append(" Idade: ")
        .append(ocorrencia.getAutorIdade() == null ? "__" : ocorrencia.getAutorIdade())
        .append(" ")
        .append(checkbox(Boolean.TRUE.equals(ocorrencia.getAutorNaoConsta())))
        .append(" Nao consta</div>");
    html.append("<div class=\"linha\">3.2 Possui vinculo de parentesco? ")
        .append(radio(ocorrencia.getAutorParentesco(), "Sim"))
        .append(" Sim ")
        .append(radio(ocorrencia.getAutorParentesco(), "Nao"))
        .append(" Nao ")
        .append(radio(ocorrencia.getAutorParentesco(), "Nao consta"))
        .append(" Nao consta</div>");
    html.append("<div class=\"linha bloco\">3.2.1 Grau de parentesco: ")
        .append(valorOuLinha(ocorrencia.getAutorParentescoGrau()))
        .append("</div>");
    html.append("<div class=\"linha\">3.3 Nome do responsavel (quando crianca/adolescente): ")
        .append(radio(ocorrencia.getAutorResponsavelTipo(), "Mae"))
        .append(" Mae: ")
        .append(valorOuLinha(ocorrencia.getAutorResponsavelNome()))
        .append(" ")
        .append(radio(ocorrencia.getAutorResponsavelTipo(), "Outro"))
        .append(" Outro: ")
        .append(valorOuLinha(ocorrencia.getAutorResponsavelNome()))
        .append(" Telefone: ")
        .append(valorOuLinha(ocorrencia.getAutorResponsavelTelefone()))
        .append(" ")
        .append(checkbox(Boolean.TRUE.equals(ocorrencia.getAutorResponsavelNaoConsta())))
        .append(" Nao consta</div>");
    html.append("<div class=\"linha\">3.4 Dados da residencia do(a) possivel autor(a):</div>");
    html.append("<div class=\"linha bloco\">Logradouro: ")
        .append(valorOuLinha(ocorrencia.getAutorEnderecoLogradouro()))
        .append(" Complemento: ")
        .append(valorOuLinha(ocorrencia.getAutorEnderecoComplemento()))
        .append("</div>");
    html.append("<div class=\"linha bloco\">Bairro: ")
        .append(valorOuLinha(ocorrencia.getAutorEnderecoBairro()))
        .append(" Municipio: ")
        .append(valorOuLinha(ocorrencia.getAutorEnderecoMunicipio()))
        .append(" ")
        .append(checkbox(Boolean.TRUE.equals(ocorrencia.getAutorEnderecoNaoConsta())))
        .append(" Nao consta</div>");

    html.append("<div class=\"secao\"><strong>4. TIPIFICACAO DA VIOLENCIA CONTRA CRIANCA E ADOLESCENTE:</strong></div>");
    html.append("<div class=\"linha\">")
        .append(checkbox(contem(tipificacao, "Violencia fisica")))
        .append(" Violencia fisica</div>");
    html.append("<div class=\"linha\">")
        .append(checkbox(contem(tipificacao, "Violencia psicologica")))
        .append(" Violencia psicologica</div>");
    html.append("<div class=\"linha bloco\">")
        .append(checkbox(contem(tipificacaoPsicologica, "ameaca")))
        .append(" ameaca ")
        .append(checkbox(contem(tipificacaoPsicologica, "constrangimento")))
        .append(" constrangimento ")
        .append(checkbox(contem(tipificacaoPsicologica, "humilhacao")))
        .append(" humilhacao ")
        .append(checkbox(contem(tipificacaoPsicologica, "manipulacao")))
        .append(" manipulacao ")
        .append(checkbox(contem(tipificacaoPsicologica, "isolamento")))
        .append(" isolamento</div>");
    html.append("<div class=\"linha bloco\">")
        .append(checkbox(contem(tipificacaoPsicologica, "agressao verbal e xingamento")))
        .append(" agressao verbal e xingamento ")
        .append(checkbox(contem(tipificacaoPsicologica, "bullying")))
        .append(" bullying ")
        .append(checkbox(contem(tipificacaoPsicologica, "alienacao parental")))
        .append(" alienacao parental</div>");
    html.append("<div class=\"linha\">")
        .append(checkbox(
            contem(
                tipificacao,
                "Exposicao da crianca/adolescente a crime violento contra membro da familia ou rede de apoio")))
        .append(
            " Exposicao da crianca/adolescente a crime violento contra membro da familia ou rede de apoio</div>");
    html.append("<div class=\"linha\">")
        .append(checkbox(contem(tipificacao, "Violencia sexual")))
        .append(" Violencia sexual</div>");
    html.append("<div class=\"linha bloco\">")
        .append(checkbox(contem(tipificacaoSexual, "abuso sexual")))
        .append(" abuso sexual ")
        .append(checkbox(contem(tipificacaoSexual, "exploracao sexual")))
        .append(" exploracao sexual ")
        .append(checkbox(contem(tipificacaoSexual, "trafico de pessoas")))
        .append(" trafico de pessoas ")
        .append(checkbox(contem(tipificacaoSexual, "violencia mediada por TICS")))
        .append(" violencia mediada por TICS</div>");
    html.append("<div class=\"linha\">")
        .append(checkbox(contem(tipificacao, "Negligencia")))
        .append(" Negligencia</div>");
    html.append("<div class=\"linha\">")
        .append(checkbox(contem(tipificacao, "Maus tratos")))
        .append(" Maus tratos</div>");
    html.append("<div class=\"linha\">")
        .append(checkbox(contem(tipificacao, "Violencia institucional")))
        .append(" Violencia institucional</div>");

    html.append("<div class=\"secao\"><strong>5. TIPIFICACAO DA VIOLENCIA AUTOPROVOCADA:</strong></div>");
    html.append("<div class=\"linha\">")
        .append(checkbox(contem(violenciaAutoprovocada, "Suicidio consumado")))
        .append(" Suicidio consumado ")
        .append(checkbox(contem(violenciaAutoprovocada, "Tentativa de suicidio")))
        .append(" Tentativa de suicidio ")
        .append(checkbox(contem(violenciaAutoprovocada, "Automutilacao")))
        .append(" Automutilacao ")
        .append(checkbox(contem(violenciaAutoprovocada, "Ideacao suicida")))
        .append(" Ideacao suicida</div>");

    html.append("<div class=\"secao\"><strong>6. OUTRO TIPO DE VIOLENCIA (descreva):</strong></div>");
    html.append("<div class=\"linha\">")
        .append(valorOuLinha(ocorrencia.getOutroTipoViolenciaDescricao()))
        .append("</div>");

    html.append("<div class=\"secao\"><strong>7. VIOLENCIA MOTIVADA POR:</strong></div>");
    html.append("<div class=\"linha\">")
        .append(checkbox(contem(violenciaMotivada, "Sexismo")))
        .append(" Sexismo ")
        .append(checkbox(contem(violenciaMotivada, "LGBTfobia")))
        .append(" LGBTfobia ")
        .append(checkbox(contem(violenciaMotivada, "Racismo")))
        .append(" Racismo ")
        .append(checkbox(contem(violenciaMotivada, "Intolerancia religiosa")))
        .append(" Intolerancia religiosa ")
        .append(checkbox(contem(violenciaMotivada, "Xenofobia")))
        .append(" Xenofobia ")
        .append(checkbox(contem(violenciaMotivada, "Conflito geracional")))
        .append(" Conflito geracional</div>");
    html.append("<div class=\"linha\">")
        .append(checkbox(contem(violenciaMotivada, "Capacitismo")))
        .append(" Capacitismo ")
        .append(checkbox(contem(violenciaMotivada, "Condicao economica")))
        .append(" Condicao economica ")
        .append(checkbox(contem(violenciaMotivada, "Outros")))
        .append(" Outros: ")
        .append(valorOuLinha(ocorrencia.getViolenciaMotivadaOutro()))
        .append("</div>");

    html.append("<div class=\"secao\"><strong>8. VIOLENCIA PRATICADA POR:</strong></div>");
    html.append("<div class=\"linha\">")
        .append(checkbox(contem(violenciaPraticada, "crianca")))
        .append(" crianca ")
        .append(checkbox(contem(violenciaPraticada, "adolescente")))
        .append(" adolescente ")
        .append(checkbox(contem(violenciaPraticada, "pai")))
        .append(" pai ")
        .append(checkbox(contem(violenciaPraticada, "mae")))
        .append(" mae ")
        .append(checkbox(contem(violenciaPraticada, "responsavel")))
        .append(" responsavel ")
        .append(checkbox(contem(violenciaPraticada, "professor/a")))
        .append(" professor/a ")
        .append(checkbox(contem(violenciaPraticada, "gestor/a")))
        .append(" gestor/a ")
        .append(checkbox(contem(violenciaPraticada, "funcionario")))
        .append(" funcionario</div>");
    html.append("<div class=\"linha\">")
        .append(checkbox(contem(violenciaPraticada, "outro")))
        .append(" outro: Informe ")
        .append(valorOuLinha(ocorrencia.getViolenciaPraticadaOutro()))
        .append("</div>");

    html.append("<div class=\"secao\"><strong>9. RESUMO DA VIOLENCIA (OU SUSPEITA DA VIOLENCIA) PRATICADA:</strong></div>");
    html.append("<div class=\"linha\">")
        .append(valorOuLinha(ocorrencia.getResumoViolencia()))
        .append("</div>");

    html.append("<div class=\"secao\"><strong>10. OUTRAS VIOLACOES DOS DIREITOS DA CRIANCA E DO ADOLESCENTE:</strong></div>");
    html.append("<div class=\"linha\">")
        .append(checkbox(contem(outrasViolacoes, "Abandono escolar")))
        .append(" Abandono escolar ")
        .append(checkbox(contem(outrasViolacoes, "Evasao escolar")))
        .append(" Evasao escolar ")
        .append(checkbox(contem(outrasViolacoes, "Gravidez na adolescencia")))
        .append(" Gravidez na adolescencia ")
        .append(checkbox(contem(outrasViolacoes, "Trabalho Infantil")))
        .append(" Trabalho Infantil</div>");

    html.append("<div class=\"secao\"><strong>11. ENCAMINHAMENTO REALIZADO AO CONSELHO TUTELAR:</strong></div>");
    html.append("<div class=\"linha\">")
        .append(checkbox(Boolean.TRUE.equals(ocorrencia.getEncaminharConselho())))
        .append(" SIM (gerar ficha de notificacao) ")
        .append(checkbox(Boolean.FALSE.equals(ocorrencia.getEncaminharConselho())))
        .append(" NAO. Qual o motivo: ")
        .append(valorOuLinha(ocorrencia.getEncaminharMotivo()))
        .append("</div>");

    html.append("<div class=\"secao\"><strong>12. COMO A DENUNCIA CHEGOU A ADRA:</strong></div>");
    html.append("<div class=\"linha\">")
        .append(checkbox(contem(denunciaOrigem, "Denuncia espontanea")))
        .append(" Denuncia espontanea ")
        .append(checkbox(contem(denunciaOrigem, "Suspeita por observacao")))
        .append(" Suspeita por observacao ")
        .append(checkbox(contem(denunciaOrigem, "Relato de outros alunos")))
        .append(" Relato de outros alunos</div>");
    html.append("<div class=\"linha\">")
        .append(checkbox(contem(denunciaOrigem, "Familiares")))
        .append(" Familiares ")
        .append(checkbox(contem(denunciaOrigem, "Denuncia anonima")))
        .append(" Denuncia anonima ")
        .append(checkbox(contem(denunciaOrigem, "Comunidade")))
        .append(" Comunidade ")
        .append(checkbox(contem(denunciaOrigem, "Outro")))
        .append(" Outro: ")
        .append(valorOuLinha(ocorrencia.getDenunciaOrigemOutro()))
        .append("</div>");

    html.append(
        "<div class=\"secao\"><strong>13. DATA DE ENVIO DA FICHA DE NOTIFICACAO AO CONSELHO TUTELAR:</strong></div>");
    html.append("<div class=\"linha\">Data: ")
        .append(formatarData(ocorrencia.getDataEnvioConselho()))
        .append("</div>");

    html.append("<div class=\"observacao\">Observacao: uso restrito de imagens, preservacao da privacidade e protecao das vitimas. ");
    html.append("Referencia legal: Art. 13 da Lei 13.431/2017.</div>");
    html.append("</body></html>");
    return html.toString();
  }

  private String gerarHtmlConselhoTutelar(OcorrenciaCrianca ocorrencia) {
    List<String> tipificacao = desserializarLista(ocorrencia.getTipificacaoViolenciaJson());
    List<String> denunciaOrigem = desserializarLista(ocorrencia.getDenunciaOrigemJson());

    StringBuilder html = new StringBuilder();
    html.append("<html><head><meta charset=\"")
        .append(StandardCharsets.UTF_8)
        .append("\">");
    html.append("<style>");
    html.append(
        "@page { size: A4; margin: 20mm 20mm 20mm 20mm; }"
            + "body { font-family: Arial, sans-serif; font-size: 12pt; color: #000; }"
            + ".titulo { text-align: center; font-weight: bold; margin-bottom: 8px; }"
            + ".linha { margin: 4px 0; }"
            + ".secao { margin-top: 10px; }"
            + ".bloco { margin-left: 12px; }"
            + ".assinaturas { margin-top: 18px; }"
            + ".linha-assinatura { margin-top: 16px; border-top: 1px solid #000; width: 60%; }");
    html.append("</style></head><body>");
    html.append("<div class=\"titulo\">FICHA DE NOTIFICACAO AO CONSELHO TUTELAR</div>");

    html.append("<div class=\"secao\"><strong>A) Identificacao da Crianca/Adolescente</strong></div>");
    html.append("<div class=\"linha\">Nome: ")
        .append(valorOuLinha(ocorrencia.getVitimaNome()))
        .append(" Idade: ")
        .append(ocorrencia.getVitimaIdade() == null ? "__" : ocorrencia.getVitimaIdade())
        .append("</div>");
    html.append("<div class=\"linha\">Endereco: ")
        .append(valorOuLinha(ocorrencia.getVitimaEnderecoLogradouro()))
        .append(" Municipio: ")
        .append(valorOuLinha(ocorrencia.getVitimaEnderecoMunicipio()))
        .append("</div>");

    html.append("<div class=\"secao\"><strong>B) Caracterizacao da Violencia</strong></div>");
    html.append("<div class=\"linha\">Tipos: ")
        .append(checkbox(contem(tipificacao, "Violencia fisica")))
        .append(" Fisica ")
        .append(checkbox(contem(tipificacao, "Violencia psicologica")))
        .append(" Psicologica ")
        .append(checkbox(contem(tipificacao, "Violencia sexual")))
        .append(" Sexual ")
        .append(checkbox(contem(tipificacao, "Negligencia")))
        .append(" Negligencia ")
        .append(checkbox(contem(tipificacao, "Maus tratos")))
        .append(" Maus-tratos ")
        .append(checkbox(contem(tipificacao, "Violencia institucional")))
        .append(" Institucional ")
        .append(checkbox(contem(tipificacao, "Outro")))
        .append(" Outro: ")
        .append(valorOuLinha(ocorrencia.getOutroTipoViolenciaDescricao()))
        .append("</div>");
    html.append("<div class=\"linha\">Local: ")
        .append(radio(ocorrencia.getLocalViolencia(), "Na escola"))
        .append(" Escola ")
        .append(radio(ocorrencia.getLocalViolencia(), "No ambito familiar"))
        .append(" Ambito familiar ")
        .append(radio(ocorrencia.getLocalViolencia(), "Outros espacos"))
        .append(" Outros: ")
        .append(valorOuLinha(ocorrencia.getLocalViolenciaOutro()))
        .append("</div>");

    html.append("<div class=\"secao\"><strong>C) Suspeito/Autor</strong></div>");
    html.append("<div class=\"linha\">Nome: ")
        .append(valorOuLinha(ocorrencia.getAutorNome()))
        .append(" Parentesco: ")
        .append(valorOuLinha(ocorrencia.getAutorParentescoGrau()))
        .append(" ")
        .append(checkbox(Boolean.TRUE.equals(ocorrencia.getAutorNaoConsta())))
        .append(" Nao consta</div>");

    html.append("<div class=\"secao\"><strong>D) Origem da denuncia</strong></div>");
    html.append("<div class=\"linha\">")
        .append(checkbox(contem(denunciaOrigem, "Denuncia espontanea")))
        .append(" Espontanea ")
        .append(checkbox(contem(denunciaOrigem, "Suspeita por observacao")))
        .append(" Observacao ")
        .append(checkbox(contem(denunciaOrigem, "Relato de outros alunos")))
        .append(" Relato de alunos ")
        .append(checkbox(contem(denunciaOrigem, "Familiares")))
        .append(" Familiares ")
        .append(checkbox(contem(denunciaOrigem, "Denuncia anonima")))
        .append(" Anonima ")
        .append(checkbox(contem(denunciaOrigem, "Comunidade")))
        .append(" Comunidade ")
        .append(checkbox(contem(denunciaOrigem, "Outro")))
        .append(" Outro: ")
        .append(valorOuLinha(ocorrencia.getDenunciaOrigemOutro()))
        .append("</div>");

    html.append("<div class=\"secao\"><strong>E) Relato resumido</strong></div>");
    html.append("<div class=\"linha\">")
        .append(valorOuLinha(ocorrencia.getResumoViolencia()))
        .append("</div>");

    html.append("<div class=\"secao\"><strong>F) Encaminhamento</strong></div>");
    html.append("<div class=\"linha\">")
        .append(checkbox(Boolean.TRUE.equals(ocorrencia.getEncaminharConselho())))
        .append(" Encaminhado ao Conselho Tutelar</div>");
    html.append("<div class=\"linha\">Data de envio: ")
        .append(formatarData(ocorrencia.getDataEnvioConselho()))
        .append("</div>");

    html.append("<div class=\"secao\"><strong>G) Unidade notificadora / Responsavel</strong></div>");
    html.append("<div class=\"linha\">Unidade: ____________________  Responsavel: ____________________</div>");
    html.append("<div class=\"linha\">Data: ")
        .append(formatarData(LocalDate.now()))
        .append("</div>");
    html.append("<div class=\"assinaturas\">Assinaturas:</div>");
    html.append("<div class=\"linha-assinatura\"></div>");
    html.append("<div class=\"linha-assinatura\"></div>");

    html.append("</body></html>");
    return html.toString();
  }
}
