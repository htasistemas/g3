package br.com.g3.chamadas.fila.service;

import br.com.g3.chamadas.beneficiario.entity.BeneficiarioEntity;
import br.com.g3.chamadas.beneficiario.repository.BeneficiarioRepository;
import br.com.g3.chamadas.fila.dto.FilaAtualizacaoDto;
import br.com.g3.chamadas.fila.dto.FilaRespostaDto;
import br.com.g3.chamadas.fila.entity.FilaAtendimentoEntity;
import br.com.g3.chamadas.fila.repository.FilaAtendimentoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FilaAtendimentoService {
  private final FilaAtendimentoRepository filaRepository;
  private final BeneficiarioRepository beneficiarioRepository;

  public FilaAtendimentoService(
      FilaAtendimentoRepository filaRepository,
      BeneficiarioRepository beneficiarioRepository) {
    this.filaRepository = filaRepository;
    this.beneficiarioRepository = beneficiarioRepository;
  }

  public List<FilaRespostaDto> listarAguardando() {
    return filaRepository.findByStatusFilaOrderByPrioridadeDescDataHoraEntradaAsc("AGUARDANDO")
        .stream()
        .map(this::mapear)
        .collect(Collectors.toList());
  }

  public FilaRespostaDto entrarFila(FilaAtualizacaoDto dto) {
    BeneficiarioEntity beneficiario = beneficiarioRepository.findById(dto.getIdBeneficiario())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Beneficiario nao encontrado."));

    if (Boolean.FALSE.equals(beneficiario.getAtivo())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Beneficiario inativo.");
    }

    LocalDateTime agora = LocalDateTime.now();
    FilaAtendimentoEntity fila = new FilaAtendimentoEntity();
    fila.setBeneficiario(beneficiario);
    fila.setStatusFila("AGUARDANDO");
    fila.setPrioridade(dto.getPrioridade() != null ? dto.getPrioridade() : 0);
    fila.setDataHoraEntrada(agora);
    fila.setDataHoraAtualizacao(agora);
    return mapear(filaRepository.save(fila));
  }

  public FilaAtendimentoEntity buscarPorId(Long id) {
    return filaRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fila nao encontrada."));
  }

  public void atualizarStatus(FilaAtendimentoEntity fila, String status) {
    fila.setStatusFila(status);
    fila.setDataHoraAtualizacao(LocalDateTime.now());
    filaRepository.save(fila);
  }

  private FilaRespostaDto mapear(FilaAtendimentoEntity fila) {
    return new FilaRespostaDto(
        fila.getIdFilaAtendimento(),
        fila.getBeneficiario().getIdBeneficiario(),
        fila.getBeneficiario().getNomeBeneficiario(),
        fila.getStatusFila(),
        fila.getPrioridade(),
        fila.getDataHoraEntrada());
  }
}
