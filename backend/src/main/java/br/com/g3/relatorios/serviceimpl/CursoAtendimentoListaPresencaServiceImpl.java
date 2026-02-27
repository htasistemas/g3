package br.com.g3.relatorios.serviceimpl;

import br.com.g3.cursosatendimentos.domain.CursoAtendimento;
import br.com.g3.cursosatendimentos.domain.CursoAtendimentoMatricula;
import br.com.g3.cursosatendimentos.repository.CursoAtendimentoRepository;
import br.com.g3.relatorios.dto.CursoAtendimentoListaPresencaRequest;
import br.com.g3.relatorios.service.CursoAtendimentoListaPresencaService;
import br.com.g3.relatorios.util.HtmlPdfRenderer;
import br.com.g3.relatorios.util.RelatorioTemplatePadrao;
import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialResponse;
import br.com.g3.unidadeassistencial.service.UnidadeAssistencialService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CursoAtendimentoListaPresencaServiceImpl implements CursoAtendimentoListaPresencaService {
  private static final DateTimeFormatter DATA_FORMATTER =
      DateTimeFormatter.ofPattern("dd/MM/yyyy");
  private final CursoAtendimentoRepository repository;
  private final UnidadeAssistencialService unidadeService;

  public CursoAtendimentoListaPresencaServiceImpl(
      CursoAtendimentoRepository repository, UnidadeAssistencialService unidadeService) {
    this.repository = repository;
    this.unidadeService = unidadeService;
  }

  @Override
  public byte[] gerarPdf(CursoAtendimentoListaPresencaRequest request) {
    if (request == null || request.getCursoId() == null || request.getDataAula() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do relatorio nao informados.");
    }

    CursoAtendimento curso =
        repository
            .buscarPorId(request.getCursoId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    List<CursoAtendimentoMatricula> matriculas =
        curso.getMatriculas() == null
            ? List.of()
            : curso.getMatriculas().stream()
                .filter((matricula) -> matricula != null)
                .filter((matricula) -> "Ativo".equalsIgnoreCase(matricula.getStatus()))
                .sorted(
                    Comparator.comparing(
                        (CursoAtendimentoMatricula m) -> textoNormalizado(m.getBeneficiarioNome())))
                .collect(Collectors.toList());

    UnidadeAssistencialResponse unidade = unidadeService.obterAtual();
    String corpoHtml = montarCorpo(request, curso, matriculas);
    String html =
        RelatorioTemplatePadrao.buildHtml(
            "Lista de Presenca",
            corpoHtml,
            unidade,
            textoSeguro(request.getUsuarioEmissor()),
            LocalDateTime.now());

    return HtmlPdfRenderer.render(html);
  }

  private String montarCorpo(
      CursoAtendimentoListaPresencaRequest request,
      CursoAtendimento curso,
      List<CursoAtendimentoMatricula> matriculas) {
    boolean exibirCpf = request.getExibirCpf() == null || request.getExibirCpf();
    StringBuilder sb = new StringBuilder();
    sb.append("<section>");
    sb.append("<p><strong>Curso/Atendimento:</strong> ")
        .append(escape(valorOuNaoInformado(curso.getNome())))
        .append("</p>");
    sb.append("<p><strong>Data da aula:</strong> ")
        .append(escape(DATA_FORMATTER.format(request.getDataAula())))
        .append("</p>");
    sb.append("<table class=\"print-table\">");
    sb.append("<thead><tr>");
    sb.append("<th>#</th>");
    sb.append("<th>Nome</th>");
    if (exibirCpf) {
      sb.append("<th>CPF</th>");
    }
    sb.append("<th>Presente</th>");
    sb.append("<th>Ausente</th>");
    sb.append("<th>Assinatura</th>");
    sb.append("</tr></thead>");
    sb.append("<tbody>");
    int idx = 1;
    for (CursoAtendimentoMatricula matricula : matriculas) {
      sb.append("<tr>");
      sb.append("<td>").append(idx++).append("</td>");
      sb.append("<td>").append(escape(valorOuNaoInformado(matricula.getBeneficiarioNome()))).append("</td>");
      if (exibirCpf) {
        sb.append("<td>").append(escape(valorOuNaoInformado(matricula.getCpf()))).append("</td>");
      }
      sb.append("<td>[ ]</td>");
      sb.append("<td>[ ]</td>");
      sb.append("<td></td>");
      sb.append("</tr>");
    }
    if (matriculas.isEmpty()) {
      int colunas = exibirCpf ? 6 : 5;
      sb.append("<tr><td colspan=\"").append(colunas).append("\">Nenhuma matricula ativa.</td></tr>");
    }
    sb.append("</tbody>");
    sb.append("</table>");
    sb.append("</section>");
    return sb.toString();
  }

  private String valorOuNaoInformado(String valor) {
    if (valor == null || valor.trim().isEmpty()) {
      return "Nao informado";
    }
    return valor.trim();
  }

  private String textoSeguro(String valor) {
    return valor == null || valor.trim().isEmpty() ? "Sistema" : valor.trim();
  }

  private String textoNormalizado(String valor) {
    if (valor == null) {
      return "";
    }
    return valor.toLowerCase(Locale.ROOT).trim();
  }

  private String escape(String valor) {
    if (valor == null) {
      return "";
    }
    return valor.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
  }
}
