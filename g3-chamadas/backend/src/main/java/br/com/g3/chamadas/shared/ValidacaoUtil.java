package br.com.g3.chamadas.shared;

public final class ValidacaoUtil {
  private ValidacaoUtil() {}

  public static String textoObrigatorio(String valor, String mensagemErro) {
    if (valor == null || valor.trim().isEmpty()) {
      throw new IllegalArgumentException(mensagemErro);
    }
    return valor.trim();
  }
}
