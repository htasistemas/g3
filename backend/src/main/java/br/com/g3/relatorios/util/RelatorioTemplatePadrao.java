package br.com.g3.relatorios.util;

import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class RelatorioTemplatePadrao {
  private static final DateTimeFormatter DATA_HORA_FORMATTER =
      DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

  private RelatorioTemplatePadrao() {}

  public static String buildHtml(
      String titulo,
      String corpoHtml,
      UnidadeAssistencialResponse unidade,
      String usuarioEmissor,
      LocalDateTime dataHoraEmissao) {
    return buildHtml(
        titulo, corpoHtml, unidade, usuarioEmissor, dataHoraEmissao, null);
  }

  public static String buildHtml(
      String titulo,
      String corpoHtml,
      UnidadeAssistencialResponse unidade,
      String usuarioEmissor,
      LocalDateTime dataHoraEmissao,
      String nomeInstituicaoOverride) {
    String nomeInstituicao =
        safe(
            nomeInstituicaoOverride != null && !nomeInstituicaoOverride.trim().isEmpty()
                ? nomeInstituicaoOverride
                : unidade != null ? unidade.getNomeFantasia() : null);
    String razaoSocial = safe(unidade != null ? unidade.getRazaoSocial() : null);
    String cnpj = safe(unidade != null ? unidade.getCnpj() : null);
    String telefone = safe(unidade != null ? unidade.getTelefone() : null);
    String endereco = montarEndereco(unidade);
    String emissao = DATA_HORA_FORMATTER.format(dataHoraEmissao);
    String usuario = safe(usuarioEmissor);

    String htmlTemplate =
        "<!DOCTYPE html>\n"
            + "<html lang=\"pt-BR\">\n"
            + "  <head>\n"
            + "    <meta charset=\"UTF-8\" />\n"
            + "    <title>%s</title>\n"
            + "    <style>\n"
            + "      @page {\n"
            + "        size: A4;\n"
            + "        margin: 20mm;\n"
            + "        @top-center { content: element(page-header); }\n"
            + "        @bottom-center { content: element(page-footer); }\n"
            + "      }\n"
            + "      * { box-sizing: border-box; }\n"
            + "      body { font-family: Arial, sans-serif; margin: 0; color: #111827; }\n"
            + "      .page-header { position: running(page-header); }\n"
            + "      .page-footer { position: running(page-footer); }\n"
            + "      .header {\n"
            + "        text-align: center;\n"
            + "        border-bottom: 1px solid #111827;\n"
            + "        padding-bottom: 8px;\n"
            + "        margin-bottom: 16px;\n"
            + "      }\n"
            + "      .header__meta {\n"
            + "        display: flex;\n"
            + "        justify-content: space-between;\n"
            + "        font-size: 10px;\n"
            + "        margin-bottom: 8px;\n"
            + "      }\n"
            + "      .header__title { font-size: 16px; font-weight: 700; }\n"
            + "      .header__subtitle { font-size: 14px; font-weight: 600; }\n"
            + "      .content { font-size: 12px; line-height: 1.5; }\n"
            + "      .print-table { width: 100%%; border-collapse: collapse; margin-top: 8px; }\n"
            + "      .print-table th, .print-table td { border: 1px solid #111827; padding: 6px 8px; text-align: left; }\n"
            + "      .print-table th { width: 28%%; background: #f3f4f6; }\n"
            + "      .footer {\n"
            + "        border-top: 1px solid #111827;\n"
            + "        margin-top: 18px;\n"
            + "        padding-top: 8px;\n"
            + "        font-size: 10px;\n"
            + "      }\n"
            + "      .page-number:before {\n"
            + "        content: \"Pagina \" counter(page) \" de \" counter(pages);\n"
            + "      }\n"
            + "      .signature {\n"
            + "        margin-top: 28px;\n"
            + "        display: flex;\n"
            + "        justify-content: space-between;\n"
            + "        gap: 24px;\n"
            + "      }\n"
            + "      .signature__line {\n"
            + "        flex: 1;\n"
            + "        border-top: 1px solid #111827;\n"
            + "        padding-top: 4px;\n"
            + "        text-align: center;\n"
            + "        font-size: 11px;\n"
            + "      }\n"
            + "    </style>\n"
            + "  </head>\n"
            + "  <body>\n"
            + "    <div class=\"header page-header\">\n"
            + "      <div class=\"header__meta\">\n"
            + "        <span>Usuario: %s</span>\n"
            + "        <span>Emissao: %s</span>\n"
            + "      </div>\n"
            + "      <div class=\"header__title\">%s</div>\n"
            + "      <div class=\"header__subtitle\">%s</div>\n"
            + "    </div>\n"
            + "    <main class=\"content\">\n"
            + "      %s\n"
            + "    </main>\n"
            + "    <div class=\"footer page-footer\">\n"
            + "      <div>%s</div>\n"
            + "      <div>CNPJ: %s</div>\n"
            + "      <div>Endereco: %s</div>\n"
            + "      <div>Telefone: %s</div>\n"
            + "      <div class=\"page-number\"></div>\n"
            + "      <div>Gerado em: %s</div>\n"
            + "    </div>\n"
            + "  </body>\n"
            + "</html>\n";

    return String.format(
        htmlTemplate,
        escape(titulo),
        escape(usuario),
        escape(emissao),
        escape(nomeInstituicao.isEmpty() ? "Instituicao nao informada" : nomeInstituicao),
        escape(titulo),
        corpoHtml,
        escape(razaoSocial),
        escape(cnpj),
        escape(endereco),
        escape(telefone),
        escape(emissao));
  }

  private static String montarEndereco(UnidadeAssistencialResponse unidade) {
    if (unidade == null) {
      return "Nao informado";
    }
    StringBuilder sb = new StringBuilder();
    appendParte(sb, unidade.getEndereco());
    appendParte(sb, unidade.getNumeroEndereco());
    appendParte(sb, unidade.getComplemento());
    appendParte(sb, unidade.getBairro());
    appendParte(sb, unidade.getCidade());
    appendParte(sb, unidade.getEstado());
    return sb.length() > 0 ? sb.toString() : "Nao informado";
  }

  private static void appendParte(StringBuilder sb, String valor) {
    if (valor == null || valor.trim().isEmpty()) {
      return;
    }
    if (sb.length() > 0) {
      sb.append(" - ");
    }
    sb.append(valor.trim());
  }

  private static String safe(String valor) {
    return valor == null ? "" : valor.trim();
  }

  private static String escape(String valor) {
    return valor == null ? "" : valor.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
  }
}
