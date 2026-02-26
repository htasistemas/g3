package br.com.g3.rh.service;

import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class PontoNetworkValidatorTeste {
  private final PontoNetworkValidator validator = new PontoNetworkValidator(false);

  @Test
  void obterIpCliente_deveUsarForwardedQuandoProxyLocal() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    Mockito.when(request.getRemoteAddr()).thenReturn("127.0.0.1");
    Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("200.150.10.20, 10.0.0.1");

    String ip = validator.obterIpCliente(request);

    Assertions.assertEquals("200.150.10.20", ip);
  }

  @Test
  void obterIpCliente_deveUsarCloudflareQuandoDisponivel() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    Mockito.when(request.getRemoteAddr()).thenReturn("10.0.0.2");
    Mockito.when(request.getHeader("CF-Connecting-IP")).thenReturn("203.0.113.8");
    Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("198.51.100.4");

    String ip = validator.obterIpCliente(request);

    Assertions.assertEquals("203.0.113.8", ip);
  }

  @Test
  void obterIpCliente_deveManterRemotoQuandoProxyNaoConfiavel() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    Mockito.when(request.getRemoteAddr()).thenReturn("200.150.10.20");
    Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("10.0.0.1");

    String ip = validator.obterIpCliente(request);

    Assertions.assertEquals("200.150.10.20", ip);
  }

  @Test
  void validarAutorizacao_deveBloquearMobile() {
    UnidadeAssistencialResponse unidade = criarUnidade(
        "200.150.10.20",
        null,
        "IP_PUBLICO");
    ResultadoValidacaoPonto resultado =
        validator.validarAutorizacao("200.150.10.20", "Mozilla/5.0 (iPhone; CPU iPhone OS 16_2)", unidade);

    Assertions.assertFalse(resultado.permitido());
    Assertions.assertEquals("Registro de ponto permitido apenas no computador da instituição.", resultado.motivo());
  }

  @Test
  void validarAutorizacao_devePermitirIpPublicoExato() {
    UnidadeAssistencialResponse unidade = criarUnidade(
        "200.150.10.20, 187.12.0.5",
        null,
        "IP_PUBLICO");
    ResultadoValidacaoPonto resultado =
        validator.validarAutorizacao("200.150.10.20", "Mozilla/5.0", unidade);

    Assertions.assertTrue(resultado.permitido());
  }

  @Test
  void validarAutorizacao_deveValidarCidrDentroFora() {
    UnidadeAssistencialResponse unidade = criarUnidade(
        null,
        "192.168.0.0/24",
        "REDE_LOCAL");
    ResultadoValidacaoPonto dentro =
        validator.validarAutorizacao("192.168.0.50", "Mozilla/5.0", unidade);
    ResultadoValidacaoPonto fora =
        validator.validarAutorizacao("192.168.1.10", "Mozilla/5.0", unidade);

    Assertions.assertTrue(dentro.permitido());
    Assertions.assertFalse(fora.permitido());
  }

  private UnidadeAssistencialResponse criarUnidade(
      String ipsPublicos,
      String redesLocais,
      String modoValidacao) {
    return new UnidadeAssistencialResponse(
        1L,
        "Sede",
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        List.<br.com.g3.unidadeassistencial.dto.DiretoriaUnidadeResponse>of(),
        true,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        100,
        200,
        null,
        ipsPublicos,
        redesLocais,
        modoValidacao,
        2000);
  }
}
