import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import {
  faBell,
  faBook,
  faBoxesStacked,
  faBriefcase,
  faBullhorn,
  faCalculator,
  faCalendarCheck,
  faCalendarDay,
  faCarSide,
  faChartColumn,
  faChartPie,
  faClipboardCheck,
  faClipboardList,
  faClipboardUser,
  faDisplay,
  faFileContract,
  faFileInvoiceDollar,
  faFileSignature,
  faFolderOpen,
  faGift,
  faGraduationCap,
  faHandshakeAngle,
  faHandHoldingDollar,
  faHandHoldingHeart,
  faHeadset,
  faHospitalUser,
  faImages,
  faLandmark,
  faListCheck,
  faMapLocationDot,
  faNotesMedical,
  faPeopleGroup,
  faReceipt,
  faScaleBalanced,
  faServer,
  faSliders,
  faTags,
  faTriangleExclamation,
  faUserDoctor,
  faUsers,
  faWallet,
  faWrench,
  faGauge,
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
      { label: 'Visao Geral', icon: faChartPie, route: '/dashboard/visao-geral' },
      { label: 'Indicadores', icon: faChartColumn, route: '/dashboard/indicadores' },
    ],
  },
  {
    label: 'Cadastros',
    icon: faUsers,
    children: [
      {
        label: 'Beneficiarios',
        icon: faUsers,
        route: '/cadastros/beneficiarios',
      },
      {
        label: 'Profissionais',
        icon: faUserDoctor,
        route: '/cadastros/profissionais',
      },
      {
        label: 'Unidade Assistencial',
        icon: faHospitalUser,
        route: '/unidades/cadastro',
      },
      {
        label: 'Vinculo Familiar',
        icon: faPeopleGroup,
        route: '/cadastros/vinculo-familiar',
      },
      {
        label: 'Voluntariados',
        icon: faHandHoldingHeart,
        route: '/cadastros/voluntariados',
      },
    ],
  },
  {
    label: 'Atendimentos',
    icon: faHandshakeAngle,
    children: [
      { label: 'Banco de Empregos', icon: faBriefcase, route: '/atendimentos/banco-empregos' },
      { label: 'Biblioteca', icon: faBook, route: '/atendimentos/biblioteca' },
      { label: 'Prontuario social', icon: faNotesMedical, route: '/atendimentos/prontuario' },
      {
        label: 'Recebimento de doacao',
        icon: faHandHoldingDollar,
        route: '/atendimentos/recebimento-doacao',
      },
      { label: 'Registro de visitas', icon: faClipboardUser, route: '/atendimentos/visitas' },
      { label: 'Doacao Realizada', icon: faGift, route: '/atendimentos/doacoes' },
      { label: 'Matriculas', icon: faGraduationCap, route: '/atendimentos/cursos' },
      { label: 'Ocorrencias', icon: faTriangleExclamation, route: '/atendimentos/ocorrencias-crianca' },
      { label: 'Chamada de senhas', icon: faBullhorn, route: '/senhas/chamar' },
      { label: 'Painel de senhas', icon: faDisplay, urlExterna: '/senhas/painel' },
    ],
  },
  {
    label: 'Setor Administrativo',
    icon: faClipboardList,
    children: [
      { label: 'Almoxarifado', icon: faBoxesStacked, route: '/administrativo/almoxarifado' },
      {
        label: 'Controle de Veiculos',
        icon: faCarSide,
        route: '/administrativo/controle-veiculos',
      },
      {
        label: 'Emprestimo para Eventos',
        icon: faCalendarCheck,
        route: '/administrativo/patrimonio/emprestimos-eventos',
      },
      { label: 'Fotos e Eventos', icon: faImages, route: '/administrativo/fotos-eventos' },
      {
        label: 'Gestao de Documentos',
        icon: faFolderOpen,
        route: '/administrativo/documentos',
      },
      { label: 'Oficios e Protocolos', icon: faFileSignature, route: '/administrativo/oficios' },
      { label: 'Patrimonio', icon: faLandmark, route: '/administrativo/patrimonio' },
      { label: 'Tarefas e pendencias', icon: faListCheck, route: '/administrativo/tarefas' },
    ],
  },
  {
    label: 'Setor Financeiro',
    icon: faWallet,
    children: [
      {
        label: 'Autorização de Compras',
        icon: faFileInvoiceDollar,
        route: '/financeiro/autorizacao-compras',
      },
      { label: 'Contabilidade', icon: faCalculator, route: '/financeiro/contabilidade' },
      { label: 'Prestacao de Contas', icon: faReceipt, route: '/financeiro/prestacao-contas' },
    ],
  },
  {
    label: 'Setor Juridico',
    icon: faScaleBalanced,
    children: [
      { label: 'Plano de Trabalho', icon: faClipboardCheck, route: '/juridico/planos-trabalho' },
      { label: 'Termo de Fomento', icon: faFileContract, route: '/juridico/termos-fomento' },
    ],
  },
  {
    label: 'Configuracoes Gerais',
    icon: faWrench,
    children: [
      {
        label: 'Alertas do sistema',
        icon: faBell,
        route: '/configuracoes/alertas-sistema',
      },
      {
        label: 'Chamado tecnico',
        icon: faHeadset,
        route: '/configuracoes/chamados-tecnicos',
      },
      {
        label: 'Gerenciamento de Dados',
        icon: faServer,
        route: '/configuracoes/gerenciamento-dados',
      },
      { label: 'Feriados', icon: faCalendarDay, route: '/administrativo/feriados' },
      { label: 'Configurações do sistema', icon: faSliders, route: '/configuracoes/parametros' },
      { label: 'Versao do sistema', icon: faTags, route: '/configuracoes/versao' },
    ],
  },
];

