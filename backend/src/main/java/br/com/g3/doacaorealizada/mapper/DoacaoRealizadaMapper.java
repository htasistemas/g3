package br.com.g3.doacaorealizada.mapper;

import br.com.g3.almoxarifado.domain.AlmoxarifadoItem;
import br.com.g3.doacaorealizada.domain.DoacaoRealizada;
import br.com.g3.doacaorealizada.domain.DoacaoRealizadaItem;
import br.com.g3.doacaorealizada.dto.DoacaoRealizadaItemRequest;
import br.com.g3.doacaorealizada.dto.DoacaoRealizadaItemResponse;
import br.com.g3.doacaorealizada.dto.DoacaoRealizadaRequest;
import br.com.g3.doacaorealizada.dto.DoacaoRealizadaResponse;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DoacaoRealizadaMapper {
  private DoacaoRealizadaMapper() {}

  public static DoacaoRealizada toDomain(DoacaoRealizadaRequest request) {
    DoacaoRealizada doacao = new DoacaoRealizada();
    aplicarDados(doacao, request);
    LocalDateTime agora = LocalDateTime.now();
    doacao.setCriadoEm(agora);
    doacao.setAtualizadoEm(agora);
    return doacao;
  }

  public static void aplicarDados(DoacaoRealizada doacao, DoacaoRealizadaRequest request) {
    doacao.setTipoDoacao(limparTexto(request.getTipoDoacao()));
    doacao.setSituacao(limparTexto(request.getSituacao()));
    doacao.setResponsavel(limparTexto(request.getResponsavel()));
    doacao.setObservacoes(limparTexto(request.getObservacoes()));
    doacao.setDataDoacao(request.getDataDoacao());
    doacao.setAtualizadoEm(LocalDateTime.now());
  }

  public static DoacaoRealizadaItem toItemDomain(
      DoacaoRealizadaItemRequest request, DoacaoRealizada doacao, AlmoxarifadoItem item) {
    DoacaoRealizadaItem itemDoacao = new DoacaoRealizadaItem();
    itemDoacao.setDoacaoRealizada(doacao);
    itemDoacao.setItem(item);
    itemDoacao.setQuantidade(request.getQuantidade());
    itemDoacao.setObservacoes(limparTexto(request.getObservacoes()));
    itemDoacao.setCriadoEm(LocalDateTime.now());
    return itemDoacao;
  }

  public static DoacaoRealizadaResponse toResponse(DoacaoRealizada doacao) {
    String nomeBeneficiario = null;
    if (doacao.getBeneficiario() != null) {
      nomeBeneficiario =
          doacao.getBeneficiario().getNomeCompleto() != null
              ? doacao.getBeneficiario().getNomeCompleto()
              : doacao.getBeneficiario().getNomeSocial();
    }
    String nomeFamilia = doacao.getVinculoFamiliar() != null ? doacao.getVinculoFamiliar().getNomeFamilia() : null;

    return new DoacaoRealizadaResponse(
        doacao.getId(),
        doacao.getBeneficiario() != null ? doacao.getBeneficiario().getId() : null,
        doacao.getVinculoFamiliar() != null ? doacao.getVinculoFamiliar().getId() : null,
        nomeBeneficiario,
        nomeFamilia,
        doacao.getTipoDoacao(),
        doacao.getSituacao(),
        doacao.getResponsavel(),
        doacao.getObservacoes(),
        doacao.getDataDoacao(),
        toItemResponses(doacao.getItens()));
  }

  private static List<DoacaoRealizadaItemResponse> toItemResponses(List<DoacaoRealizadaItem> itens) {
    if (itens == null) {
      return Collections.emptyList();
    }
    return itens.stream().map(DoacaoRealizadaMapper::toItemResponse).collect(Collectors.toList());
  }

  private static DoacaoRealizadaItemResponse toItemResponse(DoacaoRealizadaItem item) {
    AlmoxarifadoItem almoxarifadoItem = item.getItem();
    return new DoacaoRealizadaItemResponse(
        item.getId(),
        almoxarifadoItem != null ? almoxarifadoItem.getId() : null,
        almoxarifadoItem != null ? almoxarifadoItem.getCodigo() : null,
        almoxarifadoItem != null ? almoxarifadoItem.getDescricao() : null,
        almoxarifadoItem != null ? almoxarifadoItem.getUnidade() : null,
        item.getQuantidade(),
        item.getObservacoes());
  }

  private static String limparTexto(String valor) {
    if (valor == null) {
      return null;
    }
    String trimmed = valor.trim();
    return trimmed.isEmpty() ? null : trimmed;
  }
}
