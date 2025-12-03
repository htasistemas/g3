import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ConfigService, BeneficiaryDocumentConfig } from '../../services/config.service';
import { SystemParameterService } from '../../services/system-parameter.service';

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
    private readonly configService: ConfigService,
    private readonly systemParameters: SystemParameterService
  ) {
    this.form = this.fb.group({
      documents: this.fb.array([]),
      habilitarModuloBiblioteca: [false]
    });
  }

  get documents(): FormArray {
    return this.form.get('documents') as FormArray;
  }

  ngOnInit(): void {
    this.loadDocuments();
    this.loadParameters();
  }

  loadDocuments(): void {
    this.configService.getBeneficiaryDocuments().subscribe({
      next: ({ documents }) => {
        const controls = documents.map((doc) =>
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

  loadParameters(): void {
    const params = this.systemParameters.current();
    this.form.patchValue({ habilitarModuloBiblioteca: params.habilitarModuloBiblioteca }, { emitEvent: false });
  }

  save(): void {
    this.feedback = null;

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.saving = true;
    const documents = (this.form.value.documents || []) as BeneficiaryDocumentConfig[];
    this.systemParameters.update({ habilitarModuloBiblioteca: Boolean(this.form.value.habilitarModuloBiblioteca) });

    this.configService.updateBeneficiaryDocuments(documents).subscribe({
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
