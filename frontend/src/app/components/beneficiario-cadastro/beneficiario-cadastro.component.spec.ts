import { FormBuilder } from '@angular/forms';
import { NgZone } from '@angular/core';
import { convertToParamMap } from '@angular/router';
import { of } from 'rxjs';
import { BeneficiarioCadastroComponent } from './beneficiario-cadastro.component';

describe('BeneficiarioCadastroComponent', () => {
  function criarComponente(): BeneficiarioCadastroComponent {
    const fb = new FormBuilder();
    const serviceMock = jasmine.createSpyObj('BeneficiarioApiService', [
      'list',
      'getById',
      'create',
      'update',
      'delete',
      'geocodificarEndereco'
    ]);
    const beneficiaryServiceMock = jasmine.createSpyObj('BeneficiaryService', ['list']);
    const configServiceMock = {} as any;
    const routerMock = jasmine.createSpyObj('Router', ['navigate']);
    const routeMock = { paramMap: of(convertToParamMap({})) } as any;
    const httpMock = jasmine.createSpyObj('HttpClient', ['get']);
    const assistanceUnitServiceMock = jasmine.createSpyObj('AssistanceUnitService', ['get']);
    const reportServiceMock = jasmine.createSpyObj('ReportService', [
      'gerarRelatorioBeneficiarios',
      'gerarFichaBeneficiario',
      'gerarTermoAutorizacao'
    ]);
    const authServiceMock = jasmine.createSpyObj('AuthService', ['getUsuarioAtual']);
    const ngZone = new NgZone({ enableLongStackTrace: false });
    const sanitizerMock = jasmine.createSpyObj('DomSanitizer', ['bypassSecurityTrustResourceUrl']);

    return new BeneficiarioCadastroComponent(
      fb,
      serviceMock,
      beneficiaryServiceMock,
      configServiceMock,
      routerMock,
      routeMock,
      httpMock,
      assistanceUnitServiceMock,
      reportServiceMock,
      authServiceMock,
      ngZone,
      sanitizerMock
    );
  }

  it('dispara a busca com Enter apenas uma vez', () => {
    const componente = criarComponente();
    const evento = {
      preventDefault: jasmine.createSpy('preventDefault'),
      stopPropagation: jasmine.createSpy('stopPropagation')
    } as any;
    spyOn(componente, 'buscarBeneficiariosNaListagem');

    componente.onBuscarEnter(evento);

    expect(componente.buscarBeneficiariosNaListagem).toHaveBeenCalledTimes(1);
    expect(evento.preventDefault).toHaveBeenCalled();
    expect(evento.stopPropagation).toHaveBeenCalled();
  });

  it('reinicia o cadastro ao cancelar', () => {
    const componente = criarComponente();
    spyOn(componente, 'startNewBeneficiario');

    componente.onCancel();

    expect(componente.startNewBeneficiario).toHaveBeenCalled();
  });

  it('exibe popup de sucesso ao salvar', async () => {
    const componente = criarComponente();
    const serviceMock = (componente as any).service;
    serviceMock.create.and.returnValue(
      of({
        beneficiario: {
          id_beneficiario: '10',
          codigo: '0010',
          nome_completo: 'Maria Teste',
          nome_mae: 'Josefa Teste',
          data_nascimento: '1990-01-01',
          cpf: '11144477735',
          status: 'EM_ANALISE'
        }
      })
    );
    (componente as any).beneficiarioId = null;
    componente.form.patchValue({
      status: 'EM_ANALISE',
      dadosPessoais: {
        nome_completo: 'Maria Teste',
        data_nascimento: '1990-01-01',
        nome_mae: 'Josefa Teste'
      },
      endereco: { cep: '12345-678' },
      contato: { telefone_principal: '11999999999' },
      documentos: { cpf: '111.444.777-35' },
      observacoes: { aceite_lgpd: true }
    });

    await componente.submit();

    expect(componente.popupTitulo).toBe('Sucesso');
    expect(componente.popupMensagens.length).toBe(1);
  });
});
