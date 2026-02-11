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
  faClock,
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
  faUserClock,
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
      { label: 'Visão Geral', icon: faChartPie, route: '/dashboard/visao-geral' },
      { label: 'Indicadores', icon: faChartColumn, route: '/dashboard/indicadores' },
    ],
  },
  {
    label: 'Cadastros',
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
        label: 'Unidade Assistencial',
        icon: faHospitalUser,
        route: '/unidades/cadastro',
      },
      {
        label: 'Vínculo Familiar',
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
      { label: 'Prontuário Social', icon: faNotesMedical, route: '/atendimentos/prontuario' },
      {
        label: 'Recebimento de Doação',
        icon: faHandHoldingDollar,
        route: '/atendimentos/recebimento-doacao',
      },
      { label: 'Registro de Visitas', icon: faClipboardUser, route: '/atendimentos/visitas' },
      { label: 'Doação Realizada', icon: faGift, route: '/atendimentos/doacoes' },
      { label: 'Matrículas', icon: faGraduationCap, route: '/atendimentos/cursos' },
      { label: 'Ocorrências', icon: faTriangleExclamation, route: '/atendimentos/ocorrencias-crianca' },
      { label: 'Chamada de Senhas', icon: faBullhorn, route: '/senhas/chamar' },
      { label: 'Painel de Senhas', icon: faDisplay, urlExterna: '/senhas/painel' },
    ],
  },
  {
    label: 'Setor Administrativo',
    icon: faClipboardList,
    children: [
      { label: 'Almoxarifado', icon: faBoxesStacked, route: '/administrativo/almoxarifado' },
      {
        label: 'Controle de Veículos',
        icon: faCarSide,
        route: '/administrativo/controle-veiculos',
      },
      {
        label: 'Empréstimo para Eventos',
        icon: faCalendarCheck,
        route: '/administrativo/patrimonio/emprestimos-eventos',
      },
      { label: 'Fotos e Eventos', icon: faImages, route: '/administrativo/fotos-eventos' },
      {
        label: 'Gestão de Documentos',
        icon: faFolderOpen,
        route: '/administrativo/documentos',
      },
      { label: 'Ofícios e Protocolos', icon: faFileSignature, route: '/administrativo/oficios' },
      { label: 'Patrimônio', icon: faLandmark, route: '/administrativo/patrimonio' },
      { label: 'Tarefas e Pendências', icon: faListCheck, route: '/administrativo/tarefas' },
      { label: 'Lembretes Diários', icon: faBell, route: '/administrativo/lembretes-diarios' },
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
      { label: 'Prestação de Contas', icon: faReceipt, route: '/financeiro/prestacao-contas' },
    ],
  },
  {
    label: 'Setor Jurídico',
    icon: faScaleBalanced,
    children: [
      { label: 'Plano de Trabalho', icon: faClipboardCheck, route: '/juridico/planos-trabalho' },
      { label: 'Termo de Fomento', icon: faFileContract, route: '/juridico/termos-fomento' },
    ],
  },
  {
    label: 'Setor RH',
    icon: faUserClock,
    children: [
      { label: 'Folha de ponto', icon: faClock, route: '/rh/folha-ponto' },
    ],
  },
  {
    label: 'Configurações Gerais',
    icon: faWrench,
    children: [
      {
        label: 'Chamado Técnico',
        icon: faHeadset,
        route: '/configuracoes/chamados-tecnicos',
      },
      {
        label: 'Gerenciamento de Dados',
        icon: faServer,
        route: '/configuracoes/gerenciamento-dados',
      },
      { label: 'Configurações do Sistema', icon: faSliders, route: '/configuracoes/parametros' },
      { label: 'Versão do Sistema', icon: faTags, route: '/configuracoes/versao' },
    ],
  },
];


