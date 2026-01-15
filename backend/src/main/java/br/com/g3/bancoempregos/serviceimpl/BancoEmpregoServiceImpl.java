package br.com.g3.bancoempregos.serviceimpl;

import br.com.g3.bancoempregos.domain.BancoEmprego;
import br.com.g3.bancoempregos.domain.BancoEmpregoEncaminhamento;
import br.com.g3.bancoempregos.dto.BancoEmpregoRequest;
import br.com.g3.bancoempregos.dto.BancoEmpregoResponse;
import br.com.g3.bancoempregos.repository.BancoEmpregoEncaminhamentoRepository;
import br.com.g3.bancoempregos.repository.BancoEmpregoRepository;
import br.com.g3.bancoempregos.service.BancoEmpregoService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BancoEmpregoServiceImpl implements BancoEmpregoService {
  private final BancoEmpregoRepository repository;
  private final BancoEmpregoEncaminhamentoRepository encaminhamentoRepository;

  public BancoEmpregoServiceImpl(
      BancoEmpregoRepository repository,
      BancoEmpregoEncaminhamentoRepository encaminhamentoRepository) {
    this.repository = repository;
    this.encaminhamentoRepository = encaminhamentoRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<BancoEmpregoResponse> listar() {
    return repository.listar().stream()
        .map((registro) -> toResponse(registro, listarEncaminhamentos(registro.getId())))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public BancoEmpregoResponse buscarPorId(Long id) {
    BancoEmprego registro =
        repository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Vaga nao encontrada."));
    return toResponse(registro, listarEncaminhamentos(id));
  }

  @Override
  @Transactional
  public BancoEmpregoResponse criar(BancoEmpregoRequest request) {
    validarObrigatorios(request);
    BancoEmprego emprego = new BancoEmprego();
    aplicar(emprego, request);
    LocalDateTime agora = LocalDateTime.now();
    emprego.setCriadoEm(agora);
    emprego.setAtualizadoEm(agora);
    BancoEmprego salvo = repository.salvar(emprego);
    salvarEncaminhamentos(salvo.getId(), request.getEncaminhamentos());
    return toResponse(salvo, listarEncaminhamentos(salvo.getId()));
  }

  @Override
  @Transactional
  public BancoEmpregoResponse atualizar(Long id, BancoEmpregoRequest request) {
    validarObrigatorios(request);
    BancoEmprego emprego =
        repository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Vaga nao encontrada."));
    aplicar(emprego, request);
    emprego.setAtualizadoEm(LocalDateTime.now());
    BancoEmprego salvo = repository.salvar(emprego);
    salvarEncaminhamentos(salvo.getId(), request.getEncaminhamentos());
    return toResponse(salvo, listarEncaminhamentos(salvo.getId()));
  }

  @Override
  @Transactional
  public void remover(Long id) {
    BancoEmprego emprego =
        repository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Vaga nao encontrada."));
    repository.remover(emprego);
  }

  private void aplicar(BancoEmprego emprego, BancoEmpregoRequest request) {
    BancoEmpregoRequest.DadosVaga dados = request.getDadosVaga();
    if (dados != null) {
      emprego.setTitulo(dados.getTitulo());
      emprego.setArea(dados.getArea());
      emprego.setTipo(dados.getTipo());
      emprego.setNivel(dados.getNivel());
      emprego.setModelo(dados.getModelo());
      emprego.setStatus(dados.getStatus());
      emprego.setDataAbertura(dados.getDataAbertura());
      emprego.setDataEncerramento(dados.getDataEncerramento());
      emprego.setTipoContrato(dados.getTipoContrato());
      emprego.setCargaHoraria(dados.getCargaHoraria());
      emprego.setSalario(dados.getSalario());
      emprego.setBeneficios(dados.getBeneficios());
    }

    BancoEmpregoRequest.EmpresaLocal empresa = request.getEmpresaLocal();
    if (empresa != null) {
      emprego.setNomeEmpresa(empresa.getNomeEmpresa());
      emprego.setCnpj(empresa.getCnpj());
      emprego.setResponsavel(empresa.getResponsavel());
      emprego.setTelefone(empresa.getTelefone());
      emprego.setEmail(empresa.getEmail());
      emprego.setEndereco(empresa.getEndereco());
      emprego.setBairro(empresa.getBairro());
      emprego.setCidade(empresa.getCidade());
      emprego.setUf(empresa.getUf());
    }

    BancoEmpregoRequest.Requisitos requisitos = request.getRequisitos();
    if (requisitos != null) {
      emprego.setEscolaridade(requisitos.getEscolaridade());
      emprego.setExperiencia(requisitos.getExperiencia());
      emprego.setHabilidades(requisitos.getHabilidades());
      emprego.setRequisitos(requisitos.getRequisitos());
      emprego.setDescricao(requisitos.getDescricao());
      emprego.setObservacoes(requisitos.getObservacoes());
    }
  }

  private void validarObrigatorios(BancoEmpregoRequest request) {
    if (request == null || request.getDadosVaga() == null) {
      throw new IllegalArgumentException("Dados da vaga nao informados.");
    }
    if (isBlank(request.getDadosVaga().getTitulo())) {
      throw new IllegalArgumentException("Titulo da vaga nao informado.");
    }
    if (isBlank(request.getDadosVaga().getStatus())) {
      throw new IllegalArgumentException("Status da vaga nao informado.");
    }
    if (request.getDadosVaga().getDataAbertura() == null) {
      throw new IllegalArgumentException("Data de abertura nao informada.");
    }
    BancoEmpregoRequest.EmpresaLocal empresa = request.getEmpresaLocal();
    if (empresa == null || isBlank(empresa.getNomeEmpresa())) {
      throw new IllegalArgumentException("Nome da empresa nao informado.");
    }
    if (empresa != null && isBlank(empresa.getCidade())) {
      throw new IllegalArgumentException("Cidade da empresa nao informada.");
    }
    if (empresa != null && !empresa.isCnpjValido()) {
      throw new IllegalArgumentException("CNPJ informado e invalido.");
    }
    BancoEmpregoRequest.Requisitos requisitos = request.getRequisitos();
    if (requisitos == null || isBlank(requisitos.getDescricao())) {
      throw new IllegalArgumentException("Descricao da vaga nao informada.");
    }
  }

  private void salvarEncaminhamentos(
      Long empregoId, List<BancoEmpregoRequest.EncaminhamentoRequest> encaminhamentos) {
    encaminhamentoRepository.removerPorEmpregoId(empregoId);
    if (encaminhamentos == null || encaminhamentos.isEmpty()) {
      return;
    }
    for (BancoEmpregoRequest.EncaminhamentoRequest request : encaminhamentos) {
      BancoEmpregoEncaminhamento encaminhamento = new BancoEmpregoEncaminhamento();
      encaminhamento.setEmpregoId(empregoId);
      encaminhamento.setBeneficiarioId(request.getBeneficiarioId());
      encaminhamento.setBeneficiarioNome(request.getBeneficiarioNome());
      encaminhamento.setStatus(request.getStatus());
      encaminhamento.setObservacoes(request.getObservacoes());
      encaminhamento.setDataEncaminhamento(parseData(request.getData()));
      encaminhamentoRepository.salvar(encaminhamento);
    }
  }

  private List<BancoEmpregoEncaminhamento> listarEncaminhamentos(Long empregoId) {
    if (empregoId == null) {
      return new ArrayList<>();
    }
    return encaminhamentoRepository.listarPorEmpregoId(empregoId);
  }

  private BancoEmpregoResponse toResponse(
      BancoEmprego emprego, List<BancoEmpregoEncaminhamento> encaminhamentos) {
    BancoEmpregoResponse response = new BancoEmpregoResponse();
    response.setId(emprego.getId());
    response.setCriadoEm(emprego.getCriadoEm());
    response.setAtualizadoEm(emprego.getAtualizadoEm());
    response.setDadosVaga(toDadosVaga(emprego));
    response.setEmpresaLocal(toEmpresaLocal(emprego));
    response.setRequisitos(toRequisitos(emprego));
    response.setEncaminhamentos(toEncaminhamentos(encaminhamentos));
    return response;
  }

  private BancoEmpregoResponse.DadosVaga toDadosVaga(BancoEmprego emprego) {
    BancoEmpregoResponse.DadosVaga dados = new BancoEmpregoResponse.DadosVaga();
    dados.setTitulo(emprego.getTitulo());
    dados.setArea(emprego.getArea());
    dados.setTipo(emprego.getTipo());
    dados.setNivel(emprego.getNivel());
    dados.setModelo(emprego.getModelo());
    dados.setStatus(emprego.getStatus());
    dados.setDataAbertura(emprego.getDataAbertura());
    dados.setDataEncerramento(emprego.getDataEncerramento());
    dados.setTipoContrato(emprego.getTipoContrato());
    dados.setCargaHoraria(emprego.getCargaHoraria());
    dados.setSalario(emprego.getSalario());
    dados.setBeneficios(emprego.getBeneficios());
    return dados;
  }

  private BancoEmpregoResponse.EmpresaLocal toEmpresaLocal(BancoEmprego emprego) {
    BancoEmpregoResponse.EmpresaLocal empresa = new BancoEmpregoResponse.EmpresaLocal();
    empresa.setNomeEmpresa(emprego.getNomeEmpresa());
    empresa.setCnpj(emprego.getCnpj());
    empresa.setResponsavel(emprego.getResponsavel());
    empresa.setTelefone(emprego.getTelefone());
    empresa.setEmail(emprego.getEmail());
    empresa.setEndereco(emprego.getEndereco());
    empresa.setBairro(emprego.getBairro());
    empresa.setCidade(emprego.getCidade());
    empresa.setUf(emprego.getUf());
    return empresa;
  }

  private BancoEmpregoResponse.Requisitos toRequisitos(BancoEmprego emprego) {
    BancoEmpregoResponse.Requisitos requisitos = new BancoEmpregoResponse.Requisitos();
    requisitos.setEscolaridade(emprego.getEscolaridade());
    requisitos.setExperiencia(emprego.getExperiencia());
    requisitos.setHabilidades(emprego.getHabilidades());
    requisitos.setRequisitos(emprego.getRequisitos());
    requisitos.setDescricao(emprego.getDescricao());
    requisitos.setObservacoes(emprego.getObservacoes());
    return requisitos;
  }

  private List<BancoEmpregoResponse.EncaminhamentoResponse> toEncaminhamentos(
      List<BancoEmpregoEncaminhamento> encaminhamentos) {
    if (encaminhamentos == null) {
      return new ArrayList<>();
    }
    return encaminhamentos.stream()
        .filter(Objects::nonNull)
        .map(this::toEncaminhamentoResponse)
        .collect(Collectors.toList());
  }

  private BancoEmpregoResponse.EncaminhamentoResponse toEncaminhamentoResponse(
      BancoEmpregoEncaminhamento encaminhamento) {
    BancoEmpregoResponse.EncaminhamentoResponse response =
        new BancoEmpregoResponse.EncaminhamentoResponse();
    response.setId(encaminhamento.getId());
    response.setBeneficiarioId(encaminhamento.getBeneficiarioId());
    response.setBeneficiarioNome(encaminhamento.getBeneficiarioNome());
    response.setData(encaminhamento.getDataEncaminhamento());
    response.setStatus(encaminhamento.getStatus());
    response.setObservacoes(encaminhamento.getObservacoes());
    return response;
  }

  private LocalDate parseData(String valor) {
    if (valor == null || valor.trim().isEmpty()) {
      return LocalDate.now();
    }
    return LocalDate.parse(valor);
  }

  private boolean isBlank(String valor) {
    return valor == null || valor.trim().isEmpty();
  }
}
