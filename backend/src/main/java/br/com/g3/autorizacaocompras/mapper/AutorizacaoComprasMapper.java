package br.com.g3.autorizacaocompras.mapper;

import br.com.g3.autorizacaocompras.domain.AutorizacaoCompra;
import br.com.g3.autorizacaocompras.dto.AutorizacaoCompraRequest;
import br.com.g3.autorizacaocompras.dto.AutorizacaoCompraResponse;
import java.time.LocalDateTime;

public final class AutorizacaoComprasMapper {
  private AutorizacaoComprasMapper() {}

  public static AutorizacaoCompra toDomain(AutorizacaoCompraRequest request) {
    AutorizacaoCompra compra = new AutorizacaoCompra();
    compra.setTitulo(request.getTitulo());
    compra.setTipo(request.getTipo());
    compra.setArea(request.getArea());
    compra.setResponsavel(request.getResponsavel());
    compra.setDataPrevista(request.getDataPrevista());
    compra.setValor(request.getValor());
    compra.setQuantidadeItens(request.getQuantidadeItens() != null ? request.getQuantidadeItens() : 1);
    compra.setJustificativa(request.getJustificativa());
    compra.setCentroCusto(request.getCentroCusto());
    compra.setPrioridade(request.getPrioridade() != null ? request.getPrioridade() : "normal");
    compra.setStatus(request.getStatus());
    compra.setAprovador(request.getAprovador());
    compra.setDecisao(request.getDecisao());
    compra.setObservacoesAprovacao(request.getObservacoesAprovacao());
    compra.setDataAprovacao(request.getDataAprovacao());
    compra.setDispensarCotacao(request.getDispensarCotacao() != null ? request.getDispensarCotacao() : false);
    compra.setMotivoDispensa(request.getMotivoDispensa());
    compra.setVencedor(request.getVencedor());
    compra.setRegistroPatrimonio(request.getRegistroPatrimonio() != null ? request.getRegistroPatrimonio() : false);
    compra.setRegistroAlmoxarifado(request.getRegistroAlmoxarifado() != null ? request.getRegistroAlmoxarifado() : false);
    compra.setNumeroReserva(request.getNumeroReserva());
    compra.setNumeroTermo(request.getNumeroTermo());
    compra.setAutorizacaoPagamentoNumero(request.getAutorizacaoPagamentoNumero());
    compra.setAutorizacaoPagamentoAutor(request.getAutorizacaoPagamentoAutor());
    compra.setAutorizacaoPagamentoData(request.getAutorizacaoPagamentoData());
    compra.setAutorizacaoPagamentoObservacoes(request.getAutorizacaoPagamentoObservacoes());
    return compra;
  }

  public static AutorizacaoCompraResponse toResponse(AutorizacaoCompra domain) {
    AutorizacaoCompraResponse response = new AutorizacaoCompraResponse();
    response.setId(domain.getId());
    response.setTitulo(domain.getTitulo());
    response.setTipo(domain.getTipo());
    response.setArea(domain.getArea());
    response.setResponsavel(domain.getResponsavel());
    response.setDataPrevista(domain.getDataPrevista());
    response.setValor(domain.getValor());
    response.setJustificativa(domain.getJustificativa());
    response.setCentroCusto(domain.getCentroCusto());
    response.setStatus(domain.getStatus());
    response.setAprovador(domain.getAprovador());
    response.setDecisao(domain.getDecisao());
    response.setObservacoesAprovacao(domain.getObservacoesAprovacao());
    response.setDataAprovacao(domain.getDataAprovacao());
    response.setDispensarCotacao(domain.getDispensarCotacao());
    response.setMotivoDispensa(domain.getMotivoDispensa());
    response.setVencedor(domain.getVencedor());
    response.setRegistroPatrimonio(domain.getRegistroPatrimonio());
    response.setRegistroAlmoxarifado(domain.getRegistroAlmoxarifado());
    response.setNumeroReserva(domain.getNumeroReserva());
    response.setNumeroTermo(domain.getNumeroTermo());
    response.setAutorizacaoPagamentoNumero(domain.getAutorizacaoPagamentoNumero());
    response.setAutorizacaoPagamentoAutor(domain.getAutorizacaoPagamentoAutor());
    response.setAutorizacaoPagamentoData(domain.getAutorizacaoPagamentoData());
    response.setAutorizacaoPagamentoObservacoes(domain.getAutorizacaoPagamentoObservacoes());
    response.setQuantidadeItens(domain.getQuantidadeItens());
    response.setPrioridade(domain.getPrioridade());
    response.setCriadoEm(domain.getCriadoEm());
    response.setAtualizadoEm(domain.getAtualizadoEm());
    return response;
  }

  public static void aplicarAtualizacao(AutorizacaoCompra domain, AutorizacaoCompraRequest request) {
    domain.setTitulo(request.getTitulo());
    domain.setTipo(request.getTipo());
    domain.setArea(request.getArea());
    domain.setResponsavel(request.getResponsavel());
    domain.setDataPrevista(request.getDataPrevista());
    domain.setValor(request.getValor());
    domain.setQuantidadeItens(request.getQuantidadeItens() != null ? request.getQuantidadeItens() : 1);
    domain.setJustificativa(request.getJustificativa());
    domain.setCentroCusto(request.getCentroCusto());
    domain.setPrioridade(request.getPrioridade() != null ? request.getPrioridade() : "normal");
    domain.setStatus(request.getStatus());
    domain.setAprovador(request.getAprovador());
    domain.setDecisao(request.getDecisao());
    domain.setObservacoesAprovacao(request.getObservacoesAprovacao());
    domain.setDataAprovacao(request.getDataAprovacao());
    domain.setDispensarCotacao(request.getDispensarCotacao() != null ? request.getDispensarCotacao() : false);
    domain.setMotivoDispensa(request.getMotivoDispensa());
    domain.setVencedor(request.getVencedor());
    domain.setRegistroPatrimonio(request.getRegistroPatrimonio() != null ? request.getRegistroPatrimonio() : false);
    domain.setRegistroAlmoxarifado(request.getRegistroAlmoxarifado() != null ? request.getRegistroAlmoxarifado() : false);
    domain.setNumeroReserva(request.getNumeroReserva());
    domain.setNumeroTermo(request.getNumeroTermo());
    domain.setAutorizacaoPagamentoNumero(request.getAutorizacaoPagamentoNumero());
    domain.setAutorizacaoPagamentoAutor(request.getAutorizacaoPagamentoAutor());
    domain.setAutorizacaoPagamentoData(request.getAutorizacaoPagamentoData());
    domain.setAutorizacaoPagamentoObservacoes(request.getAutorizacaoPagamentoObservacoes());
  }
}
