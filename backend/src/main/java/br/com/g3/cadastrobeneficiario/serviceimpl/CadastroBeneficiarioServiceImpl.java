package br.com.g3.cadastrobeneficiario.serviceimpl;

import br.com.g3.cadastrobeneficiario.domain.CadastroBeneficiario;
import br.com.g3.cadastrobeneficiario.dto.CadastroBeneficiarioCriacaoRequest;
import br.com.g3.cadastrobeneficiario.dto.CadastroBeneficiarioResponse;
import br.com.g3.cadastrobeneficiario.mapper.CadastroBeneficiarioMapper;
import br.com.g3.cadastrobeneficiario.repository.CadastroBeneficiarioRepository;
import br.com.g3.cadastrobeneficiario.service.CadastroBeneficiarioService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CadastroBeneficiarioServiceImpl implements CadastroBeneficiarioService {
  private final CadastroBeneficiarioRepository repository;

  public CadastroBeneficiarioServiceImpl(CadastroBeneficiarioRepository repository) {
    this.repository = repository;
  }

  @Override
  @Transactional
  public CadastroBeneficiarioResponse criar(CadastroBeneficiarioCriacaoRequest request) {
    CadastroBeneficiario cadastro = CadastroBeneficiarioMapper.toDomain(request);
    CadastroBeneficiario salvo = repository.salvar(cadastro);
    return CadastroBeneficiarioMapper.toResponse(salvo);
  }

  @Override
  @Transactional
  public CadastroBeneficiarioResponse atualizar(Long id, CadastroBeneficiarioCriacaoRequest request) {
    CadastroBeneficiario cadastro =
        repository.buscarPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    CadastroBeneficiarioMapper.aplicarAtualizacao(cadastro, request);
    CadastroBeneficiario salvo = repository.salvar(cadastro);
    return CadastroBeneficiarioMapper.toResponse(salvo);
  }

  @Override
  public CadastroBeneficiarioResponse buscarPorId(Long id) {
    CadastroBeneficiario cadastro =
        repository.buscarPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    return CadastroBeneficiarioMapper.toResponse(cadastro);
  }

  @Override
  public List<CadastroBeneficiarioResponse> listar(String nome) {
    List<CadastroBeneficiario> cadastros =
        (nome == null || nome.trim().isEmpty()) ? repository.listar() : repository.buscarPorNome(nome);
    return cadastros.stream().map(CadastroBeneficiarioMapper::toResponse).collect(Collectors.toList());
  }

  @Override
  public void remover(Long id) {
    CadastroBeneficiario cadastro =
        repository.buscarPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    repository.remover(cadastro);
  }
}
