package br.com.g3.chamadas.shared;

import java.time.LocalDateTime;
import java.util.List;

public class ApiErro {
  private String mensagem;
  private String caminho;
  private LocalDateTime dataHora;
  private List<String> detalhes;

  public ApiErro(String mensagem, String caminho, LocalDateTime dataHora, List<String> detalhes) {
    this.mensagem = mensagem;
    this.caminho = caminho;
    this.dataHora = dataHora;
    this.detalhes = detalhes;
  }

  public String getMensagem() {
    return mensagem;
  }

  public String getCaminho() {
    return caminho;
  }

  public LocalDateTime getDataHora() {
    return dataHora;
  }

  public List<String> getDetalhes() {
    return detalhes;
  }
}
