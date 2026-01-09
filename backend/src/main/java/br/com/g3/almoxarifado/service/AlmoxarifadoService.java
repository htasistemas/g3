package br.com.g3.almoxarifado.service;

import br.com.g3.almoxarifado.dto.AlmoxarifadoItemCriacaoRequest;
import br.com.g3.almoxarifado.dto.AlmoxarifadoItemResponse;
import br.com.g3.almoxarifado.dto.AlmoxarifadoMovimentacaoCadastroResponse;
import br.com.g3.almoxarifado.dto.AlmoxarifadoMovimentacaoRequest;
import br.com.g3.almoxarifado.dto.AlmoxarifadoMovimentacaoResponse;
import br.com.g3.almoxarifado.dto.MovimentacaoKitVinculoResponse;
import br.com.g3.almoxarifado.dto.ProdutoKitComposicaoRequest;
import br.com.g3.almoxarifado.dto.ProdutoKitComposicaoResponse;
import java.util.List;

public interface AlmoxarifadoService {
  List<AlmoxarifadoItemResponse> listarItens();

  String obterProximoCodigo();

  AlmoxarifadoItemResponse criarItem(AlmoxarifadoItemCriacaoRequest request);

  AlmoxarifadoItemResponse atualizarItem(Long id, AlmoxarifadoItemCriacaoRequest request);

  List<AlmoxarifadoMovimentacaoResponse> listarMovimentacoes();

  AlmoxarifadoMovimentacaoCadastroResponse registrarMovimentacao(
      AlmoxarifadoMovimentacaoRequest request);

  List<ProdutoKitComposicaoResponse> listarComposicaoKit(Long produtoKitId);

  List<ProdutoKitComposicaoResponse> atualizarComposicaoKit(
      Long produtoKitId, List<ProdutoKitComposicaoRequest> itens);

  List<MovimentacaoKitVinculoResponse> listarVinculosKit(Long movimentacaoPrincipalId);
}
