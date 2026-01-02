package br.com.g3.fotoseventos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "fotos_eventos_fotos")
public class FotoEventoFoto {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "evento_id", nullable = false)
  private Long eventoId;

  @Column(name = "arquivo", columnDefinition = "TEXT", nullable = false)
  private String arquivo;

  @Column(name = "legenda", length = 255)
  private String legenda;

  @Column(name = "ordem")
  private Integer ordem;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getEventoId() {
    return eventoId;
  }

  public void setEventoId(Long eventoId) {
    this.eventoId = eventoId;
  }

  public String getArquivo() {
    return arquivo;
  }

  public void setArquivo(String arquivo) {
    this.arquivo = arquivo;
  }

  public String getLegenda() {
    return legenda;
  }

  public void setLegenda(String legenda) {
    this.legenda = legenda;
  }

  public Integer getOrdem() {
    return ordem;
  }

  public void setOrdem(Integer ordem) {
    this.ordem = ordem;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
