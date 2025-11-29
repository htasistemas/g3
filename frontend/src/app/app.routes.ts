import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { LayoutComponent } from './layout/layout.component';
import { authGuard } from './guards/auth.guard';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { BeneficiaryFormComponent } from './components/beneficiary-form/beneficiary-form.component';
import { AssistanceUnitComponent } from './components/assistance-unit/assistance-unit.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: '', component: DashboardComponent },
      { path: 'beneficiarios/cadastro', component: BeneficiaryFormComponent },
      { path: 'unidades/cadastro', component: AssistanceUnitComponent }
    ]
  },
  { path: '**', redirectTo: '' }
];
