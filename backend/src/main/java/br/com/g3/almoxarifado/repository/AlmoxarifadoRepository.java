package br.com.g3.almoxarifado.repository;

import br.com.g3.almoxarifado.domain.AlmoxarifadoItem;
import br.com.g3.almoxarifado.domain.AlmoxarifadoMovimentacao;
import java.util.List;
import java.util.Optional;

public interface AlmoxarifadoRepository {
  List<AlmoxarifadoItem> listarItens();

  Optional<AlmoxarifadoItem> buscarItemPorId(Long id);

  Optional<AlmoxarifadoItem> buscarItemPorCodigo(String codigo);

  Optional<AlmoxarifadoItem> buscarItemPorCodigoBarras(String codigoBarras);

  Optional<AlmoxarifadoItem> buscarItemDuplicado(
      String descricao,
      String categoria,
      String unidade,
      String localizacao,
      String localizacaoInterna);

  AlmoxarifadoItem salvarItem(AlmoxarifadoItem item);

  List<AlmoxarifadoMovimentacao> listarMovimentacoes();

  AlmoxarifadoMovimentacao salvarMovimentacao(AlmoxarifadoMovimentacao movimentacao);

  int obterProximoCodigo();
}
