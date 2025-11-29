import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserPayload, UserService } from '../../services/user.service';

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-management.component.html',
  styleUrl: './user-management.component.scss'
})
export class UserManagementComponent implements OnInit {
  form: FormGroup;
  users: UserPayload[] = [];
  editingId: number | null = null;
  loading = false;
  feedback: { type: 'success' | 'error'; message: string } | null = null;

  constructor(
    private readonly fb: FormBuilder,
    private readonly userService: UserService
  ) {
    this.form = this.fb.group({
      nomeUsuario: ['', [Validators.required, Validators.minLength(3)]],
      senha: ['', [Validators.minLength(4)]]
    });
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading = true;
    this.userService.list().subscribe({
      next: (users) => (this.users = users),
      error: (error) => {
        console.error('Erro ao carregar usuários', error);
        this.feedback = { type: 'error', message: 'Não foi possível carregar os usuários.' };
      },
      complete: () => (this.loading = false)
    });
  }

  submit(): void {
    this.feedback = null;
    if (this.form.invalid || (!this.editingId && !this.form.get('senha')?.value)) {
      this.form.markAllAsTouched();
      return;
    }

    const { nomeUsuario, senha } = this.form.value as { nomeUsuario: string; senha?: string };

    const request$ = this.editingId
      ? this.userService.update(this.editingId, { nomeUsuario, ...(senha ? { senha } : {}) })
      : this.userService.create({ nomeUsuario, senha: senha || '' });

    request$.subscribe({
      next: () => {
        this.feedback = { type: 'success', message: 'Usuário salvo com sucesso.' };
        this.resetForm();
        this.loadUsers();
      },
      error: (error) => {
        console.error('Erro ao salvar usuário', error);
        const message = error?.error?.message ?? 'Não foi possível salvar o usuário.';
        this.feedback = { type: 'error', message };
      }
    });
  }

  edit(user: UserPayload): void {
    this.editingId = user.id;
    this.form.reset({ nomeUsuario: user.nomeUsuario, senha: '' });
  }

  delete(user: UserPayload): void {
    const confirmDelete = confirm(`Deseja realmente remover o usuário "${user.nomeUsuario}"?`);
    if (!confirmDelete) {
      return;
    }

    this.userService.delete(user.id).subscribe({
      next: () => {
        this.feedback = { type: 'success', message: 'Usuário removido com sucesso.' };
        this.loadUsers();
        if (this.editingId === user.id) {
          this.resetForm();
        }
      },
      error: (error) => {
        console.error('Erro ao remover usuário', error);
        this.feedback = { type: 'error', message: 'Não foi possível remover o usuário.' };
      }
    });
  }

  resetForm(): void {
    this.editingId = null;
    this.form.reset({ nomeUsuario: '', senha: '' });
  }
}
