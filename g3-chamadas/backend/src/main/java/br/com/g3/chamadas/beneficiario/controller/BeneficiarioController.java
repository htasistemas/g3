package br.com.g3.chamadas.beneficiario.controller;

import br.com.g3.chamadas.beneficiario.dto.BeneficiarioRespostaDto;
import br.com.g3.chamadas.beneficiario.service.BeneficiarioService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/beneficiarios")
public class BeneficiarioController {
  private final BeneficiarioService beneficiarioService;

  public BeneficiarioController(BeneficiarioService beneficiarioService) {
    this.beneficiarioService = beneficiarioService;
  }

  @GetMapping
  public List<BeneficiarioRespostaDto> listar() {
    return beneficiarioService.listarAtivos();
  }
}
