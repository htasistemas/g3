package br.com.g3.relatorios.serviceimpl;

import br.com.g3.relatorios.service.RelatorioTarefasPendenciasService;
import br.com.g3.tarefaspendencias.domain.TarefaPendencia;
import br.com.g3.tarefaspendencias.repository.TarefaPendenciaRepository;
import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialResponse;
import br.com.g3.unidadeassistencial.service.UnidadeAssistencialService;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RelatorioTarefasPendenciasServiceImpl implements RelatorioTarefasPendenciasService {
  private static final DateTimeFormatter DATA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  private static final DateTimeFormatter DATA_HORA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
  private static final List<String> STATUS_ORDER =
      Arrays.asList("Aberta", "Em andamento", "Em atraso", "Concluída");
  private static final String TASK_TEMPLATE = "%-30.30s | %-18.18s | %-12.12s | %-12.12s";

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
    String conteudo = montarConteudoPdf(unidade, tarefas);
    return gerarPdfSimples(conteudo);
  }

  private String montarConteudoPdf(UnidadeAssistencialResponse unidade, List<TarefaPendencia> tarefas) {
    List<String> linhas = new ArrayList<>();
    linhas.add("RELATÓRIO DE TAREFAS E PENDÊNCIAS");
    linhas.add("Logomarca: " + descricaoLogomarca(unidade));
    linhas.add("Razão social: " + textoSeguro(unidade != null ? unidade.getRazaoSocial() : null));
    linhas.add("Nome do relatório: Relatório de Tarefas e Pendências");
    if (unidade != null && temValor(unidade.getNomeFantasia())) {
      linhas.add("Nome fantasia: " + unidade.getNomeFantasia().trim());
    }
    linhas.add("Data de geração: " + DATA_HORA_FORMATTER.format(LocalDateTime.now()));
    linhas.add(" ");
    linhas.add("Corpo do relatório:");

    if (tarefas == null || tarefas.isEmpty()) {
      linhas.add("Nenhuma pendência registrada.");
    } else {
      linhas.add(" ");
      List<TarefaPendencia> restantes = new ArrayList<>(tarefas);
      for (String status : STATUS_ORDER) {
        List<TarefaPendencia> bloco = extrairPorStatus(restantes, status);
        if (bloco.isEmpty()) {
          continue;
        }
        linhas.add("Etapa: " + status + " (" + bloco.size() + ")");
        linhas.add(tabelaCabecalho());
        adicionarTarefas(linhas, bloco);
        linhas.add(" ");
      }
      if (!restantes.isEmpty()) {
        linhas.add("Etapas adicionais:");
        linhas.add(tabelaCabecalho());
        adicionarTarefas(linhas, restantes);
      }
    }

    linhas.add(" ");
    linhas.add("Rodapé:");
    linhas.add("Endereço: " + montarEndereco(unidade));
    linhas.add("Telefone: " + textoSeguro(unidade != null ? unidade.getTelefone() : null));
    linhas.add("E-mail: " + textoSeguro(unidade != null ? unidade.getEmail() : null));
    linhas.add("CNPJ: " + textoSeguro(unidade != null ? unidade.getCnpj() : null));

    return montarStreamTexto(linhas);
  }

  private void adicionarTarefas(List<String> linhas, List<TarefaPendencia> tarefas) {
    for (TarefaPendencia tarefa : tarefas) {
      linhas.add(formatarLinhaTarefa(tarefa));
      linhas.add("    Descrição: " + abreviar(tarefa.getDescricao(), 120));
    }
  }

  private String tabelaCabecalho() {
    return String.format(Locale.US, TASK_TEMPLATE, "Título", "Responsável", "Prazo", "Status");
  }

  private String formatarLinhaTarefa(TarefaPendencia tarefa) {
    String titulo = truncar(tarefa.getTitulo(), 30);
    String responsavel = truncar(tarefa.getResponsavel(), 18);
    String prazo = formatarPrazo(tarefa.getPrazo());
    String status = truncar(tarefa.getStatus(), 12);
    return String.format(Locale.US, TASK_TEMPLATE, titulo, responsavel, prazo, status);
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

  private String montarEndereco(UnidadeAssistencialResponse unidade) {
    if (unidade == null) {
      return "Não informado";
    }
    List<String> partes = new ArrayList<>();
    adicionarSeTem(partes, unidade.getEndereco());
    adicionarSeTem(partes, unidade.getNumeroEndereco(), "Nº ");
    adicionarSeTem(partes, unidade.getComplemento());
    adicionarSeTem(partes, unidade.getBairro());
    adicionarSeTem(partes, unidade.getCidade());
    adicionarSeTem(partes, unidade.getEstado());
    return partes.isEmpty() ? "Não informado" : String.join(" - ", partes);
  }

  private void adicionarSeTem(List<String> partes, String valor) {
    if (temValor(valor)) {
      partes.add(valor.trim());
    }
  }

  private void adicionarSeTem(List<String> partes, String valor, String prefixo) {
    if (temValor(valor)) {
      partes.add(prefixo + valor.trim());
    }
  }

  private String truncar(String valor, int limite) {
    String texto = textoSeguro(valor);
    if (texto.length() <= limite) {
      return texto;
    }
    return texto.substring(0, limite - 3).trim() + "...";
  }

  private String abreviar(String valor, int limite) {
    String texto = textoSeguro(valor);
    if (texto.length() <= limite) {
      return texto;
    }
    return texto.substring(0, limite).trim() + "...";
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

  private String montarStreamTexto(List<String> linhas) {
    StringBuilder sb = new StringBuilder();
    sb.append("BT\n");
    sb.append("/F1 12 Tf\n");
    sb.append("72 720 Td\n");
    boolean primeira = true;
    for (String linha : linhas) {
      if (!primeira) {
        sb.append("0 -16 Td\n");
      }
      sb.append("(").append(escapePdf(linha)).append(") Tj\n");
      primeira = false;
    }
    sb.append("ET\n");
    return sb.toString();
  }

  private String escapePdf(String valor) {
    return valor.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)");
  }

  private byte[] gerarPdfSimples(String streamConteudo) {
    byte[] conteudoBytes = streamConteudo.getBytes(StandardCharsets.US_ASCII);
    StringBuilder pdf = new StringBuilder();
    List<Integer> offsets = new ArrayList<>();

    pdf.append("%PDF-1.4\n");
    offsets.add(pdf.length());
    pdf.append("1 0 obj\n");
    pdf.append("<< /Type /Catalog /Pages 2 0 R >>\n");
    pdf.append("endobj\n");
    offsets.add(pdf.length());
    pdf.append("2 0 obj\n");
    pdf.append("<< /Type /Pages /Kids [3 0 R] /Count 1 >>\n");
    pdf.append("endobj\n");
    offsets.add(pdf.length());
    pdf.append("3 0 obj\n");
    pdf.append("<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792]\n");
    pdf.append("/Resources << /Font << /F1 4 0 R >> >>\n");
    pdf.append("/Contents 5 0 R >>\n");
    pdf.append("endobj\n");
    offsets.add(pdf.length());
    pdf.append("4 0 obj\n");
    pdf.append("<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\n");
    pdf.append("endobj\n");
    offsets.add(pdf.length());
    pdf.append("5 0 obj\n");
    pdf.append("<< /Length ").append(conteudoBytes.length).append(" >>\n");
    pdf.append("stream\n");
    pdf.append(new String(conteudoBytes, StandardCharsets.US_ASCII));
    pdf.append("endstream\n");
    pdf.append("endobj\n");

    int xrefOffset = pdf.length();
    pdf.append("xref\n");
    pdf.append("0 6\n");
    pdf.append("0000000000 65535 f \n");
    for (Integer offset : offsets) {
      pdf.append(String.format("%010d 00000 n \n", offset));
    }
    pdf.append("trailer\n");
    pdf.append("<< /Size 6 /Root 1 0 R >>\n");
    pdf.append("startxref\n");
    pdf.append(xrefOffset).append("\n");
    pdf.append("%%EOF\n");

    return pdf.toString().getBytes(StandardCharsets.US_ASCII);
  }
}
