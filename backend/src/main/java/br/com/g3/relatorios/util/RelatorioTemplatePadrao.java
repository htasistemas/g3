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
                : unidade != null ? unidade.getRazaoSocial() : null);
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
            + "      .header__meta { width: 100%%; font-size: 10px; margin-bottom: 8px; text-align: right; }\n"
            + "      .header__meta span { display: block; }\n"
            + "      .header__brand { width: 100%%; margin-bottom: 8px; }\n"
            + "      .header__brand-table { width: 100%%; border-collapse: collapse; margin-bottom: 6px; }\n"
            + "      .header__brand-logo { width: 30%%; vertical-align: middle; text-align: left; }\n"
            + "      .header__brand-razao { text-align: center; vertical-align: middle; padding-left: 10px; }\n"
            + "      .header__logo { max-height: 72px; max-width: 180px; display: block; }\n"
            + "      .header__razao { font-size: 14px; font-weight: 600; text-transform: uppercase; }\n"
            + "      .header__center { text-align: center; }\n"
            + "      .header__spacer { width: 100%%; }\n"
            + "      .header__title { font-size: 26px; font-weight: 700; margin: 0; text-transform: uppercase; }\n"
            + "      .header__subtitle { font-size: 14px; font-weight: 600; margin: 4px 0 0; text-transform: uppercase; }\n"
            + "      .content { font-size: 12px; line-height: 1.4; margin-top: 8mm; padding: 24px; }\n"
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
            + "      .cards { display: block; }\n"
            + "      .cards--two { }\n"
            + "      .hero { margin-bottom: 12px; }\n"
            + "      .hero__photo { border: 1px solid #111827; border-radius: 10px; width: 115px; height: 153px; background: #f3f4f6; overflow: hidden; text-align: center; }\n"
            + "      .hero__photo img { width: 100%%; height: 100%%; }\n"
            + "      .hero__summary { border: 1px solid #111827; border-radius: 10px; padding: 10px 12px; background: #ffffff; }\n"
            + "      .hero__name { font-size: 16px; font-weight: 700; margin: 0; text-transform: uppercase; }\n"
            + "      .hero__meta { font-size: 11px; margin: 0 0 4px; }\n"
            + "      .hero-table { width: 100%%; border-collapse: collapse; margin-bottom: 12px; }\n"
            + "      .hero-table__photo { width: 120px; vertical-align: top; border: 1px solid #111827; border-radius: 12px; background: #f3f4f6; height: 153px; text-align: center; overflow: hidden; }\n"
            + "      .hero-table__photo img { width: 100%%; height: 100%%; display: block; border-radius: 12px; }\n"
            + "      .hero-table__info { vertical-align: top; padding-left: 12px; }\n"
            + "      .hero-table__header { width: 100%%; border-collapse: collapse; margin-bottom: 6px; }\n"
            + "      .hero-table__name { vertical-align: top; }\n"
            + "      .hero-table__status { text-align: right; vertical-align: top; }\n"
            + "      .hero__age { font-size: 14px; margin: 6px 0 0; font-weight: 700; }\n"
            + "      .status-badge { display: inline-block; padding: 4px 10px; border-radius: 999px; font-size: 10px; font-weight: 700; text-transform: uppercase; border: 1px solid #111827; }\n"
            + "      .status-badge--ativo { background: #dcfce7; color: #166534; border-color: #14532d; }\n"
            + "      .status-badge--desatualizado { background: #ffedd5; color: #c2410c; border-color: #9a3412; }\n"
            + "      .status-badge--incompleto { background: #fef9c3; color: #854d0e; border-color: #a16207; }\n"
            + "      .status-badge--bloqueado { background: #fecaca; color: #991b1b; border-color: #7f1d1d; }\n"
            + "      .status-badge--analise { background: #e0f2fe; color: #0ea5e9; border-color: #0369a1; }\n"
            + "      .card { border: 1px solid #e5e7eb; border-radius: 14px; background: #ffffff; page-break-inside: avoid; }\n"
            + "      .card__header { padding: 10px 16px; border-bottom: 1px solid #e5e7eb; background: #f3f4f6; }\n"
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
            + "      .card__title { font-size: 12px; font-weight: 600; text-transform: uppercase; letter-spacing: 0.3px; margin: 0; }\n"
            + "      .card__body { padding: 16px; }\n"
            + "      .ficha-info { margin-bottom: 16px; }\n"
            + "      .info-line { width: 100%%; border-collapse: collapse; }\n"
            + "      .info-line td { font-size: 12px; padding: 4px 0; }\n"
            + "      .info-label { font-weight: 600; }\n"
            + "      .info-value { font-weight: 600; }\n"
            + "      .info-value--highlight { font-size: 14px; font-weight: 700; }\n"
            + "      .ficha-grid { width: 100%%; border-collapse: separate; border-spacing: 16px 8px; }\n"
            + "      .ficha-campo { vertical-align: top; }\n"
            + "      .field__line { font-size: 12px; padding: 2px 0; min-height: 18px; }\n"
            + "      .field__line--textarea { min-height: 72px; border: 1px solid #111827; padding: 6px; }\n"
            + "      .field__label-inline { font-size: 12px; text-transform: uppercase; letter-spacing: 0.2px; color: #374151; font-weight: 700; }\n"
            + "      .field__value-inline { font-size: 12px; font-weight: 600; color: #111827; }\n"
            + "      .field__value-inline--cpf { font-size: 14px; font-weight: 700; }\n"
            + "      .subtitulo-termo { font-size: 12px; font-weight: 600; margin: 0 0 8px; }\n"
            + "      .termo-texto { font-size: 12px; border: 1px solid #111827; padding: 10px; margin-bottom: 16px; min-height: 60px; }\n"
            + "      .footer {\n"
            + "        border-top: 1px solid #111827;\n"
            + "        margin-top: 18px;\n"
            + "        padding-top: 8px;\n"
            + "        font-size: 10px;\n"
            + "        text-align: center;\n"
            + "      }\n"
            + "      .page-number:before {\n"
            + "        content: \"Pagina \" counter(page) \" de \" counter(pages);\n"
            + "      }\n"
            + "      .signature { margin-top: 28px; width: 100%%; }\n"
            + "      .signature__line { display: inline-block; width: 49%%; border-top: 1px solid #111827; padding-top: 4px; text-align: center; font-size: 11px; vertical-align: top; }\n"
            + "    </style>\n"
            + "  </head>\n"
            + "  <body>\n"
            + "    <div class=\"header page-header\">\n"
            + "      <div class=\"header__meta\">\n"
            + "        <span>Usuario: %s</span>\n"
            + "        <span>Emissao: %s</span>\n"
            + "      </div>\n"
            + "      <div class=\"header__brand\">\n"
            + "        <table class=\"header__brand-table\">\n"
            + "          <tr>\n"
            + "            <td class=\"header__brand-logo\">%s</td>\n"
            + "            <td class=\"header__brand-razao\"><span class=\"header__razao\">%s</span></td>\n"
            + "          </tr>\n"
            + "        </table>\n"
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
        prepararParaFormatacao(escape(titulo)),
        prepararParaFormatacao(escape(usuario)),
        prepararParaFormatacao(escape(emissao)),
        prepararParaFormatacao(montarLogoHtml(logomarcaRelatorio)),
        prepararParaFormatacao(
            escape(nomeInstituicao.isEmpty() ? "Instituicao nao informada" : nomeInstituicao)),
        prepararParaFormatacao(escape(titulo)),
        prepararParaFormatacao(""),
        prepararParaFormatacao(corpoHtml),
        prepararParaFormatacao(escape(razaoSocial)),
        prepararParaFormatacao(escape(cnpj)),
        prepararParaFormatacao(escape(endereco)),
        prepararParaFormatacao(escape(telefone)),
        prepararParaFormatacao(escape(emissao)));
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

  private static String prepararParaFormatacao(String valor) {
    if (valor == null) {
      return "";
    }
    return valor.replace("%", "%%");
  }
}
