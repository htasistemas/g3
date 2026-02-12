package br.com.g3.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class UsuarioAtualizacaoRequest {
  @NotBlank
  private String nome;

  @NotBlank
  @Email
  private String email;

  private String senha;
  @NotEmpty
  private List<String> permissoes = new ArrayList<>();
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

  public String getNomeUsuario() {
    return email;
  }

  public void setNomeUsuario(String nomeUsuario) {
    this.email = nomeUsuario;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }

  public List<String> getPermissoes() {
    return permissoes;
  }

  public void setPermissoes(List<String> permissoes) {
    this.permissoes = permissoes;
  }

  public LocalTime getHorarioEntrada1() {
    return horarioEntrada1;
  }

  public void setHorarioEntrada1(LocalTime horarioEntrada1) {
    this.horarioEntrada1 = horarioEntrada1;
  }

  public LocalTime getHorarioSaida1() {
    return horarioSaida1;
  }

  public void setHorarioSaida1(LocalTime horarioSaida1) {
    this.horarioSaida1 = horarioSaida1;
  }

  public LocalTime getHorarioEntrada2() {
    return horarioEntrada2;
  }

  public void setHorarioEntrada2(LocalTime horarioEntrada2) {
    this.horarioEntrada2 = horarioEntrada2;
  }

  public LocalTime getHorarioSaida2() {
    return horarioSaida2;
  }

  public void setHorarioSaida2(LocalTime horarioSaida2) {
    this.horarioSaida2 = horarioSaida2;
  }

  public LocalTime getHorarioSegundaEntrada1() {
    return horarioSegundaEntrada1;
  }

  public void setHorarioSegundaEntrada1(LocalTime horarioSegundaEntrada1) {
    this.horarioSegundaEntrada1 = horarioSegundaEntrada1;
  }

  public LocalTime getHorarioSegundaSaida1() {
    return horarioSegundaSaida1;
  }

  public void setHorarioSegundaSaida1(LocalTime horarioSegundaSaida1) {
    this.horarioSegundaSaida1 = horarioSegundaSaida1;
  }

  public LocalTime getHorarioSegundaEntrada2() {
    return horarioSegundaEntrada2;
  }

  public void setHorarioSegundaEntrada2(LocalTime horarioSegundaEntrada2) {
    this.horarioSegundaEntrada2 = horarioSegundaEntrada2;
  }

  public LocalTime getHorarioSegundaSaida2() {
    return horarioSegundaSaida2;
  }

  public void setHorarioSegundaSaida2(LocalTime horarioSegundaSaida2) {
    this.horarioSegundaSaida2 = horarioSegundaSaida2;
  }

  public LocalTime getHorarioTercaEntrada1() {
    return horarioTercaEntrada1;
  }

  public void setHorarioTercaEntrada1(LocalTime horarioTercaEntrada1) {
    this.horarioTercaEntrada1 = horarioTercaEntrada1;
  }

  public LocalTime getHorarioTercaSaida1() {
    return horarioTercaSaida1;
  }

  public void setHorarioTercaSaida1(LocalTime horarioTercaSaida1) {
    this.horarioTercaSaida1 = horarioTercaSaida1;
  }

  public LocalTime getHorarioTercaEntrada2() {
    return horarioTercaEntrada2;
  }

  public void setHorarioTercaEntrada2(LocalTime horarioTercaEntrada2) {
    this.horarioTercaEntrada2 = horarioTercaEntrada2;
  }

  public LocalTime getHorarioTercaSaida2() {
    return horarioTercaSaida2;
  }

  public void setHorarioTercaSaida2(LocalTime horarioTercaSaida2) {
    this.horarioTercaSaida2 = horarioTercaSaida2;
  }

  public LocalTime getHorarioQuartaEntrada1() {
    return horarioQuartaEntrada1;
  }

  public void setHorarioQuartaEntrada1(LocalTime horarioQuartaEntrada1) {
    this.horarioQuartaEntrada1 = horarioQuartaEntrada1;
  }

  public LocalTime getHorarioQuartaSaida1() {
    return horarioQuartaSaida1;
  }

  public void setHorarioQuartaSaida1(LocalTime horarioQuartaSaida1) {
    this.horarioQuartaSaida1 = horarioQuartaSaida1;
  }

  public LocalTime getHorarioQuartaEntrada2() {
    return horarioQuartaEntrada2;
  }

  public void setHorarioQuartaEntrada2(LocalTime horarioQuartaEntrada2) {
    this.horarioQuartaEntrada2 = horarioQuartaEntrada2;
  }

  public LocalTime getHorarioQuartaSaida2() {
    return horarioQuartaSaida2;
  }

  public void setHorarioQuartaSaida2(LocalTime horarioQuartaSaida2) {
    this.horarioQuartaSaida2 = horarioQuartaSaida2;
  }

  public LocalTime getHorarioQuintaEntrada1() {
    return horarioQuintaEntrada1;
  }

  public void setHorarioQuintaEntrada1(LocalTime horarioQuintaEntrada1) {
    this.horarioQuintaEntrada1 = horarioQuintaEntrada1;
  }

  public LocalTime getHorarioQuintaSaida1() {
    return horarioQuintaSaida1;
  }

  public void setHorarioQuintaSaida1(LocalTime horarioQuintaSaida1) {
    this.horarioQuintaSaida1 = horarioQuintaSaida1;
  }

  public LocalTime getHorarioQuintaEntrada2() {
    return horarioQuintaEntrada2;
  }

  public void setHorarioQuintaEntrada2(LocalTime horarioQuintaEntrada2) {
    this.horarioQuintaEntrada2 = horarioQuintaEntrada2;
  }

  public LocalTime getHorarioQuintaSaida2() {
    return horarioQuintaSaida2;
  }

  public void setHorarioQuintaSaida2(LocalTime horarioQuintaSaida2) {
    this.horarioQuintaSaida2 = horarioQuintaSaida2;
  }

  public LocalTime getHorarioSextaEntrada1() {
    return horarioSextaEntrada1;
  }

  public void setHorarioSextaEntrada1(LocalTime horarioSextaEntrada1) {
    this.horarioSextaEntrada1 = horarioSextaEntrada1;
  }

  public LocalTime getHorarioSextaSaida1() {
    return horarioSextaSaida1;
  }

  public void setHorarioSextaSaida1(LocalTime horarioSextaSaida1) {
    this.horarioSextaSaida1 = horarioSextaSaida1;
  }

  public LocalTime getHorarioSextaEntrada2() {
    return horarioSextaEntrada2;
  }

  public void setHorarioSextaEntrada2(LocalTime horarioSextaEntrada2) {
    this.horarioSextaEntrada2 = horarioSextaEntrada2;
  }

  public LocalTime getHorarioSextaSaida2() {
    return horarioSextaSaida2;
  }

  public void setHorarioSextaSaida2(LocalTime horarioSextaSaida2) {
    this.horarioSextaSaida2 = horarioSextaSaida2;
  }

  public LocalTime getHorarioSabadoEntrada1() {
    return horarioSabadoEntrada1;
  }

  public void setHorarioSabadoEntrada1(LocalTime horarioSabadoEntrada1) {
    this.horarioSabadoEntrada1 = horarioSabadoEntrada1;
  }

  public LocalTime getHorarioSabadoSaida1() {
    return horarioSabadoSaida1;
  }

  public void setHorarioSabadoSaida1(LocalTime horarioSabadoSaida1) {
    this.horarioSabadoSaida1 = horarioSabadoSaida1;
  }

  public LocalTime getHorarioSabadoEntrada2() {
    return horarioSabadoEntrada2;
  }

  public void setHorarioSabadoEntrada2(LocalTime horarioSabadoEntrada2) {
    this.horarioSabadoEntrada2 = horarioSabadoEntrada2;
  }

  public LocalTime getHorarioSabadoSaida2() {
    return horarioSabadoSaida2;
  }

  public void setHorarioSabadoSaida2(LocalTime horarioSabadoSaida2) {
    this.horarioSabadoSaida2 = horarioSabadoSaida2;
  }

  public LocalTime getHorarioDomingoEntrada1() {
    return horarioDomingoEntrada1;
  }

  public void setHorarioDomingoEntrada1(LocalTime horarioDomingoEntrada1) {
    this.horarioDomingoEntrada1 = horarioDomingoEntrada1;
  }

  public LocalTime getHorarioDomingoSaida1() {
    return horarioDomingoSaida1;
  }

  public void setHorarioDomingoSaida1(LocalTime horarioDomingoSaida1) {
    this.horarioDomingoSaida1 = horarioDomingoSaida1;
  }

  public LocalTime getHorarioDomingoEntrada2() {
    return horarioDomingoEntrada2;
  }

  public void setHorarioDomingoEntrada2(LocalTime horarioDomingoEntrada2) {
    this.horarioDomingoEntrada2 = horarioDomingoEntrada2;
  }

  public LocalTime getHorarioDomingoSaida2() {
    return horarioDomingoSaida2;
  }

  public void setHorarioDomingoSaida2(LocalTime horarioDomingoSaida2) {
    this.horarioDomingoSaida2 = horarioDomingoSaida2;
  }
}
