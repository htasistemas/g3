package br.com.g3.autorizacaocompras.serviceimpl;

import br.com.g3.autorizacaocompras.domain.AutorizacaoCompra;
import br.com.g3.autorizacaocompras.dto.AutorizacaoCompraRequest;
import br.com.g3.autorizacaocompras.dto.AutorizacaoCompraResponse;
import br.com.g3.autorizacaocompras.mapper.AutorizacaoComprasMapper;
import br.com.g3.autorizacaocompras.repository.AutorizacaoComprasRepository;
import br.com.g3.autorizacaocompras.service.AutorizacaoComprasService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class AutorizacaoComprasServiceImpl implements AutorizacaoComprasService {
  private final AutorizacaoComprasRepository repository;

  public AutorizacaoComprasServiceImpl(AutorizacaoComprasRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<AutorizacaoCompraResponse> listar() {
    return repository.listar().stream().map(AutorizacaoComprasMapper::toResponse).collect(Collectors.toList());
  }

  @Override
  public AutorizacaoCompraResponse buscarPorId(Long id) {
    return repository.buscarPorId(id)
        .map(AutorizacaoComprasMapper::toResponse)
        .orElseThrow(() -> new IllegalArgumentException("Autorização não encontrada."));
  }

  @Override
  @Transactional
  public AutorizacaoCompraResponse criar(AutorizacaoCompraRequest request) {
    AutorizacaoCompra compra = AutorizacaoComprasMapper.toDomain(request);
    LocalDateTime agora = LocalDateTime.now();
    compra.setCriadoEm(agora);
    compra.setAtualizadoEm(agora);
    compra.setStatus(resolveStatus(request.getStatus()));
    return AutorizacaoComprasMapper.toResponse(repository.salvar(compra));
  }

  @Override
  @Transactional
  public AutorizacaoCompraResponse atualizar(Long id, AutorizacaoCompraRequest request) {
    AutorizacaoCompra compra =
        repository.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Autorização não encontrada."));
    AutorizacaoComprasMapper.aplicarAtualizacao(compra, request);
    compra.setAtualizadoEm(LocalDateTime.now());
    compra.setStatus(resolveStatus(request.getStatus()));
    return AutorizacaoComprasMapper.toResponse(repository.salvar(compra));
  }

  @Override
  @Transactional
  public void remover(Long id) {
    AutorizacaoCompra compra =
        repository.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Autorização não encontrada."));
    repository.remover(compra);
  }

  private String resolveStatus(String status) {
    return StringUtils.hasText(status) ? status : "solicitacao";
  }
}


