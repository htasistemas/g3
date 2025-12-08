import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AssistenteTextosService, AssistenteTextoPayload } from '../../services/assistente-textos.service';

@Component({
  selector: 'app-assistente-textos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './assistente-textos.component.html',
  styleUrl: './assistente-textos.component.scss'
})
export class AssistenteTextosComponent {
  payload: AssistenteTextoPayload = {
    tipo: 'parecer',
    beneficiario: { situacaoBasica: '' }
  };
  textoGerado = '';
  loading = false;
  error: string | null = null;

  constructor(private readonly assistenteTextosService: AssistenteTextosService) {}

  gerarTexto(): void {
    this.loading = true;
    this.error = null;
    this.assistenteTextosService.gerarTexto(this.payload).subscribe({
      next: (response) => {
        this.textoGerado = response.texto;
        this.loading = false;
      },
      error: () => {
        this.error = 'Não foi possível gerar o texto sugerido. Tente novamente.';
        this.loading = false;
      }
    });
  }
}
