package br.com.g3.relatorios.controller;

import br.com.g3.relatorios.dto.BeneficiarioFichaRequest;
import br.com.g3.relatorios.dto.TermoAutorizacaoRequest;
import br.com.g3.relatorios.service.BeneficiarioFichaService;
import br.com.g3.relatorios.service.RelatorioTarefasPendenciasService;
import br.com.g3.relatorios.service.TermoAutorizacaoService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class RelatoriosController {
  private final TermoAutorizacaoService termoAutorizacaoService;
  private final RelatorioTarefasPendenciasService relatorioTarefasPendenciasService;
  private final BeneficiarioFichaService beneficiarioFichaService;

  public RelatoriosController(
      TermoAutorizacaoService termoAutorizacaoService,
      RelatorioTarefasPendenciasService relatorioTarefasPendenciasService,
      BeneficiarioFichaService beneficiarioFichaService) {
    this.termoAutorizacaoService = termoAutorizacaoService;
    this.relatorioTarefasPendenciasService = relatorioTarefasPendenciasService;
    this.beneficiarioFichaService = beneficiarioFichaService;
  }

  @PostMapping(value = "/authorization-term", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> gerarTermoAutorizacao(@RequestBody TermoAutorizacaoRequest request) {
    byte[] pdf = termoAutorizacaoService.gerarPdf(request);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDisposition(ContentDisposition.inline().filename("termo-autorizacao.pdf").build());
    return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
  }

  @GetMapping(value = "/tarefas-pendencias", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> gerarRelatorioTarefasPendencias() {
    byte[] pdf = relatorioTarefasPendenciasService.gerarPdf();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDisposition(
        ContentDisposition.inline().filename("relatorio-tarefas-pendencias.pdf").build());
    return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
  }

  @PostMapping(value = "/beneficiarios/ficha", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> gerarFichaBeneficiario(
      @RequestBody BeneficiarioFichaRequest request) {
    byte[] pdf = beneficiarioFichaService.gerarPdf(request);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDisposition(ContentDisposition.inline().filename("ficha-beneficiario.pdf").build());
    return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
  }
}
