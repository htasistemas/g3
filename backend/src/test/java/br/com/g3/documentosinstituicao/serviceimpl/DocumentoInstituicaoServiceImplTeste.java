package br.com.g3.documentosinstituicao.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import br.com.g3.documentosinstituicao.domain.DocumentoInstituicao;
import br.com.g3.documentosinstituicao.domain.DocumentoInstituicaoHistorico;
import br.com.g3.documentosinstituicao.dto.DocumentoInstituicaoRequest;
import br.com.g3.documentosinstituicao.repository.DocumentoInstituicaoAnexoRepository;
import br.com.g3.documentosinstituicao.repository.DocumentoInstituicaoHistoricoRepository;
import br.com.g3.documentosinstituicao.repository.DocumentoInstituicaoRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class DocumentoInstituicaoServiceImplTeste {
  @Mock
  private DocumentoInstituicaoRepository repository;

  @Mock
  private DocumentoInstituicaoAnexoRepository anexoRepository;

  @Mock
  private DocumentoInstituicaoHistoricoRepository historicoRepository;

  @InjectMocks
  private DocumentoInstituicaoServiceImpl service;

  @Test
  void criarDeveCalcularSituacaoComoVenceEmBreve() {
    DocumentoInstituicaoRequest request = new DocumentoInstituicaoRequest();
    request.setTipoDocumento("Certidao");
    request.setOrgaoEmissor("Orgao");
    request.setEmissao(LocalDate.now());
    request.setValidade(LocalDate.now().plusDays(10));
    request.setSemVencimento(false);
    request.setEmRenovacao(false);

    when(repository.salvar(any(DocumentoInstituicao.class)))
        .thenAnswer(
            invocation -> {
              DocumentoInstituicao documento = invocation.getArgument(0);
              documento.setId(1L);
              return documento;
            });
    when(historicoRepository.salvar(any(DocumentoInstituicaoHistorico.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    String situacao = service.criar(request).getSituacao();

    assertEquals("vence_em_breve", situacao);
  }

  @Test
  void criarDeveRetornarErroQuandoValidadeAnteriorEmissao() {
    DocumentoInstituicaoRequest request = new DocumentoInstituicaoRequest();
    request.setTipoDocumento("Certidao");
    request.setOrgaoEmissor("Orgao");
    request.setEmissao(LocalDate.of(2025, 1, 10));
    request.setValidade(LocalDate.of(2025, 1, 1));
    request.setSemVencimento(false);

    ResponseStatusException erro =
        assertThrows(ResponseStatusException.class, () -> service.criar(request));

    assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());
  }
}
