type DadosEndereco = {
  logradouro?: string;
  numero?: string;
  complemento?: string;
  bairro?: string;
  cep?: string;
  cidade?: string;
  uf?: string;
};

type UnidadeTermo = {
  nomeFantasia?: string;
  razaoSocial?: string;
  cnpj?: string;
  inscricaoMunicipal?: string;
  endereço?: string;
  numeroEndereco?: string;
  complemento?: string;
  bairro?: string;
  cidade?: string;
  estado?: string;
  coordenadorNome?: string;
};

type DisponibilidadeSemanal = {
  dom?: string[];
  seg?: string[];
  ter?: string[];
  qua?: string[];
  qui?: string[];
  sex?: string[];
  sab?: string[];
};

type ProfissionalTermo = {
  nome?: string;
  rg?: string;
  cpf?: string;
  dataNascimento?: string;
  estadoCivil?: string;
  email?: string;
  telefoneCelular?: string;
  telefoneResidencial?: string;
  endereço?: DadosEndereco;
  voluntárioOutraInstituicao?: boolean;
  voluntárioOutraDescricao?: string;
  voluntariadoLocalAtividade?: string;
  voluntariadoPeriodo?: string;
  voluntariadoDisponibilidade?: DisponibilidadeSemanal;
  voluntariadoAtividades?: string[];
  voluntariadoOutros?: string;
  lgpdAceite?: boolean;
  imagemAceite?: boolean;
};

const diasSemana: Array<{ key: keyof DisponibilidadeSemanal; label: string }> = [
  { key: 'dom', label: 'Dom' },
  { key: 'seg', label: 'Seg' },
  { key: 'ter', label: 'Ter' },
  { key: 'qua', label: 'Qua' },
  { key: 'qui', label: 'Qui' },
  { key: 'sex', label: 'Sex' },
  { key: 'sab', label: 'Sab' }
];

const turnos = ['Manha', 'Tarde', 'Noite'];

const atividadesPadrao = [
  'Acolhimento',
  'Triagem',
  'Visitas',
  'Acompanhamento',
  'Oficinas',
  'Campanhas',
  'Administrativo'
];

function escapeHtml(value?: string | null): string {
  const text = (value ?? '').toString();
  return text
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#39;');
}

function joinParts(parts: Array<string | undefined | null>, separator = ' '): string {
  return parts.filter((part) => part && part.toString().trim().length).join(separator);
}

function formatarEnderecoUnidade(unidade: UnidadeTermo): string {
  const endereço = joinParts(
    [unidade.endereço, unidade.numeroEndereco, unidade.complemento, unidade.bairro, unidade.cidade, unidade.estado],
    ', '
  );
  return endereço || 'Não informado';
}

function formatarEnderecoVoluntario(endereço?: DadosEndereco): string {
  if (!endereço) return '';
  return joinParts(
    [
      endereço.logradouro,
      endereço.numero,
      endereço.complemento,
      endereço.bairro,
      endereço.cidade,
      endereço.uf,
      endereço.cep
    ],
    ', '
  );
}

function marcarAceite(valor?: boolean): { sim: string; nao: string } {
  return {
    sim: valor ? 'X' : '&nbsp;',
    nao: valor === false ? 'X' : '&nbsp;'
  };
}

function possuiTurno(
  disponibilidade: DisponibilidadeSemanal | undefined,
  diaKey: keyof DisponibilidadeSemanal,
  turno: string
): boolean {
  if (!disponibilidade) return false;
  const dia = disponibilidade[diaKey];
  return Array.isArray(dia) && dia.includes(turno);
}

export function buildTermoVoluntariadoHTML(unidade: UnidadeTermo, voluntário: ProfissionalTermo): string {
  const nomeUnidade =
    unidade.razaoSocial ||
    unidade.nomeFantasia ||
    'ADRA - Agência Adventista de Desenvolvimento e Recursos Assistenciais';
  const endereçoUnidade = formatarEnderecoUnidade(unidade);
  const voluntárioEndereco = formatarEnderecoVoluntario(voluntário.endereço);
  const aceiteLgpd = marcarAceite(voluntário.lgpdAceite);
  const aceiteImagem = marcarAceite(voluntário.imagemAceite);
  const outrasAtividades = escapeHtml(voluntário.voluntariadoOutros || '');
  const atividadesSelecionadas = voluntário.voluntariadoAtividades ?? [];

  const atividadesHtml = atividadesPadrao
    .map((atividade) => {
      const marcado = atividadesSelecionadas.includes(atividade) ? 'X' : '&nbsp;';
      return `<div class="check-item"><span class="check">${marcado}</span><span>${escapeHtml(
        atividade
      )}</span></div>`;
    })
    .join('');

  const disponibilidadeHtml = diasSemana
    .map((dia) => {
      const cells = turnos
        .map((turno) => `<td class="center">${possuiTurno(voluntário.voluntariadoDisponibilidade, dia.key, turno) ? 'X' : ''}</td>`)
        .join('');
      return `<tr><td class="center">${dia.label}</td>${cells}</tr>`;
    })
    .join('');

  return `
  <html>
    <head>
      <meta charset="utf-8" />
      <title>Termo de Ades&atilde;o ao Voluntariado - ADRA</title>
      <style>
        @page { size: A4; margin: 12mm; }
        body { font-family: Arial, sans-serif; color: #000; font-size: 12px; line-height: 1.35; }
        h1, h2 { margin: 0; text-align: center; }
        h1 { font-size: 16px; font-weight: 700; }
        h2 { font-size: 13px; font-weight: 700; margin-top: 6px; }
        .section { margin-top: 12px; }
        .line { border-bottom: 1px solid #000; padding: 3px 0; }
        .label { font-weight: 700; }
        .grid { display: grid; grid-template-columns: 1fr 1fr; gap: 8px 16px; }
        table { width: 100%; border-collapse: collapse; margin-top: 6px; }
        th, td { border: 1px solid #000; padding: 4px; font-size: 11px; }
        th { background: #f2f2f2; }
        .center { text-align: center; }
        .check-item { display: flex; align-items: center; gap: 6px; margin-bottom: 4px; }
        .check { width: 14px; height: 14px; border: 1px solid #000; display: inline-flex; align-items: center; justify-content: center; font-size: 11px; }
        .signature { display: grid; grid-template-columns: 1fr 1fr; gap: 24px; margin-top: 20px; }
        .signature-line { border-bottom: 1px solid #000; height: 18px; }
        .small { font-size: 10px; }
      </style>
    </head>
    <body>
      <h1>Termo de Ades&atilde;o ao Voluntariado &ndash; ADRA</h1>
      <h2>${escapeHtml(nomeUnidade)}</h2>

      <div class="section">
        <div class="line"><span class="label">CNPJ:</span> ${escapeHtml(unidade.cnpj || '')}</div>
        <div class="line"><span class="label">Endere&ccedil;o:</span> ${escapeHtml(endereçoUnidade)}</div>
        <div class="line"><span class="label">Cidade/UF:</span> ${escapeHtml(joinParts([unidade.cidade, unidade.estado], ' / '))}</div>
        ${unidade.inscricaoMunicipal ? `<div class="line"><span class="label">Inscri&ccedil;&atilde;o municipal:</span> ${escapeHtml(unidade.inscricaoMunicipal)}</div>` : ''}
      </div>

      <div class="section">
        <h2>Dados do volunt&aacute;rio</h2>
        <div class="grid">
          <div class="line"><span class="label">Nome:</span> ${escapeHtml(voluntário.nome)}</div>
          <div class="line"><span class="label">RG:</span> ${escapeHtml(voluntário.rg)}</div>
          <div class="line"><span class="label">CPF:</span> ${escapeHtml(voluntário.cpf)}</div>
          <div class="line"><span class="label">Data de nascimento:</span> ${escapeHtml(voluntário.dataNascimento)}</div>
          <div class="line"><span class="label">Estado civil:</span> ${escapeHtml(voluntário.estadoCivil)}</div>
          <div class="line"><span class="label">Email:</span> ${escapeHtml(voluntário.email)}</div>
          <div class="line"><span class="label">Celular:</span> ${escapeHtml(voluntário.telefoneCelular)}</div>
          <div class="line"><span class="label">Residencial:</span> ${escapeHtml(voluntário.telefoneResidencial)}</div>
        </div>
        <div class="line"><span class="label">Endere&ccedil;o completo:</span> ${escapeHtml(voluntárioEndereco)}</div>
        <div class="line"><span class="label">Volunt&aacute;rio em outra institui&ccedil;&atilde;o?</span> ${voluntário.voluntárioOutraInstituicao ? 'Sim' : 'Não'} - ${escapeHtml(voluntário.voluntárioOutraDescricao)}</div>
        <div class="line"><span class="label">Local da atividade requerida:</span> ${escapeHtml(voluntário.voluntariadoLocalAtividade)}</div>
        <div class="line"><span class="label">Per&iacute;odo do voluntariado:</span> ${escapeHtml(voluntário.voluntariadoPeriodo)}</div>
      </div>

      <div class="section">
        <h2>Disponibilidade (m&aacute;x. 16h semanais)</h2>
        <table>
          <thead>
            <tr>
              <th>Dia</th>
              <th>Manh&atilde;</th>
              <th>Tarde</th>
              <th>Noite</th>
            </tr>
          </thead>
          <tbody>
            ${disponibilidadeHtml}
          </tbody>
        </table>
      </div>

      <div class="section">
        <h2>Atividade volunt&aacute;ria</h2>
        ${atividadesHtml}
        <div class="line"><span class="label">Outros:</span> ${outrasAtividades}</div>
      </div>

      <div class="section">
        <h2>Condi&ccedil;&otilde;es gerais</h2>
        <ol>
          <li>Este termo est&aacute; regido pela Lei 9.608/1998 e n&atilde;o gera v&iacute;nculo empregat&iacute;cio.</li>
          <li>O volunt&aacute;rio atua de forma espont&acirc;nea e sem remunera&ccedil;&atilde;o, podendo receber ressarcimento de despesas.</li>
          <li>O volunt&aacute;rio dever&aacute; cumprir as orienta&ccedil;&otilde;es e pol&iacute;ticas da unidade.</li>
          <li>Este termo pode ser encerrado por qualquer das partes mediante comunica&ccedil;&atilde;o pr&eacute;via.</li>
          <li>Atividades dever&atilde;o respeitar limites de seguran&ccedil;a e capacidade do volunt&aacute;rio.</li>
          <li>O volunt&aacute;rio compromete-se a manter sigilo sobre informa&ccedil;&otilde;es sens&iacute;veis.</li>
          <li>Casos omissos ser&atilde;o resolvidos em comum acordo pela coordena&ccedil;&atilde;o local.</li>
        </ol>
      </div>

      <div class="section">
        <p>Declaro estar ciente e de acordo com as condi&ccedil;&otilde;es acima, aderindo ao programa de voluntariado da ADRA.</p>
        <div class="line"><span class="label">Local/Data:</span> ____________________________________________</div>
      </div>

      <div class="section">
        <h2>LGPD</h2>
        <p>Autorizo o tratamento dos meus dados pessoais para fins de voluntariado.</p>
        <div class="check-item"><span class="check">${aceiteLgpd.sim}</span> Aceito</div>
        <div class="check-item"><span class="check">${aceiteLgpd.nao}</span> N&atilde;o aceito</div>
      </div>

      <div class="section">
        <h2>Cess&atilde;o de imagem</h2>
        <p>Autorizo o uso de minha imagem para fins institucionais da ADRA.</p>
        <div class="check-item"><span class="check">${aceiteImagem.sim}</span> Aceito</div>
        <div class="check-item"><span class="check">${aceiteImagem.nao}</span> N&atilde;o aceito</div>
      </div>

      <div class="signature">
        <div>
          <div class="signature-line"></div>
          <div class="small center">Volunt&aacute;rio/Respons&aacute;vel</div>
        </div>
        <div>
          <div class="signature-line"></div>
          <div class="small center">Coordenador da unidade${unidade.coordenadorNome ? `: ${escapeHtml(unidade.coordenadorNome)}` : ''}</div>
        </div>
      </div>
    </body>
  </html>
  `;
}
