package br.com.g3.relatorios.serviceimpl;

import br.com.g3.cadastrobeneficiario.dto.CadastroBeneficiarioResponse;
import br.com.g3.cadastrobeneficiario.service.CadastroBeneficiarioService;
import br.com.g3.relatorios.dto.BeneficiarioFichaRequest;
import br.com.g3.relatorios.service.BeneficiarioFichaService;
import br.com.g3.relatorios.util.HtmlPdfRenderer;
import br.com.g3.relatorios.util.RelatorioTemplatePadrao;
import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialResponse;
import br.com.g3.unidadeassistencial.service.UnidadeAssistencialService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BeneficiarioFichaServiceImpl implements BeneficiarioFichaService {
  private static final DateTimeFormatter DATA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  private final CadastroBeneficiarioService cadastroBeneficiarioService;
  private final UnidadeAssistencialService unidadeService;

  public BeneficiarioFichaServiceImpl(
      CadastroBeneficiarioService cadastroBeneficiarioService,
      UnidadeAssistencialService unidadeService) {
    this.cadastroBeneficiarioService = cadastroBeneficiarioService;
    this.unidadeService = unidadeService;
  }

  @Override
  public byte[] gerarPdf(BeneficiarioFichaRequest request) {
    if (request == null || request.getBeneficiarioId() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Beneficiario nao informado.");
    }

    CadastroBeneficiarioResponse beneficiario =
        cadastroBeneficiarioService.buscarPorId(request.getBeneficiarioId());

    UnidadeAssistencialResponse unidade = unidadeService.obterAtual();
    String corpoHtml = montarCorpo(beneficiario);
    String html =
        RelatorioTemplatePadrao.buildHtml(
            "Ficha do Beneficiario",
            corpoHtml,
            unidade,
            "Sistema",
            LocalDateTime.now());

    return HtmlPdfRenderer.render(html);
  }

  private String montarCorpo(CadastroBeneficiarioResponse b) {
    StringBuilder sb = new StringBuilder();
    sb.append("<section>");
    sb.append("<h3>Dados pessoais</h3>");
    sb.append("<table class=\"print-table\">");
    appendLinhaSePreenchido(sb, "Codigo", b.getCodigo());
    appendLinhaSePreenchido(sb, "Nome completo", b.getNomeCompleto());
    appendLinhaSePreenchido(sb, "Nome social", b.getNomeSocial());
    appendLinhaSePreenchido(sb, "Apelido", b.getApelido());
    appendLinhaSePreenchido(sb, "Data de nascimento", formatarData(b.getDataNascimento()));
    appendLinhaSePreenchido(sb, "Sexo biologico", b.getSexoBiologico());
    appendLinhaSePreenchido(sb, "Identidade de genero", b.getIdentidadeGenero());
    appendLinhaSePreenchido(sb, "Cor raca", b.getCorRaca());
    appendLinhaSePreenchido(sb, "Estado civil", b.getEstadoCivil());
    appendLinhaSePreenchido(sb, "Nacionalidade", b.getNacionalidade());
    appendLinhaSePreenchido(
        sb, "Naturalidade", juntar(b.getNaturalidadeCidade(), b.getNaturalidadeUf()));
    appendLinhaSePreenchido(sb, "Nome da mae", b.getNomeMae());
    appendLinhaSePreenchido(sb, "Nome do pai", b.getNomePai());
    appendLinhaSePreenchido(sb, "Status", b.getStatus());
    appendLinhaSePreenchido(
        sb, "Opta receber cesta basica", formatarBoolean(b.getOptaReceberCestaBasica()));
    appendLinhaSePreenchido(
        sb, "Apto receber cesta basica", formatarBoolean(b.getAptoReceberCestaBasica()));
    sb.append("</table>");
    sb.append("</section>");

    sb.append("<section>");
    sb.append("<h3>Endereco</h3>");
    sb.append("<table class=\"print-table\">");
    appendLinhaSePreenchido(sb, "CEP", b.getCep());
    appendLinhaSePreenchido(sb, "Logradouro", b.getLogradouro());
    appendLinhaSePreenchido(sb, "Numero", b.getNumero());
    appendLinhaSePreenchido(sb, "Complemento", b.getComplemento());
    appendLinhaSePreenchido(sb, "Bairro", b.getBairro());
    appendLinhaSePreenchido(sb, "Ponto de referencia", b.getPontoReferencia());
    appendLinhaSePreenchido(sb, "Municipio", b.getMunicipio());
    appendLinhaSePreenchido(sb, "UF", b.getUf());
    appendLinhaSePreenchido(sb, "Zona", b.getZona());
    appendLinhaSePreenchido(sb, "Subzona", b.getSubzona());
    appendLinhaSePreenchido(sb, "Latitude", b.getLatitude());
    appendLinhaSePreenchido(sb, "Longitude", b.getLongitude());
    sb.append("</table>");
    sb.append("</section>");

    sb.append("<section>");
    sb.append("<h3>Contatos</h3>");
    sb.append("<table class=\"print-table\">");
    appendLinhaSePreenchido(sb, "Telefone principal", b.getTelefonePrincipal());
    appendLinhaSePreenchido(
        sb,
        "Telefone principal whatsapp",
        formatarBoolean(b.getTelefonePrincipalWhatsapp()));
    appendLinhaSePreenchido(sb, "Telefone secundario", b.getTelefoneSecundario());
    appendLinhaSePreenchido(sb, "Telefone recado nome", b.getTelefoneRecadoNome());
    appendLinhaSePreenchido(sb, "Telefone recado numero", b.getTelefoneRecadoNumero());
    appendLinhaSePreenchido(sb, "Email", b.getEmail());
    appendLinhaSePreenchido(
        sb, "Permite contato telefone", formatarBoolean(b.getPermiteContatoTel()));
    appendLinhaSePreenchido(
        sb, "Permite contato whatsapp", formatarBoolean(b.getPermiteContatoWhatsapp()));
    appendLinhaSePreenchido(
        sb, "Permite contato sms", formatarBoolean(b.getPermiteContatoSms()));
    appendLinhaSePreenchido(
        sb, "Permite contato email", formatarBoolean(b.getPermiteContatoEmail()));
    appendLinhaSePreenchido(sb, "Horario preferencial", b.getHorarioPreferencialContato());
    sb.append("</table>");
    sb.append("</section>");

    sb.append("<section>");
    sb.append("<h3>Documentos</h3>");
    sb.append("<table class=\"print-table\">");
    appendLinhaSePreenchido(sb, "CPF", b.getCpf());
    appendLinhaSePreenchido(sb, "RG numero", b.getRgNumero());
    appendLinhaSePreenchido(sb, "RG orgao emissor", b.getRgOrgaoEmissor());
    appendLinhaSePreenchido(sb, "RG UF", b.getRgUf());
    appendLinhaSePreenchido(sb, "RG data emissao", formatarData(b.getRgDataEmissao()));
    appendLinhaSePreenchido(sb, "NIS", b.getNis());
    appendLinhaSePreenchido(sb, "Certidao tipo", b.getCertidaoTipo());
    appendLinhaSePreenchido(sb, "Certidao livro", b.getCertidaoLivro());
    appendLinhaSePreenchido(sb, "Certidao folha", b.getCertidaoFolha());
    appendLinhaSePreenchido(sb, "Certidao termo", b.getCertidaoTermo());
    appendLinhaSePreenchido(sb, "Certidao cartorio", b.getCertidaoCartorio());
    appendLinhaSePreenchido(sb, "Certidao municipio", b.getCertidaoMunicipio());
    appendLinhaSePreenchido(sb, "Certidao UF", b.getCertidaoUf());
    appendLinhaSePreenchido(sb, "Titulo de eleitor", b.getTituloEleitor());
    appendLinhaSePreenchido(sb, "CNH", b.getCnh());
    appendLinhaSePreenchido(sb, "Cartao SUS", b.getCartaoSus());
    sb.append("</table>");
    sb.append("</section>");

    sb.append("<section>");
    sb.append("<h3>Situacao familiar e social</h3>");
    sb.append("<table class=\"print-table\">");
    appendLinhaSePreenchido(sb, "Mora com familia", formatarBoolean(b.getMoraComFamilia()));
    appendLinhaSePreenchido(
        sb, "Responsavel legal", formatarBoolean(b.getResponsavelLegal()));
    appendLinhaSePreenchido(sb, "Vinculo familiar", b.getVinculoFamiliar());
    appendLinhaSePreenchido(
        sb, "Situacao de vulnerabilidade", b.getSituacaoVulnerabilidade());
    appendLinhaSePreenchido(sb, "Composicao familiar", b.getComposicaoFamiliar());
    appendLinhaSePreenchido(
        sb, "Criancas e adolescentes", formatarNumero(b.getCriancasAdolescentes()));
    appendLinhaSePreenchido(sb, "Idosos", formatarNumero(b.getIdosos()));
    appendLinhaSePreenchido(
        sb, "Acompanhamento CRAS", formatarBoolean(b.getAcompanhamentoCras()));
    appendLinhaSePreenchido(
        sb, "Acompanhamento saude", formatarBoolean(b.getAcompanhamentoSaude()));
    appendLinhaSePreenchido(sb, "Participa comunidade", b.getParticipaComunidade());
    appendLinhaSePreenchido(sb, "Rede de apoio", b.getRedeApoio());
    sb.append("</table>");
    sb.append("</section>");

    sb.append("<section>");
    sb.append("<h3>Educacao e trabalho</h3>");
    sb.append("<table class=\"print-table\">");
    appendLinhaSePreenchido(
        sb, "Sabe ler e escrever", formatarBoolean(b.getSabeLerEscrever()));
    appendLinhaSePreenchido(sb, "Nivel escolaridade", b.getNivelEscolaridade());
    appendLinhaSePreenchido(
        sb, "Estuda atualmente", formatarBoolean(b.getEstudaAtualmente()));
    appendLinhaSePreenchido(sb, "Ocupacao", b.getOcupacao());
    appendLinhaSePreenchido(sb, "Situacao trabalho", b.getSituacaoTrabalho());
    appendLinhaSePreenchido(sb, "Local trabalho", b.getLocalTrabalho());
    appendLinhaSePreenchido(sb, "Renda mensal", b.getRendaMensal());
    appendLinhaSePreenchido(sb, "Fonte renda", b.getFonteRenda());
    sb.append("</table>");
    sb.append("</section>");

    sb.append("<section>");
    sb.append("<h3>Saude</h3>");
    sb.append("<table class=\"print-table\">");
    appendLinhaSePreenchido(
        sb, "Possui deficiencia", formatarBoolean(b.getPossuiDeficiencia()));
    appendLinhaSePreenchido(sb, "Tipo deficiencia", b.getTipoDeficiencia());
    appendLinhaSePreenchido(sb, "CID principal", b.getCidPrincipal());
    appendLinhaSePreenchido(
        sb, "Usa medicacao continua", formatarBoolean(b.getUsaMedicacaoContinua()));
    appendLinhaSePreenchido(sb, "Descricao medicacao", b.getDescricaoMedicacao());
    appendLinhaSePreenchido(sb, "Servico saude referencia", b.getServicoSaudeReferencia());
    sb.append("</table>");
    sb.append("</section>");

    sb.append("<section>");
    sb.append("<h3>Beneficios</h3>");
    sb.append("<table class=\"print-table\">");
    appendLinhaSePreenchido(
        sb, "Recebe beneficio", formatarBoolean(b.getRecebeBeneficio()));
    appendLinhaSePreenchido(sb, "Descricao beneficios", b.getBeneficiosDescricao());
    appendLinhaSePreenchido(sb, "Valor total beneficios", b.getValorTotalBeneficios());
    appendLinhaSePreenchido(
        sb, "Beneficios recebidos", formatarLista(b.getBeneficiosRecebidos()));
    sb.append("</table>");
    sb.append("</section>");

    sb.append("<section>");
    sb.append("<h3>LGPD</h3>");
    sb.append("<table class=\"print-table\">");
    appendLinhaSePreenchido(sb, "Aceite LGPD", formatarBoolean(b.getAceiteLgpd()));
    appendLinhaSePreenchido(sb, "Data aceite LGPD", formatarData(b.getDataAceiteLgpd()));
    sb.append("</table>");
    sb.append("</section>");

    sb.append("<section>");
    sb.append("<h3>Observacoes</h3>");
    appendObservacoes(sb, b.getObservacoes());
    sb.append("</section>");

    sb.append("<section class=\"signature\">");
    sb.append("<div class=\"signature__line\">Responsavel</div>");
    sb.append("<div class=\"signature__line\">Beneficiario</div>");
    sb.append("</section>");

    return sb.toString();
  }

  private void appendLinha(StringBuilder sb, String rotulo, String valor) {
    sb.append("<tr><th>")
        .append(escape(rotulo))
        .append("</th><td>")
        .append(escape(valorOuNaoInformado(valor)))
        .append("</td></tr>");
  }

  private void appendLinhaSePreenchido(StringBuilder sb, String rotulo, String valor) {
    if (!isPreenchido(valor)) {
      return;
    }
    sb.append("<tr><th>")
        .append(escape(rotulo))
        .append("</th><td>")
        .append(escape(valor.trim()))
        .append("</td></tr>");
  }

  private void appendObservacoes(StringBuilder sb, String observacoes) {
    if (!isPreenchido(observacoes)) {
      return;
    }
    sb.append("<div>").append(escape(observacoes.trim())).append("</div>");
  }

  private boolean isPreenchido(String valor) {
    return valor != null && !valor.trim().isEmpty() && !"Nao informado".equals(valor);
  }

  private String valorOuNaoInformado(String valor) {
    if (valor == null || valor.trim().isEmpty()) {
      return "Nao informado";
    }
    return valor.trim();
  }

  private String formatarData(LocalDate data) {
    if (data == null) {
      return "Nao informado";
    }
    return DATA_FORMATTER.format(data);
  }

  private String formatarBoolean(Boolean valor) {
    if (valor == null) {
      return "Nao informado";
    }
    return valor ? "Sim" : "Nao";
  }

  private String formatarNumero(Integer valor) {
    if (valor == null) {
      return "Nao informado";
    }
    return String.valueOf(valor);
  }

  private String formatarLista(List<String> valores) {
    if (valores == null || valores.isEmpty()) {
      return "Nao informado";
    }
    return valores.stream()
        .filter(Objects::nonNull)
        .map(String::trim)
        .filter((item) -> !item.isEmpty())
        .collect(Collectors.joining(", "));
  }

  private String juntar(String valor1, String valor2) {
    String v1 = valorOuNaoInformado(valor1);
    String v2 = valorOuNaoInformado(valor2);
    if ("Nao informado".equals(v1) && "Nao informado".equals(v2)) {
      return "Nao informado";
    }
    if ("Nao informado".equals(v1)) {
      return v2;
    }
    if ("Nao informado".equals(v2)) {
      return v1;
    }
    return v1 + " - " + v2;
  }

  private String escape(String valor) {
    if (valor == null) {
      return "";
    }
    return valor.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
  }
}
