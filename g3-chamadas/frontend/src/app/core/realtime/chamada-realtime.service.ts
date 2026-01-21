import { Injectable } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import { Observable, Subject } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ChamadaEventoModel } from '../shared/modelos/chamada.model';

@Injectable({ providedIn: 'root' })
export class ChamadaRealtimeService {
  private cliente: Client | null = null;
  private assunto = new Subject<ChamadaEventoModel>();

  conectar(): Observable<ChamadaEventoModel> {
    if (!this.cliente) {
      this.criarCliente();
    }
    return this.assunto.asObservable();
  }

  desconectar(): void {
    if (this.cliente) {
      this.cliente.deactivate();
      this.cliente = null;
    }
  }

  private criarCliente(): void {
    const url = `${environment.apiUrl}/ws`;
    this.cliente = new Client({
      webSocketFactory: () => new SockJS(url),
      reconnectDelay: 5000
    });

    this.cliente.onConnect = () => {
      this.cliente?.subscribe('/topic/chamadas', (mensagem: IMessage) => {
        try {
          const payload = JSON.parse(mensagem.body) as ChamadaEventoModel;
          this.assunto.next(payload);
        } catch {
          // Ignorar mensagens invalidas
        }
      });
    };

    this.cliente.activate();
  }
}
