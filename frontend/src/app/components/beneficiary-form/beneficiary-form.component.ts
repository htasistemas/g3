import { Component, OnDestroy, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {
  BeneficiaryPayload,
  BeneficiaryService,
  DocumentoObrigatorio
} from '../../services/beneficiary.service';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-beneficiary-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './beneficiary-form.component.html',
  styleUrl: './beneficiary-form.component.scss'
})
export class BeneficiaryFormComponent implements OnInit, OnDestroy {
  beneficiaryForm: FormGroup;
  filterForm: FormGroup;
  documentStatus = 'Pendente';
  requiredDocuments: DocumentoObrigatorio[] = [];
  missingRequiredDocuments: string[] = [];
  photoPreview: string | null = null;
  photoError: string | null = null;
  cameraActive = false;
  cameraReady = false;
  cameraError: string | null = null;
  filteredBeneficiaries: BeneficiaryPayload[] = [];
  beneficiaries: BeneficiaryPayload[] = [];
  editingId: number | null = null;
  saveFeedback: { type: 'success' | 'error'; message: string } | null = null;
  photoFile: File | null = null;
  activeTab: 'family' | 'address' | 'members' | 'documents' | 'notes' = 'members';
  beneficiaryTab: 'data' | 'family' | 'socio' | 'documents' = 'data';
  readonly statusOptions = ['Ativo', 'Inativo', 'Em análise', 'Bloqueado'];
  readonly sanitationOptions = [
    'Água tratada',
    'Coleta de lixo',
    'Rede de esgoto',
    'Fossa séptica',
    'Água de poço',
    'Drenagem pluvial'
  ];
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
  readonly tabControlMap: Record<string, string[]> = {
    members: [
      'documentos',
      'nomeCompleto',
      'dataNascimento',
      'idade',
      'escolaridade',
      'rendaIndividual',
      'rendaFamiliar',
      'situacaoEmprego',
      'ocupacao',
      'possuiFilhosMenores',
      'quantidadeFilhosMenores',
      'possuiCnh',
      'informacoesMoradia',
      'condicoesSaneamento'
    ],
    family: ['nomeCompleto', 'nomeMae', 'telefone', 'email', 'status', 'motivoBloqueio'],
    address: ['cep', 'endereco', 'numeroEndereco', 'bairro', 'cidade', 'estado'],
    documents: ['arquivosDocumentos'],
    notes: ['observacoes']
  };
  private mediaStream?: MediaStream;
  private currentVideoElement?: HTMLVideoElement;
  private photoObjectUrl?: string;
  selectedSanitationOptions = new Set<string>();
  private routeSubscription?: Subscription;
  private currentEditingBeneficiary: BeneficiaryPayload | null = null;

  constructor(
    private readonly fb: FormBuilder,
    private readonly beneficiaryService: BeneficiaryService,
    private readonly router: Router,
    private readonly route: ActivatedRoute
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
      motivoBloqueio: [''],
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

    this.filterForm = this.fb.group({
      nome: [''],
      cpf: [''],
      idade: [''],
      dataNascimento: [''],
      bairro: [''],
      status: [[]]
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

    this.beneficiaryForm
      .get('status')
      ?.valueChanges.subscribe((value: string) => this.handleStatusChange(value));

    this.filterForm.valueChanges.subscribe(() => this.applyFilters());
  }

  ngOnInit(): void {
    this.loadRequiredDocuments();
    this.loadBeneficiaries();
    this.handleStatusChange(this.beneficiaryForm.get('status')?.value);
    this.syncSanitationSelections(this.beneficiaryForm.get('condicoesSaneamento')?.value);
    this.listenToRouteChanges();
  }

  submit(): void {
    this.saveFeedback = null;

    if (this.beneficiaryForm.invalid) {
      this.beneficiaryForm.markAllAsTouched();
      this.activeTab = this.findFirstInvalidTab();
      return;
    }

    const formValue = {
      ...this.beneficiaryForm.getRawValue(),
      id: this.editingId ?? undefined,
      documentosAnexos: this.requiredDocuments.map((doc) => ({ nome: doc.nome, nomeArquivo: doc.nomeArquivo })),
      foto: this.photoPreview || (this.beneficiaryForm.get('foto')?.value as string) || null
    } as BeneficiaryPayload;

    this.beneficiaryService.save(formValue, this.photoFile).subscribe({
      next: () => {
        this.resetForm();
        this.loadBeneficiaries();
        this.saveFeedback = { type: 'success', message: 'Família salva com sucesso.' };
      },
      error: (error) => {
        console.error('Erro ao salvar beneficiário', error);
        const missing = error?.error?.missingDocuments as string[] | undefined;
        const message = missing?.length
          ? `Documentos obrigatórios ausentes: ${missing.join(', ')}`
          : 'Erro ao salvar a família.';
        this.saveFeedback = { type: 'error', message };
      }
    });
  }

  setActiveTab(tab: 'family' | 'address' | 'members' | 'documents' | 'notes'): void {
    this.activeTab = tab;
  }

  setBeneficiaryTab(tab: 'data' | 'family' | 'socio' | 'documents'): void {
    this.beneficiaryTab = tab;
  }

  tabHasErrors(tab: 'family' | 'address' | 'members' | 'documents' | 'notes'): boolean {
    const controls = this.tabControlMap[tab];
    return controls?.some((controlName) => {
      const control = this.beneficiaryForm.get(controlName);
      return control?.invalid && control?.touched;
    });
  }

  get observacoesLength(): number {
    return (this.beneficiaryForm.get('observacoes')?.value as string)?.length ?? 0;
  }

  private findFirstInvalidTab(): 'family' | 'address' | 'members' | 'documents' | 'notes' {
    const target = Object.entries(this.tabControlMap).find(([, controls]) =>
      controls.some((controlName) => this.beneficiaryForm.get(controlName)?.invalid)
    );

    return (target?.[0] as typeof this.activeTab) ?? this.activeTab;
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
      this.revokePhotoObjectUrl();
      this.photoPreview = null;
      this.photoFile = null;
      this.photoError = null;
      this.beneficiaryForm.get('foto')?.setValue('');
      return;
    }

    const isImage = /^image\//.test(file.type);
    const maxSizeMb = 5;
    const isSizeValid = file.size <= maxSizeMb * 1024 * 1024;

    if (!isImage || !isSizeValid) {
      this.photoError = !isImage ? 'Formato não suportado. Use JPG ou PNG.' : 'A foto deve ter no máximo 5MB.';
      input.value = '';
      return;
    }

    this.photoError = null;

    this.revokePhotoObjectUrl();
    this.photoObjectUrl = URL.createObjectURL(file);
    this.photoPreview = this.photoObjectUrl;
    this.photoFile = file;

    const reader = new FileReader();
    reader.onload = () => {
      this.beneficiaryForm.get('foto')?.setValue(reader.result as string);
    };
    reader.readAsDataURL(file);
  }

  async startCamera(videoElement: HTMLVideoElement): Promise<void> {
    this.cameraError = null;
    this.photoError = null;
    this.cameraReady = false;

    try {
      if (!navigator.mediaDevices?.getUserMedia) {
        throw new Error('MEDIA_NOT_SUPPORTED');
      }

      this.stopCamera(videoElement);
      this.currentVideoElement = videoElement;
      this.mediaStream = await navigator.mediaDevices.getUserMedia({ video: true });
      this.cameraActive = true;
      videoElement.srcObject = this.mediaStream;
      await videoElement.play();
      await this.waitForVideoReady(videoElement);
      this.cameraReady = true;
    } catch (error) {
      console.error('Erro ao acessar a câmera', error);
      this.cameraActive = false;
      this.cameraReady = false;

      if (error instanceof DOMException && error.name === 'NotAllowedError') {
        this.cameraError = 'Permissão negada para acessar a câmera. Verifique as permissões do navegador.';
      } else if (error instanceof Error && error.message === 'MEDIA_NOT_SUPPORTED') {
        this.cameraError = 'Este dispositivo ou navegador não oferece suporte à captura por câmera.';
      } else {
        this.cameraError = 'Não foi possível inicializar a câmera. Confirme se há um dispositivo disponível.';
      }
    }
  }

  capturePhoto(videoElement: HTMLVideoElement, canvasElement: HTMLCanvasElement): void {
    this.cameraError = null;

    if (!this.cameraActive || !this.cameraReady) {
      this.cameraError = 'A câmera ainda não está ativa para captura.';
      return;
    }

    const context = canvasElement.getContext('2d');
    if (!context) {
      this.cameraError = 'Não foi possível preparar a captura da imagem.';
      return;
    }

    const { videoWidth, videoHeight } = videoElement;
    if (!videoWidth || !videoHeight) {
      this.cameraError = 'A câmera ainda está carregando. Tente novamente em instantes.';
      return;
    }

    canvasElement.width = videoWidth;
    canvasElement.height = videoHeight;
    context.drawImage(videoElement, 0, 0, videoWidth, videoHeight);
    this.photoPreview = canvasElement.toDataURL('image/png');
    this.photoFile = this.dataUrlToFile(this.photoPreview, 'foto.png');
    this.beneficiaryForm.get('foto')?.setValue(this.photoPreview);
    this.stopCamera(videoElement);
  }

  stopCamera(videoElement?: HTMLVideoElement): void {
    this.mediaStream?.getTracks().forEach((track) => track.stop());
    this.mediaStream = undefined;
    const targetVideo = videoElement ?? this.currentVideoElement;
    if (targetVideo) {
      targetVideo.pause();
      targetVideo.srcObject = null;
    }
    this.cameraActive = false;
    this.cameraReady = false;
  }

  private waitForVideoReady(videoElement: HTMLVideoElement): Promise<void> {
    return new Promise((resolve, reject) => {
      const handleReady = (): void => {
        videoElement.onloadedmetadata = null;
        videoElement.oncanplay = null;
        videoElement.onerror = null;
        resolve();
      };

      videoElement.onloadedmetadata = handleReady;
      videoElement.oncanplay = handleReady;
      videoElement.onerror = () => reject(new Error('FAILED_TO_START_CAMERA'));

      if (videoElement.readyState >= 2) {
        handleReady();
      }
    });
  }

  ngOnDestroy(): void {
    this.revokePhotoObjectUrl();
    this.stopCamera();
    this.routeSubscription?.unsubscribe();
  }

  startEdit(beneficiary: BeneficiaryPayload): void {
    if (!beneficiary.id) {
      return;
    }

    this.router.navigate(['/beneficiarios/editar', beneficiary.id]);
  }

  resetForm(): void {
    this.editingId = null;
    this.currentEditingBeneficiary = null;
    this.documentStatus = 'Pendente';
    this.activeTab = 'family';
    this.revokePhotoObjectUrl();
    this.photoPreview = null;
    this.photoFile = null;
    this.selectedSanitationOptions.clear();
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
      quantidadeFilhosMenores: '',
      motivoBloqueio: ''
    });
    this.beneficiaryForm.get('quantidadeFilhosMenores')?.disable({ emitEvent: false });
    this.toggleMinorChildrenRequirement(false);
    this.handleStatusChange('Ativo');
    this.updateSanitationControl();

    if (this.router.url.includes('/beneficiarios/editar/')) {
      this.router.navigate(['/beneficiarios/cadastro']);
    }
  }

  private listenToRouteChanges(): void {
    this.routeSubscription = this.route.paramMap.subscribe((params) => {
      const idParam = params.get('id');

      if (idParam) {
        this.loadBeneficiaryForEdit(Number(idParam));
      } else if (this.editingId) {
        this.resetForm();
      }
    });
  }

  private loadBeneficiaryForEdit(id: number): void {
    this.beneficiaryService.getById(id).subscribe({
      next: (beneficiary) => this.applyEditState(beneficiary),
      error: (error) => {
        console.error('Erro ao carregar beneficiário', error);
        this.saveFeedback = { type: 'error', message: 'Não foi possível carregar o beneficiário selecionado.' };
      }
    });
  }

  private applyEditState(beneficiary: BeneficiaryPayload): void {
    this.currentEditingBeneficiary = beneficiary;
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
    this.revokePhotoObjectUrl();
    this.photoPreview = beneficiary.foto || null;
    this.photoFile = null;
    this.toggleMinorChildrenRequirement(Boolean(beneficiary.possuiFilhosMenores));
    this.syncSanitationSelections(beneficiary.condicoesSaneamento);
    this.handleStatusChange(beneficiary.status);
    this.updateConditionalDocumentRequirements();
  }

  private revokePhotoObjectUrl(): void {
    if (this.photoObjectUrl) {
      URL.revokeObjectURL(this.photoObjectUrl);
      this.photoObjectUrl = undefined;
    }
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

        if (this.editingId && this.currentEditingBeneficiary?.id === this.editingId) {
          this.applyEditState(this.currentEditingBeneficiary);
        }
      },
      error: (error) => console.error('Erro ao carregar documentos obrigatórios', error)
    });
  }

  private loadBeneficiaries(): void {
    this.beneficiaryService.list().subscribe({
      next: ({ beneficiarios }) => {
        this.beneficiaries = this.addAgeToBeneficiaries(beneficiarios);
        this.applyFilters();
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

  private handleStatusChange(status: string | null): void {
    const reasonControl = this.beneficiaryForm.get('motivoBloqueio');

    if (!reasonControl) {
      return;
    }

    if (status === 'Bloqueado') {
      reasonControl.setValidators([Validators.required, Validators.minLength(3)]);
    } else {
      reasonControl.clearValidators();
      reasonControl.reset('', { emitEvent: false });
    }

    reasonControl.updateValueAndValidity({ emitEvent: false });
  }

  toggleSanitationOption(option: string, checked: boolean): void {
    if (checked) {
      this.selectedSanitationOptions.add(option);
    } else {
      this.selectedSanitationOptions.delete(option);
    }

    this.updateSanitationControl();
  }

  private updateSanitationControl(): void {
    const control = this.beneficiaryForm.get('condicoesSaneamento');

    if (!control) {
      return;
    }

    const value = Array.from(this.selectedSanitationOptions).join(', ');
    control.setValue(value, { emitEvent: false });
  }

  private dataUrlToFile(dataUrl: string, filename: string): File | null {
    const parts = dataUrl.split(',');

    if (parts.length < 2) {
      return null;
    }

    const match = parts[0].match(/:(.*?);/);
    const mime = match?.[1] ?? 'image/png';
    const binary = atob(parts[1]);
    const array = new Uint8Array(binary.length);

    for (let i = 0; i < binary.length; i++) {
      array[i] = binary.charCodeAt(i);
    }

    return new File([array], filename, { type: mime });
  }

  formatCpf(value: string | undefined | null): string {
    const digits = (value ?? '').replace(/\D/g, '');

    if (digits.length !== 11) {
      return value ?? '—';
    }

    return digits.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
  }

  formatAddress(beneficiary: BeneficiaryPayload): string {
    const cityState = [beneficiary.cidade, beneficiary.estado].filter(Boolean).join(' - ');
    const addressParts = [
      beneficiary.endereco,
      beneficiary.numeroEndereco,
      beneficiary.bairro,
      cityState
    ].filter((part) => Boolean(part && String(part).trim()));

    return addressParts.length ? addressParts.join(', ') : '—';
  }

  formatContact(beneficiary: BeneficiaryPayload): string {
    return beneficiary.telefone?.trim() || '—';
  }

  getParentesco(beneficiary: BeneficiaryPayload): string {
    return beneficiary.parentesco?.trim() || '—';
  }

  private syncSanitationSelections(value: string | string[] | null | undefined): void {
    const entries = Array.isArray(value)
      ? value
      : (value ?? '')
          .split(',')
          .map((item) => item.trim())
          .filter(Boolean);

    this.selectedSanitationOptions = new Set(entries);
    this.updateSanitationControl();
  }

  private addAgeToBeneficiaries(list: BeneficiaryPayload[]): BeneficiaryPayload[] {
    return list.map((beneficiary) => {
      const idade = beneficiary.idade ?? this.calculateAgeFromDate(beneficiary.dataNascimento);
      return { ...beneficiary, idade: idade ?? beneficiary.idade };
    });
  }

  private calculateAgeFromDate(dateString?: string): number | null {
    if (!dateString) {
      return null;
    }

    const birthDate = new Date(dateString);

    if (Number.isNaN(birthDate.getTime())) {
      return null;
    }

    const today = new Date();
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();

    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
      age--;
    }

    return age >= 0 ? age : null;
  }

  toggleStatusFilter(status: string, checked: boolean): void {
    const control = this.filterForm.get('status');

    if (!control) {
      return;
    }

    const current = (control.value as string[]) ?? [];
    const updated = checked
      ? Array.from(new Set([...current, status]))
      : current.filter((item) => item !== status);

    control.setValue(updated, { emitEvent: false });
    this.applyFilters();
  }

  applyFilters(): void {
    const { nome, cpf, idade, dataNascimento, bairro, status } = this.filterForm.getRawValue();
    const statusFilters = (status as string[])?.filter(Boolean) ?? [];

    this.filteredBeneficiaries = this.beneficiaries.filter((beneficiary) => {
      const matchesNome = nome
        ? beneficiary.nomeCompleto?.toLowerCase().includes((nome as string).toLowerCase())
        : true;
      const matchesCpf = cpf ? beneficiary.documentos?.includes(cpf as string) : true;
      const matchesIdade = idade ? beneficiary.idade === Number(idade) : true;
      const matchesData = dataNascimento ? beneficiary.dataNascimento === dataNascimento : true;
      const matchesBairro = bairro
        ? (beneficiary.bairro ?? '').toLowerCase().includes((bairro as string).toLowerCase())
        : true;
      const matchesStatus = statusFilters.length ? statusFilters.includes(beneficiary.status) : true;

      return matchesNome && matchesCpf && matchesIdade && matchesData && matchesBairro && matchesStatus;
    });
  }

  clearFilters(): void {
    this.filterForm.reset({ nome: '', cpf: '', idade: '', dataNascimento: '', bairro: '', status: [] });
    this.applyFilters();
  }
}
