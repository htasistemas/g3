package br.com.g3.autorizacaocompras.serviceimpl;

import br.com.g3.autorizacaocompras.domain.AutorizacaoCompra;
import br.com.g3.autorizacaocompras.domain.AutorizacaoCompraReservaBancaria;
import br.com.g3.autorizacaocompras.dto.AutorizacaoCompraRequest;
import br.com.g3.autorizacaocompras.dto.AutorizacaoCompraResponse;
import br.com.g3.autorizacaocompras.dto.AutorizacaoCompraReservaBancariaRequest;
import br.com.g3.autorizacaocompras.dto.AutorizacaoCompraReservaBancariaResponse;
import br.com.g3.autorizacaocompras.dto.AutorizacaoPagamentoRequest;
import br.com.g3.autorizacaocompras.mapper.AutorizacaoComprasMapper;
import br.com.g3.autorizacaocompras.repository.AutorizacaoComprasRepository;
import br.com.g3.autorizacaocompras.repository.AutorizacaoCompraReservaBancariaRepository;
import br.com.g3.autorizacaocompras.service.AutorizacaoComprasService;
import br.com.g3.contabilidade.domain.LancamentoFinanceiro;
import br.com.g3.contabilidade.repository.LancamentoFinanceiroRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class AutorizacaoComprasServiceImpl implements AutorizacaoComprasService {
  private final AutorizacaoComprasRepository repository;
  private final AutorizacaoCompraReservaBancariaRepository reservaBancariaRepository;
  private final LancamentoFinanceiroRepository lancamentoRepository;

  public AutorizacaoComprasServiceImpl(
      AutorizacaoComprasRepository repository,
      AutorizacaoCompraReservaBancariaRepository reservaBancariaRepository,
      LancamentoFinanceiroRepository lancamentoRepository) {
    this.repository = repository;
    this.reservaBancariaRepository = reservaBancariaRepository;
    this.lancamentoRepository = lancamentoRepository;
  }

  @Override
  public List<AutorizacaoCompraResponse> listar() {
    return repository.listar().stream().map(AutorizacaoComprasMapper::toResponse).collect(Collectors.toList());
  }

  @Override
  public AutorizacaoCompraResponse buscarPorId(Long id) {
    return repository.buscarPorId(id)
        .map(AutorizacaoComprasMapper::toResponse)
        .orElseThrow(() -> new IllegalArgumentException("Autorização não encontrada."));
  }

  @Override
  @Transactional
  public AutorizacaoCompraResponse criar(AutorizacaoCompraRequest request) {
    AutorizacaoCompra compra = AutorizacaoComprasMapper.toDomain(request);
    LocalDateTime agora = LocalDateTime.now();
    compra.setCriadoEm(agora);
    compra.setAtualizadoEm(agora);
    compra.setStatus(resolveStatus(request.getStatus()));
    return AutorizacaoComprasMapper.toResponse(repository.salvar(compra));
  }

  @Override
  @Transactional
  public AutorizacaoCompraResponse atualizar(Long id, AutorizacaoCompraRequest request) {
    AutorizacaoCompra compra =
        repository.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Autorização não encontrada."));
    AutorizacaoComprasMapper.aplicarAtualizacao(compra, request);
    if ("conclusao".equalsIgnoreCase(request.getStatus()) && !StringUtils.hasText(compra.getNumeroTermo())) {
      compra.setNumeroTermo(gerarNumeroTermoDocumento());
    }
    compra.setAtualizadoEm(LocalDateTime.now());
    compra.setStatus(resolveStatus(request.getStatus()));
    return AutorizacaoComprasMapper.toResponse(repository.salvar(compra));
  }

  @Override
  @Transactional
  public void remover(Long id) {
    AutorizacaoCompra compra =
        repository.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Autorização não encontrada."));
    repository.remover(compra);
  }

  @Override
  @Transactional
  public AutorizacaoCompraReservaBancariaResponse registrarReservaBancaria(
      Long id, AutorizacaoCompraReservaBancariaRequest request) {
    AutorizacaoCompra compra =
        repository.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Autorização não encontrada."));
    AutorizacaoCompraReservaBancaria reserva =
        reservaBancariaRepository
            .buscarPorCompraEConta(compra.getId(), request.getContaBancariaId())
            .orElseGet(AutorizacaoCompraReservaBancaria::new);
    reserva.setAutorizacaoCompraId(compra.getId());
    reserva.setContaBancariaId(request.getContaBancariaId());
    reserva.setValor(request.getValor());
    reserva.setCriadoEm(LocalDateTime.now());
    AutorizacaoCompraReservaBancaria salva = reservaBancariaRepository.salvar(reserva);
    AutorizacaoCompraReservaBancariaResponse response = new AutorizacaoCompraReservaBancariaResponse();
    response.setId(salva.getId());
    response.setAutorizacaoCompraId(salva.getAutorizacaoCompraId());
    response.setContaBancariaId(salva.getContaBancariaId());
    response.setValor(salva.getValor());
    response.setCriadoEm(salva.getCriadoEm());
    return response;
  }

  @Override
  @Transactional
  public void removerReservaBancaria(Long id, Long contaBancariaId) {
    AutorizacaoCompraReservaBancaria reserva =
        reservaBancariaRepository
            .buscarPorCompraEConta(id, contaBancariaId)
            .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada."));
    reservaBancariaRepository.remover(reserva);
  }

  @Override
  @Transactional
  public AutorizacaoCompraResponse gerarAutorizacaoPagamento(Long id, AutorizacaoPagamentoRequest request) {
    AutorizacaoCompra compra =
        repository.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Autorização não encontrada."));
    if (!StringUtils.hasText(compra.getAutorizacaoPagamentoNumero())) {
      compra.setAutorizacaoPagamentoNumero(gerarNumeroAutorizacaoPagamento());
    }
    compra.setAutorizacaoPagamentoAutor(request.getAutor());
    compra.setAutorizacaoPagamentoData(request.getData() != null ? request.getData() : LocalDate.now());
    compra.setAutorizacaoPagamentoObservacoes(request.getObservacoes());
    compra.setAtualizadoEm(LocalDateTime.now());
    AutorizacaoCompra salva = repository.salvar(compra);

    lancamentoRepository
        .buscarPorCompraId(compra.getId())
        .orElseGet(
            () -> {
              LancamentoFinanceiro lancamento = new LancamentoFinanceiro();
              lancamento.setTipo("pagar");
              lancamento.setDescricao("Autorização de pagamento - reserva");
              lancamento.setContraparte(
                  StringUtils.hasText(request.getAutor())
                      ? request.getAutor()
                      : (StringUtils.hasText(compra.getVencedor()) ? compra.getVencedor() : compra.getResponsavel()));
              lancamento.setVencimento(
                  compra.getDataPrevista() != null ? compra.getDataPrevista() : LocalDate.now());
              lancamento.setValor(totalReserva(compra.getId(), compra.getValor()));
              lancamento.setSituacao("aberto");
              lancamento.setCompraId(compra.getId());
              lancamento.setCriadoEm(LocalDateTime.now());
              lancamento.setAtualizadoEm(LocalDateTime.now());
              return lancamentoRepository.salvar(lancamento);
            });

    return AutorizacaoComprasMapper.toResponse(salva);
  }

  private String resolveStatus(String status) {
    return StringUtils.hasText(status) ? status : "solicitacao";
  }

  private String gerarNumeroTermoDocumento() {
    int ano = LocalDate.now().getYear();
    List<String> numeros =
        repository.listar().stream()
            .map(AutorizacaoCompra::getNumeroTermo)
            .filter(num -> StringUtils.hasText(num) && num.endsWith("/" + ano))
            .collect(Collectors.toList());
    int ultimo = numeros.stream()
        .map(num -> num.split("/")[0])
        .filter(StringUtils::hasText)
        .mapToInt(parte -> {
          try {
            return Integer.parseInt(parte);
          } catch (NumberFormatException ex) {
            return 0;
          }
        })
        .max()
        .orElse(0);
    return String.format("%04d/%d", ultimo + 1, ano);
  }

  private String gerarNumeroAutorizacaoPagamento() {
    int ano = LocalDate.now().getYear();
    List<String> numeros =
        repository.listar().stream()
            .map(AutorizacaoCompra::getAutorizacaoPagamentoNumero)
            .filter(num -> StringUtils.hasText(num) && num.endsWith("/" + ano))
            .collect(Collectors.toList());
    int ultimo = numeros.stream()
        .map(num -> num.split("/")[0])
        .filter(StringUtils::hasText)
        .mapToInt(parte -> {
          try {
            return Integer.parseInt(parte);
          } catch (NumberFormatException ex) {
            return 0;
          }
        })
        .max()
        .orElse(0);
    return String.format("%04d/%d", ultimo + 1, ano);
  }

  private BigDecimal totalReserva(Long compraId, BigDecimal valorPadrao) {
    BigDecimal total =
        reservaBancariaRepository.listarPorCompra(compraId).stream()
            .map(AutorizacaoCompraReservaBancaria::getValor)
            .filter(valor -> valor != null)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    return total.compareTo(BigDecimal.ZERO) > 0 ? total : (valorPadrao == null ? BigDecimal.ZERO : valorPadrao);
  }
}


