import { Beneficiario } from '../../../entities/Beneficiario';

export interface BeneficiaryProfilePayload {
  issuedAt: string;
  beneficiario: Beneficiario;
}

export function buildBeneficiaryProfileTemplate(payload: BeneficiaryProfilePayload): string {
  const { beneficiario } = payload;
  const issuedAt = formatDateTime(payload.issuedAt);
  const nascimento = formatDate(beneficiario.dataNascimento);
  const endereco = [beneficiario.logradouro, beneficiario.numero, beneficiario.bairro, beneficiario.municipio, beneficiario.uf]
    .filter(Boolean)
    .join(', ');

  return `
    <!DOCTYPE html>
    <html lang="pt-BR">
      <head>
        <meta charset="UTF-8" />
        <title>Ficha Individual do Beneficiário</title>
        ${styles()}
      </head>
      <body>
        <header class="header">
          <div>
            <p class="eyebrow">G3 – Gestão do Terceiro Setor</p>
            <h1>Ficha Individual do Beneficiário</h1>
            <p class="muted">Emitido em ${issuedAt}</p>
          </div>
          <div class="badge">${beneficiario.codigo || '—'}</div>
        </header>

        <section class="card">
          <div class="card__title">
            <h2>Identificação</h2>
            <span class="status ${beneficiario.status?.toLowerCase()}">${formatStatus(beneficiario.status)}</span>
          </div>
          <div class="grid">
            ${field('Nome completo', beneficiario.nomeCompleto || beneficiario.nomeSocial)}
            ${field('CPF', beneficiario.cpf)}
            ${field('NIS', beneficiario.nis)}
            ${field('Nascimento', nascimento)}
            ${field('Sexo', beneficiario.sexoBiologico)}
            ${field('Estado civil', beneficiario.estadoCivil)}
          </div>
        </section>

        <section class="card">
          <div class="card__title">
            <h2>Endereço e Contato</h2>
          </div>
          <div class="grid">
            ${field('Endereço', endereco)}
            ${field('CEP', beneficiario.cep)}
            ${field('Telefone principal', beneficiario.telefonePrincipal)}
            ${field('Telefone secundário', beneficiario.telefoneSecundario)}
            ${field('Email', beneficiario.email)}
          </div>
        </section>

        <section class="card">
          <div class="card__title">
            <h2>Documentos</h2>
          </div>
          <div class="grid">
            ${field('RG', beneficiario.rgNumero)}
            ${field('Órgão emissor', beneficiario.rgOrgaoEmissor)}
            ${field('UF', beneficiario.rgUf)}
            ${field('Data de emissão', formatDate(beneficiario.rgDataEmissao))}
            ${field('Certidão', beneficiario.certidaoTipo)}
            ${field('Cartão SUS', beneficiario.cartaoSus)}
          </div>
        </section>

        <section class="card">
          <div class="card__title">
            <h2>Observações</h2>
          </div>
          <p class="note">${beneficiario.observacoes || 'Nenhuma observação registrada.'}</p>
        </section>

        <footer class="report-footer">
          <span>G3 – Gestão do Terceiro Setor</span>
          <span>Página <span class="pageNumber"></span> de <span class="totalPages"></span></span>
        </footer>
      </body>
    </html>
  `;
}

function field(label: string, value?: string | null): string {
  return `
    <div class="field">
      <p class="field__label">${label}</p>
      <p class="field__value">${value || '—'}</p>
    </div>
  `;
}

function formatDate(value?: string | null): string {
  if (!value) return '—';
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return '—';
  return date.toLocaleDateString('pt-BR');
}

function formatDateTime(value?: string): string {
  if (!value) return '—';
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return '—';
  return `${date.toLocaleDateString('pt-BR')} às ${date.toLocaleTimeString('pt-BR')}`;
}

function formatStatus(status?: string | null): string {
  if (!status) return '—';
  const labels: Record<string, string> = {
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
      @page { size: A4; margin: 12mm; }
      * { box-sizing: border-box; }
      body { font-family: 'Inter', 'Segoe UI', system-ui, -apple-system, sans-serif; color: #0f172a; margin: 0; padding: 12px; background: #f8fafc; }
      h1, h2 { margin: 0; }
      .eyebrow { text-transform: uppercase; letter-spacing: 0.24em; font-size: 11px; color: #0f7a43; margin: 0 0 6px; }
      .muted { color: #64748b; font-size: 12px; margin: 4px 0 0; }
      .header { display: flex; justify-content: space-between; align-items: flex-start; border-bottom: 2px solid #e2e8f0; padding-bottom: 10px; margin-bottom: 12px; }
      .badge { background: #0f172a; color: #fff; padding: 6px 10px; border-radius: 10px; font-weight: 700; letter-spacing: 0.2px; }
      .card { background: #fff; border: 1px solid #e2e8f0; border-radius: 12px; padding: 12px; margin-bottom: 12px; box-shadow: 0 12px 24px rgba(15, 23, 42, 0.06); page-break-inside: avoid; }
      .card__title { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; }
      .grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 10px; }
      .field { background: #f8fafc; border: 1px solid #e2e8f0; border-radius: 10px; padding: 10px; }
      .field__label { margin: 0 0 4px; font-size: 11px; color: #475569; text-transform: uppercase; letter-spacing: 0.08em; }
      .field__value { margin: 0; font-size: 14px; font-weight: 700; color: #0f172a; word-break: break-word; }
      .status { padding: 6px 12px; border-radius: 999px; font-size: 12px; font-weight: 700; text-transform: uppercase; }
      .status.ativo { background: #dcfce7; color: #166534; border: 1px solid #bbf7d0; }
      .status.inativo { background: #fee2e2; color: #991b1b; border: 1px solid #fecdd3; }
      .status.em_analise { background: #fef9c3; color: #854d0e; border: 1px solid #fef08a; }
      .status.bloqueado { background: #ffe4e6; color: #9f1239; border: 1px solid #fecdd3; }
      .status.desatualizado { background: #e0f2fe; color: #075985; border: 1px solid #bfdbfe; }
      .status.incompleto { background: #ede9fe; color: #5b21b6; border: 1px solid #ddd6fe; }
      .note { margin: 0; color: #0f172a; line-height: 1.6; }
      .report-footer { display: flex; justify-content: space-between; align-items: center; margin-top: 12px; padding-top: 10px; border-top: 1px solid #e2e8f0; font-size: 12px; color: #475569; }
    </style>
  `;
}
