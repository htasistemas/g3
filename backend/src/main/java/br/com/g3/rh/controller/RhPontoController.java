package br.com.g3.rh.controller;

import br.com.g3.rh.dto.RhConfiguracaoPontoRequest;
import br.com.g3.rh.dto.RhConfiguracaoPontoResponse;
import br.com.g3.rh.dto.RhLocalPontoRequest;
import br.com.g3.rh.dto.RhLocalPontoResponse;
import br.com.g3.rh.dto.RhPontoBaterRequest;
import br.com.g3.rh.dto.RhPontoDiaAtualizacaoRequest;
import br.com.g3.rh.dto.RhPontoDiaResponse;
import br.com.g3.rh.dto.RhPontoDiaResumoResponse;
import br.com.g3.rh.dto.RhPontoEspelhoResponse;
import br.com.g3.rh.service.RhPontoService;
import jakarta.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rh/ponto")
public class RhPontoController {
  private static final DateTimeFormatter DATA_FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  private final RhPontoService service;

  public RhPontoController(RhPontoService service) {
    this.service = service;
  }

  @GetMapping("/locais")
  public List<RhLocalPontoResponse> listarLocais() {
    return service.listarLocais();
  }

  @PostMapping("/locais")
  public RhLocalPontoResponse criarLocal(@RequestBody RhLocalPontoRequest request) {
    return service.criarLocal(request);
  }

  @PutMapping("/locais/{id}")
  public RhLocalPontoResponse atualizarLocal(
      @PathVariable("id") Long id,
      @RequestBody RhLocalPontoRequest request) {
    return service.atualizarLocal(id, request);
  }

  @DeleteMapping("/locais/{id}")
  public void removerLocal(@PathVariable("id") Long id) {
    service.removerLocal(id);
  }

  @GetMapping("/locais/ativo")
  public RhLocalPontoResponse buscarLocalAtivo() {
    return service.buscarLocalAtivo();
  }

  @GetMapping("/configuracao")
  public RhConfiguracaoPontoResponse buscarConfiguracao() {
    return service.buscarConfiguracao();
  }

  @PutMapping("/configuracao")
  public RhConfiguracaoPontoResponse atualizarConfiguracao(
      @RequestParam("usuarioId") Long usuarioId,
      @RequestBody RhConfiguracaoPontoRequest request) {
    return service.atualizarConfiguracao(request, usuarioId);
  }

  @PostMapping("/bater")
  public RhPontoDiaResponse baterPonto(
      @RequestBody RhPontoBaterRequest request,
      @RequestParam("usuarioId") Long usuarioId,
      HttpServletRequest httpRequest) {
    String ip = httpRequest.getRemoteAddr();
    String userAgent = httpRequest.getHeader("User-Agent");
    return service.baterPonto(request, usuarioId, ip, userAgent);
  }

  @GetMapping("/espelho")
  public RhPontoEspelhoResponse consultarEspelho(
      @RequestParam(value = "mes", required = false) Integer mes,
      @RequestParam(value = "ano", required = false) Integer ano,
      @RequestParam(value = "funcionarioId") Long funcionarioId) {
    return service.consultarEspelho(mes, ano, funcionarioId);
  }

  @PutMapping("/dia/{id}")
  public RhPontoDiaResponse atualizarDia(
      @PathVariable("id") Long id,
      @RequestParam("usuarioId") Long usuarioId,
      @RequestBody RhPontoDiaAtualizacaoRequest request) {
    return service.atualizarDia(id, request, usuarioId);
  }

  @GetMapping("/relatorio/excel")
  public ResponseEntity<byte[]> exportarExcel(
      @RequestParam(value = "mes", required = false) Integer mes,
      @RequestParam(value = "ano", required = false) Integer ano,
      @RequestParam(value = "funcionarioId") Long funcionarioId) throws IOException {
    RhPontoEspelhoResponse espelho = service.consultarEspelho(mes, ano, funcionarioId);
    byte[] bytes = gerarRelatorioExcel(espelho);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType(
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=folha_ponto.xlsx");
    return ResponseEntity.ok().headers(headers).body(bytes);
  }

  private byte[] gerarRelatorioExcel(RhPontoEspelhoResponse espelho) throws IOException {
    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
      var sheet = workbook.createSheet("Folha de Ponto");
      int rowIndex = 0;

      Row header = sheet.createRow(rowIndex++);
      String[] colunas = {
        "Data", "Ocorr.", "Entrada", "Saída", "Entrada", "Saída",
        "Horas Totais", "Hs Extras Total", "Faltas e Atrasos", "Observações"
      };
      CellStyle headerStyle = workbook.createCellStyle();
      headerStyle.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex());
      headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      headerStyle.setAlignment(HorizontalAlignment.CENTER);
      var headerFont = workbook.createFont();
      headerFont.setColor(IndexedColors.WHITE.getIndex());
      headerFont.setBold(true);
      headerStyle.setFont(headerFont);

      for (int i = 0; i < colunas.length; i++) {
        Cell cell = header.createCell(i);
        cell.setCellValue(colunas[i]);
        cell.setCellStyle(headerStyle);
      }

      for (RhPontoDiaResumoResponse dia : espelho.getDias()) {
        Row row = sheet.createRow(rowIndex++);
        row.createCell(0).setCellValue(dia.getData() != null ? dia.getData().format(DATA_FORMATO) : "");
        row.createCell(1).setCellValue(valorOuVazio(dia.getOcorrencia()));
        row.createCell(2).setCellValue(valorOuVazio(dia.getEntradaManha()));
        row.createCell(3).setCellValue(valorOuVazio(dia.getSaidaManha()));
        row.createCell(4).setCellValue(valorOuVazio(dia.getEntradaTarde()));
        row.createCell(5).setCellValue(valorOuVazio(dia.getSaidaTarde()));
        row.createCell(6).setCellValue(formatarMinutos(dia.getTotalTrabalhadoMinutos()));
        row.createCell(7).setCellValue(formatarMinutos(dia.getExtrasMinutos()));
        row.createCell(8).setCellValue(formatarMinutos(dia.getFaltasAtrasosMinutos()));
        row.createCell(9).setCellValue(valorOuVazio(dia.getObservacoes()));
      }

      Row totalRow = sheet.createRow(rowIndex + 1);
      totalRow.createCell(5).setCellValue("Totais");
      totalRow.createCell(6).setCellValue(formatarMinutos(espelho.getTotalTrabalhadoMinutos()));
      totalRow.createCell(7).setCellValue(formatarMinutos(espelho.getTotalExtrasMinutos()));
      totalRow.createCell(8).setCellValue(formatarMinutos(espelho.getTotalFaltasAtrasosMinutos()));

      for (int i = 0; i < colunas.length; i++) {
        sheet.autoSizeColumn(i);
      }

      workbook.write(output);
      return output.toByteArray();
    }
  }

  private String formatarMinutos(Integer minutos) {
    if (minutos == null) {
      return "00:00";
    }
    int total = Math.max(minutos, 0);
    int horas = total / 60;
    int resto = total % 60;
    return String.format("%02d:%02d", horas, resto);
  }

  private String valorOuVazio(String valor) {
    return valor == null ? "" : valor;
  }
}
