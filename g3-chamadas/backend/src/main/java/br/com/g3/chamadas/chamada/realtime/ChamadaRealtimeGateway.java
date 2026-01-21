package br.com.g3.chamadas.chamada.realtime;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class ChamadaRealtimeGateway {
  private final SimpMessagingTemplate messagingTemplate;

  public ChamadaRealtimeGateway(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  public void enviarChamada(ChamadaEventoDto evento) {
    messagingTemplate.convertAndSend("/topic/chamadas", evento);
  }
}
