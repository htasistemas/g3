import { Beneficiario } from '../entities/Beneficiario';

interface TextoAssistentePayload {
  beneficiario?: Partial<Beneficiario> & { idadeAproximada?: number; situacaoBasica?: string };
  tipo?: 'parecer' | 'relatorio' | 'plano_atendimento' | string;
}

export function gerarTextoSugerido(payload: TextoAssistentePayload): string {
  const nome = payload.beneficiario?.nomeCompleto || 'o beneficiário';
  const idade = payload.beneficiario?.idadeAproximada ?? calcularIdade(payload.beneficiario?.dataNascimento);
  const situacao = payload.beneficiario?.situacaoBasica || 'acompanhamento social em andamento';
  const tipo = (payload.tipo || 'parecer').toLowerCase();

  const baseIntro = `O(a) beneficiário(a) ${nome}${idade ? `, com idade aproximada de ${idade} anos` : ''}, ` +
    `encontra-se em situação de ${situacao}.`;

  if (tipo === 'plano_atendimento') {
    return `${baseIntro} Sugere-se a elaboração de um plano de atendimento com objetivos claros, metas mensuráveis e acompanhamento periódico. ` +
      `As ações devem priorizar a proteção social, fortalecimento de vínculos e acesso a direitos, com revisões trimestrais.`;
  }

  if (tipo === 'relatorio') {
    return `${baseIntro} Foram identificados aspectos relevantes do contexto familiar e socioeconômico que demandam atenção continuada. ` +
      `Recomenda-se registrar evoluções, encaminhamentos e resultados de intervenções para compor o histórico do prontuário.`;
  }

  return `${baseIntro} Com base nas informações levantadas, recomenda-se registrar parecer técnico, ` +
    `indicando encaminhamentos, orientações e monitoramento conforme as diretrizes do SUAS.`;
}

function calcularIdade(dataNascimento?: string): number | undefined {
  if (!dataNascimento) return undefined;
  const nascimento = new Date(dataNascimento);
  if (Number.isNaN(nascimento.getTime())) return undefined;

  const hoje = new Date();
  let idade = hoje.getFullYear() - nascimento.getFullYear();
  const mesAtual = hoje.getMonth();
  const mesNascimento = nascimento.getMonth();
  if (mesAtual < mesNascimento || (mesAtual === mesNascimento && hoje.getDate() < nascimento.getDate())) {
    idade -= 1;
  }
  return idade;
}
