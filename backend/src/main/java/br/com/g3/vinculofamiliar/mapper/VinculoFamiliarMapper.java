package br.com.g3.vinculofamiliar.mapper;

import br.com.g3.cadastrobeneficiario.domain.CadastroBeneficiario;
import br.com.g3.cadastrobeneficiario.domain.ContatoBeneficiario;
import br.com.g3.cadastrobeneficiario.domain.DocumentoBeneficiario;
import br.com.g3.unidadeassistencial.domain.Endereco;
import br.com.g3.vinculofamiliar.domain.VinculoFamiliar;
import br.com.g3.vinculofamiliar.domain.VinculoFamiliarMembro;
import br.com.g3.vinculofamiliar.dto.BeneficiarioResumoResponse;
import br.com.g3.vinculofamiliar.dto.VinculoFamiliarCriacaoRequest;
import br.com.g3.vinculofamiliar.dto.VinculoFamiliarMembroRequest;
import br.com.g3.vinculofamiliar.dto.VinculoFamiliarMembroResponse;
import br.com.g3.vinculofamiliar.dto.VinculoFamiliarResponse;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class VinculoFamiliarMapper {
  private VinculoFamiliarMapper() {}

  private static final String STATUS_PADRAO = "ATIVO";

  public static VinculoFamiliar toDomain(VinculoFamiliarCriacaoRequest request) {
    VinculoFamiliar vinculo = new VinculoFamiliar();
    aplicarDados(vinculo, request);
    LocalDateTime agora = LocalDateTime.now();
    vinculo.setCriadoEm(agora);
    vinculo.setAtualizadoEm(agora);
    return vinculo;
  }

  public static void aplicarAtualizacao(VinculoFamiliar vinculo, VinculoFamiliarCriacaoRequest request) {
    aplicarDados(vinculo, request);
    vinculo.setAtualizadoEm(LocalDateTime.now());
  }

  public static VinculoFamiliarMembro criarMembro(
      VinculoFamiliar vinculo,
      CadastroBeneficiario beneficiario,
      VinculoFamiliarMembroRequest request) {
    VinculoFamiliarMembro membro = new VinculoFamiliarMembro();
    aplicarDadosMembro(membro, vinculo, beneficiario, request);
    LocalDateTime agora = LocalDateTime.now();
    membro.setCriadoEm(agora);
    membro.setAtualizadoEm(agora);
    return membro;
  }

  public static void aplicarAtualizacaoMembro(
      VinculoFamiliarMembro membro,
      VinculoFamiliar vinculo,
      CadastroBeneficiario beneficiario,
      VinculoFamiliarMembroRequest request) {
    aplicarDadosMembro(membro, vinculo, beneficiario, request);
    membro.setAtualizadoEm(LocalDateTime.now());
  }

  public static VinculoFamiliarResponse toResponse(VinculoFamiliar vinculo) {
    return new VinculoFamiliarResponse(
        vinculo.getId(),
        vinculo.getNomeFamilia(),
        vinculo.getReferenciaFamiliar() != null ? vinculo.getReferenciaFamiliar().getId() : null,
        toResumo(vinculo.getReferenciaFamiliar()),
        vinculo.getStatus(),
        vinculo.getCep(),
        vinculo.getLogradouro(),
        vinculo.getNumero(),
        vinculo.getComplemento(),
        vinculo.getBairro(),
        vinculo.getPontoReferencia(),
        vinculo.getMunicipio(),
        vinculo.getUf(),
        vinculo.getZona(),
        vinculo.getSituacaoImovel(),
        vinculo.getTipoMoradia(),
        vinculo.getAguaEncanada(),
        vinculo.getEsgotoTipo(),
        vinculo.getColetaLixo(),
        vinculo.getEnergiaEletrica(),
        vinculo.getInternet(),
        vinculo.getArranjoFamiliar(),
        vinculo.getQtdMembros(),
        vinculo.getQtdCriancas(),
        vinculo.getQtdAdolescentes(),
        vinculo.getQtdIdosos(),
        vinculo.getQtdPessoasDeficiencia(),
        vinculo.getRendaFamiliarTotal(),
        vinculo.getRendaPerCapita(),
        vinculo.getFaixaRendaPerCapita(),
        vinculo.getPrincipaisFontesRenda(),
        vinculo.getSituacaoInsegurancaAlimentar(),
        vinculo.getPossuiDividasRelevantes(),
        vinculo.getDescricaoDividas(),
        vinculo.getVulnerabilidadesFamilia(),
        vinculo.getServicosAcompanhamento(),
        vinculo.getTecnicoResponsavel(),
        vinculo.getPeriodicidadeAtendimento(),
        vinculo.getProximaVisitaPrevista(),
        vinculo.getObservacoes(),
        vinculo.getMembros() == null
            ? java
                .util.Collections.emptyList()
            : vinculo.getMembros().stream().map(VinculoFamiliarMapper::toMembroResponse).collect(Collectors.toList()),
        vinculo.getCriadoEm(),
        vinculo.getAtualizadoEm());
  }

  private static void aplicarDados(VinculoFamiliar vinculo, VinculoFamiliarCriacaoRequest request) {
    vinculo.setNomeFamilia(limparTexto(request.getNomeFamilia()));
    vinculo.setStatus(normalizarStatus(request.getStatus()));
    vinculo.setCep(limparTexto(request.getCep()));
    vinculo.setLogradouro(limparTexto(request.getLogradouro()));
    vinculo.setNumero(limparTexto(request.getNumero()));
    vinculo.setComplemento(limparTexto(request.getComplemento()));
    vinculo.setBairro(limparTexto(request.getBairro()));
    vinculo.setPontoReferencia(limparTexto(request.getPontoReferencia()));
    vinculo.setMunicipio(limparTexto(request.getMunicipio()));
    vinculo.setUf(limparTexto(request.getUf()));
    vinculo.setZona(limparTexto(request.getZona()));
    vinculo.setSituacaoImovel(limparTexto(request.getSituacaoImovel()));
    vinculo.setTipoMoradia(limparTexto(request.getTipoMoradia()));
    vinculo.setAguaEncanada(Boolean.TRUE.equals(request.getAguaEncanada()));
    vinculo.setEsgotoTipo(limparTexto(request.getEsgotoTipo()));
    vinculo.setColetaLixo(limparTexto(request.getColetaLixo()));
    vinculo.setEnergiaEletrica(Boolean.TRUE.equals(request.getEnergiaEletrica()));
    vinculo.setInternet(Boolean.TRUE.equals(request.getInternet()));
    vinculo.setArranjoFamiliar(limparTexto(request.getArranjoFamiliar()));
    vinculo.setQtdMembros(request.getQtdMembros());
    vinculo.setQtdCriancas(request.getQtdCriancas());
    vinculo.setQtdAdolescentes(request.getQtdAdolescentes());
    vinculo.setQtdIdosos(request.getQtdIdosos());
    vinculo.setQtdPessoasDeficiencia(request.getQtdPessoasDeficiencia());
    vinculo.setRendaFamiliarTotal(limparTexto(request.getRendaFamiliarTotal()));
    vinculo.setRendaPerCapita(limparTexto(request.getRendaPerCapita()));
    vinculo.setFaixaRendaPerCapita(limparTexto(request.getFaixaRendaPerCapita()));
    vinculo.setPrincipaisFontesRenda(limparTexto(request.getPrincipaisFontesRenda()));
    vinculo.setSituacaoInsegurancaAlimentar(limparTexto(request.getSituacaoInsegurancaAlimentar()));
    vinculo.setPossuiDividasRelevantes(Boolean.TRUE.equals(request.getPossuiDividasRelevantes()));
    vinculo.setDescricaoDividas(limparTexto(request.getDescricaoDividas()));
    vinculo.setVulnerabilidadesFamilia(limparTexto(request.getVulnerabilidadesFamilia()));
    vinculo.setServicosAcompanhamento(limparTexto(request.getServicosAcompanhamento()));
    vinculo.setTecnicoResponsavel(limparTexto(request.getTecnicoResponsavel()));
    vinculo.setPeriodicidadeAtendimento(limparTexto(request.getPeriodicidadeAtendimento()));
    vinculo.setProximaVisitaPrevista(request.getProximaVisitaPrevista());
    vinculo.setObservacoes(limparTexto(request.getObservacoes()));
  }

  private static void aplicarDadosMembro(
      VinculoFamiliarMembro membro,
      VinculoFamiliar vinculo,
      CadastroBeneficiario beneficiario,
      VinculoFamiliarMembroRequest request) {
    membro.setVinculoFamiliar(vinculo);
    membro.setBeneficiario(beneficiario);
    membro.setParentesco(limparTexto(request.getParentesco()));
    membro.setResponsavelFamiliar(Boolean.TRUE.equals(request.getResponsavelFamiliar()));
    membro.setContribuiRenda(Boolean.TRUE.equals(request.getContribuiRenda()));
    membro.setRendaIndividual(limparTexto(request.getRendaIndividual()));
    membro.setParticipaServicos(Boolean.TRUE.equals(request.getParticipaServicos()));
    membro.setObservacoes(limparTexto(request.getObservacoes()));
    membro.setUsaEnderecoFamilia(Boolean.TRUE.equals(request.getUsaEnderecoFamilia()));
  }

  private static String limparTexto(String value) {
    if (value == null) {
      return null;
    }
    String trimmed = value.trim();
    return trimmed.isEmpty() ? null : trimmed;
  }

  private static String normalizarStatus(String status) {
    String normalized = limparTexto(status);
    return normalized != null ? normalized : STATUS_PADRAO;
  }

  private static VinculoFamiliarMembroResponse toMembroResponse(VinculoFamiliarMembro membro) {
    CadastroBeneficiario beneficiario = membro.getBeneficiario();
    return new VinculoFamiliarMembroResponse(
        membro.getId(),
        beneficiario != null ? beneficiario.getId() : null,
        membro.getParentesco(),
        membro.getResponsavelFamiliar(),
        membro.getContribuiRenda(),
        membro.getRendaIndividual(),
        membro.getParticipaServicos(),
        membro.getObservacoes(),
        membro.getUsaEnderecoFamilia(),
        toResumo(beneficiario));
  }

  private static BeneficiarioResumoResponse toResumo(CadastroBeneficiario beneficiario) {
    if (beneficiario == null) {
      return null;
    }
    String telefone = obterTelefone(beneficiario.getContatos());
    String cpf = obterCpf(beneficiario.getDocumentos());
    Endereco endereco = beneficiario.getEndereco();
    String bairro = endereco != null ? endereco.getBairro() : null;
    return new BeneficiarioResumoResponse(
        beneficiario.getId(),
        beneficiario.getCodigo(),
        beneficiario.getNomeCompleto(),
        beneficiario.getNomeSocial(),
        cpf,
        telefone,
        bairro,
        beneficiario.getDataNascimento());
  }

  private static String obterTelefone(List<ContatoBeneficiario> contatos) {
    if (contatos == null || contatos.isEmpty()) {
      return null;
    }
    return contatos.stream()
        .filter((contato) -> contato.getTelefonePrincipal() != null)
        .max(Comparator.comparing(ContatoBeneficiario::getAtualizadoEm))
        .map(ContatoBeneficiario::getTelefonePrincipal)
        .orElse(null);
  }

  private static String obterCpf(List<DocumentoBeneficiario> documentos) {
    if (documentos == null || documentos.isEmpty()) {
      return null;
    }
    return documentos.stream()
        .filter((doc) -> doc.getNumeroDocumento() != null)
        .filter((doc) -> {
          String tipo = doc.getTipoDocumento();
          return tipo != null && tipo.trim().equalsIgnoreCase("CPF");
        })
        .max(Comparator.comparing(DocumentoBeneficiario::getAtualizadoEm))
        .map(DocumentoBeneficiario::getNumeroDocumento)
        .orElse(null);
  }
}
