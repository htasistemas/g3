package br.com.g3.fotoseventos.serviceimpl;

import br.com.g3.fotoseventos.domain.FotoEvento;
import br.com.g3.fotoseventos.domain.FotoEventoFoto;
import br.com.g3.fotoseventos.dto.FotoEventoDetalheResponse;
import br.com.g3.fotoseventos.dto.FotoEventoFotoAtualizacaoRequest;
import br.com.g3.fotoseventos.dto.FotoEventoFotoRequest;
import br.com.g3.fotoseventos.dto.FotoEventoFotoResponse;
import br.com.g3.fotoseventos.dto.FotoEventoListaResponse;
import br.com.g3.fotoseventos.dto.FotoEventoRequest;
import br.com.g3.fotoseventos.dto.FotoEventoResponse;
import br.com.g3.fotoseventos.repository.FotoEventoFotoRepository;
import br.com.g3.fotoseventos.repository.FotoEventoRepository;
import br.com.g3.fotoseventos.service.ArmazenamentoFotoEventoService;
import br.com.g3.fotoseventos.service.FotoEventoService;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FotoEventoServiceImpl implements FotoEventoService {
  private final FotoEventoRepository repository;
  private final FotoEventoFotoRepository fotoRepository;
  private final ArmazenamentoFotoEventoService armazenamentoService;

  public FotoEventoServiceImpl(
      FotoEventoRepository repository,
      FotoEventoFotoRepository fotoRepository,
      ArmazenamentoFotoEventoService armazenamentoService) {
    this.repository = repository;
    this.fotoRepository = fotoRepository;
    this.armazenamentoService = armazenamentoService;
  }

  @Override
  public FotoEventoListaResponse listar(
      String busca, LocalDate dataInicio, LocalDate dataFim, Long unidadeId, int pagina, int tamanho) {
    PageRequest pageable = PageRequest.of(Math.max(pagina, 0), Math.max(tamanho, 1));
    Page<FotoEvento> page =
        repository.listarComFiltros(normalizarBusca(busca), dataInicio, dataFim, unidadeId, pageable);
    List<FotoEventoResponse> eventos =
        page.getContent().stream().map(this::mapEvento).collect(Collectors.toList());
    return new FotoEventoListaResponse(
        eventos, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
  }

  @Override
  public FotoEventoDetalheResponse obter(Long id) {
    FotoEvento evento = buscarEvento(id);
    List<FotoEventoFotoResponse> fotos = mapFotos(fotoRepository.listarPorEvento(evento.getId()));
    return new FotoEventoDetalheResponse(mapEvento(evento), fotos);
  }

  @Override
  public FotoEventoResponse criar(FotoEventoRequest request) {
    if (request.getFotoPrincipalUpload() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Foto principal obrigatoria.");
    }

    LocalDateTime agora = LocalDateTime.now();
    FotoEvento evento = new FotoEvento();
    evento.setTitulo(request.getTitulo());
    evento.setDescricao(request.getDescricao());
    evento.setDataEvento(request.getDataEvento());
    evento.setLocal(request.getLocal());
    evento.setTags(request.getTags());
    evento.setUnidadeId(request.getUnidadeId());
    evento.setCriadoEm(agora);
    evento.setAtualizadoEm(agora);

    FotoEvento salvo = repository.salvar(evento);
    String fotoPrincipal =
        armazenamentoService.salvarArquivo(salvo.getId(), request.getFotoPrincipalUpload());
    if (fotoPrincipal == null || fotoPrincipal.trim().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Foto principal obrigatoria.");
    }
    salvo.setFotoPrincipal(fotoPrincipal);
    return mapEvento(repository.salvar(salvo));
  }

  @Override
  public FotoEventoResponse atualizar(Long id, FotoEventoRequest request) {
    FotoEvento evento = buscarEvento(id);
    evento.setTitulo(request.getTitulo());
    evento.setDescricao(request.getDescricao());
    evento.setDataEvento(request.getDataEvento());
    evento.setLocal(request.getLocal());
    evento.setTags(request.getTags());
    evento.setUnidadeId(request.getUnidadeId());
    evento.setAtualizadoEm(LocalDateTime.now());

    if (request.getFotoPrincipalUpload() != null) {
      String fotoPrincipal =
          armazenamentoService.salvarArquivo(evento.getId(), request.getFotoPrincipalUpload());
      if (fotoPrincipal != null && !fotoPrincipal.trim().isEmpty()) {
        evento.setFotoPrincipal(fotoPrincipal);
      }
    } else if (request.getFotoPrincipalId() != null) {
      FotoEventoFoto foto = buscarFoto(evento.getId(), request.getFotoPrincipalId());
      evento.setFotoPrincipal(foto.getArquivo());
    }

    return mapEvento(repository.salvar(evento));
  }

  @Override
  public void excluir(Long id) {
    FotoEvento evento = buscarEvento(id);
    List<FotoEventoFoto> fotos = fotoRepository.listarPorEvento(evento.getId());
    Set<String> caminhos = new HashSet<>();
    for (FotoEventoFoto foto : fotos) {
      if (foto.getArquivo() != null) {
        caminhos.add(foto.getArquivo());
      }
    }
    if (evento.getFotoPrincipal() != null) {
      caminhos.add(evento.getFotoPrincipal());
    }

    fotos.forEach(fotoRepository::remover);
    repository.remover(evento);
    caminhos.forEach(armazenamentoService::removerArquivo);
  }

  @Override
  public FotoEventoFotoResponse adicionarFoto(Long id, FotoEventoFotoRequest request) {
    FotoEvento evento = buscarEvento(id);
    if (request == null || request.getArquivo() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Envie o arquivo da foto.");
    }

    String caminho = armazenamentoService.salvarArquivo(evento.getId(), request.getArquivo());
    if (caminho == null || caminho.trim().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Arquivo invalido.");
    }

    FotoEventoFoto foto = new FotoEventoFoto();
    foto.setEventoId(evento.getId());
    foto.setArquivo(caminho);
    foto.setLegenda(request.getLegenda());
    foto.setOrdem(request.getOrdem());
    foto.setCriadoEm(LocalDateTime.now());

    FotoEventoFoto salva = fotoRepository.salvar(foto);
    return mapFoto(salva);
  }

  @Override
  public FotoEventoFotoResponse atualizarFoto(
      Long id, Long fotoId, FotoEventoFotoAtualizacaoRequest request) {
    FotoEvento evento = buscarEvento(id);
    FotoEventoFoto foto = buscarFoto(evento.getId(), fotoId);
    foto.setLegenda(request.getLegenda());
    foto.setOrdem(request.getOrdem());
    FotoEventoFoto salva = fotoRepository.salvar(foto);
    return mapFoto(salva);
  }

  @Override
  public void removerFoto(Long id, Long fotoId) {
    FotoEvento evento = buscarEvento(id);
    FotoEventoFoto foto = buscarFoto(evento.getId(), fotoId);

    if (evento.getFotoPrincipal() != null
        && evento.getFotoPrincipal().equalsIgnoreCase(foto.getArquivo())) {
      List<FotoEventoFoto> fotos = fotoRepository.listarPorEvento(evento.getId());
      List<FotoEventoFoto> restantes =
          fotos.stream().filter(item -> !item.getId().equals(foto.getId())).collect(Collectors.toList());
      if (restantes.isEmpty()) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Defina outra foto principal antes de remover esta foto.");
      }
      FotoEventoFoto novaPrincipal =
          restantes.stream()
              .sorted(
                  Comparator.comparing(FotoEventoFoto::getOrdem, Comparator.nullsLast(Integer::compareTo))
                      .thenComparing(FotoEventoFoto::getCriadoEm, Comparator.nullsLast(LocalDateTime::compareTo)))
              .findFirst()
              .orElse(restantes.get(0));
      evento.setFotoPrincipal(novaPrincipal.getArquivo());
      evento.setAtualizadoEm(LocalDateTime.now());
      repository.salvar(evento);
    }

    fotoRepository.remover(foto);
    armazenamentoService.removerArquivo(foto.getArquivo());
  }

  @Override
  public Resource obterArquivoFoto(Long id, Long fotoId) {
    FotoEvento evento = buscarEvento(id);
    FotoEventoFoto foto = buscarFoto(evento.getId(), fotoId);
    try {
      Path caminho = Paths.get(foto.getArquivo());
      Resource resource = new UrlResource(caminho.toUri());
      if (!resource.exists()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo nao encontrado.");
      }
      return resource;
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo nao encontrado.");
    }
  }

  @Override
  public Resource obterFotoPrincipal(Long id) {
    FotoEvento evento = buscarEvento(id);
    if (evento.getFotoPrincipal() == null || evento.getFotoPrincipal().trim().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo nao encontrado.");
    }
    try {
      Path caminho = Paths.get(evento.getFotoPrincipal());
      Resource resource = new UrlResource(caminho.toUri());
      if (!resource.exists()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo nao encontrado.");
      }
      return resource;
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo nao encontrado.");
    }
  }

  private FotoEvento buscarEvento(Long id) {
    return repository
        .buscarPorId(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento nao encontrado."));
  }

  private FotoEventoFoto buscarFoto(Long eventoId, Long fotoId) {
    return fotoRepository
        .buscarPorEvento(eventoId, fotoId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Foto nao encontrada."));
  }

  private FotoEventoResponse mapEvento(FotoEvento evento) {
    String principalUrl =
        evento.getId() != null && evento.getFotoPrincipal() != null
            ? "/api/fotos-eventos/" + evento.getId() + "/principal"
            : null;
    return new FotoEventoResponse(
        evento.getId(),
        evento.getUnidadeId(),
        evento.getTitulo(),
        evento.getDescricao(),
        evento.getDataEvento(),
        evento.getLocal(),
        evento.getTags(),
        evento.getFotoPrincipal(),
        principalUrl,
        evento.getCriadoEm(),
        evento.getAtualizadoEm());
  }

  private FotoEventoFotoResponse mapFoto(FotoEventoFoto foto) {
    String arquivoUrl =
        foto.getEventoId() != null && foto.getId() != null
            ? "/api/fotos-eventos/" + foto.getEventoId() + "/fotos/" + foto.getId() + "/arquivo"
            : null;
    return new FotoEventoFotoResponse(
        foto.getId(),
        foto.getEventoId(),
        foto.getArquivo(),
        arquivoUrl,
        foto.getLegenda(),
        foto.getOrdem(),
        foto.getCriadoEm());
  }

  private List<FotoEventoFotoResponse> mapFotos(List<FotoEventoFoto> fotos) {
    List<FotoEventoFotoResponse> respostas = new ArrayList<>();
    for (FotoEventoFoto foto : fotos) {
      respostas.add(mapFoto(foto));
    }
    return respostas;
  }

  private String normalizarBusca(String busca) {
    if (busca == null) {
      return null;
    }
    String texto = busca.trim().toLowerCase(Locale.ROOT);
    return texto.isEmpty() ? null : texto;
  }
}
