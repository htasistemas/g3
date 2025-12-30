package br.com.g3.autorizacaocompras.cotacoes.serviceimpl;

import br.com.g3.autorizacaocompras.cotacoes.domain.AutorizacaoCompraCotacao;
import br.com.g3.autorizacaocompras.cotacoes.dto.AutorizacaoCompraCotacaoRequest;
import br.com.g3.autorizacaocompras.cotacoes.dto.AutorizacaoCompraCotacaoResponse;
import br.com.g3.autorizacaocompras.cotacoes.mapper.AutorizacaoCompraCotacaoMapper;
import br.com.g3.autorizacaocompras.cotacoes.repository.AutorizacaoCompraCotacaoRepository;
import br.com.g3.autorizacaocompras.cotacoes.service.AutorizacaoCompraCotacaoService;
import br.com.g3.autorizacaocompras.domain.AutorizacaoCompra;
import br.com.g3.autorizacaocompras.repository.AutorizacaoComprasRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutorizacaoCompraCotacaoServiceImpl implements AutorizacaoCompraCotacaoService {
  private final AutorizacaoComprasRepository comprasRepository;
  private final AutorizacaoCompraCotacaoRepository cotacaoRepository;

  public AutorizacaoCompraCotacaoServiceImpl(
      AutorizacaoComprasRepository comprasRepository,
      AutorizacaoCompraCotacaoRepository cotacaoRepository) {
    this.comprasRepository = comprasRepository;
    this.cotacaoRepository = cotacaoRepository;
  }

  @Override
  public List<AutorizacaoCompraCotacaoResponse> listarPorCompraId(Long compraId) {
    return cotacaoRepository.listarPorCompraId(compraId).stream()
        .map(AutorizacaoCompraCotacaoMapper::toResponse)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public AutorizacaoCompraCotacaoResponse criar(
      Long compraId, AutorizacaoCompraCotacaoRequest request) {
    AutorizacaoCompra compra =
        comprasRepository
            .buscarPorId(compraId)
            .orElseThrow(() -> new IllegalArgumentException("Autorização de compra não encontrada."));
    AutorizacaoCompraCotacao cotacao = AutorizacaoCompraCotacaoMapper.toDomain(request, compra);
    cotacao.setCriadoEm(LocalDateTime.now());
    return AutorizacaoCompraCotacaoMapper.toResponse(cotacaoRepository.salvar(cotacao));
  }
}
