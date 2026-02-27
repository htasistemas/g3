package br.com.g3.relatorios.serviceimpl;

import br.com.g3.contabilidade.domain.ContaBancaria;
import br.com.g3.contabilidade.domain.LancamentoFinanceiro;
import br.com.g3.contabilidade.domain.MovimentacaoFinanceira;
import br.com.g3.contabilidade.repository.ContaBancariaRepository;
import br.com.g3.contabilidade.repository.LancamentoFinanceiroRepository;
import br.com.g3.contabilidade.repository.MovimentacaoFinanceiraRepository;
import br.com.g3.relatorios.dto.RelatorioContasAPagarRequest;
import br.com.g3.relatorios.dto.RelatorioContasAReceberRequest;
import br.com.g3.relatorios.dto.RelatorioContasBancariasRequest;
import br.com.g3.relatorios.dto.RelatorioExtratoMensalRequest;
import br.com.g3.relatorios.service.RelatorioContabilidadeService;
import br.com.g3.relatorios.util.HtmlPdfRenderer;
import br.com.g3.relatorios.util.RelatorioTemplatePadrao;
import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialResponse;
import br.com.g3.unidadeassistencial.service.UnidadeAssistencialService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RelatorioContabilidadeServiceImpl implements RelatorioContabilidadeService {
  // implementar relatorio conforme #relatorio padrao
  private static final DateTimeFormatter DATA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  private static final DateTimeFormatter MES_FORMATTER = DateTimeFormatter.ofPattern("MMMM/yyyy", new Locale("pt", "BR"));
  private static final DateTimeFormatter MES_REFERENCIA_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

  private final MovimentacaoFinanceiraRepository movimentacaoRepository;
  private final LancamentoFinanceiroRepository lancamentoRepository;
  private final ContaBancariaRepository contaRepository;
  private final UnidadeAssistencialService unidadeService;

  public RelatorioContabilidadeServiceImpl(
      MovimentacaoFinanceiraRepository movimentacaoRepository,
      LancamentoFinanceiroRepository lancamentoRepository,
      ContaBancariaRepository contaRepository,
      UnidadeAssistencialService unidadeService) {
    this.movimentacaoRepository = movimentacaoRepository;
    this.lancamentoRepository = lancamentoRepository;
    this.contaRepository = contaRepository;
    this.unidadeService = unidadeService;
  }

  @Override
  public byte[] gerarExtratoMensal(RelatorioExtratoMensalRequest request) {
    YearMonth mesReferencia = obterMesReferencia(request);
    List<MovimentacaoFinanceira> movimentos = movimentacaoRepository.listar().stream()
        .filter(item -> item.getDataMovimentacao() != null)
        .filter(item -> YearMonth.from(item.getDataMovimentacao()).equals(mesReferencia))
        .sorted(Comparator.comparing(MovimentacaoFinanceira::getDataMovimentacao).reversed()
            .thenComparing(item -> item.getId() == null ? 0L : item.getId(), Comparator.reverseOrder()))
        .collect(Collectors.toList());

    UnidadeAssistencialResponse unidade = unidadeService.obterAtual();
    String corpoHtml = montarExtratoMensalHtml(mesReferencia, movimentos);
    String html = RelatorioTemplatePadrao.buildHtml(
        "Extrato mensal",
        corpoHtml,
        unidade,
        obterUsuarioEmissor(request != null ? request.getUsuarioEmissor() : null),
        LocalDateTime.now());
    return HtmlPdfRenderer.render(html);
  }

  @Override
  public byte[] gerarContasAReceber(RelatorioContasAReceberRequest request) {
    List<LancamentoFinanceiro> lancamentos = lancamentoRepository.listar().stream()
        .filter(item -> "receber".equalsIgnoreCase(item.getTipo()))
        .filter(item -> !"pago".equalsIgnoreCase(item.getSituacao()))
        .sorted(Comparator.comparing(LancamentoFinanceiro::getVencimento, Comparator.nullsLast(Comparator.naturalOrder())))
        .collect(Collectors.toList());

    UnidadeAssistencialResponse unidade = unidadeService.obterAtual();
    String corpoHtml = montarLancamentosHtml("Contas a receber", lancamentos);
    String html = RelatorioTemplatePadrao.buildHtml(
        "Contas a receber",
        corpoHtml,
        unidade,
        obterUsuarioEmissor(request != null ? request.getUsuarioEmissor() : null),
        LocalDateTime.now());
    return HtmlPdfRenderer.render(html);
  }

  @Override
  public byte[] gerarContasAPagar(RelatorioContasAPagarRequest request) {
    List<LancamentoFinanceiro> lancamentos = lancamentoRepository.listar().stream()
        .filter(item -> "pagar".equalsIgnoreCase(item.getTipo()))
        .filter(item -> !"pago".equalsIgnoreCase(item.getSituacao()))
        .sorted(Comparator.comparing(LancamentoFinanceiro::getVencimento, Comparator.nullsLast(Comparator.naturalOrder())))
        .collect(Collectors.toList());

    UnidadeAssistencialResponse unidade = unidadeService.obterAtual();
    String corpoHtml = montarLancamentosHtml("Contas a pagar", lancamentos);
    String html = RelatorioTemplatePadrao.buildHtml(
        "Contas a pagar",
        corpoHtml,
        unidade,
        obterUsuarioEmissor(request != null ? request.getUsuarioEmissor() : null),
        LocalDateTime.now());
    return HtmlPdfRenderer.render(html);
  }

  @Override
  public byte[] gerarContasBancarias(RelatorioContasBancariasRequest request) {
    LocalDate dataReferencia = request != null && request.getDataReferencia() != null
        ? request.getDataReferencia()
        : LocalDate.now();
    List<ContaBancaria> contas = contaRepository.listar();
    List<MovimentacaoFinanceira> movimentos = movimentacaoRepository.listar();
    Map<Long, List<MovimentacaoFinanceira>> movimentosPorConta = movimentos.stream()
        .filter(item -> item.getContaBancaria() != null && item.getContaBancaria().getId() != null)
        .collect(Collectors.groupingBy(item -> item.getContaBancaria().getId()));

    UnidadeAssistencialResponse unidade = unidadeService.obterAtual();
    String corpoHtml = montarContasBancariasHtml(contas, movimentosPorConta, dataReferencia);
    String html = RelatorioTemplatePadrao.buildHtml(
        "Contas bancárias",
        corpoHtml,
        unidade,
        obterUsuarioEmissor(request != null ? request.getUsuarioEmissor() : null),
        LocalDateTime.now());
    return HtmlPdfRenderer.render(html);
  }

  private String montarExtratoMensalHtml(YearMonth mesReferencia, List<MovimentacaoFinanceira> movimentos) {
    StringBuilder sb = new StringBuilder();
    sb.append("<section class=\"section\">");
    sb.append("<h2 class=\"section-title\">Extrato mensal</h2>");
    sb.append("<table class=\"print-table\">");
    sb.append("<tr><th>Mês de referência</th><td>")
        .append(escape(mesReferencia.format(MES_FORMATTER)))
        .append("</td></tr>");
    sb.append("<tr><th>Total de registros</th><td>")
        .append(movimentos.size())
        .append("</td></tr>");
    sb.append("</table>");
    sb.append("</section>");

    BigDecimal totalEntradas = BigDecimal.ZERO;
    BigDecimal totalSaidas = BigDecimal.ZERO;

    sb.append("<section class=\"section\">");
    sb.append("<h2 class=\"section-title\">Movimentações do período</h2>");
    sb.append("<table class=\"print-table\">");
    sb.append("<thead><tr>");
    sb.append("<th>Data</th>");
    sb.append("<th>Conta</th>");
    sb.append("<th>Descricao</th>");
    sb.append("<th>Categoria</th>");
    sb.append("<th>Tipo</th>");
    sb.append("<th>Valor</th>");
    sb.append("</tr></thead>");
    sb.append("<tbody>");
    if (movimentos.isEmpty()) {
      sb.append("<tr><td colspan=\"6\">Nenhuma movimentação registrada no período.</td></tr>");
    } else {
      for (MovimentacaoFinanceira item : movimentos) {
        BigDecimal valor = normalizarValor(item.getValor());
        if ("ENTRADA".equalsIgnoreCase(item.getTipo())) {
          totalEntradas = totalEntradas.add(valor);
        } else {
          totalSaidas = totalSaidas.add(valor);
        }
        sb.append("<tr>");
        sb.append("<td>").append(escape(formatarData(item.getDataMovimentacao()))).append("</td>");
        sb.append("<td>").append(escape(obterDescricaoConta(item))).append("</td>");
        sb.append("<td>").append(escape(textoSeguro(item.getDescricao()))).append("</td>");
        sb.append("<td>").append(escape(textoSeguro(item.getCategoria()))).append("</td>");
        sb.append("<td>").append(escape(obterTipoMovimentacao(item.getTipo()))).append("</td>");
        sb.append("<td>").append(escape(formatarMoeda(valor))).append("</td>");
        sb.append("</tr>");
      }
    }
    sb.append("</tbody>");
    sb.append("</table>");
    sb.append("</section>");

    sb.append("<section class=\"section\">");
    sb.append("<h2 class=\"section-title\">Totais do período</h2>");
    sb.append("<table class=\"print-table\">");
    sb.append("<tr><th>Total de entradas</th><td>").append(escape(formatarMoeda(totalEntradas))).append("</td></tr>");
    sb.append("<tr><th>Total de saídas</th><td>").append(escape(formatarMoeda(totalSaidas))).append("</td></tr>");
    sb.append("<tr><th>Saldo do período</th><td>")
        .append(escape(formatarMoeda(totalEntradas.subtract(totalSaidas)))).append("</td></tr>");
    sb.append("</table>");
    sb.append("</section>");

    return sb.toString();
  }

  private String montarLancamentosHtml(String titulo, List<LancamentoFinanceiro> lancamentos) {
    StringBuilder sb = new StringBuilder();
    sb.append("<section class=\"section\">");
    sb.append("<h2 class=\"section-title\">").append(escape(titulo)).append("</h2>");
    sb.append("<table class=\"print-table\">");
    sb.append("<thead><tr>");
    sb.append("<th>Vencimento</th>");
    sb.append("<th>Descrição</th>");
    sb.append("<th>Contraparte</th>");
    sb.append("<th>Situação</th>");
    sb.append("<th>Valor</th>");
    sb.append("</tr></thead>");
    sb.append("<tbody>");
    if (lancamentos.isEmpty()) {
      sb.append("<tr><td colspan=\"5\">Nenhum registro encontrado.</td></tr>");
    } else {
      for (LancamentoFinanceiro item : lancamentos) {
        sb.append("<tr>");
        sb.append("<td>").append(escape(formatarData(item.getVencimento()))).append("</td>");
        sb.append("<td>").append(escape(textoSeguro(item.getDescricao()))).append("</td>");
        sb.append("<td>").append(escape(textoSeguro(item.getContraparte()))).append("</td>");
        sb.append("<td>").append(escape(textoSeguro(formatarSituacao(item.getSituacao())))).append("</td>");
        sb.append("<td>").append(escape(formatarMoeda(normalizarValor(item.getValor())))).append("</td>");
        sb.append("</tr>");
      }
    }
    sb.append("</tbody>");
    sb.append("</table>");
    sb.append("</section>");
    return sb.toString();
  }

  private String montarContasBancariasHtml(
      List<ContaBancaria> contas,
      Map<Long, List<MovimentacaoFinanceira>> movimentosPorConta,
      LocalDate dataReferencia) {
    StringBuilder sb = new StringBuilder();
    sb.append("<section class=\"section\">");
    sb.append("<h2 class=\"section-title\">Contas bancárias</h2>");
    sb.append("<table class=\"print-table\">");
    sb.append("<tr><th>Data de referência</th><td>")
        .append(escape(formatarData(dataReferencia)))
        .append("</td></tr>");
    sb.append("</table>");
    sb.append("</section>");

    sb.append("<section class=\"section\">");
    sb.append("<h2 class=\"section-title\">Saldos por conta</h2>");
    sb.append("<table class=\"print-table\">");
    sb.append("<thead><tr>");
    sb.append("<th>Banco</th>");
    sb.append("<th>Agencia</th>");
    sb.append("<th>Conta</th>");
    sb.append("<th>Tipo</th>");
    sb.append("<th>Saldo</th>");
    sb.append("</tr></thead>");
    sb.append("<tbody>");
    if (contas == null || contas.isEmpty()) {
      sb.append("<tr><td colspan=\"5\">Nenhuma conta cadastrada.</td></tr>");
    } else {
      for (ContaBancaria conta : contas) {
        BigDecimal saldo = calcularSaldoAteData(conta, movimentosPorConta.get(conta.getId()), dataReferencia);
        sb.append("<tr>");
        sb.append("<td>").append(escape(textoSeguro(conta.getBanco()))).append("</td>");
        sb.append("<td>").append(escape(textoSeguro(conta.getAgencia()))).append("</td>");
        sb.append("<td>").append(escape(textoSeguro(conta.getNumero()))).append("</td>");
        sb.append("<td>").append(escape(textoSeguro(conta.getTipo()))).append("</td>");
        sb.append("<td>").append(escape(formatarMoeda(saldo))).append("</td>");
        sb.append("</tr>");
      }
    }
    sb.append("</tbody>");
    sb.append("</table>");
    sb.append("</section>");
    return sb.toString();
  }

  private YearMonth obterMesReferencia(RelatorioExtratoMensalRequest request) {
    if (request == null || request.getMesReferencia() == null || request.getMesReferencia().trim().isEmpty()) {
      return YearMonth.now();
    }
    String valor = request.getMesReferencia().trim();
    try {
      return YearMonth.parse(valor, MES_REFERENCIA_FORMATTER);
    } catch (Exception ex) {
      return YearMonth.now();
    }
  }

  private BigDecimal calcularSaldoAteData(
      ContaBancaria conta,
      List<MovimentacaoFinanceira> movimentos,
      LocalDate dataReferencia) {
    BigDecimal saldo = normalizarValor(conta != null ? conta.getSaldo() : null);
    if (conta == null || dataReferencia == null) {
      return saldo;
    }
    LocalDate dataAtualizacao = conta.getDataAtualizacao();
    if (dataAtualizacao == null || !dataReferencia.isBefore(dataAtualizacao)) {
      return saldo;
    }
    if (movimentos == null || movimentos.isEmpty()) {
      return saldo;
    }
    for (MovimentacaoFinanceira movimento : movimentos) {
      if (movimento.getDataMovimentacao() == null) {
        continue;
      }
      if (movimento.getDataMovimentacao().isAfter(dataReferencia)) {
        BigDecimal valor = normalizarValor(movimento.getValor());
        if ("ENTRADA".equalsIgnoreCase(movimento.getTipo())) {
          saldo = saldo.subtract(valor);
        } else {
          saldo = saldo.add(valor);
        }
      }
    }
    return saldo;
  }

  private String obterDescricaoConta(MovimentacaoFinanceira item) {
    if (item == null || item.getContaBancaria() == null) {
      return "Não informado";
    }
    ContaBancaria conta = item.getContaBancaria();
    String banco = textoSeguro(conta.getBanco());
    String numero = textoSeguro(conta.getNumero());
    if (banco.isEmpty() && numero.isEmpty()) {
      return "Não informado";
    }
    if (!banco.isEmpty() && !numero.isEmpty()) {
      return banco + " - " + numero;
    }
    return !banco.isEmpty() ? banco : numero;
  }

  private String obterTipoMovimentacao(String tipo) {
    if (tipo == null) {
      return "Não informado";
    }
    return "ENTRADA".equalsIgnoreCase(tipo) ? "Entrada" : "Saída";
  }

  private String formatarSituacao(String situacao) {
    if (situacao == null || situacao.trim().isEmpty()) {
      return "Não informado";
    }
    String valor = situacao.trim().toLowerCase();
    if ("pago".equals(valor)) {
      return "Pago";
    }
    if ("atrasado".equals(valor)) {
      return "Em atraso";
    }
    return "Aberto";
  }

  private String formatarData(LocalDate data) {
    if (data == null) {
      return "Não informado";
    }
    return DATA_FORMATTER.format(data);
  }

  private String obterUsuarioEmissor(String usuario) {
    if (usuario == null || usuario.trim().isEmpty()) {
      return "Sistema";
    }
    return usuario.trim();
  }

  private BigDecimal normalizarValor(BigDecimal valor) {
    return valor == null ? BigDecimal.ZERO : valor;
  }

  private String formatarMoeda(BigDecimal valor) {
    BigDecimal seguro = valor == null ? BigDecimal.ZERO : valor;
    return "R$ " + String.format(new Locale("pt", "BR"), "%,.2f", seguro);
  }

  private String textoSeguro(String valor) {
    if (valor == null) {
      return "";
    }
    return valor.trim();
  }

  private String escape(String valor) {
    if (valor == null) {
      return "";
    }
    return valor.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
  }
}
