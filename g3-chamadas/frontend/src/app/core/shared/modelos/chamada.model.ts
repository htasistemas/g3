export interface ChamadaModel {
  idChamada: string;
  idFilaAtendimento: number;
  nomeBeneficiario: string;
  localAtendimento: string;
  statusChamada: string;
  dataHoraChamada: string;
  chamadoPor?: string;
}

export interface ChamadaEventoModel {
  evento: string;
  dados: {
    idChamada: string;
    nomeBeneficiario: string;
    localAtendimento: string;
    dataHoraChamada: string;
  };
}
