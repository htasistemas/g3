package br.com.g3.relatorios.serviceimpl;

import br.com.g3.autorizacaocompras.domain.AutorizacaoCompra;
import br.com.g3.autorizacaocompras.repository.AutorizacaoComprasRepository;
import br.com.g3.relatorios.dto.AutorizacaoCompraSolicitacaoRequest;
import br.com.g3.relatorios.service.RelatorioSolicitacaoComprasService;
import br.com.g3.relatorios.util.HtmlPdfRenderer;
import br.com.g3.relatorios.util.RelatorioTemplatePadrao;
import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialResponse;
import br.com.g3.unidadeassistencial.service.UnidadeAssistencialService;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.math.RoundingMode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RelatorioSolicitacaoComprasServiceImpl implements RelatorioSolicitacaoComprasService {
  private static final DateTimeFormatter DATA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  private static final NumberFormat MOEDA_FORMATTER =
      NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

  private final AutorizacaoComprasRepository autorizacaoRepository;
  private final UnidadeAssistencialService unidadeService;

  public RelatorioSolicitacaoComprasServiceImpl(
      AutorizacaoComprasRepository autorizacaoRepository, UnidadeAssistencialService unidadeService) {
    this.autorizacaoRepository = autorizacaoRepository;
    this.unidadeService = unidadeService;
  }

  @Override
  public byte[] gerarPdf(AutorizacaoCompraSolicitacaoRequest request) {
    if (request == null || request.getSolicitacaoId() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solicitacao nao informada.");
    }

    AutorizacaoCompra solicitacao =
        autorizacaoRepository
            .buscarPorId(request.getSolicitacaoId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitacao nao encontrada."));

    UnidadeAssistencialResponse unidade = unidadeService.obterAtual();
    String corpoHtml = montarCorpo(solicitacao);
    String html =
        RelatorioTemplatePadrao.buildHtml(
            "Solicitacao de Compras",
            corpoHtml,
            unidade,
            textoSeguro(request.getUsuarioEmissor()),
            LocalDateTime.now());

    return HtmlPdfRenderer.render(html);
  }

  private String montarCorpo(AutorizacaoCompra solicitacao) {
    StringBuilder sb = new StringBuilder();
    sb.append("<section class=\"section\">");
    sb.append("<div class=\"section-title\">Dados da Solicitacao</div>");
    sb.append("<p><strong>Nº da Solicitacao:</strong> ")
        .append(escape(valorOuNaoInformado(solicitacao.getId())))
        .append("</p>");
    sb.append("<p><strong>Data:</strong> ")
        .append(escape(formatarData(solicitacao.getCriadoEm() == null ? null : solicitacao.getCriadoEm().toLocalDate())))
        .append("</p>");
    sb.append("<p><strong>Titulo da compra:</strong> ")
        .append(escape(valorOuNaoInformado(solicitacao.getTitulo())))
        .append("</p>");
    sb.append("<p><strong>Tipo:</strong> ")
        .append(escape(formataTipoSolicitacao(solicitacao.getTipo())))
        .append("</p>");
    sb.append("<p><strong>Quantidade:</strong> ")
        .append(escape(valorOuNaoInformado(solicitacao.getQuantidadeItens())))
        .append("</p>");
    sb.append("<p><strong>Valor estimado:</strong> ")
        .append(escape(formatarMoeda(solicitacao.getValor())))
        .append("</p>");
    sb.append("<p><strong>Previsao / necessidade:</strong> ")
        .append(escape(formatarData(solicitacao.getDataPrevista())))
        .append("</p>");
    sb.append("<p><strong>Prioridade:</strong> ")
        .append(escape(valorOuNaoInformado(solicitacao.getPrioridade())))
        .append("</p>");
    sb.append("<p><strong>Setor:</strong> ")
        .append(escape(valorOuNaoInformado(solicitacao.getArea())))
        .append("</p>");
    sb.append("<p><strong>Solicitante:</strong> ")
        .append(escape(valorOuNaoInformado(solicitacao.getResponsavel())))
        .append("</p>");
    sb.append("</section>");

    sb.append("<section class=\"section\">");
    sb.append("<div class=\"section-title\">Itens Solicitados</div>");
    sb.append("<table class=\"print-table\">");
    sb.append("<thead>");
    sb.append("<tr>");
    sb.append("<th>Item</th>");
    sb.append("<th>Descricao</th>");
    sb.append("<th>Quantidade</th>");
    sb.append("<th>Unidade</th>");
    sb.append("<th>Valor Unitario (R$)</th>");
    sb.append("<th>Valor Total (R$)</th>");
    sb.append("</tr>");
    sb.append("</thead>");
    sb.append("<tbody>");
    sb.append(montarLinhaItem(1, solicitacao));
    sb.append(montarLinhaVazia(2));
    sb.append(montarLinhaVazia(3));
    sb.append("</tbody>");
    sb.append("</table>");
    sb.append("</section>");

    sb.append("<section class=\"section\">");
    sb.append("<div class=\"section-title\">Justificativa da Compra</div>");
    sb.append("<div class=\"box-text\">")
        .append(escape(valorOuNaoInformado(solicitacao.getJustificativa())))
        .append("</div>");
    sb.append("</section>");

    sb.append("<section class=\"section\">");
    sb.append("<div class=\"section-title\">Observacoes</div>");
    sb.append("<div class=\"box-text\">")
        .append(escape(valorOuNaoInformado(solicitacao.getObservacoesAprovacao())))
        .append("</div>");
    sb.append("</section>");

    sb.append("<div class=\"signature\">");
    sb.append("<div><div class=\"signature-line\">Assinatura do Solicitante</div></div>");
    sb.append("<div><div class=\"signature-line\">Assinatura do Responsavel / Aprovacao</div></div>");
    sb.append("</div>");
    return sb.toString();
  }

  private String montarLinhaItem(int indice, AutorizacaoCompra solicitacao) {
    int quantidade = solicitacao.getQuantidadeItens() == null ? 0 : solicitacao.getQuantidadeItens();
    BigDecimal valorTotal = solicitacao.getValor() == null ? BigDecimal.ZERO : solicitacao.getValor();
    BigDecimal valorUnitario =
        quantidade > 0
            ? valorTotal.divide(BigDecimal.valueOf(quantidade), 2, RoundingMode.HALF_UP)
            : valorTotal;

    return "<tr>"
        + "<td>" + indice + "</td>"
        + "<td>" + escape(valorOuNaoInformado(solicitacao.getTitulo())) + "</td>"
        + "<td>" + escape(String.valueOf(quantidade)) + "</td>"
        + "<td>un</td>"
        + "<td>" + escape(formatarMoeda(valorUnitario)) + "</td>"
        + "<td>" + escape(formatarMoeda(valorTotal)) + "</td>"
        + "</tr>";
  }

  private String montarLinhaVazia(int indice) {
    return "<tr>"
        + "<td>" + indice + "</td>"
        + "<td></td>"
        + "<td></td>"
        + "<td></td>"
        + "<td></td>"
        + "<td></td>"
        + "</tr>";
  }

  private String formatarData(LocalDate data) {
    if (data == null) {
      return "Nao informado";
    }
    return DATA_FORMATTER.format(data);
  }

  private String formatarMoeda(BigDecimal valor) {
    if (valor == null) {
      return "R$ 0,00";
    }
    return MOEDA_FORMATTER.format(valor);
  }

  private String formataTipoSolicitacao(String tipo) {
    if (tipo == null || tipo.trim().isEmpty()) {
      return "Nao informado";
    }
    String normalizado = tipo.trim().toLowerCase(Locale.ROOT);
    if ("produto".equals(normalizado)) {
      return "Compra de produtos";
    }
    if ("bem".equals(normalizado)) {
      return "Compra de bens";
    }
    if ("servico".equals(normalizado)) {
      return "Autorizacao de servicos";
    }
    if ("contrato".equals(normalizado)) {
      return "Contratos";
    }
    return tipo.trim();
  }

  private String valorOuNaoInformado(Object valor) {
    if (valor == null) {
      return "Nao informado";
    }
    String texto = String.valueOf(valor).trim();
    return texto.isEmpty() ? "Nao informado" : texto;
  }

  private String textoSeguro(String valor) {
    return valor == null || valor.trim().isEmpty() ? "Sistema" : valor.trim();
  }

  private String escape(String valor) {
    if (valor == null) {
      return "";
    }
    return valor.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
  }
}
