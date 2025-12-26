package br.com.g3.cadastroprofissionais.serviceimpl;

import br.com.g3.cadastroprofissionais.domain.CadastroProfissional;
import br.com.g3.cadastroprofissionais.dto.CadastroProfissionalCriacaoRequest;
import br.com.g3.cadastroprofissionais.dto.CadastroProfissionalResponse;
import br.com.g3.cadastroprofissionais.mapper.CadastroProfissionalMapper;
import br.com.g3.cadastroprofissionais.repository.CadastroProfissionalRepository;
import br.com.g3.cadastroprofissionais.service.CadastroProfissionalService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CadastroProfissionalServiceImpl implements CadastroProfissionalService {
  private final CadastroProfissionalRepository repository;

  public CadastroProfissionalServiceImpl(CadastroProfissionalRepository repository) {
    this.repository = repository;
  }

  @Override
  @Transactional
  public CadastroProfissionalResponse criar(CadastroProfissionalCriacaoRequest request) {
    CadastroProfissional cadastro = CadastroProfissionalMapper.toDomain(request);
    CadastroProfissional salvo = repository.salvar(cadastro);
    return CadastroProfissionalMapper.toResponse(salvo);
  }

  @Override
  @Transactional
  public CadastroProfissionalResponse atualizar(Long id, CadastroProfissionalCriacaoRequest request) {
    CadastroProfissional cadastro =
        repository.buscarPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    CadastroProfissionalMapper.aplicarAtualizacao(cadastro, request);
    CadastroProfissional salvo = repository.salvar(cadastro);
    return CadastroProfissionalMapper.toResponse(salvo);
  }

  @Override
  public CadastroProfissionalResponse buscarPorId(Long id) {
    CadastroProfissional cadastro =
        repository.buscarPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    return CadastroProfissionalMapper.toResponse(cadastro);
  }

  @Override
  public List<CadastroProfissionalResponse> listar(String nome) {
    List<CadastroProfissional> cadastros =
        (nome == null || nome.trim().isEmpty()) ? repository.listar() : repository.buscarPorNome(nome);
    return cadastros.stream().map(CadastroProfissionalMapper::toResponse).collect(Collectors.toList());
  }

  @Override
  public void remover(Long id) {
    CadastroProfissional cadastro =
        repository.buscarPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    repository.remover(cadastro);
  }
}
