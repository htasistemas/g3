package br.com.g3.alertasistema.serviceimpl;

import br.com.g3.alertasistema.domain.AlertaSistema;
import br.com.g3.alertasistema.dto.AlertaSistemaRequest;
import br.com.g3.alertasistema.dto.AlertaSistemaResponse;
import br.com.g3.alertasistema.repository.AlertaSistemaRepository;
import br.com.g3.alertasistema.service.AlertaSistemaService;
import br.com.g3.shared.service.EmailService;
import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialResponse;
import br.com.g3.unidadeassistencial.service.UnidadeAssistencialService;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AlertaSistemaServiceImpl implements AlertaSistemaService {
  private final AlertaSistemaRepository repository;
  private final UnidadeAssistencialService unidadeAssistencialService;
  private final EmailService emailService;

  public AlertaSistemaServiceImpl(
      AlertaSistemaRepository repository,
      UnidadeAssistencialService unidadeAssistencialService,
      EmailService emailService) {
    this.repository = repository;
    this.unidadeAssistencialService = unidadeAssistencialService;
    this.emailService = emailService;
  }

  @Override
  public AlertaSistemaResponse obterConfiguracao() {
    List<AlertaSistema> alertas = repository.listar();
    if (alertas.isEmpty()) {
      return new AlertaSistemaResponse(Collections.emptyList(), "imediato");
    }
    String frequencia =
        alertas.stream()
            .map(AlertaSistema::getFrequenciaEnvio)
            .filter(Objects::nonNull)
            .findFirst()
            .orElse("imediato");
    List<String> selecionados =
        alertas.stream()
            .filter(AlertaSistema::isAtivo)
            .map(AlertaSistema::getTipoAlerta)
            .collect(Collectors.toList());
    return new AlertaSistemaResponse(selecionados, frequencia);
  }

  @Override
  @Transactional
  public AlertaSistemaResponse salvarConfiguracao(AlertaSistemaRequest request) {
    if (request == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados invalidos.");
    }
    String frequencia = normalizarTexto(request.getFrequenciaEnvio());
    if (frequencia == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Frequencia de envio nao informada.");
    }
    List<String> alertas =
        request.getAlertasSelecionados() == null
            ? Collections.emptyList()
            : request.getAlertasSelecionados().stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(valor -> !valor.isEmpty())
                .collect(Collectors.toList());

    repository.removerTodos();

    for (String alerta : alertas) {
      AlertaSistema entidade = new AlertaSistema();
      entidade.setTipoAlerta(alerta);
      entidade.setFrequenciaEnvio(frequencia);
      entidade.setAtivo(true);
      repository.salvar(entidade);
    }

    if (!alertas.isEmpty()) {
      UnidadeAssistencialResponse unidade = unidadeAssistencialService.obterAtual();
      String emailInstitucional =
          unidade == null ? null : normalizarTexto(unidade.getEmail());
      if (emailInstitucional == null) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Email da unidade assistencial nao informado para envio dos alertas.");
      }
      emailService.enviarAlertasSistema(emailInstitucional, alertas, frequencia);
    }

    return new AlertaSistemaResponse(alertas, frequencia);
  }

  private String normalizarTexto(String valor) {
    if (valor == null) {
      return null;
    }
    String texto = valor.trim();
    return texto.isEmpty() ? null : texto;
  }
}
