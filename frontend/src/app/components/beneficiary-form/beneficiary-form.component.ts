import { CommonModule } from '@angular/common';
import { Component, OnDestroy } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators
} from '@angular/forms';

@Component({
  selector: 'app-beneficiary-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './beneficiary-form.component.html',
  styleUrl: './beneficiary-form.component.scss'
})
export class BeneficiaryFormComponent implements OnDestroy {
  beneficiaryForm: FormGroup;
  documentStatus = 'Pendente';
  uploadedDocuments: File[] = [];
  photoPreview: string | null = null;
  cameraActive = false;
  private mediaStream?: MediaStream;

  constructor(private readonly fb: FormBuilder) {
    this.beneficiaryForm = this.fb.group({
      zipCode: ['', [Validators.required, this.cepValidator]],
      fullName: ['', [Validators.required, Validators.minLength(3)]],
      motherName: [''],
      document: ['', [Validators.required, this.cpfValidator]],
      birthDate: ['', Validators.required],
      age: [{ value: '', disabled: true }],
      phone: ['', [Validators.required, this.phoneValidator]],
      email: ['', [Validators.required, Validators.email]],
      address: ['', Validators.required],
      neighborhood: [''],
      city: [''],
      state: [''],
      notes: [''],
      status: ['Ativo', Validators.required],
      documentFiles: [[], [this.documentsRequiredValidator]],
      photo: ['']
    });

    this.beneficiaryForm
      .get('birthDate')
      ?.valueChanges.subscribe((value: string | null) => this.updateAge(value));
  }

  submit(): void {
    if (this.beneficiaryForm.invalid) {
      this.beneficiaryForm.markAllAsTouched();
      return;
    }

    const formValue = this.beneficiaryForm.value;
    console.table(formValue);
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

    this.beneficiaryForm.get('document')?.setValue(formatted, { emitEvent: false });
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

    this.beneficiaryForm.get('phone')?.setValue(formatted, { emitEvent: false });
  }

  handleCepInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const digits = input.value.replace(/\D/g, '').slice(0, 8);

    let formatted = digits;
    if (digits.length > 5) {
      formatted = `${digits.slice(0, 5)}-${digits.slice(5)}`;
    }

    this.beneficiaryForm.get('zipCode')?.setValue(formatted, { emitEvent: false });

    if (digits.length === 8) {
      this.fetchAddressByCep(digits);
    } else {
      this.removeZipError('invalidCep');
    }
  }

  private async fetchAddressByCep(cep: string): Promise<void> {
    const zipControl = this.beneficiaryForm.get('zipCode');

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
        address: data.logradouro || '',
        neighborhood: data.bairro || '',
        city: data.localidade || '',
        state: data.uf || ''
      });

      this.removeZipError('invalidCep');
    } catch (error) {
      console.error('Erro ao buscar CEP', error);
      this.clearAddressFields();
      this.setZipError('invalidCep');
    }
  }

  private clearAddressFields(): void {
    this.beneficiaryForm.patchValue({ address: '', neighborhood: '', city: '', state: '' });
  }

  private setZipError(errorKey: string): void {
    const zipControl = this.beneficiaryForm.get('zipCode');

    if (!zipControl) {
      return;
    }

    const existingErrors = zipControl.errors ?? {};
    zipControl.setErrors({ ...existingErrors, [errorKey]: true });
  }

  private removeZipError(errorKey: string): void {
    const zipControl = this.beneficiaryForm.get('zipCode');

    if (!zipControl?.errors) {
      return;
    }

    const { [errorKey]: _, ...rest } = zipControl.errors;
    zipControl.setErrors(Object.keys(rest).length ? rest : null);
  }

  private updateAge(dateString: string | null): void {
    if (!dateString) {
      this.beneficiaryForm.get('age')?.setValue('', { emitEvent: false });
      return;
    }

    const birthDate = new Date(dateString);
    const today = new Date();
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();

    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
      age--;
    }

    this.beneficiaryForm.get('age')?.setValue(age >= 0 ? age : '', { emitEvent: false });
  }

  onDocumentUpload(event: Event): void {
    const input = event.target as HTMLInputElement;
    const files = input.files ? Array.from(input.files) : [];
    this.uploadedDocuments = files;
    this.documentStatus = files.length ? 'Enviado' : 'Pendente';
    const documentControl = this.beneficiaryForm.get('documentFiles');
    documentControl?.setValue(files);
    documentControl?.markAsTouched();
  }

  handlePhotoUpload(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];

    if (!file) {
      return;
    }

    const reader = new FileReader();
    reader.onload = () => {
      this.photoPreview = reader.result as string;
      this.beneficiaryForm.get('photo')?.setValue(file.name);
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
    this.beneficiaryForm.get('photo')?.setValue('captured-photo.png');
    this.stopCamera();
  }

  stopCamera(): void {
    this.mediaStream?.getTracks().forEach((track) => track.stop());
    this.cameraActive = false;
  }

  ngOnDestroy(): void {
    this.stopCamera();
  }

  private documentsRequiredValidator = (control: AbstractControl): ValidationErrors | null => {
    const files = control.value as File[] | undefined;

    return Array.isArray(files) && files.length > 0 ? null : { documentsRequired: true };
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
