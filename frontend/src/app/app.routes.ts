import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { LayoutComponent } from './layout/layout.component';
import { authGuard } from './guards/auth.guard';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { BeneficiaryFormComponent } from './components/beneficiary-form/beneficiary-form.component';
import { BeneficiaryListComponent } from './components/beneficiary-list/beneficiary-list.component';
import { AssistanceUnitComponent } from './components/assistance-unit/assistance-unit.component';
import { SystemSettingsComponent } from './components/system-settings/system-settings.component';
import { UserManagementComponent } from './components/user-management/user-management.component';
import { SystemParametersComponent } from './components/system-parameters/system-parameters.component';
import { FamilyCompositionComponent } from './components/family-composition/family-composition.component';
import { BeneficiarioCadastroComponent } from './components/beneficiario-cadastro/beneficiario-cadastro.component';
import { FamiliaCadastroComponent } from './components/familia-cadastro/familia-cadastro.component';
import { SystemVersionComponent } from './components/system-version/system-version.component';
import { PatrimonioComponent } from './components/patrimonio/patrimonio.component';
import { PersonalizacaoComponent } from './components/personalizacao/personalizacao.component';
import { TextTemplatesComponent } from './components/text-templates/text-templates.component';
import { DonationManagementComponent } from './components/donation-management/donation-management.component';
import { AlmoxarifadoComponent } from './components/almoxarifado/almoxarifado.component';
import { VoluntariadoCadastroComponent } from './components/voluntariado-cadastro/voluntariado-cadastro.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: '', component: DashboardComponent, data: { title: 'Visão geral' } },
      { path: 'beneficiarios', component: BeneficiaryListComponent, data: { title: 'Beneficiários' } },
      {
        path: 'beneficiarios/editar/:id',
        component: BeneficiaryFormComponent,
        data: { title: 'Beneficiários' }
      },
      {
        path: 'beneficiarios/cadastro',
        component: BeneficiaryFormComponent,
        data: { title: 'Beneficiários' }
      },
      {
        path: 'beneficiarios/novo',
        component: BeneficiaryFormComponent,
        data: { title: 'Beneficiários' }
      },
      { path: 'familias', component: FamilyCompositionComponent, data: { title: 'Composição familiar' } },
      { path: 'unidades/cadastro', component: AssistanceUnitComponent, data: { title: 'Unidades assistenciais' } },
      { path: 'cadastros/beneficiarios', component: BeneficiarioCadastroComponent, data: { title: 'Cadastro de Beneficiário' } },
      { path: 'cadastros/beneficiarios/:id', component: BeneficiarioCadastroComponent, data: { title: 'Editar Beneficiário' } },
      { path: 'cadastros/familias', component: FamiliaCadastroComponent, data: { title: 'Cadastro de Família' } },
      { path: 'cadastros/familias/:id', component: FamiliaCadastroComponent, data: { title: 'Editar Família' } },
      {
        path: 'cadastros/voluntariados',
        component: VoluntariadoCadastroComponent,
        data: { title: 'Cadastro de Voluntário' }
      },
      {
        path: 'atendimentos/doacoes',
        component: DonationManagementComponent,
        data: { title: 'Registrar doações' }
      },
      {
        path: 'administrativo/patrimonio',
        component: PatrimonioComponent,
        data: { title: 'Controle patrimonial' }
      },
      {
        path: 'administrativo/almoxarifado',
        component: AlmoxarifadoComponent,
        data: { title: 'Almoxarifado' }
      },
      {
        path: 'configuracoes/sistema',
        component: SystemSettingsComponent,
        data: { title: 'Documentos obrigatórios' }
      },
      {
        path: 'configuracoes/modelos-texto',
        component: TextTemplatesComponent,
        data: { title: 'Modelos de textos' }
      },
      {
        path: 'configuracoes/versao',
        component: SystemVersionComponent,
        data: { title: 'Versão do sistema' }
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
      },
      {
        path: 'configuracoes/personalizacao',
        component: PersonalizacaoComponent,
        data: { title: 'Personalização' }
      }
    ]
  },
  { path: '**', redirectTo: '' }
];
