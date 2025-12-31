package br.com.g3.patrimonio.mapper;

import br.com.g3.patrimonio.domain.PatrimonioItem;
import br.com.g3.patrimonio.domain.PatrimonioMovimentacao;
import br.com.g3.patrimonio.dto.PatrimonioMovimentoResponse;
import br.com.g3.patrimonio.dto.PatrimonioRequest;
import br.com.g3.patrimonio.dto.PatrimonioResponse;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class PatrimonioMapper {
  private PatrimonioMapper() {}

  public static PatrimonioItem toDomain(PatrimonioRequest request) {
    PatrimonioItem item = new PatrimonioItem();
    apply(item, request);
    return item;
  }

  public static void apply(PatrimonioItem item, PatrimonioRequest request) {
    item.setNumeroPatrimonio(request.getNumeroPatrimonio());
    item.setNome(request.getNome());
    item.setCategoria(request.getCategoria());
    item.setSubcategoria(request.getSubcategoria());
    item.setConservacao(request.getConservacao());
    item.setStatus(request.getStatus());
    item.setDataAquisicao(request.getDataAquisicao());
    item.setValorAquisicao(request.getValorAquisicao());
    item.setOrigem(request.getOrigem());
    item.setResponsavel(request.getResponsavel());
    item.setUnidade(request.getUnidade());
    item.setSala(request.getSala());
    item.setTaxaDepreciacao(request.getTaxaDepreciacao());
    item.setObservacoes(request.getObservacoes());
  }

  public static PatrimonioResponse toResponse(PatrimonioItem item) {
    PatrimonioResponse response = new PatrimonioResponse();
    response.setIdPatrimonio(item.getId());
    response.setNumeroPatrimonio(item.getNumeroPatrimonio());
    response.setNome(item.getNome());
    response.setCategoria(item.getCategoria());
    response.setSubcategoria(item.getSubcategoria());
    response.setConservacao(item.getConservacao());
    response.setStatus(item.getStatus());
    response.setDataAquisicao(item.getDataAquisicao());
    response.setValorAquisicao(item.getValorAquisicao());
    response.setOrigem(item.getOrigem());
    response.setResponsavel(item.getResponsavel());
    response.setUnidade(item.getUnidade());
    response.setSala(item.getSala());
    response.setTaxaDepreciacao(item.getTaxaDepreciacao());
    response.setObservacoes(item.getObservacoes());
    response.setCriadoEm(item.getCriadoEm());
    response.setAtualizadoEm(item.getAtualizadoEm());
    response.setMovimentos(toMovimentos(item.getMovimentos()));
    return response;
  }

  private static List<PatrimonioMovimentoResponse> toMovimentos(
      List<PatrimonioMovimentacao> movimentos) {
    return movimentos.stream()
        .sorted(Comparator.comparing(PatrimonioMovimentacao::getDataMovimento).reversed())
        .map(PatrimonioMapper::toMovimentoResponse)
        .collect(Collectors.toList());
  }

  private static PatrimonioMovimentoResponse toMovimentoResponse(PatrimonioMovimentacao movimento) {
    PatrimonioMovimentoResponse response = new PatrimonioMovimentoResponse();
    response.setIdMovimento(movimento.getId());
    response.setTipo(movimento.getTipo());
    response.setDestino(movimento.getDestino());
    response.setResponsavel(movimento.getResponsavel());
    response.setObservacao(movimento.getObservacao());
    response.setDataMovimento(movimento.getDataMovimento());
    return response;
  }
}
