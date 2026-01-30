package br.com.g3.configuracoes.serviceimpl;

import br.com.g3.configuracoes.domain.HistoricoVersaoSistema;
import br.com.g3.configuracoes.domain.VersaoSistema;
import br.com.g3.configuracoes.dto.AtualizarVersaoRequest;
import br.com.g3.configuracoes.dto.DestinoChamadoResponse;
import br.com.g3.configuracoes.dto.HistoricoVersaoResponse;
import br.com.g3.configuracoes.dto.VersaoSistemaResponse;
import br.com.g3.configuracoes.repository.HistoricoVersaoSistemaRepository;
import br.com.g3.configuracoes.repository.VersaoSistemaRepository;
import br.com.g3.configuracoes.service.ConfiguracaoSistemaService;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ConfiguracaoSistemaServiceImpl implements ConfiguracaoSistemaService {
  private static final String CAMINHO_VERSAO = "classpath:static/version.txt";
  private static final String DESCRICAO_ATUALIZACAO_AUTOMATICA =
      "Atualizacao automatica via version.txt.";
  private final VersaoSistemaRepository versaoRepository;
  private final HistoricoVersaoSistemaRepository historicoRepository;
  private final String destinoChamado;
  private final ResourceLoader resourceLoader;

  public ConfiguracaoSistemaServiceImpl(
      VersaoSistemaRepository versaoRepository,
      HistoricoVersaoSistemaRepository historicoRepository,
      @Value("${app.email.chamados-destino:htasistemas@gmail.com}") String destinoChamado,
      ResourceLoader resourceLoader) {
    this.versaoRepository = versaoRepository;
    this.historicoRepository = historicoRepository;
    this.destinoChamado = destinoChamado;
    this.resourceLoader = resourceLoader;
  }

  @Override
  public VersaoSistemaResponse obterVersaoAtual() {
    Optional<VersaoSistema> versaoAtual = versaoRepository.buscarAtual();
    String versaoArquivo = carregarVersaoArquivo();

    if (versaoArquivo != null
        && !versaoArquivo.equals(safeTrim(versaoAtual.map(VersaoSistema::getVersao).orElse(null)))) {
      VersaoSistema versao = versaoAtual.orElseGet(VersaoSistema::new);
      versao.setVersao(versaoArquivo);
      versao.setDescricao(DESCRICAO_ATUALIZACAO_AUTOMATICA);
      versao.setAtualizadoEm(LocalDateTime.now());
      VersaoSistema salvo = versaoRepository.salvar(versao);
      registrarHistorico(salvo);
      return new VersaoSistemaResponse(salvo.getVersao(), salvo.getDescricao(), salvo.getAtualizadoEm());
    }
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

    registrarHistorico(salvo);

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

  @Override
  public DestinoChamadoResponse obterDestinoChamados() {
    return new DestinoChamadoResponse(destinoChamado);
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

  private String carregarVersaoArquivo() {
    Resource recurso = resourceLoader.getResource(CAMINHO_VERSAO);
    if (!recurso.exists()) {
      return null;
    }
    try (BufferedReader leitor =
        new BufferedReader(new InputStreamReader(recurso.getInputStream(), StandardCharsets.UTF_8))) {
      String versao = leitor.readLine();
      return safeTrim(versao);
    } catch (Exception ex) {
      return null;
    }
  }

  private void registrarHistorico(VersaoSistema versao) {
    HistoricoVersaoSistema historico = new HistoricoVersaoSistema();
    historico.setVersao(versao.getVersao());
    historico.setDescricao(versao.getDescricao());
    historico.setCriadoEm(LocalDateTime.now());
    historicoRepository.salvar(historico);
  }
}

