package br.com.g3.cadastrobeneficiario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "contato_beneficiario")
public class ContatoBeneficiario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "beneficiario_id", nullable = false)
  private CadastroBeneficiario beneficiario;

  @Column(name = "telefone_principal", length = 30)
  private String telefonePrincipal;

  @Column(name = "telefone_principal_whatsapp")
  private Boolean telefonePrincipalWhatsapp;

  @Column(name = "telefone_secundario", length = 30)
  private String telefoneSecundario;

  @Column(name = "telefone_recado_nome", length = 150)
  private String telefoneRecadoNome;

  @Column(name = "telefone_recado_numero", length = 30)
  private String telefoneRecadoNumero;

  @Column(length = 150)
  private String email;

  @Column(name = "permite_contato_tel")
  private Boolean permiteContatoTel;

  @Column(name = "permite_contato_whatsapp")
  private Boolean permiteContatoWhatsapp;

  @Column(name = "permite_contato_sms")
  private Boolean permiteContatoSms;

  @Column(name = "permite_contato_email")
  private Boolean permiteContatoEmail;

  @Column(name = "horario_preferencial_contato", length = 80)
  private String horarioPreferencialContato;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  @Column(name = "atualizado_em", nullable = false)
  private LocalDateTime atualizadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CadastroBeneficiario getBeneficiario() {
    return beneficiario;
  }

  public void setBeneficiario(CadastroBeneficiario beneficiario) {
    this.beneficiario = beneficiario;
  }

  public String getTelefonePrincipal() {
    return telefonePrincipal;
  }

  public void setTelefonePrincipal(String telefonePrincipal) {
    this.telefonePrincipal = telefonePrincipal;
  }

  public Boolean getTelefonePrincipalWhatsapp() {
    return telefonePrincipalWhatsapp;
  }

  public void setTelefonePrincipalWhatsapp(Boolean telefonePrincipalWhatsapp) {
    this.telefonePrincipalWhatsapp = telefonePrincipalWhatsapp;
  }

  public String getTelefoneSecundario() {
    return telefoneSecundario;
  }

  public void setTelefoneSecundario(String telefoneSecundario) {
    this.telefoneSecundario = telefoneSecundario;
  }

  public String getTelefoneRecadoNome() {
    return telefoneRecadoNome;
  }

  public void setTelefoneRecadoNome(String telefoneRecadoNome) {
    this.telefoneRecadoNome = telefoneRecadoNome;
  }

  public String getTelefoneRecadoNumero() {
    return telefoneRecadoNumero;
  }

  public void setTelefoneRecadoNumero(String telefoneRecadoNumero) {
    this.telefoneRecadoNumero = telefoneRecadoNumero;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Boolean getPermiteContatoTel() {
    return permiteContatoTel;
  }

  public void setPermiteContatoTel(Boolean permiteContatoTel) {
    this.permiteContatoTel = permiteContatoTel;
  }

  public Boolean getPermiteContatoWhatsapp() {
    return permiteContatoWhatsapp;
  }

  public void setPermiteContatoWhatsapp(Boolean permiteContatoWhatsapp) {
    this.permiteContatoWhatsapp = permiteContatoWhatsapp;
  }

  public Boolean getPermiteContatoSms() {
    return permiteContatoSms;
  }

  public void setPermiteContatoSms(Boolean permiteContatoSms) {
    this.permiteContatoSms = permiteContatoSms;
  }

  public Boolean getPermiteContatoEmail() {
    return permiteContatoEmail;
  }

  public void setPermiteContatoEmail(Boolean permiteContatoEmail) {
    this.permiteContatoEmail = permiteContatoEmail;
  }

  public String getHorarioPreferencialContato() {
    return horarioPreferencialContato;
  }

  public void setHorarioPreferencialContato(String horarioPreferencialContato) {
    this.horarioPreferencialContato = horarioPreferencialContato;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }

  public LocalDateTime getAtualizadoEm() {
    return atualizadoEm;
  }

  public void setAtualizadoEm(LocalDateTime atualizadoEm) {
    this.atualizadoEm = atualizadoEm;
  }
}
