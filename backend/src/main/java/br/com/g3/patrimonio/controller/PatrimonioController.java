package br.com.g3.patrimonio.controller;

import br.com.g3.patrimonio.dto.PatrimonioCadastroResponse;
import br.com.g3.patrimonio.dto.PatrimonioListaResponse;
import br.com.g3.patrimonio.dto.PatrimonioMovimentoRequest;
import br.com.g3.patrimonio.dto.PatrimonioRequest;
import br.com.g3.patrimonio.dto.PatrimonioResponse;
import br.com.g3.patrimonio.service.PatrimonioService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patrimonios")
public class PatrimonioController {
  private final PatrimonioService service;

  public PatrimonioController(PatrimonioService service) {
    this.service = service;
  }

  @GetMapping
  public PatrimonioListaResponse listar() {
    List<PatrimonioResponse> patrimonios = service.listar();
    return new PatrimonioListaResponse(patrimonios);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public PatrimonioCadastroResponse criar(@Valid @RequestBody PatrimonioRequest request) {
    return new PatrimonioCadastroResponse(service.criar(request));
  }

  @PutMapping("/{id}")
  public PatrimonioCadastroResponse atualizar(
      @PathVariable("id") Long id, @Valid @RequestBody PatrimonioRequest request) {
    return new PatrimonioCadastroResponse(service.atualizar(id, request));
  }

  @PostMapping("/{id}/movimentos")
  public PatrimonioCadastroResponse registrarMovimento(
      @PathVariable("id") Long id, @Valid @RequestBody PatrimonioMovimentoRequest request) {
    return new PatrimonioCadastroResponse(service.registrarMovimento(id, request));
  }
}
