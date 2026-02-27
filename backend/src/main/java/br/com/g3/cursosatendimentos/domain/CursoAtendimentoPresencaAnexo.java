package br.com.g3.cursosatendimentos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cursos_atendimentos_presenca_anexos")
public class CursoAtendimentoPresencaAnexo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "presenca_data_id", nullable = false)
  private CursoAtendimentoPresencaData presencaData;

  @Column(name = "nome_arquivo", nullable = false, length = 200)
  private String nomeArquivo;

  @Column(name = "tipo_mime", nullable = false, length = 120)
  private String tipoMime;

  @Column(name = "tamanho", length = 40)
  private String tamanho;

  @Column(name = "caminho_arquivo", columnDefinition = "TEXT")
  private String caminhoArquivo;

  @Column(name = "data_upload", nullable = false)
  private LocalDate dataUpload;

  @Column(name = "usuario", nullable = false, length = 120)
  private String usuario;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CursoAtendimentoPresencaData getPresencaData() {
    return presencaData;
  }

  public void setPresencaData(CursoAtendimentoPresencaData presencaData) {
    this.presencaData = presencaData;
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

  public String getTamanho() {
    return tamanho;
  }

  public void setTamanho(String tamanho) {
    this.tamanho = tamanho;
  }

  public String getCaminhoArquivo() {
    return caminhoArquivo;
  }

  public void setCaminhoArquivo(String caminhoArquivo) {
    this.caminhoArquivo = caminhoArquivo;
  }

  public LocalDate getDataUpload() {
    return dataUpload;
  }

  public void setDataUpload(LocalDate dataUpload) {
    this.dataUpload = dataUpload;
  }

  public String getUsuario() {
    return usuario;
  }

  public void setUsuario(String usuario) {
    this.usuario = usuario;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
