package br.com.g3.senhas.serviceimpl;

import br.com.g3.senhas.domain.SenhaConfig;
import br.com.g3.senhas.dto.SenhaConfigRequest;
import br.com.g3.senhas.dto.SenhaConfigResponse;
import br.com.g3.senhas.repository.SenhaConfigRepository;
import br.com.g3.senhas.service.SenhaConfigService;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SenhaConfigServiceImpl implements SenhaConfigService {
  private static final long CONFIG_ID = 1L;
  private static final String FRASE_PADRAO =
      "Beneficiário {beneficiario} dirija-se a {sala} para atendimento.";
  private static final String RSS_PADRAO =
      "https://www.gov.br/pt-br/noticias/assistencia-social/RSS";
  private static final int VELOCIDADE_PADRAO = 60;
  private static final String MODO_NOTICIAS_PADRAO = "RSS";
  private static final String NOTICIAS_MANUAIS_PADRAO = null;
  private static final int LIMITE_NOTICIAS_MANUAIS = 10;
  private static final int TAMANHO_MAXIMO_NOTICIA_MANUAL = 120;
  private static final int QUANTIDADE_ULTIMAS_CHAMADAS_PADRAO = 4;
  private static final String TITULO_PADRAO = "Chamada de senhas";
  private static final String DESCRICAO_PADRAO = "Controle de fila e chamada de beneficiarios.";

  private final SenhaConfigRepository repository;

  public SenhaConfigServiceImpl(SenhaConfigRepository repository) {
    this.repository = repository;
  }

  @Override
  @Transactional(readOnly = true)
  public SenhaConfigResponse obterConfiguracao() {
    return repository.findById(CONFIG_ID).map(this::toResponse).orElseGet(this::defaultResponse);
  }

  @Override
  @Transactional
  public SenhaConfigResponse atualizarConfiguracao(SenhaConfigRequest request) {
    String frase = request.getFraseFala();
    if (frase == null || frase.trim().isEmpty()) {
      throw new org.springframework.web.server.ResponseStatusException(
          org.springframework.http.HttpStatus.BAD_REQUEST, "Frase da chamada e obrigatoria.");
    }
    if (!frase.contains("{beneficiario}")) {
      throw new org.springframework.web.server.ResponseStatusException(
          org.springframework.http.HttpStatus.BAD_REQUEST, "A frase deve conter {beneficiario}.");
    }
    if (!frase.contains("{sala}")) {
      throw new org.springframework.web.server.ResponseStatusException(
          org.springframework.http.HttpStatus.BAD_REQUEST, "A frase deve conter {sala}.");
    }
    String modoNoticias = normalizarModoNoticias(request.getModoNoticias());
    if ("RSS".equals(modoNoticias) && (request.getRssUrl() == null || request.getRssUrl().trim().isEmpty())) {
      throw new org.springframework.web.server.ResponseStatusException(
          org.springframework.http.HttpStatus.BAD_REQUEST, "Feed de noticias (RSS) e obrigatorio.");
    }
    if ("MANUAL".equals(modoNoticias) && request.getNoticiasManuais() != null) {
      validarNoticiasManuais(request.getNoticiasManuais());
    }

    Integer quantidadeUltimasChamadas = request.getQuantidadeUltimasChamadas();
    if (quantidadeUltimasChamadas == null || quantidadeUltimasChamadas < 1 || quantidadeUltimasChamadas > 10) {
      throw new org.springframework.web.server.ResponseStatusException(
          org.springframework.http.HttpStatus.BAD_REQUEST,
          "Quantidade de ultimas chamadas deve ser entre 1 e 10.");
    }
    SenhaConfig config = repository.findById(CONFIG_ID).orElseGet(this::defaultEntity);
    config.setFraseFala(request.getFraseFala());
    config.setRssUrl(request.getRssUrl());
    config.setVelocidadeTicker(request.getVelocidadeTicker());
    config.setModoNoticias(modoNoticias);
    config.setNoticiasManuais(request.getNoticiasManuais());
    config.setQuantidadeUltimasChamadas(request.getQuantidadeUltimasChamadas());
    config.setUnidadePainelId(request.getUnidadePainelId());
    config.setTituloTela(request.getTituloTela());
    config.setDescricaoTela(request.getDescricaoTela());
    config.setAtualizadoEm(LocalDateTime.now());
    return toResponse(repository.save(config));
  }

  private SenhaConfigResponse toResponse(SenhaConfig config) {
    return new SenhaConfigResponse(
        config.getFraseFala(),
        config.getRssUrl(),
        config.getVelocidadeTicker(),
        config.getModoNoticias(),
        config.getNoticiasManuais(),
        config.getQuantidadeUltimasChamadas(),
        config.getUnidadePainelId(),
        config.getTituloTela(),
        config.getDescricaoTela());
  }

  private SenhaConfigResponse defaultResponse() {
    return new SenhaConfigResponse(
        FRASE_PADRAO,
        RSS_PADRAO,
        VELOCIDADE_PADRAO,
        MODO_NOTICIAS_PADRAO,
        NOTICIAS_MANUAIS_PADRAO,
        QUANTIDADE_ULTIMAS_CHAMADAS_PADRAO,
        null,
        TITULO_PADRAO,
        DESCRICAO_PADRAO);
  }

  private SenhaConfig defaultEntity() {
    SenhaConfig config = new SenhaConfig();
    config.setId(CONFIG_ID);
    config.setFraseFala(FRASE_PADRAO);
    config.setRssUrl(RSS_PADRAO);
    config.setVelocidadeTicker(VELOCIDADE_PADRAO);
    config.setModoNoticias(MODO_NOTICIAS_PADRAO);
    config.setNoticiasManuais(NOTICIAS_MANUAIS_PADRAO);
    config.setQuantidadeUltimasChamadas(QUANTIDADE_ULTIMAS_CHAMADAS_PADRAO);
    config.setUnidadePainelId(null);
    config.setTituloTela(TITULO_PADRAO);
    config.setDescricaoTela(DESCRICAO_PADRAO);
    config.setAtualizadoEm(LocalDateTime.now());
    return config;
  }

  private String normalizarModoNoticias(String modo) {
    if (modo == null || modo.trim().isEmpty()) {
      return MODO_NOTICIAS_PADRAO;
    }
    String normalizado = modo.trim().toUpperCase();
    if ("MANUAL".equals(normalizado)) {
      return "MANUAL";
    }
    return "RSS";
  }

  private void validarNoticiasManuais(String noticiasManuais) {
    String[] linhas = noticiasManuais.split("[\\n;]");
    int total = 0;
    for (String linha : linhas) {
      String texto = linha.trim();
      if (texto.isEmpty()) {
        continue;
      }
      total++;
      if (texto.length() > TAMANHO_MAXIMO_NOTICIA_MANUAL) {
        throw new org.springframework.web.server.ResponseStatusException(
            org.springframework.http.HttpStatus.BAD_REQUEST,
            "Cada noticia manual deve ter no maximo " + TAMANHO_MAXIMO_NOTICIA_MANUAL + " caracteres.");
      }
      if (total > LIMITE_NOTICIAS_MANUAIS) {
        throw new org.springframework.web.server.ResponseStatusException(
            org.springframework.http.HttpStatus.BAD_REQUEST,
            "Limite maximo de " + LIMITE_NOTICIAS_MANUAIS + " noticias manuais.");
      }
    }
  }
}
