package br.com.g3.rh.mapper;

import br.com.g3.rh.domain.RhConfiguracaoPonto;
import br.com.g3.rh.domain.RhPontoDia;
import br.com.g3.rh.domain.RhPontoMarcacao;
import br.com.g3.rh.dto.RhConfiguracaoPontoResponse;
import br.com.g3.rh.dto.RhPontoDiaResponse;
import br.com.g3.rh.dto.RhPontoMarcacaoResponse;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RhPontoMapper {
  private static final DateTimeFormatter HORA_FORMATO = DateTimeFormatter.ofPattern("HH:mm");

  public RhConfiguracaoPontoResponse toConfiguracaoResponse(RhConfiguracaoPonto configuracao) {
    RhConfiguracaoPontoResponse response = new RhConfiguracaoPontoResponse();
    response.setId(configuracao.getId());
    response.setCargaSemanalMinutos(configuracao.getCargaSemanalMinutos());
    response.setCargaSegQuiMinutos(configuracao.getCargaSegQuiMinutos());
    response.setCargaSextaMinutos(configuracao.getCargaSextaMinutos());
    response.setCargaSabadoMinutos(configuracao.getCargaSabadoMinutos());
    response.setCargaDomingoMinutos(configuracao.getCargaDomingoMinutos());
    response.setToleranciaMinutos(configuracao.getToleranciaMinutos());
    response.setAtualizadoEm(configuracao.getAtualizadoEm());
    return response;
  }

  public RhPontoDiaResponse toPontoDiaResponse(RhPontoDia pontoDia) {
    RhPontoDiaResponse response = new RhPontoDiaResponse();
    response.setId(pontoDia.getId());
    response.setFuncionarioId(pontoDia.getFuncionarioId());
    response.setData(pontoDia.getData());
    response.setOcorrencia(pontoDia.getOcorrencia());
    response.setObservacoes(pontoDia.getObservacoes());
    response.setCargaPrevistaMinutos(pontoDia.getCargaPrevistaMinutos());
    response.setToleranciaMinutos(pontoDia.getToleranciaMinutos());
    response.setTotalTrabalhadoMinutos(pontoDia.getTotalTrabalhadoMinutos());
    response.setExtrasMinutos(pontoDia.getExtrasMinutos());
    response.setFaltasAtrasosMinutos(pontoDia.getFaltasAtrasosMinutos());
    response.setMarcacoes(mapMarcacoes(pontoDia.getMarcacoes()));
    return response;
  }

  public List<RhPontoMarcacaoResponse> mapMarcacoes(List<RhPontoMarcacao> marcacoes) {
    List<RhPontoMarcacaoResponse> resposta = new ArrayList<>();
    if (marcacoes == null) {
      return resposta;
    }
    marcacoes.stream()
        .sorted(Comparator.comparing(RhPontoMarcacao::getDataHoraServidor))
        .forEach(marcacao -> {
          RhPontoMarcacaoResponse response = new RhPontoMarcacaoResponse();
          response.setId(marcacao.getId());
          response.setTipo(marcacao.getTipo());
          response.setDataHoraServidor(marcacao.getDataHoraServidor());
          response.setLatitude(marcacao.getLatitude());
          response.setLongitude(marcacao.getLongitude());
          response.setAccuracy(marcacao.getAccuracy());
          response.setDistanciaMetros(marcacao.getDistanciaMetros());
          response.setDentroPerimetro(marcacao.isDentroPerimetro());
          resposta.add(response);
        });
    return resposta;
  }

  public static String formatarHora(RhPontoMarcacao marcacao) {
    if (marcacao == null || marcacao.getDataHoraServidor() == null) {
      return "";
    }
    return marcacao.getDataHoraServidor().format(HORA_FORMATO);
  }
}
