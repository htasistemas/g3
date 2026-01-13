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
import { RecebimentoDoacaoComponent } from './components/recebimento-doacao/recebimento-doacao.component';
import { AlmoxarifadoComponent } from './components/almoxarifado/almoxarifado.component';
import { VoluntariadoCadastroComponent } from './components/voluntariado-cadastro/voluntariado-cadastro.component';
import { ContabilidadeComponent } from './components/contabilidade/contabilidade.component';
import { PrestacaoContasComponent } from './components/prestacao-contas/prestacao-contas.component';
import { OficiosGestaoComponent } from './components/oficios-gestao/oficios-gestao.component';
import { DocumentosInstitucionaisComponent } from './components/documentos-institucionais/documentos-institucionais.component';
import { FeriadosGestaoComponent } from './components/feriados-gestao/feriados-gestao.component';
import { CursosAtendimentosComponent } from './components/cursos-atendimentos/cursos-atendimentos.component';
import { ProfissionaisCadastroComponent } from './components/profissionais-cadastro/profissionais-cadastro.component';
import { PlanoTrabalhoGestaoComponent } from './components/plano-trabalho-gestao/plano-trabalho-gestao.component';
import { TermosFomentoGestaoComponent } from './components/termos-fomento-gestao/termos-fomento-gestao.component';
import { VisitaDomiciliarGestaoComponent } from './components/visita-domiciliar-gestao/visita-domiciliar-gestao.component';
import { TarefasPendenciasComponent } from './components/tarefas-pendencias/tarefas-pendencias.component';
import { GeoreferenciamentoLocalizacaoComponent } from './components/georeferenciamento-localizacao/georeferenciamento-localizacao.component';
import { ProntuarioPageComponent } from './components/prontuario-page/prontuario-page.component';
import { BancoEmpregosComponent } from './components/banco-empregos/banco-empregos.component';
import { VinculoFamiliarComponent } from './components/vinculo-familiar/vinculo-familiar.component';
import { DataManagementComponent } from './components/data-management/data-management.component';
import { AutorizacaoComprasComponent } from './components/autorizacao-compras/autorizacao-compras.component';
import { FotosEventosComponent } from './components/fotos-eventos/fotos-eventos.component';
import { ChamadosTecnicosComponent } from './components/chamados-tecnicos/chamados-tecnicos.component';
import { ChamadoTecnicoDetalheComponent } from './components/chamado-tecnico-detalhe/chamado-tecnico-detalhe.component';
import { ChamadoTecnicoKanbanComponent } from './components/chamado-tecnico-kanban/chamado-tecnico-kanban.component';
import { AlertasSistemaComponent } from './components/alertas-sistema/alertas-sistema.component';
import { OcorrenciasCriancaComponent } from './components/ocorrencias-crianca/ocorrencias-crianca.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'dashboard/visao-geral', pathMatch: 'full' },
      { path: 'dashboard/visao-geral', component: DashboardComponent, data: { title: 'Visao geral' } },
      { path: 'dashboard/indicadores', component: DashboardIndicatorsComponent, data: { title: 'Indicadores' } },
      { path: 'beneficiarios', component: BeneficiaryListComponent, data: { title: 'Beneficiarios' } },
      { path: 'beneficiarios/:id/prontuario', redirectTo: 'atendimentos/prontuario/:id', pathMatch: 'full' },
      { path: 'atendimentos/prontuario', component: ProntuarioPageComponent, data: { title: 'Prontuario' } },
      {
        path: 'atendimentos/prontuario/:beneficiarioId',
        component: ProntuarioPageComponent,
        data: { title: 'Prontuario' }
      },
      { path: 'beneficiarios/novo', redirectTo: 'cadastros/beneficiarios', pathMatch: 'full' },
      { path: 'beneficiarios/editar/:id', redirectTo: 'cadastros/beneficiarios/:id', pathMatch: 'full' },
      { path: 'unidades/cadastro', component: AssistanceUnitComponent, data: { title: 'Unidades assistenciais' } },
      { path: 'cadastros/beneficiarios', component: BeneficiarioCadastroComponent, data: { title: 'Cadastro de Beneficiario' } },
      {
        path: 'cadastros/beneficiarios/:id',
        component: BeneficiarioCadastroComponent,
        data: { title: 'Editar Beneficiario' }
      },
      { path: 'cadastros/vinculo-familiar', component: VinculoFamiliarComponent, data: { title: 'Vinculo familiar' } },
      {
        path: 'cadastros/voluntariados',
        component: VoluntariadoCadastroComponent,
        data: { title: 'Cadastro de Voluntario' }
      },
      {
        path: 'cadastros/profissionais',
        component: ProfissionaisCadastroComponent,
        data: { title: 'Cadastro de Profissionais' }
      },
      {
        path: 'atendimentos/doacoes',
        component: DonationManagementComponent,
        data: { title: 'Registrar doacoes' }
      },
      {
        path: 'atendimentos/recebimento-doacao',
        component: RecebimentoDoacaoComponent,
        data: { title: 'Recebimento de doacao' }
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
        path: 'atendimentos/ocorrencias-crianca',
        component: OcorrenciasCriancaComponent,
        data: { title: 'Registro de violencia contra crianca e adolescente' }
      },
      {
        path: 'administrativo/patrimonio',
        component: PatrimonioComponent,
        data: { title: 'Controle patrimonial' }
      },
      {
        path: 'administrativo/patrimonio/emprestimos-eventos',
        loadChildren: () =>
          import('./components/emprestimos-eventos/emprestimos-eventos.module').then(
            (m) => m.EmprestimosEventosModule
          ),
        data: { title: 'Emprestimo para eventos' }
      },
      {
        path: 'administrativo/oficios',
        component: OficiosGestaoComponent,
        data: { title: 'Gestao de oficios' }
      },
      {
        path: 'administrativo/feriados',
        component: FeriadosGestaoComponent,
        data: { title: 'Cadastro de feriados' }
      },
      {
        path: 'administrativo/documentos',
        component: DocumentosInstitucionaisComponent,
        data: { title: 'Gestao de Documentos da Instituicao' }
      },
      {
        path: 'administrativo/almoxarifado',
        component: AlmoxarifadoComponent,
        data: { title: 'Almoxarifado' }
      },
      {
        path: 'administrativo/tarefas',
        component: TarefasPendenciasComponent,
        data: { title: 'Tarefas e pendencias' }
      },
      {
        path: 'administrativo/fotos-eventos',
        component: FotosEventosComponent,
        data: { title: 'Fotos e Eventos' }
      },
      {
        path: 'administrativo/fotos-eventos/:id',
        component: FotosEventosComponent,
        data: { title: 'Detalhe do evento' }
      },
      {
        path: 'configuracoes/versao',
        component: SystemVersionComponent,
        data: { title: 'Versao do sistema' }
      },
      {
        path: 'configuracoes/chamados-tecnicos',
        component: ChamadoTecnicoKanbanComponent,
        data: { title: 'Chamado tecnico', perfil: 'usuario' }
      },
      {
        path: 'configuracoes/chamados-tecnicos/lista',
        component: ChamadosTecnicosComponent,
        data: { title: 'Chamados tecnicos', perfil: 'usuario' }
      },
      {
        path: 'configuracoes/chamados-tecnicos/novo',
        component: ChamadoTecnicoDetalheComponent,
        data: { title: 'Novo chamado tecnico', perfil: 'usuario' }
      },
      {
        path: 'configuracoes/chamados-tecnicos/:id',
        component: ChamadoTecnicoDetalheComponent,
        data: { title: 'Chamado tecnico', perfil: 'usuario' }
      },
      {
        path: 'configuracoes/chamados-tecnicos-dev',
        component: ChamadoTecnicoKanbanComponent,
        data: { title: 'Chamado tecnico', perfil: 'desenvolvedor' }
      },
      {
        path: 'configuracoes/chamados-tecnicos-dev/lista',
        component: ChamadosTecnicosComponent,
        data: { title: 'Chamados tecnicos', perfil: 'desenvolvedor' }
      },
      {
        path: 'configuracoes/chamados-tecnicos-dev/:id',
        component: ChamadoTecnicoDetalheComponent,
        data: { title: 'Chamado tecnico', perfil: 'desenvolvedor' }
      },
      {
        path: 'configuracoes/parametros',
        component: SystemParametersComponent,
        data: { title: 'Parametros do sistema' }
      },
      {
        path: 'configuracoes/alertas-sistema',
        component: AlertasSistemaComponent,
        data: { title: 'Alertas do sistema' }
      },
      { path: 'configuracoes/sistema', redirectTo: 'configuracoes/parametros', pathMatch: 'full' },
      {
        path: 'configuracoes/modelos-texto',
        redirectTo: 'configuracoes/parametros',
        pathMatch: 'full'
      },
      { path: 'configuracoes/usuarios', redirectTo: 'configuracoes/parametros', pathMatch: 'full' },
      {
        path: 'configuracoes/gerenciamento-dados',
        component: DataManagementComponent,
        data: { title: 'Gerenciamento de Dados' }
      },
      { path: 'configuracoes/importacao', redirectTo: 'configuracoes/parametros', pathMatch: 'full' },
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
        data: { title: 'Prestacao de contas' }
      },
      {
        path: 'financeiro/autorizacao-compras',
        component: AutorizacaoComprasComponent,
        data: { title: 'Autorizacao de compras' }
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
        path: 'georeferenciamento/localizacao',
        component: GeoreferenciamentoLocalizacaoComponent,
        data: { title: 'Georeferenciamento' }
      }
    ]
  },
  { path: '**', redirectTo: '' }
];
