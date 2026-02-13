package br.com.g3.rhcontratacao.serviceimpl;

import br.com.g3.rhcontratacao.dto.RhArquivoRequest;
import br.com.g3.rhcontratacao.service.ArmazenamentoRhArquivoService;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ArmazenamentoRhArquivoServiceImpl implements ArmazenamentoRhArquivoService {
  private static final long TAMANHO_MAXIMO_BYTES = 10 * 1024 * 1024;
  private static final String[] TIPOS_PERMITIDOS = {
    "application/pdf",
    "image/jpeg",
    "image/png"
  };

  @Override
  public String salvarArquivo(Long processoId, RhArquivoRequest request) {
    validarRequest(request);
    byte[] conteudo = decodificarBase64(request.getConteudoBase64());
    if (conteudo.length > TAMANHO_MAXIMO_BYTES) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Arquivo excede o tamanho maximo de 10MB.");
    }

    try {
      Path pasta = Paths.get("storage", "rh-contratacao", String.valueOf(processoId));
      Files.createDirectories(pasta);
      String nomeSeguro = request.getNomeArquivo().replaceAll("[^a-zA-Z0-9._-]", "_");
      Path arquivo = pasta.resolve(System.currentTimeMillis() + "_" + nomeSeguro);
      Files.write(arquivo, conteudo);
      return arquivo.toString();
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao salvar o arquivo.");
    }
  }

  private void validarRequest(RhArquivoRequest request) {
    if (request == null || !StringUtils.hasText(request.getNomeArquivo())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome do arquivo e obrigatorio.");
    }
    if (!StringUtils.hasText(request.getMimeType())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo MIME do arquivo e obrigatorio.");
    }
    boolean permitido = false;
    for (String tipo : TIPOS_PERMITIDOS) {
      if (tipo.equalsIgnoreCase(request.getMimeType())) {
        permitido = true;
        break;
      }
    }
    if (!permitido) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Envie apenas arquivos PDF, JPG ou PNG.");
    }
    if (!StringUtils.hasText(request.getConteudoBase64())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conteudo do arquivo e obrigatorio.");
    }
  }

  private byte[] decodificarBase64(String conteudoBase64) {
    try {
      return Base64.getDecoder().decode(conteudoBase64);
    } catch (IllegalArgumentException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conteudo do arquivo invalido.");
    }
  }
}
