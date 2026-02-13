package br.com.g3.rhcontratacao.service;

import br.com.g3.rhcontratacao.dto.RhArquivoRequest;

public interface ArmazenamentoRhArquivoService {
  String salvarArquivo(Long processoId, RhArquivoRequest request);
}
