package br.com.g3.informacoesadministrativas.crypto;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Component
public class CriptografiaSegredoService {
  private static final String TRANSFORMACAO = "AES/GCM/NoPadding";
  private static final int IV_BYTES = 12;
  private static final int TAG_BYTES = 16;
  private final SecureRandom random = new SecureRandom();
  private final String chaveEnv;

  public CriptografiaSegredoService(@Value("${G3_SECRET_KEY:}") String chaveEnv) {
    this.chaveEnv = chaveEnv == null ? "" : chaveEnv.trim();
  }

  public ResultadoCriptografia criptografar(String valor) {
    if (valor == null || valor.isBlank()) {
      return null;
    }
    try {
      byte[] iv = new byte[IV_BYTES];
      random.nextBytes(iv);
      Cipher cipher = Cipher.getInstance(TRANSFORMACAO);
      SecretKey key = obterChave();
      cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(8 * TAG_BYTES, iv));
      byte[] cipherText = cipher.doFinal(valor.getBytes(StandardCharsets.UTF_8));
      int cipherLen = cipherText.length - TAG_BYTES;
      byte[] conteudo = new byte[cipherLen];
      byte[] tag = new byte[TAG_BYTES];
      System.arraycopy(cipherText, 0, conteudo, 0, cipherLen);
      System.arraycopy(cipherText, cipherLen, tag, 0, TAG_BYTES);
      return new ResultadoCriptografia(
          Base64.getEncoder().encodeToString(conteudo),
          Base64.getEncoder().encodeToString(iv),
          Base64.getEncoder().encodeToString(tag));
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao criptografar segredo.");
    }
  }

  public String descriptografar(String conteudoBase64, String ivBase64, String tagBase64) {
    if (conteudoBase64 == null || conteudoBase64.isBlank()) {
      return null;
    }
    try {
      byte[] conteudo = Base64.getDecoder().decode(conteudoBase64);
      byte[] iv = Base64.getDecoder().decode(ivBase64);
      byte[] tag = Base64.getDecoder().decode(tagBase64);
      ByteBuffer buffer = ByteBuffer.allocate(conteudo.length + tag.length);
      buffer.put(conteudo);
      buffer.put(tag);
      Cipher cipher = Cipher.getInstance(TRANSFORMACAO);
      SecretKey key = obterChave();
      cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(8 * TAG_BYTES, iv));
      byte[] plain = cipher.doFinal(buffer.array());
      return new String(plain, StandardCharsets.UTF_8);
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao descriptografar segredo.");
    }
  }

  private SecretKey obterChave() {
    if (chaveEnv.isBlank()) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Chave de criptografia ausente.");
    }
    try {
      byte[] keyBytes;
      try {
        keyBytes = Base64.getDecoder().decode(chaveEnv);
      } catch (IllegalArgumentException ex) {
        keyBytes = null;
      }
      if (keyBytes == null || keyBytes.length != 32) {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        keyBytes = digest.digest(chaveEnv.getBytes(StandardCharsets.UTF_8));
      }
      return new SecretKeySpec(keyBytes, "AES");
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Chave de criptografia invalida.");
    }
  }

  public static class ResultadoCriptografia {
    private final String ciphertext;
    private final String iv;
    private final String tag;

    public ResultadoCriptografia(String ciphertext, String iv, String tag) {
      this.ciphertext = ciphertext;
      this.iv = iv;
      this.tag = tag;
    }

    public String getCiphertext() {
      return ciphertext;
    }

    public String getIv() {
      return iv;
    }

    public String getTag() {
      return tag;
    }
  }
}
