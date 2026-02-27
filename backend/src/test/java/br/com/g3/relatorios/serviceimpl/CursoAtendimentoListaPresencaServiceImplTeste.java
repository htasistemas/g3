package br.com.g3.relatorios.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.g3.relatorios.dto.CursoAtendimentoListaPresencaRequest;
import br.com.g3.relatorios.serviceimpl.CursoAtendimentoListaPresencaServiceImpl;
import br.com.g3.cursosatendimentos.repository.CursoAtendimentoRepository;
import br.com.g3.unidadeassistencial.service.UnidadeAssistencialService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class CursoAtendimentoListaPresencaServiceImplTeste {
  @Mock
  private CursoAtendimentoRepository repository;

  @Mock
  private UnidadeAssistencialService unidadeService;

  @InjectMocks
  private CursoAtendimentoListaPresencaServiceImpl service;

  @Test
  void deveFalharQuandoRequestNulo() {
    ResponseStatusException ex =
        assertThrows(ResponseStatusException.class, () -> service.gerarPdf(null));
    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
  }

  @Test
  void deveFalharQuandoDadosObrigatoriosNaoInformados() {
    CursoAtendimentoListaPresencaRequest request = new CursoAtendimentoListaPresencaRequest();
    ResponseStatusException ex =
        assertThrows(ResponseStatusException.class, () -> service.gerarPdf(request));
    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
  }
}
