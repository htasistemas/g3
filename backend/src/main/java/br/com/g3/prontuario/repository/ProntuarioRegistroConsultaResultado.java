package br.com.g3.prontuario.repository;

import br.com.g3.prontuario.dto.ProntuarioRegistroResponse;
import java.util.List;

public class ProntuarioRegistroConsultaResultado {
  private final List<ProntuarioRegistroResponse> registros;
  private final long total;

  public ProntuarioRegistroConsultaResultado(List<ProntuarioRegistroResponse> registros, long total) {
    this.registros = registros;
    this.total = total;
  }

  public List<ProntuarioRegistroResponse> getRegistros() {
    return registros;
  }

  public long getTotal() {
    return total;
  }
}
