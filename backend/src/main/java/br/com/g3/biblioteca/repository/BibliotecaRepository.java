package br.com.g3.biblioteca.repository;

import br.com.g3.biblioteca.domain.BibliotecaEmprestimo;
import br.com.g3.biblioteca.domain.BibliotecaLivro;
import java.util.List;
import java.util.Optional;

public interface BibliotecaRepository {
  List<BibliotecaLivro> listarLivros();

  Optional<BibliotecaLivro> buscarLivroPorId(Long id);

  Optional<BibliotecaLivro> buscarLivroPorCodigo(String codigo);

  BibliotecaLivro salvarLivro(BibliotecaLivro livro);

  void excluirLivro(BibliotecaLivro livro);

  int obterProximoCodigoLivro();

  List<BibliotecaEmprestimo> listarEmprestimos();

  Optional<BibliotecaEmprestimo> buscarEmprestimoPorId(Long id);

  BibliotecaEmprestimo salvarEmprestimo(BibliotecaEmprestimo emprestimo);

  void excluirEmprestimo(BibliotecaEmprestimo emprestimo);

  long contarEmprestimosAtivosPorLivro(Long livroId);
}
