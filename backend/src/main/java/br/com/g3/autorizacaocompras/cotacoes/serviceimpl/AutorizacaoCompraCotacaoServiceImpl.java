package br.com.g3.autorizacaocompras.cotacoes.serviceimpl;

import br.com.g3.autorizacaocompras.cotacoes.domain.AutorizacaoCompraCotacao;
import br.com.g3.autorizacaocompras.cotacoes.dto.AutorizacaoCompraCotacaoRequest;
import br.com.g3.autorizacaocompras.cotacoes.dto.AutorizacaoCompraCotacaoResponse;
import br.com.g3.autorizacaocompras.cotacoes.mapper.AutorizacaoCompraCotacaoMapper;
import br.com.g3.autorizacaocompras.cotacoes.repository.AutorizacaoCompraCotacaoRepository;
import br.com.g3.autorizacaocompras.cotacoes.service.AutorizacaoCompraCotacaoService;
import br.com.g3.autorizacaocompras.domain.AutorizacaoCompra;
import br.com.g3.autorizacaocompras.repository.AutorizacaoComprasRepository;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class AutorizacaoCompraCotacaoServiceImpl implements AutorizacaoCompraCotacaoService {
  private final AutorizacaoComprasRepository comprasRepository;
  private final AutorizacaoCompraCotacaoRepository cotacaoRepository;

  public AutorizacaoCompraCotacaoServiceImpl(
      AutorizacaoComprasRepository comprasRepository,
      AutorizacaoCompraCotacaoRepository cotacaoRepository) {
    this.comprasRepository = comprasRepository;
    this.cotacaoRepository = cotacaoRepository;
  }

  @Override
  public List<AutorizacaoCompraCotacaoResponse> listarPorCompraId(Long compraId) {
    return cotacaoRepository.listarPorCompraId(compraId).stream()
        .map(AutorizacaoCompraCotacaoMapper::toResponse)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public AutorizacaoCompraCotacaoResponse criar(
      Long compraId, AutorizacaoCompraCotacaoRequest request) {
    AutorizacaoCompra compra =
        comprasRepository
            .buscarPorId(compraId)
            .orElseThrow(() -> new IllegalArgumentException("Autorização de compra não encontrada."));
    AutorizacaoCompraCotacao cotacao = AutorizacaoCompraCotacaoMapper.toDomain(request, compra);
    cotacao.setCriadoEm(LocalDateTime.now());
    preencherCartaoCnpj(cotacao, request.getCartaoCnpjUrl());
    return AutorizacaoCompraCotacaoMapper.toResponse(cotacaoRepository.salvar(cotacao));
  }

  @Override
  @Transactional
  public void remover(Long compraId, Long cotacaoId) {
    if (!cotacaoRepository.existePorIdECompraId(cotacaoId, compraId)) {
      throw new IllegalArgumentException("Cotação não encontrada para esta compra.");
    }
    AutorizacaoCompraCotacao cotacao = cotacaoRepository.buscarPorId(cotacaoId);
    if (cotacao != null) {
      cotacaoRepository.remover(cotacao);
    }
  }

  private void preencherCartaoCnpj(AutorizacaoCompraCotacao cotacao, String cartaoCnpjUrl) {
    if (!StringUtils.hasText(cartaoCnpjUrl)) {
      cotacao.setCartaoCnpjUrl(null);
      cotacao.setCartaoCnpjNome(null);
      cotacao.setCartaoCnpjTipo(null);
      cotacao.setCartaoCnpjConteudo(null);
      return;
    }
    String conteudo = gerarPdfCartaoCnpj(cartaoCnpjUrl.trim());
    cotacao.setCartaoCnpjUrl(cartaoCnpjUrl.trim());
    cotacao.setCartaoCnpjNome("cartao-cnpj.pdf");
    cotacao.setCartaoCnpjTipo("application/pdf");
    cotacao.setCartaoCnpjConteudo(conteudo);
  }

  private String gerarPdfCartaoCnpj(String cartaoCnpjUrl) {
    StringBuilder stream = new StringBuilder();
    stream.append("BT\n");
    stream.append("/F1 12 Tf\n");
    stream.append("72 720 Td\n");
    stream.append("(Cartao CNPJ) Tj\n");
    stream.append("0 -16 Td\n");
    stream.append("(Link: ").append(escapePdf(cartaoCnpjUrl)).append(") Tj\n");
    stream.append("ET\n");
    byte[] conteudoBytes = stream.toString().getBytes(StandardCharsets.US_ASCII);
    StringBuilder pdf = new StringBuilder();
    List<Integer> offsets = new ArrayList<>();

    pdf.append("%PDF-1.4\n");
    offsets.add(pdf.length());
    pdf.append("1 0 obj\n");
    pdf.append("<< /Type /Catalog /Pages 2 0 R >>\n");
    pdf.append("endobj\n");
    offsets.add(pdf.length());
    pdf.append("2 0 obj\n");
    pdf.append("<< /Type /Pages /Kids [3 0 R] /Count 1 >>\n");
    pdf.append("endobj\n");
    offsets.add(pdf.length());
    pdf.append("3 0 obj\n");
    pdf.append("<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792]\n");
    pdf.append("/Resources << /Font << /F1 4 0 R >> >>\n");
    pdf.append("/Contents 5 0 R >>\n");
    pdf.append("endobj\n");
    offsets.add(pdf.length());
    pdf.append("4 0 obj\n");
    pdf.append("<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\n");
    pdf.append("endobj\n");
    offsets.add(pdf.length());
    pdf.append("5 0 obj\n");
    pdf.append("<< /Length ").append(conteudoBytes.length).append(" >>\n");
    pdf.append("stream\n");
    pdf.append(new String(conteudoBytes, StandardCharsets.US_ASCII));
    pdf.append("endstream\n");
    pdf.append("endobj\n");

    int xrefOffset = pdf.length();
    pdf.append("xref\n");
    pdf.append("0 6\n");
    pdf.append("0000000000 65535 f \n");
    for (Integer offset : offsets) {
      pdf.append(String.format("%010d 00000 n \n", offset));
    }
    pdf.append("trailer\n");
    pdf.append("<< /Size 6 /Root 1 0 R >>\n");
    pdf.append("startxref\n");
    pdf.append(xrefOffset).append("\n");
    pdf.append("%%EOF\n");

    String base64 = Base64.getEncoder().encodeToString(pdf.toString().getBytes(StandardCharsets.US_ASCII));
    return "data:application/pdf;base64," + base64;
  }

  private String escapePdf(String valor) {
    return valor.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)");
  }
}
