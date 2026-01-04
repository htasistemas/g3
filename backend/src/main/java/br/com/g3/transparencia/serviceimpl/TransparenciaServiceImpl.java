package br.com.g3.transparencia.serviceimpl;

import br.com.g3.transparencia.domain.Transparencia;
import br.com.g3.transparencia.domain.TransparenciaChecklist;
import br.com.g3.transparencia.domain.TransparenciaComprovante;
import br.com.g3.transparencia.domain.TransparenciaDestinacao;
import br.com.g3.transparencia.domain.TransparenciaRecebimento;
import br.com.g3.transparencia.domain.TransparenciaTimeline;
import br.com.g3.transparencia.dto.TransparenciaChecklistRequest;
import br.com.g3.transparencia.dto.TransparenciaChecklistResponse;
import br.com.g3.transparencia.dto.TransparenciaComprovanteRequest;
import br.com.g3.transparencia.dto.TransparenciaComprovanteResponse;
import br.com.g3.transparencia.dto.TransparenciaDestinacaoRequest;
import br.com.g3.transparencia.dto.TransparenciaDestinacaoResponse;
import br.com.g3.transparencia.dto.TransparenciaListaResponse;
import br.com.g3.transparencia.dto.TransparenciaRecebimentoRequest;
import br.com.g3.transparencia.dto.TransparenciaRecebimentoResponse;
import br.com.g3.transparencia.dto.TransparenciaRequest;
import br.com.g3.transparencia.dto.TransparenciaResponse;
import br.com.g3.transparencia.dto.TransparenciaTimelineRequest;
import br.com.g3.transparencia.dto.TransparenciaTimelineResponse;
import br.com.g3.transparencia.repository.TransparenciaChecklistRepository;
import br.com.g3.transparencia.repository.TransparenciaComprovanteRepository;
import br.com.g3.transparencia.repository.TransparenciaDestinacaoRepository;
import br.com.g3.transparencia.repository.TransparenciaRecebimentoRepository;
import br.com.g3.transparencia.repository.TransparenciaRepository;
import br.com.g3.transparencia.repository.TransparenciaTimelineRepository;
import br.com.g3.transparencia.service.TransparenciaService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TransparenciaServiceImpl implements TransparenciaService {
  private final TransparenciaRepository repository;
  private final TransparenciaRecebimentoRepository recebimentoRepository;
  private final TransparenciaDestinacaoRepository destinacaoRepository;
  private final TransparenciaComprovanteRepository comprovanteRepository;
  private final TransparenciaTimelineRepository timelineRepository;
  private final TransparenciaChecklistRepository checklistRepository;

  public TransparenciaServiceImpl(
      TransparenciaRepository repository,
      TransparenciaRecebimentoRepository recebimentoRepository,
      TransparenciaDestinacaoRepository destinacaoRepository,
      TransparenciaComprovanteRepository comprovanteRepository,
      TransparenciaTimelineRepository timelineRepository,
      TransparenciaChecklistRepository checklistRepository) {
    this.repository = repository;
    this.recebimentoRepository = recebimentoRepository;
    this.destinacaoRepository = destinacaoRepository;
    this.comprovanteRepository = comprovanteRepository;
    this.timelineRepository = timelineRepository;
    this.checklistRepository = checklistRepository;
  }

  @Override
  public TransparenciaListaResponse listar() {
    List<TransparenciaResponse> resposta = new ArrayList<>();
    for (Transparencia transparencia : repository.listar()) {
      resposta.add(mapTransparencia(transparencia));
    }
    return new TransparenciaListaResponse(resposta);
  }

  @Override
  public TransparenciaResponse obter(Long id) {
    return mapTransparencia(buscarTransparencia(id));
  }

  @Override
  @Transactional
  public TransparenciaResponse criar(TransparenciaRequest request) {
    Transparencia transparencia = mapRequest(new Transparencia(), request);
    LocalDateTime agora = LocalDateTime.now();
    transparencia.setCriadoEm(agora);
    transparencia.setAtualizadoEm(agora);
    Transparencia salvo = repository.salvar(transparencia);
    salvarDetalhes(salvo.getId(), request);
    return mapTransparencia(buscarTransparencia(salvo.getId()));
  }

  @Override
  @Transactional
  public TransparenciaResponse atualizar(Long id, TransparenciaRequest request) {
    Transparencia transparencia = buscarTransparencia(id);
    mapRequest(transparencia, request);
    transparencia.setAtualizadoEm(LocalDateTime.now());
    Transparencia salvo = repository.salvar(transparencia);
    removerDetalhes(salvo.getId());
    salvarDetalhes(salvo.getId(), request);
    return mapTransparencia(buscarTransparencia(salvo.getId()));
  }

  @Override
  @Transactional
  public void excluir(Long id) {
    Transparencia transparencia = buscarTransparencia(id);
    removerDetalhes(id);
    repository.remover(transparencia);
  }

  private Transparencia buscarTransparencia(Long id) {
    Optional<Transparencia> transparencia = repository.buscarPorId(id);
    if (!transparencia.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transparencia nao encontrada.");
    }
    return transparencia.get();
  }

  private Transparencia mapRequest(Transparencia transparencia, TransparenciaRequest request) {
    transparencia.setUnidadeId(request.getUnidadeId());
    transparencia.setTotalRecebido(request.getTotalRecebido());
    transparencia.setTotalRecebidoHelper(request.getTotalRecebidoHelper());
    transparencia.setTotalAplicado(request.getTotalAplicado());
    transparencia.setTotalAplicadoHelper(request.getTotalAplicadoHelper());
    transparencia.setSaldoDisponivel(request.getSaldoDisponivel());
    transparencia.setSaldoDisponivelHelper(request.getSaldoDisponivelHelper());
    transparencia.setPrestadoMes(request.getPrestadoMes());
    transparencia.setPrestadoMesHelper(request.getPrestadoMesHelper());
    return transparencia;
  }

  private void removerDetalhes(Long transparenciaId) {
    recebimentoRepository.removerPorTransparencia(transparenciaId);
    destinacaoRepository.removerPorTransparencia(transparenciaId);
    comprovanteRepository.removerPorTransparencia(transparenciaId);
    timelineRepository.removerPorTransparencia(transparenciaId);
    checklistRepository.removerPorTransparencia(transparenciaId);
  }

  private void salvarDetalhes(Long transparenciaId, TransparenciaRequest request) {
    LocalDateTime agora = LocalDateTime.now();
    if (request.getRecebimentos() != null) {
      for (TransparenciaRecebimentoRequest item : request.getRecebimentos()) {
        TransparenciaRecebimento recebimento = new TransparenciaRecebimento();
        recebimento.setTransparenciaId(transparenciaId);
        recebimento.setFonte(item.getFonte());
        recebimento.setValor(item.getValor());
        recebimento.setPeriodicidade(item.getPeriodicidade());
        recebimento.setStatus(item.getStatus());
        recebimento.setCriadoEm(agora);
        recebimentoRepository.salvar(recebimento);
      }
    }
    if (request.getDestinacoes() != null) {
      for (TransparenciaDestinacaoRequest item : request.getDestinacoes()) {
        TransparenciaDestinacao destinacao = new TransparenciaDestinacao();
        destinacao.setTransparenciaId(transparenciaId);
        destinacao.setTitulo(item.getTitulo());
        destinacao.setDescricao(item.getDescricao());
        destinacao.setPercentual(item.getPercentual());
        destinacao.setCriadoEm(agora);
        destinacaoRepository.salvar(destinacao);
      }
    }
    if (request.getComprovantes() != null) {
      for (TransparenciaComprovanteRequest item : request.getComprovantes()) {
        TransparenciaComprovante comprovante = new TransparenciaComprovante();
        comprovante.setTransparenciaId(transparenciaId);
        comprovante.setTitulo(item.getTitulo());
        comprovante.setDescricao(item.getDescricao());
        comprovante.setArquivoNome(item.getArquivoNome());
        comprovante.setArquivoUrl(item.getArquivoUrl());
        comprovante.setCriadoEm(agora);
        comprovanteRepository.salvar(comprovante);
      }
    }
    if (request.getTimelines() != null) {
      for (TransparenciaTimelineRequest item : request.getTimelines()) {
        TransparenciaTimeline timeline = new TransparenciaTimeline();
        timeline.setTransparenciaId(transparenciaId);
        timeline.setTitulo(item.getTitulo());
        timeline.setDetalhe(item.getDetalhe());
        timeline.setStatus(item.getStatus());
        timeline.setCriadoEm(agora);
        timelineRepository.salvar(timeline);
      }
    }
    if (request.getChecklist() != null) {
      for (TransparenciaChecklistRequest item : request.getChecklist()) {
        TransparenciaChecklist checklist = new TransparenciaChecklist();
        checklist.setTransparenciaId(transparenciaId);
        checklist.setTitulo(item.getTitulo());
        checklist.setDescricao(item.getDescricao());
        checklist.setStatus(item.getStatus());
        checklist.setCriadoEm(agora);
        checklistRepository.salvar(checklist);
      }
    }
  }

  private TransparenciaResponse mapTransparencia(Transparencia transparencia) {
    List<TransparenciaRecebimentoResponse> recebimentos = new ArrayList<>();
    for (TransparenciaRecebimento item : recebimentoRepository.listarPorTransparencia(transparencia.getId())) {
      recebimentos.add(
          new TransparenciaRecebimentoResponse(
              item.getId(),
              item.getFonte(),
              item.getValor(),
              item.getPeriodicidade(),
              item.getStatus()));
    }
    List<TransparenciaDestinacaoResponse> destinacoes = new ArrayList<>();
    for (TransparenciaDestinacao item : destinacaoRepository.listarPorTransparencia(transparencia.getId())) {
      destinacoes.add(
          new TransparenciaDestinacaoResponse(
              item.getId(),
              item.getTitulo(),
              item.getDescricao(),
              item.getPercentual()));
    }
    List<TransparenciaComprovanteResponse> comprovantes = new ArrayList<>();
    for (TransparenciaComprovante item : comprovanteRepository.listarPorTransparencia(transparencia.getId())) {
      comprovantes.add(
          new TransparenciaComprovanteResponse(
              item.getId(),
              item.getTitulo(),
              item.getDescricao(),
              item.getArquivoNome(),
              item.getArquivoUrl()));
    }
    List<TransparenciaTimelineResponse> timelines = new ArrayList<>();
    for (TransparenciaTimeline item : timelineRepository.listarPorTransparencia(transparencia.getId())) {
      timelines.add(
          new TransparenciaTimelineResponse(
              item.getId(),
              item.getTitulo(),
              item.getDetalhe(),
              item.getStatus()));
    }
    List<TransparenciaChecklistResponse> checklist = new ArrayList<>();
    for (TransparenciaChecklist item : checklistRepository.listarPorTransparencia(transparencia.getId())) {
      checklist.add(
          new TransparenciaChecklistResponse(
              item.getId(),
              item.getTitulo(),
              item.getDescricao(),
              item.getStatus()));
    }
    return new TransparenciaResponse(
        transparencia.getId(),
        transparencia.getUnidadeId(),
        transparencia.getTotalRecebido(),
        transparencia.getTotalRecebidoHelper(),
        transparencia.getTotalAplicado(),
        transparencia.getTotalAplicadoHelper(),
        transparencia.getSaldoDisponivel(),
        transparencia.getSaldoDisponivelHelper(),
        transparencia.getPrestadoMes(),
        transparencia.getPrestadoMesHelper(),
        recebimentos,
        destinacoes,
        comprovantes,
        timelines,
        checklist);
  }
}
