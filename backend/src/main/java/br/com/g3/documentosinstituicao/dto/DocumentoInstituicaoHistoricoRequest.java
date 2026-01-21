package br.com.g3.documentosinstituicao.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class DocumentoInstituicaoHistoricoRequest {
  private LocalDateTime dataHora;

  @NotBlank(message = "Usuario responsavel e obrigatorio.")
  private String usuario;

  @NotBlank(message = "Tipo de alteracao e obrigatorio.")
  private String tipoAlteracao;

  private String observacao;

  public LocalDateTime getDataHora() {
    return dataHora;
  }

  public void setDataHora(LocalDateTime dataHora) {
    this.dataHora = dataHora;
  }

  public String getUsuario() {
    return usuario;
  }

  public void setUsuario(String usuario) {
    this.usuario = usuario;
  }

  public String getTipoAlteracao() {
    return tipoAlteracao;
  }

  public void setTipoAlteracao(String tipoAlteracao) {
    this.tipoAlteracao = tipoAlteracao;
  }

  public String getObservacao() {
    return observacao;
  }

  public void setObservacao(String observacao) {
    this.observacao = observacao;
  }
}
