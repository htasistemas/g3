package br.com.g3.doacaoplanejada.mapper;

import br.com.g3.almoxarifado.domain.AlmoxarifadoItem;
import br.com.g3.cadastrobeneficiario.domain.CadastroBeneficiario;
import br.com.g3.doacaoplanejada.domain.DoacaoPlanejada;
import br.com.g3.doacaoplanejada.dto.DoacaoPlanejadaRequest;
import br.com.g3.doacaoplanejada.dto.DoacaoPlanejadaResponse;
import br.com.g3.vinculofamiliar.domain.VinculoFamiliar;
import java.time.LocalDateTime;

public class DoacaoPlanejadaMapper {
  private DoacaoPlanejadaMapper() {}

  public static DoacaoPlanejada toDomain(
      DoacaoPlanejadaRequest request,
      CadastroBeneficiario beneficiario,
      VinculoFamiliar vinculoFamiliar,
      AlmoxarifadoItem item) {
    DoacaoPlanejada doacao = new DoacaoPlanejada();
    aplicarDados(doacao, request, beneficiario, vinculoFamiliar, item);
    LocalDateTime agora = LocalDateTime.now();
    doacao.setCriadoEm(agora);
    doacao.setAtualizadoEm(agora);
    return doacao;
  }

  public static void aplicarDados(
      DoacaoPlanejada doacao,
      DoacaoPlanejadaRequest request,
      CadastroBeneficiario beneficiario,
      VinculoFamiliar vinculoFamiliar,
      AlmoxarifadoItem item) {
    doacao.setBeneficiario(beneficiario);
    doacao.setVinculoFamiliar(vinculoFamiliar);
    doacao.setItem(item);
    doacao.setQuantidade(request.getQuantidade());
    doacao.setDataPrevista(request.getDataPrevista());
    doacao.setPrioridade(request.getPrioridade());
    doacao.setStatus(request.getStatus());
    doacao.setObservacoes(limparTexto(request.getObservacoes()));
    doacao.setMotivoCancelamento(limparTexto(request.getMotivoCancelamento()));
    doacao.setAtualizadoEm(LocalDateTime.now());
  }

  public static DoacaoPlanejadaResponse toResponse(DoacaoPlanejada doacao) {
    Long beneficiarioId = doacao.getBeneficiario() != null ? doacao.getBeneficiario().getId() : null;
    Long vinculoId = doacao.getVinculoFamiliar() != null ? doacao.getVinculoFamiliar().getId() : null;
    AlmoxarifadoItem item = doacao.getItem();
    return new DoacaoPlanejadaResponse(
        doacao.getId(),
        beneficiarioId,
        vinculoId,
        item != null ? item.getCodigo() : null,
        item != null ? item.getDescricao() : null,
        item != null ? item.getUnidade() : null,
        doacao.getQuantidade(),
        doacao.getDataPrevista(),
        doacao.getPrioridade(),
        doacao.getStatus(),
        doacao.getObservacoes(),
        doacao.getMotivoCancelamento());
  }

  private static String limparTexto(String valor) {
    if (valor == null) {
      return null;
    }
    String normalizado = valor.trim();
    return normalizado.isEmpty() ? null : normalizado;
  }
}
