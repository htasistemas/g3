package br.com.g3.contabilidade.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.g3.autorizacaocompras.repository.AutorizacaoCompraReservaBancariaRepository;
import br.com.g3.contabilidade.domain.LancamentoFinanceiro;
import br.com.g3.contabilidade.repository.ContaBancariaRepository;
import br.com.g3.contabilidade.repository.EmendaImpositivaRepository;
import br.com.g3.contabilidade.repository.LancamentoFinanceiroRepository;
import br.com.g3.contabilidade.repository.MovimentacaoFinanceiraRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class ContabilidadeServiceImplTeste {
  @Mock
  private ContaBancariaRepository contaRepository;

  @Mock
  private LancamentoFinanceiroRepository lancamentoRepository;

  @Mock
  private MovimentacaoFinanceiraRepository movimentacaoRepository;

  @Mock
  private EmendaImpositivaRepository emendaRepository;

  @Mock
  private AutorizacaoCompraReservaBancariaRepository reservaBancariaRepository;

  @InjectMocks
  private ContabilidadeServiceImpl service;

  @Test
  void naoDeveExcluirLancamentoVinculadoACompra() {
    LancamentoFinanceiro lancamento = new LancamentoFinanceiro();
    lancamento.setId(10L);
    lancamento.setCompraId(99L);
    when(lancamentoRepository.buscarPorId(10L)).thenReturn(Optional.of(lancamento));

    ResponseStatusException ex =
        assertThrows(ResponseStatusException.class, () -> service.removerLancamento(10L));

    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    verify(lancamentoRepository, never()).remover(anyLong());
  }

  @Test
  void deveExcluirLancamentoSemCompra() {
    LancamentoFinanceiro lancamento = new LancamentoFinanceiro();
    lancamento.setId(11L);
    when(lancamentoRepository.buscarPorId(11L)).thenReturn(Optional.of(lancamento));

    service.removerLancamento(11L);

    verify(lancamentoRepository).remover(11L);
  }
}
