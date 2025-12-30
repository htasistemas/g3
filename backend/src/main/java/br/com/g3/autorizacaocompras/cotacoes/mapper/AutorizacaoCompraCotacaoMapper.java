package br.com.g3.autorizacaocompras.cotacoes.mapper;

import br.com.g3.autorizacaocompras.cotacoes.domain.AutorizacaoCompraCotacao;
import br.com.g3.autorizacaocompras.cotacoes.dto.AutorizacaoCompraCotacaoRequest;
import br.com.g3.autorizacaocompras.cotacoes.dto.AutorizacaoCompraCotacaoResponse;
import br.com.g3.autorizacaocompras.domain.AutorizacaoCompra;

public final class AutorizacaoCompraCotacaoMapper {
  private AutorizacaoCompraCotacaoMapper() {}

  public static AutorizacaoCompraCotacao toDomain(
      AutorizacaoCompraCotacaoRequest request, AutorizacaoCompra compra) {
    AutorizacaoCompraCotacao cotacao = new AutorizacaoCompraCotacao();
    cotacao.setAutorizacaoCompra(compra);
    cotacao.setFornecedor(request.getFornecedor());
    cotacao.setRazaoSocial(request.getRazaoSocial());
    cotacao.setCnpj(request.getCnpj());
    cotacao.setValor(request.getValor());
    cotacao.setPrazoEntrega(request.getPrazoEntrega());
    cotacao.setValidade(request.getValidade());
    cotacao.setConformidade(request.getConformidade());
    cotacao.setObservacoes(request.getObservacoes());
    cotacao.setOrcamentoFisicoNome(request.getOrcamentoFisicoNome());
    cotacao.setOrcamentoFisicoTipo(request.getOrcamentoFisicoTipo());
    cotacao.setOrcamentoFisicoConteudo(request.getOrcamentoFisicoConteudo());
    return cotacao;
  }

  public static AutorizacaoCompraCotacaoResponse toResponse(AutorizacaoCompraCotacao cotacao) {
    AutorizacaoCompraCotacaoResponse response = new AutorizacaoCompraCotacaoResponse();
    response.setId(cotacao.getId());
    response.setAutorizacaoCompraId(
        cotacao.getAutorizacaoCompra() != null ? cotacao.getAutorizacaoCompra().getId() : null);
    response.setFornecedor(cotacao.getFornecedor());
    response.setRazaoSocial(cotacao.getRazaoSocial());
    response.setCnpj(cotacao.getCnpj());
    response.setValor(cotacao.getValor());
    response.setPrazoEntrega(cotacao.getPrazoEntrega());
    response.setValidade(cotacao.getValidade());
    response.setConformidade(cotacao.getConformidade());
    response.setObservacoes(cotacao.getObservacoes());
    response.setOrcamentoFisicoNome(cotacao.getOrcamentoFisicoNome());
    response.setOrcamentoFisicoTipo(cotacao.getOrcamentoFisicoTipo());
    response.setOrcamentoFisicoConteudo(cotacao.getOrcamentoFisicoConteudo());
    response.setCriadoEm(cotacao.getCriadoEm());
    return response;
  }
}
