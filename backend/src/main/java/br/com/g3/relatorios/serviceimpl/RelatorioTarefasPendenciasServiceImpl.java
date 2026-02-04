package br.com.g3.relatorios.serviceimpl;

import br.com.g3.relatorios.service.RelatorioTarefasPendenciasService;
import br.com.g3.relatorios.util.HtmlPdfRenderer;
import br.com.g3.relatorios.util.RelatorioTemplatePadrao;
import br.com.g3.tarefaspendencias.domain.TarefaPendencia;
import br.com.g3.tarefaspendencias.repository.TarefaPendenciaRepository;
import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialResponse;
import br.com.g3.unidadeassistencial.service.UnidadeAssistencialService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RelatorioTarefasPendenciasServiceImpl implements RelatorioTarefasPendenciasService {
  private static final DateTimeFormatter DATA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  private static final DateTimeFormatter DATA_HORA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
  private static final List<String> STATUS_ORDER =
      Arrays.asList("Aberta", "Em andamento", "Em atraso", "Concluída");

  private final TarefaPendenciaRepository repository;
  private final UnidadeAssistencialService unidadeService;

  public RelatorioTarefasPendenciasServiceImpl(
      TarefaPendenciaRepository repository, UnidadeAssistencialService unidadeService) {
    this.repository = repository;
    this.unidadeService = unidadeService;
  }

  @Override
  public byte[] gerarPdf() {
    List<TarefaPendencia> tarefas = repository.listar();
    UnidadeAssistencialResponse unidade = unidadeService.obterAtual();
    String corpoHtml = montarCorpoHtml(unidade, tarefas);
    String html =
        RelatorioTemplatePadrao.buildHtml(
            "Relatório de Tarefas e Pendências", corpoHtml, unidade, "Sistema", LocalDateTime.now());
    return HtmlPdfRenderer.render(html);
  }

  private String montarCorpoHtml(UnidadeAssistencialResponse unidade, List<TarefaPendencia> tarefas) {
    StringBuilder sb = new StringBuilder();
    sb.append("<section class=\"section\">");
    sb.append("<h2 class=\"section-title\">Dados do relatório</h2>");
    sb.append("<table class=\"print-table\">");
    sb.append("<tr><th>Nome do relatório</th><td>Relatório de Tarefas e Pendências</td></tr>");
    sb.append("<tr><th>Razão social</th><td>")
        .append(escape(textoSeguro(unidade != null ? unidade.getRazaoSocial() : null)))
        .append("</td></tr>");
    if (unidade != null && temValor(unidade.getNomeFantasia())) {
      sb.append("<tr><th>Nome fantasia</th><td>")
          .append(escape(unidade.getNomeFantasia().trim()))
          .append("</td></tr>");
    }
    sb.append("<tr><th>Logomarca</th><td>")
        .append(escape(descricaoLogomarca(unidade)))
        .append("</td></tr>");
    sb.append("<tr><th>Data de geração</th><td>")
        .append(escape(DATA_HORA_FORMATTER.format(LocalDateTime.now())))
        .append("</td></tr>");
    sb.append("</table>");
    sb.append("</section>");

    if (tarefas == null || tarefas.isEmpty()) {
      sb.append("<section class=\"section\"><p>Nenhuma pendência registrada.</p></section>");
      return sb.toString();
    }

    List<TarefaPendencia> restantes = new ArrayList<>(tarefas);
    for (String status : STATUS_ORDER) {
      List<TarefaPendencia> bloco = extrairPorStatus(restantes, status);
      if (bloco.isEmpty()) {
        continue;
      }
      adicionarSecaoStatus(sb, status, bloco);
    }
    if (!restantes.isEmpty()) {
      adicionarSecaoStatus(sb, "Outras etapas", restantes);
    }

    return sb.toString();
  }

  private void adicionarSecaoStatus(StringBuilder sb, String titulo, List<TarefaPendencia> tarefas) {
    sb.append("<section class=\"section\">");
    sb.append("<h2 class=\"section-title\">")
        .append(escape(titulo))
        .append(" (")
        .append(tarefas.size())
        .append(")</h2>");
    sb.append("<table class=\"print-table\">");
    sb.append("<thead><tr>");
    sb.append("<th>Título</th>");
    sb.append("<th>Responsável</th>");
    sb.append("<th>Prazo</th>");
    sb.append("<th>Status</th>");
    sb.append("</tr></thead>");
    sb.append("<tbody>");
    for (TarefaPendencia tarefa : tarefas) {
      sb.append("<tr>");
      sb.append("<td>").append(escape(textoSeguro(tarefa.getTitulo()))).append("</td>");
      sb.append("<td>").append(escape(textoSeguro(tarefa.getResponsavel()))).append("</td>");
      sb.append("<td>").append(escape(formatarPrazo(tarefa.getPrazo()))).append("</td>");
      sb.append("<td>").append(escape(textoSeguro(tarefa.getStatus()))).append("</td>");
      sb.append("</tr>");
      sb.append("<tr>");
      sb.append("<td colspan=\"4\" style=\"font-size: 11px; color: #374151;\">Descrição: ");
      sb.append(escape(textoSeguro(tarefa.getDescricao())));
      sb.append("</td>");
      sb.append("</tr>");
    }
    if (tarefas.isEmpty()) {
      sb.append("<tr><td colspan=\"4\">Nenhuma pendência registrada.</td></tr>");
    }
    sb.append("</tbody>");
    sb.append("</table>");
    sb.append("</section>");
  }

  private String formatarPrazo(LocalDate prazo) {
    if (prazo == null) {
      return "Sem prazo";
    }
    return DATA_FORMATTER.format(prazo);
  }

  private List<TarefaPendencia> extrairPorStatus(List<TarefaPendencia> source, String status) {
    List<TarefaPendencia> resultado =
        source.stream().filter(tarefa -> status.equals(tarefa.getStatus())).collect(Collectors.toList());
    source.removeAll(resultado);
    return resultado;
  }

  private String descricaoLogomarca(UnidadeAssistencialResponse unidade) {
    if (unidade == null) {
      return "Não informada";
    }
    if (temValor(unidade.getLogomarcaRelatorio())) {
      return "Disponível para relatórios";
    }
    if (temValor(unidade.getLogomarca())) {
      return "Disponível";
    }
    return "Não informada";
  }

  private String textoSeguro(String valor) {
    if (valor == null) {
      return "Não informado";
    }
    String limpo = valor.trim();
    return limpo.isEmpty() ? "Não informado" : limpo;
  }

  private boolean temValor(String valor) {
    return valor != null && !valor.trim().isEmpty();
  }

  private String escape(String valor) {
    if (valor == null) {
      return "";
    }
    return valor.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
  }
}
