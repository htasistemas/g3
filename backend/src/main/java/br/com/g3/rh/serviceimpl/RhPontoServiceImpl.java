package br.com.g3.rh.serviceimpl;

import br.com.g3.auditoria.service.AuditoriaService;
import br.com.g3.rh.domain.RhConfiguracaoPonto;
import br.com.g3.rh.domain.RhLocalPonto;
import br.com.g3.rh.domain.RhPontoAuditoria;
import br.com.g3.rh.domain.RhPontoDia;
import br.com.g3.rh.domain.RhPontoMarcacao;
import br.com.g3.rh.dto.RhConfiguracaoPontoRequest;
import br.com.g3.rh.dto.RhConfiguracaoPontoResponse;
import br.com.g3.rh.dto.RhLocalPontoRequest;
import br.com.g3.rh.dto.RhLocalPontoResponse;
import br.com.g3.rh.dto.RhPontoBaterRequest;
import br.com.g3.rh.dto.RhPontoDiaAtualizacaoRequest;
import br.com.g3.rh.dto.RhPontoDiaResponse;
import br.com.g3.rh.dto.RhPontoDiaResumoResponse;
import br.com.g3.rh.dto.RhPontoEspelhoResponse;
import br.com.g3.rh.mapper.RhPontoMapper;
import br.com.g3.rh.repository.RhConfiguracaoPontoRepository;
import br.com.g3.rh.repository.RhLocalPontoRepository;
import br.com.g3.rh.repository.RhPontoAuditoriaRepository;
import br.com.g3.rh.repository.RhPontoDiaRepository;
import br.com.g3.rh.repository.RhPontoMarcacaoRepository;
import br.com.g3.rh.service.RhPontoService;
import br.com.g3.usuario.domain.Permissao;
import br.com.g3.usuario.domain.Usuario;
import br.com.g3.usuario.repository.UsuarioRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
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

  private final RhLocalPontoRepository localRepository;
  private final RhConfiguracaoPontoRepository configuracaoRepository;
  private final RhPontoDiaRepository pontoDiaRepository;
  private final RhPontoMarcacaoRepository marcacaoRepository;
  private final RhPontoAuditoriaRepository auditoriaRepository;
  private final UsuarioRepository usuarioRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuditoriaService auditoriaService;
  private final RhPontoMapper mapper = new RhPontoMapper();

  public RhPontoServiceImpl(
      RhLocalPontoRepository localRepository,
      RhConfiguracaoPontoRepository configuracaoRepository,
      RhPontoDiaRepository pontoDiaRepository,
      RhPontoMarcacaoRepository marcacaoRepository,
      RhPontoAuditoriaRepository auditoriaRepository,
      UsuarioRepository usuarioRepository,
      PasswordEncoder passwordEncoder,
      AuditoriaService auditoriaService) {
    this.localRepository = localRepository;
    this.configuracaoRepository = configuracaoRepository;
    this.pontoDiaRepository = pontoDiaRepository;
    this.marcacaoRepository = marcacaoRepository;
    this.auditoriaRepository = auditoriaRepository;
    this.usuarioRepository = usuarioRepository;
    this.passwordEncoder = passwordEncoder;
    this.auditoriaService = auditoriaService;
  }

  @Override
  public List<RhLocalPontoResponse> listarLocais() {
    return localRepository.listar().stream().map(mapper::toLocalResponse).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public RhLocalPontoResponse criarLocal(RhLocalPontoRequest request) {
    validarLocalRequest(request);
    RhLocalPonto local = new RhLocalPonto();
    aplicarLocal(local, request);
    LocalDateTime agora = LocalDateTime.now();
    local.setCriadoEm(agora);
    local.setAtualizadoEm(agora);
    RhLocalPonto salvo = localRepository.salvar(local);
    return mapper.toLocalResponse(salvo);
  }

  @Override
  @Transactional
  public RhLocalPontoResponse atualizarLocal(Long id, RhLocalPontoRequest request) {
    validarLocalRequest(request);
    RhLocalPonto local = localRepository.buscarPorId(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Local de ponto não encontrado."));
    aplicarLocal(local, request);
    local.setAtualizadoEm(LocalDateTime.now());
    RhLocalPonto salvo = localRepository.salvar(local);
    return mapper.toLocalResponse(salvo);
  }

  @Override
  @Transactional
  public void removerLocal(Long id) {
    RhLocalPonto local = localRepository.buscarPorId(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Local de ponto não encontrado."));
    localRepository.remover(local);
  }

  @Override
  public RhLocalPontoResponse buscarLocalAtivo() {
    return localRepository.buscarPrimeiroAtivo()
        .map(mapper::toLocalResponse)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Local de ponto ativo não configurado."));
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
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe os dados da marcação.");
    }
    if (usuarioId == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não informado.");
    }
    String tipo = normalizarTexto(request.getTipo());
    if (!TIPOS_VALIDOS.contains(tipo)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de marcação inválido.");
    }
    if (request.getSenha() == null || request.getSenha().isBlank()) {
      registrarAuditoria(usuarioId, "PONTO_SENHA_VAZIA", "Senha não informada");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe a senha para registrar o ponto.");
    }
    if (request.getLatitude() == null || request.getLongitude() == null || request.getAccuracy() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Localização não informada.");
    }

    Usuario usuario = buscarUsuario(usuarioId);
    if (!passwordEncoder.matches(request.getSenha(), usuario.getSenhaHash())) {
      registrarAuditoria(usuarioId, "PONTO_SENHA_INVALIDA", "Senha inválida");
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha inválida.");
    }

    RhLocalPonto local = localRepository.buscarPrimeiroAtivo()
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Local de ponto ativo não configurado."));
    validarAccuracy(request.getAccuracy(), local.getAccuracyMaxMetros());

    double distancia = calcularDistanciaMetros(
        request.getLatitude(), request.getLongitude(), local.getLatitude(), local.getLongitude());
    boolean dentroPerimetro = distancia <= local.getRaioMetros();
    if (!dentroPerimetro) {
      registrarAuditoria(usuarioId, "PONTO_FORA_PERIMETRO",
          String.format(Locale.forLanguageTag("pt-BR"), "Distância %.2f m", distancia));
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Você precisa estar na instituição para registrar o ponto.");
    }

    LocalDate hoje = LocalDate.now();
    RhConfiguracaoPonto configuracao = configuracaoRepository.buscarAtual().orElseGet(this::criarConfiguracaoPadrao);
    RhPontoDia pontoDia = pontoDiaRepository.buscarPorFuncionarioEData(usuarioId, hoje)
        .orElseGet(() -> criarPontoDia(hoje, usuarioId, configuracao));

    String proximoTipo = obterProximoTipo(pontoDia.getMarcacoes());
    if (!tipo.equals(proximoTipo)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Sequência de marcação inválida. Próxima marcação esperada: " + proximoTipo + ".");
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
        "Marcação " + tipo,
        usuarioId);
    registrarAuditoria(usuarioId, "PONTO_REGISTRADO", "Marcação " + tipo);

    return mapper.toPontoDiaResponse(pontoAtualizado);
  }

  @Override
  public RhPontoEspelhoResponse consultarEspelho(Integer mes, Integer ano, Long funcionarioId) {
    if (funcionarioId == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Funcionário não informado.");
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
    int diasTrabalhados = 0;
    List<RhPontoDiaResumoResponse> dias = new ArrayList<>();

    for (LocalDate data = inicio; !data.isAfter(fim); data = data.plusDays(1)) {
      RhPontoDia registro = porData.get(data);
      RhPontoDiaResumoResponse diaResumo = montarResumoDia(data, registro, configuracao);
      dias.add(diaResumo);

      totalTrabalhado += optionalInt(diaResumo.getTotalTrabalhadoMinutos());
      totalExtras += optionalInt(diaResumo.getExtrasMinutos());
      totalFaltas += optionalInt(diaResumo.getFaltasAtrasosMinutos());
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
    response.setDiasTrabalhados(diasTrabalhados);

    return response;
  }

  @Override
  @Transactional
  public RhPontoDiaResponse atualizarDia(Long id, RhPontoDiaAtualizacaoRequest request, Long usuarioId) {
    validarAdminRh(usuarioId);
    RhPontoDia pontoDia = pontoDiaRepository.buscarPorId(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ponto do dia não encontrado."));
    if (request != null) {
      if (request.getOcorrencia() != null && !request.getOcorrencia().isBlank()) {
        pontoDia.setOcorrencia(normalizarTexto(request.getOcorrencia()));
      }
      pontoDia.setObservacoes(request.getObservacoes());
    }
    RhConfiguracaoPonto configuracao = configuracaoRepository.buscarAtual().orElseGet(this::criarConfiguracaoPadrao);
    recalcularPontoDia(pontoDia, configuracao);
    pontoDia.setAtualizadoEm(LocalDateTime.now());
    RhPontoDia salvo = pontoDiaRepository.salvar(pontoDia);
    auditoriaService.registrarEvento(
        "Ajuste de ponto",
        "rh_ponto_dia",
        String.valueOf(salvo.getId()),
        "Ocorrência " + salvo.getOcorrencia(),
        usuarioId);
    registrarAuditoria(usuarioId, "PONTO_AJUSTADO", "Ponto dia " + salvo.getId());
    return mapper.toPontoDiaResponse(salvo);
  }

  private void validarLocalRequest(RhLocalPontoRequest request) {
    if (request == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe os dados do local de ponto.");
    }
    if (request.getNome() == null || request.getNome().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome do local de ponto obrigatório.");
    }
    if (request.getLatitude() == null || request.getLongitude() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Latitude e longitude são obrigatórias.");
    }
    if (request.getRaioMetros() == null || request.getRaioMetros() <= 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Raio em metros inválido.");
    }
    if (request.getAccuracyMaxMetros() == null || request.getAccuracyMaxMetros() <= 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Precisão máxima inválida.");
    }
  }

  private void aplicarLocal(RhLocalPonto local, RhLocalPontoRequest request) {
    local.setNome(request.getNome());
    local.setEndereco(request.getEndereco());
    local.setLatitude(request.getLatitude());
    local.setLongitude(request.getLongitude());
    local.setRaioMetros(request.getRaioMetros());
    local.setAccuracyMaxMetros(request.getAccuracyMaxMetros());
    local.setAtivo(request.isAtivo());
  }

  private void validarAccuracy(Double accuracy, Integer limite) {
    if (accuracy == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Precisa informar a precisão do GPS.");
    }
    if (limite != null && accuracy > limite) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Precisão do GPS insuficiente para registrar o ponto.");
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
    return pontoDiaRepository.salvar(pontoDia);
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
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todas as marcações do dia já foram registradas.");
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
      resumo.setObservacoes("");
      return resumo;
    }

    resumo.setPontoDiaId(registro.getId());
    resumo.setOcorrencia(registro.getOcorrencia());
    resumo.setObservacoes(registro.getObservacoes());
    resumo.setTotalTrabalhadoMinutos(registro.getTotalTrabalhadoMinutos());
    resumo.setExtrasMinutos(registro.getExtrasMinutos());
    resumo.setFaltasAtrasosMinutos(registro.getFaltasAtrasosMinutos());

    Map<String, RhPontoMarcacao> mapa = registro.getMarcacoes().stream()
        .collect(Collectors.toMap(RhPontoMarcacao::getTipo, item -> item, (a, b) -> b));
    resumo.setEntradaManha(RhPontoMapper.formatarHora(mapa.get("E1")));
    resumo.setSaidaManha(RhPontoMapper.formatarHora(mapa.get("S1")));
    resumo.setEntradaTarde(RhPontoMapper.formatarHora(mapa.get("E2")));
    resumo.setSaidaTarde(RhPontoMapper.formatarHora(mapa.get("S2")));
    return resumo;
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
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
  }

  private void validarAdminRh(Long usuarioId) {
    if (usuarioId == null) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permissão insuficiente.");
    }
    Usuario usuario = buscarUsuario(usuarioId);
    boolean autorizado = usuario.getPermissoes().stream()
        .map(Permissao::getNome)
        .map(this::normalizarTexto)
        .anyMatch(nome -> nome.equals("RH_ADMIN") || nome.equals("ADMINISTRADOR"));
    if (!autorizado) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permissão insuficiente.");
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
