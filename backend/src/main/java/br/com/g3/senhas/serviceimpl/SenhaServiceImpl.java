package br.com.g3.senhas.serviceimpl;

import br.com.g3.cadastrobeneficiario.domain.CadastroBeneficiario;
import br.com.g3.cadastrobeneficiario.repository.CadastroBeneficiarioRepository;
import br.com.g3.senhas.domain.SenhaChamada;
import br.com.g3.senhas.domain.SenhaFila;
import br.com.g3.senhas.dto.SenhaChamadaResponse;
import br.com.g3.senhas.dto.SenhaChamarRequest;
import br.com.g3.senhas.dto.SenhaEmitirRequest;
import br.com.g3.senhas.dto.SenhaEventoResponse;
import br.com.g3.senhas.dto.SenhaFilaResponse;
import br.com.g3.senhas.realtime.SenhaRealtimeGateway;
import br.com.g3.senhas.repository.SenhaChamadaRepository;
import br.com.g3.senhas.repository.SenhaFilaRepository;
import br.com.g3.senhas.service.SenhaService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SenhaServiceImpl implements SenhaService {
  private final SenhaFilaRepository filaRepository;
  private final SenhaChamadaRepository chamadaRepository;
  private final CadastroBeneficiarioRepository beneficiarioRepository;
  private final SenhaRealtimeGateway realtimeGateway;

  public SenhaServiceImpl(
      SenhaFilaRepository filaRepository,
      SenhaChamadaRepository chamadaRepository,
      CadastroBeneficiarioRepository beneficiarioRepository,
      SenhaRealtimeGateway realtimeGateway) {
    this.filaRepository = filaRepository;
    this.chamadaRepository = chamadaRepository;
    this.beneficiarioRepository = beneficiarioRepository;
    this.realtimeGateway = realtimeGateway;
  }

  @Override
  @Transactional(readOnly = true)
  public List<SenhaFilaResponse> listarAguardando(Long unidadeId) {
    List<SenhaFila> filas =
        unidadeId == null ? filaRepository.listarAtivas() : filaRepository.listarAtivasPorUnidade(unidadeId);
    return filas.stream().map(this::mapFila).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public SenhaFilaResponse emitirSenha(SenhaEmitirRequest request) {
    CadastroBeneficiario beneficiario =
        beneficiarioRepository
            .buscarPorId(request.getBeneficiarioId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Beneficiario nao encontrado."));

    SenhaFila fila = new SenhaFila();
    fila.setBeneficiario(beneficiario);
    fila.setStatus("AGUARDANDO");
    fila.setPrioridade(request.getPrioridade() != null ? request.getPrioridade() : 0);
    LocalDateTime agora = LocalDateTime.now();
    fila.setDataHoraEntrada(agora);
    fila.setDataHoraAtualizacao(agora);
    fila.setUnidadeId(request.getUnidadeId());
    fila.setUsuarioId(request.getUsuarioId());
    fila.setSalaAtendimento(request.getSalaAtendimento());

    return mapFila(filaRepository.save(fila));
  }

  @Override
  @Transactional
  public SenhaChamadaResponse chamarSenha(SenhaChamarRequest request, String chamadoPor) {
    SenhaFila fila =
        filaRepository
            .findById(request.getFilaId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fila nao encontrada."));

    if (!"AGUARDANDO".equalsIgnoreCase(fila.getStatus()) && !"CHAMADO".equalsIgnoreCase(fila.getStatus())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fila nao esta aguardando chamada.");
    }

    LocalDateTime agora = LocalDateTime.now();
    String salaAtendimento = request.getLocalAtendimento();
    if (salaAtendimento == null || salaAtendimento.trim().isEmpty()) {
      salaAtendimento = fila.getSalaAtendimento();
    }
    if (salaAtendimento == null || salaAtendimento.trim().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sala de atendimento e obrigatoria.");
    }

    SenhaChamada chamada = new SenhaChamada();
    chamada.setId(UUID.randomUUID());
    chamada.setFila(fila);
    chamada.setNomeBeneficiario(fila.getBeneficiario().getNomeCompleto());
    chamada.setLocalAtendimento(salaAtendimento.trim());
    chamada.setStatus("CHAMADO");
    chamada.setDataHoraChamada(agora);
    chamada.setDataHoraCriacao(agora);
    chamada.setChamadoPor(chamadoPor);
    chamada.setUnidadeId(request.getUnidadeId() != null ? request.getUnidadeId() : fila.getUnidadeId());
    chamada.setUsuarioId(request.getUsuarioId());

    fila.setStatus("CHAMADO");
    fila.setDataHoraAtualizacao(agora);
    filaRepository.save(fila);

    SenhaChamada salvo = chamadaRepository.save(chamada);
    SenhaChamadaResponse response = mapChamada(salvo);
    realtimeGateway.enviarEvento(new SenhaEventoResponse("CHAMADA_SENHA", response));
    return response;
  }

  @Override
  @Transactional
  public void finalizarSenha(UUID chamadaId) {
    SenhaChamada chamada =
        chamadaRepository
            .findById(chamadaId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chamada nao encontrada."));
    chamada.setStatus("ATENDIDO");
    chamadaRepository.save(chamada);

    SenhaFila fila = chamada.getFila();
    fila.setStatus("ATENDIDO");
    fila.setDataHoraAtualizacao(LocalDateTime.now());
    filaRepository.save(fila);
  }

  @Override
  @Transactional
  public void finalizarPorFila(Long filaId) {
    SenhaFila fila =
        filaRepository
            .findById(filaId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fila nao encontrada."));
    SenhaChamada chamada = chamadaRepository.findTopByFilaIdOrderByDataHoraChamadaDesc(filaId);
    if (chamada != null) {
      chamada.setStatus("ATENDIDO");
      chamadaRepository.save(chamada);
    }
    fila.setStatus("ATENDIDO");
    fila.setDataHoraAtualizacao(LocalDateTime.now());
    filaRepository.save(fila);
  }

  @Override
  @Transactional(readOnly = true)
  public SenhaChamadaResponse obterAtual(Long unidadeId) {
    SenhaChamada chamada =
        chamadaRepository.buscarUltimaPorUnidade(unidadeId);
    return chamada != null ? mapChamada(chamada) : null;
  }

  @Override
  @Transactional(readOnly = true)
  public List<SenhaChamadaResponse> listarPainel(Long unidadeId, Integer limite) {
    List<SenhaChamada> chamadas =
        unidadeId == null
            ? chamadaRepository.listarUltimas()
            : chamadaRepository.listarUltimasPorUnidade(unidadeId);
    if (chamadas.isEmpty()) {
      return Collections.emptyList();
    }
    int tamanho = limite != null && limite > 0 ? Math.min(limite, chamadas.size()) : chamadas.size();
    return chamadas.subList(0, tamanho).stream().map(this::mapChamada).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<SenhaChamadaResponse> listarHistorico(Long unidadeId, Integer limite) {
    return listarPainel(unidadeId, limite);
  }

  private SenhaFilaResponse mapFila(SenhaFila fila) {
    return new SenhaFilaResponse(
        fila.getId(),
        fila.getBeneficiario().getId(),
        fila.getBeneficiario().getNomeCompleto(),
        fila.getStatus(),
        fila.getPrioridade(),
        fila.getDataHoraEntrada(),
        fila.getUnidadeId(),
        fila.getSalaAtendimento());
  }

  private SenhaChamadaResponse mapChamada(SenhaChamada chamada) {
    return new SenhaChamadaResponse(
        chamada.getId(),
        chamada.getFila().getId(),
        chamada.getFila().getBeneficiario().getId(),
        chamada.getNomeBeneficiario(),
        chamada.getLocalAtendimento(),
        chamada.getStatus(),
        chamada.getDataHoraChamada(),
        chamada.getUnidadeId(),
        chamada.getChamadoPor());
  }
}
