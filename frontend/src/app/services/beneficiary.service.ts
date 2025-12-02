import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { catchError, map, Observable, throwError } from 'rxjs';

export interface DocumentoObrigatorio {
  nome: string;
  nomeArquivo?: string;
  file?: File;
  obrigatorio?: boolean;
  required?: boolean;
  baseRequired?: boolean;
  conteudo?: string;
  contentType?: string;
}

export interface BeneficiaryPayload {
  id?: number;
  codigo?: string;
  cpf?: string;
  cep: string;
  nomeCompleto: string;
  nomeSocial?: string;
  apelido?: string;
  nomeMae?: string;
  nomePai?: string;
  rg?: string;
  orgaoEmissor?: string;
  ufEmissor?: string;
  dataEmissaoRg?: string;
  documentos: string;
  dataNascimento: string;
  idade?: number;
  sexo?: string;
  sexoBiologico?: string;
  identidadeGenero?: string;
  corRaca?: string;
  nacionalidade?: string;
  naturalidade?: string;
  telefone?: string;
  telefoneFixo?: string;
  celular?: string;
  contatoWhatsApp?: boolean;
  telefoneRecado?: string;
  nomeRecado?: string;
  email: string;
  logradouro?: string;
  endereco?: string;
  numero?: string;
  numeroEndereco?: string;
  complemento?: string;
  pontoReferencia?: string;
  bairro?: string;
  cidade?: string;
  estado?: string;
  uf?: string;
  zona?: string;
  situacaoImovel?: string;
  tipoMoradia?: string;
  condicoesMoradia?: string[];
  acessoAgua?: string;
  esgoto?: string;
  coletaLixo?: string;
  energiaEletrica?: string;
  internet?: string;
  parentesco?: string;
  observacoes?: string;
  status?: string;
  motivoBloqueio?: string;
  nis?: string;
  nisBeneficio?: string;
  estadoCivil?: string;
  situacaoEscolarCrianca?: string;
  responsavelLegal?: boolean;
  responsavelPor?: string;
  responsavelLegalPor?: string;
  situacaoMoradia?: string;
  pessoasResidencia?: number | string;
  rendaFamiliar?: number | string;
  rendaPerCapita?: number | string;
  renda?: number | string;
  situacaoTrabalho?: string;
  localTrabalho?: string;
  fonteRenda?: string;
  programasSociais?: string;
  contatoPreferencias?: string[];
  horarioContato?: string;
  autorizacaoContato?: boolean;
  possuiFilhosMenores?: boolean;
  possuiCnh?: boolean;
  quantidadeFilhosMenores?: number;
  escolaridade?: string;
  rendaIndividual?: number | string;
  informacoesMoradia?: string;
  condicoesSaneamento?: string;
  situacaoEmprego?: string;
  ocupacao?: string;
  alfabetizado?: string;
  serieConcluida?: string;
  situacaoEscolarAtual?: string;
  nivelEnsino?: string;
  moraComFamilia?: string;
  responsavelFamiliar?: boolean;
  vinculoFamiliar?: string;
  situacoesVulnerabilidade?: string[];
  acompanhamentos?: string[];
  tempoAcompanhamento?: string;
  origemEncaminhamento?: string;
  possuiDeficiencia?: string | boolean;
  tiposDeficiencia?: string[];
  cidPrincipal?: string;
  usaMedicacao?: string | boolean;
  medicacaoDescricao?: string;
  acompanhamentoSaude?: string[];
  recebeBeneficio?: string | boolean;
  beneficiosAtuais?: string[];
  valorBeneficios?: number | string;
  tempoBeneficios?: string;
  historicoBeneficio?: string;
  documentosAnexos: DocumentoObrigatorio[];
  foto?: string | null;
}

@Injectable({ providedIn: 'root' })
export class BeneficiaryService {
  private readonly baseUrls = [
    `${environment.apiUrl}/api/beneficiarios`,
    `${environment.apiUrl}/api/beneficiaries`
  ];

  constructor(private readonly http: HttpClient) {}

  getById(id: number): Observable<BeneficiaryPayload> {
    return this.requestWithFallback((baseUrl) =>
      this.http.get<{ beneficiario: BeneficiaryPayload }>(`${baseUrl}/${id}`)
    ).pipe(map(({ beneficiario }) => beneficiario));
  }

  getRequiredDocuments(): Observable<{ documents: DocumentoObrigatorio[] }> {
    return this.requestWithFallback((baseUrl) =>
      this.http.get<{ documents: DocumentoObrigatorio[] }>(`${baseUrl}/documents`)
    );
  }

  list(): Observable<{ beneficiarios: BeneficiaryPayload[] }> {
    return this.requestWithFallback((baseUrl) =>
      this.http.get<
        { beneficiarios?: BeneficiaryPayload[] } | { beneficiaries?: BeneficiaryPayload[] } | { data?: BeneficiaryPayload[] } | BeneficiaryPayload[]
      >(baseUrl)
    ).pipe(
      map((response) => {
        if (Array.isArray(response)) return { beneficiarios: response };

        if ('beneficiarios' in response) return { beneficiarios: response.beneficiarios ?? [] };

        if ('beneficiaries' in response) return { beneficiarios: response.beneficiaries ?? [] };

        if ('data' in response) return { beneficiarios: response.data ?? [] };

        return { beneficiarios: [] };
      })
    );
  }

  save(payload: BeneficiaryPayload, photoFile?: File | null): Observable<BeneficiaryPayload> {
    const formData = this.buildFormData(payload, photoFile);

    if (payload.id) {
      return this.requestWithFallback((baseUrl) =>
        this.http.put<BeneficiaryPayload>(`${baseUrl}/${payload.id}`, formData)
      );
    }

    return this.requestWithFallback((baseUrl) => this.http.post<BeneficiaryPayload>(baseUrl, formData));
  }

  private buildFormData(payload: BeneficiaryPayload, photoFile?: File | null): FormData {
    const formData = new FormData();
    const { documentosAnexos, foto, ...rest } = payload;

    Object.entries(rest).forEach(([key, value]) => {
      if (value === undefined || value === null) {
        return;
      }

      formData.append(key, typeof value === 'object' ? JSON.stringify(value) : String(value));
    });

    const documentsWithoutFiles = (documentosAnexos ?? []).map(({ file, ...doc }) => doc);
    formData.append('documentosAnexos', JSON.stringify(documentsWithoutFiles));

    documentosAnexos?.forEach((doc, index) => {
      if (doc.file) {
        formData.append(
          `documentosArquivos[${index}]`,
          doc.file,
          doc.file.name || doc.nomeArquivo || `documento-${index + 1}`
        );
      }
    });

    if (photoFile) {
      formData.append('fotoArquivo', photoFile, photoFile.name || 'foto.jpg');
    } else if (foto) {
      formData.append('foto', foto);
    }

    return formData;
  }

  private requestWithFallback<T>(requestFactory: (baseUrl: string) => Observable<T>): Observable<T> {
    const [primaryUrl, secondaryUrl] = this.baseUrls;

    return requestFactory(primaryUrl).pipe(
      catchError((primaryError) =>
        requestFactory(secondaryUrl).pipe(
          catchError((secondaryError) => throwError(() => secondaryError ?? primaryError))
        )
      )
    );
  }
}
