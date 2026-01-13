package br.com.g3.ocorrenciacrianca.service;

import br.com.g3.ocorrenciacrianca.dto.OcorrenciaCriancaAnexoRequest;
import br.com.g3.ocorrenciacrianca.dto.OcorrenciaCriancaAnexoResponse;
import br.com.g3.ocorrenciacrianca.dto.OcorrenciaCriancaRequest;
import br.com.g3.ocorrenciacrianca.dto.OcorrenciaCriancaResponse;
import java.util.List;

public interface OcorrenciaCriancaService {
  OcorrenciaCriancaResponse criar(OcorrenciaCriancaRequest request);

  OcorrenciaCriancaResponse atualizar(Long id, OcorrenciaCriancaRequest request);

  OcorrenciaCriancaResponse buscar(Long id);

  List<OcorrenciaCriancaResponse> listar();

  void remover(Long id);

  List<OcorrenciaCriancaAnexoResponse> listarAnexos(Long ocorrenciaId);

  OcorrenciaCriancaAnexoResponse adicionarAnexo(Long ocorrenciaId, OcorrenciaCriancaAnexoRequest request);

  void removerAnexo(Long ocorrenciaId, Long anexoId);

  byte[] gerarPdfDenuncia(Long id);

  byte[] gerarPdfConselhoTutelar(Long id);
}
