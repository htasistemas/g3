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
            textoSeguro(request.getUsuarioEmissor()),
            LocalDateTime.now());

    return HtmlPdfRenderer.render(html);
  }

  private String montarCorpo(CadastroBeneficiarioResponse b) {
    StringBuilder sb = new StringBuilder();
    sb.append("<section class=\"hero\">");
    sb.append("<div class=\"hero__photo\">");
    if (isPreenchido(b.getFoto3x4())) {
      sb.append("<img src=\"").append(escape(b.getFoto3x4())).append("\" alt=\"Foto 3x4\" />");
    } else {
      sb.append("<span>Sem foto</span>");
    }
    sb.append("</div>");
    sb.append("<div class=\"hero__summary\">");
    sb.append("<p class=\"hero__name\">").append(escape(valorOuNaoInformado(b.getNomeCompleto()))).append("</p>");
    appendResumo(sb, "Codigo", b.getCodigo());
    appendResumo(sb, "CPF", b.getCpf());
    appendResumo(sb, "Nascimento", formatarData(b.getDataNascimento()));
    sb.append("<span class=\"status-badge ")
        .append(statusClasse(b.getStatus()))
        .append("\">")
        .append(escape(valorOuNaoInformado(b.getStatus())))
        .append("</span>");
    sb.append("</div>");
    sb.append("</section>");

    sb.append("<section class=\"cards cards--two\">");
    sb.append("<div class=\"card\">");
    sb.append("<div class=\"card__header card__header--pessoais\"><p class=\"card__title\">Dados pessoais</p></div>");
    sb.append("<div class=\"card__body\"><div class=\"card__grid\">");
    appendCampo(sb, "Codigo", b.getCodigo());
    appendCampo(sb, "Nome completo", b.getNomeCompleto());
    appendCampo(sb, "Nome social", b.getNomeSocial());
    appendCampo(sb, "Apelido", b.getApelido());
    appendCampo(sb, "Data de nascimento", formatarData(b.getDataNascimento()));
    appendCampo(sb, "Sexo biologico", b.getSexoBiologico());
    appendCampo(sb, "Identidade de genero", b.getIdentidadeGenero());
    appendCampo(sb, "Cor raca", b.getCorRaca());
    appendCampo(sb, "Estado civil", b.getEstadoCivil());
    appendCampo(sb, "Nacionalidade", b.getNacionalidade());
    appendCampo(sb, "Naturalidade", juntar(b.getNaturalidadeCidade(), b.getNaturalidadeUf()));
    appendCampo(sb, "Nome da mae", b.getNomeMae());
    appendCampo(sb, "Nome do pai", b.getNomePai());
    appendCampo(sb, "Status", b.getStatus());
    appendCampo(sb, "Opta receber cesta basica", formatarBoolean(b.getOptaReceberCestaBasica()));
    appendCampo(sb, "Apto receber cesta basica", formatarBoolean(b.getAptoReceberCestaBasica()));
    sb.append("</div></div></div>");

    sb.append("<div class=\"card\">");
    sb.append("<div class=\"card__header card__header--endereco\"><p class=\"card__title\">Endereco</p></div>");
    sb.append("<div class=\"card__body\"><div class=\"card__grid\">");
    appendCampo(sb, "CEP", b.getCep());
    appendCampo(sb, "Logradouro", b.getLogradouro());
    appendCampo(sb, "Numero", b.getNumero());
    appendCampo(sb, "Complemento", b.getComplemento());
    appendCampo(sb, "Bairro", b.getBairro());
    appendCampo(sb, "Ponto de referencia", b.getPontoReferencia());
    appendCampo(sb, "Municipio", b.getMunicipio());
    appendCampo(sb, "UF", b.getUf());
    appendCampo(sb, "Zona", b.getZona());
    appendCampo(sb, "Subzona", b.getSubzona());
    appendCampo(sb, "Latitude", b.getLatitude());
    appendCampo(sb, "Longitude", b.getLongitude());
    sb.append("</div></div></div>");

    sb.append("<div class=\"card\">");
    sb.append("<div class=\"card__header card__header--contatos\"><p class=\"card__title\">Contatos</p></div>");
    sb.append("<div class=\"card__body\"><div class=\"card__grid\">");
    appendCampo(sb, "Telefone principal", b.getTelefonePrincipal());
    appendCampo(sb, "Telefone principal whatsapp", formatarBoolean(b.getTelefonePrincipalWhatsapp()));
    appendCampo(sb, "Telefone secundario", b.getTelefoneSecundario());
    appendCampo(sb, "Telefone recado nome", b.getTelefoneRecadoNome());
    appendCampo(sb, "Telefone recado numero", b.getTelefoneRecadoNumero());
    appendCampo(sb, "Email", b.getEmail());
    appendCampo(sb, "Permite contato telefone", formatarBoolean(b.getPermiteContatoTel()));
    appendCampo(sb, "Permite contato whatsapp", formatarBoolean(b.getPermiteContatoWhatsapp()));
    appendCampo(sb, "Permite contato sms", formatarBoolean(b.getPermiteContatoSms()));
    appendCampo(sb, "Permite contato email", formatarBoolean(b.getPermiteContatoEmail()));
    appendCampo(sb, "Horario preferencial", b.getHorarioPreferencialContato());
    sb.append("</div></div></div>");

    sb.append("<div class=\"card\">");
    sb.append("<div class=\"card__header card__header--documentos\"><p class=\"card__title\">Documentos</p></div>");
    sb.append("<div class=\"card__body\"><div class=\"card__grid\">");
    appendCampo(sb, "CPF", b.getCpf());
    appendCampo(sb, "RG numero", b.getRgNumero());
    appendCampo(sb, "RG orgao emissor", b.getRgOrgaoEmissor());
    appendCampo(sb, "RG UF", b.getRgUf());
    appendCampo(sb, "RG data emissao", formatarData(b.getRgDataEmissao()));
    appendCampo(sb, "NIS", b.getNis());
    appendCampo(sb, "Certidao tipo", b.getCertidaoTipo());
    appendCampo(sb, "Certidao livro", b.getCertidaoLivro());
    appendCampo(sb, "Certidao folha", b.getCertidaoFolha());
    appendCampo(sb, "Certidao termo", b.getCertidaoTermo());
    appendCampo(sb, "Certidao cartorio", b.getCertidaoCartorio());
    appendCampo(sb, "Certidao municipio", b.getCertidaoMunicipio());
    appendCampo(sb, "Certidao UF", b.getCertidaoUf());
    appendCampo(sb, "Titulo de eleitor", b.getTituloEleitor());
    appendCampo(sb, "CNH", b.getCnh());
    appendCampo(sb, "Cartao SUS", b.getCartaoSus());
    sb.append("</div></div></div>");

    sb.append("<div class=\"card\">");
    sb.append("<div class=\"card__header card__header--familia\"><p class=\"card__title\">Situacao familiar e social</p></div>");
    sb.append("<div class=\"card__body\"><div class=\"card__grid\">");
    appendCampo(sb, "Mora com familia", formatarBoolean(b.getMoraComFamilia()));
    appendCampo(sb, "Responsavel legal", formatarBoolean(b.getResponsavelLegal()));
    appendCampo(sb, "Vinculo familiar", b.getVinculoFamiliar());
    appendCampo(sb, "Situacao de vulnerabilidade", b.getSituacaoVulnerabilidade());
    appendCampo(sb, "Composicao familiar", b.getComposicaoFamiliar());
    appendCampo(sb, "Criancas e adolescentes", formatarNumero(b.getCriancasAdolescentes()));
    appendCampo(sb, "Idosos", formatarNumero(b.getIdosos()));
    appendCampo(sb, "Acompanhamento CRAS", formatarBoolean(b.getAcompanhamentoCras()));
    appendCampo(sb, "Acompanhamento saude", formatarBoolean(b.getAcompanhamentoSaude()));
    appendCampo(sb, "Participa comunidade", b.getParticipaComunidade());
    appendCampo(sb, "Rede de apoio", b.getRedeApoio());
    sb.append("</div></div></div>");

    sb.append("<div class=\"card\">");
    sb.append("<div class=\"card__header card__header--educacao\"><p class=\"card__title\">Educacao e trabalho</p></div>");
    sb.append("<div class=\"card__body\"><div class=\"card__grid\">");
    appendCampo(sb, "Sabe ler e escrever", formatarBoolean(b.getSabeLerEscrever()));
    appendCampo(sb, "Nivel escolaridade", b.getNivelEscolaridade());
    appendCampo(sb, "Estuda atualmente", formatarBoolean(b.getEstudaAtualmente()));
    appendCampo(sb, "Ocupacao", b.getOcupacao());
    appendCampo(sb, "Situacao trabalho", b.getSituacaoTrabalho());
    appendCampo(sb, "Local trabalho", b.getLocalTrabalho());
    appendCampo(sb, "Renda mensal", b.getRendaMensal());
    appendCampo(sb, "Fonte renda", b.getFonteRenda());
    sb.append("</div></div></div>");

    sb.append("<div class=\"card\">");
    sb.append("<div class=\"card__header card__header--saude\"><p class=\"card__title\">Saude</p></div>");
    sb.append("<div class=\"card__body\"><div class=\"card__grid\">");
    appendCampo(sb, "Possui deficiencia", formatarBoolean(b.getPossuiDeficiencia()));
    appendCampo(sb, "Tipo deficiencia", b.getTipoDeficiencia());
    appendCampo(sb, "CID principal", b.getCidPrincipal());
    appendCampo(sb, "Usa medicacao continua", formatarBoolean(b.getUsaMedicacaoContinua()));
    appendCampo(sb, "Descricao medicacao", b.getDescricaoMedicacao());
    appendCampo(sb, "Servico saude referencia", b.getServicoSaudeReferencia());
    sb.append("</div></div></div>");

    sb.append("<div class=\"card\">");
    sb.append("<div class=\"card__header card__header--beneficios\"><p class=\"card__title\">Beneficios</p></div>");
    sb.append("<div class=\"card__body\"><div class=\"card__grid\">");
    appendCampo(sb, "Recebe beneficio", formatarBoolean(b.getRecebeBeneficio()));
    appendCampo(sb, "Descricao beneficios", b.getBeneficiosDescricao());
    appendCampo(sb, "Valor total beneficios", b.getValorTotalBeneficios());
    appendCampo(sb, "Beneficios recebidos", formatarLista(b.getBeneficiosRecebidos()));
    sb.append("</div></div></div>");

    sb.append("<div class=\"card\">");
    sb.append("<div class=\"card__header card__header--lgpd\"><p class=\"card__title\">LGPD</p></div>");
    sb.append("<div class=\"card__body\"><div class=\"card__grid\">");
    appendCampo(sb, "Aceite LGPD", formatarBoolean(b.getAceiteLgpd()));
    appendCampo(sb, "Data aceite LGPD", formatarData(b.getDataAceiteLgpd()));
    sb.append("</div></div></div>");

    sb.append("<div class=\"card\">");
    sb.append("<div class=\"card__header card__header--observacoes\"><p class=\"card__title\">Observacoes</p></div>");
    sb.append("<div class=\"card__body\"><div class=\"card__note\">")
        .append(escape(valorOuNaoInformado(b.getObservacoes())))
        .append("</div></div>");
    sb.append("</div>");
    sb.append("</section>");

    sb.append("<section class=\"signature\">");
    sb.append("<div class=\"signature__line\">Responsavel</div>");
    sb.append("<div class=\"signature__line\">Beneficiario</div>");
    sb.append("</section>");

    return sb.toString();
  }

  private void appendCampo(StringBuilder sb, String rotulo, String valor) {
    sb.append("<div class=\"card__field\">")
        .append("<p class=\"card__label\">")
        .append(escape(rotulo))
        .append("</p>")
        .append("<p class=\"card__value\">")
        .append(escape(valorOuNaoInformado(valor)))
        .append("</p>")
        .append("</div>");
  }

  private void appendResumo(StringBuilder sb, String rotulo, String valor) {
    sb.append("<p class=\"hero__meta\"><strong>")
        .append(escape(rotulo))
        .append(":</strong> ")
        .append(escape(valorOuNaoInformado(valor)))
        .append("</p>");
  }

  private String statusClasse(String status) {
    if (status == null) {
      return "status-badge--analise";
    }
    switch (status.trim().toUpperCase()) {
      case "ATIVO":
        return "status-badge--ativo";
      case "DESATUALIZADO":
        return "status-badge--desatualizado";
      case "INCOMPLETO":
        return "status-badge--incompleto";
      case "BLOQUEADO":
        return "status-badge--bloqueado";
      case "EM_ANALISE":
      default:
        return "status-badge--analise";
    }
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

  private String textoSeguro(String valor) {
    if (valor == null || valor.trim().isEmpty()) {
      return "Sistema";
    }
    return valor.trim();
  }
}
