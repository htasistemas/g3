package br.com.g3.emprestimoseventos.dto;

import java.time.LocalDateTime;
import java.util.List;

public class AgendaDiaDetalheResponse {
  private Long emprestimoId;
  private String status;
  private PeriodoEmprestimoResponse periodo;
  private ResponsavelResumoResponse responsavel;
  private EventoEmprestimoResponse evento;
  private List<EmprestimoEventoItemResponse> itens;

  public Long getEmprestimoId() {
    return emprestimoId;
  }

  public void setEmprestimoId(Long emprestimoId) {
    this.emprestimoId = emprestimoId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public PeriodoEmprestimoResponse getPeriodo() {
    return periodo;
  }

  public void setPeriodo(PeriodoEmprestimoResponse periodo) {
    this.periodo = periodo;
  }

  public ResponsavelResumoResponse getResponsavel() {
    return responsavel;
  }

  public void setResponsavel(ResponsavelResumoResponse responsavel) {
    this.responsavel = responsavel;
  }

  public EventoEmprestimoResponse getEvento() {
    return evento;
  }

  public void setEvento(EventoEmprestimoResponse evento) {
    this.evento = evento;
  }

  public List<EmprestimoEventoItemResponse> getItens() {
    return itens;
  }

  public void setItens(List<EmprestimoEventoItemResponse> itens) {
    this.itens = itens;
  }

  public static class PeriodoEmprestimoResponse {
    private LocalDateTime retiradaPrevista;
    private LocalDateTime devolucaoPrevista;
    private LocalDateTime retiradaReal;
    private LocalDateTime devolucaoReal;

    public LocalDateTime getRetiradaPrevista() {
      return retiradaPrevista;
    }

    public void setRetiradaPrevista(LocalDateTime retiradaPrevista) {
      this.retiradaPrevista = retiradaPrevista;
    }

    public LocalDateTime getDevolucaoPrevista() {
      return devolucaoPrevista;
    }

    public void setDevolucaoPrevista(LocalDateTime devolucaoPrevista) {
      this.devolucaoPrevista = devolucaoPrevista;
    }

    public LocalDateTime getRetiradaReal() {
      return retiradaReal;
    }

    public void setRetiradaReal(LocalDateTime retiradaReal) {
      this.retiradaReal = retiradaReal;
    }

    public LocalDateTime getDevolucaoReal() {
      return devolucaoReal;
    }

    public void setDevolucaoReal(LocalDateTime devolucaoReal) {
      this.devolucaoReal = devolucaoReal;
    }
  }
}
