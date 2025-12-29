package br.com.g3.configuracoes.serviceimpl;

import br.com.g3.configuracoes.domain.HistoricoVersaoSistema;
import br.com.g3.configuracoes.domain.VersaoSistema;
import br.com.g3.configuracoes.dto.AtualizarVersaoRequest;
import br.com.g3.configuracoes.dto.HistoricoVersaoResponse;
import br.com.g3.configuracoes.dto.VersaoSistemaResponse;
import br.com.g3.configuracoes.repository.HistoricoVersaoSistemaRepository;
import br.com.g3.configuracoes.repository.VersaoSistemaRepository;
import br.com.g3.configuracoes.service.ConfiguracaoSistemaService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ConfiguracaoSistemaServiceImpl implements ConfiguracaoSistemaService {
  private final VersaoSistemaRepository versaoRepository;
  private final HistoricoVersaoSistemaRepository historicoRepository;

  public ConfiguracaoSistemaServiceImpl(
      VersaoSistemaRepository versaoRepository,
      HistoricoVersaoSistemaRepository historicoRepository) {
    this.versaoRepository = versaoRepository;
    this.historicoRepository = historicoRepository;
  }

  @Override
  public VersaoSistemaResponse obterVersaoAtual() {
    Optional<VersaoSistema> versaoAtual = versaoRepository.buscarAtual();
    if (!versaoAtual.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Versão do sistema não encontrada.");
    }
    VersaoSistema versao = versaoAtual.get();
    return new VersaoSistemaResponse(versao.getVersao(), versao.getDescricao(), versao.getAtualizadoEm());
  }

  @Override
  public VersaoSistemaResponse atualizarVersao(AtualizarVersaoRequest request) {
    if (isBlank(request.getVersao())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Versão é obrigatória.");
    }

    VersaoSistema versao = versaoRepository.buscarAtual().orElseGet(VersaoSistema::new);
    versao.setVersao(request.getVersao().trim());
    versao.setDescricao(safeTrim(request.getDescricao()));
    versao.setAtualizadoEm(LocalDateTime.now());
    VersaoSistema salvo = versaoRepository.salvar(versao);

    HistoricoVersaoSistema historico = new HistoricoVersaoSistema();
    historico.setVersao(salvo.getVersao());
    historico.setDescricao(salvo.getDescricao());
    historico.setCriadoEm(LocalDateTime.now());
    historicoRepository.salvar(historico);

    return new VersaoSistemaResponse(salvo.getVersao(), salvo.getDescricao(), salvo.getAtualizadoEm());
  }

  @Override
  public List<HistoricoVersaoResponse> listarHistorico() {
    return historicoRepository.listar().stream()
        .map(
            item ->
                new HistoricoVersaoResponse(
                    item.getId(), item.getVersao(), item.getDescricao(), item.getCriadoEm()))
        .collect(Collectors.toList());
  }

  private boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }

  private String safeTrim(String value) {
    if (value == null) {
      return null;
    }
    String trimmed = value.trim();
    return trimmed.isEmpty() ? null : trimmed;
  }
}
