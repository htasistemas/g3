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
    String logomarcaRelatorio =
        safe(
            unidade != null && unidade.getLogomarcaRelatorio() != null
                ? unidade.getLogomarcaRelatorio()
                : unidade != null ? unidade.getLogomarca() : null);
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
            + "        margin: 40mm 20mm 20mm;\n"
            + "        @top-center { content: element(page-header); }\n"
            + "        @bottom-center { content: element(page-footer); }\n"
            + "      }\n"
            + "      * { box-sizing: border-box; }\n"
            + "      body { font-family: Arial, sans-serif; margin: 0; color: #111827; }\n"
            + "      .page-header { position: running(page-header); }\n"
            + "      .page-footer { position: running(page-footer); }\n"
            + "      .header {\n"
            + "        text-align: left;\n"
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
            + "      .header__brand {\n"
            + "        display: grid;\n"
            + "        grid-template-columns: 120px 1fr 120px;\n"
            + "        align-items: center;\n"
            + "        gap: 12px;\n"
            + "        margin-bottom: 8px;\n"
            + "      }\n"
            + "      .header__logo { max-height: 48px; max-width: 120px; justify-self: start; display: block; margin-right: auto; }\n"
            + "      .header__center { text-align: center; }\n"
            + "      .header__spacer { width: 100%; }\n"
            + "      .header__title { font-size: 16px; font-weight: 700; margin: 0; }\n"
            + "      .header__subtitle { font-size: 14px; font-weight: 600; margin: 2px 0 0; }\n"
            + "      .content { font-size: 12px; line-height: 1.5; margin-top: 10mm; }\n"
            + "      .content h1, .content h2, .content h3 { margin: 0 0 12px; font-size: 13px; }\n"
            + "      .content .title-centered { text-align: center; }\n"
            + "      .content p { margin: 0 0 10px; }\n"
            + "      .content ol { margin: 0 0 12px 18px; padding: 0; }\n"
            + "      .content li { margin-bottom: 8px; }\n"
            + "      .print-table { width: 100%%; border-collapse: collapse; margin-top: 8px; }\n"
            + "      .print-table th, .print-table td { border: 1px solid #111827; padding: 6px 8px; text-align: left; }\n"
            + "      .print-table th { width: 28%%; background: #f3f4f6; }\n"
            + "      .section { margin-bottom: 16px; }\n"
            + "      .section-title { font-weight: 700; border-bottom: 2px solid #111827; margin: 16px 0 8px; padding-bottom: 4px; }\n"
            + "      .box-text { border: 1px solid #111827; padding: 10px; min-height: 60px; }\n"
            + "      .cards { display: grid; gap: 12px; }\n"
            + "      .cards--two { grid-template-columns: repeat(2, minmax(0, 1fr)); }\n"
            + "      .hero { display: grid; grid-template-columns: 115px 1fr; gap: 12px; margin-bottom: 12px; align-items: stretch; }\n"
            + "      .hero__photo { border: 1px solid #111827; border-radius: 10px; width: 115px; height: 153px; display: flex; align-items: center; justify-content: center; background: #f3f4f6; overflow: hidden; }\n"
            + "      .hero__photo img { width: 100%%; height: 100%%; object-fit: cover; }\n"
            + "      .hero__summary { border: 1px solid #111827; border-radius: 10px; padding: 10px 12px; background: #ffffff; }\n"
            + "      .hero__name { font-size: 16px; font-weight: 700; margin: 0 0 6px; text-transform: uppercase; }\n"
            + "      .hero__meta { font-size: 11px; margin: 0 0 4px; }\n"
            + "      .status-badge { display: inline-block; padding: 4px 10px; border-radius: 999px; font-size: 10px; font-weight: 700; text-transform: uppercase; border: 1px solid #111827; }\n"
            + "      .status-badge--ativo { background: #dcfce7; color: #166534; border-color: #14532d; }\n"
            + "      .status-badge--desatualizado { background: #ffedd5; color: #c2410c; border-color: #9a3412; }\n"
            + "      .status-badge--incompleto { background: #fef9c3; color: #854d0e; border-color: #a16207; }\n"
            + "      .status-badge--bloqueado { background: #fecaca; color: #991b1b; border-color: #7f1d1d; }\n"
            + "      .status-badge--analise { background: #e0f2fe; color: #0ea5e9; border-color: #0369a1; }\n"
            + "      .card { border: 1px solid #e5e7eb; border-radius: 10px; background: #ffffff; page-break-inside: avoid; }\n"
            + "      .card__header { padding: 8px 10px; border-bottom: 1px solid #e5e7eb; }\n"
            + "      .card__header--pessoais { background: #eff6ff; }\n"
            + "      .card__header--endereco { background: #ecfccb; }\n"
            + "      .card__header--contatos { background: #fef3c7; }\n"
            + "      .card__header--documentos { background: #f1f5f9; }\n"
            + "      .card__header--familia { background: #fae8ff; }\n"
            + "      .card__header--educacao { background: #e0f2fe; }\n"
            + "      .card__header--saude { background: #dcfce7; }\n"
            + "      .card__header--beneficios { background: #fef9c3; }\n"
            + "      .card__header--lgpd { background: #e2e8f0; }\n"
            + "      .card__header--observacoes { background: #fef2f2; }\n"
            + "      .card__title { font-size: 12px; font-weight: 700; text-transform: uppercase; letter-spacing: 0.2px; margin: 0; }\n"
            + "      .card__body { padding: 10px 12px; }\n"
            + "      .card__grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(160px, 1fr)); gap: 8px; }\n"
            + "      .card__field { border: 1px solid #e5e7eb; border-radius: 8px; padding: 6px 8px; background: #f9fafb; }\n"
            + "      .card__label { font-size: 10px; text-transform: uppercase; letter-spacing: 0.2px; color: #374151; margin: 0 0 4px; }\n"
            + "      .card__value { font-size: 12px; font-weight: 700; margin: 0; }\n"
            + "      .card__note { border: 1px solid #fde68a; background: #fef3c7; padding: 8px; border-radius: 8px; font-size: 12px; }\n"
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
            + "      <div class=\"header__brand\">\n"
            + "        %s\n"
            + "        <div class=\"header__center\">\n"
            + "          <p class=\"header__title\">%s</p>\n"
            + "          <p class=\"header__subtitle\">%s</p>\n"
            + "        </div>\n"
            + "        <div class=\"header__spacer\"></div>\n"
            + "      </div>\n"
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
        montarLogoHtml(logomarcaRelatorio),
        escape(nomeInstituicao.isEmpty() ? "Instituicao nao informada" : nomeInstituicao),
        escape(titulo),
        corpoHtml,
        escape(razaoSocial),
        escape(cnpj),
        escape(endereco),
        escape(telefone),
        escape(emissao));
  }

  private static String montarLogoHtml(String logomarcaRelatorio) {
    if (logomarcaRelatorio == null || logomarcaRelatorio.trim().isEmpty()) {
      return "";
    }
    return "<img class=\"header__logo\" src=\"" + escape(logomarcaRelatorio) + "\" alt=\"Logomarca\" />";
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
