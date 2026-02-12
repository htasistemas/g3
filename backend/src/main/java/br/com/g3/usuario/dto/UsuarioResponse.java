package br.com.g3.usuario.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class UsuarioResponse {
  private Long id;
  private String nomeUsuario;
  private String nome;
  private String email;
  private LocalTime horarioEntrada1;
  private LocalTime horarioSaida1;
  private LocalTime horarioEntrada2;
  private LocalTime horarioSaida2;
  private LocalTime horarioSegundaEntrada1;
  private LocalTime horarioSegundaSaida1;
  private LocalTime horarioSegundaEntrada2;
  private LocalTime horarioSegundaSaida2;
  private LocalTime horarioTercaEntrada1;
  private LocalTime horarioTercaSaida1;
  private LocalTime horarioTercaEntrada2;
  private LocalTime horarioTercaSaida2;
  private LocalTime horarioQuartaEntrada1;
  private LocalTime horarioQuartaSaida1;
  private LocalTime horarioQuartaEntrada2;
  private LocalTime horarioQuartaSaida2;
  private LocalTime horarioQuintaEntrada1;
  private LocalTime horarioQuintaSaida1;
  private LocalTime horarioQuintaEntrada2;
  private LocalTime horarioQuintaSaida2;
  private LocalTime horarioSextaEntrada1;
  private LocalTime horarioSextaSaida1;
  private LocalTime horarioSextaEntrada2;
  private LocalTime horarioSextaSaida2;
  private LocalTime horarioSabadoEntrada1;
  private LocalTime horarioSabadoSaida1;
  private LocalTime horarioSabadoEntrada2;
  private LocalTime horarioSabadoSaida2;
  private LocalTime horarioDomingoEntrada1;
  private LocalTime horarioDomingoSaida1;
  private LocalTime horarioDomingoEntrada2;
  private LocalTime horarioDomingoSaida2;
  private LocalDateTime criadoEm;
  private LocalDateTime atualizadoEm;
  private List<String> permissoes;

  public UsuarioResponse(
      Long id,
      String nomeUsuario,
      String nome,
      String email,
      LocalTime horarioEntrada1,
      LocalTime horarioSaida1,
      LocalTime horarioEntrada2,
      LocalTime horarioSaida2,
      LocalTime horarioSegundaEntrada1,
      LocalTime horarioSegundaSaida1,
      LocalTime horarioSegundaEntrada2,
      LocalTime horarioSegundaSaida2,
      LocalTime horarioTercaEntrada1,
      LocalTime horarioTercaSaida1,
      LocalTime horarioTercaEntrada2,
      LocalTime horarioTercaSaida2,
      LocalTime horarioQuartaEntrada1,
      LocalTime horarioQuartaSaida1,
      LocalTime horarioQuartaEntrada2,
      LocalTime horarioQuartaSaida2,
      LocalTime horarioQuintaEntrada1,
      LocalTime horarioQuintaSaida1,
      LocalTime horarioQuintaEntrada2,
      LocalTime horarioQuintaSaida2,
      LocalTime horarioSextaEntrada1,
      LocalTime horarioSextaSaida1,
      LocalTime horarioSextaEntrada2,
      LocalTime horarioSextaSaida2,
      LocalTime horarioSabadoEntrada1,
      LocalTime horarioSabadoSaida1,
      LocalTime horarioSabadoEntrada2,
      LocalTime horarioSabadoSaida2,
      LocalTime horarioDomingoEntrada1,
      LocalTime horarioDomingoSaida1,
      LocalTime horarioDomingoEntrada2,
      LocalTime horarioDomingoSaida2,
      LocalDateTime criadoEm,
      LocalDateTime atualizadoEm,
      List<String> permissoes) {
    this.id = id;
    this.nomeUsuario = nomeUsuario;
    this.nome = nome;
    this.email = email;
    this.horarioEntrada1 = horarioEntrada1;
    this.horarioSaida1 = horarioSaida1;
    this.horarioEntrada2 = horarioEntrada2;
    this.horarioSaida2 = horarioSaida2;
    this.horarioSegundaEntrada1 = horarioSegundaEntrada1;
    this.horarioSegundaSaida1 = horarioSegundaSaida1;
    this.horarioSegundaEntrada2 = horarioSegundaEntrada2;
    this.horarioSegundaSaida2 = horarioSegundaSaida2;
    this.horarioTercaEntrada1 = horarioTercaEntrada1;
    this.horarioTercaSaida1 = horarioTercaSaida1;
    this.horarioTercaEntrada2 = horarioTercaEntrada2;
    this.horarioTercaSaida2 = horarioTercaSaida2;
    this.horarioQuartaEntrada1 = horarioQuartaEntrada1;
    this.horarioQuartaSaida1 = horarioQuartaSaida1;
    this.horarioQuartaEntrada2 = horarioQuartaEntrada2;
    this.horarioQuartaSaida2 = horarioQuartaSaida2;
    this.horarioQuintaEntrada1 = horarioQuintaEntrada1;
    this.horarioQuintaSaida1 = horarioQuintaSaida1;
    this.horarioQuintaEntrada2 = horarioQuintaEntrada2;
    this.horarioQuintaSaida2 = horarioQuintaSaida2;
    this.horarioSextaEntrada1 = horarioSextaEntrada1;
    this.horarioSextaSaida1 = horarioSextaSaida1;
    this.horarioSextaEntrada2 = horarioSextaEntrada2;
    this.horarioSextaSaida2 = horarioSextaSaida2;
    this.horarioSabadoEntrada1 = horarioSabadoEntrada1;
    this.horarioSabadoSaida1 = horarioSabadoSaida1;
    this.horarioSabadoEntrada2 = horarioSabadoEntrada2;
    this.horarioSabadoSaida2 = horarioSabadoSaida2;
    this.horarioDomingoEntrada1 = horarioDomingoEntrada1;
    this.horarioDomingoSaida1 = horarioDomingoSaida1;
    this.horarioDomingoEntrada2 = horarioDomingoEntrada2;
    this.horarioDomingoSaida2 = horarioDomingoSaida2;
    this.criadoEm = criadoEm;
    this.atualizadoEm = atualizadoEm;
    this.permissoes = permissoes;
  }

  public Long getId() {
    return id;
  }

  public String getNomeUsuario() {
    return nomeUsuario;
  }

  public String getNome() {
    return nome;
  }

  public String getEmail() {
    return email;
  }

  public LocalTime getHorarioEntrada1() {
    return horarioEntrada1;
  }

  public LocalTime getHorarioSaida1() {
    return horarioSaida1;
  }

  public LocalTime getHorarioEntrada2() {
    return horarioEntrada2;
  }

  public LocalTime getHorarioSaida2() {
    return horarioSaida2;
  }

  public LocalTime getHorarioSegundaEntrada1() {
    return horarioSegundaEntrada1;
  }

  public LocalTime getHorarioSegundaSaida1() {
    return horarioSegundaSaida1;
  }

  public LocalTime getHorarioSegundaEntrada2() {
    return horarioSegundaEntrada2;
  }

  public LocalTime getHorarioSegundaSaida2() {
    return horarioSegundaSaida2;
  }

  public LocalTime getHorarioTercaEntrada1() {
    return horarioTercaEntrada1;
  }

  public LocalTime getHorarioTercaSaida1() {
    return horarioTercaSaida1;
  }

  public LocalTime getHorarioTercaEntrada2() {
    return horarioTercaEntrada2;
  }

  public LocalTime getHorarioTercaSaida2() {
    return horarioTercaSaida2;
  }

  public LocalTime getHorarioQuartaEntrada1() {
    return horarioQuartaEntrada1;
  }

  public LocalTime getHorarioQuartaSaida1() {
    return horarioQuartaSaida1;
  }

  public LocalTime getHorarioQuartaEntrada2() {
    return horarioQuartaEntrada2;
  }

  public LocalTime getHorarioQuartaSaida2() {
    return horarioQuartaSaida2;
  }

  public LocalTime getHorarioQuintaEntrada1() {
    return horarioQuintaEntrada1;
  }

  public LocalTime getHorarioQuintaSaida1() {
    return horarioQuintaSaida1;
  }

  public LocalTime getHorarioQuintaEntrada2() {
    return horarioQuintaEntrada2;
  }

  public LocalTime getHorarioQuintaSaida2() {
    return horarioQuintaSaida2;
  }

  public LocalTime getHorarioSextaEntrada1() {
    return horarioSextaEntrada1;
  }

  public LocalTime getHorarioSextaSaida1() {
    return horarioSextaSaida1;
  }

  public LocalTime getHorarioSextaEntrada2() {
    return horarioSextaEntrada2;
  }

  public LocalTime getHorarioSextaSaida2() {
    return horarioSextaSaida2;
  }

  public LocalTime getHorarioSabadoEntrada1() {
    return horarioSabadoEntrada1;
  }

  public LocalTime getHorarioSabadoSaida1() {
    return horarioSabadoSaida1;
  }

  public LocalTime getHorarioSabadoEntrada2() {
    return horarioSabadoEntrada2;
  }

  public LocalTime getHorarioSabadoSaida2() {
    return horarioSabadoSaida2;
  }

  public LocalTime getHorarioDomingoEntrada1() {
    return horarioDomingoEntrada1;
  }

  public LocalTime getHorarioDomingoSaida1() {
    return horarioDomingoSaida1;
  }

  public LocalTime getHorarioDomingoEntrada2() {
    return horarioDomingoEntrada2;
  }

  public LocalTime getHorarioDomingoSaida2() {
    return horarioDomingoSaida2;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public LocalDateTime getAtualizadoEm() {
    return atualizadoEm;
  }

  public List<String> getPermissoes() {
    return permissoes;
  }
}
