import { Beneficiario, BeneficiarioStatus } from '../../../entities/Beneficiario';

export interface BeneficiaryListRequest {
  nome?: string;
  cpf?: string;
  codigo?: string;
  status?: BeneficiarioStatus;
  dataNascimento?: string;
}

export interface BeneficiaryListPayload {
  issuedAt: string;
  filters: BeneficiaryListRequest;
  beneficiarios: Beneficiario[];
}

export function buildBeneficiaryListTemplate(payload: BeneficiaryListPayload): string {
  const formattedDate = formatDate(payload.issuedAt);
  const rows = payload.beneficiarios
    .map(
      (beneficiario, index) => `
        <tr class="${index % 2 === 0 ? 'row-even' : 'row-odd'}">
          <td>${beneficiario.codigo || '—'}</td>
          <td>${beneficiario.nomeCompleto || beneficiario.nomeSocial || '—'}</td>
          <td>${beneficiario.cpf || beneficiario.nis || '—'}</td>
          <td>${formatDate(beneficiario.dataNascimento)}</td>
          <td>${beneficiario.bairro || '—'}</td>
          <td>${beneficiario.municipio || '—'}</td>
          <td>${formatStatus(beneficiario.status)}</td>
          <td>${formatDate(beneficiario.dataCadastro)}</td>
        </tr>
      `
    )
    .join('');

  const filterChips = buildFilterChips(payload.filters);

  return `
    <html lang="pt-BR">
      <head>
        <title>Relação de Beneficiários</title>
        ${styles()}
      </head>
      <body>
        <header class="report-header">
          <div>
            <p class="eyebrow">G3 – Gestão do Terceiro Setor</p>
            <h1>Relação de Beneficiários</h1>
            <p class="muted">Emitido em ${formattedDate}</p>
          </div>
        </header>

        <section class="filters">
          <p class="filters__title">Filtros aplicados</p>
          <div class="filters__chips">${filterChips || '<span class="muted">Nenhum filtro informado</span>'}</div>
        </section>

        <table class="table">
          <thead>
            <tr>
              <th>Código</th>
              <th>Nome completo</th>
              <th>CPF/NIS</th>
              <th>Nascimento</th>
              <th>Bairro</th>
              <th>Município</th>
              <th>Status</th>
              <th>Cadastro</th>
            </tr>
          </thead>
          <tbody>${rows || '<tr><td colspan="8" class="empty">Nenhum beneficiário encontrado.</td></tr>'}</tbody>
        </table>

        <footer class="report-footer">
          <span>G3 – Gestão do Terceiro Setor</span>
          <span>Página <span class="pageNumber"></span> de <span class="totalPages"></span></span>
        </footer>
      </body>
    </html>
  `;
}

function buildFilterChips(filters: BeneficiaryListRequest): string {
  const entries: [string, string | undefined][] = [
    ['Nome', filters.nome],
    ['Código', filters.codigo],
    ['CPF', filters.cpf],
    ['Status', filters.status ? formatStatus(filters.status) : undefined],
    ['Data de nascimento', filters.dataNascimento ? formatDate(filters.dataNascimento) : undefined]
  ];

  return entries
    .filter(([, value]) => Boolean(value))
    .map(([label, value]) => `<span class="chip"><strong>${label}:</strong> ${value}</span>`)
    .join('');
}

function formatDate(value?: string | null): string {
  if (!value) return '—';
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return '—';
  return date.toLocaleDateString('pt-BR');
}

function formatStatus(status?: BeneficiarioStatus | null): string {
  if (!status) return '—';
  const labels: Record<BeneficiarioStatus, string> = {
    ATIVO: 'Ativo',
    INATIVO: 'Inativo',
    DESATUALIZADO: 'Desatualizado',
    INCOMPLETO: 'Incompleto',
    EM_ANALISE: 'Em análise',
    BLOQUEADO: 'Bloqueado'
  };

  return labels[status] ?? status;
}

function styles(): string {
  return `
    <style>
      @page {
        size: A4;
        margin: 12mm;
      }

      * { box-sizing: border-box; }
      body {
        font-family: 'Inter', 'Segoe UI', system-ui, -apple-system, sans-serif;
        color: #0f172a;
        background: #f8fafc;
        margin: 0;
        padding: 12px;
      }

      .report-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        border-bottom: 2px solid #e2e8f0;
        padding-bottom: 12px;
        margin-bottom: 12px;
      }

      h1 { margin: 0; font-size: 20px; letter-spacing: 0.2px; }
      .eyebrow { text-transform: uppercase; letter-spacing: 0.24em; font-size: 11px; color: #0f7a43; margin: 0 0 4px; }
      .muted { color: #64748b; font-size: 12px; margin: 4px 0 0; }

      .filters { margin-bottom: 12px; background: #fff; border: 1px solid #e2e8f0; border-radius: 12px; padding: 12px; }
      .filters__title { margin: 0 0 6px; font-weight: 700; color: #0f172a; }
      .filters__chips { display: flex; flex-wrap: wrap; gap: 6px; }
      .chip { background: #f1f5f9; color: #0f172a; border-radius: 999px; padding: 6px 10px; font-size: 12px; border: 1px solid #e2e8f0; }
      .chip strong { margin-right: 4px; color: #0f7a43; }

      .table { width: 100%; border-collapse: collapse; background: #ffffff; border: 1px solid #e2e8f0; border-radius: 12px; overflow: hidden; }
      thead { background: #f1f5f9; }
      th { text-align: left; padding: 10px 12px; font-size: 12px; text-transform: uppercase; letter-spacing: 0.08em; color: #0f172a; }
      td { padding: 10px 12px; font-size: 13px; border-top: 1px solid #e2e8f0; }
      .row-even { background: #fff; }
      .row-odd { background: #f8fafc; }
      .empty { text-align: center; color: #94a3b8; }

      .report-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-top: 12px;
        padding-top: 10px;
        border-top: 1px solid #e2e8f0;
        font-size: 12px;
        color: #475569;
      }
    </style>
  `;
}
