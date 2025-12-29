package br.com.g3.recebimentodoacao.dto;

import java.util.ArrayList;
import java.util.List;

public class RecebimentoDoacaoListaResponse {
  private List<RecebimentoDoacaoResponse> recebimentos = new ArrayList<>();

  public RecebimentoDoacaoListaResponse() {}

  public RecebimentoDoacaoListaResponse(List<RecebimentoDoacaoResponse> recebimentos) {
    if (recebimentos != null) {
      this.recebimentos = recebimentos;
    }
  }

  public List<RecebimentoDoacaoResponse> getRecebimentos() {
    return recebimentos;
  }

  public void setRecebimentos(List<RecebimentoDoacaoResponse> recebimentos) {
    this.recebimentos = recebimentos;
  }
}
