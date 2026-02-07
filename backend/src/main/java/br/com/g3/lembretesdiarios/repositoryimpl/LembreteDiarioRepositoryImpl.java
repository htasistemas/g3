package br.com.g3.lembretesdiarios.repositoryimpl;

import br.com.g3.lembretesdiarios.domain.LembreteDiario;
import br.com.g3.lembretesdiarios.repository.LembreteDiarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class LembreteDiarioRepositoryImpl implements LembreteDiarioRepository {
  private final LembreteDiarioJpaRepository jpaRepository;

  public LembreteDiarioRepositoryImpl(LembreteDiarioJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public List<LembreteDiario> listarAtivos() {
    return jpaRepository.findAllByDeletadoEmIsNullOrderByProximaExecucaoEmAsc();
  }

  @Override
  public List<LembreteDiario> listarAtivosPorUsuario(Long usuarioId) {
    return jpaRepository.listarPorUsuarioOuTodos(usuarioId);
  }

  @Override
  public Optional<LembreteDiario> buscarPorIdAtivo(Long id) {
    return jpaRepository.findByIdAndDeletadoEmIsNull(id);
  }

  @Override
  public LembreteDiario salvar(LembreteDiario lembrete) {
    return jpaRepository.save(lembrete);
  }
}
