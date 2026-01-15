package br.com.g3.bancoempregos.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BancoEmpregoResponse {
  private Long id;
  private DadosVaga dadosVaga;
  private EmpresaLocal empresaLocal;
  private Requisitos requisitos;
  private List<EncaminhamentoResponse> encaminhamentos = new ArrayList<>();
  private LocalDateTime criadoEm;
  private LocalDateTime atualizadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public DadosVaga getDadosVaga() {
    return dadosVaga;
  }

  public void setDadosVaga(DadosVaga dadosVaga) {
    this.dadosVaga = dadosVaga;
  }

  public EmpresaLocal getEmpresaLocal() {
    return empresaLocal;
  }

  public void setEmpresaLocal(EmpresaLocal empresaLocal) {
    this.empresaLocal = empresaLocal;
  }

  public Requisitos getRequisitos() {
    return requisitos;
  }

  public void setRequisitos(Requisitos requisitos) {
    this.requisitos = requisitos;
  }

  public List<EncaminhamentoResponse> getEncaminhamentos() {
    return encaminhamentos;
  }

  public void setEncaminhamentos(List<EncaminhamentoResponse> encaminhamentos) {
    this.encaminhamentos = encaminhamentos;
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

  public static class DadosVaga {
    private String titulo;
    private String area;
    private String tipo;
    private String nivel;
    private String modelo;
    private String status;
    private LocalDate dataAbertura;
    private LocalDate dataEncerramento;
    private String tipoContrato;
    private String cargaHoraria;
    private String salario;
    private String beneficios;

    public String getTitulo() {
      return titulo;
    }

    public void setTitulo(String titulo) {
      this.titulo = titulo;
    }

    public String getArea() {
      return area;
    }

    public void setArea(String area) {
      this.area = area;
    }

    public String getTipo() {
      return tipo;
    }

    public void setTipo(String tipo) {
      this.tipo = tipo;
    }

    public String getNivel() {
      return nivel;
    }

    public void setNivel(String nivel) {
      this.nivel = nivel;
    }

    public String getModelo() {
      return modelo;
    }

    public void setModelo(String modelo) {
      this.modelo = modelo;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    public LocalDate getDataAbertura() {
      return dataAbertura;
    }

    public void setDataAbertura(LocalDate dataAbertura) {
      this.dataAbertura = dataAbertura;
    }

    public LocalDate getDataEncerramento() {
      return dataEncerramento;
    }

    public void setDataEncerramento(LocalDate dataEncerramento) {
      this.dataEncerramento = dataEncerramento;
    }

    public String getTipoContrato() {
      return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
      this.tipoContrato = tipoContrato;
    }

    public String getCargaHoraria() {
      return cargaHoraria;
    }

    public void setCargaHoraria(String cargaHoraria) {
      this.cargaHoraria = cargaHoraria;
    }

    public String getSalario() {
      return salario;
    }

    public void setSalario(String salario) {
      this.salario = salario;
    }

    public String getBeneficios() {
      return beneficios;
    }

    public void setBeneficios(String beneficios) {
      this.beneficios = beneficios;
    }
  }

  public static class EmpresaLocal {
    private String nomeEmpresa;
    private String cnpj;
    private String responsavel;
    private String telefone;
    private String email;
    private String endereco;
    private String bairro;
    private String cidade;
    private String uf;

    public String getNomeEmpresa() {
      return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
      this.nomeEmpresa = nomeEmpresa;
    }

    public String getCnpj() {
      return cnpj;
    }

    public void setCnpj(String cnpj) {
      this.cnpj = cnpj;
    }

    public String getResponsavel() {
      return responsavel;
    }

    public void setResponsavel(String responsavel) {
      this.responsavel = responsavel;
    }

    public String getTelefone() {
      return telefone;
    }

    public void setTelefone(String telefone) {
      this.telefone = telefone;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getEndereco() {
      return endereco;
    }

    public void setEndereco(String endereco) {
      this.endereco = endereco;
    }

    public String getBairro() {
      return bairro;
    }

    public void setBairro(String bairro) {
      this.bairro = bairro;
    }

    public String getCidade() {
      return cidade;
    }

    public void setCidade(String cidade) {
      this.cidade = cidade;
    }

    public String getUf() {
      return uf;
    }

    public void setUf(String uf) {
      this.uf = uf;
    }
  }

  public static class Requisitos {
    private String escolaridade;
    private String experiencia;
    private String habilidades;
    private String requisitos;
    private String descricao;
    private String observacoes;

    public String getEscolaridade() {
      return escolaridade;
    }

    public void setEscolaridade(String escolaridade) {
      this.escolaridade = escolaridade;
    }

    public String getExperiencia() {
      return experiencia;
    }

    public void setExperiencia(String experiencia) {
      this.experiencia = experiencia;
    }

    public String getHabilidades() {
      return habilidades;
    }

    public void setHabilidades(String habilidades) {
      this.habilidades = habilidades;
    }

    public String getRequisitos() {
      return requisitos;
    }

    public void setRequisitos(String requisitos) {
      this.requisitos = requisitos;
    }

    public String getDescricao() {
      return descricao;
    }

    public void setDescricao(String descricao) {
      this.descricao = descricao;
    }

    public String getObservacoes() {
      return observacoes;
    }

    public void setObservacoes(String observacoes) {
      this.observacoes = observacoes;
    }
  }

  public static class EncaminhamentoResponse {
    private Long id;
    private Long beneficiarioId;
    private String beneficiarioNome;
    private LocalDate data;
    private String status;
    private String observacoes;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public Long getBeneficiarioId() {
      return beneficiarioId;
    }

    public void setBeneficiarioId(Long beneficiarioId) {
      this.beneficiarioId = beneficiarioId;
    }

    public String getBeneficiarioNome() {
      return beneficiarioNome;
    }

    public void setBeneficiarioNome(String beneficiarioNome) {
      this.beneficiarioNome = beneficiarioNome;
    }

    public LocalDate getData() {
      return data;
    }

    public void setData(LocalDate data) {
      this.data = data;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    public String getObservacoes() {
      return observacoes;
    }

    public void setObservacoes(String observacoes) {
      this.observacoes = observacoes;
    }
  }
}
