package br.com.g3.relatorios.serviceimpl;

import br.com.g3.informacoesadministrativas.domain.InformacaoAdministrativa;
import br.com.g3.informacoesadministrativas.repository.InformacaoAdministrativaRepository;
import br.com.g3.relatorios.dto.InformacaoAdministrativaRelatorioRequest;
import br.com.g3.relatorios.service.RelatorioInformacoesAdministrativasService;
import br.com.g3.relatorios.util.HtmlPdfRenderer;
import br.com.g3.relatorios.util.RelatorioTemplatePadrao;
import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialResponse;
import br.com.g3.unidadeassistencial.service.UnidadeAssistencialService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RelatorioInformacoesAdministrativasServiceImpl
    implements RelatorioInformacoesAdministrativasService {
  private final InformacaoAdministrativaRepository repository;
  private final UnidadeAssistencialService unidadeService;

  public RelatorioInformacoesAdministrativasServiceImpl(
      InformacaoAdministrativaRepository repository,
      UnidadeAssistencialService unidadeService) {
    this.repository = repository;
    this.unidadeService = unidadeService;
  }

  @Override
  public byte[] gerarPdf(InformacaoAdministrativaRelatorioRequest request) {
    UnidadeAssistencialResponse unidade = unidadeService.obterAtual();
    List<InformacaoAdministrativa> registros = filtrar(repository.listar(), request);
    String corpoHtml = montarCorpoHtml(registros);
    String usuario = request != null ? request.getUsuarioEmissor() : "Sistema";
    String html = RelatorioTemplatePadrao.buildHtml(
        "Relatorio de Informacoes Administrativas",
        corpoHtml,
        unidade,
        usuario,
        LocalDateTime.now());
    return HtmlPdfRenderer.render(html);
  }

  private List<InformacaoAdministrativa> filtrar(
      List<InformacaoAdministrativa> registros,
      InformacaoAdministrativaRelatorioRequest request) {
    if (request == null) {
      return registros;
    }
    String tipo = normalizar(request.getTipo());
    String categoria = normalizar(request.getCategoria());
    String titulo = normalizar(request.getTitulo());
    String tags = normalizar(request.getTags());
    Boolean status = request.getStatus();
    return registros.stream()
        .filter(info -> tipo.isEmpty() || normalizar(info.getTipo()).contains(tipo))
        .filter(info -> categoria.isEmpty() || normalizar(info.getCategoria()).contains(categoria))
        .filter(info -> titulo.isEmpty() || normalizar(info.getTitulo()).contains(titulo))
        .filter(info -> tags.isEmpty() || normalizar(info.getTags()).contains(tags))
        .filter(info -> status == null || Objects.equals(info.getStatus(), status))
        .collect(Collectors.toList());
  }

  private String montarCorpoHtml(List<InformacaoAdministrativa> registros) {
    StringBuilder sb = new StringBuilder();
    sb.append("<section class=\"section\">");
    sb.append("<h2 class=\"section-title\">Informacoes administrativas</h2>");
    if (registros == null || registros.isEmpty()) {
      sb.append("<p>Nenhum registro encontrado.</p>");
      sb.append("</section>");
      return sb.toString();
    }
    sb.append("<table class=\"print-table\">");
    sb.append("<thead><tr>");
    sb.append("<th>Tipo</th>");
    sb.append("<th>Categoria</th>");
    sb.append("<th>Titulo</th>");
    sb.append("<th>Responsavel</th>");
    sb.append("<th>Host/URL</th>");
    sb.append("<th>Login</th>");
    sb.append("<th>Status</th>");
    sb.append("</tr></thead>");
    sb.append("<tbody>");
    for (InformacaoAdministrativa info : registros) {
      sb.append("<tr>");
      sb.append("<td>").append(escape(textoSeguro(info.getTipo()))).append("</td>");
      sb.append("<td>").append(escape(textoSeguro(info.getCategoria()))).append("</td>");
      sb.append("<td>").append(escape(textoSeguro(info.getTitulo()))).append("</td>");
      sb.append("<td>").append(escape(textoSeguro(info.getResponsavel()))).append("</td>");
      sb.append("<td>").append(escape(textoSeguro(info.getHostUrl()))).append("</td>");
      sb.append("<td>").append(escape(textoSeguro(info.getLogin()))).append("</td>");
      sb.append("<td>").append(info.getStatus() != null && info.getStatus() ? "Ativo" : "Inativo")
          .append("</td>");
      sb.append("</tr>");
    }
    sb.append("</tbody></table></section>");
    return sb.toString();
  }

  private String normalizar(String valor) {
    if (valor == null) {
      return "";
    }
    return valor.trim().toLowerCase(Locale.forLanguageTag("pt-BR"));
  }

  private String textoSeguro(String valor) {
    return valor == null ? "" : valor.trim();
  }

  private String escape(String valor) {
    if (valor == null) {
      return "";
    }
    return valor
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&#x27;");
  }
}
