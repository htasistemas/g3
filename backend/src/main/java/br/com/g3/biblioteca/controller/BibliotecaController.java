package br.com.g3.biblioteca.controller;

import br.com.g3.biblioteca.dto.BibliotecaAlertaListaResponse;
import br.com.g3.biblioteca.dto.BibliotecaEmprestimoListaResponse;
import br.com.g3.biblioteca.dto.BibliotecaEmprestimoRequest;
import br.com.g3.biblioteca.dto.BibliotecaEmprestimoResponse;
import br.com.g3.biblioteca.dto.BibliotecaLivroListaResponse;
import br.com.g3.biblioteca.dto.BibliotecaLivroRequest;
import br.com.g3.biblioteca.dto.BibliotecaLivroResponse;
import br.com.g3.biblioteca.dto.BibliotecaProximoCodigoResponse;
import br.com.g3.biblioteca.service.BibliotecaService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/biblioteca")
public class BibliotecaController {
  private final BibliotecaService servico;

  public BibliotecaController(BibliotecaService servico) {
    this.servico = servico;
  }

  @GetMapping("/livros")
  public BibliotecaLivroListaResponse listarLivros() {
    return servico.listarLivros();
  }

  @GetMapping("/livros/next-code")
  public BibliotecaProximoCodigoResponse obterProximoCodigoLivro() {
    return new BibliotecaProximoCodigoResponse(servico.obterProximoCodigoLivro());
  }

  @PostMapping("/livros")
  public BibliotecaLivroResponse criarLivro(@RequestBody BibliotecaLivroRequest requisicao) {
    return servico.criarLivro(requisicao);
  }

  @PutMapping("/livros/{id}")
  public BibliotecaLivroResponse atualizarLivro(
      @PathVariable("id") Long id, @RequestBody BibliotecaLivroRequest requisicao) {
    return servico.atualizarLivro(id, requisicao);
  }

  @DeleteMapping("/livros/{id}")
  public void excluirLivro(@PathVariable("id") Long id) {
    servico.excluirLivro(id);
  }

  @GetMapping("/emprestimos")
  public BibliotecaEmprestimoListaResponse listarEmprestimos() {
    return servico.listarEmprestimos();
  }

  @PostMapping("/emprestimos")
  public BibliotecaEmprestimoResponse criarEmprestimo(@RequestBody BibliotecaEmprestimoRequest requisicao) {
    return servico.criarEmprestimo(requisicao);
  }

  @PutMapping("/emprestimos/{id}")
  public BibliotecaEmprestimoResponse atualizarEmprestimo(
      @PathVariable("id") Long id, @RequestBody BibliotecaEmprestimoRequest requisicao) {
    return servico.atualizarEmprestimo(id, requisicao);
  }

  @PutMapping("/emprestimos/{id}/devolucao")
  public BibliotecaEmprestimoResponse registrarDevolucao(
      @PathVariable("id") Long id, @RequestBody BibliotecaEmprestimoRequest requisicao) {
    return servico.registrarDevolucao(id, requisicao);
  }

  @DeleteMapping("/emprestimos/{id}")
  public void excluirEmprestimo(@PathVariable("id") Long id) {
    servico.excluirEmprestimo(id);
  }

  @GetMapping("/alertas")
  public BibliotecaAlertaListaResponse listarAlertas() {
    return servico.listarAlertas();
  }
}
