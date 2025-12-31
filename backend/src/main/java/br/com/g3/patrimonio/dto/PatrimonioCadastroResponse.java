package br.com.g3.patrimonio.dto;

public class PatrimonioCadastroResponse {
  private PatrimonioResponse patrimonio;

  public PatrimonioCadastroResponse(PatrimonioResponse patrimonio) {
    this.patrimonio = patrimonio;
  }

  public PatrimonioResponse getPatrimonio() {
    return patrimonio;
  }

  public void setPatrimonio(PatrimonioResponse patrimonio) {
    this.patrimonio = patrimonio;
  }
}
