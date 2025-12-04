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
        <main class="report-shell">
          <article class="report">
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

            <div class="report-meta">
              <p class="eyebrow">Documento LGPD</p>
              <h1>Termo de Autorização e Consentimento</h1>
              <p class="muted">Emitido em ${issueDate}</p>
            </div>

            <section class="report-section">
              <div class="section-title"><span class="badge">1</span><h2>Identificação do(a) beneficiário(a)</h2></div>
              <div class="data-grid">
                <div class="data-item"><p class="label">Nome</p><p class="value">${beneficiaryName}</p></div>
                <div class="data-item"><p class="label">CPF</p><p class="value">${payload.cpf || '---'}</p></div>
                <div class="data-item"><p class="label">RG</p><p class="value">${payload.rg || '---'}</p></div>
                <div class="data-item"><p class="label">NIS</p><p class="value">${payload.nis || '---'}</p></div>
                <div class="data-item"><p class="label">Nascimento</p><p class="value">${payload.birthDate || '---'}</p></div>
                <div class="data-item"><p class="label">Nome da mãe</p><p class="value">${payload.motherName || '---'}</p></div>
                <div class="data-item data-item--wide"><p class="label">Endereço</p><p class="value">${payload.address || '---'}</p></div>
                <div class="data-item data-item--wide"><p class="label">Contato</p><p class="value">${payload.contact || '---'}</p></div>
              </div>
            </section>

            <section class="report-section">
              <div class="section-title"><span class="badge">2</span><h2>Objeto</h2></div>
              <p>Autorizo a coleta, o uso, o armazenamento e o compartilhamento controlado de meus dados pessoais e sensíveis pela instituição acima identificada para fins de atendimento socioassistencial, registros administrativos, cumprimento de obrigações legais e prestação de contas.</p>
            </section>

            <section class="report-section">
              <div class="section-title"><span class="badge">3</span><h2>Termos e condições</h2></div>
              <p>O tratamento observará a Lei nº 13.709/2018 (LGPD), com base em finalidade, necessidade e transparência. As informações poderão ser utilizadas para identificação, segurança, comprovação de atendimento e registros institucionais, vedado o uso comercial.</p>
              <p>O compartilhamento ocorrerá apenas quando necessário à execução de políticas públicas, convênios ou exigências legais, assegurando sigilo e segurança da informação.</p>
              <p>Sei que posso solicitar acesso, correção ou eliminação dos dados, bem como revogar esta autorização, exceto quando houver fundamento legal para sua manutenção.</p>
            </section>

            <section class="report-section">
              <div class="section-title"><span class="badge">4</span><h2>Declaração</h2></div>
              <p>Declaro ciência sobre a guarda segura dos dados, prazos de retenção e canais para exercício de direitos, assumindo a veracidade das informações prestadas. Autorizo a utilização da minha assinatura em registros físicos ou digitais necessários aos serviços prestados.</p>
            </section>

            <section class="signature">
              <div class="signature__line"></div>
              <p>${beneficiaryName}</p>
              <p class="muted">${issueDate}</p>
              <p class="muted">Assinatura do beneficiário ou responsável legal</p>
            </section>

            ${footer}
          </article>
        </main>
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
      body { font-family: 'Inter', 'Segoe UI', system-ui, -apple-system, sans-serif; padding: 12px; line-height: 1.6; color: #0f172a; background: #e2e8f0; }
      h1 { margin: 0 0 4px; }
      h2 { margin: 0 0 2px; }
      p { margin: 0 0 6px; }
      .muted { color: #475569; font-size: 12px; }
      .report-shell { width: 100%; }
      .report { max-width: 100%; margin: 0 auto; background: #ffffff; border-radius: 16px; box-shadow: 0 14px 32px rgba(15, 23, 42, 0.08); padding-bottom: 18px; }
      .report-header { display: flex; justify-content: flex-start; align-items: center; gap: 14px; padding: 18px 18px 12px; border-bottom: 2px solid #e2e8f0; }
      .report-header__logo { max-height: 68px; object-fit: contain; border-radius: 12px; background: #f8fafc; padding: 6px; border: 1px solid #e2e8f0; }
      .report-header__identity { text-align: left; }
      .report-header__name { font-weight: 800; font-size: 15pt; margin: 0; letter-spacing: 0.2px; text-transform: uppercase; }
      .report-header__fantasy { margin: 2px 0 0; font-size: 11pt; color: #1f2937; }
      .report-meta { text-align: center; padding: 10px 18px 6px; }
      .eyebrow { margin: 0; text-transform: uppercase; letter-spacing: 0.22em; font-size: 10px; color: #0f7a43; font-weight: 800; }
      .report-section { margin: 12px 18px; padding: 14px; border: 1px solid #e2e8f0; border-radius: 12px; background: linear-gradient(135deg, #f8fafc, #eef2ff); page-break-inside: avoid; box-shadow: inset 0 0 0 1px #e2e8f0; }
      .section-title { display: flex; align-items: center; gap: 8px; margin-bottom: 8px; }
      .badge { display: inline-flex; align-items: center; justify-content: center; width: 26px; height: 26px; border-radius: 8px; background: #0f7a43; color: #fff; font-weight: 800; font-size: 12px; }
      .data-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 10px; }
      .data-item { background: #fff; border: 1px dashed #e2e8f0; border-radius: 10px; padding: 10px; box-shadow: 0 4px 12px rgba(15, 23, 42, 0.05); }
      .data-item--wide { grid-column: 1 / -1; }
      .label { margin: 0 0 4px; font-size: 11px; color: #475569; letter-spacing: 0.04em; text-transform: uppercase; }
      .value { margin: 0; font-size: 14px; font-weight: 700; color: #0f172a; word-break: break-word; }
      .signature { margin: 18px 18px 0; text-align: center; page-break-inside: avoid; }
      .signature__line { height: 1px; background: #cbd5e1; margin: 0 0 10px; }
      .signature p { margin: 4px 0; }
      .report-footer { border-top: 1px solid #e2e8f0; margin: 14px 18px 0; padding-top: 12px; text-align: center; color: #475569; font-size: 11px; }
      @media print { body { background: #fff; padding: 0; } .report { box-shadow: none; border-radius: 0; } }
    </style>
  `;
}
