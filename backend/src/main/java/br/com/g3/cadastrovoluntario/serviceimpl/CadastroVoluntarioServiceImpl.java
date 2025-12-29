package br.com.g3.cadastrovoluntario.serviceimpl;

import br.com.g3.cadastroprofissionais.domain.CadastroProfissional;
import br.com.g3.cadastroprofissionais.repository.CadastroProfissionalRepository;
import br.com.g3.cadastrovoluntario.domain.CadastroVoluntario;
import br.com.g3.cadastrovoluntario.dto.CadastroVoluntarioCriacaoRequest;
import br.com.g3.cadastrovoluntario.dto.CadastroVoluntarioResponse;
import br.com.g3.cadastrovoluntario.mapper.CadastroVoluntarioMapper;
import br.com.g3.cadastrovoluntario.repository.CadastroVoluntarioRepository;
import br.com.g3.cadastrovoluntario.service.CadastroVoluntarioService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CadastroVoluntarioServiceImpl implements CadastroVoluntarioService {
  private final CadastroVoluntarioRepository repository;
  private final CadastroProfissionalRepository profissionalRepository;

  public CadastroVoluntarioServiceImpl(
      CadastroVoluntarioRepository repository,
      CadastroProfissionalRepository profissionalRepository) {
    this.repository = repository;
    this.profissionalRepository = profissionalRepository;
  }

  @Override
  public CadastroVoluntarioResponse criar(CadastroVoluntarioCriacaoRequest request) {
    validarCpf(request.getCpf());
    CadastroVoluntario cadastro = CadastroVoluntarioMapper.toDomain(request);
    aplicarProfissional(cadastro, request.getProfissionalId());
    return CadastroVoluntarioMapper.toResponse(repository.salvar(cadastro));
  }

  @Override
  public CadastroVoluntarioResponse atualizar(Long id, CadastroVoluntarioCriacaoRequest request) {
    validarCpf(request.getCpf());
    CadastroVoluntario cadastro =
        repository
            .buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Cadastro de voluntario nao encontrado."));
    CadastroVoluntarioMapper.aplicarAtualizacao(cadastro, request);
    aplicarProfissional(cadastro, request.getProfissionalId());
    cadastro.setAtualizadoEm(LocalDateTime.now());
    return CadastroVoluntarioMapper.toResponse(repository.salvar(cadastro));
  }

  @Override
  public CadastroVoluntarioResponse buscarPorId(Long id) {
    CadastroVoluntario cadastro =
        repository
            .buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Cadastro de voluntario nao encontrado."));
    return CadastroVoluntarioMapper.toResponse(cadastro);
  }

  @Override
  public List<CadastroVoluntarioResponse> listar() {
    return repository.listar().stream()
        .map(CadastroVoluntarioMapper::toResponse)
        .collect(Collectors.toList());
  }

  @Override
  public void remover(Long id) {
    CadastroVoluntario cadastro =
        repository
            .buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Cadastro de voluntario nao encontrado."));
    repository.remover(cadastro);
  }

  private void aplicarProfissional(CadastroVoluntario cadastro, Long profissionalId) {
    if (profissionalId == null) {
      cadastro.setProfissional(null);
      return;
    }
    CadastroProfissional profissional =
        profissionalRepository
            .buscarPorId(profissionalId)
            .orElseThrow(() -> new IllegalArgumentException("Profissional nao encontrado."));
    cadastro.setProfissional(profissional);
  }

  private void validarCpf(String cpf) {
    String digits = (cpf == null) ? "" : cpf.replaceAll("\\D", "");
    if (digits.length() != 11 || digits.chars().distinct().count() == 1) {
      throw new IllegalArgumentException("CPF invalido.");
    }
    int sum = 0;
    for (int i = 0; i < 9; i += 1) {
      sum += Character.getNumericValue(digits.charAt(i)) * (10 - i);
    }
    int digit = 11 - (sum % 11);
    if (digit > 9) {
      digit = 0;
    }
    if (digit != Character.getNumericValue(digits.charAt(9))) {
      throw new IllegalArgumentException("CPF invalido.");
    }
    sum = 0;
    for (int i = 0; i < 10; i += 1) {
      sum += Character.getNumericValue(digits.charAt(i)) * (11 - i);
    }
    digit = 11 - (sum % 11);
    if (digit > 9) {
      digit = 0;
    }
    if (digit != Character.getNumericValue(digits.charAt(10))) {
      throw new IllegalArgumentException("CPF invalido.");
    }
  }
}
