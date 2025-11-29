import { Component, OnDestroy, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators
} from '@angular/forms';
import {
  BeneficiaryPayload,
  BeneficiaryService,
  DocumentoObrigatorio
} from '../../services/beneficiary.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-beneficiary-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './beneficiary-form.component.html',
  styleUrl: './beneficiary-form.component.scss'
})
export class BeneficiaryFormComponent implements OnInit, OnDestroy {
  beneficiaryForm: FormGroup;
  documentStatus = 'Pendente';
  requiredDocuments: DocumentoObrigatorio[] = [];
  missingRequiredDocuments: string[] = [];
  photoPreview: string | null = null;
  cameraActive = false;
  beneficiaries: BeneficiaryPayload[] = [];
  editingId: number | null = null;
  saveFeedback: { type: 'success' | 'error'; message: string } | null = null;
  readonly states = [
    'AC',
    'AL',
    'AP',
    'AM',
    'BA',
    'CE',
    'DF',
    'ES',
    'GO',
    'MA',
    'MT',
    'MS',
    'MG',
    'PA',
    'PB',
    'PR',
    'PE',
    'PI',
    'RJ',
    'RN',
    'RS',
    'RO',
    'RR',
    'SC',
    'SP',
    'SE',
    'TO'
  ];
  private mediaStream?: MediaStream;

  constructor(
    private readonly fb: FormBuilder,
    private readonly beneficiaryService: BeneficiaryService
  ) {
    this.beneficiaryForm = this.fb.group({
      cep: ['', [Validators.required, this.cepValidator]],
      nomeCompleto: ['', [Validators.required, Validators.minLength(3)]],
      nomeMae: [''],
      documentos: ['', [Validators.required, this.cpfValidator]],
      dataNascimento: ['', Validators.required],
      idade: [{ value: '', disabled: true }],
      telefone: ['', [Validators.required, this.phoneValidator]],
      email: ['', [Validators.required, Validators.email]],
      endereco: ['', Validators.required],
      numeroEndereco: ['', Validators.required],
      pontoReferencia: [''],
      bairro: [''],
      cidade: [''],
      estado: ['', Validators.required],
      observacoes: [''],
      status: ['Ativo', Validators.required],
      possuiCnh: [false],
      arquivosDocumentos: [[], [this.documentsRequiredValidator]],
      foto: [''],
      possuiFilhosMenores: [false],
      quantidadeFilhosMenores: [{ value: '', disabled: true }],
      escolaridade: [''],
      rendaIndividual: [''],
      rendaFamiliar: [''],
      informacoesMoradia: [''],
      condicoesSaneamento: [''],
      situacaoEmprego: [''],
      ocupacao: ['']
    });

    this.beneficiaryForm
      .get('dataNascimento')
      ?.valueChanges.subscribe((value: string | null) => this.updateAge(value));

    this.beneficiaryForm
      .get('possuiFilhosMenores')
      ?.valueChanges.subscribe((value: boolean) => this.toggleMinorChildrenRequirement(Boolean(value)));

    this.beneficiaryForm
      .get('possuiCnh')
      ?.valueChanges.subscribe(() => this.updateConditionalDocumentRequirements());
  }

  ngOnInit(): void {
    this.loadRequiredDocuments();
    this.loadBeneficiaries();
  }

  submit(): void {
    this.saveFeedback = null;

    if (this.beneficiaryForm.invalid) {
      this.beneficiaryForm.markAllAsTouched();
      return;
    }

    const formValue = {
      ...this.beneficiaryForm.getRawValue(),
      id: this.editingId ?? undefined,
      documentosAnexos: this.requiredDocuments.map((doc) => ({ nome: doc.nome, nomeArquivo: doc.nomeArquivo }))
    } as BeneficiaryPayload;

    this.beneficiaryService.save(formValue).subscribe({
      next: () => {
        this.resetForm();
        this.loadBeneficiaries();
        this.saveFeedback = { type: 'success', message: 'Beneficiário salvo com sucesso.' };
      },
      error: (error) => {
        console.error('Erro ao salvar beneficiário', error);
        const missing = error?.error?.missingDocuments as string[] | undefined;
        const message = missing?.length
          ? `Documentos obrigatórios ausentes: ${missing.join(', ')}`
          : 'Erro ao salvar beneficiário.';
        this.saveFeedback = { type: 'error', message };
      }
    });
  }

  formatTitleCase(controlName: string): void {
    const control = this.beneficiaryForm.get(controlName);
    const value = control?.value as string;

    if (!control || typeof value !== 'string') {
      return;
    }

    const formatted = value
      .toLowerCase()
      .replace(/\S+/g, (word) => word.charAt(0).toUpperCase() + word.slice(1));

    if (formatted !== value) {
      control.setValue(formatted, { emitEvent: false });
    }
  }

  onCpfInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const digits = input.value.replace(/\D/g, '').slice(0, 11);

    let formatted = digits;
    if (digits.length > 9) {
      formatted = digits.replace(/(\d{3})(\d{3})(\d{3})(\d{0,2}).*/, '$1.$2.$3-$4');
    } else if (digits.length > 6) {
      formatted = digits.replace(/(\d{3})(\d{3})(\d{0,3}).*/, '$1.$2.$3');
    } else if (digits.length > 3) {
      formatted = digits.replace(/(\d{3})(\d{0,3}).*/, '$1.$2');
    }

    this.beneficiaryForm.get('documentos')?.setValue(formatted, { emitEvent: false });
  }

  onPhoneInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const digits = input.value.replace(/\D/g, '').slice(0, 11);

    let formatted = digits;
    if (digits.length > 10) {
      formatted = digits.replace(/(\d{2})(\d{5})(\d{0,4}).*/, '($1) $2-$3');
    } else if (digits.length > 6) {
      formatted = digits.replace(/(\d{2})(\d{4})(\d{0,4}).*/, '($1) $2-$3');
    } else if (digits.length > 2) {
      formatted = digits.replace(/(\d{2})(\d{0,5}).*/, '($1) $2');
    }

    this.beneficiaryForm.get('telefone')?.setValue(formatted, { emitEvent: false });
  }

  handleCepInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const digits = input.value.replace(/\D/g, '').slice(0, 8);

    let formatted = digits;
    if (digits.length > 5) {
      formatted = `${digits.slice(0, 5)}-${digits.slice(5)}`;
    }

    this.beneficiaryForm.get('cep')?.setValue(formatted, { emitEvent: false });

    if (digits.length === 8) {
      this.fetchAddressByCep(digits);
    } else {
      this.removeZipError('invalidCep');
    }
  }

  private async fetchAddressByCep(cep: string): Promise<void> {
    const zipControl = this.beneficiaryForm.get('cep');

    if (!zipControl) {
      return;
    }

    try {
      const response = await fetch(`https://viacep.com.br/ws/${cep}/json/`);
      const data = await response.json();

      if (!response.ok || data?.erro) {
        throw new Error('CEP não encontrado');
      }

      this.beneficiaryForm.patchValue({
        endereco: data.logradouro || '',
        bairro: data.bairro || '',
        cidade: data.localidade || '',
        estado: data.uf || ''
      });

      this.removeZipError('invalidCep');
    } catch (error) {
      console.error('Erro ao buscar CEP', error);
      this.clearAddressFields();
      this.setZipError('invalidCep');
    }
  }

  private clearAddressFields(): void {
    this.beneficiaryForm.patchValue({ endereco: '', bairro: '', cidade: '', estado: '' });
  }

  private setZipError(errorKey: string): void {
    const zipControl = this.beneficiaryForm.get('cep');

    if (!zipControl) {
      return;
    }

    const existingErrors = zipControl.errors ?? {};
    zipControl.setErrors({ ...existingErrors, [errorKey]: true });
  }

  private removeZipError(errorKey: string): void {
    const zipControl = this.beneficiaryForm.get('cep');

    if (!zipControl?.errors) {
      return;
    }

    const { [errorKey]: _, ...rest } = zipControl.errors;
    zipControl.setErrors(Object.keys(rest).length ? rest : null);
  }

  private updateAge(dateString: string | null): void {
    if (!dateString) {
      this.beneficiaryForm.get('idade')?.setValue('', { emitEvent: false });
      return;
    }

    const birthDate = new Date(dateString);
    const today = new Date();
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();

    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
      age--;
    }

    this.beneficiaryForm.get('idade')?.setValue(age >= 0 ? age : '', { emitEvent: false });
  }

  private toggleMinorChildrenRequirement(hasMinorChildren: boolean): void {
    const countControl = this.beneficiaryForm.get('quantidadeFilhosMenores');

    if (!countControl) {
      return;
    }

    if (hasMinorChildren) {
      countControl.setValidators([Validators.required, Validators.min(0)]);
      countControl.enable({ emitEvent: false });
    } else {
      countControl.clearValidators();
      countControl.reset({ value: '', disabled: true }, { emitEvent: false });
    }

    countControl.updateValueAndValidity({ emitEvent: false });
    this.updateConditionalDocumentRequirements();
  }

  private updateConditionalDocumentRequirements(): void {
    const hasMinorChildren = Boolean(this.beneficiaryForm.get('possuiFilhosMenores')?.value);
    const hasDriverLicense = Boolean(this.beneficiaryForm.get('possuiCnh')?.value);

    this.requiredDocuments = this.requiredDocuments.map((doc) => {
      const baseRequired = doc.baseRequired ?? doc.required ?? false;
      const isBirthCertificate = doc.nome === 'Certidão de Nascimento';
      const isDriverLicense = doc.nome.toLowerCase() === 'cnh';
      const required =
        baseRequired || (isBirthCertificate && hasMinorChildren) || (isDriverLicense && hasDriverLicense);

      return { ...doc, required, baseRequired };
    });

    this.updateDocumentControl();
  }

  onDocumentUpload(event: Event, documentName: string): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    const targetDoc = this.requiredDocuments.find((doc) => doc.nome === documentName);

    if (!targetDoc) {
      return;
    }

    targetDoc.file = file || undefined;
    targetDoc.nomeArquivo = file?.name || targetDoc.nomeArquivo;
    this.updateDocumentControl();
  }

  handlePhotoUpload(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];

    if (!file) {
      this.photoPreview = null;
      this.beneficiaryForm.get('foto')?.setValue('');
      return;
    }

    const reader = new FileReader();
    reader.onload = () => {
      this.photoPreview = reader.result as string;
      this.beneficiaryForm.get('foto')?.setValue(this.photoPreview);
    };
    reader.readAsDataURL(file);
  }

  async startCamera(videoElement: HTMLVideoElement): Promise<void> {
    try {
      this.mediaStream = await navigator.mediaDevices.getUserMedia({ video: true });
      videoElement.srcObject = this.mediaStream;
      videoElement.play();
      this.cameraActive = true;
    } catch (error) {
      console.error('Erro ao acessar a câmera', error);
    }
  }

  capturePhoto(videoElement: HTMLVideoElement, canvasElement: HTMLCanvasElement): void {
    if (!this.cameraActive) {
      return;
    }

    const context = canvasElement.getContext('2d');
    if (!context) {
      return;
    }

    canvasElement.width = videoElement.videoWidth;
    canvasElement.height = videoElement.videoHeight;
    context.drawImage(videoElement, 0, 0, canvasElement.width, canvasElement.height);
    this.photoPreview = canvasElement.toDataURL('image/png');
    this.beneficiaryForm.get('foto')?.setValue(this.photoPreview);
    this.stopCamera();
  }

  stopCamera(): void {
    this.mediaStream?.getTracks().forEach((track) => track.stop());
    this.cameraActive = false;
  }

  ngOnDestroy(): void {
    this.stopCamera();
  }

  startEdit(beneficiary: BeneficiaryPayload): void {
    this.saveFeedback = null;
    this.editingId = beneficiary.id ?? null;
    this.requiredDocuments = this.requiredDocuments.map((doc) => ({
      ...doc,
      file: undefined,
      nomeArquivo: beneficiary.documentosAnexos?.find((d) => d.nome === doc.nome)?.nomeArquivo
    }));
    this.updateDocumentControl();

    this.beneficiaryForm.reset({
      ...beneficiary,
      idade: beneficiary.idade,
      arquivosDocumentos: this.requiredDocuments,
      foto: beneficiary.foto || '',
      possuiCnh: Boolean(beneficiary.possuiCnh)
    });
    this.photoPreview = beneficiary.foto || null;
    this.toggleMinorChildrenRequirement(Boolean(beneficiary.possuiFilhosMenores));
    this.updateConditionalDocumentRequirements();
  }

  resetForm(): void {
    this.editingId = null;
    this.documentStatus = 'Pendente';
    this.photoPreview = null;
    this.saveFeedback = null;
    this.requiredDocuments = this.requiredDocuments.map((doc) => ({
      nome: doc.nome,
      required: doc.baseRequired ?? doc.required,
      baseRequired: doc.baseRequired ?? doc.required
    }));
    this.beneficiaryForm.reset({
      status: 'Ativo',
      arquivosDocumentos: this.requiredDocuments,
      idade: '',
      possuiCnh: false,
      possuiFilhosMenores: false,
      quantidadeFilhosMenores: ''
    });
    this.beneficiaryForm.get('quantidadeFilhosMenores')?.disable({ emitEvent: false });
    this.toggleMinorChildrenRequirement(false);
  }

  private updateDocumentControl(): void {
    const documentControl = this.beneficiaryForm.get('arquivosDocumentos');
    documentControl?.setValue(this.requiredDocuments);
    documentControl?.updateValueAndValidity();

    this.missingRequiredDocuments = this.requiredDocuments
      .filter((doc) => doc.required && !doc.nomeArquivo)
      .map((doc) => doc.nome);

    const allRequiredProvided = this.requiredDocuments
      .filter((doc) => doc.required)
      .every((doc) => doc.nomeArquivo);
    this.documentStatus = allRequiredProvided ? 'Enviado' : 'Pendente';
  }

  private loadRequiredDocuments(): void {
    this.beneficiaryService.getRequiredDocuments().subscribe({
      next: ({ documents }) => {
        this.requiredDocuments = documents.map((document) => ({
          nome: document.nome,
          required: Boolean(document.obrigatorio ?? document.required),
          baseRequired: Boolean(document.obrigatorio ?? document.required),
          nomeArquivo: document.nomeArquivo
        }));
        this.updateConditionalDocumentRequirements();
      },
      error: (error) => console.error('Erro ao carregar documentos obrigatórios', error)
    });
  }

  private loadBeneficiaries(): void {
    this.beneficiaryService.list().subscribe({
      next: ({ beneficiarios }) => {
        this.beneficiaries = beneficiarios;
      },
      error: (error) => console.error('Erro ao carregar beneficiários', error)
    });
  }

  private documentsRequiredValidator = (control: AbstractControl): ValidationErrors | null => {
    const docs = control.value as DocumentoObrigatorio[];
    const missingDocuments = this.requiredDocuments.some((doc) => {
      if (!doc.required) {
        return false;
      }

      const matched = docs?.find((item) => item.nome === doc.nome);
      return !matched?.nomeArquivo;
    });

    return missingDocuments ? { documentsRequired: true } : null;
  };

  private cepValidator(control: AbstractControl): ValidationErrors | null {
    const digits = (control.value as string)?.replace(/\D/g, '');

    if (!digits) {
      return { cep: true };
    }

    return digits.length === 8 ? null : { cep: true };
  }

  private cpfValidator(control: AbstractControl): ValidationErrors | null {
    const value = (control.value as string)?.replace(/\D/g, '');

    if (!value || value.length !== 11 || /^([0-9])\1*$/.test(value)) {
      return { cpf: true };
    }

    const calculateDigit = (slice: number) => {
      const numbers = value.slice(0, slice).split('').map(Number);
      const factorStart = slice + 1;
      const sum = numbers.reduce((acc, num, index) => acc + num * (factorStart - index), 0);
      const remainder = (sum * 10) % 11;
      return remainder === 10 ? 0 : remainder;
    };

    const digit1 = calculateDigit(9);
    const digit2 = calculateDigit(10);

    return digit1 === Number(value[9]) && digit2 === Number(value[10]) ? null : { cpf: true };
  }

  private phoneValidator(control: AbstractControl): ValidationErrors | null {
    const value = (control.value as string)?.replace(/\D/g, '');
    if (!value) {
      return { phone: true };
    }

    return value.length === 10 || value.length === 11 ? null : { phone: true };
  }
}
