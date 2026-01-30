package br.com.g3.noticiasassistencia.service;

import br.com.g3.noticiasassistencia.dto.NoticiaAssistenciaResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@Service
public class NoticiasAssistenciaService {
  private static final List<String> PALAVRAS_CHAVE =
      List.of(
          "assistencia social",
          "assistência social",
          "cidadania",
          "desenvolvimento social",
          "bolsa familia",
          "bolsa família",
          "cadunico",
          "cadúnico",
          "bpc",
          "suas",
          "proteção social",
          "protecao social");

  private final RestTemplate restTemplate;
  private final String rssUrl;
  private final String userAgent;
  private final Duration cacheTtl;

  private List<NoticiaAssistenciaResponse> cache = Collections.emptyList();
  private LocalDateTime cacheAtualizadoEm;

  public NoticiasAssistenciaService(
      @Value("${app.noticias-assistencia.rss-url:https://www.gov.br/pt-br/noticias/assistencia-social/RSS}")
          String rssUrl,
      @Value("${app.noticias-assistencia.user-agent:g3-noticias}") String userAgent,
      @Value("${app.noticias-assistencia.cache-minutos:10}") long cacheMinutos) {
    this.restTemplate = new RestTemplate();
    this.rssUrl = rssUrl;
    this.userAgent = userAgent;
    this.cacheTtl = Duration.ofMinutes(Math.max(1, cacheMinutos));
  }

  public List<NoticiaAssistenciaResponse> listarNoticias(int limite, String rssCustomizado) {
    if (cacheAtualizadoEm != null && LocalDateTime.now().isBefore(cacheAtualizadoEm.plus(cacheTtl))) {
      return limitar(cache, limite);
    }

    String url = rssCustomizado != null && !rssCustomizado.trim().isEmpty() ? rssCustomizado.trim() : rssUrl;
    List<NoticiaAssistenciaResponse> noticias = carregarNoticias(url);
    if (!noticias.isEmpty()) {
      cache = noticias;
      cacheAtualizadoEm = LocalDateTime.now();
    }
    return limitar(cache, limite);
  }

  private List<NoticiaAssistenciaResponse> carregarNoticias(String url) {
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.set(HttpHeaders.USER_AGENT, userAgent);
      ResponseEntity<String> response =
          restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
      String body = response.getBody();
      if (body == null || body.trim().isEmpty()) {
        return Collections.emptyList();
      }

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
      factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
      factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
      Document document = factory.newDocumentBuilder().parse(new java.io.ByteArrayInputStream(body.getBytes()));

      NodeList itens = document.getElementsByTagName("item");
      List<NoticiaAssistenciaResponse> todas = new ArrayList<>();
      List<NoticiaAssistenciaResponse> filtradas = new ArrayList<>();
      for (int i = 0; i < itens.getLength(); i++) {
        Element item = (Element) itens.item(i);
        String titulo = texto(item, "title").orElse("");
        String link = texto(item, "link").orElse("");
        String descricao = texto(item, "description").orElse("");
        List<String> categorias = categorias(item);
        LocalDateTime publicadoEm = parseData(texto(item, "pubDate").orElse(null));

        if (isIndice(titulo)) {
          continue;
        }

        NoticiaAssistenciaResponse noticia =
            new NoticiaAssistenciaResponse(titulo, link, publicadoEm);
        todas.add(noticia);

        if (relevante(titulo, descricao, categorias)) {
          filtradas.add(noticia);
        }
      }

      if (!filtradas.isEmpty()) {
        return filtradas;
      }
      return todas;
    } catch (Exception ex) {
      return Collections.emptyList();
    }
  }

  private Optional<String> texto(Element item, String tag) {
    NodeList list = item.getElementsByTagName(tag);
    if (list.getLength() == 0) {
      return Optional.empty();
    }
    String valor = list.item(0).getTextContent();
    if (valor == null || valor.trim().isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(valor.trim());
  }

  private List<String> categorias(Element item) {
    NodeList list = item.getElementsByTagName("category");
    List<String> categorias = new ArrayList<>();
    for (int i = 0; i < list.getLength(); i++) {
      String valor = list.item(i).getTextContent();
      if (valor != null && !valor.trim().isEmpty()) {
        categorias.add(valor.trim().toLowerCase(Locale.ROOT));
      }
    }
    return categorias;
  }

  private boolean relevante(String titulo, String descricao, List<String> categorias) {
    String base =
        (titulo + " " + descricao).toLowerCase(Locale.ROOT);
    for (String palavra : PALAVRAS_CHAVE) {
      String alvo = palavra.toLowerCase(Locale.ROOT);
      if (base.contains(alvo)) {
        return true;
      }
    }
    for (String categoria : categorias) {
      for (String palavra : PALAVRAS_CHAVE) {
        String alvo = palavra.toLowerCase(Locale.ROOT);
        if (categoria.contains(alvo)) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean isIndice(String titulo) {
    String valor = titulo.toLowerCase(Locale.ROOT).trim();
    return valor.startsWith("noticias de ")
        || valor.startsWith("notícias de ")
        || valor.startsWith("ultimas noticias")
        || valor.startsWith("últimas notícias");
  }

  private LocalDateTime parseData(String valor) {
    if (valor == null || valor.trim().isEmpty()) {
      return null;
    }
    try {
      return ZonedDateTime.parse(valor, DateTimeFormatter.RFC_1123_DATE_TIME).toLocalDateTime();
    } catch (Exception ex) {
      return null;
    }
  }

  private List<NoticiaAssistenciaResponse> limitar(List<NoticiaAssistenciaResponse> origem, int limite) {
    if (origem.isEmpty()) {
      return Collections.emptyList();
    }
    int tamanho = limite > 0 ? Math.min(limite, origem.size()) : origem.size();
    return new ArrayList<>(origem.subList(0, tamanho));
  }
}
