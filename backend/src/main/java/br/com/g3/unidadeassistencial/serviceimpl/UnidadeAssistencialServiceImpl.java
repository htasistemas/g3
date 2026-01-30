package br.com.g3.unidadeassistencial.serviceimpl;

import br.com.g3.unidadeassistencial.domain.UnidadeAssistencial;
import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialCriacaoRequest;
import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialResponse;
import br.com.g3.unidadeassistencial.mapper.UnidadeAssistencialMapper;
import br.com.g3.unidadeassistencial.repository.UnidadeAssistencialRepository;
import br.com.g3.unidadeassistencial.service.UnidadeAssistencialService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UnidadeAssistencialServiceImpl implements UnidadeAssistencialService {
  private final UnidadeAssistencialRepository repository;

  public UnidadeAssistencialServiceImpl(UnidadeAssistencialRepository repository) {
    this.repository = repository;
  }

  @Override
  @Transactional
  public UnidadeAssistencialResponse criar(UnidadeAssistencialCriacaoRequest request) {
    if (request.isUnidadePrincipal()) {
      repository.limparUnidadePrincipal();
    }
    UnidadeAssistencial unidade = UnidadeAssistencialMapper.toDomain(request);
    UnidadeAssistencial salvo = repository.salvar(unidade);
    return UnidadeAssistencialMapper.toResponse(salvo);
  }

  @Override
  public List<UnidadeAssistencialResponse> listar() {
    return repository.listar().stream()
        .map(UnidadeAssistencialMapper::toResponse)
        .collect(Collectors.toList());
  }

  @Override
  public UnidadeAssistencialResponse obterAtual() {
    return repository.buscarAtual()
        .map(UnidadeAssistencialMapper::toResponse)
        .orElse(null);
  }

  @Override
  @Transactional
  public UnidadeAssistencialResponse atualizar(Long id, UnidadeAssistencialCriacaoRequest request) {
    UnidadeAssistencial unidade =
        repository.buscarPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    if (request.isUnidadePrincipal()) {
      repository.limparUnidadePrincipal();
    }
    UnidadeAssistencialMapper.aplicarAtualizacao(unidade, request);
    UnidadeAssistencial salvo = repository.salvar(unidade);
    return UnidadeAssistencialMapper.toResponse(salvo);
  }

  @Override
  public void remover(Long id) {
    UnidadeAssistencial unidade =
        repository.buscarPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    repository.remover(unidade);
  }
}
