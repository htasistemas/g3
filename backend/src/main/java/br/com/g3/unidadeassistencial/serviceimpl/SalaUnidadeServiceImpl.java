package br.com.g3.unidadeassistencial.serviceimpl;

import br.com.g3.unidadeassistencial.domain.SalaUnidade;
import br.com.g3.unidadeassistencial.domain.UnidadeAssistencial;
import br.com.g3.unidadeassistencial.dto.SalaUnidadeRequest;
import br.com.g3.unidadeassistencial.dto.SalaUnidadeResponse;
import br.com.g3.unidadeassistencial.repository.SalaUnidadeRepository;
import br.com.g3.unidadeassistencial.repository.UnidadeAssistencialRepository;
import br.com.g3.unidadeassistencial.service.SalaUnidadeService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SalaUnidadeServiceImpl implements SalaUnidadeService {
  private final SalaUnidadeRepository salaRepository;
  private final UnidadeAssistencialRepository unidadeRepository;

  public SalaUnidadeServiceImpl(
      SalaUnidadeRepository salaRepository, UnidadeAssistencialRepository unidadeRepository) {
    this.salaRepository = salaRepository;
    this.unidadeRepository = unidadeRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<SalaUnidadeResponse> listar(Long unidadeId) {
    List<SalaUnidade> salas =
        unidadeId == null ? salaRepository.listar() : salaRepository.listarPorUnidade(unidadeId);
    return salas.stream().map(this::toResponse).collect(Collectors.toList());
  }

  @Override
  public SalaUnidadeResponse criar(SalaUnidadeRequest request) {
    UnidadeAssistencial unidade =
        unidadeRepository
            .buscarPorId(request.getUnidadeId())
            .orElseThrow(() -> new IllegalArgumentException("Unidade não encontrada."));

    LocalDateTime agora = LocalDateTime.now();
    SalaUnidade sala = new SalaUnidade();
    sala.setUnidadeAssistencial(unidade);
    sala.setNome(request.getNome().trim());
    sala.setCriadoEm(agora);
    sala.setAtualizadoEm(agora);
    return toResponse(salaRepository.salvar(sala));
  }

  @Override
  public SalaUnidadeResponse atualizar(Long id, SalaUnidadeRequest request) {
    SalaUnidade sala =
        salaRepository
            .buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada."));

    UnidadeAssistencial unidade =
        unidadeRepository
            .buscarPorId(request.getUnidadeId())
            .orElseThrow(() -> new IllegalArgumentException("Unidade não encontrada."));

    sala.setUnidadeAssistencial(unidade);
    sala.setNome(request.getNome().trim());
    sala.setAtualizadoEm(LocalDateTime.now());
    return toResponse(salaRepository.salvar(sala));
  }

  @Override
  public void remover(Long id) {
    SalaUnidade sala =
        salaRepository
            .buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada."));
    salaRepository.remover(sala);
  }

  private SalaUnidadeResponse toResponse(SalaUnidade sala) {
    return new SalaUnidadeResponse(
        sala.getId(), sala.getUnidadeAssistencial().getId(), sala.getNome());
  }
}
