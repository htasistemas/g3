import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { LayoutComponent } from './layout/layout.component';
import { authGuard } from './guards/auth.guard';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { BeneficiaryFormComponent } from './components/beneficiary-form/beneficiary-form.component';
import { AssistanceUnitComponent } from './components/assistance-unit/assistance-unit.component';
import { SystemSettingsComponent } from './components/system-settings/system-settings.component';
import { UserManagementComponent } from './components/user-management/user-management.component';
import { SystemParametersComponent } from './components/system-parameters/system-parameters.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: '', component: DashboardComponent, data: { title: 'Visão geral' } },
      {
        path: 'beneficiarios/editar/:id',
        component: BeneficiaryFormComponent,
        data: { title: 'Cadastro de famílias' }
      },
      {
        path: 'beneficiarios/cadastro',
        component: BeneficiaryFormComponent,
        data: { title: 'Cadastro de famílias' }
      },
      { path: 'unidades/cadastro', component: AssistanceUnitComponent, data: { title: 'Unidades assistenciais' } },
      {
        path: 'configuracoes/sistema',
        component: SystemSettingsComponent,
        data: { title: 'Documentos obrigatórios' }
      },
      {
        path: 'configuracoes/usuarios',
        component: UserManagementComponent,
        data: { title: 'Usuários e permissões' }
      },
      {
        path: 'configuracoes/parametros',
        component: SystemParametersComponent,
        data: { title: 'Parâmetros do sistema' }
      }
    ]
  },
  { path: '**', redirectTo: '' }
];
