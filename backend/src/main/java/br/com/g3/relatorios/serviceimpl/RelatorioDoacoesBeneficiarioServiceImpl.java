package br.com.g3.relatorios.serviceimpl;

import br.com.g3.cadastrobeneficiario.domain.CadastroBeneficiario;
import br.com.g3.cadastrobeneficiario.repository.CadastroBeneficiarioRepository;
import br.com.g3.doacaoplanejada.domain.DoacaoPlanejada;
import br.com.g3.doacaoplanejada.repository.DoacaoPlanejadaRepository;
import br.com.g3.doacaorealizada.domain.DoacaoRealizada;
import br.com.g3.doacaorealizada.domain.DoacaoRealizadaItem;
import br.com.g3.doacaorealizada.repository.DoacaoRealizadaRepository;
import br.com.g3.relatorios.dto.DoacaoBeneficiarioRelatorioRequest;
import br.com.g3.relatorios.service.RelatorioDoacoesBeneficiarioService;
import br.com.g3.relatorios.util.HtmlPdfRenderer;
import br.com.g3.relatorios.util.RelatorioTemplatePadrao;
import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialResponse;
import br.com.g3.unidadeassistencial.service.UnidadeAssistencialService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RelatorioDoacoesBeneficiarioServiceImpl implements RelatorioDoacoesBeneficiarioService {
  private static final DateTimeFormatter DATA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  private final CadastroBeneficiarioRepository beneficiarioRepository;
  private final DoacaoRealizadaRepository doacaoRealizadaRepository;
  private final DoacaoPlanejadaRepository doacaoPlanejadaRepository;
  private final UnidadeAssistencialService unidadeService;

  public RelatorioDoacoesBeneficiarioServiceImpl(
      CadastroBeneficiarioRepository beneficiarioRepository,
      DoacaoRealizadaRepository doacaoRealizadaRepository,
      DoacaoPlanejadaRepository doacaoPlanejadaRepository,
      UnidadeAssistencialService unidadeService) {
    this.beneficiarioRepository = beneficiarioRepository;
    this.doacaoRealizadaRepository = doacaoRealizadaRepository;
    this.doacaoPlanejadaRepository = doacaoPlanejadaRepository;
    this.unidadeService = unidadeService;
  }

  @Override
  public byte[] gerarPdf(DoacaoBeneficiarioRelatorioRequest request) {
    if (request == null || request.getBeneficiarioId() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Beneficiario nao informado.");
    }

    CadastroBeneficiario beneficiario =
        beneficiarioRepository
            .buscarPorId(request.getBeneficiarioId())
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Beneficiario nao encontrado."));

    List<DoacaoRealizada> realizadas =
        doacaoRealizadaRepository.listarPorBeneficiario(request.getBeneficiarioId());
    List<DoacaoPlanejada> planejadas =
        doacaoPlanejadaRepository.listarPorBeneficiario(request.getBeneficiarioId());

    UnidadeAssistencialResponse unidade = unidadeService.obterAtual();
    String corpoHtml = montarCorpo(beneficiario, realizadas, planejadas);
    String html =
        RelatorioTemplatePadrao.buildHtml(
            "Relatorio de Doacoes do Beneficiario",
            corpoHtml,
            unidade,
            textoSeguro(request.getUsuarioEmissor()),
            LocalDateTime.now());

    return HtmlPdfRenderer.render(html);
  }

  private String montarCorpo(
      CadastroBeneficiario beneficiario,
      List<DoacaoRealizada> realizadas,
      List<DoacaoPlanejada> planejadas) {
    StringBuilder sb = new StringBuilder();

    sb.append("<section class=\"section\">");
    sb.append("<div class=\"section-title\">Beneficiario</div>");
    sb.append("<p><strong>Nome:</strong> ")
        .append(escape(valorOuNaoInformado(nomeBeneficiario(beneficiario))))
        .append("</p>");
    sb.append("</section>");

    sb.append("<section class=\"section\">");
    sb.append("<div class=\"section-title\">Doacoes realizadas</div>");
    List<DetalheRealizada> detalhesRealizadas = montarDetalhesRealizadas(realizadas);
    if (detalhesRealizadas.isEmpty()) {
      sb.append("<p>Nenhuma doacao realizada registrada.</p>");
    } else {
      sb.append("<table class=\"print-table\">\n");
      sb.append("<thead><tr>");
      sb.append("<th>Data</th>");
      sb.append("<th>Item</th>");
      sb.append("<th>Quantidade</th>");
      sb.append("<th>Situacao</th>");
      sb.append("</tr></thead>");
      sb.append("<tbody>");
      for (DetalheRealizada detalhe : detalhesRealizadas) {
        sb.append("<tr>")
            .append("<td>").append(escape(formatarData(detalhe.dataDoacao))).append("</td>")
            .append("<td>").append(escape(valorOuNaoInformado(detalhe.item))).append("</td>")
            .append("<td>").append(detalhe.quantidade).append("</td>")
            .append("<td>").append(escape(valorOuNaoInformado(detalhe.situacao))).append("</td>")
            .append("</tr>");
      }
      sb.append("</tbody></table>");
    }
    sb.append("</section>");

    sb.append("<section class=\"section\">");
    sb.append("<div class=\"section-title\">Doacoes pendentes</div>");
    List<DetalhePlanejada> detalhesPendentes = montarDetalhesPendentes(planejadas);
    if (detalhesPendentes.isEmpty()) {
      sb.append("<p>Nenhuma doacao pendente registrada.</p>");
    } else {
      sb.append("<table class=\"print-table\">\n");
      sb.append("<thead><tr>");
      sb.append("<th>Data prevista</th>");
      sb.append("<th>Item</th>");
      sb.append("<th>Quantidade</th>");
      sb.append("<th>Status</th>");
      sb.append("</tr></thead>");
      sb.append("<tbody>");
      for (DetalhePlanejada detalhe : detalhesPendentes) {
        sb.append("<tr>")
            .append("<td>").append(escape(formatarData(detalhe.dataPrevista))).append("</td>")
            .append("<td>").append(escape(valorOuNaoInformado(detalhe.item))).append("</td>")
            .append("<td>").append(detalhe.quantidade).append("</td>")
            .append("<td>").append(escape(valorOuNaoInformado(detalhe.status))).append("</td>")
            .append("</tr>");
      }
      sb.append("</tbody></table>");
    }
    sb.append("</section>");

    return sb.toString();
  }

  private List<DetalheRealizada> montarDetalhesRealizadas(List<DoacaoRealizada> realizadas) {
    List<DetalheRealizada> detalhes = new ArrayList<>();
    if (realizadas == null) {
      return detalhes;
    }

    for (DoacaoRealizada doacao : realizadas) {
      if (doacao == null) {
        continue;
      }
      String situacao = valorOuNaoInformado(doacao.getSituacao());
      LocalDate dataDoacao = doacao.getDataDoacao();
      if (doacao.getItens() == null || doacao.getItens().isEmpty()) {
        detalhes.add(new DetalheRealizada(dataDoacao, "Nao informado", 0, situacao));
        continue;
      }
      for (DoacaoRealizadaItem item : doacao.getItens()) {
        String descricao =
            item.getItem() == null ? "Nao informado" : valorOuNaoInformado(item.getItem().getDescricao());
        detalhes.add(
            new DetalheRealizada(
                dataDoacao,
                descricao,
                quantidadeSegura(item.getQuantidade()),
                situacao));
      }
    }

    detalhes.sort(
        Comparator.comparing((DetalheRealizada detalhe) -> detalhe.dataDoacao == null ? LocalDate.MIN : detalhe.dataDoacao)
            .thenComparing(detalhe -> detalhe.item.toLowerCase(Locale.ROOT)));
    return detalhes;
  }

  private List<DetalhePlanejada> montarDetalhesPendentes(List<DoacaoPlanejada> planejadas) {
    List<DetalhePlanejada> detalhes = new ArrayList<>();
    if (planejadas == null) {
      return detalhes;
    }
    for (DoacaoPlanejada doacao : planejadas) {
      if (doacao == null) {
        continue;
      }
      if (!"pendente".equalsIgnoreCase(valorSeguro(doacao.getStatus()))) {
        continue;
      }
      String descricao =
          doacao.getItem() == null ? "Nao informado" : valorOuNaoInformado(doacao.getItem().getDescricao());
      detalhes.add(
          new DetalhePlanejada(
              doacao.getDataPrevista(),
              descricao,
              quantidadeSegura(doacao.getQuantidade()),
              valorOuNaoInformado(doacao.getStatus())));
    }

    detalhes.sort(
        Comparator.comparing((DetalhePlanejada detalhe) -> detalhe.dataPrevista == null ? LocalDate.MIN : detalhe.dataPrevista)
            .thenComparing(detalhe -> detalhe.item.toLowerCase(Locale.ROOT)));
    return detalhes;
  }

  private String nomeBeneficiario(CadastroBeneficiario beneficiario) {
    if (beneficiario == null) {
      return "Nao informado";
    }
    String nomeSocial = valorSeguro(beneficiario.getNomeSocial());
    if (!nomeSocial.isEmpty()) {
      return nomeSocial;
    }
    String nomeCompleto = valorSeguro(beneficiario.getNomeCompleto());
    return nomeCompleto.isEmpty() ? "Nao informado" : nomeCompleto;
  }

  private String formatarData(LocalDate data) {
    if (data == null) {
      return "Nao informado";
    }
    return DATA_FORMATTER.format(data);
  }

  private int quantidadeSegura(Integer quantidade) {
    return quantidade == null ? 0 : quantidade;
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

  private String escape(String valor) {
    if (valor == null) {
      return "";
    }
    return valor.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
  }

  private static class DetalheRealizada {
    private final LocalDate dataDoacao;
    private final String item;
    private final int quantidade;
    private final String situacao;

    private DetalheRealizada(LocalDate dataDoacao, String item, int quantidade, String situacao) {
      this.dataDoacao = dataDoacao;
      this.item = item;
      this.quantidade = quantidade;
      this.situacao = situacao;
    }
  }

  private static class DetalhePlanejada {
    private final LocalDate dataPrevista;
    private final String item;
    private final int quantidade;
    private final String status;

    private DetalhePlanejada(LocalDate dataPrevista, String item, int quantidade, String status) {
      this.dataPrevista = dataPrevista;
      this.item = item;
      this.quantidade = quantidade;
      this.status = status;
    }
  }
}
