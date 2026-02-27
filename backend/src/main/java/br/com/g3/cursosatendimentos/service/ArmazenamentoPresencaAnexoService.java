package br.com.g3.cursosatendimentos.service;

import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaAnexoRequest;
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
public class ArmazenamentoPresencaAnexoService {
  private final Path baseDir;

  public ArmazenamentoPresencaAnexoService(
      @Value("${app.storage.presencas:storage/presencas}") String baseDir) {
    this.baseDir = Paths.get(baseDir);
  }

  public String salvarArquivo(Long presencaDataId, CursoAtendimentoPresencaAnexoRequest request) {
    if (request == null || request.getConteudoBase64() == null || request.getConteudoBase64().trim().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Envie o arquivo da lista de presenca.");
    }
    validarTipoArquivo(request.getTipoMime(), request.getNomeArquivo());
    String extensao = obterExtensao(request.getNomeArquivo(), request.getTipoMime());
    String nomeArquivo = UUID.randomUUID() + extensao;
    Path destino = baseDir.resolve(String.valueOf(presencaDataId)).resolve(nomeArquivo);

    try {
      Files.createDirectories(destino.getParent());
      byte[] bytes = decodificarBase64(request.getConteudoBase64());
      Files.write(destino, bytes);
      return destino.toString();
    } catch (IOException ex) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao salvar o anexo.");
    }
  }

  private void validarTipoArquivo(String contentType, String nomeArquivo) {
    String tipo = contentType == null ? "" : contentType.toLowerCase();
    String nome = nomeArquivo == null ? "" : nomeArquivo.toLowerCase();
    boolean valido =
        tipo.contains("pdf") || nome.endsWith(".pdf");
    if (!valido) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Envie apenas arquivo PDF.");
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
    if (contentType != null && contentType.toLowerCase().contains("pdf")) {
      return ".pdf";
    }
    return ".bin";
  }
}
