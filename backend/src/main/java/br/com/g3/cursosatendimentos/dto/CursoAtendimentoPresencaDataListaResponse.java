package br.com.g3.cursosatendimentos.dto;

import java.util.ArrayList;
import java.util.List;

public class CursoAtendimentoPresencaDataListaResponse {
  private List<CursoAtendimentoPresencaDataResponse> datas = new ArrayList<>();

  public CursoAtendimentoPresencaDataListaResponse() {}

  public CursoAtendimentoPresencaDataListaResponse(List<CursoAtendimentoPresencaDataResponse> datas) {
    this.datas = datas;
  }

  public List<CursoAtendimentoPresencaDataResponse> getDatas() {
    return datas;
  }

  public void setDatas(List<CursoAtendimentoPresencaDataResponse> datas) {
    this.datas = datas;
  }
}
