package br.com.g3.cursosatendimentos.repository;

import br.com.g3.cursosatendimentos.domain.CursoAtendimento;
import java.util.List;
import java.util.Optional;

public interface CursoAtendimentoRepository {
  CursoAtendimento salvar(CursoAtendimento curso);

  List<CursoAtendimento> listar();

  Optional<CursoAtendimento> buscarPorId(Long id);

  void remover(CursoAtendimento curso);
}
