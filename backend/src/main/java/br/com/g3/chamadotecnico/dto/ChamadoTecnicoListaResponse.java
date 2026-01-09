package br.com.g3.chamadotecnico.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class ChamadoTecnicoListaResponse {
  private List<ChamadoTecnicoListaItemResponse> chamados = new ArrayList<>();
  private int pagina;
  @JsonProperty("tamanho_pagina")
  private int tamanhoPagina;
  private long total;

  public ChamadoTecnicoListaResponse() {}

  public ChamadoTecnicoListaResponse(
      List<ChamadoTecnicoListaItemResponse> chamados, int pagina, int tamanhoPagina, long total) {
    this.chamados = chamados;
    this.pagina = pagina;
    this.tamanhoPagina = tamanhoPagina;
    this.total = total;
  }

  public List<ChamadoTecnicoListaItemResponse> getChamados() {
    return chamados;
  }

  public void setChamados(List<ChamadoTecnicoListaItemResponse> chamados) {
    this.chamados = chamados;
  }

  public int getPagina() {
    return pagina;
  }

  public void setPagina(int pagina) {
    this.pagina = pagina;
  }

  public int getTamanhoPagina() {
    return tamanhoPagina;
  }

  public void setTamanhoPagina(int tamanhoPagina) {
    this.tamanhoPagina = tamanhoPagina;
  }

  public long getTotal() {
    return total;
  }

  public void setTotal(long total) {
    this.total = total;
  }
}
