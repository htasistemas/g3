import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { LayoutComponent } from './layout/layout.component';
import { authGuard } from './guards/auth.guard';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { DashboardIndicatorsComponent } from './components/dashboard-indicators/dashboard-indicators.component';
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
import { ProfissionaisCadastroComponent } from './components/profissionais-cadastro/profissionais-cadastro.component';
import { PlanoTrabalhoGestaoComponent } from './components/plano-trabalho-gestao/plano-trabalho-gestao.component';
import { TermosFomentoGestaoComponent } from './components/termos-fomento-gestao/termos-fomento-gestao.component';
import { VisitaDomiciliarGestaoComponent } from './components/visita-domiciliar-gestao/visita-domiciliar-gestao.component';
import { TarefasPendenciasComponent } from './components/tarefas-pendencias/tarefas-pendencias.component';
import { GeoreferenciamentoLocalizacaoComponent } from './components/georeferenciamento-localizacao/georeferenciamento-localizacao.component';
import { DashboardBiComponent } from './components/dashboard-bi/dashboard-bi.component';
import { AssistenteTextosComponent } from './components/assistente-textos/assistente-textos.component';
import { ProntuarioComponent } from './components/prontuario/prontuario.component';
import { BancoEmpregosComponent } from './components/banco-empregos/banco-empregos.component';
import { VinculoFamiliarComponent } from './components/vinculo-familiar/vinculo-familiar.component';

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
      { path: 'dashboard/gerencial', component: DashboardBiComponent, data: { title: 'Dashboard gerencial' } },
      { path: 'beneficiarios', component: BeneficiaryListComponent, data: { title: 'Beneficiários' } },
      {
        path: 'beneficiarios/:id/prontuario',
        component: ProntuarioComponent,
        data: { title: 'Prontuário' }
      },
      { path: 'beneficiarios/novo', redirectTo: 'cadastros/beneficiarios', pathMatch: 'full' },
      { path: 'beneficiarios/editar/:id', redirectTo: 'cadastros/beneficiarios/:id', pathMatch: 'full' },
      { path: 'unidades/cadastro', component: AssistanceUnitComponent, data: { title: 'Unidades assistenciais' } },
      { path: 'cadastros/beneficiarios', component: BeneficiarioCadastroComponent, data: { title: 'Cadastro de Beneficiário' } },
      { path: 'cadastros/beneficiarios/:id', component: BeneficiarioCadastroComponent, data: { title: 'Editar Beneficiário' } },
      { path: 'cadastros/vinculo-familiar', component: VinculoFamiliarComponent, data: { title: 'Vínculo familiar' } },
      {
        path: 'cadastros/voluntariados',
        component: VoluntariadoCadastroComponent,
        data: { title: 'Cadastro de Voluntário' }
      },
      {
        path: 'cadastros/profissionais',
        component: ProfissionaisCadastroComponent,
        data: { title: 'Cadastro de Profissionais' }
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
        path: 'atendimentos/banco-empregos',
        component: BancoEmpregosComponent,
        data: { title: 'Banco de Empregos' }
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
        path: 'administrativo/tarefas',
        component: TarefasPendenciasComponent,
        data: { title: 'Tarefas e pendências' }
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
      },
      {
        path: 'assistente-textos',
        component: AssistenteTextosComponent,
        data: { title: 'Assistente de textos' }
      },
      {
        path: 'georeferenciamento/localizacao',
        component: GeoreferenciamentoLocalizacaoComponent,
        data: { title: 'Georeferenciamento' }
      }
    ]
  },
  { path: '**', redirectTo: '' }
];
