package br.com.g3.prontuario.repository;

public class ProntuarioEncaminhamentoIndicadores {
  private final long total;
  private final long concluidos;
  private final Double tempoMedioDias;

  public ProntuarioEncaminhamentoIndicadores(long total, long concluidos, Double tempoMedioDias) {
    this.total = total;
    this.concluidos = concluidos;
    this.tempoMedioDias = tempoMedioDias;
  }

  public long getTotal() {
    return total;
  }

  public long getConcluidos() {
    return concluidos;
  }

  public Double getTempoMedioDias() {
    return tempoMedioDias;
  }
}
