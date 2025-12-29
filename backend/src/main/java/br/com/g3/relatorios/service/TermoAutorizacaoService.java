package br.com.g3.relatorios.service;

import br.com.g3.relatorios.dto.TermoAutorizacaoRequest;

public interface TermoAutorizacaoService {
  byte[] gerarPdf(TermoAutorizacaoRequest request);
}
