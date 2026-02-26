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
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class GerenciamentoDadosServiceImpl implements GerenciamentoDadosService {
  private static final DateTimeFormatter CODIGO_FORMATO =
      DateTimeFormatter.ofPattern("yyyyMMdd", Locale.getDefault());
  private static final Logger LOGGER = LoggerFactory.getLogger(GerenciamentoDadosServiceImpl.class);

  @Value("${spring.datasource.url:jdbc:postgresql://localhost:5432/g3}")
  private String datasourceUrl;

  @Value("${spring.datasource.username:postgres}")
  private String datasourceUsuario;

  @Value("${spring.datasource.password:}")
  private String datasourceSenha;

  @Value("${app.backup.pg-dump-path:pg_dump}")
  private String pgDumpPath;

  @Value("${app.backup.psql-path:psql}")
  private String psqlPath;

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
      PostgresConfig postgresConfig = obterConfiguracaoPostgres();
      Path pasta = Paths.get(caminhoSeguro);
      Files.createDirectories(pasta);
      String arquivoNome = backup.getCodigo() + ".sql";
      Path arquivo = pasta.resolve(arquivoNome);

      List<String> comando = new ArrayList<>();
      comando.add(pgDumpPath);
      comando.add("-h");
      comando.add(postgresConfig.host);
      comando.add("-p");
      comando.add(String.valueOf(postgresConfig.porta));
      comando.add("-U");
      comando.add(postgresConfig.usuario);
      comando.add("--clean");
      comando.add("--if-exists");
      comando.add("-F");
      comando.add("p");
      comando.add("-f");
      comando.add(arquivo.toString());
      comando.add(postgresConfig.banco);

      ProcessBuilder builder = new ProcessBuilder(comando);
      if (StringUtils.hasText(postgresConfig.senha)) {
        builder.environment().put("PGPASSWORD", postgresConfig.senha);
      }
      builder.redirectErrorStream(true);
      Process processo = builder.start();
      String saida = lerSaidaProcesso(processo);
      int resultado = processo.waitFor();
      if (resultado == 0 && Files.exists(arquivo)) {
        long tamanhoBytes = Files.size(arquivo);
        backup.setStatus("sucesso");
        backup.setArmazenadoEm(arquivo.toString());
        backup.setTamanho(String.format("%.2f MB", tamanhoBytes / (1024.0 * 1024.0)));
      } else {
        backup.setStatus("falha");
        LOGGER.error(
            "Falha ao executar backup. Codigo: {}. Saida: {}",
            backup.getCodigo(),
            limitarTexto(saida, 2000));
      }
    } catch (IOException | InterruptedException ex) {
      backup.setStatus("falha");
      Thread.currentThread().interrupt();
      LOGGER.error("Falha ao executar backup. Codigo: {}.", backup.getCodigo(), ex);
    } finally {
      backup.setAtualizadoEm(LocalDateTime.now());
      backupRepository.salvar(backup);
    }
  }

  private boolean executarRestauracaoPostgres(String arquivoBackup) {
    try {
      PostgresConfig postgresConfig = obterConfiguracaoPostgres();
      Path arquivo = Paths.get(arquivoBackup);
      if (!Files.exists(arquivo)) {
        return false;
      }

      boolean schemaLimpo =
          executarComandoPsql(
              montarComandoPsql(
                  postgresConfig,
                  List.of(
                      "-v",
                      "ON_ERROR_STOP=1",
                      "-c",
                      "DROP SCHEMA public CASCADE; CREATE SCHEMA public;")),
              postgresConfig.senha);
      if (!schemaLimpo) {
        return false;
      }

      List<String> comando =
          montarComandoPsql(
              postgresConfig,
              List.of("-v", "ON_ERROR_STOP=1", "-f", arquivo.toString()));
      ProcessBuilder builder = new ProcessBuilder(comando);
      if (StringUtils.hasText(postgresConfig.senha)) {
        builder.environment().put("PGPASSWORD", postgresConfig.senha);
      }
      builder.redirectErrorStream(true);
      Process processo = builder.start();
      String saida = lerSaidaProcesso(processo);
      int resultado = processo.waitFor();
      if (resultado != 0) {
        LOGGER.error("Falha ao restaurar backup. Saida: {}", limitarTexto(saida, 2000));
      }
      return resultado == 0;
    } catch (IOException | InterruptedException ex) {
      Thread.currentThread().interrupt();
      LOGGER.error("Falha ao restaurar backup.", ex);
      return false;
    }
  }

  private boolean executarComandoPsql(List<String> argumentos, String senha) {
    try {
      ProcessBuilder builder = new ProcessBuilder(argumentos);
      if (StringUtils.hasText(senha)) {
        builder.environment().put("PGPASSWORD", senha);
      }
      builder.redirectErrorStream(true);
      Process processo = builder.start();
      String saida = lerSaidaProcesso(processo);
      int resultado = processo.waitFor();
      if (resultado != 0) {
        LOGGER.error("Falha ao executar comando psql. Saida: {}", limitarTexto(saida, 2000));
      }
      return resultado == 0;
    } catch (IOException | InterruptedException ex) {
      Thread.currentThread().interrupt();
      LOGGER.error("Falha ao executar comando psql.", ex);
      return false;
    }
  }

  private PostgresConfig obterConfiguracaoPostgres() {
    String urlSeguro = safeText(datasourceUrl, "jdbc:postgresql://localhost:5432/g3");
    String usuario = safeText(datasourceUsuario, "postgres");
    String senha = safeText(datasourceSenha, "");
    String host = "localhost";
    int porta = 5432;
    String banco = "g3";

    try {
      String semJdbc = urlSeguro.startsWith("jdbc:") ? urlSeguro.substring(5) : urlSeguro;
      URI uri = URI.create(semJdbc);
      if (StringUtils.hasText(uri.getHost())) {
        host = uri.getHost();
      }
      if (uri.getPort() > 0) {
        porta = uri.getPort();
      }
      String caminho = uri.getPath();
      if (StringUtils.hasText(caminho)) {
        banco = caminho.startsWith("/") ? caminho.substring(1) : caminho;
      }
    } catch (IllegalArgumentException ex) {
      String semPrefixo = urlSeguro.replace("jdbc:postgresql://", "");
      String[] partes = semPrefixo.split("/", 2);
      if (partes.length > 0 && StringUtils.hasText(partes[0])) {
        String hostPorta = partes[0];
        if (hostPorta.contains(":")) {
          String[] hp = hostPorta.split(":", 2);
          host = hp[0];
          porta = parsePorta(hp[1], porta);
        } else {
          host = hostPorta;
        }
      }
      if (partes.length > 1 && StringUtils.hasText(partes[1])) {
        banco = partes[1].split("\\?")[0];
      }
    }

    return new PostgresConfig(host, porta, banco, usuario, senha);
  }

  private List<String> montarComandoPsql(PostgresConfig config, List<String> argumentosExtras) {
    List<String> comando = new ArrayList<>();
    comando.add(psqlPath);
    comando.add("-h");
    comando.add(config.host);
    comando.add("-p");
    comando.add(String.valueOf(config.porta));
    comando.add("-U");
    comando.add(config.usuario);
    comando.add("-d");
    comando.add(config.banco);
    comando.addAll(argumentosExtras);
    return comando;
  }

  private String lerSaidaProcesso(Process processo) throws IOException {
    try (InputStream inputStream = processo.getInputStream()) {
      return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }
  }

  private String limitarTexto(String texto, int limite) {
    if (!StringUtils.hasText(texto) || texto.length() <= limite) {
      return texto;
    }
    return texto.substring(0, limite);
  }

  private int parsePorta(String valor, int fallback) {
    try {
      return Integer.parseInt(valor.trim());
    } catch (NumberFormatException ex) {
      return fallback;
    }
  }

  private static class PostgresConfig {
    private final String host;
    private final int porta;
    private final String banco;
    private final String usuario;
    private final String senha;

    private PostgresConfig(String host, int porta, String banco, String usuario, String senha) {
      this.host = host;
      this.porta = porta;
      this.banco = banco;
      this.usuario = usuario;
      this.senha = senha;
    }
  }
}
