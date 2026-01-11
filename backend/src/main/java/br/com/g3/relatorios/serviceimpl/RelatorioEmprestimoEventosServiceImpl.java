package br.com.g3.relatorios.serviceimpl;

import br.com.g3.emprestimoseventos.domain.EmprestimoEvento;
import br.com.g3.emprestimoseventos.domain.EmprestimoEventoItem;
import br.com.g3.emprestimoseventos.repository.EmprestimoEventoItemRepository;
import br.com.g3.emprestimoseventos.repository.EmprestimoEventoRepository;
import br.com.g3.relatorios.dto.EmprestimoEventoRelatorioRequest;
import br.com.g3.relatorios.service.RelatorioEmprestimoEventosService;
import br.com.g3.relatorios.util.HtmlPdfRenderer;
import br.com.g3.relatorios.util.RelatorioTemplatePadrao;
import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialResponse;
import br.com.g3.unidadeassistencial.service.UnidadeAssistencialService;
import br.com.g3.usuario.domain.Usuario;
import br.com.g3.usuario.repository.UsuarioRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RelatorioEmprestimoEventosServiceImpl implements RelatorioEmprestimoEventosService {
  private static final DateTimeFormatter DATA_HORA_FORMATTER =
      DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.forLanguageTag("pt-BR"));

  private final EmprestimoEventoRepository emprestimoRepository;
  private final EmprestimoEventoItemRepository itemRepository;
  private final UnidadeAssistencialService unidadeService;
  private final UsuarioRepository usuarioRepository;
  private final JdbcTemplate jdbcTemplate;

  public RelatorioEmprestimoEventosServiceImpl(
      EmprestimoEventoRepository emprestimoRepository,
      EmprestimoEventoItemRepository itemRepository,
      UnidadeAssistencialService unidadeService,
      UsuarioRepository usuarioRepository,
      JdbcTemplate jdbcTemplate) {
    this.emprestimoRepository = emprestimoRepository;
    this.itemRepository = itemRepository;
    this.unidadeService = unidadeService;
    this.usuarioRepository = usuarioRepository;
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public byte[] gerarPdf(EmprestimoEventoRelatorioRequest requisicao) {
    if (requisicao == null || requisicao.getEmprestimoId() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Emprestimo nao informado.");
    }

    EmprestimoEvento emprestimo =
        emprestimoRepository
            .buscarPorId(requisicao.getEmprestimoId())
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Emprestimo nao encontrado."));

    List<EmprestimoEventoItem> itens = itemRepository.listarPorEmprestimoId(emprestimo.getId());
    UnidadeAssistencialResponse unidade = unidadeService.obterAtual();

    String corpo = montarCorpo(emprestimo, itens);
    String html =
        RelatorioTemplatePadrao.buildHtml(
            "Emprestimo para Eventos",
            corpo,
            unidade,
            textoSeguro(requisicao.getUsuarioEmissor()),
            LocalDateTime.now());

    return HtmlPdfRenderer.render(html);
  }

  private String montarCorpo(EmprestimoEvento emprestimo, List<EmprestimoEventoItem> itens) {
    StringBuilder conteudo = new StringBuilder();
    conteudo.append("<section class=\"section\">");
    conteudo.append("<div class=\"section-title\">Evento</div>");
    conteudo.append("<p><strong>Titulo:</strong> ").append(escape(emprestimo.getEvento().getTitulo())).append("</p>");
    conteudo.append("<p><strong>Local:</strong> ").append(escape(valorOuNaoInformado(emprestimo.getEvento().getLocal()))).append("</p>");
    conteudo.append("<p><strong>Periodo do evento:</strong> ")
        .append(formatarDataHora(emprestimo.getEvento().getDataInicio()))
        .append(" ate ")
        .append(formatarDataHora(emprestimo.getEvento().getDataFim()))
        .append("</p>");
    conteudo.append("</section>");

    conteudo.append("<section class=\"section\">");
    conteudo.append("<div class=\"section-title\">Emprestimo</div>");
    conteudo.append("<p><strong>Status:</strong> ").append(escape(valorOuNaoInformado(emprestimo.getStatus()))).append("</p>");
    conteudo.append("<p><strong>Retirada prevista:</strong> ")
        .append(formatarDataHora(emprestimo.getDataRetiradaPrevista()))
        .append("</p>");
    conteudo.append("<p><strong>Devolucao prevista:</strong> ")
        .append(formatarDataHora(emprestimo.getDataDevolucaoPrevista()))
        .append("</p>");
    conteudo.append("<p><strong>Responsavel:</strong> ")
        .append(escape(valorOuNaoInformado(obterNomeResponsavel(emprestimo.getResponsavelId()))))
        .append("</p>");
    conteudo.append("</section>");

    conteudo.append("<section class=\"section\">");
    conteudo.append("<div class=\"section-title\">Itens do Emprestimo</div>");
    conteudo.append("<table class=\"print-table\">");
    conteudo.append("<thead>");
    conteudo.append("<tr>");
    conteudo.append("<th>Item</th>");
    conteudo.append("<th>Patrimonio</th>");
    conteudo.append("<th>Tipo</th>");
    conteudo.append("<th>Quantidade</th>");
    conteudo.append("<th>Status</th>");
    conteudo.append("</tr>");
    conteudo.append("</thead>");
    conteudo.append("<tbody>");
    for (EmprestimoEventoItem item : itens) {
      ItemResumo resumo = obterResumoItem(item);
      conteudo.append("<tr>");
      conteudo.append("<td>").append(escape(valorOuNaoInformado(resumo.nomeItem))).append("</td>");
      conteudo.append("<td>").append(escape(valorOuNaoInformado(resumo.numeroPatrimonio))).append("</td>");
      conteudo.append("<td>").append(escape(valorOuNaoInformado(item.getTipoItem()))).append("</td>");
      conteudo.append("<td>").append(escape(String.valueOf(item.getQuantidade()))).append("</td>");
      conteudo.append("<td>").append(escape(valorOuNaoInformado(item.getStatusItem()))).append("</td>");
      conteudo.append("</tr>");
    }
    conteudo.append("</tbody>");
    conteudo.append("</table>");
    conteudo.append("</section>");

    conteudo.append("<div class=\"signature\">");
    conteudo.append("<div><div class=\"signature-line\">Assinatura do Responsavel</div></div>");
    conteudo.append("<div><div class=\"signature-line\">Assinatura da Unidade</div></div>");
    conteudo.append("</div>");

    return conteudo.toString();
  }

  private ItemResumo obterResumoItem(EmprestimoEventoItem item) {
    if ("PATRIMONIO".equalsIgnoreCase(item.getTipoItem())) {
      List<ItemResumo> resultados =
          jdbcTemplate.query(
              "SELECT nome, numero_patrimonio FROM patrimonio_item WHERE id = ?",
              (rs, rowNum) -> new ItemResumo(rs.getString("nome"), rs.getString("numero_patrimonio")),
              item.getItemId());
      return resultados.isEmpty() ? new ItemResumo("Item", null) : resultados.get(0);
    }
    if ("ALMOXARIFADO".equalsIgnoreCase(item.getTipoItem())) {
      List<ItemResumo> resultados =
          jdbcTemplate.query(
              "SELECT descricao FROM almoxarifado_item WHERE id = ?",
              (rs, rowNum) -> new ItemResumo(rs.getString("descricao"), null),
              item.getItemId());
      return resultados.isEmpty() ? new ItemResumo("Item", null) : resultados.get(0);
    }
    return new ItemResumo("Item", null);
  }

  private String obterNomeResponsavel(Long id) {
    if (id == null) {
      return null;
    }
    Optional<Usuario> usuario = usuarioRepository.buscarPorId(id);
    if (!usuario.isPresent()) {
      return null;
    }
    Usuario item = usuario.get();
    return item.getNome() != null && !item.getNome().trim().isEmpty() ? item.getNome() : item.getNomeUsuario();
  }

  private String formatarDataHora(LocalDateTime data) {
    if (data == null) {
      return "Nao informado";
    }
    return DATA_HORA_FORMATTER.format(data);
  }

  private String textoSeguro(String valor) {
    return valor == null || valor.trim().isEmpty() ? "Sistema" : valor.trim();
  }

  private String valorOuNaoInformado(String valor) {
    if (valor == null || valor.trim().isEmpty()) {
      return "Nao informado";
    }
    return valor;
  }

  private String escape(String valor) {
    if (valor == null) {
      return "";
    }
    return valor.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
  }

  private static class ItemResumo {
    private final String nomeItem;
    private final String numeroPatrimonio;

    private ItemResumo(String nomeItem, String numeroPatrimonio) {
      this.nomeItem = nomeItem;
      this.numeroPatrimonio = numeroPatrimonio;
    }
  }
}

