package br.com.g3.configuracoes.controller;

import br.com.g3.configuracoes.dto.AtualizarVersaoRequest;
import br.com.g3.configuracoes.dto.DestinoChamadoResponse;
import br.com.g3.configuracoes.dto.HistoricoVersaoResponse;
import br.com.g3.configuracoes.dto.VersaoSistemaResponse;
import br.com.g3.configuracoes.service.ConfiguracaoSistemaService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/config")
public class ConfiguracaoSistemaController {
  private final ConfiguracaoSistemaService service;

  public ConfiguracaoSistemaController(ConfiguracaoSistemaService service) {
    this.service = service;
  }

  @GetMapping("/versao")
  public VersaoSistemaResponse obterVersaoAtual() {
    return service.obterVersaoAtual();
  }

  @PutMapping("/versao")
  public VersaoSistemaResponse atualizarVersao(@RequestBody AtualizarVersaoRequest request) {
    return service.atualizarVersao(request);
  }

  @GetMapping("/versao/historico")
  public List<HistoricoVersaoResponse> listarHistorico() {
    return service.listarHistorico();
  }

  @GetMapping("/chamados/destino")
  public DestinoChamadoResponse obterDestinoChamados() {
    return service.obterDestinoChamados();
  }
}
