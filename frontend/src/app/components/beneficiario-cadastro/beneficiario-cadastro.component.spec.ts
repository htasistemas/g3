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
    spyOn(componente, 'triggerSearchFromUi');

    componente.onBuscarEnter(evento);

    expect(componente.triggerSearchFromUi).toHaveBeenCalledTimes(1);
    expect(evento.preventDefault).toHaveBeenCalled();
    expect(evento.stopPropagation).toHaveBeenCalled();
  });

  it('abre confirmação ao cancelar com alterações pendentes', () => {
    const componente = criarComponente();
    componente.form.markAsDirty();

    componente.onCancel();

    expect(componente.dialogConfirmacaoAberta).toBeTrue();
    expect(componente.dialogTitulo).toBe('Descartar alterações');
  });

  it('volta para a listagem ao cancelar sem alterações em novo cadastro', () => {
    const componente = criarComponente();
    componente.form.markAsPristine();
    componente.beneficiarios = [{} as any];

    componente.onCancel();

    expect(componente.activeTab).toBe('lista');
  });
});
