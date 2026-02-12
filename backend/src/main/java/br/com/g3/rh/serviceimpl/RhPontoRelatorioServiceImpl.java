package br.com.g3.rh.serviceimpl;

import br.com.g3.relatorios.util.HtmlPdfRenderer;
import br.com.g3.relatorios.util.RelatorioTemplatePadrao;
import br.com.g3.rh.dto.RhConfiguracaoPontoResponse;
import br.com.g3.rh.dto.RhPontoDiaResumoResponse;
import br.com.g3.rh.dto.RhPontoEspelhoResponse;
import br.com.g3.rh.service.RhPontoRelatorioService;
import br.com.g3.rh.service.RhPontoService;
import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialResponse;
import br.com.g3.unidadeassistencial.service.UnidadeAssistencialService;
import br.com.g3.usuario.domain.Usuario;
import br.com.g3.usuario.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RhPontoRelatorioServiceImpl implements RhPontoRelatorioService {
  private static final DateTimeFormatter DATA_FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  private static final DateTimeFormatter MES_ANO_FORMATO = DateTimeFormatter.ofPattern("MM/yyyy");

  private final RhPontoService pontoService;
  private final UnidadeAssistencialService unidadeAssistencialService;
  private final UsuarioRepository usuarioRepository;

  public RhPontoRelatorioServiceImpl(
      RhPontoService pontoService,
      UnidadeAssistencialService unidadeAssistencialService,
      UsuarioRepository usuarioRepository) {
    this.pontoService = pontoService;
    this.unidadeAssistencialService = unidadeAssistencialService;
    this.usuarioRepository = usuarioRepository;
  }

  @Override
  public byte[] gerarRelatorioEspelhoMensal(
      Integer mes, Integer ano, Long funcionarioId, Long usuarioId) {
    if (funcionarioId == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Funcionario nao informado.");
    }
    RhPontoEspelhoResponse espelho = pontoService.consultarEspelho(mes, ano, funcionarioId);
    Usuario funcionario = buscarUsuario(funcionarioId);
    Usuario emissor = usuarioId != null ? buscarUsuario(usuarioId) : null;
    UnidadeAssistencialResponse unidade = unidadeAssistencialService.obterAtual();

    String titulo = "Espelho mensal de ponto";
    String corpo = montarHtmlEspelho(espelho, funcionario);
    String html = RelatorioTemplatePadrao.buildHtml(
        titulo,
        corpo,
        unidade,
        emissor != null ? nomeUsuario(emissor) : "Sistema",
        LocalDateTime.now());
    return HtmlPdfRenderer.render(html);
  }

  @Override
  public byte[] gerarRelacaoColaboradores(Long usuarioId) {
    Usuario emissor = usuarioId != null ? buscarUsuario(usuarioId) : null;
    UnidadeAssistencialResponse unidade = unidadeAssistencialService.obterAtual();
    List<Usuario> usuarios = usuarioRepository.listar();

    String titulo = "Relacao de colaboradores";
    String corpo = montarHtmlColaboradores(usuarios);
    String html = RelatorioTemplatePadrao.buildHtml(
        titulo,
        corpo,
        unidade,
        emissor != null ? nomeUsuario(emissor) : "Sistema",
        LocalDateTime.now());
    return HtmlPdfRenderer.render(html);
  }

  @Override
  public byte[] gerarRelatorioConfiguracao(Long usuarioId) {
    Usuario emissor = usuarioId != null ? buscarUsuario(usuarioId) : null;
    UnidadeAssistencialResponse unidade = unidadeAssistencialService.obterAtual();
    RhConfiguracaoPontoResponse configuracao = pontoService.buscarConfiguracao();
    List<Usuario> usuarios = usuarioRepository.listar();

    String titulo = "Configuracao do ponto";
    String corpo = montarHtmlConfiguracao(configuracao, usuarios);
    String html = RelatorioTemplatePadrao.buildHtml(
        titulo,
        corpo,
        unidade,
        emissor != null ? nomeUsuario(emissor) : "Sistema",
        LocalDateTime.now());
    return HtmlPdfRenderer.render(html);
  }

  private String montarHtmlEspelho(RhPontoEspelhoResponse espelho, Usuario funcionario) {
    StringBuilder sb = new StringBuilder();
    sb.append("<div class=\"section\">");
    sb.append("<p><strong>Funcionario:</strong> ").append(escape(nomeUsuario(funcionario))).append("</p>");
    sb.append("<p><strong>Periodo:</strong> ");
    sb.append(String.format(Locale.forLanguageTag("pt-BR"), "%02d/%d", espelho.getMes(), espelho.getAno()));
    sb.append("</p>");
    sb.append("</div>");

    sb.append("<table class=\"print-table\">");
    sb.append("<thead><tr>");
    sb.append("<th>Data</th>");
    sb.append("<th>Ocorr.</th>");
    sb.append("<th>Entrada</th>");
    sb.append("<th>Saida</th>");
    sb.append("<th>Entrada</th>");
    sb.append("<th>Saida</th>");
    sb.append("<th>Horas</th>");
    sb.append("<th>Extras</th>");
    sb.append("<th>Banco</th>");
    sb.append("<th>Faltas/Atrasos</th>");
    sb.append("<th>Observacoes</th>");
    sb.append("</tr></thead><tbody>");

    for (RhPontoDiaResumoResponse dia : espelho.getDias()) {
      sb.append("<tr>");
      sb.append("<td>").append(dia.getData() != null ? dia.getData().format(DATA_FORMATO) : "").append("</td>");
      sb.append("<td>").append(escape(valorOuVazio(dia.getOcorrencia()))).append("</td>");
      sb.append("<td>").append(escape(valorOuVazio(dia.getEntradaManha()))).append("</td>");
      sb.append("<td>").append(escape(valorOuVazio(dia.getSaidaManha()))).append("</td>");
      sb.append("<td>").append(escape(valorOuVazio(dia.getEntradaTarde()))).append("</td>");
      sb.append("<td>").append(escape(valorOuVazio(dia.getSaidaTarde()))).append("</td>");
      sb.append("<td>").append(formatarMinutos(dia.getTotalTrabalhadoMinutos())).append("</td>");
      sb.append("<td>").append(formatarMinutos(dia.getExtrasMinutos())).append("</td>");
      sb.append("<td>").append(formatarMinutos(dia.getBancoHorasMinutos())).append("</td>");
      sb.append("<td>").append(formatarMinutos(dia.getFaltasAtrasosMinutos())).append("</td>");
      sb.append("<td>").append(escape(valorOuVazio(dia.getObservacoes()))).append("</td>");
      sb.append("</tr>");
    }
    sb.append("</tbody>");
    sb.append("<tfoot><tr>");
    sb.append("<td colspan=\"6\"><strong>Totais</strong></td>");
    sb.append("<td>").append(formatarMinutos(espelho.getTotalTrabalhadoMinutos())).append("</td>");
    sb.append("<td>").append(formatarMinutos(espelho.getTotalExtrasMinutos())).append("</td>");
    sb.append("<td>").append(formatarMinutos(espelho.getTotalBancoHorasMinutos())).append("</td>");
    sb.append("<td>").append(formatarMinutos(espelho.getTotalFaltasAtrasosMinutos())).append("</td>");
    sb.append("<td></td>");
    sb.append("</tr></tfoot>");
    sb.append("</table>");

    sb.append("<div class=\"signature\">");
    sb.append("<div class=\"signature__line\">Funcionario</div>");
    sb.append("<div class=\"signature__line\">Instituicao</div>");
    sb.append("</div>");
    return sb.toString();
  }

  private String montarHtmlColaboradores(List<Usuario> usuarios) {
    StringBuilder sb = new StringBuilder();
    sb.append("<table class=\"print-table\">");
    sb.append("<thead><tr>");
    sb.append("<th>Funcionario</th>");
    sb.append("<th>Email</th>");
    sb.append("<th>Entrada 1</th>");
    sb.append("<th>Saida 1</th>");
    sb.append("<th>Entrada 2</th>");
    sb.append("<th>Saida 2</th>");
    sb.append("</tr></thead><tbody>");
    for (Usuario usuario : usuarios) {
      sb.append("<tr>");
      sb.append("<td>").append(escape(nomeUsuario(usuario))).append("</td>");
      sb.append("<td>").append(escape(valorOuVazio(usuario.getEmail()))).append("</td>");
      sb.append("<td>").append(formatarHora(usuario.getHorarioEntrada1())).append("</td>");
      sb.append("<td>").append(formatarHora(usuario.getHorarioSaida1())).append("</td>");
      sb.append("<td>").append(formatarHora(usuario.getHorarioEntrada2())).append("</td>");
      sb.append("<td>").append(formatarHora(usuario.getHorarioSaida2())).append("</td>");
      sb.append("</tr>");
    }
    sb.append("</tbody></table>");
    return sb.toString();
  }

  private String montarHtmlConfiguracao(RhConfiguracaoPontoResponse configuracao, List<Usuario> usuarios) {
    StringBuilder sb = new StringBuilder();
    sb.append("<div class=\"section\">");
    sb.append("<p><strong>Carga semanal:</strong> ").append(formatarMinutos(configuracao.getCargaSemanalMinutos()))
        .append("</p>");
    sb.append("<p><strong>Segunda a quinta:</strong> ")
        .append(formatarMinutos(configuracao.getCargaSegQuiMinutos())).append("</p>");
    sb.append("<p><strong>Sexta:</strong> ").append(formatarMinutos(configuracao.getCargaSextaMinutos()))
        .append("</p>");
    sb.append("<p><strong>Sabado:</strong> ").append(formatarMinutos(configuracao.getCargaSabadoMinutos()))
        .append("</p>");
    sb.append("<p><strong>Domingo:</strong> ").append(formatarMinutos(configuracao.getCargaDomingoMinutos()))
        .append("</p>");
    sb.append("<p><strong>Tolerancia:</strong> ").append(formatarMinutos(configuracao.getToleranciaMinutos()))
        .append("</p>");
    sb.append("</div>");

    sb.append("<div class=\"section\">");
    sb.append("<p class=\"section-title\">Horarios de trabalho</p>");
    sb.append("<table class=\"print-table\">");
    sb.append("<thead><tr>");
    sb.append("<th>Funcionario</th>");
    sb.append("<th>Entrada 1</th>");
    sb.append("<th>Saida 1</th>");
    sb.append("<th>Entrada 2</th>");
    sb.append("<th>Saida 2</th>");
    sb.append("</tr></thead><tbody>");
    for (Usuario usuario : usuarios) {
      sb.append("<tr>");
      sb.append("<td>").append(escape(nomeUsuario(usuario))).append("</td>");
      sb.append("<td>").append(formatarHora(usuario.getHorarioEntrada1())).append("</td>");
      sb.append("<td>").append(formatarHora(usuario.getHorarioSaida1())).append("</td>");
      sb.append("<td>").append(formatarHora(usuario.getHorarioEntrada2())).append("</td>");
      sb.append("<td>").append(formatarHora(usuario.getHorarioSaida2())).append("</td>");
      sb.append("</tr>");
    }
    sb.append("</tbody></table>");
    sb.append("</div>");
    return sb.toString();
  }

  private String formatarHora(java.time.LocalTime horario) {
    if (horario == null) {
      return "--:--";
    }
    return horario.format(DateTimeFormatter.ofPattern("HH:mm"));
  }

  private String formatarMinutos(Integer minutos) {
    if (minutos == null) {
      return "00:00";
    }
    int total = Math.max(minutos, 0);
    int horas = total / 60;
    int resto = total % 60;
    return String.format("%02d:%02d", horas, resto);
  }

  private Usuario buscarUsuario(Long id) {
    return usuarioRepository.buscarPorId(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado."));
  }

  private String nomeUsuario(Usuario usuario) {
    if (usuario == null) {
      return "Nao informado";
    }
    String nome = usuario.getNome();
    if (nome == null || nome.isBlank()) {
      return usuario.getNomeUsuario();
    }
    return nome;
  }

  private String valorOuVazio(String valor) {
    return valor == null ? "" : valor;
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
        .replace("'", "&#39;");
  }
}
