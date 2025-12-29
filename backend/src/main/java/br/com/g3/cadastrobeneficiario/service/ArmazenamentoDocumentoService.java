package br.com.g3.cadastrobeneficiario.service;

import br.com.g3.cadastrobeneficiario.dto.DocumentoUploadRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ArmazenamentoDocumentoService {
  private final Path baseDir;

  public ArmazenamentoDocumentoService(
      @Value("${app.storage.beneficiarios:storage/beneficiarios}") String baseDir) {
    this.baseDir = Paths.get(baseDir);
  }

  public String salvarArquivo(Long beneficiarioId, DocumentoUploadRequest request) {
    if (request == null || request.getConteudo() == null || request.getConteudo().trim().isEmpty()) {
      return null;
    }

    validarTipoArquivo(request.getContentType(), request.getNomeArquivo());

    String extensao = obterExtensao(request.getNomeArquivo(), request.getContentType());
    String nomeArquivo = UUID.randomUUID() + extensao;
    Path destino = baseDir.resolve(String.valueOf(beneficiarioId)).resolve(nomeArquivo);

    try {
      Files.createDirectories(destino.getParent());
      byte[] bytes = decodificarBase64(request.getConteudo());
      Files.write(destino, bytes);
      return destino.toString();
    } catch (IOException ex) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao salvar documento.");
    }
  }

  private void validarTipoArquivo(String contentType, String nomeArquivo) {
    String tipo = (contentType == null ? "" : contentType.toLowerCase());
    String nome = (nomeArquivo == null ? "" : nomeArquivo.toLowerCase());

    boolean valido =
        tipo.contains("jpeg")
            || tipo.contains("jpg")
            || tipo.contains("png")
            || tipo.contains("pdf")
            || nome.endsWith(".jpg")
            || nome.endsWith(".jpeg")
            || nome.endsWith(".png")
            || nome.endsWith(".pdf");

    if (!valido) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Envie apenas arquivos JPG ou PDF.");
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

  private String obterExtensao(String nomeArquivo, String contentType) {
    if (nomeArquivo != null && nomeArquivo.contains(".")) {
      return nomeArquivo.substring(nomeArquivo.lastIndexOf('.'));
    }

    if (contentType == null) {
      return ".bin";
    }

    String tipo = contentType.toLowerCase();
    if (tipo.contains("pdf")) {
      return ".pdf";
    }
    if (tipo.contains("png")) {
      return ".png";
    }
    if (tipo.contains("jpeg") || tipo.contains("jpg")) {
      return ".jpg";
    }
    return ".bin";
  }
}
