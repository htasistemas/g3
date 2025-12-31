package br.com.g3.recebimentodoacao.serviceimpl;

import br.com.g3.recebimentodoacao.domain.Doador;
import br.com.g3.recebimentodoacao.domain.RecebimentoDoacao;
import br.com.g3.recebimentodoacao.dto.DoadorRequest;
import br.com.g3.recebimentodoacao.dto.DoadorResponse;
import br.com.g3.recebimentodoacao.dto.RecebimentoDoacaoListaResponse;
import br.com.g3.recebimentodoacao.dto.RecebimentoDoacaoRequest;
import br.com.g3.recebimentodoacao.dto.RecebimentoDoacaoResponse;
import br.com.g3.recebimentodoacao.mapper.RecebimentoDoacaoMapper;
import br.com.g3.recebimentodoacao.repository.DoadorRepository;
import br.com.g3.recebimentodoacao.repository.RecebimentoDoacaoRepository;
import br.com.g3.recebimentodoacao.service.RecebimentoDoacaoService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecebimentoDoacaoServiceImpl implements RecebimentoDoacaoService {
  private final DoadorRepository doadorRepository;
  private final RecebimentoDoacaoRepository recebimentoRepository;
  private final RecebimentoDoacaoMapper mapper = new RecebimentoDoacaoMapper();

  public RecebimentoDoacaoServiceImpl(
      DoadorRepository doadorRepository,
      RecebimentoDoacaoRepository recebimentoRepository) {
    this.doadorRepository = doadorRepository;
    this.recebimentoRepository = recebimentoRepository;
  }

  @Override
  @Transactional
  public DoadorResponse criarDoador(DoadorRequest request) {
    Doador doador = mapper.toDoador(request);
    LocalDateTime now = LocalDateTime.now();
    doador.setCriadoEm(now);
    doador.setAtualizadoEm(now);
    return mapper.toDoadorResponse(doadorRepository.salvar(doador));
  }

  @Override
  public List<DoadorResponse> listarDoadores() {
    List<DoadorResponse> responses = new ArrayList<DoadorResponse>();
    for (Doador doador : doadorRepository.listar()) {
      responses.add(mapper.toDoadorResponse(doador));
    }
    return responses;
  }

  @Override
  @Transactional
  public RecebimentoDoacaoResponse criarRecebimento(RecebimentoDoacaoRequest request) {
    RecebimentoDoacao recebimento = new RecebimentoDoacao();
    mapper.applyRecebimento(recebimento, request);
    if (request.getDoadorId() != null) {
      Optional<Doador> doador = doadorRepository.buscarPorId(request.getDoadorId());
      if (doador.isPresent()) {
        recebimento.setDoador(doador.get());
      }
    }
    LocalDateTime now = LocalDateTime.now();
    recebimento.setCriadoEm(now);
    recebimento.setAtualizadoEm(now);
    recebimento.setContabilidadePendente("RECEBIDA".equalsIgnoreCase(request.getStatus()));
    return mapper.toRecebimentoResponse(recebimentoRepository.salvar(recebimento));
  }

  @Override
  @Transactional(readOnly = true)
  public RecebimentoDoacaoListaResponse listarRecebimentos() {
    List<RecebimentoDoacaoResponse> responses = new ArrayList<RecebimentoDoacaoResponse>();
    for (RecebimentoDoacao recebimento : recebimentoRepository.listar()) {      
      responses.add(mapper.toRecebimentoResponse(recebimento));
    }
    return new RecebimentoDoacaoListaResponse(responses);
  }

  @Override
  @Transactional
  public RecebimentoDoacaoResponse atualizarRecebimento(Long id, RecebimentoDoacaoRequest request) {
    RecebimentoDoacao recebimento =
        recebimentoRepository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Recebimento nao encontrado"));
    mapper.applyRecebimento(recebimento, request);
    if (request.getDoadorId() != null) {
      Optional<Doador> doador = doadorRepository.buscarPorId(request.getDoadorId());
      doador.ifPresent(recebimento::setDoador);
    }
    recebimento.setAtualizadoEm(LocalDateTime.now());
    recebimento.setContabilidadePendente("RECEBIDA".equalsIgnoreCase(request.getStatus()));
    return mapper.toRecebimentoResponse(recebimentoRepository.salvar(recebimento));
  }

  @Override
  @Transactional
  public void excluirRecebimento(Long id) {
    RecebimentoDoacao recebimento =
        recebimentoRepository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Recebimento nao encontrado"));
    recebimentoRepository.remover(recebimento);
  }
}
