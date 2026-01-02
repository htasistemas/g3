package br.com.g3.prontuario.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class ProntuarioResumoResponse {
  private BeneficiarioResumoResponse beneficiario;
  private Map<String, Long> contagens;
  private ProntuarioIndicadoresResponse indicadores;
  private LocalDateTime ultimaAtualizacao;

  public ProntuarioResumoResponse(
      BeneficiarioResumoResponse beneficiario,
      Map<String, Long> contagens,
      ProntuarioIndicadoresResponse indicadores,
      LocalDateTime ultimaAtualizacao) {
    this.beneficiario = beneficiario;
    this.contagens = contagens;
    this.indicadores = indicadores;
    this.ultimaAtualizacao = ultimaAtualizacao;
  }

  public BeneficiarioResumoResponse getBeneficiario() {
    return beneficiario;
  }

  public void setBeneficiario(BeneficiarioResumoResponse beneficiario) {
    this.beneficiario = beneficiario;
  }

  public Map<String, Long> getContagens() {
    return contagens;
  }

  public void setContagens(Map<String, Long> contagens) {
    this.contagens = contagens;
  }

  public ProntuarioIndicadoresResponse getIndicadores() {
    return indicadores;
  }

  public void setIndicadores(ProntuarioIndicadoresResponse indicadores) {
    this.indicadores = indicadores;
  }

  public LocalDateTime getUltimaAtualizacao() {
    return ultimaAtualizacao;
  }

  public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) {
    this.ultimaAtualizacao = ultimaAtualizacao;
  }
}
