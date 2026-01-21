package br.com.g3.chamadas.chamada.service;

import br.com.g3.chamadas.chamada.dto.ChamadaCriarDto;
import br.com.g3.chamadas.chamada.dto.ChamadaRespostaDto;
import br.com.g3.chamadas.chamada.entity.ChamadaEntity;
import br.com.g3.chamadas.chamada.realtime.ChamadaEventoDto;
import br.com.g3.chamadas.chamada.realtime.ChamadaRealtimeGateway;
import br.com.g3.chamadas.chamada.repository.ChamadaRepository;
import br.com.g3.chamadas.fila.entity.FilaAtendimentoEntity;
import br.com.g3.chamadas.fila.service.FilaAtendimentoService;
import br.com.g3.chamadas.shared.ValidacaoUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ChamadaService {
  private final ChamadaRepository chamadaRepository;
  private final FilaAtendimentoService filaService;
  private final ChamadaRealtimeGateway realtimeGateway;

  public ChamadaService(
      ChamadaRepository chamadaRepository,
      FilaAtendimentoService filaService,
      ChamadaRealtimeGateway realtimeGateway) {
    this.chamadaRepository = chamadaRepository;
    this.filaService = filaService;
    this.realtimeGateway = realtimeGateway;
  }

  public ChamadaRespostaDto chamar(ChamadaCriarDto dto, String chamadoPor) {
    ValidacaoUtil.textoObrigatorio(dto.getLocalAtendimento(), "Local de atendimento obrigatorio.");
    FilaAtendimentoEntity fila = filaService.buscarPorId(dto.getIdFilaAtendimento());

    if (!"AGUARDANDO".equalsIgnoreCase(fila.getStatusFila())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fila nao esta aguardando.");
    }

    LocalDateTime agora = LocalDateTime.now();
    ChamadaEntity chamada = new ChamadaEntity();
    chamada.setIdChamada(UUID.randomUUID());
    chamada.setFilaAtendimento(fila);
    chamada.setNomeBeneficiario(fila.getBeneficiario().getNomeBeneficiario());
    chamada.setLocalAtendimento(dto.getLocalAtendimento().trim());
    chamada.setStatusChamada("CHAMADO");
    chamada.setDataHoraChamada(agora);
    chamada.setDataHoraCriacao(agora);
    chamada.setChamadoPor(chamadoPor);

    filaService.atualizarStatus(fila, "CHAMADO");
    ChamadaEntity salvo = chamadaRepository.save(chamada);

    ChamadaEventoDto evento = new ChamadaEventoDto(
        "CHAMADA_BENEFICIARIO",
        new ChamadaEventoDto.Dados(
            salvo.getIdChamada(),
            salvo.getNomeBeneficiario(),
            salvo.getLocalAtendimento(),
            salvo.getDataHoraChamada()));
    realtimeGateway.enviarChamada(evento);

    return mapear(salvo);
  }

  public List<ChamadaRespostaDto> listarUltimas(Integer limite) {
    List<ChamadaEntity> chamadas = chamadaRepository.findTop10ByOrderByDataHoraChamadaDesc();
    if (limite != null && limite > 0 && limite < chamadas.size()) {
      chamadas = chamadas.subList(0, limite);
    }
    return chamadas.stream().map(this::mapear).collect(Collectors.toList());
  }

  public ChamadaRespostaDto ultimaChamada() {
    return chamadaRepository.findTop1ByOrderByDataHoraChamadaDesc()
        .map(this::mapear)
        .orElse(null);
  }

  private ChamadaRespostaDto mapear(ChamadaEntity chamada) {
    return new ChamadaRespostaDto(
        chamada.getIdChamada(),
        chamada.getFilaAtendimento().getIdFilaAtendimento(),
        chamada.getNomeBeneficiario(),
        chamada.getLocalAtendimento(),
        chamada.getStatusChamada(),
        chamada.getDataHoraChamada(),
        chamada.getChamadoPor());
  }
}
