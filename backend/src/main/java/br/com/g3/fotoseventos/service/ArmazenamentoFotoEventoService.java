package br.com.g3.fotoseventos.service;

import br.com.g3.fotoseventos.dto.FotoEventoUploadRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Locale;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ArmazenamentoFotoEventoService {
  private static final int LIMITE_BYTES = 10 * 1024 * 1024;
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
    validarTamanho(bytes);

    String extensao = obterExtensao(request.getNomeArquivo(), request.getContentType());
    String nomeArquivo = UUID.randomUUID() + extensao;
    Path destino = baseDir.resolve(String.valueOf(eventoId)).resolve(nomeArquivo);

    try {
      Files.createDirectories(destino.getParent());
      Files.write(destino, bytes);
      Integer largura = null;
      Integer altura = null;
      try (ByteArrayInputStream input = new ByteArrayInputStream(bytes)) {
        BufferedImage imagem = ImageIO.read(input);
        if (imagem != null) {
          largura = imagem.getWidth();
          altura = imagem.getHeight();
        }
      } catch (IOException ex) {
        largura = null;
        altura = null;
      }
      return new FotoEventoArquivoInfo(destino.toString(), (long) bytes.length, largura, altura);
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

  private void validarTamanho(byte[] bytes) {
    if (bytes != null && bytes.length > LIMITE_BYTES) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Imagem excede o tamanho maximo permitido.");
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
