package br.com.g3.unidadeassistencial.serviceimpl;

import br.com.g3.unidadeassistencial.domain.Endereco;
import br.com.g3.unidadeassistencial.domain.UnidadeAssistencial;
import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialCriacaoRequest;
import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialResponse;
import br.com.g3.unidadeassistencial.mapper.UnidadeAssistencialMapper;
import br.com.g3.unidadeassistencial.repository.UnidadeAssistencialRepository;
import br.com.g3.unidadeassistencial.service.GeocodificacaoService;
import br.com.g3.unidadeassistencial.service.UnidadeAssistencialService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UnidadeAssistencialServiceImpl implements UnidadeAssistencialService {
  private final UnidadeAssistencialRepository repository;
  private final GeocodificacaoService geocodificacaoService;

  public UnidadeAssistencialServiceImpl(
      UnidadeAssistencialRepository repository,
      GeocodificacaoService geocodificacaoService) {
    this.repository = repository;
    this.geocodificacaoService = geocodificacaoService;
  }

  @Override
  @Transactional
  public UnidadeAssistencialResponse criar(UnidadeAssistencialCriacaoRequest request) {
    ajustarConfiguracaoPonto(request);
    if (request.isUnidadePrincipal()) {
      repository.limparUnidadePrincipal();
    }
    UnidadeAssistencial unidade = UnidadeAssistencialMapper.toDomain(request);
    geocodificarSeNecessario(unidade);
    validarCoordenadasObrigatorias(unidade);
    UnidadeAssistencial salvo = repository.salvar(unidade);
    return UnidadeAssistencialMapper.toResponse(salvo);
  }

  @Override
  public List<UnidadeAssistencialResponse> listar() {
    return repository.listar().stream()
        .map(UnidadeAssistencialMapper::toResponse)
        .collect(Collectors.toList());
  }

  @Override
  public UnidadeAssistencialResponse obterAtual() {
    return repository.buscarPrincipal()
        .or(() -> repository.buscarAtual())
        .map(UnidadeAssistencialMapper::toResponse)
        .orElse(null);
  }

  @Override
  @Transactional
  public UnidadeAssistencialResponse atualizar(Long id, UnidadeAssistencialCriacaoRequest request) {
    ajustarConfiguracaoPonto(request);
    UnidadeAssistencial unidade =
        repository.buscarPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    if (request.isUnidadePrincipal()) {
      repository.limparUnidadePrincipal();
    }
    UnidadeAssistencialMapper.aplicarAtualizacao(unidade, request);
    geocodificarSeNecessario(unidade);
    validarCoordenadasObrigatorias(unidade);
    UnidadeAssistencial salvo = repository.salvar(unidade);
    return UnidadeAssistencialMapper.toResponse(salvo);
  }

  @Override
  public void remover(Long id) {
    UnidadeAssistencial unidade =
        repository.buscarPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    repository.remover(unidade);
  }

  @Override
  @Transactional
  public UnidadeAssistencialResponse geocodificarEndereco(Long id, boolean forcar) {
    UnidadeAssistencial unidade =
        repository.buscarPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    Endereco endereco = unidade.getEndereco();
    List<String> camposFaltantes = obterCamposEnderecoFaltantes(endereco);
    if (!camposFaltantes.isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Endereco incompleto: " + String.join(", ", camposFaltantes) + ".");
    }
    if (!forcar && endereco.getLatitude() != null && endereco.getLongitude() != null) {
      return UnidadeAssistencialMapper.toResponse(unidade);
    }
    return geocodificacaoService
        .geocodificar(endereco)
        .map(
            coordenadas -> {
              endereco.setLatitude(coordenadas.getLatitude());
              endereco.setLongitude(coordenadas.getLongitude());
              endereco.setAtualizadoEm(LocalDateTime.now());
              unidade.setAtualizadoEm(LocalDateTime.now());
              UnidadeAssistencial salvo = repository.salvar(unidade);
              return UnidadeAssistencialMapper.toResponse(salvo);
            })
        .orElseThrow(
            () -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Nao foi possivel geocodificar o endereco informado. Verifique logradouro, numero, cidade e estado. "
                    + "Endereco utilizado: " + montarEnderecoFormatado(endereco) + "."));
  }

  private List<String> obterCamposEnderecoFaltantes(Endereco endereco) {
    if (endereco == null) {
      return List.of("endereco");
    }
    List<String> faltantes = new ArrayList<>();
    if (endereco.getLogradouro() == null || endereco.getLogradouro().trim().isEmpty()) {
      faltantes.add("logradouro");
    }
    if (endereco.getNumero() == null || endereco.getNumero().trim().isEmpty()) {
      faltantes.add("numero");
    }
    if (endereco.getCidade() == null || endereco.getCidade().trim().isEmpty()) {
      faltantes.add("cidade");
    }
    if (endereco.getEstado() == null || endereco.getEstado().trim().isEmpty()) {
      faltantes.add("estado");
    }
    return faltantes;
  }

  private void ajustarConfiguracaoPonto(UnidadeAssistencialCriacaoRequest request) {
    if (request == null) {
      return;
    }
    Integer raio = request.getRaioPontoMetros();
    Integer accuracy = request.getAccuracyMaxPontoMetros();
    Integer timeout = request.getPingTimeoutMs();
    if (raio == null || raio <= 0) {
      request.setRaioPontoMetros(100);
    }
    if (accuracy == null || accuracy <= 0) {
      request.setAccuracyMaxPontoMetros(200);
    }
    if (timeout == null || timeout <= 0) {
      request.setPingTimeoutMs(2000);
    }
  }

  private void geocodificarSeNecessario(UnidadeAssistencial unidade) {
    Endereco endereco = unidade.getEndereco();
    if (endereco == null) {
      return;
    }
    if (endereco.getLatitude() != null && endereco.getLongitude() != null) {
      return;
    }
    List<String> camposFaltantes = obterCamposEnderecoFaltantes(endereco);
    if (!camposFaltantes.isEmpty()) {
      return;
    }
    geocodificacaoService.geocodificar(endereco).ifPresent(coordenadas -> {
      endereco.setLatitude(coordenadas.getLatitude());
      endereco.setLongitude(coordenadas.getLongitude());
      endereco.setAtualizadoEm(LocalDateTime.now());
      unidade.setAtualizadoEm(LocalDateTime.now());
    });
  }

  private void validarCoordenadasObrigatorias(UnidadeAssistencial unidade) {
    Endereco endereco = unidade.getEndereco();
    if (endereco == null) {
      return;
    }
    if (endereco.getLatitude() == null || endereco.getLongitude() == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Latitude e longitude sao obrigatorias para salvar o endereco.");
    }
  }

  private String montarEnderecoFormatado(Endereco endereco) {
    if (endereco == null) {
      return "";
    }
    List<String> partes = new ArrayList<>();
    if (endereco.getLogradouro() != null && !endereco.getLogradouro().isBlank()) {
      partes.add(endereco.getLogradouro().trim());
    }
    if (endereco.getNumero() != null && !endereco.getNumero().isBlank()) {
      partes.add("nº " + endereco.getNumero().trim());
    }
    if (endereco.getBairro() != null && !endereco.getBairro().isBlank()) {
      partes.add(endereco.getBairro().trim());
    }
    if (endereco.getCidade() != null && !endereco.getCidade().isBlank()) {
      partes.add(endereco.getCidade().trim());
    }
    if (endereco.getEstado() != null && !endereco.getEstado().isBlank()) {
      partes.add(endereco.getEstado().trim());
    }
    if (endereco.getCep() != null && !endereco.getCep().isBlank()) {
      partes.add("CEP " + endereco.getCep().trim());
    }
    return String.join(", ", partes);
  }
}
