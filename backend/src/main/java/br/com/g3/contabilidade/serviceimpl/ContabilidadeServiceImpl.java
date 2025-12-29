package br.com.g3.contabilidade.serviceimpl;

import br.com.g3.contabilidade.domain.ContaBancaria;
import br.com.g3.contabilidade.domain.EmendaImpositiva;
import br.com.g3.contabilidade.domain.LancamentoFinanceiro;
import br.com.g3.contabilidade.domain.MovimentacaoFinanceira;
import br.com.g3.contabilidade.dto.ContaBancariaRequest;
import br.com.g3.contabilidade.dto.ContaBancariaResponse;
import br.com.g3.contabilidade.dto.EmendaImpositivaRequest;
import br.com.g3.contabilidade.dto.EmendaImpositivaResponse;
import br.com.g3.contabilidade.dto.LancamentoFinanceiroRequest;
import br.com.g3.contabilidade.dto.LancamentoFinanceiroResponse;
import br.com.g3.contabilidade.dto.MovimentacaoFinanceiraRequest;
import br.com.g3.contabilidade.dto.MovimentacaoFinanceiraResponse;
import br.com.g3.contabilidade.repository.ContaBancariaRepository;
import br.com.g3.contabilidade.repository.EmendaImpositivaRepository;
import br.com.g3.contabilidade.repository.LancamentoFinanceiroRepository;
import br.com.g3.contabilidade.repository.MovimentacaoFinanceiraRepository;
import br.com.g3.contabilidade.service.ContabilidadeService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ContabilidadeServiceImpl implements ContabilidadeService {
  private final ContaBancariaRepository contaRepository;
  private final LancamentoFinanceiroRepository lancamentoRepository;
  private final MovimentacaoFinanceiraRepository movimentacaoRepository;
  private final EmendaImpositivaRepository emendaRepository;

  public ContabilidadeServiceImpl(
      ContaBancariaRepository contaRepository,
      LancamentoFinanceiroRepository lancamentoRepository,
      MovimentacaoFinanceiraRepository movimentacaoRepository,
      EmendaImpositivaRepository emendaRepository) {
    this.contaRepository = contaRepository;
    this.lancamentoRepository = lancamentoRepository;
    this.movimentacaoRepository = movimentacaoRepository;
    this.emendaRepository = emendaRepository;
  }

  @Override
  @Transactional
  public ContaBancariaResponse criarContaBancaria(ContaBancariaRequest request) {
    validarChavePix(request);
    ContaBancaria conta = new ContaBancaria();
    conta.setBanco(request.getBanco());
    conta.setAgencia(request.getAgencia());
    conta.setNumero(request.getNumero());
    conta.setTipo(request.getTipo());
    conta.setProjetoVinculado(request.getProjetoVinculado());
    conta.setPixVinculado(Boolean.TRUE.equals(request.getPixVinculado()));
    conta.setTipoChavePix(Boolean.TRUE.equals(request.getPixVinculado()) ? request.getTipoChavePix() : null);
    conta.setChavePix(Boolean.TRUE.equals(request.getPixVinculado()) ? request.getChavePix() : null);
    conta.setSaldo(normalizarValor(request.getSaldo()));
    conta.setDataAtualizacao(request.getDataAtualizacao());
    LocalDateTime agora = LocalDateTime.now();
    conta.setCriadoEm(agora);
    conta.setAtualizadoEm(agora);
    return mapConta(contaRepository.salvar(conta));
  }

  @Override
  @Transactional(readOnly = true)
  public List<ContaBancariaResponse> listarContasBancarias() {
    return contaRepository.listar().stream().map(this::mapConta).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public ContaBancariaResponse atualizarContaBancaria(Long id, ContaBancariaRequest request) {
    validarChavePix(request);
    ContaBancaria conta =
        contaRepository.buscarPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    conta.setBanco(request.getBanco());
    conta.setAgencia(request.getAgencia());
    conta.setNumero(request.getNumero());
    conta.setTipo(request.getTipo());
    conta.setProjetoVinculado(request.getProjetoVinculado());
    conta.setPixVinculado(Boolean.TRUE.equals(request.getPixVinculado()));
    conta.setTipoChavePix(Boolean.TRUE.equals(request.getPixVinculado()) ? request.getTipoChavePix() : null);
    conta.setChavePix(Boolean.TRUE.equals(request.getPixVinculado()) ? request.getChavePix() : null);
    conta.setSaldo(normalizarValor(request.getSaldo()));
    conta.setDataAtualizacao(request.getDataAtualizacao());
    conta.setAtualizadoEm(LocalDateTime.now());
    return mapConta(contaRepository.salvar(conta));
  }

  @Override
  @Transactional
  public void removerContaBancaria(Long id) {
    contaRepository.remover(id);
  }

  @Override
  @Transactional
  public LancamentoFinanceiroResponse criarLancamento(LancamentoFinanceiroRequest request) {
    LancamentoFinanceiro lancamento = new LancamentoFinanceiro();
    lancamento.setTipo(request.getTipo());
    lancamento.setDescricao(request.getDescricao());
    lancamento.setContraparte(request.getContraparte());
    lancamento.setVencimento(request.getVencimento());
    lancamento.setValor(normalizarValor(request.getValor()));
    lancamento.setSituacao(request.getSituacao());
    LocalDateTime agora = LocalDateTime.now();
    lancamento.setCriadoEm(agora);
    lancamento.setAtualizadoEm(agora);
    return mapLancamento(lancamentoRepository.salvar(lancamento));
  }

  @Override
  @Transactional
  public LancamentoFinanceiroResponse atualizarLancamento(Long id, LancamentoFinanceiroRequest request) {
    LancamentoFinanceiro lancamento =
        lancamentoRepository
            .buscarPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    lancamento.setTipo(request.getTipo());
    lancamento.setDescricao(request.getDescricao());
    lancamento.setContraparte(request.getContraparte());
    lancamento.setVencimento(request.getVencimento());
    lancamento.setValor(normalizarValor(request.getValor()));
    lancamento.setSituacao(request.getSituacao());
    lancamento.setAtualizadoEm(LocalDateTime.now());
    return mapLancamento(lancamentoRepository.salvar(lancamento));
  }

  @Override
  @Transactional(readOnly = true)
  public List<LancamentoFinanceiroResponse> listarLancamentos() {
    return lancamentoRepository.listar().stream().map(this::mapLancamento).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public LancamentoFinanceiroResponse atualizarSituacaoLancamento(Long id, String status) {
    LancamentoFinanceiro lancamento =
        lancamentoRepository
            .buscarPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    lancamento.setSituacao(status);
    lancamento.setAtualizadoEm(LocalDateTime.now());
    return mapLancamento(lancamentoRepository.salvar(lancamento));
  }

  @Override
  @Transactional
  public MovimentacaoFinanceiraResponse criarMovimentacao(MovimentacaoFinanceiraRequest request) {
    MovimentacaoFinanceira movimentacao = new MovimentacaoFinanceira();
    movimentacao.setTipo(request.getTipo());
    movimentacao.setDescricao(request.getDescricao());
    movimentacao.setContraparte(request.getContraparte());
    movimentacao.setCategoria(request.getCategoria());
    movimentacao.setDataMovimentacao(request.getDataMovimentacao());
    movimentacao.setValor(normalizarValor(request.getValor()));
    movimentacao.setCriadoEm(LocalDateTime.now());

    ContaBancaria conta = null;
    if (request.getContaBancariaId() != null) {
      Optional<ContaBancaria> contaOpt = contaRepository.buscarPorId(request.getContaBancariaId());
      if (contaOpt.isPresent()) {
        conta = contaOpt.get();
        movimentacao.setContaBancaria(conta);
        atualizarSaldoConta(conta, movimentacao);
      }
    }

    MovimentacaoFinanceira salvo = movimentacaoRepository.salvar(movimentacao);
    return mapMovimentacao(salvo, conta);
  }

  @Override
  @Transactional(readOnly = true)
  public List<MovimentacaoFinanceiraResponse> listarMovimentacoes() {
    return movimentacaoRepository.listar().stream()
        .map(this::mapMovimentacao)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public EmendaImpositivaResponse criarEmenda(EmendaImpositivaRequest request) {
    EmendaImpositiva emenda = new EmendaImpositiva();
    emenda.setIdentificacao(request.getIdentificacao());
    emenda.setReferenciaLegal(request.getReferenciaLegal());
    emenda.setDataPrevista(request.getDataPrevista());
    emenda.setValorPrevisto(normalizarValor(request.getValorPrevisto()));
    emenda.setDiasAlerta(request.getDiasAlerta() == null ? 15 : request.getDiasAlerta());
    emenda.setStatus(request.getStatus());
    emenda.setObservacoes(request.getObservacoes());
    LocalDateTime agora = LocalDateTime.now();
    emenda.setCriadoEm(agora);
    emenda.setAtualizadoEm(agora);
    return mapEmenda(emendaRepository.salvar(emenda));
  }

  @Override
  @Transactional(readOnly = true)
  public List<EmendaImpositivaResponse> listarEmendas() {
    return emendaRepository.listar().stream().map(this::mapEmenda).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public EmendaImpositivaResponse atualizarStatusEmenda(Long id, String status) {
    EmendaImpositiva emenda =
        emendaRepository
            .buscarPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    emenda.setStatus(status);
    emenda.setAtualizadoEm(LocalDateTime.now());
    return mapEmenda(emendaRepository.salvar(emenda));
  }

  private ContaBancariaResponse mapConta(ContaBancaria conta) {
    ContaBancariaResponse response = new ContaBancariaResponse();
    response.setId(conta.getId());
    response.setBanco(conta.getBanco());
    response.setAgencia(conta.getAgencia());
    response.setNumero(conta.getNumero());
    response.setTipo(conta.getTipo());
    response.setProjetoVinculado(conta.getProjetoVinculado());
    response.setPixVinculado(conta.getPixVinculado());
    response.setTipoChavePix(conta.getTipoChavePix());
    response.setChavePix(conta.getChavePix());
    response.setSaldo(conta.getSaldo());
    response.setDataAtualizacao(conta.getDataAtualizacao());
    return response;
  }

  private LancamentoFinanceiroResponse mapLancamento(LancamentoFinanceiro lancamento) {
    LancamentoFinanceiroResponse response = new LancamentoFinanceiroResponse();
    response.setId(lancamento.getId());
    response.setTipo(lancamento.getTipo());
    response.setDescricao(lancamento.getDescricao());
    response.setContraparte(lancamento.getContraparte());
    response.setVencimento(lancamento.getVencimento());
    response.setValor(lancamento.getValor());
    response.setSituacao(lancamento.getSituacao());
    return response;
  }

  private MovimentacaoFinanceiraResponse mapMovimentacao(MovimentacaoFinanceira movimentacao) {
    return mapMovimentacao(movimentacao, movimentacao.getContaBancaria());
  }

  private MovimentacaoFinanceiraResponse mapMovimentacao(MovimentacaoFinanceira movimentacao, ContaBancaria conta) {
    MovimentacaoFinanceiraResponse response = new MovimentacaoFinanceiraResponse();
    response.setId(movimentacao.getId());
    response.setTipo(movimentacao.getTipo());
    response.setDescricao(movimentacao.getDescricao());
    response.setContraparte(movimentacao.getContraparte());
    response.setCategoria(movimentacao.getCategoria());
    response.setDataMovimentacao(movimentacao.getDataMovimentacao());
    response.setValor(movimentacao.getValor());
    if (conta != null) {
      response.setContaBancariaId(conta.getId());
      response.setContaBancariaNumero(conta.getNumero());
      response.setContaBancariaBanco(conta.getBanco());
    }
    return response;
  }

  private EmendaImpositivaResponse mapEmenda(EmendaImpositiva emenda) {
    EmendaImpositivaResponse response = new EmendaImpositivaResponse();
    response.setId(emenda.getId());
    response.setIdentificacao(emenda.getIdentificacao());
    response.setReferenciaLegal(emenda.getReferenciaLegal());
    response.setDataPrevista(emenda.getDataPrevista());
    response.setValorPrevisto(emenda.getValorPrevisto());
    response.setDiasAlerta(emenda.getDiasAlerta());
    response.setStatus(emenda.getStatus());
    response.setObservacoes(emenda.getObservacoes());
    return response;
  }

  private void atualizarSaldoConta(ContaBancaria conta, MovimentacaoFinanceira movimentacao) {
    BigDecimal saldoAtual = conta.getSaldo() == null ? BigDecimal.ZERO : conta.getSaldo();
    BigDecimal valor = movimentacao.getValor() == null ? BigDecimal.ZERO : movimentacao.getValor();
    BigDecimal novoSaldo;
    if ("ENTRADA".equalsIgnoreCase(movimentacao.getTipo())) {
      novoSaldo = saldoAtual.add(valor);
    } else {
      novoSaldo = saldoAtual.subtract(valor);
    }
    conta.setSaldo(novoSaldo);
    conta.setDataAtualizacao(movimentacao.getDataMovimentacao());
    conta.setAtualizadoEm(LocalDateTime.now());
    contaRepository.salvar(conta);
  }

  private BigDecimal normalizarValor(BigDecimal valor) {
    return valor == null ? BigDecimal.ZERO : valor;
  }

  private void validarChavePix(ContaBancariaRequest request) {
    if (!Boolean.TRUE.equals(request.getPixVinculado())) {
      return;
    }
    String tipo = request.getTipoChavePix();
    String chave = request.getChavePix();
    if (tipo == null || tipo.trim().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o tipo da chave Pix.");
    }
    if (chave == null || chave.trim().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe a chave Pix.");
    }
    String chaveLimpa = chave.trim();
    if ("cnpj".equalsIgnoreCase(tipo) && !validarCnpj(chaveLimpa)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CNPJ invalido na chave Pix.");
    }
    if ("email".equalsIgnoreCase(tipo) && !validarEmail(chaveLimpa)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email invalido na chave Pix.");
    }
    if ("telefone".equalsIgnoreCase(tipo) && !validarTelefone(chaveLimpa)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Telefone invalido na chave Pix.");
    }
    if ("aleatoria".equalsIgnoreCase(tipo) && !validarChaveAleatoria(chaveLimpa)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chave aleatoria invalida.");
    }
  }

  private boolean validarCnpj(String valor) {
    String cnpj = valor.replaceAll("\\\\D", "");
    if (cnpj.length() != 14 || cnpj.matches("(\\\\d)\\\\1{13}")) {
      return false;
    }
    int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    int soma = 0;
    for (int i = 0; i < pesos1.length; i++) {
      soma += Character.getNumericValue(cnpj.charAt(i)) * pesos1[i];
    }
    int resto = soma % 11;
    int digito1 = resto < 2 ? 0 : 11 - resto;
    if (digito1 != Character.getNumericValue(cnpj.charAt(12))) {
      return false;
    }
    soma = 0;
    for (int i = 0; i < pesos2.length; i++) {
      soma += Character.getNumericValue(cnpj.charAt(i)) * pesos2[i];
    }
    resto = soma % 11;
    int digito2 = resto < 2 ? 0 : 11 - resto;
    return digito2 == Character.getNumericValue(cnpj.charAt(13));
  }

  private boolean validarEmail(String valor) {
    return valor.matches("^[^\\\\s@]+@[^\\\\s@]+\\\\.[^\\\\s@]+$");
  }

  private boolean validarTelefone(String valor) {
    String telefone = valor.replaceAll("\\\\D", "");
    return telefone.length() == 10 || telefone.length() == 11;
  }

  private boolean validarChaveAleatoria(String valor) {
    return valor.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$");
  }
}
