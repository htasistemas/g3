package br.com.g3.controleveiculos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "controle_veiculos_motoristas_autorizados")
public class MotoristaAutorizado {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "veiculo_id", nullable = false)
  private Long veiculoId;

  @Column(name = "tipo_origem", length = 20, nullable = false)
  private String tipoOrigem;

  @Column(name = "profissional_id")
  private Long profissionalId;

  @Column(name = "voluntario_id")
  private Long voluntarioId;

  @Column(name = "nome_motorista", length = 200, nullable = false)
  private String nomeMotorista;

  @Column(name = "numero_carteira", length = 40)
  private String numeroCarteira;

  @Column(name = "categoria_carteira", length = 10)
  private String categoriaCarteira;

  @Column(name = "vencimento_carteira")
  private LocalDate vencimentoCarteira;

  @Column(name = "arquivo_carteira_pdf", columnDefinition = "TEXT")
  private String arquivoCarteiraPdf;

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

  public Long getVeiculoId() {
    return veiculoId;
  }

  public void setVeiculoId(Long veiculoId) {
    this.veiculoId = veiculoId;
  }

  public String getTipoOrigem() {
    return tipoOrigem;
  }

  public void setTipoOrigem(String tipoOrigem) {
    this.tipoOrigem = tipoOrigem;
  }

  public Long getProfissionalId() {
    return profissionalId;
  }

  public void setProfissionalId(Long profissionalId) {
    this.profissionalId = profissionalId;
  }

  public Long getVoluntarioId() {
    return voluntarioId;
  }

  public void setVoluntarioId(Long voluntarioId) {
    this.voluntarioId = voluntarioId;
  }

  public String getNomeMotorista() {
    return nomeMotorista;
  }

  public void setNomeMotorista(String nomeMotorista) {
    this.nomeMotorista = nomeMotorista;
  }

  public String getNumeroCarteira() {
    return numeroCarteira;
  }

  public void setNumeroCarteira(String numeroCarteira) {
    this.numeroCarteira = numeroCarteira;
  }

  public String getCategoriaCarteira() {
    return categoriaCarteira;
  }

  public void setCategoriaCarteira(String categoriaCarteira) {
    this.categoriaCarteira = categoriaCarteira;
  }

  public LocalDate getVencimentoCarteira() {
    return vencimentoCarteira;
  }

  public void setVencimentoCarteira(LocalDate vencimentoCarteira) {
    this.vencimentoCarteira = vencimentoCarteira;
  }

  public String getArquivoCarteiraPdf() {
    return arquivoCarteiraPdf;
  }

  public void setArquivoCarteiraPdf(String arquivoCarteiraPdf) {
    this.arquivoCarteiraPdf = arquivoCarteiraPdf;
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
