package br.com.g3.controleveiculos.serviceimpl;

import br.com.g3.controleveiculos.domain.DiarioBordo;
import br.com.g3.controleveiculos.domain.Veiculo;
import br.com.g3.controleveiculos.dto.DiarioBordoRequest;
import br.com.g3.controleveiculos.dto.DiarioBordoResponse;
import br.com.g3.controleveiculos.dto.VeiculoRequest;
import br.com.g3.controleveiculos.dto.VeiculoResponse;
import br.com.g3.controleveiculos.repository.DiarioBordoRepository;
import br.com.g3.controleveiculos.repository.VeiculoRepository;
import br.com.g3.controleveiculos.service.ControleVeiculosService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ControleVeiculosServiceImpl implements ControleVeiculosService {
  private final VeiculoRepository repositorioVeiculo;
  private final DiarioBordoRepository repositorioDiarioBordo;

  public ControleVeiculosServiceImpl(
      VeiculoRepository repositorioVeiculo,
      DiarioBordoRepository repositorioDiarioBordo) {
    this.repositorioVeiculo = repositorioVeiculo;
    this.repositorioDiarioBordo = repositorioDiarioBordo;
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
    repositorioVeiculo.buscarPorPlaca(requisicao.getPlaca())
        .ifPresent(
            veiculo -> {
              throw new ResponseStatusException(
                  HttpStatus.BAD_REQUEST, "Ja existe um veiculo cadastrado com esta placa.");
            });
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
    repositorioVeiculo.buscarPorPlaca(requisicao.getPlaca())
        .filter(existente -> !existente.getId().equals(veiculo.getId()))
        .ifPresent(
            existente -> {
              throw new ResponseStatusException(
                  HttpStatus.BAD_REQUEST, "Ja existe um veiculo cadastrado com esta placa.");
            });
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
    validarVeiculoExistente(requisicao.getVeiculoId());
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
    validarVeiculoExistente(requisicao.getVeiculoId());
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

  private void validarRequisicaoVeiculo(VeiculoRequest requisicao) {
    if (requisicao == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Informe os dados do veiculo.");
    }
  }

  private void validarRequisicaoDiario(DiarioBordoRequest requisicao) {
    if (requisicao == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Informe os dados do diario de bordo.");
    }
  }

  private void validarVeiculoExistente(Long veiculoId) {
    repositorioVeiculo
        .buscarPorId(veiculoId)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veiculo nao encontrado."));
  }

  private void aplicarRequisicaoVeiculo(Veiculo veiculo, VeiculoRequest requisicao) {
    veiculo.setPlaca(requisicao.getPlaca().trim());
    veiculo.setModelo(requisicao.getModelo().trim());
    veiculo.setMarca(requisicao.getMarca().trim());
    veiculo.setAno(requisicao.getAno());
    veiculo.setTipoCombustivel(requisicao.getTipoCombustivel().trim());
    veiculo.setMediaConsumoPadrao(requisicao.getMediaConsumoPadrao());
    veiculo.setCapacidadeTanqueLitros(requisicao.getCapacidadeTanqueLitros());
    veiculo.setObservacoes(requisicao.getObservacoes());
    veiculo.setAtivo(Boolean.TRUE.equals(requisicao.getAtivo()));
  }

  private void aplicarRequisicaoDiario(DiarioBordo diario, DiarioBordoRequest requisicao) {
    diario.setVeiculoId(requisicao.getVeiculoId());
    diario.setData(requisicao.getData());
    diario.setCondutor(requisicao.getCondutor().trim());
    diario.setHorarioSaida(requisicao.getHorarioSaida());
    diario.setKmInicial(requisicao.getKmInicial());
    diario.setHorarioChegada(requisicao.getHorarioChegada());
    diario.setKmFinal(requisicao.getKmFinal());
    diario.setDestino(requisicao.getDestino().trim());
    diario.setObservacoes(requisicao.getObservacoes());
  }

  private void calcularMetricas(DiarioBordo diario) {
    BigDecimal kmInicial = diario.getKmInicial();
    BigDecimal kmFinal = diario.getKmFinal();
    if (kmInicial == null || kmFinal == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Informe o km inicial e o km final.");
    }
    BigDecimal kmRodados = kmFinal.subtract(kmInicial);
    if (kmRodados.compareTo(BigDecimal.ZERO) <= 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "O km final deve ser maior que o km inicial.");
    }
    diario.setKmRodados(kmRodados);

    Veiculo veiculo =
        repositorioVeiculo
            .buscarPorId(diario.getVeiculoId())
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veiculo nao encontrado."));
    BigDecimal mediaConsumoPadrao = veiculo.getMediaConsumoPadrao();
    if (mediaConsumoPadrao == null || mediaConsumoPadrao.compareTo(BigDecimal.ZERO) <= 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Informe a media de consumo no cadastro do veiculo.");
    }
    BigDecimal combustivelConsumido =
        kmRodados.divide(mediaConsumoPadrao, 2, RoundingMode.HALF_UP);
    diario.setCombustivelConsumidoLitros(combustivelConsumido);
    diario.setMediaConsumo(mediaConsumoPadrao.setScale(2, RoundingMode.HALF_UP));
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
