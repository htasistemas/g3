import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';
import { ProfissionaisCadastroComponent } from './profissionais-cadastro.component';

describe('ProfissionaisCadastroComponent', () => {
  function criarComponente(): ProfissionaisCadastroComponent {
    const fb = new FormBuilder();
    const professionalServiceMock = jasmine.createSpyObj('ProfessionalService', [
      'list',
      'create',
      'update',
      'delete'
    ]);
    professionalServiceMock.list.and.returnValue(of([]));
    const assistanceUnitServiceMock = jasmine.createSpyObj('AssistanceUnitService', ['get', 'list']);
    assistanceUnitServiceMock.get.and.returnValue(of({ unidade: null }));
    assistanceUnitServiceMock.list.and.returnValue(of([]));
    const termoPrintServiceMock = jasmine.createSpyObj('VoluntariadoTermoPrintService', ['printTermoVoluntariado']);
    const authServiceMock = jasmine.createSpyObj('AuthService', ['user']);
    const httpMock = jasmine.createSpyObj('HttpClient', ['get']);
    const salasServiceMock = jasmine.createSpyObj('SalasService', ['list']);
    salasServiceMock.list.and.returnValue(of([]));

    return new ProfissionaisCadastroComponent(
      fb,
      professionalServiceMock,
      assistanceUnitServiceMock,
      termoPrintServiceMock,
      authServiceMock,
      httpMock,
      salasServiceMock
    );
  }

  it('dispara a busca com Enter apenas uma vez', () => {
    const componente = criarComponente();
    const evento = {
      preventDefault: jasmine.createSpy('preventDefault'),
      stopPropagation: jasmine.createSpy('stopPropagation')
    } as any;
    spyOn(componente, 'onBuscar');

    componente.onBuscarEnter(evento);

    expect(componente.onBuscar).toHaveBeenCalledTimes(1);
    expect(evento.preventDefault).toHaveBeenCalled();
    expect(evento.stopPropagation).toHaveBeenCalled();
  });

  it('aciona busca e abre a aba de listagem', () => {
    const componente = criarComponente();
    const buscarSpy = spyOn<any>(componente, 'buscarProfissionaisNaListagem');
    componente.changeTab('dados');

    componente.onBuscar();

    expect(componente.activeTab).toBe('lista');
    expect(buscarSpy).toHaveBeenCalled();
  });
});
