from pathlib import Path
import re
path = Path('frontend/src/app/components/cursos-atendimentos/cursos-atendimentos.component.html')
text = path.read_text(encoding='utf-8')

# 1) Inverter cards dentro de agenda-grid
marker1 = '<div class="agenda-panel">'
marker2 = '<div class="agenda-panel" [formGroup]="agendamentoForm">'
start1 = text.find(marker1)
start2 = text.find(marker2, start1)
marker_after = '<div class="list-card">'
end2 = text.find(marker_after, start2)
if start1 == -1 or start2 == -1 or end2 == -1:
    raise SystemExit('Nao foi possivel localizar os cards da agenda')
block1 = text[start1:start2]
block2 = text[start2:end2]
text = text[:start1] + block2 + block1 + text[end2:]

# 2) Agrupar por profissional e adicionar WhatsApp
label = 'Beneficiários agendados'
label_idx = text.find(label)
if label_idx == -1:
    raise SystemExit('Label de agendados nao encontrado')
start = text.find('<div class="table-wrapper">', label_idx)
if start == -1:
    raise SystemExit('Table wrapper nao encontrado')
marker_end = '\n        </div>\n      </ng-container>'
end = text.find(marker_end, start)
if end == -1:
    raise SystemExit('Fim do bloco nao encontrado')
start_content = start + len('<div class="table-wrapper">')
new_inner = '''
            <ng-container *ngIf="agendamentosPorProfissional.length; else emptyAgendamentos">
              <div class="agenda-profissional" *ngFor="let grupo of agendamentosPorProfissional">
                <p class="agenda-profissional__titulo">Profissional: {{ grupo.profissional }}</p>
                <table>
                  <thead>
                    <tr>
                      <th>Hora</th>
                      <th>Beneficiário</th>
                      <th>CPF</th>
                      <th>Situação</th>
                      <th>Presença</th>
                      <th>Ações</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr *ngFor="let matricula of grupo.itens">
                      <td>{{ matricula.horaAgendada || '--:--' }}</td>
                      <td>{{ matricula.beneficiaryName }}</td>
                      <td>{{ matricula.cpf }}</td>
                      <td>
                        <select
                          [ngModel]="matricula.statusAgendamento || 'AGUARDANDO'"
                          [ngModelOptions]="{ standalone: true }"
                          (ngModelChange)="atualizarStatusAgendamento(matricula, $event)"
                        >
                          <option *ngFor="let status of statusAgendamentoOpcoes" [value]="status.valor">
                            {{ status.label }}
                          </option>
                        </select>
                      </td>
                      <td>{{ matricula.confirmacaoPresenca ? 'Confirmada' : 'Pendente' }}</td>
                      <td class="table-actions">
                        <button
                          type="button"
                          class="primary"
                          (click)="confirmarPresencaAgendamento(matricula)"
                          [disabled]="matricula.confirmacaoPresenca"
                        >
                          Confirmar presença
                        </button>
                        <button type="button" class="ghost" (click)="enviarWhatsappAgendamento(matricula)">WhatsApp</button>
                        <button type="button" class="ghost" (click)="marcarRemarcar(matricula)">Remarcar</button>
                        <button type="button" class="danger" (click)="marcarNaoRespondeu(matricula)">Não respondeu</button>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </ng-container>
            <ng-template #emptyAgendamentos>
              <div class="muted">Nenhum atendimento agendado para este dia.</div>
            </ng-template>
          '''
text = text[:start_content] + new_inner + text[end:]

path.write_text(text, encoding='utf-8')
