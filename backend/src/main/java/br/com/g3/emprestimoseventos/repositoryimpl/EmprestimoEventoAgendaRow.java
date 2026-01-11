package br.com.g3.emprestimoseventos.repositoryimpl;

import java.time.LocalDateTime;

public class EmprestimoEventoAgendaRow {
  private Long emprestimoId;
  private String status;
  private LocalDateTime retiradaPrevista;
  private LocalDateTime devolucaoPrevista;
  private LocalDateTime retiradaReal;
  private LocalDateTime devolucaoReal;
  private Long responsavelId;
  private String responsavelNome;
  private Long eventoId;
  private String eventoTitulo;
  private String eventoLocal;
  private LocalDateTime eventoInicio;
  private LocalDateTime eventoFim;
  private Long itemId;
  private String nomeItem;
  private String numeroPatrimonio;
  private Integer quantidade;
  private String statusItem;
  private String tipoItem;

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

  public Long getResponsavelId() {
    return responsavelId;
  }

  public void setResponsavelId(Long responsavelId) {
    this.responsavelId = responsavelId;
  }

  public String getResponsavelNome() {
    return responsavelNome;
  }

  public void setResponsavelNome(String responsavelNome) {
    this.responsavelNome = responsavelNome;
  }

  public Long getEventoId() {
    return eventoId;
  }

  public void setEventoId(Long eventoId) {
    this.eventoId = eventoId;
  }

  public String getEventoTitulo() {
    return eventoTitulo;
  }

  public void setEventoTitulo(String eventoTitulo) {
    this.eventoTitulo = eventoTitulo;
  }

  public String getEventoLocal() {
    return eventoLocal;
  }

  public void setEventoLocal(String eventoLocal) {
    this.eventoLocal = eventoLocal;
  }

  public LocalDateTime getEventoInicio() {
    return eventoInicio;
  }

  public void setEventoInicio(LocalDateTime eventoInicio) {
    this.eventoInicio = eventoInicio;
  }

  public LocalDateTime getEventoFim() {
    return eventoFim;
  }

  public void setEventoFim(LocalDateTime eventoFim) {
    this.eventoFim = eventoFim;
  }

  public Long getItemId() {
    return itemId;
  }

  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }

  public String getNomeItem() {
    return nomeItem;
  }

  public void setNomeItem(String nomeItem) {
    this.nomeItem = nomeItem;
  }

  public String getNumeroPatrimonio() {
    return numeroPatrimonio;
  }

  public void setNumeroPatrimonio(String numeroPatrimonio) {
    this.numeroPatrimonio = numeroPatrimonio;
  }

  public Integer getQuantidade() {
    return quantidade;
  }

  public void setQuantidade(Integer quantidade) {
    this.quantidade = quantidade;
  }

  public String getStatusItem() {
    return statusItem;
  }

  public void setStatusItem(String statusItem) {
    this.statusItem = statusItem;
  }

  public String getTipoItem() {
    return tipoItem;
  }

  public void setTipoItem(String tipoItem) {
    this.tipoItem = tipoItem;
  }
}
