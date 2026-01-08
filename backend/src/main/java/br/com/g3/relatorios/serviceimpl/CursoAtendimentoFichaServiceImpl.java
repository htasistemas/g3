package br.com.g3.relatorios.serviceimpl;

import br.com.g3.cursosatendimentos.domain.CursoAtendimento;
import br.com.g3.cursosatendimentos.domain.CursoAtendimentoFilaEspera;
import br.com.g3.cursosatendimentos.domain.CursoAtendimentoMatricula;
import br.com.g3.cursosatendimentos.repository.CursoAtendimentoRepository;
import br.com.g3.relatorios.dto.CursoAtendimentoFichaRequest;
import br.com.g3.relatorios.service.CursoAtendimentoFichaService;
import br.com.g3.relatorios.util.HtmlPdfRenderer;
import br.com.g3.relatorios.util.RelatorioTemplatePadrao;
import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialResponse;
import br.com.g3.unidadeassistencial.service.UnidadeAssistencialService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CursoAtendimentoFichaServiceImpl implements CursoAtendimentoFichaService {
  private static final DateTimeFormatter DATA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  private static final DateTimeFormatter HORA_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

  private final CursoAtendimentoRepository repository;
  private final UnidadeAssistencialService unidadeService;

  public CursoAtendimentoFichaServiceImpl(
      CursoAtendimentoRepository repository, UnidadeAssistencialService unidadeService) {
    this.repository = repository;
    this.unidadeService = unidadeService;
  }

  @Override
  public byte[] gerarPdf(CursoAtendimentoFichaRequest request) {
    if (request == null || request.getCursoId() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Curso nao informado.");
    }

    CursoAtendimento curso =
        repository
            .buscarPorId(request.getCursoId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso nao encontrado."));

    UnidadeAssistencialResponse unidade = unidadeService.obterAtual();
    String corpoHtml = montarCorpo(curso);
    String html =
        RelatorioTemplatePadrao.buildHtml(
            "Ficha de Curso/Atendimento",
            corpoHtml,
            unidade,
            textoSeguro(request.getUsuarioEmissor()),
            LocalDateTime.now());

    return HtmlPdfRenderer.render(html);
  }

  private String montarCorpo(CursoAtendimento curso) {
    StringBuilder sb = new StringBuilder();
    sb.append("<section>");
    sb.append("<table class=\"print-table\">");
    sb.append(linha("Tipo", curso.getTipo()));
    sb.append(linha("Nome", curso.getNome()));
    sb.append(linha("Descricao", curso.getDescricao()));
    sb.append(linha("Status", curso.getStatus()));
    sb.append(linha("Profissional", curso.getProfissional()));
    sb.append(linha("Sala", curso.getSala() != null ? curso.getSala().getNome() : "Nao informado"));
    sb.append(linha("Vagas", vagasTexto(curso)));
    sb.append(linha("Carga horaria (h)", valorOuNaoInformado(curso.getCargaHoraria())));
    sb.append(linha("Horario inicial", formatarHora(curso.getHorarioInicial())));
    sb.append(linha("Duracao (h)", valorOuNaoInformado(curso.getDuracaoHoras())));
    sb.append(linha("Dias da semana", diasSemanaTexto(curso.getDiasSemana())));
    sb.append(linha("Restricoes", curso.getRestricoes()));
    sb.append(linha("Data triagem", formatarData(curso.getDataTriagem())));
    sb.append(linha("Data encaminhamento", formatarData(curso.getDataEncaminhamento())));
    sb.append(linha("Data conclusao", formatarData(curso.getDataConclusao())));
    sb.append("</table>");
    sb.append("</section>");

    sb.append("<section style=\"margin-top: 16px;\">");
    sb.append("<h4>Matriculas</h4>");
    sb.append(buildMatriculasTable(curso.getMatriculas()));
    sb.append("</section>");

    sb.append("<section style=\"margin-top: 16px;\">");
    sb.append("<h4>Fila de espera</h4>");
    sb.append(buildFilaEsperaTable(curso.getFilaEspera()));
    sb.append("</section>");

    return sb.toString();
  }

  private String buildMatriculasTable(List<CursoAtendimentoMatricula> matriculas) {
    StringBuilder sb = new StringBuilder();
    sb.append("<table class=\"print-table\">");
    sb.append("<thead><tr><th>Beneficiario</th><th>CPF</th><th>Status</th><th>Data</th></tr></thead>");
    sb.append("<tbody>");
    for (CursoAtendimentoMatricula matricula : matriculas) {
      sb.append("<tr>");
      sb.append("<td>").append(escape(valorOuNaoInformado(matricula.getBeneficiarioNome()))).append("</td>");
      sb.append("<td>").append(escape(valorOuNaoInformado(matricula.getCpf()))).append("</td>");
      sb.append("<td>").append(escape(valorOuNaoInformado(matricula.getStatus()))).append("</td>");
      sb.append("<td>")
          .append(escape(formatarDataHora(matricula.getDataMatricula())))
          .append("</td>");
      sb.append("</tr>");
    }
    if (matriculas.isEmpty()) {
      sb.append("<tr><td colspan=\"4\">Nenhuma matricula registrada.</td></tr>");
    }
    sb.append("</tbody>");
    sb.append("</table>");
    return sb.toString();
  }

  private String buildFilaEsperaTable(List<CursoAtendimentoFilaEspera> fila) {
    StringBuilder sb = new StringBuilder();
    sb.append("<table class=\"print-table\">");
    sb.append("<thead><tr><th>Beneficiario</th><th>CPF</th><th>Data entrada</th></tr></thead>");
    sb.append("<tbody>");
    for (CursoAtendimentoFilaEspera entry : fila) {
      sb.append("<tr>");
      sb.append("<td>").append(escape(valorOuNaoInformado(entry.getBeneficiarioNome()))).append("</td>");
      sb.append("<td>").append(escape(valorOuNaoInformado(entry.getCpf()))).append("</td>");
      sb.append("<td>").append(escape(formatarDataHora(entry.getDataEntrada()))).append("</td>");
      sb.append("</tr>");
    }
    if (fila.isEmpty()) {
      sb.append("<tr><td colspan=\"3\">Nenhum registro na fila de espera.</td></tr>");
    }
    sb.append("</tbody>");
    sb.append("</table>");
    return sb.toString();
  }

  private String linha(String label, String valor) {
    return "<tr><th>" + escape(label) + "</th><td>" + escape(valorOuNaoInformado(valor)) + "</td></tr>";
  }

  private String vagasTexto(CursoAtendimento curso) {
    int disponiveis = curso.getVagasDisponiveis() == null ? 0 : curso.getVagasDisponiveis();
    int totais = curso.getVagasTotais() == null ? 0 : curso.getVagasTotais();
    return String.format(Locale.ROOT, "%d/%d", disponiveis, totais);
  }

  private String diasSemanaTexto(String diasSemana) {
    if (diasSemana == null || diasSemana.trim().isEmpty()) {
      return "Nao informado";
    }
    return diasSemana;
  }

  private String formatarData(LocalDate data) {
    if (data == null) {
      return "Nao informado";
    }
    return DATA_FORMATTER.format(data);
  }

  private String formatarHora(LocalTime hora) {
    if (hora == null) {
      return "Nao informado";
    }
    return HORA_FORMATTER.format(hora);
  }

  private String formatarDataHora(LocalDateTime dataHora) {
    if (dataHora == null) {
      return "Nao informado";
    }
    return DATA_FORMATTER.format(dataHora.toLocalDate()) + " " + HORA_FORMATTER.format(dataHora.toLocalTime());
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
