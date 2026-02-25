package br.com.g3.manualsistema.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ManualSistemaResumoResponse {
  private List<ManualSistemaSecaoResumoResponse> secoes;
  private LocalDateTime ultimaAtualizacao;

  public ManualSistemaResumoResponse() {}

  public ManualSistemaResumoResponse(
      List<ManualSistemaSecaoResumoResponse> secoes, LocalDateTime ultimaAtualizacao) {
    this.secoes = secoes;
    this.ultimaAtualizacao = ultimaAtualizacao;
  }

  public List<ManualSistemaSecaoResumoResponse> getSecoes() {
    return secoes;
  }

  public void setSecoes(List<ManualSistemaSecaoResumoResponse> secoes) {
    this.secoes = secoes;
  }

  public LocalDateTime getUltimaAtualizacao() {
    return ultimaAtualizacao;
  }

  public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) {
    this.ultimaAtualizacao = ultimaAtualizacao;
  }
}
