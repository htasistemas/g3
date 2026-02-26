package br.com.g3.relatorios.serviceimpl;

import br.com.g3.doacaoplanejada.domain.DoacaoPlanejada;
import br.com.g3.doacaoplanejada.repository.DoacaoPlanejadaRepository;
import br.com.g3.relatorios.dto.DoacaoPlanejadaRelatorioRequest;
import br.com.g3.relatorios.service.RelatorioDoacoesPlanejadasService;
import br.com.g3.relatorios.util.HtmlPdfRenderer;
import br.com.g3.relatorios.util.RelatorioTemplatePadrao;
import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialResponse;
import br.com.g3.unidadeassistencial.service.UnidadeAssistencialService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class RelatorioDoacoesPlanejadasServiceImpl implements RelatorioDoacoesPlanejadasService {
  private static final DateTimeFormatter DATA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  private final DoacaoPlanejadaRepository doacaoPlanejadaRepository;
  private final UnidadeAssistencialService unidadeService;

  public RelatorioDoacoesPlanejadasServiceImpl(
      DoacaoPlanejadaRepository doacaoPlanejadaRepository,
      UnidadeAssistencialService unidadeService) {
    this.doacaoPlanejadaRepository = doacaoPlanejadaRepository;
    this.unidadeService = unidadeService;
  }

  @Override
  public byte[] gerarPdf(DoacaoPlanejadaRelatorioRequest request) {
    List<DoacaoPlanejada> pendentes = doacaoPlanejadaRepository.listarPendentes();
    UnidadeAssistencialResponse unidade = unidadeService.obterAtual();

    String corpo = montarCorpo(pendentes);
    String html =
        RelatorioTemplatePadrao.buildHtml(
            "Relatorio de Doacoes Pendentes",
            corpo,
            unidade,
            textoSeguro(request == null ? null : request.getUsuarioEmissor()),
            LocalDateTime.now());

    return HtmlPdfRenderer.render(html);
  }

  private String montarCorpo(List<DoacaoPlanejada> pendentes) {
    StringBuilder sb = new StringBuilder();
    sb.append("<section class=\"section\">");
    sb.append("<div class=\"section-title\">Resumo por item</div>");

    List<ResumoItem> resumo = montarResumo(pendentes);
    if (resumo.isEmpty()) {
      sb.append("<p>Nenhuma doacao pendente registrada.</p>");
    } else {
      sb.append("<table class=\"print-table\">");
      sb.append("<thead>");
      sb.append("<tr>");
      sb.append("<th>Item</th>");
      sb.append("<th>Codigo</th>");
      sb.append("<th>Unidade</th>");
      sb.append("<th>Quantidade total</th>");
      sb.append("</tr>");
      sb.append("</thead>");
      sb.append("<tbody>");
      for (ResumoItem item : resumo) {
        sb.append("<tr>")
            .append("<td>").append(escape(item.descricao)).append("</td>")
            .append("<td>").append(escape(item.codigo)).append("</td>")
            .append("<td>").append(escape(item.unidade)).append("</td>")
            .append("<td>").append(item.quantidadeTotal).append("</td>")
            .append("</tr>");
      }
      sb.append("</tbody>");
      sb.append("</table>");
    }
    sb.append("</section>");

    sb.append("<section class=\"section\">");
    sb.append("<div class=\"section-title\">Doacoes pendentes</div>");
    if (pendentes == null || pendentes.isEmpty()) {
      sb.append("<p>Nenhuma doacao pendente registrada.</p>");
      sb.append("</section>");
      return sb.toString();
    }

    List<DetalheDoacao> detalhes = montarDetalhes(pendentes);
    sb.append("<table class=\"print-table\">");
    sb.append("<thead>");
    sb.append("<tr>");
    sb.append("<th>Beneficiario</th>");
    sb.append("<th>Item</th>");
    sb.append("<th>Data prevista</th>");
    sb.append("<th>Quantidade</th>");
    sb.append("</tr>");
    sb.append("</thead>");
    sb.append("<tbody>");
    for (DetalheDoacao detalhe : detalhes) {
      sb.append("<tr>")
          .append("<td>").append(escape(detalhe.beneficiario)).append("</td>")
          .append("<td>").append(escape(detalhe.item)).append("</td>")
          .append("<td>").append(escape(formatarData(detalhe.dataPrevista))).append("</td>")
          .append("<td>").append(detalhe.quantidade).append("</td>")
          .append("</tr>");
    }
    sb.append("</tbody>");
    sb.append("</table>");
    sb.append("</section>");

    return sb.toString();
  }

  private List<ResumoItem> montarResumo(List<DoacaoPlanejada> pendentes) {
    Map<String, ResumoItem> agrupado = new LinkedHashMap<>();
    if (pendentes == null) {
      return new ArrayList<>();
    }

    for (DoacaoPlanejada doacao : pendentes) {
      if (doacao == null) {
        continue;
      }
      String descricao = valorOuNaoInformado(doacao.getItem() == null ? null : doacao.getItem().getDescricao());
      String codigo = valorOuNaoInformado(doacao.getItem() == null ? null : doacao.getItem().getCodigo());
      String unidade = valorOuNaoInformado(doacao.getItem() == null ? null : doacao.getItem().getUnidade());
      String chave = codigo + "|" + descricao + "|" + unidade;
      ResumoItem item = agrupado.get(chave);
      if (item == null) {
        item = new ResumoItem(codigo, descricao, unidade, 0);
        agrupado.put(chave, item);
      }
      item.quantidadeTotal += quantidadeSegura(doacao.getQuantidade());
    }

    List<ResumoItem> resultado = new ArrayList<>(agrupado.values());
    resultado.sort(Comparator.comparing((ResumoItem item) -> item.descricao.toLowerCase(Locale.ROOT)));
    return resultado;
  }

  private List<DetalheDoacao> montarDetalhes(List<DoacaoPlanejada> pendentes) {
    List<DetalheDoacao> detalhes = new ArrayList<>();
    for (DoacaoPlanejada doacao : pendentes) {
      if (doacao == null) {
        continue;
      }
      String beneficiario = montarNomeBeneficiario(doacao);
      String item = valorOuNaoInformado(doacao.getItem() == null ? null : doacao.getItem().getDescricao());
      detalhes.add(
          new DetalheDoacao(
              beneficiario,
              item,
              doacao.getDataPrevista(),
              quantidadeSegura(doacao.getQuantidade())));
    }
    detalhes.sort(
        Comparator.comparing((DetalheDoacao detalhe) -> detalhe.beneficiario.toLowerCase(Locale.ROOT))
            .thenComparing(detalhe -> detalhe.item.toLowerCase(Locale.ROOT))
            .thenComparing(detalhe -> detalhe.dataPrevista == null ? LocalDate.MIN : detalhe.dataPrevista));
    return detalhes;
  }

  private String montarNomeBeneficiario(DoacaoPlanejada doacao) {
    if (doacao == null) {
      return "Nao informado";
    }
    if (doacao.getBeneficiario() != null) {
      String nomeSocial = valorSeguro(doacao.getBeneficiario().getNomeSocial());
      if (!nomeSocial.isEmpty()) {
        return nomeSocial;
      }
      String nomeCompleto = valorSeguro(doacao.getBeneficiario().getNomeCompleto());
      if (!nomeCompleto.isEmpty()) {
        return nomeCompleto;
      }
    }
    if (doacao.getVinculoFamiliar() != null) {
      String nomeFamilia = valorSeguro(doacao.getVinculoFamiliar().getNomeFamilia());
      if (!nomeFamilia.isEmpty()) {
        return "Familia " + nomeFamilia;
      }
    }
    return "Nao informado";
  }

  private String formatarData(LocalDate data) {
    if (data == null) {
      return "Nao informado";
    }
    return DATA_FORMATTER.format(data);
  }

  private String textoSeguro(String valor) {
    return valor == null || valor.trim().isEmpty() ? "Sistema" : valor.trim();
  }

  private String valorSeguro(String valor) {
    return valor == null ? "" : valor.trim();
  }

  private String valorOuNaoInformado(String valor) {
    String texto = valorSeguro(valor);
    return texto.isEmpty() ? "Nao informado" : texto;
  }

  private int quantidadeSegura(Integer quantidade) {
    return quantidade == null ? 0 : quantidade;
  }

  private String escape(String valor) {
    if (valor == null) {
      return "";
    }
    return valor.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
  }

  private static class ResumoItem {
    private final String codigo;
    private final String descricao;
    private final String unidade;
    private int quantidadeTotal;

    private ResumoItem(String codigo, String descricao, String unidade, int quantidadeTotal) {
      this.codigo = codigo;
      this.descricao = descricao;
      this.unidade = unidade;
      this.quantidadeTotal = quantidadeTotal;
    }
  }

  private static class DetalheDoacao {
    private final String beneficiario;
    private final String item;
    private final LocalDate dataPrevista;
    private final int quantidade;

    private DetalheDoacao(String beneficiario, String item, LocalDate dataPrevista, int quantidade) {
      this.beneficiario = beneficiario;
      this.item = item;
      this.dataPrevista = dataPrevista;
      this.quantidade = quantidade;
    }
  }
}
