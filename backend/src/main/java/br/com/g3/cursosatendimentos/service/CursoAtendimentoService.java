package br.com.g3.cursosatendimentos.service;

import br.com.g3.cursosatendimentos.dto.CursoAtendimentoRequest;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaRequest;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoStatusRequest;
import java.time.LocalDate;
import java.util.List;

public interface CursoAtendimentoService {
  List<CursoAtendimentoResponse> listar();

  CursoAtendimentoResponse criar(CursoAtendimentoRequest request);

  CursoAtendimentoResponse atualizar(Long id, CursoAtendimentoRequest request);

  CursoAtendimentoResponse atualizarStatus(Long id, CursoAtendimentoStatusRequest request);

  CursoAtendimentoPresencaResponse listarPresencas(Long cursoId, LocalDate dataAula);

  CursoAtendimentoPresencaResponse salvarPresencas(Long cursoId, CursoAtendimentoPresencaRequest request);

  void remover(Long id);
}
