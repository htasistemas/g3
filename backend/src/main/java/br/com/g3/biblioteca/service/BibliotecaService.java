package br.com.g3.biblioteca.service;

import br.com.g3.biblioteca.dto.BibliotecaAlertaListaResponse;
import br.com.g3.biblioteca.dto.BibliotecaEmprestimoListaResponse;
import br.com.g3.biblioteca.dto.BibliotecaEmprestimoRequest;
import br.com.g3.biblioteca.dto.BibliotecaEmprestimoResponse;
import br.com.g3.biblioteca.dto.BibliotecaLivroListaResponse;
import br.com.g3.biblioteca.dto.BibliotecaLivroRequest;
import br.com.g3.biblioteca.dto.BibliotecaLivroResponse;

public interface BibliotecaService {
  BibliotecaLivroListaResponse listarLivros();

  BibliotecaLivroResponse criarLivro(BibliotecaLivroRequest requisicao);

  BibliotecaLivroResponse atualizarLivro(Long id, BibliotecaLivroRequest requisicao);

  void excluirLivro(Long id);

  String obterProximoCodigoLivro();

  BibliotecaEmprestimoListaResponse listarEmprestimos();

  BibliotecaEmprestimoResponse criarEmprestimo(BibliotecaEmprestimoRequest requisicao);

  BibliotecaEmprestimoResponse atualizarEmprestimo(Long id, BibliotecaEmprestimoRequest requisicao);

  BibliotecaEmprestimoResponse registrarDevolucao(Long id, BibliotecaEmprestimoRequest requisicao);

  void excluirEmprestimo(Long id);

  BibliotecaAlertaListaResponse listarAlertas();
}
