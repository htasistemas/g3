import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { LayoutComponent } from './layout/layout.component';
import { authGuard } from './guards/auth.guard';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { DashboardIndicatorsComponent } from './components/dashboard-indicators/dashboard-indicators.component';
import { BeneficiaryFormComponent } from './components/beneficiary-form/beneficiary-form.component';
import { BeneficiaryListComponent } from './components/beneficiary-list/beneficiary-list.component';
import { AssistanceUnitComponent } from './components/assistance-unit/assistance-unit.component';
import { SystemParametersComponent } from './components/system-parameters/system-parameters.component';
import { BeneficiarioCadastroComponent } from './components/beneficiario-cadastro/beneficiario-cadastro.component';
import { SystemVersionComponent } from './components/system-version/system-version.component';
import { PatrimonioComponent } from './components/patrimonio/patrimonio.component';
import { DonationManagementComponent } from './components/donation-management/donation-management.component';
import { AlmoxarifadoComponent } from './components/almoxarifado/almoxarifado.component';
import { VoluntariadoCadastroComponent } from './components/voluntariado-cadastro/voluntariado-cadastro.component';
import { ContabilidadeComponent } from './components/contabilidade/contabilidade.component';
import { PrestacaoContasComponent } from './components/prestacao-contas/prestacao-contas.component';
import { OficiosGestaoComponent } from './components/oficios-gestao/oficios-gestao.component';
import { DocumentosInstitucionaisComponent } from './components/documentos-institucionais/documentos-institucionais.component';
import { CursosAtendimentosComponent } from './components/cursos-atendimentos/cursos-atendimentos.component';
import { PlanoTrabalhoGestaoComponent } from './components/plano-trabalho-gestao/plano-trabalho-gestao.component';
import { TermosFomentoGestaoComponent } from './components/termos-fomento-gestao/termos-fomento-gestao.component';
import { VisitaDomiciliarGestaoComponent } from './components/visita-domiciliar-gestao/visita-domiciliar-gestao.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'dashboard/visao-geral', pathMatch: 'full' },
      { path: 'dashboard/visao-geral', component: DashboardComponent, data: { title: 'Visão geral' } },
      { path: 'dashboard/indicadores', component: DashboardIndicatorsComponent, data: { title: 'Indicadores' } },
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
      { path: 'unidades/cadastro', component: AssistanceUnitComponent, data: { title: 'Unidades assistenciais' } },
      { path: 'cadastros/beneficiarios', component: BeneficiarioCadastroComponent, data: { title: 'Cadastro de Beneficiário' } },
      { path: 'cadastros/beneficiarios/:id', component: BeneficiarioCadastroComponent, data: { title: 'Editar Beneficiário' } },
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
        path: 'atendimentos/visitas',
        component: VisitaDomiciliarGestaoComponent,
        data: { title: 'Visita domiciliar' }
      },
      {
        path: 'atendimentos/cursos',
        component: CursosAtendimentosComponent,
        data: { title: 'Cursos e atendimentos' }
      },
      {
        path: 'administrativo/patrimonio',
        component: PatrimonioComponent,
        data: { title: 'Controle patrimonial' }
      },
      {
        path: 'administrativo/oficios',
        component: OficiosGestaoComponent,
        data: { title: 'Gestão de ofícios' }
      },
      {
        path: 'administrativo/documentos',
        component: DocumentosInstitucionaisComponent,
        data: { title: 'Gestão de Documentos da Instituição' }
      },
      {
        path: 'administrativo/almoxarifado',
        component: AlmoxarifadoComponent,
        data: { title: 'Almoxarifado' }
      },
      {
        path: 'configuracoes/versao',
        component: SystemVersionComponent,
        data: { title: 'Versão do sistema' }
      },
      {
        path: 'configuracoes/parametros',
        component: SystemParametersComponent,
        data: { title: 'Parâmetros do sistema' }
      },
      { path: 'configuracoes/sistema', redirectTo: 'configuracoes/parametros', pathMatch: 'full' },
      {
        path: 'configuracoes/modelos-texto',
        redirectTo: 'configuracoes/parametros',
        pathMatch: 'full'
      },
      { path: 'configuracoes/usuarios', redirectTo: 'configuracoes/parametros', pathMatch: 'full' },
      {
        path: 'configuracoes/personalizacao',
        redirectTo: 'configuracoes/parametros',
        pathMatch: 'full'
      },
      {
        path: 'financeiro/contabilidade',
        component: ContabilidadeComponent,
        data: { title: 'Contabilidade' }
      },
      {
        path: 'financeiro/prestacao-contas',
        component: PrestacaoContasComponent,
        data: { title: 'Prestação de contas' }
      },
      {
        path: 'juridico/planos-trabalho',
        component: PlanoTrabalhoGestaoComponent,
        data: { title: 'Plano de Trabalho' }
      },
      {
        path: 'juridico/termos-fomento',
        component: TermosFomentoGestaoComponent,
        data: { title: 'Termos de Fomento' }
      }
    ]
  },
  { path: '**', redirectTo: '' }
];
