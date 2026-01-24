package br.com.g3.biblioteca.serviceimpl;

import br.com.g3.biblioteca.domain.BibliotecaEmprestimo;
import br.com.g3.biblioteca.domain.BibliotecaLivro;
import br.com.g3.biblioteca.dto.BibliotecaAlertaListaResponse;
import br.com.g3.biblioteca.dto.BibliotecaAlertaResponse;
import br.com.g3.biblioteca.dto.BibliotecaEmprestimoListaResponse;
import br.com.g3.biblioteca.dto.BibliotecaEmprestimoRequest;
import br.com.g3.biblioteca.dto.BibliotecaEmprestimoResponse;
import br.com.g3.biblioteca.dto.BibliotecaLivroListaResponse;
import br.com.g3.biblioteca.dto.BibliotecaLivroRequest;
import br.com.g3.biblioteca.dto.BibliotecaLivroResponse;
import br.com.g3.biblioteca.repository.BibliotecaRepository;
import br.com.g3.biblioteca.service.BibliotecaService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class BibliotecaServiceImpl implements BibliotecaService {
  private final BibliotecaRepository repositorio;

  public BibliotecaServiceImpl(BibliotecaRepository repositorio) {
    this.repositorio = repositorio;
  }

  @Override
  @Transactional(readOnly = true)
  public BibliotecaLivroListaResponse listarLivros() {
    List<BibliotecaLivroResponse> livros = repositorio.listarLivros().stream()
        .sorted(Comparator.comparing(BibliotecaLivro::getTitulo, String.CASE_INSENSITIVE_ORDER))
        .map(this::mapearLivroResposta)
        .collect(Collectors.toList());
    return new BibliotecaLivroListaResponse(livros);
  }

  @Override
  public BibliotecaLivroResponse criarLivro(BibliotecaLivroRequest requisicao) {
    validarLivroRequest(requisicao, null);

    BibliotecaLivro livro = new BibliotecaLivro();
    aplicarLivroRequest(livro, requisicao);
    LocalDateTime agora = LocalDateTime.now();
    livro.setCriadoEm(agora);
    livro.setAtualizadoEm(agora);
    BibliotecaLivro salvo = repositorio.salvarLivro(livro);
    return mapearLivroResposta(salvo);
  }

  @Override
  public String obterProximoCodigoLivro() {
    return String.valueOf(repositorio.obterProximoCodigoLivro());
  }

  @Override
  public BibliotecaLivroResponse atualizarLivro(Long id, BibliotecaLivroRequest requisicao) {
    BibliotecaLivro livro = repositorio.buscarLivroPorId(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro nao encontrado."));

    validarLivroRequest(requisicao, livro.getCodigo());
    aplicarLivroRequest(livro, requisicao);
    livro.setAtualizadoEm(LocalDateTime.now());
    BibliotecaLivro salvo = repositorio.salvarLivro(livro);
    return mapearLivroResposta(salvo);
  }

  @Override
  public void excluirLivro(Long id) {
    BibliotecaLivro livro = repositorio.buscarLivroPorId(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro nao encontrado."));

    long emprestimosAtivos = repositorio.contarEmprestimosAtivosPorLivro(id);
    if (emprestimosAtivos > 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Nao e possivel excluir livro com emprestimos ativos.");
    }

    repositorio.excluirLivro(livro);
  }

  @Override
  @Transactional(readOnly = true)
  public BibliotecaEmprestimoListaResponse listarEmprestimos() {
    atualizarEmprestimosAtrasados();
    List<BibliotecaEmprestimoResponse> emprestimos = repositorio.listarEmprestimos().stream()
        .map(this::mapearEmprestimoResposta)
        .collect(Collectors.toList());
    return new BibliotecaEmprestimoListaResponse(emprestimos);
  }

  @Override
  public BibliotecaEmprestimoResponse criarEmprestimo(BibliotecaEmprestimoRequest requisicao) {
    validarEmprestimoRequest(requisicao);

    BibliotecaLivro livro = repositorio.buscarLivroPorId(requisicao.getLivroId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro nao encontrado."));

    validarDisponibilidadeLivro(livro, requisicao.getStatus());

    BibliotecaEmprestimo emprestimo = new BibliotecaEmprestimo();
    emprestimo.setLivro(livro);
    aplicarEmprestimoRequest(emprestimo, requisicao);

    LocalDateTime agora = LocalDateTime.now();
    emprestimo.setCriadoEm(agora);
    emprestimo.setAtualizadoEm(agora);

    ajustarDisponibilidadeLivro(livro, null, emprestimo.getStatus());
    BibliotecaEmprestimo salvo = repositorio.salvarEmprestimo(emprestimo);
    return mapearEmprestimoResposta(salvo);
  }

  @Override
  public BibliotecaEmprestimoResponse atualizarEmprestimo(Long id, BibliotecaEmprestimoRequest requisicao) {
    BibliotecaEmprestimo emprestimo = repositorio.buscarEmprestimoPorId(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Emprestimo nao encontrado."));

    validarEmprestimoRequest(requisicao);

    BibliotecaLivro livroAtual = emprestimo.getLivro();
    BibliotecaLivro livroNovo = livroAtual;

    if (requisicao.getLivroId() != null && !Objects.equals(livroAtual.getId(), requisicao.getLivroId())) {
      livroNovo = repositorio.buscarLivroPorId(requisicao.getLivroId())
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro nao encontrado."));
    }

    String statusAnterior = emprestimo.getStatus();

    if (!Objects.equals(livroAtual.getId(), livroNovo.getId())) {
      ajustarDisponibilidadeLivro(livroAtual, statusAnterior, "CANCELADO");
      validarDisponibilidadeLivro(livroNovo, requisicao.getStatus());
      emprestimo.setLivro(livroNovo);
      ajustarDisponibilidadeLivro(livroNovo, null, statusDeterminado(requisicao));
    } else {
      ajustarDisponibilidadeLivro(livroAtual, statusAnterior, statusDeterminado(requisicao));
    }

    aplicarEmprestimoRequest(emprestimo, requisicao);
    emprestimo.setAtualizadoEm(LocalDateTime.now());
    BibliotecaEmprestimo salvo = repositorio.salvarEmprestimo(emprestimo);
    return mapearEmprestimoResposta(salvo);
  }

  @Override
  public BibliotecaEmprestimoResponse registrarDevolucao(Long id, BibliotecaEmprestimoRequest requisicao) {
    BibliotecaEmprestimo emprestimo = repositorio.buscarEmprestimoPorId(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Emprestimo nao encontrado."));

    if ("DEVOLVIDO".equalsIgnoreCase(emprestimo.getStatus())) {
    return mapearEmprestimoResposta(emprestimo);
    }

    LocalDate dataDevolucao = requisicao != null && requisicao.getDataDevolucaoReal() != null
        ? requisicao.getDataDevolucaoReal()
        : LocalDate.now();

    String statusAnterior = emprestimo.getStatus();
    emprestimo.setDataDevolucaoReal(dataDevolucao);
    emprestimo.setStatus("DEVOLVIDO");
    emprestimo.setAtualizadoEm(LocalDateTime.now());

    ajustarDisponibilidadeLivro(emprestimo.getLivro(), statusAnterior, "DEVOLVIDO");

    BibliotecaEmprestimo salvo = repositorio.salvarEmprestimo(emprestimo);
    return mapearEmprestimoResposta(salvo);
  }

  @Override
  public void excluirEmprestimo(Long id) {
    BibliotecaEmprestimo emprestimo = repositorio.buscarEmprestimoPorId(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Emprestimo nao encontrado."));

    ajustarDisponibilidadeLivro(emprestimo.getLivro(), emprestimo.getStatus(), "CANCELADO");
    repositorio.excluirEmprestimo(emprestimo);
  }

  @Override
  @Transactional(readOnly = true)
  public BibliotecaAlertaListaResponse listarAlertas() {
    atualizarEmprestimosAtrasados();
    LocalDate hoje = LocalDate.now();
    List<BibliotecaAlertaResponse> alertas = repositorio.listarEmprestimos().stream()
        .filter(emprestimo -> "ATIVO".equalsIgnoreCase(emprestimo.getStatus())
            || "ATRASADO".equalsIgnoreCase(emprestimo.getStatus()))
        .map(emprestimo -> montarAlerta(emprestimo, hoje))
        .sorted(Comparator.comparing(BibliotecaAlertaResponse::getDiasParaVencimento))
        .collect(Collectors.toList());
    return new BibliotecaAlertaListaResponse(alertas);
  }

  private void validarLivroRequest(BibliotecaLivroRequest requisicao, String codigoAtual) {
    if (requisicao == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do livro nao informados.");
    }

    if (isBlank(requisicao.getCodigo())) {
      requisicao.setCodigo(String.valueOf(repositorio.obterProximoCodigoLivro()));
    }

    if (isBlank(requisicao.getTitulo())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Titulo do livro nao informado.");
    }

    if (isBlank(requisicao.getAutor())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Autor do livro nao informado.");
    }

    if (requisicao.getQuantidadeTotal() == null || requisicao.getQuantidadeTotal() < 1) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantidade total deve ser maior que zero.");
    }

    if (requisicao.getQuantidadeDisponivel() == null || requisicao.getQuantidadeDisponivel() < 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantidade disponivel nao pode ser negativa.");
    }

    if (requisicao.getQuantidadeDisponivel() > requisicao.getQuantidadeTotal()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Quantidade disponivel nao pode ser maior que a quantidade total.");
    }

    if (isBlank(requisicao.getStatus())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status do livro nao informado.");
    }

    String codigoNormalizado = requisicao.getCodigo().trim();
    if (codigoAtual == null || !codigoAtual.equalsIgnoreCase(codigoNormalizado)) {
      repositorio.buscarLivroPorCodigo(codigoNormalizado).ifPresent((existente) -> {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Codigo do livro ja cadastrado.");
      });
    }
  }

  private void validarEmprestimoRequest(BibliotecaEmprestimoRequest requisicao) {
    if (requisicao == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do emprestimo nao informados.");
    }

    if (requisicao.getLivroId() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Livro do emprestimo nao informado.");
    }

    if (isBlank(requisicao.getBeneficiarioNome())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Beneficiario do emprestimo nao informado.");
    }

    if (requisicao.getDataEmprestimo() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data de emprestimo nao informada.");
    }

    if (requisicao.getDataDevolucaoPrevista() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data de devolucao prevista nao informada.");
    }

    if (requisicao.getDataDevolucaoPrevista().isBefore(requisicao.getDataEmprestimo())) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Data de devolucao prevista nao pode ser anterior ao emprestimo.");
    }
  }

  private void aplicarLivroRequest(BibliotecaLivro livro, BibliotecaLivroRequest requisicao) {
    livro.setCodigo(requisicao.getCodigo().trim());
    livro.setTitulo(requisicao.getTitulo().trim());
    livro.setAutor(requisicao.getAutor().trim());
    livro.setIsbn(safeTrim(requisicao.getIsbn()));
    livro.setEditora(safeTrim(requisicao.getEditora()));
    livro.setAnoPublicacao(requisicao.getAnoPublicacao());
    livro.setCategoria(safeTrim(requisicao.getCategoria()));
    livro.setQuantidadeTotal(requisicao.getQuantidadeTotal());
    livro.setQuantidadeDisponivel(requisicao.getQuantidadeDisponivel());
    livro.setLocalizacao(safeTrim(requisicao.getLocalizacao()));
    livro.setStatus(requisicao.getStatus().trim().toUpperCase());
    livro.setEstadoLivro(safeTrim(requisicao.getEstadoLivro()));
    livro.setObservacoes(safeTrim(requisicao.getObservacoes()));
  }

  private void aplicarEmprestimoRequest(BibliotecaEmprestimo emprestimo, BibliotecaEmprestimoRequest requisicao) {
    emprestimo.setBeneficiarioId(requisicao.getBeneficiarioId());
    emprestimo.setBeneficiarioNome(safeTrim(requisicao.getBeneficiarioNome()));
    emprestimo.setResponsavelId(requisicao.getResponsavelId());
    emprestimo.setResponsavelNome(safeTrim(requisicao.getResponsavelNome()));
    emprestimo.setDataEmprestimo(requisicao.getDataEmprestimo());
    emprestimo.setDataDevolucaoPrevista(requisicao.getDataDevolucaoPrevista());
    emprestimo.setDataDevolucaoReal(requisicao.getDataDevolucaoReal());
    emprestimo.setStatus(statusDeterminado(requisicao));
    emprestimo.setObservacoes(safeTrim(requisicao.getObservacoes()));
  }

  private String statusDeterminado(BibliotecaEmprestimoRequest requisicao) {
    if (requisicao.getDataDevolucaoReal() != null) {
      return "DEVOLVIDO";
    }
    if (!isBlank(requisicao.getStatus())) {
      return requisicao.getStatus().trim().toUpperCase();
    }
    return "ATIVO";
  }

  private void validarDisponibilidadeLivro(BibliotecaLivro livro, String status) {
    if (livro == null) return;
    String statusEfetivo = status != null ? status.trim().toUpperCase() : "ATIVO";
    if (("ATIVO".equals(statusEfetivo) || "ATRASADO".equals(statusEfetivo))
        && livro.getQuantidadeDisponivel() != null
        && livro.getQuantidadeDisponivel() <= 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Livro sem disponibilidade para emprestimo.");
    }
  }

  private void ajustarDisponibilidadeLivro(BibliotecaLivro livro, String statusAnterior, String statusNovo) {
    if (livro == null) return;

    boolean antesAtivo = "ATIVO".equalsIgnoreCase(statusAnterior) || "ATRASADO".equalsIgnoreCase(statusAnterior);
    boolean depoisAtivo = "ATIVO".equalsIgnoreCase(statusNovo) || "ATRASADO".equalsIgnoreCase(statusNovo);

    int disponibilidade = livro.getQuantidadeDisponivel() != null ? livro.getQuantidadeDisponivel() : 0;

    if (!antesAtivo && depoisAtivo) {
      livro.setQuantidadeDisponivel(Math.max(0, disponibilidade - 1));
      livro.setAtualizadoEm(LocalDateTime.now());
      repositorio.salvarLivro(livro);
    }

    if (antesAtivo && !depoisAtivo) {
      livro.setQuantidadeDisponivel(disponibilidade + 1);
      livro.setAtualizadoEm(LocalDateTime.now());
      repositorio.salvarLivro(livro);
    }
  }

  private void atualizarEmprestimosAtrasados() {
    LocalDate hoje = LocalDate.now();
    List<BibliotecaEmprestimo> emprestimos = repositorio.listarEmprestimos();
    for (BibliotecaEmprestimo emprestimo : emprestimos) {
      if ("ATIVO".equalsIgnoreCase(emprestimo.getStatus())
          && emprestimo.getDataDevolucaoPrevista() != null
          && emprestimo.getDataDevolucaoPrevista().isBefore(hoje)) {
        emprestimo.setStatus("ATRASADO");
        emprestimo.setAtualizadoEm(LocalDateTime.now());
        repositorio.salvarEmprestimo(emprestimo);
      }
    }
  }

  private BibliotecaLivroResponse mapearLivroResposta(BibliotecaLivro livro) {
    BibliotecaLivroResponse resposta = new BibliotecaLivroResponse();
    resposta.setId(livro.getId());
    resposta.setCodigo(livro.getCodigo());
    resposta.setTitulo(livro.getTitulo());
    resposta.setAutor(livro.getAutor());
    resposta.setIsbn(livro.getIsbn());
    resposta.setEditora(livro.getEditora());
    resposta.setAnoPublicacao(livro.getAnoPublicacao());
    resposta.setCategoria(livro.getCategoria());
    resposta.setQuantidadeTotal(livro.getQuantidadeTotal());
    resposta.setQuantidadeDisponivel(livro.getQuantidadeDisponivel());
    resposta.setLocalizacao(livro.getLocalizacao());
    resposta.setStatus(livro.getStatus());
    resposta.setEstadoLivro(livro.getEstadoLivro());
    resposta.setObservacoes(livro.getObservacoes());
    resposta.setCriadoEm(livro.getCriadoEm());
    resposta.setAtualizadoEm(livro.getAtualizadoEm());
    return resposta;
  }

  private BibliotecaEmprestimoResponse mapearEmprestimoResposta(BibliotecaEmprestimo emprestimo) {
    BibliotecaEmprestimoResponse resposta = new BibliotecaEmprestimoResponse();
    resposta.setId(emprestimo.getId());
    resposta.setLivroId(emprestimo.getLivro().getId());
    resposta.setLivroTitulo(emprestimo.getLivro().getTitulo());
    resposta.setLivroCodigo(emprestimo.getLivro().getCodigo());
    resposta.setBeneficiarioId(emprestimo.getBeneficiarioId());
    resposta.setBeneficiarioNome(emprestimo.getBeneficiarioNome());
    resposta.setResponsavelId(emprestimo.getResponsavelId());
    resposta.setResponsavelNome(emprestimo.getResponsavelNome());
    resposta.setDataEmprestimo(emprestimo.getDataEmprestimo());
    resposta.setDataDevolucaoPrevista(emprestimo.getDataDevolucaoPrevista());
    resposta.setDataDevolucaoReal(emprestimo.getDataDevolucaoReal());
    resposta.setStatus(emprestimo.getStatus());
    resposta.setObservacoes(emprestimo.getObservacoes());
    return resposta;
  }

  private BibliotecaAlertaResponse montarAlerta(BibliotecaEmprestimo emprestimo, LocalDate hoje) {
    BibliotecaAlertaResponse alerta = new BibliotecaAlertaResponse();
    alerta.setEmprestimoId(emprestimo.getId());
    alerta.setLivroTitulo(emprestimo.getLivro().getTitulo());
    alerta.setBeneficiarioNome(emprestimo.getBeneficiarioNome());
    alerta.setDataDevolucaoPrevista(emprestimo.getDataDevolucaoPrevista());

    long dias = ChronoUnit.DAYS.between(hoje, emprestimo.getDataDevolucaoPrevista());
    alerta.setDiasParaVencimento(dias);

    if (dias < 0) {
      alerta.setStatus("ATRASADO");
    } else if (dias <= 3) {
      alerta.setStatus("VENCENDO");
    } else {
      alerta.setStatus("EM_DIA");
    }

    return alerta;
  }

  private boolean isBlank(String valor) {
    return valor == null || valor.trim().isEmpty();
  }

  private String safeTrim(String valor) {
    return isBlank(valor) ? null : valor.trim();
  }
}
