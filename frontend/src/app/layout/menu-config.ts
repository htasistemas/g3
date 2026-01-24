import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import {
  faClipboardList,
  faDatabase,
  faGauge,
  faHandshakeAngle,
  faHouseChimneyUser,
  faMapLocationDot,
  faPalette,
  faScaleBalanced,
  faUserDoctor,
  faUserPlus,
  faUsers,
  faWallet,
  faWrench,
} from '@fortawesome/free-solid-svg-icons';

export interface MenuChild {
  label: string;
  icon: IconDefinition;
  route?: string;
  urlExterna?: string;
  permissao?: string;
}

export interface MenuItem {
  label: string;
  icon: IconDefinition;
  children?: MenuChild[];
}

export const menuSections: MenuItem[] = [
  {
    label: 'Dashboard',
    icon: faGauge,
    children: [
      { label: 'Indicadores', icon: faGauge, route: '/dashboard/indicadores' },
      { label: 'Mapa dos Beneficiarios', icon: faMapLocationDot, route: '/dashboard/mapa-beneficiarios' },
      { label: 'Visao Geral', icon: faGauge, route: '/dashboard/visao-geral' },
    ],
  },
  {
    label: 'Cadastros',
    icon: faUsers,
    children: [
      {
        label: 'Beneficiarios',
        icon: faUserPlus,
        route: '/cadastros/beneficiarios',
      },
      {
        label: 'Profissionais',
        icon: faUserDoctor,
        route: '/cadastros/profissionais',
      },
      {
        label: 'Unidade Assistencial',
        icon: faHouseChimneyUser,
        route: '/unidades/cadastro',
      },
      {
        label: 'Vinculo Familiar',
        icon: faUserPlus,
        route: '/cadastros/vinculo-familiar',
      },
      {
        label: 'Voluntariados',
        icon: faClipboardList,
        route: '/cadastros/voluntariados',
      },
    ],
  },
  {
    label: 'Atendimentos',
    icon: faHandshakeAngle,
    children: [
      { label: 'Banco de Empregos', icon: faClipboardList, route: '/atendimentos/banco-empregos' },
      { label: 'Prontuario social', icon: faClipboardList, route: '/atendimentos/prontuario' },
      {
        label: 'Recebimento de doacao',
        icon: faClipboardList,
        route: '/atendimentos/recebimento-doacao',
      },
      { label: 'Registro de visitas', icon: faHouseChimneyUser, route: '/atendimentos/visitas' },
      { label: 'Doacao Realizada', icon: faClipboardList, route: '/atendimentos/doacoes' },
      { label: 'Matriculas', icon: faClipboardList, route: '/atendimentos/cursos' },
      { label: 'Ocorrencias', icon: faClipboardList, route: '/atendimentos/ocorrencias-crianca' },
    ],
  },
  {
    label: 'Setor Administrativo',
    icon: faClipboardList,
    children: [
      { label: 'Almoxarifado', icon: faClipboardList, route: '/administrativo/almoxarifado' },
      {
        label: 'Controle de Veiculos',
        icon: faClipboardList,
        route: '/administrativo/controle-veiculos',
      },
      {
        label: 'Emprestimo para Eventos',
        icon: faClipboardList,
        route: '/administrativo/patrimonio/emprestimos-eventos',
      },
      { label: 'Fotos e Eventos', icon: faClipboardList, route: '/administrativo/fotos-eventos' },
      {
        label: 'Gestao de Documentos',
        icon: faClipboardList,
        route: '/administrativo/documentos',
      },
      { label: 'Oficios e Documentos', icon: faClipboardList, route: '/administrativo/oficios' },
      { label: 'Patrimonio', icon: faClipboardList, route: '/administrativo/patrimonio' },
      { label: 'Tarefas e pendencias', icon: faClipboardList, route: '/administrativo/tarefas' },
    ],
  },
  {
    label: 'Setor Financeiro',
    icon: faWallet,
    children: [
      {
        label: 'Autorizacao de Compras',
        icon: faClipboardList,
        route: '/financeiro/autorizacao-compras',
      },
      { label: 'Contabilidade', icon: faWallet, route: '/financeiro/contabilidade' },
      { label: 'Prestacao de Contas', icon: faWallet, route: '/financeiro/prestacao-contas' },
    ],
  },
  {
    label: 'Setor Juridico',
    icon: faScaleBalanced,
    children: [
      { label: 'Plano de Trabalho', icon: faClipboardList, route: '/juridico/planos-trabalho' },
      { label: 'Termo de Fomento', icon: faScaleBalanced, route: '/juridico/termos-fomento' },
    ],
  },
  {
    label: 'Configuracoes Gerais',
    icon: faWrench,
    children: [
      {
        label: 'Alertas do sistema',
        icon: faClipboardList,
        route: '/configuracoes/alertas-sistema',
      },
      {
        label: 'Chamado tecnico',
        icon: faClipboardList,
        route: '/configuracoes/chamados-tecnicos',
      },
      {
        label: 'Gerenciamento de Dados',
        icon: faDatabase,
        route: '/configuracoes/gerenciamento-dados',
      },
      { label: 'Feriados', icon: faClipboardList, route: '/administrativo/feriados' },
      { label: 'Parametros do sistema', icon: faWrench, route: '/configuracoes/parametros' },
      {
        label: 'Painel de senha',
        icon: faPalette,
        urlExterna: 'http://localhost:4201/#/painel/senha',
      },
      {
        label: 'Chamar senha',
        icon: faPalette,
        urlExterna: 'http://localhost:4201/#/operador/chamadas',
      },
      { label: 'Versao do sistema', icon: faClipboardList, route: '/configuracoes/versao' },
    ],
  },
];
