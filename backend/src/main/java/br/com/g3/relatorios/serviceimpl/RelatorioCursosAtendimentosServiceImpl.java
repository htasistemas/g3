package br.com.g3.relatorios.serviceimpl;

import br.com.g3.cursosatendimentos.domain.CursoAtendimento;
import br.com.g3.cursosatendimentos.repository.CursoAtendimentoRepository;
import br.com.g3.relatorios.dto.CursoAtendimentoRelacaoRequest;
import br.com.g3.relatorios.service.RelatorioCursosAtendimentosService;
import br.com.g3.relatorios.util.HtmlPdfRenderer;
import br.com.g3.relatorios.util.RelatorioTemplatePadrao;
import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialResponse;
import br.com.g3.unidadeassistencial.service.UnidadeAssistencialService;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RelatorioCursosAtendimentosServiceImpl implements RelatorioCursosAtendimentosService {
  private final CursoAtendimentoRepository repository;
  private final UnidadeAssistencialService unidadeService;

  public RelatorioCursosAtendimentosServiceImpl(
      CursoAtendimentoRepository repository, UnidadeAssistencialService unidadeService) {
    this.repository = repository;
    this.unidadeService = unidadeService;
  }

  @Override
  public byte[] gerarPdf(CursoAtendimentoRelacaoRequest request) {
    if (request == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Filtros do relatorio nao informados.");
    }

    List<CursoAtendimento> registros = repository.listar();
    registros = aplicarFiltros(registros, request);

    registros =
        registros.stream()
            .sorted(Comparator.comparing(CursoAtendimento::getNome, this::nullSafeString))
            .collect(Collectors.toList());

    UnidadeAssistencialResponse unidade = unidadeService.obterAtual();
    String corpoHtml = montarCorpo(registros);
    String html =
        RelatorioTemplatePadrao.buildHtml(
            "Relacao de Cursos e Atendimentos",
            corpoHtml,
            unidade,
            textoSeguro(request.getUsuarioEmissor()),
            LocalDateTime.now());

    return HtmlPdfRenderer.render(html);
  }

  private List<CursoAtendimento> aplicarFiltros(
      List<CursoAtendimento> registros, CursoAtendimentoRelacaoRequest request) {
    String nome = textoNormalizado(request.getNome());
    String tipo = textoNormalizado(request.getTipo());
    String status = textoNormalizado(request.getStatus());
    String profissional = textoNormalizado(request.getProfissional());
    Long salaId = request.getSalaId();

    return registros.stream()
        .filter((registro) -> filtrarPorTexto(registro.getNome(), nome))
        .filter((registro) -> filtrarPorTexto(registro.getTipo(), tipo))
        .filter((registro) -> filtrarPorTexto(registro.getStatus(), status))
        .filter((registro) -> filtrarPorTexto(registro.getProfissional(), profissional))
        .filter(
            (registro) ->
                salaId == null
                    || (registro.getSala() != null && salaId.equals(registro.getSala().getId())))
        .collect(Collectors.toList());
  }

  private String montarCorpo(List<CursoAtendimento> registros) {
    StringBuilder sb = new StringBuilder();
    sb.append("<section>");
    sb.append("<table class=\"print-table\">");
    sb.append("<thead>");
    sb.append("<tr>");
    sb.append("<th>Tipo</th>");
    sb.append("<th>Nome</th>");
    sb.append("<th>Sala</th>");
    sb.append("<th>Vagas</th>");
    sb.append("<th>Status</th>");
    sb.append("<th>Profissional</th>");
    sb.append("</tr>");
    sb.append("</thead>");
    sb.append("<tbody>");
    for (CursoAtendimento registro : registros) {
      sb.append("<tr>");
      sb.append("<td>").append(escape(valorOuNaoInformado(registro.getTipo()))).append("</td>");
      sb.append("<td>").append(escape(valorOuNaoInformado(registro.getNome()))).append("</td>");
      sb.append("<td>")
          .append(
              escape(
                  registro.getSala() != null ? valorOuNaoInformado(registro.getSala().getNome()) : "Nao informado"))
          .append("</td>");
      sb.append("<td>")
          .append(
              escape(
                  String.format(
                      Locale.ROOT,
                      "%d/%d",
                      registro.getVagasDisponiveis() == null ? 0 : registro.getVagasDisponiveis(),
                      registro.getVagasTotais() == null ? 0 : registro.getVagasTotais())))
          .append("</td>");
      sb.append("<td>").append(escape(valorOuNaoInformado(registro.getStatus()))).append("</td>");
      sb.append("<td>")
          .append(escape(valorOuNaoInformado(registro.getProfissional())))
          .append("</td>");
      sb.append("</tr>");
    }
    if (registros.isEmpty()) {
      sb.append("<tr><td colspan=\"6\">Nenhum registro encontrado.</td></tr>");
    }
    sb.append("</tbody>");
    sb.append("</table>");
    sb.append("</section>");
    return sb.toString();
  }

  private boolean filtrarPorTexto(String valor, String filtro) {
    if (filtro == null || filtro.isEmpty()) {
      return true;
    }
    return textoNormalizado(valor).contains(filtro);
  }

  private String textoNormalizado(String valor) {
    if (valor == null) {
      return "";
    }
    return valor.toLowerCase(Locale.ROOT).trim();
  }

  private String textoSeguro(String valor) {
    return valor == null || valor.trim().isEmpty() ? "Sistema" : valor.trim();
  }

  private int nullSafeString(String a, String b) {
    if (a == null && b == null) {
      return 0;
    }
    if (a == null) {
      return 1;
    }
    if (b == null) {
      return -1;
    }
    return a.compareToIgnoreCase(b);
  }

  private String valorOuNaoInformado(String valor) {
    if (valor == null || valor.trim().isEmpty()) {
      return "Nao informado";
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
