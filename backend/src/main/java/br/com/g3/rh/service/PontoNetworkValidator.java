package br.com.g3.rh.service;

import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PontoNetworkValidator {
  private static final List<String> INDICADORES_MOBILE = List.of(
      "android",
      "iphone",
      "ipad",
      "ipod",
      "windows phone",
      "iemobile",
      "blackberry",
      "opera mini",
      "opera mobi",
      "mobile",
      "tablet"
  );

  private final boolean bypassValidacao;

  public PontoNetworkValidator(@Value("${app.rh.ponto.bypass:false}") boolean bypassValidacao) {
    this.bypassValidacao = bypassValidacao;
  }

  public String obterIpCliente(HttpServletRequest request) {
    if (request == null) {
      return null;
    }
    String ipRemoto = normalizarIp(request.getRemoteAddr());
    if (ipRemoto == null) {
      return null;
    }

    if (proxyConfiavel(ipRemoto)) {
      String ipCloudflare = normalizarIp(request.getHeader("CF-Connecting-IP"));
      if (ipCloudflare != null) {
        return ipCloudflare;
      }
      String forwarded = request.getHeader("X-Forwarded-For");
      String ipForwarded = extrairPrimeiroIp(forwarded);
      if (ipForwarded != null) {
        return ipForwarded;
      }
    }

    return ipRemoto;
  }

  public ResultadoValidacaoPonto validarAutorizacao(
      String ipCliente,
      String userAgent,
      UnidadeAssistencialResponse unidade) {
    if (bypassValidacao) {
      return new ResultadoValidacaoPonto(true, null);
    }
    if (isMobileUserAgent(userAgent)) {
      return new ResultadoValidacaoPonto(false, "Registro de ponto permitido apenas no computador da instituição.");
    }

    String ipNormalizado = normalizarIp(ipCliente);
    if (ipNormalizado == null) {
      return new ResultadoValidacaoPonto(false, "Não foi possível identificar o IP do cliente.");
    }

    if (unidade == null) {
      return new ResultadoValidacaoPonto(false, "Configuração de rede do ponto não definida.");
    }

    List<String> ipsPublicos = parseLista(unidade.getIpsPublicosPonto());
    List<String> redesLocais = parseLista(unidade.getRedesLocaisPonto());

    if (ipsPublicos.isEmpty() && redesLocais.isEmpty()) {
      return new ResultadoValidacaoPonto(false, "Configuração de rede do ponto não definida.");
    }

    String modo = normalizarModo(unidade.getModoValidacaoPonto());
    boolean ipPublicoOk = ipEstaNaLista(ipNormalizado, ipsPublicos);
    boolean redeOk = ipEstaEmCidr(ipNormalizado, redesLocais);

    boolean permitido = switch (modo) {
      case "IP_PUBLICO" -> ipPublicoOk;
      case "REDE_LOCAL" -> redeOk;
      case "IP_OU_REDE" -> ipPublicoOk || redeOk;
      default -> ipPublicoOk || redeOk;
    };

    if (!permitido) {
      return new ResultadoValidacaoPonto(false, "Você precisa estar na instituição para registrar o ponto.");
    }

    return new ResultadoValidacaoPonto(true, null);
  }

  public boolean isMobileUserAgent(String userAgent) {
    if (userAgent == null || userAgent.isBlank()) {
      return false;
    }
    String normalizado = userAgent.toLowerCase(Locale.forLanguageTag("pt-BR"));
    return INDICADORES_MOBILE.stream().anyMatch(normalizado::contains);
  }

  private boolean proxyConfiavel(String ipRemoto) {
    try {
      InetAddress endereco = InetAddress.getByName(ipRemoto);
      return endereco.isSiteLocalAddress() || endereco.isLoopbackAddress();
    } catch (Exception ex) {
      return false;
    }
  }

  private String extrairPrimeiroIp(String header) {
    if (header == null || header.isBlank()) {
      return null;
    }
    String[] partes = header.split(",");
    for (String parte : partes) {
      String ip = normalizarIp(parte);
      if (ip != null) {
        return ip;
      }
    }
    return null;
  }

  private String normalizarIp(String ip) {
    if (ip == null) {
      return null;
    }
    String valor = ip.trim();
    if (valor.isEmpty()) {
      return null;
    }
    if (valor.startsWith("[")) {
      int fim = valor.indexOf(']');
      if (fim > 0) {
        valor = valor.substring(1, fim);
      }
    }
    if (valor.contains(":") && valor.contains(".")) {
      int separador = valor.lastIndexOf(':');
      if (separador > 0) {
        String candidato = valor.substring(0, separador);
        if (candidato.matches("[0-9.]+")) {
          valor = candidato;
        }
      }
    }
    return valor.isEmpty() ? null : valor;
  }

  private List<String> parseLista(String valor) {
    if (valor == null || valor.isBlank()) {
      return List.of();
    }
    String[] partes = valor.split("[\\n,;]+");
    List<String> resultado = new ArrayList<>();
    for (String parte : partes) {
      String item = parte.trim();
      if (!item.isEmpty()) {
        resultado.add(item);
      }
    }
    return resultado;
  }

  private boolean ipEstaNaLista(String ip, List<String> ips) {
    if (ips.isEmpty()) {
      return false;
    }
    try {
      InetAddress alvo = InetAddress.getByName(ip);
      byte[] alvoBytes = alvo.getAddress();
      for (String item : ips) {
        try {
          String normalizado = normalizarIp(item);
          if (normalizado == null) {
            continue;
          }
          InetAddress configurado = InetAddress.getByName(normalizado);
          if (Arrays.equals(alvoBytes, configurado.getAddress())) {
            return true;
          }
        } catch (Exception ex) {
          // Ignora IPs invalidos na configuracao.
        }
      }
    } catch (Exception ex) {
      return false;
    }
    return false;
  }

  private boolean ipEstaEmCidr(String ip, List<String> cidrs) {
    if (cidrs.isEmpty()) {
      return false;
    }
    for (String cidr : cidrs) {
      if (ipDentroDeCidr(ip, cidr)) {
        return true;
      }
    }
    return false;
  }

  private boolean ipDentroDeCidr(String ip, String cidr) {
    if (ip == null || cidr == null) {
      return false;
    }
    String[] partes = cidr.trim().split("/");
    if (partes.length != 2) {
      return false;
    }
    try {
      InetAddress enderecoIp = InetAddress.getByName(ip);
      InetAddress enderecoRede = InetAddress.getByName(partes[0].trim());
      int prefixo = Integer.parseInt(partes[1].trim());

      byte[] ipBytes = enderecoIp.getAddress();
      byte[] redeBytes = enderecoRede.getAddress();
      if (ipBytes.length != redeBytes.length) {
        return false;
      }
      int maxBits = ipBytes.length * 8;
      if (prefixo < 0 || prefixo > maxBits) {
        return false;
      }
      int bitsRestantes = prefixo;
      for (int i = 0; i < ipBytes.length; i++) {
        int mascara;
        if (bitsRestantes >= 8) {
          mascara = 0xFF;
        } else if (bitsRestantes <= 0) {
          mascara = 0x00;
        } else {
          mascara = ~((1 << (8 - bitsRestantes)) - 1) & 0xFF;
        }
        if ((ipBytes[i] & mascara) != (redeBytes[i] & mascara)) {
          return false;
        }
        bitsRestantes -= 8;
      }
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  private String normalizarModo(String modo) {
    if (modo == null || modo.isBlank()) {
      return "IP_OU_REDE";
    }
    return modo.trim().toUpperCase(Locale.forLanguageTag("pt-BR"));
  }
}
