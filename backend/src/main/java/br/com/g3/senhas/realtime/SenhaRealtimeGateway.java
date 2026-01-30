package br.com.g3.senhas.realtime;

import br.com.g3.senhas.dto.SenhaEventoResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class SenhaRealtimeGateway {
  private final SimpMessagingTemplate messagingTemplate;

  public SenhaRealtimeGateway(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  public void enviarEvento(SenhaEventoResponse evento) {
    messagingTemplate.convertAndSend("/topic/senhas", evento);
  }
}
