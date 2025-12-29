package br.com.g3.prontuario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "prontuario_anexos")
public class ProntuarioAnexo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "registro_id", nullable = false)
  private Long registroId;

  @Column(name = "nome_arquivo", nullable = false, length = 200)
  private String nomeArquivo;

  @Column(name = "tipo_mime", length = 80)
  private String tipoMime;

  @Column(name = "url_arquivo", nullable = false, columnDefinition = "TEXT")
  private String urlArquivo;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getRegistroId() {
    return registroId;
  }

  public void setRegistroId(Long registroId) {
    this.registroId = registroId;
  }

  public String getNomeArquivo() {
    return nomeArquivo;
  }

  public void setNomeArquivo(String nomeArquivo) {
    this.nomeArquivo = nomeArquivo;
  }

  public String getTipoMime() {
    return tipoMime;
  }

  public void setTipoMime(String tipoMime) {
    this.tipoMime = tipoMime;
  }

  public String getUrlArquivo() {
    return urlArquivo;
  }

  public void setUrlArquivo(String urlArquivo) {
    this.urlArquivo = urlArquivo;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
