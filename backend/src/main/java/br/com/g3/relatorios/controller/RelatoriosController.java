package br.com.g3.relatorios.controller;

import br.com.g3.relatorios.dto.AutorizacaoCompraSolicitacaoRequest;
import br.com.g3.relatorios.dto.BeneficiarioFichaRequest;
import br.com.g3.relatorios.dto.BeneficiarioRelacaoRequest;
import br.com.g3.relatorios.dto.CursoAtendimentoFichaRequest;
import br.com.g3.relatorios.dto.CursoAtendimentoRelacaoRequest;
import br.com.g3.relatorios.dto.TermoAutorizacaoRequest;
import br.com.g3.relatorios.service.BeneficiarioFichaService;
import br.com.g3.relatorios.service.CursoAtendimentoFichaService;
import br.com.g3.relatorios.service.RelatorioBeneficiariosService;
import br.com.g3.relatorios.service.RelatorioCursosAtendimentosService;
import br.com.g3.relatorios.service.RelatorioSolicitacaoComprasService;
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
  private final RelatorioBeneficiariosService relatorioBeneficiariosService;
  private final CursoAtendimentoFichaService cursoAtendimentoFichaService;
  private final RelatorioCursosAtendimentosService relatorioCursosAtendimentosService;
  private final RelatorioSolicitacaoComprasService relatorioSolicitacaoComprasService;

  public RelatoriosController(
      TermoAutorizacaoService termoAutorizacaoService,
      RelatorioTarefasPendenciasService relatorioTarefasPendenciasService,
      BeneficiarioFichaService beneficiarioFichaService,
      RelatorioBeneficiariosService relatorioBeneficiariosService,
      CursoAtendimentoFichaService cursoAtendimentoFichaService,
      RelatorioCursosAtendimentosService relatorioCursosAtendimentosService,
      RelatorioSolicitacaoComprasService relatorioSolicitacaoComprasService) {
    this.termoAutorizacaoService = termoAutorizacaoService;
    this.relatorioTarefasPendenciasService = relatorioTarefasPendenciasService;
    this.beneficiarioFichaService = beneficiarioFichaService;
    this.relatorioBeneficiariosService = relatorioBeneficiariosService;
    this.cursoAtendimentoFichaService = cursoAtendimentoFichaService;
    this.relatorioCursosAtendimentosService = relatorioCursosAtendimentosService;
    this.relatorioSolicitacaoComprasService = relatorioSolicitacaoComprasService;
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

  @PostMapping(value = "/beneficiarios/relacao", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> gerarRelacaoBeneficiarios(
      @RequestBody BeneficiarioRelacaoRequest request) {
    byte[] pdf = relatorioBeneficiariosService.gerarPdf(request);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDisposition(ContentDisposition.inline().filename("relacao-beneficiarios.pdf").build());
    return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
  }

  @PostMapping(value = "/cursos-atendimentos/relacao", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> gerarRelacaoCursosAtendimentos(
      @RequestBody CursoAtendimentoRelacaoRequest request) {
    byte[] pdf = relatorioCursosAtendimentosService.gerarPdf(request);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDisposition(
        ContentDisposition.inline().filename("relacao-cursos-atendimentos.pdf").build());
    return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
  }

  @PostMapping(value = "/cursos-atendimentos/ficha", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> gerarFichaCursoAtendimento(
      @RequestBody CursoAtendimentoFichaRequest request) {
    byte[] pdf = cursoAtendimentoFichaService.gerarPdf(request);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDisposition(
        ContentDisposition.inline().filename("ficha-curso-atendimento.pdf").build());
    return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
  }

  @PostMapping(value = "/autorizacao-compras/solicitacao", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> gerarSolicitacaoCompra(
      @RequestBody AutorizacaoCompraSolicitacaoRequest request) {
    byte[] pdf = relatorioSolicitacaoComprasService.gerarPdf(request);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDisposition(
        ContentDisposition.inline().filename("solicitacao-compras.pdf").build());
    return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
  }
}
