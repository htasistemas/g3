package br.com.g3.gerenciamentodados.dto;

public class GerenciamentoDadosRestauracaoResponse {
  private final Long backupId;
  private final String status;
  private final String mensagem;
  private final String arquivo;

  public GerenciamentoDadosRestauracaoResponse(
      Long backupId, String status, String mensagem, String arquivo) {
    this.backupId = backupId;
    this.status = status;
    this.mensagem = mensagem;
    this.arquivo = arquivo;
  }

  public Long getBackupId() {
    return backupId;
  }

  public String getStatus() {
    return status;
  }

  public String getMensagem() {
    return mensagem;
  }

  public String getArquivo() {
    return arquivo;
  }
}
