package br.com.g3.biblioteca.repositoryimpl;

import br.com.g3.biblioteca.domain.BibliotecaEmprestimo;
import br.com.g3.biblioteca.domain.BibliotecaLivro;
import br.com.g3.biblioteca.repository.BibliotecaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BibliotecaRepositoryImpl implements BibliotecaRepository {
  private final BibliotecaLivroJpaRepository repositorioLivro;
  private final BibliotecaEmprestimoJpaRepository repositorioEmprestimo;
  private final JdbcTemplate jdbcTemplate;

  public BibliotecaRepositoryImpl(
      BibliotecaLivroJpaRepository repositorioLivro,
      BibliotecaEmprestimoJpaRepository repositorioEmprestimo,
      JdbcTemplate jdbcTemplate) {
    this.repositorioLivro = repositorioLivro;
    this.repositorioEmprestimo = repositorioEmprestimo;
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<BibliotecaLivro> listarLivros() {
    return repositorioLivro.findAll();
  }

  @Override
  public Optional<BibliotecaLivro> buscarLivroPorId(Long id) {
    return repositorioLivro.findById(id);
  }

  @Override
  public Optional<BibliotecaLivro> buscarLivroPorCodigo(String codigo) {
    return repositorioLivro.findByCodigo(codigo);
  }

  @Override
  public BibliotecaLivro salvarLivro(BibliotecaLivro livro) {
    return repositorioLivro.save(livro);
  }

  @Override
  public void excluirLivro(BibliotecaLivro livro) {
    repositorioLivro.delete(livro);
  }

  @Override
  public int obterProximoCodigoLivro() {
    String sql =
        "SELECT COALESCE(MAX(CAST(codigo AS INTEGER)), 0) FROM biblioteca_livros " +
        "WHERE codigo ~ '^[0-9]+$'";
    Integer total = jdbcTemplate.queryForObject(sql, Integer.class);
    return (total != null ? total : 0) + 1;
  }

  @Override
  public List<BibliotecaEmprestimo> listarEmprestimos() {
    return repositorioEmprestimo.findAllWithLivroOrderByDataEmprestimoDescIdDesc();
  }

  @Override
  public Optional<BibliotecaEmprestimo> buscarEmprestimoPorId(Long id) {
    return repositorioEmprestimo.findByIdWithLivro(id);
  }

  @Override
  public BibliotecaEmprestimo salvarEmprestimo(BibliotecaEmprestimo emprestimo) {
    return repositorioEmprestimo.save(emprestimo);
  }

  @Override
  public void excluirEmprestimo(BibliotecaEmprestimo emprestimo) {
    repositorioEmprestimo.delete(emprestimo);
  }

  @Override
  public long contarEmprestimosAtivosPorLivro(Long livroId) {
    return repositorioEmprestimo.contarPorLivroEStatus(livroId, List.of("ATIVO", "ATRASADO"));
  }
}
