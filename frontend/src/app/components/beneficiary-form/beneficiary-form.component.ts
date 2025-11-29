import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-beneficiary-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './beneficiary-form.component.html',
  styleUrl: './beneficiary-form.component.scss'
})
export class BeneficiaryFormComponent {
  beneficiaryForm: FormGroup;

  constructor(private readonly fb: FormBuilder) {
    this.beneficiaryForm = this.fb.group({
      fullName: ['', [Validators.required, Validators.minLength(3)]],
      document: ['', Validators.required],
      birthDate: ['', Validators.required],
      phone: [''],
      email: ['', Validators.email],
      address: ['', Validators.required],
      neighborhood: [''],
      city: [''],
      state: [''],
      notes: ['']
    });
  }

  submit(): void {
    if (this.beneficiaryForm.invalid) {
      this.beneficiaryForm.markAllAsTouched();
      return;
    }

    const formValue = this.beneficiaryForm.value;
    console.table(formValue);
  }
}
