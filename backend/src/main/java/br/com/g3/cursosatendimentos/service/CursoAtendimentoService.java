package br.com.g3.cursosatendimentos.service;

import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaRequest;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaAnexoRequest;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaAnexoResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaDataListaResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaDataRequest;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaDataResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoRequest;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoStatusRequest;
import java.time.LocalDate;
import java.util.List;
import org.springframework.core.io.Resource;

public interface CursoAtendimentoService {
  List<CursoAtendimentoResponse> listar();

  CursoAtendimentoResponse criar(CursoAtendimentoRequest request);

  CursoAtendimentoResponse atualizar(Long id, CursoAtendimentoRequest request);

  CursoAtendimentoResponse atualizarStatus(Long id, CursoAtendimentoStatusRequest request);

  CursoAtendimentoPresencaResponse listarPresencas(Long cursoId, LocalDate dataAula);

  CursoAtendimentoPresencaResponse salvarPresencas(Long cursoId, CursoAtendimentoPresencaRequest request);

  CursoAtendimentoPresencaDataResponse criarPresencaData(Long cursoId, CursoAtendimentoPresencaDataRequest request);

  CursoAtendimentoPresencaDataListaResponse listarPresencaDatas(Long cursoId, Boolean somentePendentes);

  CursoAtendimentoPresencaDataResponse obterPresencaData(Long cursoId, Long presencaDataId);

  CursoAtendimentoPresencaDataResponse atualizarPresencaData(
      Long cursoId, Long presencaDataId, CursoAtendimentoPresencaDataRequest request);

  void removerPresencaData(Long cursoId, Long presencaDataId);

  CursoAtendimentoPresencaResponse listarPresencasPorDataId(Long cursoId, Long presencaDataId);

  CursoAtendimentoPresencaResponse salvarPresencasPorDataId(
      Long cursoId, Long presencaDataId, CursoAtendimentoPresencaRequest request);

  List<CursoAtendimentoPresencaAnexoResponse> listarPresencaAnexos(
      Long cursoId, Long presencaDataId);

  CursoAtendimentoPresencaAnexoResponse adicionarPresencaAnexo(
      Long cursoId, Long presencaDataId, CursoAtendimentoPresencaAnexoRequest request);

  Resource obterPresencaAnexoArquivo(Long cursoId, Long presencaDataId, Long anexoId);

  void remover(Long id);
}
