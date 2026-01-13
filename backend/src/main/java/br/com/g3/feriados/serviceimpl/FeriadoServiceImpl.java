package br.com.g3.feriados.serviceimpl;

import br.com.g3.feriados.domain.Feriado;
import br.com.g3.feriados.dto.FeriadoRequest;
import br.com.g3.feriados.dto.FeriadoResponse;
import br.com.g3.feriados.repository.FeriadoRepository;
import br.com.g3.feriados.service.FeriadoService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FeriadoServiceImpl implements FeriadoService {
  private final FeriadoRepository repository;

  public FeriadoServiceImpl(FeriadoRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<FeriadoResponse> listar() {
    List<FeriadoResponse> resposta = new ArrayList<>();
    for (Feriado feriado : repository.listar()) {
      resposta.add(mapResponse(feriado));
    }
    return resposta;
  }

  @Override
  @Transactional
  public FeriadoResponse criar(FeriadoRequest request) {
    if (request == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe os dados do feriado.");
    }
    Feriado feriado = new Feriado();
    feriado.setData(request.getData());
    feriado.setDescricao(request.getDescricao());
    LocalDateTime agora = LocalDateTime.now();
    feriado.setCriadoEm(agora);
    feriado.setAtualizadoEm(agora);
    Feriado salvo = repository.salvar(feriado);
    return mapResponse(salvo);
  }

  @Override
  @Transactional
  public FeriadoResponse atualizar(Long id, FeriadoRequest request) {
    Feriado feriado = repository.buscarPorId(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Feriado nao encontrado."));
    feriado.setData(request.getData());
    feriado.setDescricao(request.getDescricao());
    feriado.setAtualizadoEm(LocalDateTime.now());
    Feriado salvo = repository.salvar(feriado);
    return mapResponse(salvo);
  }

  @Override
  @Transactional
  public void excluir(Long id) {
    Feriado feriado = repository.buscarPorId(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Feriado nao encontrado."));
    repository.remover(feriado);
  }

  private FeriadoResponse mapResponse(Feriado feriado) {
    FeriadoResponse response = new FeriadoResponse();
    response.setId(feriado.getId());
    response.setData(feriado.getData());
    response.setDescricao(feriado.getDescricao());
    response.setCriadoEm(feriado.getCriadoEm());
    response.setAtualizadoEm(feriado.getAtualizadoEm());
    return response;
  }
}
