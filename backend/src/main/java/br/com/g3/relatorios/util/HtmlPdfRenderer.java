package br.com.g3.relatorios.util;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.ByteArrayOutputStream;

public final class HtmlPdfRenderer {
  private HtmlPdfRenderer() {}

  public static byte[] render(String html) {
    try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
      PdfRendererBuilder builder = new PdfRendererBuilder();
      builder.useFastMode();
      builder.withHtmlContent(html, null);
      builder.toStream(output);
      builder.run();
      return output.toByteArray();
    } catch (Exception ex) {
      throw new IllegalStateException("Falha ao gerar PDF do relatorio.", ex);
    }
  }
}
