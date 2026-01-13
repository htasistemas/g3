import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import {
  faClipboardList,
  faDatabase,
  faGauge,
  faHandshakeAngle,
  faHouseChimneyUser,
  faMapLocationDot,
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
      { label: 'Visao Geral', icon: faGauge, route: '/dashboard/visao-geral' },
      { label: 'Indicadores', icon: faGauge, route: '/dashboard/indicadores' },
      { label: 'BI Gerencial', icon: faGauge, route: '/dashboard/gerencial' },
    ],
  },
  {
    label: 'Cadastros',
    icon: faUsers,
    children: [
      {
        label: 'Unidade Assistencial',
        icon: faHouseChimneyUser,
        route: '/unidades/cadastro',
      },
      {
        label: 'Beneficiarios',
        icon: faUserPlus,
        route: '/cadastros/beneficiarios',
      },
      {
        label: 'Vinculo Familiar',
        icon: faUserPlus,
        route: '/cadastros/vinculo-familiar',
      },
      {
        label: 'Profissionais',
        icon: faUserDoctor,
        route: '/cadastros/profissionais',
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
      { label: 'Doacao Realizada', icon: faClipboardList, route: '/atendimentos/doacoes' },
      {
        label: 'Recebimento de doacao',
        icon: faClipboardList,
        route: '/atendimentos/recebimento-doacao',
      },
      { label: 'Prontuario social', icon: faClipboardList, route: '/atendimentos/prontuario' },
      { label: 'Registro de visitas', icon: faHouseChimneyUser, route: '/atendimentos/visitas' },
      { label: 'Matriculas', icon: faClipboardList, route: '/atendimentos/cursos' },
      { label: 'Banco de Empregos', icon: faClipboardList, route: '/atendimentos/banco-empregos' },
    ],
  },
  {
    label: 'Administrativo',
    icon: faClipboardList,
    children: [
      { label: 'Oficios e Documentos', icon: faClipboardList, route: '/administrativo/oficios' },
      { label: 'Feriados', icon: faClipboardList, route: '/administrativo/feriados' },
      {
        label: 'Documentos da Instituicao',
        icon: faClipboardList,
        route: '/administrativo/documentos',
      },
      { label: 'Almoxarifado', icon: faClipboardList, route: '/administrativo/almoxarifado' },
      { label: 'Patrimonio', icon: faClipboardList, route: '/administrativo/patrimonio' },
      {
        label: 'Emprestimo para Eventos',
        icon: faClipboardList,
        route: '/administrativo/patrimonio/emprestimos-eventos',
      },
      { label: 'Tarefas e pendencias', icon: faClipboardList, route: '/administrativo/tarefas' },
      { label: 'Fotos e Eventos', icon: faClipboardList, route: '/administrativo/fotos-eventos' },
    ],
  },
  {
    label: 'Financeiro',
    icon: faWallet,
    children: [
      { label: 'Prestacao de Contas', icon: faWallet, route: '/financeiro/prestacao-contas' },
      { label: 'Contabilidade', icon: faWallet, route: '/financeiro/contabilidade' },
      {
        label: 'Autorizacao de Compras',
        icon: faClipboardList,
        route: '/financeiro/autorizacao-compras',
      },
    ],
  },
  {
    label: 'Juridico',
    icon: faScaleBalanced,
    children: [
      { label: 'Termo de Fomento', icon: faScaleBalanced, route: '/juridico/termos-fomento' },
      { label: 'Plano de Trabalho', icon: faClipboardList, route: '/juridico/planos-trabalho' },
    ],
  },
  {
    label: 'Georeferenciamento',
    icon: faMapLocationDot,
    children: [
      { label: 'Localizacao', icon: faMapLocationDot, route: '/georeferenciamento/localizacao' },
    ],
  },
  {
    label: 'Configuracoes Gerais',
    icon: faWrench,
    children: [
      { label: 'Parametros do sistema', icon: faWrench, route: '/configuracoes/parametros' },
      { label: 'Versao do sistema', icon: faClipboardList, route: '/configuracoes/versao' },
      {
        label: 'Chamado tecnico',
        icon: faClipboardList,
        route: '/configuracoes/chamados-tecnicos',
      },
      {
        label: 'Alertas do sistema',
        icon: faClipboardList,
        route: '/configuracoes/alertas-sistema',
      },
      {
        label: 'Gerenciamento de Dados',
        icon: faDatabase,
        route: '/configuracoes/gerenciamento-dados',
      },
    ],
  },
];
