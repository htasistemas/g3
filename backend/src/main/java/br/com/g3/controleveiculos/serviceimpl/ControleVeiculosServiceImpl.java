package br.com.g3.controleveiculos.serviceimpl;

import br.com.g3.cadastroprofissionais.domain.CadastroProfissional;
import br.com.g3.cadastroprofissionais.repository.CadastroProfissionalRepository;
import br.com.g3.cadastrovoluntario.domain.CadastroVoluntario;
import br.com.g3.cadastrovoluntario.repository.CadastroVoluntarioRepository;
import br.com.g3.controleveiculos.domain.DiarioBordo;
import br.com.g3.controleveiculos.domain.MotoristaAutorizado;
import br.com.g3.controleveiculos.domain.Veiculo;
import br.com.g3.controleveiculos.dto.DiarioBordoRequest;
import br.com.g3.controleveiculos.dto.DiarioBordoResponse;
import br.com.g3.controleveiculos.dto.MotoristaAutorizadoRequest;
import br.com.g3.controleveiculos.dto.MotoristaAutorizadoResponse;
import br.com.g3.controleveiculos.dto.MotoristaDisponivelResponse;
import br.com.g3.controleveiculos.dto.VeiculoRequest;
import br.com.g3.controleveiculos.dto.VeiculoResponse;
import br.com.g3.controleveiculos.repository.DiarioBordoRepository;
import br.com.g3.controleveiculos.repository.MotoristaAutorizadoRepository;
import br.com.g3.controleveiculos.repository.VeiculoRepository;
import br.com.g3.controleveiculos.service.ControleVeiculosService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ControleVeiculosServiceImpl implements ControleVeiculosService {
  private final VeiculoRepository repositorioVeiculo;
  private final DiarioBordoRepository repositorioDiarioBordo;
  private final MotoristaAutorizadoRepository repositorioMotorista;
  private final CadastroProfissionalRepository repositorioProfissional;
  private final CadastroVoluntarioRepository repositorioVoluntario;

  public ControleVeiculosServiceImpl(
      VeiculoRepository repositorioVeiculo,
      DiarioBordoRepository repositorioDiarioBordo,
      MotoristaAutorizadoRepository repositorioMotorista,
      CadastroProfissionalRepository repositorioProfissional,
      CadastroVoluntarioRepository repositorioVoluntario) {
    this.repositorioVeiculo = repositorioVeiculo;
    this.repositorioDiarioBordo = repositorioDiarioBordo;
    this.repositorioMotorista = repositorioMotorista;
    this.repositorioProfissional = repositorioProfissional;
    this.repositorioVoluntario = repositorioVoluntario;
  }

  @Override
  public List<VeiculoResponse> listarVeiculos() {
    List<VeiculoResponse> resposta = new ArrayList<>();
    for (Veiculo veiculo : repositorioVeiculo.listar()) {
      resposta.add(mapearVeiculoResposta(veiculo));
    }
    return resposta;
  }

  @Override
  @Transactional
  public VeiculoResponse criarVeiculo(VeiculoRequest requisicao) {
    validarRequisicaoVeiculo(requisicao);
    String placaNormalizada = normalizarPlaca(requisicao.getPlaca());
    if (placaNormalizada != null) {
      repositorioVeiculo.buscarPorPlaca(placaNormalizada)
          .ifPresent(
              veiculo -> {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Ja existe um veiculo cadastrado com esta placa.");
              });
    }
    Veiculo veiculo = new Veiculo();
    aplicarRequisicaoVeiculo(veiculo, requisicao);
    LocalDateTime agora = LocalDateTime.now();
    veiculo.setCriadoEm(agora);
    veiculo.setAtualizadoEm(agora);
    return mapearVeiculoResposta(repositorioVeiculo.salvar(veiculo));
  }

  @Override
  @Transactional
  public VeiculoResponse atualizarVeiculo(Long id, VeiculoRequest requisicao) {
    validarRequisicaoVeiculo(requisicao);
    Veiculo veiculo =
        repositorioVeiculo
            .buscarPorId(id)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Veiculo nao encontrado."));
    String placaNormalizada = normalizarPlaca(requisicao.getPlaca());
    if (placaNormalizada != null) {
      repositorioVeiculo.buscarPorPlaca(placaNormalizada)
          .filter(existente -> !existente.getId().equals(veiculo.getId()))
          .ifPresent(
              existente -> {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Ja existe um veiculo cadastrado com esta placa.");
              });
    }
    aplicarRequisicaoVeiculo(veiculo, requisicao);
    veiculo.setAtualizadoEm(LocalDateTime.now());
    return mapearVeiculoResposta(repositorioVeiculo.salvar(veiculo));
  }

  @Override
  @Transactional
  public void excluirVeiculo(Long id) {
    Veiculo veiculo =
        repositorioVeiculo
            .buscarPorId(id)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Veiculo nao encontrado."));
    repositorioVeiculo.remover(veiculo);
  }

  @Override
  public List<DiarioBordoResponse> listarDiarios() {
    List<DiarioBordoResponse> resposta = new ArrayList<>();
    for (DiarioBordo diario : repositorioDiarioBordo.listar()) {
      resposta.add(mapearDiarioResposta(diario));
    }
    return resposta;
  }

  @Override
  @Transactional
  public DiarioBordoResponse criarDiario(DiarioBordoRequest requisicao) {
    validarRequisicaoDiario(requisicao);
    DiarioBordo diario = new DiarioBordo();
    aplicarRequisicaoDiario(diario, requisicao);
    calcularMetricas(diario);
    LocalDateTime agora = LocalDateTime.now();
    diario.setCriadoEm(agora);
    diario.setAtualizadoEm(agora);
    return mapearDiarioResposta(repositorioDiarioBordo.salvar(diario));
  }

  @Override
  @Transactional
  public DiarioBordoResponse atualizarDiario(Long id, DiarioBordoRequest requisicao) {
    validarRequisicaoDiario(requisicao);
    DiarioBordo diario =
        repositorioDiarioBordo
            .buscarPorId(id)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Registro do diario de bordo nao encontrado."));
    aplicarRequisicaoDiario(diario, requisicao);
    calcularMetricas(diario);
    diario.setAtualizadoEm(LocalDateTime.now());
    return mapearDiarioResposta(repositorioDiarioBordo.salvar(diario));
  }

  @Override
  @Transactional
  public void excluirDiario(Long id) {
    DiarioBordo diario =
        repositorioDiarioBordo
            .buscarPorId(id)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Registro do diario de bordo nao encontrado."));
    repositorioDiarioBordo.remover(diario);
  }

  @Override
  public List<MotoristaDisponivelResponse> listarMotoristasDisponiveis(String nome) {
    String nomeNormalizado = normalizarBusca(nome);
    List<MotoristaDisponivelResponse> resposta = new ArrayList<>();
    for (CadastroProfissional profissional : repositorioProfissional.listar()) {
      if (nomeNormalizado.isEmpty()
          || normalizarBusca(profissional.getNomeCompleto()).contains(nomeNormalizado)) {
        resposta.add(
            new MotoristaDisponivelResponse(
                profissional.getId(), "PROFISSIONAL", profissional.getNomeCompleto()));
      }
    }
    for (CadastroVoluntario voluntario : repositorioVoluntario.listar()) {
      if (nomeNormalizado.isEmpty()
          || normalizarBusca(voluntario.getNomeCompleto()).contains(nomeNormalizado)) {
        resposta.add(
            new MotoristaDisponivelResponse(
                voluntario.getId(), "VOLUNTARIO", voluntario.getNomeCompleto()));
      }
    }
    resposta.sort(Comparator.comparing(MotoristaDisponivelResponse::getNome, String.CASE_INSENSITIVE_ORDER));
    return resposta;
  }

  @Override
  public List<MotoristaAutorizadoResponse> listarMotoristasAutorizados(Long veiculoId) {
    List<MotoristaAutorizado> motoristas =
        veiculoId == null ? repositorioMotorista.listar() : repositorioMotorista.listarPorVeiculo(veiculoId);
    Map<Long, Veiculo> veiculos = new HashMap<>();
    if (veiculoId == null) {
      for (Veiculo veiculo : repositorioVeiculo.listar()) {
        veiculos.put(veiculo.getId(), veiculo);
      }
    }
    List<MotoristaAutorizadoResponse> resposta = new ArrayList<>();
    for (MotoristaAutorizado motorista : motoristas) {
      Veiculo veiculo =
          veiculoId != null
              ? repositorioVeiculo.buscarPorId(motorista.getVeiculoId()).orElse(null)
              : veiculos.get(motorista.getVeiculoId());
      resposta.add(
          new MotoristaAutorizadoResponse(
              motorista.getId(),
              motorista.getVeiculoId(),
              veiculo != null ? veiculo.getPlaca() : null,
              veiculo != null ? veiculo.getModelo() : null,
              motorista.getTipoOrigem(),
              obterMotoristaId(motorista),
              motorista.getNomeMotorista(),
              motorista.getNumeroCarteira(),
              motorista.getCategoriaCarteira(),
              motorista.getVencimentoCarteira() != null
                  ? motorista.getVencimentoCarteira().toString()
                  : null,
              motorista.getArquivoCarteiraPdf()));
    }
    return resposta;
  }

  @Override
  @Transactional
  public MotoristaAutorizadoResponse criarMotoristaAutorizado(MotoristaAutorizadoRequest request) {
    validarRequisicaoMotorista(request);
    Veiculo veiculo =
        repositorioVeiculo
            .buscarPorId(request.getVeiculoId())
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veiculo nao encontrado."));
    String tipoOrigem = request.getTipoOrigem().trim().toUpperCase(Locale.ROOT);
    MotoristaAutorizado motorista = new MotoristaAutorizado();
    motorista.setVeiculoId(veiculo.getId());
    motorista.setTipoOrigem(tipoOrigem);
    if ("PROFISSIONAL".equals(tipoOrigem)) {
      CadastroProfissional profissional =
          repositorioProfissional
              .buscarPorId(request.getMotoristaId())
              .orElseThrow(
                  () ->
                      new ResponseStatusException(
                          HttpStatus.NOT_FOUND, "Profissional nao encontrado."));
      motorista.setProfissionalId(profissional.getId());
      motorista.setVoluntarioId(null);
      motorista.setNomeMotorista(profissional.getNomeCompleto());
    } else if ("VOLUNTARIO".equals(tipoOrigem)) {
      CadastroVoluntario voluntario =
          repositorioVoluntario
              .buscarPorId(request.getMotoristaId())
              .orElseThrow(
                  () ->
                      new ResponseStatusException(
                          HttpStatus.NOT_FOUND, "Voluntario nao encontrado."));
      motorista.setVoluntarioId(voluntario.getId());
      motorista.setProfissionalId(null);
      motorista.setNomeMotorista(voluntario.getNomeCompleto());
    } else {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Tipo de origem invalido para motorista.");
    }

    repositorioMotorista
        .buscarPorDados(
            motorista.getVeiculoId(),
            motorista.getTipoOrigem(),
            motorista.getProfissionalId(),
            motorista.getVoluntarioId())
        .ifPresent(
            existente -> {
              throw new ResponseStatusException(
                  HttpStatus.BAD_REQUEST, "Motorista ja autorizado para este veiculo.");
            });

    motorista.setNumeroCarteira(normalizarTexto(request.getNumeroCarteira()));
    motorista.setCategoriaCarteira(normalizarTexto(request.getCategoriaCarteira()));
    motorista.setVencimentoCarteira(parsearData(request.getVencimentoCarteira()));
    motorista.setArquivoCarteiraPdf(request.getArquivoCarteiraPdf());

    LocalDateTime agora = LocalDateTime.now();
    motorista.setCriadoEm(agora);
    motorista.setAtualizadoEm(agora);
    MotoristaAutorizado salvo = repositorioMotorista.salvar(motorista);
    return new MotoristaAutorizadoResponse(
        salvo.getId(),
        salvo.getVeiculoId(),
        veiculo.getPlaca(),
        veiculo.getModelo(),
        salvo.getTipoOrigem(),
        obterMotoristaId(salvo),
        salvo.getNomeMotorista(),
        salvo.getNumeroCarteira(),
        salvo.getCategoriaCarteira(),
        salvo.getVencimentoCarteira() != null ? salvo.getVencimentoCarteira().toString() : null,
        salvo.getArquivoCarteiraPdf());
  }

  @Override
  @Transactional
  public MotoristaAutorizadoResponse atualizarMotoristaAutorizado(
      Long id, MotoristaAutorizadoRequest request) {
    validarRequisicaoMotorista(request);
    MotoristaAutorizado motorista =
        repositorioMotorista
            .buscarPorId(id)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Motorista autorizado nao encontrado."));
    Veiculo veiculo =
        repositorioVeiculo
            .buscarPorId(request.getVeiculoId())
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veiculo nao encontrado."));
    String tipoOrigem = request.getTipoOrigem().trim().toUpperCase(Locale.ROOT);
    motorista.setVeiculoId(veiculo.getId());
    motorista.setTipoOrigem(tipoOrigem);
    motorista.setProfissionalId(null);
    motorista.setVoluntarioId(null);

    if ("PROFISSIONAL".equals(tipoOrigem)) {
      CadastroProfissional profissional =
          repositorioProfissional
              .buscarPorId(request.getMotoristaId())
              .orElseThrow(
                  () ->
                      new ResponseStatusException(
                          HttpStatus.NOT_FOUND, "Profissional nao encontrado."));
      motorista.setProfissionalId(profissional.getId());
      motorista.setNomeMotorista(profissional.getNomeCompleto());
    } else if ("VOLUNTARIO".equals(tipoOrigem)) {
      CadastroVoluntario voluntario =
          repositorioVoluntario
              .buscarPorId(request.getMotoristaId())
              .orElseThrow(
                  () ->
                      new ResponseStatusException(
                          HttpStatus.NOT_FOUND, "Voluntario nao encontrado."));
      motorista.setVoluntarioId(voluntario.getId());
      motorista.setNomeMotorista(voluntario.getNomeCompleto());
    } else {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Tipo de origem invalido para motorista.");
    }

    repositorioMotorista
        .buscarPorDados(
            motorista.getVeiculoId(),
            motorista.getTipoOrigem(),
            motorista.getProfissionalId(),
            motorista.getVoluntarioId())
        .filter(existente -> !existente.getId().equals(motorista.getId()))
        .ifPresent(
            existente -> {
              throw new ResponseStatusException(
                  HttpStatus.BAD_REQUEST, "Motorista ja autorizado para este veiculo.");
            });

    motorista.setNumeroCarteira(normalizarTexto(request.getNumeroCarteira()));
    motorista.setCategoriaCarteira(normalizarTexto(request.getCategoriaCarteira()));
    motorista.setVencimentoCarteira(parsearData(request.getVencimentoCarteira()));
    motorista.setArquivoCarteiraPdf(request.getArquivoCarteiraPdf());
    motorista.setAtualizadoEm(LocalDateTime.now());

    MotoristaAutorizado salvo = repositorioMotorista.salvar(motorista);
    return new MotoristaAutorizadoResponse(
        salvo.getId(),
        salvo.getVeiculoId(),
        veiculo.getPlaca(),
        veiculo.getModelo(),
        salvo.getTipoOrigem(),
        obterMotoristaId(salvo),
        salvo.getNomeMotorista(),
        salvo.getNumeroCarteira(),
        salvo.getCategoriaCarteira(),
        salvo.getVencimentoCarteira() != null ? salvo.getVencimentoCarteira().toString() : null,
        salvo.getArquivoCarteiraPdf());
  }

  @Override
  @Transactional
  public void excluirMotoristaAutorizado(Long id) {
    MotoristaAutorizado motorista =
        repositorioMotorista
            .buscarPorId(id)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Motorista autorizado nao encontrado."));
    repositorioMotorista.remover(motorista);
  }

  private void validarRequisicaoVeiculo(VeiculoRequest requisicao) {
    if (requisicao == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Informe os dados do veiculo.");
    }
    String placaNormalizada = normalizarPlaca(requisicao.getPlaca());
    if (placaNormalizada != null && !isPlacaValida(placaNormalizada)) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Placa informada em formato invalido.");
    }
  }

  private void validarRequisicaoDiario(DiarioBordoRequest requisicao) {
    if (requisicao == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Informe os dados do diario de bordo.");
    }
  }

  private void aplicarRequisicaoVeiculo(Veiculo veiculo, VeiculoRequest requisicao) {
    veiculo.setPlaca(normalizarPlaca(requisicao.getPlaca()));
    veiculo.setModelo(normalizarTexto(requisicao.getModelo()));
    veiculo.setMarca(normalizarTexto(requisicao.getMarca()));
    veiculo.setAno(requisicao.getAno());
    veiculo.setTipoCombustivel(normalizarTexto(requisicao.getTipoCombustivel()));
    veiculo.setMediaConsumoPadrao(requisicao.getMediaConsumoPadrao());
    veiculo.setCapacidadeTanqueLitros(requisicao.getCapacidadeTanqueLitros());
    veiculo.setObservacoes(normalizarTexto(requisicao.getObservacoes()));
    veiculo.setAtivo(requisicao.getAtivo() == null ? Boolean.TRUE : requisicao.getAtivo());
    veiculo.setFotoFrente(requisicao.getFotoFrente());
    veiculo.setFotoLateralEsquerda(requisicao.getFotoLateralEsquerda());
    veiculo.setFotoLateralDireita(requisicao.getFotoLateralDireita());
    veiculo.setFotoTraseira(requisicao.getFotoTraseira());
  }

  private void aplicarRequisicaoDiario(DiarioBordo diario, DiarioBordoRequest requisicao) {
    diario.setVeiculoId(requisicao.getVeiculoId());
    diario.setData(requisicao.getData());
    diario.setCondutor(normalizarTexto(requisicao.getCondutor()));
    diario.setHorarioSaida(requisicao.getHorarioSaida());
    diario.setKmInicial(requisicao.getKmInicial());
    diario.setHorarioChegada(requisicao.getHorarioChegada());
    diario.setKmFinal(requisicao.getKmFinal());
    diario.setDestino(normalizarTexto(requisicao.getDestino()));
    diario.setObservacoes(normalizarTexto(requisicao.getObservacoes()));
  }

  private void calcularMetricas(DiarioBordo diario) {
    BigDecimal kmInicial = diario.getKmInicial();
    BigDecimal kmFinal = diario.getKmFinal();
    if (kmInicial == null || kmFinal == null) {
      diario.setKmRodados(null);
      diario.setCombustivelConsumidoLitros(null);
      diario.setMediaConsumo(null);
      return;
    }
    BigDecimal kmRodados = kmFinal.subtract(kmInicial);
    if (kmRodados.compareTo(BigDecimal.ZERO) <= 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "O km final deve ser maior que o km inicial.");
    }
    diario.setKmRodados(kmRodados);

    if (diario.getVeiculoId() == null) {
      diario.setCombustivelConsumidoLitros(null);
      diario.setMediaConsumo(null);
      return;
    }
    Veiculo veiculo =
        repositorioVeiculo
            .buscarPorId(diario.getVeiculoId())
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veiculo nao encontrado."));
    BigDecimal mediaConsumoPadrao = veiculo.getMediaConsumoPadrao();
    if (mediaConsumoPadrao == null || mediaConsumoPadrao.compareTo(BigDecimal.ZERO) <= 0) {
      diario.setCombustivelConsumidoLitros(null);
      diario.setMediaConsumo(null);
      return;
    }
    BigDecimal combustivelConsumido =
        kmRodados.divide(mediaConsumoPadrao, 2, RoundingMode.HALF_UP);
    diario.setCombustivelConsumidoLitros(combustivelConsumido);
    diario.setMediaConsumo(mediaConsumoPadrao.setScale(2, RoundingMode.HALF_UP));
  }

  private String normalizarTexto(String valor) {
    if (valor == null) {
      return null;
    }
    String normalizado = valor.trim();
    return normalizado.isEmpty() ? null : normalizado;
  }

  private String normalizarPlaca(String placa) {
    if (placa == null) {
      return null;
    }
    String normalizada = placa.trim().toUpperCase();
    return normalizada.isEmpty() ? null : normalizada;
  }

  private boolean isPlacaValida(String placa) {
    return placa.matches("^[A-Z]{3}-?\\d{4}$") || placa.matches("^[A-Z]{3}\\d[A-Z]\\d{2}$");
  }

  private void validarRequisicaoMotorista(MotoristaAutorizadoRequest request) {
    if (request == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Informe os dados do motorista autorizado.");
    }
    if (request.getVeiculoId() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o veiculo.");
    }
    if (request.getMotoristaId() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o motorista.");
    }
    if (request.getTipoOrigem() == null || request.getTipoOrigem().trim().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe a origem do motorista.");
    }
  }

  private Long obterMotoristaId(MotoristaAutorizado motorista) {
    if ("PROFISSIONAL".equalsIgnoreCase(motorista.getTipoOrigem())) {
      return motorista.getProfissionalId();
    }
    if ("VOLUNTARIO".equalsIgnoreCase(motorista.getTipoOrigem())) {
      return motorista.getVoluntarioId();
    }
    return null;
  }

  private String normalizarBusca(String texto) {
    if (texto == null) {
      return "";
    }
    String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
    normalizado = normalizado.replaceAll("\\p{M}", "");
    return normalizado.trim().toLowerCase(Locale.ROOT);
  }

  private LocalDate parsearData(String data) {
    if (data == null || data.trim().isEmpty()) {
      return null;
    }
    return LocalDate.parse(data);
  }

  private VeiculoResponse mapearVeiculoResposta(Veiculo veiculo) {
    VeiculoResponse resposta = new VeiculoResponse();
    resposta.setId(veiculo.getId());
    resposta.setPlaca(veiculo.getPlaca());
    resposta.setModelo(veiculo.getModelo());
    resposta.setMarca(veiculo.getMarca());
    resposta.setAno(veiculo.getAno());
    resposta.setTipoCombustivel(veiculo.getTipoCombustivel());
    resposta.setMediaConsumoPadrao(veiculo.getMediaConsumoPadrao());
    resposta.setCapacidadeTanqueLitros(veiculo.getCapacidadeTanqueLitros());
    resposta.setObservacoes(veiculo.getObservacoes());
    resposta.setAtivo(veiculo.getAtivo());
    resposta.setFotoFrente(veiculo.getFotoFrente());
    resposta.setFotoLateralEsquerda(veiculo.getFotoLateralEsquerda());
    resposta.setFotoLateralDireita(veiculo.getFotoLateralDireita());
    resposta.setFotoTraseira(veiculo.getFotoTraseira());
    resposta.setCriadoEm(veiculo.getCriadoEm());
    resposta.setAtualizadoEm(veiculo.getAtualizadoEm());
    return resposta;
  }

  private DiarioBordoResponse mapearDiarioResposta(DiarioBordo diario) {
    DiarioBordoResponse resposta = new DiarioBordoResponse();
    resposta.setId(diario.getId());
    resposta.setVeiculoId(diario.getVeiculoId());
    resposta.setData(diario.getData());
    resposta.setCondutor(diario.getCondutor());
    resposta.setHorarioSaida(diario.getHorarioSaida());
    resposta.setKmInicial(diario.getKmInicial());
    resposta.setHorarioChegada(diario.getHorarioChegada());
    resposta.setKmFinal(diario.getKmFinal());
    resposta.setDestino(diario.getDestino());
    resposta.setCombustivelConsumidoLitros(diario.getCombustivelConsumidoLitros());
    resposta.setKmRodados(diario.getKmRodados());
    resposta.setMediaConsumo(diario.getMediaConsumo());
    resposta.setObservacoes(diario.getObservacoes());
    resposta.setCriadoEm(diario.getCriadoEm());
    resposta.setAtualizadoEm(diario.getAtualizadoEm());
    return resposta;
  }
}
