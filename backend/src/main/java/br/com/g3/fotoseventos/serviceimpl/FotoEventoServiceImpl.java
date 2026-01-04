package br.com.g3.fotoseventos.serviceimpl;

import br.com.g3.fotoseventos.domain.FotoEvento;
import br.com.g3.fotoseventos.domain.FotoEventoFoto;
import br.com.g3.fotoseventos.domain.FotoEventoTag;
import br.com.g3.fotoseventos.domain.StatusFotoEvento;
import br.com.g3.fotoseventos.dto.FotoEventoDetalheResponse;
import br.com.g3.fotoseventos.dto.FotoEventoFotoAtualizacaoRequest;
import br.com.g3.fotoseventos.dto.FotoEventoFotoRequest;
import br.com.g3.fotoseventos.dto.FotoEventoFotoResponse;
import br.com.g3.fotoseventos.dto.FotoEventoListaResponse;
import br.com.g3.fotoseventos.dto.FotoEventoRequest;
import br.com.g3.fotoseventos.dto.FotoEventoResponse;
import br.com.g3.fotoseventos.repository.FotoEventoFotoRepository;
import br.com.g3.fotoseventos.repository.FotoEventoRepository;
import br.com.g3.fotoseventos.repository.FotoEventoTagRepository;
import br.com.g3.fotoseventos.service.ArmazenamentoFotoEventoService;
import br.com.g3.fotoseventos.service.FotoEventoService;
import br.com.g3.fotoseventos.service.ArmazenamentoFotoEventoService.FotoEventoArquivoInfo;
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
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FotoEventoServiceImpl implements FotoEventoService {
  private final FotoEventoRepository repository;
  private final FotoEventoFotoRepository fotoRepository;
  private final FotoEventoTagRepository tagRepository;
  private final ArmazenamentoFotoEventoService armazenamentoService;

  public FotoEventoServiceImpl(
      FotoEventoRepository repository,
      FotoEventoFotoRepository fotoRepository,
      FotoEventoTagRepository tagRepository,
      ArmazenamentoFotoEventoService armazenamentoService) {
    this.repository = repository;
    this.fotoRepository = fotoRepository;
    this.tagRepository = tagRepository;
    this.armazenamentoService = armazenamentoService;
  }

  @Override
  public FotoEventoListaResponse listar(
      String busca,
      LocalDate dataInicio,
      LocalDate dataFim,
      Long unidadeId,
      String status,
      List<String> tags,
      String ordenacao,
      int pagina,
      int tamanho) {
    PageRequest pageable =
        PageRequest.of(Math.max(pagina, 0), Math.max(tamanho, 1), obterOrdenacao(ordenacao));
    List<String> tagsNormalizadas = normalizarTags(tags);
    StatusFotoEvento statusFiltro = normalizarStatusFiltro(status);
    Page<FotoEvento> page =
        "MAIS_FOTOS".equalsIgnoreCase(ordenacao)
            ? repository.listarComFiltrosOrdenadoPorFotos(
                normalizarBusca(busca),
                dataInicio,
                dataFim,
                unidadeId,
                statusFiltro,
                tagsNormalizadas,
                pageable)
            : repository.listarComFiltros(
                normalizarBusca(busca),
                dataInicio,
                dataFim,
                unidadeId,
                statusFiltro,
                tagsNormalizadas,
                pageable);
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
  @Transactional
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
    evento.setStatus(normalizarStatusEnum(request.getStatus()));
    evento.setTags(juntarTags(request.getTags()));
    evento.setUnidadeId(request.getUnidadeId());
    evento.setCriadoEm(agora);
    evento.setAtualizadoEm(agora);

    FotoEvento salvo = repository.salvar(evento);
    FotoEventoFoto fotoPrincipal =
        criarFotoPrincipal(salvo.getId(), request.getFotoPrincipalUpload(), agora);
    if (fotoPrincipal == null || fotoPrincipal.getId() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Foto principal obrigatoria.");
    }
    salvo.setFotoPrincipalId(fotoPrincipal.getId());
    FotoEvento atualizado = repository.salvar(salvo);
    atualizarTagsEvento(atualizado.getId(), request.getTags());
    return mapEvento(atualizado);
  }

  @Override
  @Transactional
  public FotoEventoResponse atualizar(Long id, FotoEventoRequest request) {
    FotoEvento evento = buscarEvento(id);
    evento.setTitulo(request.getTitulo());
    evento.setDescricao(request.getDescricao());
    evento.setDataEvento(request.getDataEvento());
    evento.setLocal(request.getLocal());
    evento.setStatus(normalizarStatusEnum(request.getStatus()));
    evento.setTags(juntarTags(request.getTags()));
    evento.setUnidadeId(request.getUnidadeId());
    evento.setAtualizadoEm(LocalDateTime.now());

    if (request.getFotoPrincipalUpload() != null) {
      FotoEventoFoto novaPrincipal =
          criarFotoPrincipal(evento.getId(), request.getFotoPrincipalUpload(), LocalDateTime.now());
      if (novaPrincipal != null && novaPrincipal.getId() != null) {
        evento.setFotoPrincipalId(novaPrincipal.getId());
      }
    } else if (request.getFotoPrincipalId() != null) {
      FotoEventoFoto foto = buscarFoto(evento.getId(), request.getFotoPrincipalId());
      evento.setFotoPrincipalId(foto.getId());
    }

    FotoEvento atualizado = repository.salvar(evento);
    atualizarTagsEvento(atualizado.getId(), request.getTags());
    return mapEvento(atualizado);
  }

  @Override
  @Transactional
  public void excluir(Long id) {
    FotoEvento evento = buscarEvento(id);
    List<FotoEventoFoto> fotos = fotoRepository.listarPorEvento(evento.getId());
    Set<String> caminhos = new HashSet<>();
    for (FotoEventoFoto foto : fotos) {
      if (foto.getArquivo() != null) {
        caminhos.add(foto.getArquivo());
      }
    }

    fotos.forEach(fotoRepository::remover);
    repository.remover(evento);
    tagRepository.removerPorEvento(evento.getId());
    caminhos.forEach(armazenamentoService::removerArquivo);
  }

  @Override
  public FotoEventoFotoResponse adicionarFoto(Long id, FotoEventoFotoRequest request) {
    FotoEvento evento = buscarEvento(id);
    if (request == null || request.getArquivo() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Envie o arquivo da foto.");
    }

    FotoEventoArquivoInfo info = armazenamentoService.salvarArquivo(evento.getId(), request.getArquivo());
    if (info == null || info.getCaminho() == null || info.getCaminho().trim().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Arquivo invalido.");
    }

    FotoEventoFoto foto = new FotoEventoFoto();
    foto.setEventoId(evento.getId());
    foto.setArquivo(info.getCaminho());
    foto.setNomeArquivo(request.getArquivo().getNomeArquivo());
    foto.setMimeType(request.getArquivo().getContentType());
    foto.setTamanhoBytes(info.getTamanhoBytes());
    foto.setLargura(info.getLargura());
    foto.setAltura(info.getAltura());
    foto.setLegenda(request.getLegenda());
    foto.setCreditos(request.getCreditos());
    foto.setTags(juntarTags(request.getTags()));
    foto.setOrdem(request.getOrdem());
    foto.setCriadoEm(LocalDateTime.now());
    foto.setAtualizadoEm(LocalDateTime.now());

    FotoEventoFoto salva = fotoRepository.salvar(foto);
    if (evento.getFotoPrincipalId() == null) {
      evento.setFotoPrincipalId(salva.getId());
      evento.setAtualizadoEm(LocalDateTime.now());
      repository.salvar(evento);
    }
    return mapFoto(salva);
  }

  @Override
  public FotoEventoFotoResponse atualizarFoto(
      Long id, Long fotoId, FotoEventoFotoAtualizacaoRequest request) {
    FotoEvento evento = buscarEvento(id);
    FotoEventoFoto foto = buscarFoto(evento.getId(), fotoId);
    foto.setLegenda(request.getLegenda());
    foto.setCreditos(request.getCreditos());
    foto.setTags(juntarTags(request.getTags()));
    foto.setOrdem(request.getOrdem());
    foto.setAtualizadoEm(LocalDateTime.now());
    FotoEventoFoto salva = fotoRepository.salvar(foto);
    return mapFoto(salva);
  }

  @Override
  public void removerFoto(Long id, Long fotoId) {
    FotoEvento evento = buscarEvento(id);
    FotoEventoFoto foto = buscarFoto(evento.getId(), fotoId);

    if (evento.getFotoPrincipalId() != null
        && evento.getFotoPrincipalId().equals(foto.getId())) {     
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
      evento.setFotoPrincipalId(novaPrincipal.getId());
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
    if (evento.getFotoPrincipalId() == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo nao encontrado.");
    }
    try {
      FotoEventoFoto foto = buscarFoto(evento.getId(), evento.getFotoPrincipalId());
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
        evento.getId() != null && evento.getFotoPrincipalId() != null
            ? "/api/fotos-eventos/" + evento.getId() + "/principal"
            : null;
    long totalFotos = evento.getId() != null ? fotoRepository.contarPorEvento(evento.getId()) : 0;
    return new FotoEventoResponse(
        evento.getId(),
        evento.getUnidadeId(),
        evento.getTitulo(),
        evento.getDescricao(),
        evento.getDataEvento(),
        evento.getLocal(),
        evento.getStatus() != null ? evento.getStatus().name() : StatusFotoEvento.PLANEJADO.name(),
        separarTags(evento.getTags()),
        evento.getFotoPrincipalId(),
        principalUrl,
        totalFotos,
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
        foto.getNomeArquivo(),
        foto.getMimeType(),
        foto.getTamanhoBytes(),
        foto.getLargura(),
        foto.getAltura(),
        foto.getLegenda(),
        foto.getCreditos(),
        separarTags(foto.getTags()),
        foto.getOrdem(),
        foto.getCriadoEm(),
        foto.getAtualizadoEm());
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
      return "";
    }
    String texto = normalizarTexto(busca);
    return texto;
  }

  private String normalizarTexto(String valor) {
    if (valor == null) {
      return "";
    }
    String texto = valor.trim().toLowerCase(Locale.ROOT);
    if (texto.isEmpty()) {
      return "";
    }
    return java.text.Normalizer.normalize(texto, java.text.Normalizer.Form.NFD)
        .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
        .trim();
  }

  private StatusFotoEvento normalizarStatusEnum(String status) {
    if (status == null || status.trim().isEmpty()) {
      return StatusFotoEvento.PLANEJADO;
    }
    try {
      return StatusFotoEvento.valueOf(status.trim().toUpperCase(Locale.ROOT));
    } catch (IllegalArgumentException ex) {
      return StatusFotoEvento.PLANEJADO;
    }
  }

  private StatusFotoEvento normalizarStatusFiltro(String status) {
    if (status == null || status.trim().isEmpty()) {
      return null;
    }
    try {
      return StatusFotoEvento.valueOf(status.trim().toUpperCase(Locale.ROOT));
    } catch (IllegalArgumentException ex) {
      return null;
    }
  }

  private List<String> normalizarTags(List<String> tags) {
    if (tags == null || tags.isEmpty()) {
      return null;
    }
    List<String> normalizadas = new ArrayList<>();
    for (String tag : tags) {
      String valor = normalizarTexto(tag);
      if (!valor.isEmpty()) {
        normalizadas.add(valor);
      }
    }
    return normalizadas.isEmpty() ? null : normalizadas;
  }

  private String juntarTags(List<String> tags) {
    if (tags == null || tags.isEmpty()) {
      return null;
    }
    List<String> filtradas =
        tags.stream()
            .map((tag) -> tag == null ? "" : tag.trim())
            .filter((tag) -> !tag.isEmpty())
            .collect(Collectors.toList());
    return filtradas.isEmpty() ? null : String.join(", ", filtradas);
  }

  private List<String> separarTags(String tags) {
    if (tags == null || tags.trim().isEmpty()) {
      return java.util.Collections.emptyList();
    }
    return java.util.Arrays.stream(tags.split(","))
        .map(String::trim)
        .filter((tag) -> !tag.isEmpty())
        .collect(Collectors.toList());
  }

  private void atualizarTagsEvento(Long eventoId, List<String> tags) {
    tagRepository.removerPorEvento(eventoId);
    List<String> normalizadas = normalizarTags(tags);
    if (normalizadas == null) {
      return;
    }
    List<FotoEventoTag> registros = new ArrayList<>();
    for (String tag : normalizadas) {
      FotoEventoTag registro = new FotoEventoTag();
      registro.setEventoId(eventoId);
      registro.setTag(tag);
      registros.add(registro);
    }
    if (!registros.isEmpty()) {
      tagRepository.salvarTodos(registros);
    }
  }

  private FotoEventoFoto criarFotoPrincipal(
      Long eventoId,
      br.com.g3.fotoseventos.dto.FotoEventoUploadRequest upload,
      LocalDateTime agora) {
    FotoEventoArquivoInfo info = armazenamentoService.salvarArquivo(eventoId, upload);
    if (info == null || info.getCaminho() == null || info.getCaminho().trim().isEmpty()) {
      return null;
    }
    FotoEventoFoto foto = new FotoEventoFoto();
    foto.setEventoId(eventoId);
    foto.setArquivo(info.getCaminho());
    foto.setNomeArquivo(upload.getNomeArquivo());
    foto.setMimeType(upload.getContentType());
    foto.setTamanhoBytes(info.getTamanhoBytes());
    foto.setLargura(info.getLargura());
    foto.setAltura(info.getAltura());
    foto.setLegenda(null);
    foto.setCreditos(null);
    foto.setTags(null);
    foto.setOrdem(1);
    foto.setCriadoEm(agora);
    foto.setAtualizadoEm(agora);
    return fotoRepository.salvar(foto);
  }

  private Sort obterOrdenacao(String ordenacao) {
    if (ordenacao == null || ordenacao.trim().isEmpty()) {
      return Sort.by(Sort.Direction.DESC, "dataEvento");
    }
    String valor = ordenacao.trim().toUpperCase(Locale.ROOT);
    switch (valor) {
      case "MAIS_ANTIGO":
        return Sort.by(Sort.Direction.ASC, "dataEvento");
      case "A_Z":
        return Sort.by(Sort.Direction.ASC, "titulo");
      case "Z_A":
        return Sort.by(Sort.Direction.DESC, "titulo");
      case "MAIS_FOTOS":
        return Sort.unsorted();
      default:
        return Sort.by(Sort.Direction.DESC, "dataEvento");
    }
  }
}
