import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ConfigService, ConfiguracaoDocumentoBeneficiario } from '../../services/config.service';

@Component({
  selector: 'app-system-settings',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './system-settings.component.html',
  styleUrl: './system-settings.component.scss'
})
export class SystemSettingsComponent implements OnInit {
  form: FormGroup;
  saving = false;
  feedback: { type: 'success' | 'error'; message: string } | null = null;

  constructor(
    private readonly fb: FormBuilder,
    private readonly configService: ConfigService
  ) {
    this.form = this.fb.group({
      documents: this.fb.array([])
    });
  }

  get documents(): FormArray {
    return this.form.get('documents') as FormArray;
  }

  ngOnInit(): void {
    this.loadDocuments();
  }

  loadDocuments(): void {
    this.configService.obterDocumentosBeneficiario().subscribe({
      next: ({ documentos }) => {
        const controls = documentos.map((doc) =>
          this.fb.group({
            id: [doc.id],
            nome: [doc.nome],
            obrigatorio: [doc.obrigatorio]
          })
        );
        this.form.setControl('documents', this.fb.array(controls));
      },
      error: (error) => {
        console.error('Erro ao carregar configurações', error);
        this.feedback = { type: 'error', message: 'Não foi possível carregar as configurações.' };
      }
    });
  }

  save(): void {
    this.feedback = null;

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.saving = true;
    const documents = (this.form.value.documents || []) as ConfiguracaoDocumentoBeneficiario[];

    this.configService.atualizarDocumentosBeneficiario(documents).subscribe({
      next: () => {
        this.feedback = { type: 'success', message: 'Configurações salvas com sucesso.' };
      },
      error: (error) => {
        console.error('Erro ao salvar configurações', error);
        this.feedback = { type: 'error', message: 'Falha ao salvar configurações. Tente novamente.' };
      },
      complete: () => {
        this.saving = false;
      }
    });
  }
}
