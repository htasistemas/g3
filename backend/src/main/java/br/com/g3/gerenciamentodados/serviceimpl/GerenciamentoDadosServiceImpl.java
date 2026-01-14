package br.com.g3.gerenciamentodados.serviceimpl;

import br.com.g3.gerenciamentodados.domain.GerenciamentoDadosBackup;
import br.com.g3.gerenciamentodados.domain.GerenciamentoDadosConfiguracao;
import br.com.g3.gerenciamentodados.dto.GerenciamentoDadosBackupRequest;
import br.com.g3.gerenciamentodados.dto.GerenciamentoDadosBackupResponse;
import br.com.g3.gerenciamentodados.dto.GerenciamentoDadosConfiguracaoRequest;
import br.com.g3.gerenciamentodados.dto.GerenciamentoDadosConfiguracaoResponse;
import br.com.g3.gerenciamentodados.dto.GerenciamentoDadosRestauracaoResponse;
import br.com.g3.gerenciamentodados.repository.GerenciamentoDadosBackupRepository;
import br.com.g3.gerenciamentodados.repository.GerenciamentoDadosConfiguracaoRepository;
import br.com.g3.gerenciamentodados.service.GerenciamentoDadosService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class GerenciamentoDadosServiceImpl implements GerenciamentoDadosService {
  private static final DateTimeFormatter CODIGO_FORMATO =
      DateTimeFormatter.ofPattern("yyyyMMdd", Locale.getDefault());

  private final GerenciamentoDadosConfiguracaoRepository configuracaoRepository;
  private final GerenciamentoDadosBackupRepository backupRepository;

  public GerenciamentoDadosServiceImpl(
      GerenciamentoDadosConfiguracaoRepository configuracaoRepository,
      GerenciamentoDadosBackupRepository backupRepository) {
    this.configuracaoRepository = configuracaoRepository;
    this.backupRepository = backupRepository;
  }

  @Override
  public GerenciamentoDadosConfiguracaoResponse obterConfiguracao() {
    GerenciamentoDadosConfiguracao configuracao =
        configuracaoRepository.buscarAtual().orElseGet(this::criarConfiguracaoPadrao);
    return mapearConfiguracao(configuracao);
  }

  @Override
  public GerenciamentoDadosConfiguracaoResponse salvarConfiguracao(
      GerenciamentoDadosConfiguracaoRequest request) {
    GerenciamentoDadosConfiguracao configuracao =
        configuracaoRepository.buscarAtual().orElseGet(GerenciamentoDadosConfiguracao::new);
    boolean novo = configuracao.getId() == null;
    aplicarConfiguracao(request, configuracao);
    LocalDateTime agora = LocalDateTime.now();
    if (novo) {
      configuracao.setCriadoEm(agora);
    }
    configuracao.setAtualizadoEm(agora);
    GerenciamentoDadosConfiguracao salvo = configuracaoRepository.salvar(configuracao);
    return mapearConfiguracao(salvo);
  }

  @Override
  public List<GerenciamentoDadosBackupResponse> listarBackups() {
    return backupRepository.listar().stream().map(this::mapearBackup).collect(Collectors.toList());
  }

  @Override
  public GerenciamentoDadosBackupResponse criarBackup(GerenciamentoDadosBackupRequest request) {
    LocalDateTime agora = LocalDateTime.now();
    GerenciamentoDadosConfiguracao configuracao =
        configuracaoRepository.buscarAtual().orElseGet(this::criarConfiguracaoPadrao);
    String caminhoDestino =
        safeText(configuracao.getCaminhoDestino(), "C:\\\\Backup G3");
    GerenciamentoDadosBackup backup = new GerenciamentoDadosBackup();
    backup.setCodigo("PENDENTE");
    backup.setRotulo(safeText(request.getRotulo(), "Backup manual"));
    backup.setTipo(safeText(request.getTipo(), "Completo"));
    backup.setStatus("executando");
    backup.setIniciadoEm(agora);
    backup.setArmazenadoEm(caminhoDestino);
    backup.setTamanho(null);
    backup.setCriptografado(Optional.ofNullable(request.getCriptografado()).orElse(Boolean.FALSE));
    backup.setRetencaoDias(Optional.ofNullable(request.getRetencaoDias()).orElse(0));
    backup.setCriadoEm(agora);
    backup.setAtualizadoEm(agora);
    GerenciamentoDadosBackup salvo = backupRepository.salvar(backup);
    String dataCodigo = agora.format(CODIGO_FORMATO);
    String codigo = String.format("BK-%s-%03d", dataCodigo, salvo.getId());
    salvo.setCodigo(codigo);
    salvo.setAtualizadoEm(LocalDateTime.now());
    salvo = backupRepository.salvar(salvo);
    executarBackupPostgres(salvo, caminhoDestino);
    return mapearBackup(salvo);
  }

  @Override
  public GerenciamentoDadosRestauracaoResponse restaurarBackup(Long backupId) {
    GerenciamentoDadosBackup backup =
        backupRepository
            .buscarPorId(backupId)
            .orElseThrow(() -> new IllegalArgumentException("Backup nao encontrado."));

    if (!"sucesso".equalsIgnoreCase(backup.getStatus())) {
      return new GerenciamentoDadosRestauracaoResponse(
          backupId,
          "falha",
          "Somente backups com status sucesso podem ser restaurados.",
          backup.getArmazenadoEm());
    }

    String arquivoBackup = safeText(backup.getArmazenadoEm(), "");
    if (!StringUtils.hasText(arquivoBackup)) {
      return new GerenciamentoDadosRestauracaoResponse(
          backupId,
          "falha",
          "Caminho do arquivo de backup nao encontrado.",
          backup.getArmazenadoEm());
    }

    boolean restaurado = executarRestauracaoPostgres(arquivoBackup);
    String status = restaurado ? "sucesso" : "falha";
    String mensagem =
        restaurado
            ? "Restauracao concluida com sucesso."
            : "Falha ao restaurar o backup. Verifique o arquivo e o psql.";
    return new GerenciamentoDadosRestauracaoResponse(backupId, status, mensagem, arquivoBackup);
  }

  private GerenciamentoDadosConfiguracao criarConfiguracaoPadrao() {
    LocalDateTime agora = LocalDateTime.now();
    GerenciamentoDadosConfiguracao configuracao = new GerenciamentoDadosConfiguracao();
    configuracao.setFrequencia("diario");
    configuracao.setHorarioExecucao("02:00");
    configuracao.setHorarioIncremental("13:00");
    configuracao.setRetencaoDias(15);
    configuracao.setAutomacaoAtiva(true);
    configuracao.setLimiteBanda(65);
    configuracao.setPausarHorarioComercial(true);
    configuracao.setProvedor("hibrido");
    configuracao.setCaminhoDestino("C:\\\\Backup G3");
    configuracao.setBucketNome("g3-operacional");
    configuracao.setCriptografia(true);
    configuracao.setCompressao("balanceada");
    configuracao.setVerificarIntegridade(true);
    configuracao.setEmailNotificacao("operacoes@sistema.local");
    configuracao.setCopiaExterna(true);
    configuracao.setAlertas(true);
    configuracao.setDeteccaoAnomalia(true);
    configuracao.setAutoVerificacao(true);
    configuracao.setIntegracoes("Email");
    configuracao.setSimulacaoRetencao(true);
    configuracao.setCriadoEm(agora);
    configuracao.setAtualizadoEm(agora);
    return configuracaoRepository.salvar(configuracao);
  }

  private GerenciamentoDadosConfiguracaoResponse mapearConfiguracao(
      GerenciamentoDadosConfiguracao configuracao) {
    return new GerenciamentoDadosConfiguracaoResponse(
        configuracao.getId(),
        configuracao.getFrequencia(),
        configuracao.getHorarioExecucao(),
        configuracao.getHorarioIncremental(),
        configuracao.getRetencaoDias(),
        configuracao.getAutomacaoAtiva(),
        configuracao.getLimiteBanda(),
        configuracao.getPausarHorarioComercial(),
        configuracao.getProvedor(),
        configuracao.getCaminhoDestino(),
        configuracao.getBucketNome(),
        configuracao.getCriptografia(),
        configuracao.getCompressao(),
        configuracao.getVerificarIntegridade(),
        configuracao.getEmailNotificacao(),
        configuracao.getCopiaExterna(),
        configuracao.getAlertas(),
        configuracao.getDeteccaoAnomalia(),
        configuracao.getAutoVerificacao(),
        configuracao.getIntegracoes(),
        configuracao.getSimulacaoRetencao(),
        configuracao.getAtualizadoEm());
  }

  private GerenciamentoDadosBackupResponse mapearBackup(GerenciamentoDadosBackup backup) {
    return new GerenciamentoDadosBackupResponse(
        backup.getId(),
        backup.getCodigo(),
        backup.getRotulo(),
        backup.getTipo(),
        backup.getStatus(),
        backup.getIniciadoEm(),
        backup.getArmazenadoEm(),
        backup.getTamanho(),
        backup.getCriptografado(),
        backup.getRetencaoDias());
  }

  private void aplicarConfiguracao(
      GerenciamentoDadosConfiguracaoRequest request, GerenciamentoDadosConfiguracao configuracao) {
    configuracao.setFrequencia(safeText(request.getFrequencia(), configuracao.getFrequencia()));
    configuracao.setHorarioExecucao(
        safeText(request.getHorarioExecucao(), configuracao.getHorarioExecucao()));
    configuracao.setHorarioIncremental(
        safeText(request.getHorarioIncremental(), configuracao.getHorarioIncremental()));
    configuracao.setRetencaoDias(
        Optional.ofNullable(request.getRetencaoDias()).orElse(configuracao.getRetencaoDias()));
    configuracao.setAutomacaoAtiva(
        Optional.ofNullable(request.getAutomacaoAtiva()).orElse(configuracao.getAutomacaoAtiva()));
    configuracao.setLimiteBanda(
        Optional.ofNullable(request.getLimiteBanda()).orElse(configuracao.getLimiteBanda()));
    configuracao.setPausarHorarioComercial(
        Optional.ofNullable(request.getPausarHorarioComercial())
            .orElse(configuracao.getPausarHorarioComercial()));
    configuracao.setProvedor(safeText(request.getProvedor(), configuracao.getProvedor()));
    configuracao.setCaminhoDestino(
        safeText(request.getCaminhoDestino(), configuracao.getCaminhoDestino()));
    configuracao.setBucketNome(safeText(request.getBucketNome(), configuracao.getBucketNome()));
    configuracao.setCriptografia(
        Optional.ofNullable(request.getCriptografia()).orElse(configuracao.getCriptografia()));
    configuracao.setCompressao(safeText(request.getCompressao(), configuracao.getCompressao()));
    configuracao.setVerificarIntegridade(
        Optional.ofNullable(request.getVerificarIntegridade())
            .orElse(configuracao.getVerificarIntegridade()));
    configuracao.setEmailNotificacao(
        safeText(request.getEmailNotificacao(), configuracao.getEmailNotificacao()));
    configuracao.setCopiaExterna(
        Optional.ofNullable(request.getCopiaExterna()).orElse(configuracao.getCopiaExterna()));
    configuracao.setAlertas(Optional.ofNullable(request.getAlertas()).orElse(configuracao.getAlertas()));
    configuracao.setDeteccaoAnomalia(
        Optional.ofNullable(request.getDeteccaoAnomalia()).orElse(configuracao.getDeteccaoAnomalia()));
    configuracao.setAutoVerificacao(
        Optional.ofNullable(request.getAutoVerificacao()).orElse(configuracao.getAutoVerificacao()));
    configuracao.setIntegracoes(safeText(request.getIntegracoes(), configuracao.getIntegracoes()));
    configuracao.setSimulacaoRetencao(
        Optional.ofNullable(request.getSimulacaoRetencao()).orElse(configuracao.getSimulacaoRetencao()));
  }

  private String safeText(String valor, String fallback) {
    if (!StringUtils.hasText(valor)) {
      return fallback;
    }
    return valor.trim();
  }

  private void executarBackupPostgres(GerenciamentoDadosBackup backup, String caminhoDestino) {
    String caminhoSeguro = safeText(caminhoDestino, "C:\\\\Backup G3");
    LocalDateTime agora = LocalDateTime.now();
    try {
      Path pasta = Paths.get(caminhoSeguro);
      Files.createDirectories(pasta);
      String arquivoNome = backup.getCodigo() + ".sql";
      Path arquivo = pasta.resolve(arquivoNome);

      ProcessBuilder builder =
          new ProcessBuilder(
              "pg_dump",
              "-h",
              "localhost",
              "-p",
              "5432",
              "-U",
              "postgres",
              "-F",
              "p",
              "-f",
              arquivo.toString(),
              "g3");
      builder.environment().put("PGPASSWORD", "admin");
      builder.redirectErrorStream(true);
      Process processo = builder.start();
      int resultado = processo.waitFor();
      if (resultado == 0 && Files.exists(arquivo)) {
        long tamanhoBytes = Files.size(arquivo);
        backup.setStatus("sucesso");
        backup.setArmazenadoEm(arquivo.toString());
        backup.setTamanho(String.format("%.2f MB", tamanhoBytes / (1024.0 * 1024.0)));
      } else {
        backup.setStatus("falha");
      }
    } catch (IOException | InterruptedException ex) {
      backup.setStatus("falha");
      Thread.currentThread().interrupt();
    } finally {
      backup.setAtualizadoEm(LocalDateTime.now());
      backupRepository.salvar(backup);
    }
  }

  private boolean executarRestauracaoPostgres(String arquivoBackup) {
    try {
      Path arquivo = Paths.get(arquivoBackup);
      if (!Files.exists(arquivo)) {
        return false;
      }

      ProcessBuilder builder =
          new ProcessBuilder(
              "psql",
              "-h",
              "localhost",
              "-p",
              "5432",
              "-U",
              "postgres",
              "-d",
              "g3",
              "-f",
              arquivo.toString());
      builder.environment().put("PGPASSWORD", "admin");
      builder.redirectErrorStream(true);
      Process processo = builder.start();
      int resultado = processo.waitFor();
      return resultado == 0;
    } catch (IOException | InterruptedException ex) {
      Thread.currentThread().interrupt();
      return false;
    }
  }
}
