package br.com.g3.cursosatendimentos.service;

import br.com.g3.cursosatendimentos.dto.CursoAtendimentoRequest;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoStatusRequest;
import java.util.List;

public interface CursoAtendimentoService {
  List<CursoAtendimentoResponse> listar();

  CursoAtendimentoResponse criar(CursoAtendimentoRequest request);

  CursoAtendimentoResponse atualizar(Long id, CursoAtendimentoRequest request);

  CursoAtendimentoResponse atualizarStatus(Long id, CursoAtendimentoStatusRequest request);

  void remover(Long id);
}
