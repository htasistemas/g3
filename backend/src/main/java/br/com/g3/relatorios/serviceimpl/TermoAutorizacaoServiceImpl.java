package br.com.g3.relatorios.serviceimpl;

import br.com.g3.relatorios.dto.TermoAutorizacaoRequest;
import br.com.g3.relatorios.service.TermoAutorizacaoService;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TermoAutorizacaoServiceImpl implements TermoAutorizacaoService {
  @Override
  public byte[] gerarPdf(TermoAutorizacaoRequest request) {
    String conteudo = montarConteudoPdf(request);
    return gerarPdfSimples(conteudo);
  }

  private String montarConteudoPdf(TermoAutorizacaoRequest request) {
    List<String> linhas = new ArrayList<>();
    linhas.add("TERMO DE AUTORIZACAO PARA TRATAMENTO DE DADOS");
    linhas.add(" ");
    linhas.add("Eu, " + textoSeguro(request.getBeneficiaryName()) + ", autorizo a unidade a tratar");
    linhas.add("meus dados pessoais para fins de atendimento social, conforme a LGPD.");
    linhas.add(" ");
    adicionarDadosUnidade(request, linhas);
    linhas.add(" ");
    linhas.add("Dados do beneficiario:");
    linhas.add("Nome: " + textoSeguro(request.getBeneficiaryName()));
    linhas.add("Data de nascimento: " + textoSeguro(request.getBirthDate()));
    linhas.add("Mae: " + textoSeguro(request.getMotherName()));
    linhas.add("CPF: " + textoSeguro(request.getCpf()));
    linhas.add("RG: " + textoSeguro(request.getRg()));
    linhas.add("NIS: " + textoSeguro(request.getNis()));
    linhas.add("Endereco: " + textoSeguro(request.getAddress()));
    linhas.add("Contato: " + textoSeguro(request.getContact()));
    linhas.add("Data de emissao: " + textoSeguro(request.getIssueDate()));
    linhas.add(" ");
    linhas.add("Declaro estar ciente dos meus direitos de acesso, correcao e revogacao.");
    linhas.add("A autorizacao pode ser revogada a qualquer momento mediante solicitacao.");
    return montarStreamTexto(linhas);
  }

  private void adicionarDadosUnidade(TermoAutorizacaoRequest request, List<String> linhas) {
    TermoAutorizacaoRequest.UnidadeAssistencialRequest unidade = request.getUnit();
    if (unidade == null) {
      return;
    }

    linhas.add("Dados da unidade:");
    linhas.add("Unidade: " + textoSeguro(unidade.getNomeFantasia()));
    linhas.add("Razao social: " + textoSeguro(unidade.getRazaoSocial()));
    linhas.add("CNPJ: " + textoSeguro(unidade.getCnpj()));
    linhas.add("Telefone: " + textoSeguro(unidade.getTelefone()));
    linhas.add("E-mail: " + textoSeguro(unidade.getEmail()));
    linhas.add("Endereco: " + montarEnderecoUnidade(unidade));
  }

  private String montarEnderecoUnidade(TermoAutorizacaoRequest.UnidadeAssistencialRequest unidade) {
    List<String> partes = new ArrayList<>();
    if (temValor(unidade.getEndereco())) {
      partes.add(unidade.getEndereco().trim());
    }
    if (temValor(unidade.getNumeroEndereco())) {
      partes.add("Numero " + unidade.getNumeroEndereco().trim());
    }
    if (temValor(unidade.getComplemento())) {
      partes.add(unidade.getComplemento().trim());
    }
    if (temValor(unidade.getBairro())) {
      partes.add(unidade.getBairro().trim());
    }
    if (temValor(unidade.getPontoReferencia())) {
      partes.add("Ref: " + unidade.getPontoReferencia().trim());
    }
    if (temValor(unidade.getCidade())) {
      partes.add(unidade.getCidade().trim());
    }
    if (temValor(unidade.getEstado())) {
      partes.add(unidade.getEstado().trim());
    }

    if (partes.isEmpty()) {
      return "Nao informado";
    }

    return String.join(" - ", partes);
  }

  private boolean temValor(String valor) {
    return valor != null && !valor.trim().isEmpty();
  }

  private String montarStreamTexto(List<String> linhas) {
    StringBuilder sb = new StringBuilder();
    sb.append("BT\n");
    sb.append("/F1 12 Tf\n");
    sb.append("72 720 Td\n");
    boolean primeira = true;
    for (String linha : linhas) {
      if (!primeira) {
        sb.append("0 -16 Td\n");
      }
      sb.append("(").append(escapePdf(linha)).append(") Tj\n");
      primeira = false;
    }
    sb.append("ET\n");
    return sb.toString();
  }

  private String textoSeguro(String valor) {
    if (valor == null) {
      return "Nao informado";
    }
    String limpo = valor.trim();
    if (limpo.isEmpty()) {
      return "Nao informado";
    }
    return limpo;
  }

  private String escapePdf(String valor) {
    return valor.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)");
  }

  private byte[] gerarPdfSimples(String streamConteudo) {
    byte[] conteudoBytes = streamConteudo.getBytes(StandardCharsets.US_ASCII);
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
    String conteudoTexto = new String(conteudoBytes, StandardCharsets.US_ASCII);
    pdf.append(conteudoTexto);
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

    return pdf.toString().getBytes(StandardCharsets.US_ASCII);
  }
}
