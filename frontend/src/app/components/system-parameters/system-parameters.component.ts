import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { PersonalizacaoComponent } from '../personalizacao/personalizacao.component';
import { SystemSettingsComponent } from '../system-settings/system-settings.component';
import { TextTemplatesComponent } from '../text-templates/text-templates.component';
import { UserManagementComponent } from '../user-management/user-management.component';

type ParameterTabId = 'documentos' | 'modelos' | 'usuarios' | 'personalizacao';

interface ParameterTab {
  id: ParameterTabId;
  label: string;
  description: string;
  badge?: string;
}

@Component({
  selector: 'app-system-parameters',
  standalone: true,
  imports: [
    CommonModule,
    SystemSettingsComponent,
    TextTemplatesComponent,
    UserManagementComponent,
    PersonalizacaoComponent
  ],
  templateUrl: './system-parameters.component.html',
  styleUrl: './system-parameters.component.scss'
})
export class SystemParametersComponent {
  readonly tabs: ParameterTab[] = [
    {
      id: 'documentos',
      label: 'Documentos obrigatórios',
      description: 'Defina quais comprovantes e documentos são exigidos para cadastro de beneficiários.',
      badge: 'Fluxo de cadastro'
    },
    {
      id: 'modelos',
      label: 'Modelos de textos',
      description: 'Edite e restaure os modelos utilizados nos termos e comunicações oficiais.',
      badge: 'Textos padrão'
    },
    {
      id: 'usuarios',
      label: 'Usuários e permissões',
      description: 'Gerencie contas de acesso, redefina senhas e mantenha os responsáveis atualizados.',
      badge: 'Segurança'
    },
    {
      id: 'personalizacao',
      label: 'Personalização',
      description: 'Ajuste tema, cores e bordas para alinhar a identidade visual do sistema.',
      badge: 'Experiência'
    }
  ];

  activeTab: ParameterTabId = this.tabs[0].id;

  get activeTabIndex(): number {
    return this.tabs.findIndex((tab) => tab.id === this.activeTab);
  }

  get currentTab(): ParameterTab {
    return this.tabs[this.activeTabIndex] ?? this.tabs[0];
  }

  changeTab(tabId: ParameterTabId): void {
    this.activeTab = tabId;
  }
}
