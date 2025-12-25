package br.com.g3.unidadeassistencial.service;

import br.com.g3.unidadeassistencial.domain.Endereco;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class GeocodificacaoService {
  private final ObjectMapper objectMapper;
  private final String baseUrl;
  private final String userAgent;
  private final boolean habilitado;
  private final RestTemplate restTemplate;

  public GeocodificacaoService(
      ObjectMapper objectMapper,
      @Value("${app.geocoding.base-url:https://nominatim.openstreetmap.org/search}") String baseUrl,
      @Value("${app.geocoding.user-agent:g3-geocoder}") String userAgent,
      @Value("${app.geocoding.enabled:true}") boolean habilitado) {
    this.objectMapper = objectMapper;
    this.baseUrl = baseUrl;
    this.userAgent = userAgent;
    this.habilitado = habilitado;
    this.restTemplate = new RestTemplate();
  }

  public Optional<Coordenadas> geocodificar(Endereco endereco) {
    if (!habilitado || endereco == null) {
      return Optional.empty();
    }

    String consulta = montarConsulta(endereco);
    if (consulta.isEmpty()) {
      return Optional.empty();
    }

    try {
      String url =
          String.format(
              "%s?format=json&limit=1&q=%s",
              baseUrl, java.net.URLEncoder.encode(consulta, "UTF-8"));
      String body = restTemplate.getForObject(url, String.class);
      if (body == null || body.trim().isEmpty()) {
        return Optional.empty();
      }
      JsonNode root = objectMapper.readTree(body);
      if (!root.isArray() || root.isEmpty()) {
        return Optional.empty();
      }

      JsonNode item = root.get(0);
      String lat = item.path("lat").asText();
      String lon = item.path("lon").asText();
      if (!temValor(lat) || !temValor(lon)) {
        return Optional.empty();
      }

      return Optional.of(new Coordenadas(new BigDecimal(lat), new BigDecimal(lon)));
    } catch (IOException | IllegalArgumentException | RestClientException ex) {
      return Optional.empty();
    }
  }

  private String montarConsulta(Endereco endereco) {
    List<String> partes = new ArrayList<>();
    adicionarSeValido(partes, endereco.getLogradouro());
    adicionarSeValido(partes, endereco.getNumero());
    adicionarSeValido(partes, endereco.getBairro());
    adicionarSeValido(partes, endereco.getCidade());
    adicionarSeValido(partes, endereco.getEstado());
    adicionarSeValido(partes, endereco.getCep());
    return String.join(", ", partes);
  }

  private void adicionarSeValido(List<String> partes, String valor) {
    if (temValor(valor)) {
      partes.add(valor.trim());
    }
  }

  private boolean temValor(String valor) {
    return valor != null && !valor.trim().isEmpty();
  }
}
