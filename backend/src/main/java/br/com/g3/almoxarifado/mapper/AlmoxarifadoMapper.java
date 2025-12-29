package br.com.g3.almoxarifado.mapper;

import br.com.g3.almoxarifado.domain.AlmoxarifadoItem;
import br.com.g3.almoxarifado.domain.AlmoxarifadoMovimentacao;
import br.com.g3.almoxarifado.dto.AlmoxarifadoItemCriacaoRequest;
import br.com.g3.almoxarifado.dto.AlmoxarifadoItemResponse;
import br.com.g3.almoxarifado.dto.AlmoxarifadoMovimentacaoRequest;
import br.com.g3.almoxarifado.dto.AlmoxarifadoMovimentacaoResponse;
import java.time.LocalDateTime;

public class AlmoxarifadoMapper {
  private AlmoxarifadoMapper() {}

  public static AlmoxarifadoItem toDomain(AlmoxarifadoItemCriacaoRequest request) {
    AlmoxarifadoItem item = new AlmoxarifadoItem();
    aplicarItem(item, request);
    LocalDateTime agora = LocalDateTime.now();
    item.setCriadoEm(agora);
    item.setAtualizadoEm(agora);
    return item;
  }

  public static void aplicarItem(AlmoxarifadoItem item, AlmoxarifadoItemCriacaoRequest request) {
    item.setCodigo(limparTexto(request.getCodigo()));
    item.setCodigoBarras(limparTexto(request.getCodigoBarras()));
    item.setDescricao(limparTexto(request.getDescricao()));
    item.setCategoria(limparTexto(request.getCategoria()));
    item.setUnidade(limparTexto(request.getUnidade()));
    item.setLocalizacao(limparTexto(request.getLocalizacao()));
    item.setLocalizacaoInterna(limparTexto(request.getLocalizacaoInterna()));
    item.setEstoqueAtual(request.getEstoqueAtual());
    item.setEstoqueMinimo(request.getEstoqueMinimo());
    item.setValorUnitario(request.getValorUnitario());
    item.setSituacao(limparTexto(request.getSituacao()));
    item.setValidade(request.getValidade());
    item.setIgnorarValidade(Boolean.TRUE.equals(request.getIgnorarValidade()));
    item.setObservacoes(limparTexto(request.getObservacoes()));
    item.setAtualizadoEm(LocalDateTime.now());
  }

  public static AlmoxarifadoItemResponse toResponse(AlmoxarifadoItem item) {
    return new AlmoxarifadoItemResponse(
        item.getId(),
        item.getCodigo(),
        item.getCodigoBarras(),
        item.getDescricao(),
        item.getCategoria(),
        item.getUnidade(),
        item.getLocalizacao(),
        item.getLocalizacaoInterna(),
        item.getEstoqueAtual(),
        item.getEstoqueMinimo(),
        item.getValorUnitario(),
        item.getSituacao(),
        item.getValidade(),
        item.getIgnorarValidade(),
        item.getObservacoes());
  }

  public static AlmoxarifadoMovimentacao toMovimentacao(
      AlmoxarifadoMovimentacaoRequest request, AlmoxarifadoItem item, int saldoApos) {
    AlmoxarifadoMovimentacao movimento = new AlmoxarifadoMovimentacao();
    movimento.setItem(item);
    movimento.setDataMovimentacao(request.getDataMovimentacao());
    movimento.setTipo(limparTexto(request.getTipo()));
    movimento.setQuantidade(request.getQuantidade());
    movimento.setSaldoApos(saldoApos);
    movimento.setReferencia(limparTexto(request.getReferencia()));
    movimento.setResponsavel(limparTexto(request.getResponsavel()));
    movimento.setObservacoes(limparTexto(request.getObservacoes()));
    movimento.setDirecaoAjuste(limparTexto(request.getDirecaoAjuste()));
    movimento.setCriadoEm(LocalDateTime.now());
    return movimento;
  }

  public static AlmoxarifadoMovimentacaoResponse toMovimentacaoResponse(
      AlmoxarifadoMovimentacao movimento) {
    AlmoxarifadoItem item = movimento.getItem();
    return new AlmoxarifadoMovimentacaoResponse(
        movimento.getId(),
        movimento.getDataMovimentacao(),
        movimento.getTipo(),
        item != null ? item.getCodigo() : null,
        item != null ? item.getDescricao() : null,
        movimento.getQuantidade(),
        movimento.getSaldoApos(),
        movimento.getReferencia(),
        movimento.getResponsavel(),
        movimento.getObservacoes());
  }

  private static String limparTexto(String valor) {
    if (valor == null) {
      return null;
    }
    String trimmed = valor.trim();
    return trimmed.isEmpty() ? null : trimmed;
  }
}
