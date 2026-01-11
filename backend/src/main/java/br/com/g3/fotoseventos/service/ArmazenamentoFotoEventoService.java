package br.com.g3.fotoseventos.service;

import br.com.g3.fotoseventos.dto.FotoEventoUploadRequest;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Locale;
import java.util.UUID;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ArmazenamentoFotoEventoService {
  private static final int MAX_LADO_IMAGEM = 1920;
  private static final float QUALIDADE_JPEG = 0.85f;
  private final Path baseDir;

  public ArmazenamentoFotoEventoService(
      @Value("${app.storage.fotos-eventos:storage/fotos-eventos}") String baseDir) {
    this.baseDir = Paths.get(baseDir);
  }

  public FotoEventoArquivoInfo salvarArquivo(Long eventoId, FotoEventoUploadRequest request) { 
    if (request == null || request.getConteudo() == null || request.getConteudo().trim().isEmpty()) {
      return null;
    }

    validarTipoArquivo(request.getContentType(), request.getNomeArquivo());

    byte[] bytes = decodificarBase64(request.getConteudo());
    BufferedImage imagemOriginal = lerImagem(bytes);
    boolean imagemValida = imagemOriginal != null;
    byte[] bytesOtimizados = imagemValida ? otimizarImagem(imagemOriginal) : bytes;

    String extensao = imagemValida ? ".jpg" : obterExtensao(request.getNomeArquivo(), request.getContentType());
    String nomeArquivo = UUID.randomUUID() + extensao;
    Path destino = baseDir.resolve(String.valueOf(eventoId)).resolve(nomeArquivo);

    try {
      Files.createDirectories(destino.getParent());
      Files.write(destino, bytesOtimizados);
      Integer largura = null;
      Integer altura = null;
      if (imagemValida) {
        largura = imagemOriginal.getWidth();
        altura = imagemOriginal.getHeight();
      }
      return new FotoEventoArquivoInfo(
          destino.toString(), (long) bytesOtimizados.length, largura, altura);
    } catch (IOException ex) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao salvar imagem.");
    }
  }

  public void removerArquivo(String caminho) {
    if (caminho == null || caminho.trim().isEmpty()) {
      return;
    }

    try {
      Files.deleteIfExists(Paths.get(caminho));
    } catch (IOException ex) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao remover imagem.");
    }
  }

  private void validarTipoArquivo(String contentType, String nomeArquivo) {
    String tipo = (contentType == null ? "" : contentType.toLowerCase(Locale.ROOT));
    String nome = (nomeArquivo == null ? "" : nomeArquivo.toLowerCase(Locale.ROOT));

    boolean valido =
        tipo.contains("jpeg")
            || tipo.contains("jpg")
            || tipo.contains("png")
            || tipo.contains("webp")
            || nome.endsWith(".jpg")
            || nome.endsWith(".jpeg")
            || nome.endsWith(".png")
            || nome.endsWith(".webp");

    if (!valido) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Envie apenas imagens JPG, PNG ou WEBP.");
    }
  }

  private byte[] decodificarBase64(String conteudo) {
    String base64 = conteudo;
    int indice = conteudo.indexOf("base64,");
    if (indice >= 0) {
      base64 = conteudo.substring(indice + 7);
    }
    return Base64.getDecoder().decode(base64);
  }

  private BufferedImage lerImagem(byte[] bytes) {
    try (ByteArrayInputStream input = new ByteArrayInputStream(bytes)) {
      return ImageIO.read(input);
    } catch (IOException ex) {
      return null;
    }
  }

  private byte[] otimizarImagem(BufferedImage original) {
    BufferedImage ajustada = redimensionarImagem(original);
    try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
      ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
      ImageWriteParam params = writer.getDefaultWriteParam();
      params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
      params.setCompressionQuality(QUALIDADE_JPEG);
      writer.setOutput(ImageIO.createImageOutputStream(output));
      writer.write(null, new IIOImage(ajustada, null, null), params);
      writer.dispose();
      return output.toByteArray();
    } catch (IOException ex) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao otimizar imagem.");
    }
  }

  private BufferedImage redimensionarImagem(BufferedImage original) {
    int largura = original.getWidth();
    int altura = original.getHeight();
    int maiorLado = Math.max(largura, altura);
    if (maiorLado <= MAX_LADO_IMAGEM) {
      return converterParaRgb(original);
    }
    double escala = (double) MAX_LADO_IMAGEM / maiorLado;
    int novaLargura = Math.max(1, (int) Math.round(largura * escala));
    int novaAltura = Math.max(1, (int) Math.round(altura * escala));
    BufferedImage redimensionada = new BufferedImage(novaLargura, novaAltura, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics = redimensionada.createGraphics();
    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, novaLargura, novaAltura);
    graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    graphics.drawImage(original, 0, 0, novaLargura, novaAltura, null);
    graphics.dispose();
    return redimensionada;
  }

  private BufferedImage converterParaRgb(BufferedImage original) {
    if (original.getType() == BufferedImage.TYPE_INT_RGB) {
      return original;
    }
    BufferedImage ajustada =
        new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics = ajustada.createGraphics();
    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, ajustada.getWidth(), ajustada.getHeight());
    graphics.drawImage(original, 0, 0, null);
    graphics.dispose();
    return ajustada;
  }

  public static class FotoEventoArquivoInfo {
    private final String caminho;
    private final Long tamanhoBytes;
    private final Integer largura;
    private final Integer altura;

    public FotoEventoArquivoInfo(String caminho, Long tamanhoBytes, Integer largura, Integer altura) {
      this.caminho = caminho;
      this.tamanhoBytes = tamanhoBytes;
      this.largura = largura;
      this.altura = altura;
    }

    public String getCaminho() {
      return caminho;
    }

    public Long getTamanhoBytes() {
      return tamanhoBytes;
    }

    public Integer getLargura() {
      return largura;
    }

    public Integer getAltura() {
      return altura;
    }
  }

  private String obterExtensao(String nomeArquivo, String contentType) {
    if (nomeArquivo != null && nomeArquivo.contains(".")) {
      return nomeArquivo.substring(nomeArquivo.lastIndexOf('.'));
    }

    if (contentType == null) {
      return ".bin";
    }

    String tipo = contentType.toLowerCase(Locale.ROOT);
    if (tipo.contains("png")) {
      return ".png";
    }
    if (tipo.contains("webp")) {
      return ".webp";
    }
    if (tipo.contains("jpeg") || tipo.contains("jpg")) {
      return ".jpg";
    }
    return ".bin";
  }
}
