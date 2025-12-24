import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {
  faBell,
  faCircleNotch,
  faCloudArrowUp,
  faDatabase,
  faDownload,
  faFileArrowDown,
  faGaugeHigh,
  faLock,
  faRecycle,
  faRotateLeft,
  faShieldHalved,
  faStopwatch,
  faTableList
} from '@fortawesome/free-solid-svg-icons';

type BackupStatus = 'executando' | 'sucesso' | 'falha';

interface BackupRecord {
  id: string;
  label: string;
  type: 'Completo' | 'Incremental';
  status: BackupStatus;
  startedAt: string;
  storedIn: string;
  size: string;
  encrypted: boolean;
  retention: string;
}

@Component({
  selector: 'app-data-management',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FontAwesomeModule],
  templateUrl: './data-management.component.html',
  styleUrl: './data-management.component.scss'
})
export class DataManagementComponent implements OnInit {
  readonly faDatabase = faDatabase;
  readonly faCloudArrowUp = faCloudArrowUp;
  readonly faRotateLeft = faRotateLeft;
  readonly faShieldHalved = faShieldHalved;
  readonly faDownload = faDownload;
  readonly faTableList = faTableList;
  readonly faGaugeHigh = faGaugeHigh;
  readonly faCircleNotch = faCircleNotch;
  readonly faBell = faBell;
  readonly faLock = faLock;
  readonly faStopwatch = faStopwatch;
  readonly faRecycle = faRecycle;
  readonly faFileArrowDown = faFileArrowDown;

  runningBackupId: string | null = null;
  automationFeedback: string | null = null;
  storageFeedback: string | null = null;
  restoreFeedback: { type: 'success' | 'error' | 'info'; message: string } | null = null;

  scheduleForm: FormGroup;
  backupSettingsForm: FormGroup;
  restoreForm: FormGroup;
  monitoringForm: FormGroup;

  recentBackups: BackupRecord[] = [
    {
      id: 'BK-240302-001',
      label: 'Backup completo semanal',
      type: 'Completo',
      status: 'sucesso',
      startedAt: '03/02/2024 02:00',
      storedIn: 'Local + Nuvem (S3)',
      size: '12.4 GB',
      encrypted: true,
      retention: '30 dias'
    },
    {
      id: 'BK-240301-002',
      label: 'Incremental diário',
      type: 'Incremental',
      status: 'sucesso',
      startedAt: '03/01/2024 13:00',
      storedIn: 'Nuvem (S3)',
      size: '2.1 GB',
      encrypted: true,
      retention: '15 dias'
    },
    {
      id: 'BK-240229-003',
      label: 'Backup completo',
      type: 'Completo',
      status: 'falha',
      startedAt: '02/29/2024 02:00',
      storedIn: 'Local',
      size: '10.9 GB',
      encrypted: true,
      retention: '30 dias'
    }
  ];

  constructor(private readonly fb: FormBuilder) {
    this.scheduleForm = this.fb.group({
      frequency: ['diario', Validators.required],
      executionTime: ['02:00', Validators.required],
      incrementalTime: ['13:00', Validators.required],
      retentionDays: [15, [Validators.required, Validators.min(1)]],
      enableAutomation: [true],
      throttle: [65, [Validators.required, Validators.min(10), Validators.max(100)]],
      pauseDuringBusiness: [true]
    });

    this.backupSettingsForm = this.fb.group({
      provider: ['hibrido', Validators.required],
      destinationPath: ['/var/backups', Validators.required],
      bucketName: ['g3-operacional', Validators.required],
      encryption: [true],
      compression: ['balanceada', Validators.required],
      verifyIntegrity: [true],
      notifyEmail: ['operacoes@sistema.local', [Validators.email]],
      offsiteCopy: [true]
    });

    this.restoreForm = this.fb.group({
      backupId: ['', Validators.required],
      target: ['produção', Validators.required],
      dryRun: [true],
      preserveNewerData: [true],
      notifyTeam: [true],
      pointInTime: ['']
    });

    this.monitoringForm = this.fb.group({
      alerts: [true],
      anomalyDetection: [true],
      autoVerification: [true],
      integrations: ['Email'],
      retentionDrill: [true]
    });
  }

  ngOnInit(): void {
    const firstBackup = this.recentBackups[0];
    if (firstBackup) {
      this.restoreForm.get('backupId')?.setValue(firstBackup.id);
    }
  }

  startBackup(type: 'Completo' | 'Incremental'): void {
    if (this.backupSettingsForm.invalid || this.runningBackupId) {
      this.backupSettingsForm.markAllAsTouched();
      return;
    }

    const now = new Date();
    const id = `BK-${now.getFullYear()}${(now.getMonth() + 1).toString().padStart(2, '0')}${now
      .getDate()
      .toString()
      .padStart(2, '0')}-${Math.floor(Math.random() * 900 + 100)}`;

    const newRecord: BackupRecord = {
      id,
      label: `${type} acionado manualmente`,
      type,
      status: 'executando',
      startedAt: now.toLocaleString(),
      storedIn: this.backupSettingsForm.value.provider === 'hibrido' ? 'Local + Nuvem' : this.backupSettingsForm.value.provider,
      size: type === 'Completo' ? '12.8 GB' : '2.3 GB',
      encrypted: this.backupSettingsForm.value.encryption,
      retention: `${this.scheduleForm.value.retentionDays} dias`
    };

    this.runningBackupId = id;
    this.recentBackups = [newRecord, ...this.recentBackups].slice(0, 6);

    setTimeout(() => this.finishBackup(id, 'sucesso'), 1400);
  }

  finishBackup(id: string, status: BackupStatus): void {
    this.recentBackups = this.recentBackups.map((backup) =>
      backup.id === id ? { ...backup, status } : backup
    );
    this.runningBackupId = null;
  }

  saveSchedule(): void {
    this.automationFeedback = null;

    if (this.scheduleForm.invalid) {
      this.scheduleForm.markAllAsTouched();
      return;
    }

    const { frequency, executionTime, retentionDays } = this.scheduleForm.value;
    const frequencyLabel = frequency === 'semanal' ? 'semanal' : 'diária';
    this.automationFeedback = `Rotina ${frequencyLabel} salva para ${executionTime} com retenção de ${retentionDays} dias.`;
  }

  saveStorageSettings(): void {
    this.storageFeedback = null;

    if (this.backupSettingsForm.invalid) {
      this.backupSettingsForm.markAllAsTouched();
      return;
    }

    const provider = this.backupSettingsForm.value.provider;
    const encryption = this.backupSettingsForm.value.encryption ? 'com criptografia' : 'sem criptografia';
    this.storageFeedback = `Destino ${provider} configurado ${encryption} e verificação de integridade ${
      this.backupSettingsForm.value.verifyIntegrity ? 'habilitada' : 'desabilitada'
    }.`;
  }

  simulateRestore(): void {
    this.restoreFeedback = null;

    if (this.restoreForm.invalid) {
      this.restoreForm.markAllAsTouched();
      return;
    }

    const selectedBackup = this.recentBackups.find((backup) => backup.id === this.restoreForm.value.backupId);

    if (!selectedBackup) {
      this.restoreFeedback = { type: 'error', message: 'Selecione um backup válido antes de restaurar.' };
      return;
    }

    this.restoreFeedback = {
      type: 'info',
      message: `Validação de restauração iniciada para ${selectedBackup.label} em ambiente ${this.restoreForm.value.target}.`
    };

    setTimeout(() => {
      this.restoreFeedback = {
        type: 'success',
        message: `Restauração preparada com sucesso. Você pode prosseguir aplicando em ${this.restoreForm.value.target}.`
      };
    }, 1200);
  }

  lastSuccessfulBackup(): BackupRecord | undefined {
    return this.recentBackups.find((backup) => backup.status === 'sucesso');
  }

  runningBackup(): BackupRecord | undefined {
    return this.recentBackups.find((backup) => backup.id === this.runningBackupId);
  }
}
