package br.com.g3.patrimonio.serviceimpl;

import br.com.g3.patrimonio.domain.PatrimonioItem;
import br.com.g3.patrimonio.domain.PatrimonioMovimentacao;
import br.com.g3.patrimonio.dto.PatrimonioMovimentoRequest;
import br.com.g3.patrimonio.dto.PatrimonioRequest;
import br.com.g3.patrimonio.dto.PatrimonioResponse;
import br.com.g3.patrimonio.mapper.PatrimonioMapper;
import br.com.g3.patrimonio.repository.PatrimonioMovimentacaoRepository;
import br.com.g3.patrimonio.repository.PatrimonioRepository;
import br.com.g3.patrimonio.service.PatrimonioService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatrimonioServiceImpl implements PatrimonioService {
  private final PatrimonioRepository repository;
  private final PatrimonioMovimentacaoRepository movimentacaoRepository;

  public PatrimonioServiceImpl(
      PatrimonioRepository repository, PatrimonioMovimentacaoRepository movimentacaoRepository) {
    this.repository = repository;
    this.movimentacaoRepository = movimentacaoRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<PatrimonioResponse> listar() {
    return repository.listar().stream()
        .map(PatrimonioMapper::toResponse)
        .collect(java.util.stream.Collectors.toList());
  }

  @Override
  @Transactional
  public PatrimonioResponse criar(PatrimonioRequest request) {
    PatrimonioItem item = PatrimonioMapper.toDomain(request);
    PatrimonioItem salvo = repository.salvar(item);
    return PatrimonioMapper.toResponse(salvo);
  }

  @Override
  @Transactional
  public PatrimonioResponse atualizar(Long id, PatrimonioRequest request) {
    PatrimonioItem existente =
        repository.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Patrimônio não encontrado"));
    PatrimonioMapper.apply(existente, request);
    PatrimonioItem salvo = repository.salvar(existente);
    return PatrimonioMapper.toResponse(salvo);
  }

  @Override
  @Transactional
  public PatrimonioResponse registrarMovimento(Long id, PatrimonioMovimentoRequest request) {
    PatrimonioItem existente =
        repository.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Patrimônio não encontrado"));
    PatrimonioMovimentacao movimento = new PatrimonioMovimentacao();
    movimento.setTipo(request.getTipo());
    movimento.setDestino(request.getDestino());
    movimento.setResponsavel(request.getResponsavel());
    movimento.setObservacao(request.getObservacao());
    movimento.setDataMovimento(LocalDate.now());

    if ("MANUTENCAO".equalsIgnoreCase(request.getTipo())) {
      existente.setStatus("Em manutenção");
    } else if ("BAIXA".equalsIgnoreCase(request.getTipo())) {
      existente.setStatus("Baixado / Inativo");
    } else if ("MOVIMENTACAO".equalsIgnoreCase(request.getTipo())) {
      existente.setStatus("Em empréstimo");
    }

    existente.adicionarMovimento(movimento);
    movimentacaoRepository.salvar(movimento);
    PatrimonioItem salvo = repository.salvar(existente);
    return PatrimonioMapper.toResponse(salvo);
  }
}
