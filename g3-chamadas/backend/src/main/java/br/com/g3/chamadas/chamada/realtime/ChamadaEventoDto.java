package br.com.g3.chamadas.chamada.realtime;

import java.time.LocalDateTime;
import java.util.UUID;

public class ChamadaEventoDto {
  private String evento;
  private Dados dados;

  public ChamadaEventoDto(String evento, Dados dados) {
    this.evento = evento;
    this.dados = dados;
  }

  public String getEvento() {
    return evento;
  }

  public Dados getDados() {
    return dados;
  }

  public static class Dados {
    private UUID idChamada;
    private String nomeBeneficiario;
    private String localAtendimento;
    private LocalDateTime dataHoraChamada;

    public Dados(UUID idChamada, String nomeBeneficiario, String localAtendimento, LocalDateTime dataHoraChamada) {
      this.idChamada = idChamada;
      this.nomeBeneficiario = nomeBeneficiario;
      this.localAtendimento = localAtendimento;
      this.dataHoraChamada = dataHoraChamada;
    }

    public UUID getIdChamada() {
      return idChamada;
    }

    public String getNomeBeneficiario() {
      return nomeBeneficiario;
    }

    public String getLocalAtendimento() {
      return localAtendimento;
    }

    public LocalDateTime getDataHoraChamada() {
      return dataHoraChamada;
    }
  }
}
