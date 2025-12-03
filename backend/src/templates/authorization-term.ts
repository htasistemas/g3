import { AssistanceUnit } from '../entities/AssistanceUnit';

export interface AuthorizationTermPayload {
  beneficiaryName: string;
  birthDate?: string | null;
  motherName?: string | null;
  cpf?: string | null;
  rg?: string | null;
  nis?: string | null;
  address?: string | null;
  contact?: string | null;
  issueDate: string;
  unit?: AssistanceUnit | null;
}

export function buildAuthorizationTermTemplate(payload: AuthorizationTermPayload): string {
  const issueDate = payload.issueDate || '';
  const beneficiaryName = payload.beneficiaryName || 'Beneficiário';
  const footer = payload.unit
    ? `
        <footer class="report-footer">
          <p>${payload.unit.razaoSocial || payload.unit.nomeFantasia || 'Instituição'}</p>
          <p>CNPJ: ${payload.unit.cnpj || '---'} | ${formatAddress(payload.unit)}</p>
          <p>Contato: ${payload.unit.telefone || '---'}${payload.unit.email ? ' | ' + payload.unit.email : ''}</p>
        </footer>
      `
    : '';

  const logo = payload.unit?.logomarcaRelatorio || payload.unit?.logomarca;
  const socialName = payload.unit?.razaoSocial || payload.unit?.nomeFantasia || 'Instituição';

  return `
    <html>
      <head>
        <title>Termo de autorização e consentimento</title>
        ${printStyles()}
      </head>
      <body>
        <header class="report-header">
          ${logo ? `<img class=\"report-header__logo\" src=\"${logo}\" alt=\"Logomarca da unidade\" />` : ''}
          <div class="report-header__identity">
            <p class="report-header__name">${socialName}</p>
            ${
              payload.unit?.nomeFantasia && payload.unit?.nomeFantasia !== payload.unit?.razaoSocial
                ? `<p class=\"report-header__fantasy\">${payload.unit.nomeFantasia}</p>`
                : ''
            }
          </div>
        </header>
        <h1>Termo de Autorização</h1>
        <p class="muted">Emitido em ${issueDate}</p>
        <section class="report-section">
          <h2>1. Identificação do(a) beneficiário(a)</h2>
          <p><strong>Nome:</strong> ${beneficiaryName}</p>
          <p><strong>CPF:</strong> ${payload.cpf || '---'} | <strong>RG:</strong> ${payload.rg || '---'} | <strong>NIS:</strong> ${payload.nis || '---'}</p>
          <p><strong>Nascimento:</strong> ${payload.birthDate || '---'} | <strong>Nome da mãe:</strong> ${payload.motherName || '---'}</p>
          <p><strong>Endereço:</strong> ${payload.address || '---'}</p>
          <p><strong>Contato:</strong> ${payload.contact || '---'}</p>
        </section>
        <section class="report-section">
          <h2>2. Objeto</h2>
          <p>Autorizo a coleta, o uso, o armazenamento e o compartilhamento controlado de meus dados pessoais e sensíveis pela instituição acima identificada para fins de atendimento socioassistencial, registros administrativos, cumprimento de obrigações legais e prestação de contas.</p>
        </section>
        <section class="report-section">
          <h2>3. Termos e condições</h2>
          <p>O tratamento observará a Lei nº 13.709/2018 (LGPD), com base em finalidade, necessidade e transparência. As informações poderão ser utilizadas para identificação, segurança, comprovação de atendimento e registros institucionais, vedado o uso comercial.</p>
          <p>O compartilhamento ocorrerá apenas quando necessário à execução de políticas públicas, convênios ou exigências legais, assegurando sigilo e segurança da informação.</p>
          <p>Sei que posso solicitar acesso, correção ou eliminação dos dados, bem como revogar esta autorização, exceto quando houver fundamento legal para sua manutenção.</p>
        </section>
        <section class="report-section">
          <h2>4. Declaração</h2>
          <p>Declaro ciência sobre a guarda segura dos dados, prazos de retenção e canais para exercício de direitos, assumindo a veracidade das informações prestadas. Autorizo a utilização da minha assinatura em registros físicos ou digitais necessários aos serviços prestados.</p>
        </section>
        <section class="signature">
          <p>${beneficiaryName}</p>
          <p class="muted">${issueDate}</p>
          <p class="muted">Assinatura do beneficiário ou responsável legal</p>
        </section>
        ${footer}
      </body>
    </html>
  `;
}

function formatAddress(unit: AssistanceUnit): string {
  return [unit.endereco, unit.numeroEndereco, unit.bairro, unit.cidade, unit.estado, unit.cep]
    .filter(Boolean)
    .join(', ');
}

function printStyles(): string {
  return `
    <style>
      * { box-sizing: border-box; }
      @page { size: A4; margin: 12mm; }
      body { font-family: 'Inter', 'Segoe UI', system-ui, -apple-system, sans-serif; padding: 0; line-height: 1.6; color: #0f172a; background: #ffffff; }
      h1 { margin: 0 0 4px; }
      h2 { margin: 0 0 2px; }
      p { margin: 0 0 6px; }
      .muted { color: #475569; font-size: 12px; }
      .report-header { display: flex; justify-content: space-between; align-items: center; gap: 16px; padding: 8px 0 12px; border-bottom: 1px solid #e2e8f0; }
      .report-header__logo { max-height: 60px; object-fit: contain; }
      .report-header__identity { text-align: right; }
      .report-header__name { font-weight: bold; font-size: 14pt; margin: 0; }
      .report-header__fantasy { margin: 0; font-size: 12pt; color: #1f2937; }
      .report-section { margin-top: 12px; padding: 12px; border: 1px solid #e2e8f0; border-radius: 10px; background: #f8fafc; page-break-inside: avoid; }
      .report-section h2 { font-size: 12pt; color: #1e3a8a; }
      .signature { margin-top: 24px; text-align: center; page-break-inside: avoid; }
      .signature p { margin: 4px 0; }
      .report-footer { border-top: 1px solid #e2e8f0; margin-top: 14px; padding-top: 10px; text-align: center; color: #475569; font-size: 11px; }
    </style>
  `;
}
