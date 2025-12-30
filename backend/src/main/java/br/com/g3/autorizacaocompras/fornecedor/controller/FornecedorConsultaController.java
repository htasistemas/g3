package br.com.g3.autorizacaocompras.fornecedor.controller;

import br.com.g3.autorizacaocompras.fornecedor.dto.FornecedorCnpjResponse;
import br.com.g3.autorizacaocompras.fornecedor.service.FornecedorConsultaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/financeiro/fornecedores")
public class FornecedorConsultaController {
  private final FornecedorConsultaService service;

  public FornecedorConsultaController(FornecedorConsultaService service) {
    this.service = service;
  }

  @GetMapping("/cnpj/{cnpj}")
  public FornecedorCnpjResponse buscarPorCnpj(@PathVariable("cnpj") String cnpj) {
    String somenteDigitos = cnpj != null ? cnpj.replaceAll("\\\\D", "") : "";
    if (somenteDigitos.length() != 14) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CNPJ inválido.");
    }
    return service.buscarPorCnpj(somenteDigitos);
  }
}
