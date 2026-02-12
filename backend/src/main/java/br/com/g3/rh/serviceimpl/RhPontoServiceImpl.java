package br.com.g3.rh.serviceimpl;

import br.com.g3.auditoria.service.AuditoriaService;
import br.com.g3.rh.domain.RhConfiguracaoPonto;
import br.com.g3.rh.domain.RhPontoAuditoria;
import br.com.g3.rh.domain.RhPontoDia;
import br.com.g3.rh.domain.RhPontoMarcacao;
import br.com.g3.rh.dto.RhConfiguracaoPontoRequest;
import br.com.g3.rh.dto.RhConfiguracaoPontoResponse;
import br.com.g3.rh.dto.RhPontoBaterRequest;
import br.com.g3.rh.dto.RhPontoDiaAtualizacaoRequest;
import br.com.g3.rh.dto.RhPontoDiaResponse;
import br.com.g3.rh.dto.RhPontoDiaResumoResponse;
import br.com.g3.rh.dto.RhPontoEspelhoResponse;
import br.com.g3.rh.mapper.RhPontoMapper;
import br.com.g3.rh.repository.RhConfiguracaoPontoRepository;
import br.com.g3.rh.repository.RhPontoAuditoriaRepository;
import br.com.g3.rh.repository.RhPontoDiaRepository;
import br.com.g3.rh.repository.RhPontoMarcacaoRepository;
import br.com.g3.rh.service.RhPontoService;
import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialResponse;
import br.com.g3.unidadeassistencial.service.UnidadeAssistencialService;
import br.com.g3.usuario.domain.Permissao;
import br.com.g3.usuario.domain.Usuario;
import br.com.g3.usuario.repository.UsuarioRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.springframework.http.HttpStatus;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RhPontoServiceImpl implements RhPontoService {
  private static final Set<String> TIPOS_VALIDOS = Set.of("E1", "S1", "E2", "S2");
  private static final Set<String> OCORRENCIAS_SEM_DESCONTO =
      Set.of("FERIADO", "FOLGA", "FERIAS", "JUSTIFICADO");
  private static final String OCORRENCIA_NORMAL = "NORMAL";
  private static final String OCORRENCIA_FALTA = "FALTA";
  private static final int RAIO_PADRAO_METROS = 100;
  private static final int ACCURACY_MAX_METROS = 200;

  private final RhConfiguracaoPontoRepository configuracaoRepository;
  private final RhPontoDiaRepository pontoDiaRepository;
  private final RhPontoMarcacaoRepository marcacaoRepository;
  private final RhPontoAuditoriaRepository auditoriaRepository;
  private final UsuarioRepository usuarioRepository;
  private final UnidadeAssistencialService unidadeAssistencialService;
  private final PasswordEncoder passwordEncoder;
  private final AuditoriaService auditoriaService;
  private final RhPontoMapper mapper = new RhPontoMapper();

  public RhPontoServiceImpl(
      RhConfiguracaoPontoRepository configuracaoRepository,
      RhPontoDiaRepository pontoDiaRepository,
      RhPontoMarcacaoRepository marcacaoRepository,
      RhPontoAuditoriaRepository auditoriaRepository,
      UsuarioRepository usuarioRepository,
      UnidadeAssistencialService unidadeAssistencialService,
      PasswordEncoder passwordEncoder,
      AuditoriaService auditoriaService) {
    this.configuracaoRepository = configuracaoRepository;
    this.pontoDiaRepository = pontoDiaRepository;
    this.marcacaoRepository = marcacaoRepository;
    this.auditoriaRepository = auditoriaRepository;
    this.usuarioRepository = usuarioRepository;
    this.unidadeAssistencialService = unidadeAssistencialService;
    this.passwordEncoder = passwordEncoder;
    this.auditoriaService = auditoriaService;
  }

  @Override
  public RhConfiguracaoPontoResponse buscarConfiguracao() {
    RhConfiguracaoPonto configuracao = configuracaoRepository.buscarAtual().orElseGet(this::criarConfiguracaoPadrao);
    return mapper.toConfiguracaoResponse(configuracao);
  }

  @Override
  @Transactional
  public RhConfiguracaoPontoResponse atualizarConfiguracao(RhConfiguracaoPontoRequest request, Long usuarioId) {
    validarAdminRh(usuarioId);
    RhConfiguracaoPonto configuracao = configuracaoRepository.buscarAtual().orElseGet(this::criarConfiguracaoPadrao);
    configuracao.setCargaSemanalMinutos(normalizarMinutos(request.getCargaSemanalMinutos(), 2400));
    configuracao.setCargaSegQuiMinutos(normalizarMinutos(request.getCargaSegQuiMinutos(), 540));
    configuracao.setCargaSextaMinutos(normalizarMinutos(request.getCargaSextaMinutos(), 240));
    configuracao.setCargaSabadoMinutos(normalizarMinutos(request.getCargaSabadoMinutos(), 0));
    configuracao.setCargaDomingoMinutos(normalizarMinutos(request.getCargaDomingoMinutos(), 0));
    configuracao.setToleranciaMinutos(normalizarMinutos(request.getToleranciaMinutos(), 10));
    configuracao.setAtualizadoEm(LocalDateTime.now());
    RhConfiguracaoPonto salvo = configuracaoRepository.salvar(configuracao);
    return mapper.toConfiguracaoResponse(salvo);
  }

  @Override
  @Transactional
  public RhPontoDiaResponse baterPonto(RhPontoBaterRequest request, Long usuarioId, String ip, String userAgent) {
    if (request == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe os dados da marcao.");
    }
    if (usuarioId == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usurio no informado.");
    }
    String tipo = normalizarTexto(request.getTipo());
    if (!TIPOS_VALIDOS.contains(tipo)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de marcao invlido.");
    }
    if (request.getSenha() == null || request.getSenha().isBlank()) {
      registrarAuditoria(usuarioId, "PONTO_SENHA_VAZIA", "Senha no informada");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe a senha para registrar o ponto.");
    }
    if (request.getLatitude() == null || request.getLongitude() == null || request.getAccuracy() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Localizao no informada.");
    }

    Usuario usuario = buscarUsuario(usuarioId);
    if (!passwordEncoder.matches(request.getSenha(), usuario.getSenhaHash())) {
      registrarAuditoria(usuarioId, "PONTO_SENHA_INVALIDA", "Senha invlida");
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha invlida.");
    }

    UnidadeAssistencialResponse unidade = unidadeAssistencialService.obterAtual();
    if (unidade == null || unidade.getLatitude() == null || unidade.getLongitude() == null
        || unidade.getLatitude().isBlank() || unidade.getLongitude().isBlank()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Unidade assistencial sem latitude/longitude configuradas.");
    }

    double latitudeUnidade = parseCoordenada(unidade.getLatitude(), "Latitude da unidade invalida.");
    double longitudeUnidade = parseCoordenada(unidade.getLongitude(), "Longitude da unidade invalida.");

    int raioMetros = unidade.getRaioPontoMetros() != null ? unidade.getRaioPontoMetros() : RAIO_PADRAO_METROS;
    int accuracyMax = unidade.getAccuracyMaxPontoMetros() != null
        ? unidade.getAccuracyMaxPontoMetros()
        : ACCURACY_MAX_METROS;
    if (accuracyMax < 1000) {
      accuracyMax = 1000;
    }
    validarPingLocal(unidade.getIpValidacaoPonto(), unidade.getPingTimeoutMs());
    validarAccuracy(request.getAccuracy(), accuracyMax);

    double distancia = calcularDistanciaMetros(
        request.getLatitude(), request.getLongitude(), latitudeUnidade, longitudeUnidade);
    boolean dentroPerimetro = distancia <= raioMetros;
    if (!dentroPerimetro) {
      registrarAuditoria(usuarioId, "PONTO_FORA_PERIMETRO",
          String.format(Locale.forLanguageTag("pt-BR"), "Distancia %.2f m", distancia));
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          String.format(
              Locale.forLanguageTag("pt-BR"),
              "Voce precisa estar na instituicao para registrar o ponto. Distancia: %.2f m. Raio permitido: %d m.",
              distancia,
              raioMetros));
    }

    LocalDate hoje = LocalDate.now();
    RhConfiguracaoPonto configuracao = configuracaoRepository.buscarAtual().orElseGet(this::criarConfiguracaoPadrao);
    RhPontoDia pontoDia = pontoDiaRepository.buscarPorFuncionarioEData(usuarioId, hoje)
        .orElseGet(() -> criarPontoDia(hoje, usuarioId, configuracao));

    String proximoTipo = obterProximoTipo(pontoDia.getMarcacoes());
    if (!tipo.equals(proximoTipo)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Sequencia de marcacao invalida. Proxima marcacao esperada: " + proximoTipo + ".");
    }

    RhPontoMarcacao marcacao = new RhPontoMarcacao();
    marcacao.setPontoDia(pontoDia);
    marcacao.setTipo(tipo);
    marcacao.setDataHoraServidor(LocalDateTime.now());
    marcacao.setLatitude(request.getLatitude());
    marcacao.setLongitude(request.getLongitude());
    marcacao.setAccuracy(request.getAccuracy());
    marcacao.setDistanciaMetros(distancia);
    marcacao.setDentroPerimetro(true);
    marcacao.setIp(ip);
    marcacao.setUserAgent(userAgent);
    marcacao.setCriadoEm(LocalDateTime.now());
    RhPontoMarcacao salvo = marcacaoRepository.salvar(marcacao);
    pontoDia.getMarcacoes().add(salvo);

    recalcularPontoDia(pontoDia, configuracao);
    pontoDia.setAtualizadoEm(LocalDateTime.now());
    RhPontoDia pontoAtualizado = pontoDiaRepository.salvar(pontoDia);

    auditoriaService.registrarEvento(
        "Ponto registrado",
        "rh_ponto_dia",
        String.valueOf(pontoAtualizado.getId()),
        "marcao " + tipo,
        usuarioId);
    registrarAuditoria(usuarioId, "PONTO_REGISTRADO", "marcao " + tipo);

    return mapper.toPontoDiaResponse(pontoAtualizado);
  }

  @Override
  public RhPontoEspelhoResponse consultarEspelho(Integer mes, Integer ano, Long funcionarioId) {
    if (funcionarioId == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Funcionrio no informado.");
    }
    YearMonth referencia = obterReferencia(mes, ano);
    LocalDate inicio = referencia.atDay(1);
    LocalDate fim = referencia.atEndOfMonth();
    RhConfiguracaoPonto configuracao = configuracaoRepository.buscarAtual().orElseGet(this::criarConfiguracaoPadrao);

    List<RhPontoDia> registros = pontoDiaRepository.listarPorFuncionarioEntreDatas(funcionarioId, inicio, fim);
    Map<LocalDate, RhPontoDia> porData = registros.stream()
        .collect(Collectors.toMap(RhPontoDia::getData, item -> item));

    RhPontoEspelhoResponse response = new RhPontoEspelhoResponse();
    response.setFuncionarioId(funcionarioId);
    response.setMes(referencia.getMonthValue());
    response.setAno(referencia.getYear());

    int totalTrabalhado = 0;
    int totalDevido = 0;
    int totalExtras = 0;
    int totalFaltas = 0;
    int totalBancoHoras = 0;
    int diasTrabalhados = 0;
    List<RhPontoDiaResumoResponse> dias = new ArrayList<>();

    for (LocalDate data = inicio; !data.isAfter(fim); data = data.plusDays(1)) {
      RhPontoDia registro = porData.get(data);
      RhPontoDiaResumoResponse diaResumo = montarResumoDia(data, registro, configuracao);
      dias.add(diaResumo);

      totalTrabalhado += optionalInt(diaResumo.getTotalTrabalhadoMinutos());
      totalExtras += optionalInt(diaResumo.getExtrasMinutos());
      totalFaltas += optionalInt(diaResumo.getFaltasAtrasosMinutos());
      totalBancoHoras += optionalInt(diaResumo.getBancoHorasMinutos());
      if (optionalInt(diaResumo.getTotalTrabalhadoMinutos()) > 0) {
        diasTrabalhados++;
      }

      if (deveSomarDevido(diaResumo.getOcorrencia())) {
        totalDevido += cargaPrevistaParaData(configuracao, data);
      }
    }

    response.setDias(dias);
    response.setTotalTrabalhadoMinutos(totalTrabalhado);
    response.setTotalDevidoMinutos(totalDevido);
    response.setTotalExtrasMinutos(totalExtras);
    response.setTotalFaltasAtrasosMinutos(totalFaltas);
    response.setTotalBancoHorasMinutos(totalBancoHoras);
    response.setDiasTrabalhados(diasTrabalhados);

    return response;
  }

  @Override
  @Transactional
  public RhPontoDiaResponse atualizarDia(Long id, RhPontoDiaAtualizacaoRequest request, Long usuarioId) {
    validarAdminRh(usuarioId);
    validarSenhaAdmin(usuarioId, request);
    RhPontoDia pontoDia = pontoDiaRepository.buscarPorId(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ponto do dia no encontrado."));
    if (request != null) {
      if (request.getOcorrencia() != null && !request.getOcorrencia().isBlank()) {
        pontoDia.setOcorrencia(normalizarTexto(request.getOcorrencia()));
      }
      String justificativa = request.getJustificativa();
      if (justificativa == null || justificativa.isBlank()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe a justificativa da correo.");
      }
      pontoDia.setObservacoes(justificativa);
      atualizarHorariosManual(pontoDia, request, usuarioId);
    }
    RhConfiguracaoPonto configuracao = configuracaoRepository.buscarAtual().orElseGet(this::criarConfiguracaoPadrao);
    recalcularPontoDia(pontoDia, configuracao);
    pontoDia.setAtualizadoEm(LocalDateTime.now());
    RhPontoDia salvo = pontoDiaRepository.salvar(pontoDia);
    auditoriaService.registrarEvento(
        "Ajuste de ponto",
        "rh_ponto_dia",
        String.valueOf(salvo.getId()),
        "Ocorrncia " + salvo.getOcorrencia(),
        usuarioId);
    registrarAuditoria(usuarioId, "PONTO_AJUSTADO", "Ponto dia " + salvo.getId());
    return mapper.toPontoDiaResponse(salvo);
  }

  private void atualizarHorariosManual(
      RhPontoDia pontoDia, RhPontoDiaAtualizacaoRequest request, Long usuarioId) {
    if (request == null) {
      return;
    }
    atualizarMarcacaoManual(pontoDia, "E1", request.getEntrada1(), usuarioId);
    atualizarMarcacaoManual(pontoDia, "S1", request.getSaida1(), usuarioId);
    atualizarMarcacaoManual(pontoDia, "E2", request.getEntrada2(), usuarioId);
    atualizarMarcacaoManual(pontoDia, "S2", request.getSaida2(), usuarioId);
  }

  private void atualizarMarcacaoManual(
      RhPontoDia pontoDia, String tipo, String horario, Long usuarioId) {
    if (horario == null || horario.isBlank()) {
      return;
    }
    LocalTime hora;
    try {
      hora = LocalTime.parse(horario.trim());
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Horario invalido: " + horario);
    }
    LocalDate data = pontoDia.getData();
    LocalDateTime dataHora = LocalDateTime.of(data, hora);
    Map<String, RhPontoMarcacao> mapa = pontoDia.getMarcacoes().stream()
        .collect(Collectors.toMap(RhPontoMarcacao::getTipo, item -> item, (a, b) -> b));
    RhPontoMarcacao marcacao = mapa.get(tipo);
    if (marcacao == null) {
      marcacao = new RhPontoMarcacao();
      marcacao.setPontoDia(pontoDia);
      marcacao.setTipo(tipo);
      marcacao.setLatitude(0.0);
      marcacao.setLongitude(0.0);
      marcacao.setAccuracy(0.0);
      marcacao.setDistanciaMetros(0.0);
      marcacao.setDentroPerimetro(true);
      marcacao.setIp("ajuste-manual");
      marcacao.setUserAgent("ajuste-manual");
      marcacao.setCriadoEm(LocalDateTime.now());
    }
    marcacao.setDataHoraServidor(dataHora);
    RhPontoMarcacao salvo = marcacaoRepository.salvar(marcacao);
    if (!pontoDia.getMarcacoes().contains(salvo)) {
      pontoDia.getMarcacoes().add(salvo);
    }
    registrarAuditoria(
        usuarioId,
        "PONTO_AJUSTE_HORARIO",
        "Ajuste manual " + tipo + " para " + horario);
  }

  private void validarSenhaAdmin(Long usuarioId, RhPontoDiaAtualizacaoRequest request) {
    if (request == null || request.getSenhaAdmin() == null || request.getSenhaAdmin().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe a senha administrativa.");
    }
    Usuario usuario = buscarUsuario(usuarioId);
    if (!passwordEncoder.matches(request.getSenhaAdmin(), usuario.getSenhaHash())) {
      registrarAuditoria(usuarioId, "PONTO_SENHA_ADMIN_INVALIDA", "Senha administrativa invlida");
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha administrativa invlida.");
    }
  }

  private void validarAccuracy(Double accuracy, Integer limite) {
    if (accuracy == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Precisa informar a precisao do GPS.");
    }
    if (limite != null && accuracy > limite) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          String.format(
              Locale.forLanguageTag("pt-BR"),
              "precisao do GPS insuficiente para registrar o ponto. Precisao atual: %.0f m. Limite: %d m.",
              accuracy,
              limite));
    }
  }

  private void validarPingLocal(String ipvalidacao, Integer timeoutMs) {
    if (ipvalidacao == null || ipvalidacao.isBlank()) {
      return;
    }
    int timeout = timeoutMs != null && timeoutMs > 0 ? timeoutMs : 2000;
    try {
      InetAddress inet = InetAddress.getByName(ipvalidacao.trim());
      boolean alcancavel = inet.isReachable(timeout);
      if (!alcancavel) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Nao foi possivel validar o acesso a rede da instituicao. "
                + "IP configurado: " + ipvalidacao + ".");
      }
    } catch (UnknownHostException ex) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "IP de validacao do ponto invalido. Informe um IP valido.");
    } catch (Exception ex) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Nao foi possivel validar o acesso a rede da instituicao. "
              + "IP configurado: " + ipvalidacao + ".");
    }
  }

  private RhPontoDia criarPontoDia(LocalDate data, Long funcionarioId, RhConfiguracaoPonto configuracao) {
    RhPontoDia pontoDia = new RhPontoDia();
    pontoDia.setFuncionarioId(funcionarioId);
    pontoDia.setData(data);
    pontoDia.setOcorrencia(OCORRENCIA_NORMAL);
    pontoDia.setObservacoes(null);
    pontoDia.setCargaPrevistaMinutos(cargaPrevistaParaData(configuracao, data));
    pontoDia.setToleranciaMinutos(configuracao.getToleranciaMinutos());
    pontoDia.setTotalTrabalhadoMinutos(0);
    pontoDia.setExtrasMinutos(0);
    pontoDia.setFaltasAtrasosMinutos(0);
    LocalDateTime agora = LocalDateTime.now();
    pontoDia.setCriadoEm(agora);
    pontoDia.setAtualizadoEm(agora);
    try {
      return pontoDiaRepository.salvar(pontoDia);
    } catch (DataIntegrityViolationException ex) {
      return pontoDiaRepository.buscarPorFuncionarioEData(funcionarioId, data)
          .orElseThrow(() -> ex);
    }
  }

  private void recalcularPontoDia(RhPontoDia pontoDia, RhConfiguracaoPonto configuracao) {
    int totalTrabalhado = calcularTotalTrabalhado(pontoDia.getMarcacoes());
    pontoDia.setTotalTrabalhadoMinutos(totalTrabalhado);

    String ocorrencia = normalizarTexto(pontoDia.getOcorrencia());
    int cargaPrevista = pontoDia.getCargaPrevistaMinutos();
    int tolerancia = pontoDia.getToleranciaMinutos();

    if (OCORRENCIA_FALTA.equals(ocorrencia)) {
      pontoDia.setTotalTrabalhadoMinutos(0);
      pontoDia.setExtrasMinutos(0);
      pontoDia.setFaltasAtrasosMinutos(cargaPrevista);
      return;
    }

    if (OCORRENCIAS_SEM_DESCONTO.contains(ocorrencia)) {
      pontoDia.setExtrasMinutos(0);
      pontoDia.setFaltasAtrasosMinutos(0);
      return;
    }

    int extras = Math.max(0, totalTrabalhado - cargaPrevista);
    int faltas = Math.max(0, cargaPrevista - totalTrabalhado - tolerancia);
    pontoDia.setExtrasMinutos(extras);
    pontoDia.setFaltasAtrasosMinutos(faltas);

    if (!OCORRENCIA_NORMAL.equals(ocorrencia)) {
      pontoDia.setOcorrencia(OCORRENCIA_NORMAL);
    }
  }

  private int calcularTotalTrabalhado(List<RhPontoMarcacao> marcacoes) {
    if (marcacoes == null || marcacoes.isEmpty()) {
      return 0;
    }
    Map<String, RhPontoMarcacao> mapa = marcacoes.stream()
        .collect(Collectors.toMap(RhPontoMarcacao::getTipo, item -> item, (a, b) -> b));
    int total = 0;
    total += calcularDuracaoMinutos(mapa.get("E1"), mapa.get("S1"));
    total += calcularDuracaoMinutos(mapa.get("E2"), mapa.get("S2"));
    return total;
  }

  private int calcularDuracaoMinutos(RhPontoMarcacao inicio, RhPontoMarcacao fim) {
    if (inicio == null || fim == null) {
      return 0;
    }
    return (int) java.time.Duration.between(inicio.getDataHoraServidor(), fim.getDataHoraServidor()).toMinutes();
  }

  private String obterProximoTipo(List<RhPontoMarcacao> marcacoes) {
    if (marcacoes == null || marcacoes.isEmpty()) {
      return "E1";
    }
    List<RhPontoMarcacao> ordenadas = marcacoes.stream()
        .sorted(Comparator.comparing(RhPontoMarcacao::getDataHoraServidor))
        .collect(Collectors.toList());
    List<String> tipos = ordenadas.stream().map(RhPontoMarcacao::getTipo).collect(Collectors.toList());
    if (tipos.contains("S2")) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todas as marcaes do dia j foram registradas.");
    }
    if (!tipos.contains("E1")) {
      return "E1";
    }
    if (!tipos.contains("S1")) {
      return "S1";
    }
    if (!tipos.contains("E2")) {
      return "E2";
    }
    return "S2";
  }

  private RhPontoDiaResumoResponse montarResumoDia(
      LocalDate data, RhPontoDia registro, RhConfiguracaoPonto configuracao) {
    RhPontoDiaResumoResponse resumo = new RhPontoDiaResumoResponse();
    resumo.setData(data);
    if (registro == null) {
      resumo.setOcorrencia("-");
      resumo.setTotalTrabalhadoMinutos(0);
      resumo.setExtrasMinutos(0);
      resumo.setFaltasAtrasosMinutos(0);
      resumo.setCargaPrevistaMinutos(cargaPrevistaParaData(configuracao, data));
      resumo.setBancoHorasMinutos(0);
      resumo.setObservacoes("");
      return resumo;
    }

    resumo.setPontoDiaId(registro.getId());
    resumo.setOcorrencia(registro.getOcorrencia());
    resumo.setObservacoes(registro.getObservacoes());
    resumo.setTotalTrabalhadoMinutos(registro.getTotalTrabalhadoMinutos());
    resumo.setExtrasMinutos(registro.getExtrasMinutos());
    resumo.setFaltasAtrasosMinutos(registro.getFaltasAtrasosMinutos());
    resumo.setCargaPrevistaMinutos(registro.getCargaPrevistaMinutos());

    Map<String, RhPontoMarcacao> mapa = registro.getMarcacoes().stream()
        .collect(Collectors.toMap(RhPontoMarcacao::getTipo, item -> item, (a, b) -> b));
    resumo.setEntradaManha(RhPontoMapper.formatarHora(mapa.get("E1")));
    resumo.setSaidaManha(RhPontoMapper.formatarHora(mapa.get("S1")));
    resumo.setEntradaTarde(RhPontoMapper.formatarHora(mapa.get("E2")));
    resumo.setSaidaTarde(RhPontoMapper.formatarHora(mapa.get("S2")));
    resumo.setBancoHorasMinutos(calcularBancoHorasMinutos(registro));
    return resumo;
  }

  private int calcularBancoHorasMinutos(RhPontoDia registro) {
    if (registro == null) {
      return 0;
    }
    String ocorrencia = normalizarTexto(registro.getOcorrencia());
    if (!OCORRENCIA_NORMAL.equals(ocorrencia)) {
      return 0;
    }
    DayOfWeek dia = registro.getData().getDayOfWeek();
    if (dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY) {
      return 0;
    }
    int total = optionalInt(registro.getTotalTrabalhadoMinutos());
    int cargaPrevista = optionalInt(registro.getCargaPrevistaMinutos());
    int tolerancia = optionalInt(registro.getToleranciaMinutos());
    int excedente = total - cargaPrevista - tolerancia;
    return Math.max(excedente, 0);
  }

  private boolean deveSomarDevido(String ocorrencia) {
    if (ocorrencia == null || ocorrencia.isBlank() || "-".equals(ocorrencia)) {
      return true;
    }
    String normalizada = normalizarTexto(ocorrencia);
    if (OCORRENCIAS_SEM_DESCONTO.contains(normalizada)) {
      return false;
    }
    return true;
  }

  private int cargaPrevistaParaData(RhConfiguracaoPonto configuracao, LocalDate data) {
    DayOfWeek dia = data.getDayOfWeek();
    return switch (dia) {
      case SATURDAY -> configuracao.getCargaSabadoMinutos();
      case SUNDAY -> configuracao.getCargaDomingoMinutos();
      case FRIDAY -> configuracao.getCargaSextaMinutos();
      default -> configuracao.getCargaSegQuiMinutos();
    };
  }

  private RhConfiguracaoPonto criarConfiguracaoPadrao() {
    RhConfiguracaoPonto configuracao = new RhConfiguracaoPonto();
    configuracao.setCargaSemanalMinutos(2400);
    configuracao.setCargaSegQuiMinutos(540);
    configuracao.setCargaSextaMinutos(240);
    configuracao.setCargaSabadoMinutos(0);
    configuracao.setCargaDomingoMinutos(0);
    configuracao.setToleranciaMinutos(10);
    configuracao.setAtualizadoEm(LocalDateTime.now());
    return configuracaoRepository.salvar(configuracao);
  }

  private Usuario buscarUsuario(Long id) {
    return usuarioRepository.buscarPorId(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usurio no encontrado."));
  }

  private void validarAdminRh(Long usuarioId) {
    if (usuarioId == null) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permisso insuficiente.");
    }
    Usuario usuario = buscarUsuario(usuarioId);
    boolean autorizado = usuario.getPermissoes().stream()
        .map(Permissao::getNome)
        .map(this::normalizarTexto)
        .anyMatch(nome -> nome.equals("RH_ADMIN") || nome.equals("ADMINISTRADOR"));
    if (!autorizado) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permisso insuficiente.");
    }
  }

  private void registrarAuditoria(Long funcionarioId, String acao, String detalhes) {
    RhPontoAuditoria auditoria = new RhPontoAuditoria();
    auditoria.setFuncionarioId(funcionarioId);
    auditoria.setAcao(acao);
    auditoria.setDetalhes(detalhes);
    auditoria.setCriadoEm(LocalDateTime.now());
    auditoriaRepository.salvar(auditoria);
  }

  private String normalizarTexto(String valor) {
    return valor == null ? "" : valor.trim().toUpperCase(Locale.forLanguageTag("pt-BR"));
  }

  private YearMonth obterReferencia(Integer mes, Integer ano) {
    LocalDate hoje = LocalDate.now();
    int mesRef = mes != null ? mes : hoje.getMonthValue();
    int anoRef = ano != null ? ano : hoje.getYear();
    return YearMonth.of(anoRef, mesRef);
  }

  private int optionalInt(Integer valor) {
    return valor == null ? 0 : valor;
  }

  private Integer normalizarMinutos(Integer valor, int padrao) {
    return valor == null || valor < 0 ? padrao : valor;
  }

  private double parseCoordenada(String valor, String mensagemErro) {
    try {
      return Double.parseDouble(valor.replace(',', '.'));
    } catch (NumberFormatException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, mensagemErro);
    }
  }

  private double calcularDistanciaMetros(
      double lat1, double lon1, double lat2, double lon2) {
    double rad = Math.PI / 180.0;
    double dLat = (lat2 - lat1) * rad;
    double dLon = (lon2 - lon1) * rad;
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
        + Math.cos(lat1 * rad) * Math.cos(lat2 * rad)
        * Math.sin(dLon / 2) * Math.sin(dLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return 6371000.0 * c;
  }
}




