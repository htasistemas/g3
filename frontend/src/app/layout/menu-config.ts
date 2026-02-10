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
    label: 'dashboard',
    icon: faGauge,
    children: [
      { label: 'Visão geral', icon: faChartPie, route: '/dashboard/visao-geral' },
      { label: 'Indicadores', icon: faChartColumn, route: '/dashboard/indicadores' },
    ],
  },
  {
    label: 'cadatros',
    icon: faUsers,
    children: [
      {
        label: 'Beneficiários',
        icon: faUsers,
        route: '/cadastros/beneficiarios',
      },
      {
        label: 'Profissionais',
        icon: faUserDoctor,
        route: '/cadastros/profissionais',
      },
      {
        label: 'Unidade assistencial',
        icon: faHospitalUser,
        route: '/unidades/cadastro',
      },
      {
        label: 'Vínculo familiar',
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
    label: 'atendimentos',
    icon: faHandshakeAngle,
    children: [
      { label: 'Banco de empregos', icon: faBriefcase, route: '/atendimentos/banco-empregos' },
      { label: 'Biblioteca', icon: faBook, route: '/atendimentos/biblioteca' },
      { label: 'Prontuário social', icon: faNotesMedical, route: '/atendimentos/prontuario' },
      {
        label: 'Recebimento de doação',
        icon: faHandHoldingDollar,
        route: '/atendimentos/recebimento-doacao',
      },
      { label: 'Registro de visitas', icon: faClipboardUser, route: '/atendimentos/visitas' },
      { label: 'Doação realizada', icon: faGift, route: '/atendimentos/doacoes' },
      { label: 'Matrículas', icon: faGraduationCap, route: '/atendimentos/cursos' },
      { label: 'Ocorrências', icon: faTriangleExclamation, route: '/atendimentos/ocorrencias-crianca' },
      { label: 'Chamada de senhas', icon: faBullhorn, route: '/senhas/chamar' },
      { label: 'Painel de senhas', icon: faDisplay, urlExterna: '/senhas/painel' },
    ],
  },
  {
    label: 'setor administrativo',
    icon: faClipboardList,
    children: [
      { label: 'Almoxarifado', icon: faBoxesStacked, route: '/administrativo/almoxarifado' },
      {
        label: 'Controle de veículos',
        icon: faCarSide,
        route: '/administrativo/controle-veiculos',
      },
      {
        label: 'Empréstimo para eventos',
        icon: faCalendarCheck,
        route: '/administrativo/patrimonio/emprestimos-eventos',
      },
      { label: 'Fotos e eventos', icon: faImages, route: '/administrativo/fotos-eventos' },
      {
        label: 'Gestão de documentos',
        icon: faFolderOpen,
        route: '/administrativo/documentos',
      },
      { label: 'Ofícios e protocolos', icon: faFileSignature, route: '/administrativo/oficios' },
      { label: 'Patrimônio', icon: faLandmark, route: '/administrativo/patrimonio' },
      { label: 'Tarefas e pendências', icon: faListCheck, route: '/administrativo/tarefas' },
      { label: 'Lembretes diários', icon: faBell, route: '/administrativo/lembretes-diarios' },
    ],
  },
  {
    label: 'setor financeiro',
    icon: faWallet,
    children: [
      {
        label: 'Autorização de compras',
        icon: faFileInvoiceDollar,
        route: '/financeiro/autorizacao-compras',
      },
      { label: 'Contabilidade', icon: faCalculator, route: '/financeiro/contabilidade' },
      { label: 'Prestação de contas', icon: faReceipt, route: '/financeiro/prestacao-contas' },
    ],
  },
  {
    label: 'setor juridico',
    icon: faScaleBalanced,
    children: [
      { label: 'Plano de trabalho', icon: faClipboardCheck, route: '/juridico/planos-trabalho' },
      { label: 'Termo de fomento', icon: faFileContract, route: '/juridico/termos-fomento' },
    ],
  },
  {
    label: 'configurações gerais',
    icon: faWrench,
    children: [
      {
        label: 'Chamado técnico',
        icon: faHeadset,
        route: '/configuracoes/chamados-tecnicos',
      },
      {
        label: 'Gerenciamento de dados',
        icon: faServer,
        route: '/configuracoes/gerenciamento-dados',
      },
      { label: 'Configurações do sistema', icon: faSliders, route: '/configuracoes/parametros' },
      { label: 'Versão do sistema', icon: faTags, route: '/configuracoes/versao' },
    ],
  },
];


