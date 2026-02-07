package br.com.g3.lembretesdiarios.repository;

import br.com.g3.lembretesdiarios.domain.LembreteDiario;
import java.util.List;
import java.util.Optional;

public interface LembreteDiarioRepository {
  List<LembreteDiario> listarAtivos();
  List<LembreteDiario> listarAtivosPorUsuario(Long usuarioId);

  Optional<LembreteDiario> buscarPorIdAtivo(Long id);

  LembreteDiario salvar(LembreteDiario lembrete);
}
