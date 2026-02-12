package br.com.g3.unidadeassistencial.mapper;

import br.com.g3.unidadeassistencial.domain.DiretoriaUnidade;
import br.com.g3.unidadeassistencial.domain.Endereco;
import br.com.g3.unidadeassistencial.domain.ImagemUnidade;
import br.com.g3.unidadeassistencial.domain.UnidadeAssistencial;
import br.com.g3.unidadeassistencial.dto.DiretoriaUnidadeRequest;
import br.com.g3.unidadeassistencial.dto.DiretoriaUnidadeResponse;
import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialCriacaoRequest;
import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UnidadeAssistencialMapper {
  private UnidadeAssistencialMapper() {}

  public static UnidadeAssistencial toDomain(UnidadeAssistencialCriacaoRequest request) {
    UnidadeAssistencial unidade = new UnidadeAssistencial();
    unidade.setNomeFantasia(request.getNomeFantasia());
    unidade.setRazaoSocial(request.getRazaoSocial());
    unidade.setCnpj(request.getCnpj());
    unidade.setEmail(request.getEmail());
    unidade.setSite(request.getSite());
    unidade.setTelefone(request.getTelefone());
    unidade.setHorarioFuncionamento(request.getHorarioFuncionamento());
    unidade.setObservacoes(request.getObservacoes());
    unidade.setUnidadePrincipal(request.isUnidadePrincipal());
    unidade.setRaioPontoMetros(request.getRaioPontoMetros());
    unidade.setAccuracyMaxPontoMetros(request.getAccuracyMaxPontoMetros());
    unidade.setIpValidacaoPonto(request.getIpValidacaoPonto());
    unidade.setPingTimeoutMs(request.getPingTimeoutMs());
    LocalDateTime agora = LocalDateTime.now();
    unidade.setCriadoEm(agora);
    unidade.setAtualizadoEm(agora);
    unidade.setEndereco(criarEndereco(request, agora));
    unidade.setImagemUnidade(criarImagemUnidade(request, unidade, agora));
    unidade.setDiretoria(criarDiretoria(request, unidade, agora));
    return unidade;
  }

  public static void aplicarAtualizacao(UnidadeAssistencial unidade, UnidadeAssistencialCriacaoRequest request) {
    unidade.setNomeFantasia(request.getNomeFantasia());
    unidade.setRazaoSocial(request.getRazaoSocial());
    unidade.setCnpj(request.getCnpj());
    unidade.setEmail(request.getEmail());
    unidade.setSite(request.getSite());
    unidade.setTelefone(request.getTelefone());
    unidade.setHorarioFuncionamento(request.getHorarioFuncionamento());
    unidade.setObservacoes(request.getObservacoes());
    unidade.setUnidadePrincipal(request.isUnidadePrincipal());
    unidade.setRaioPontoMetros(request.getRaioPontoMetros());
    unidade.setAccuracyMaxPontoMetros(request.getAccuracyMaxPontoMetros());
    unidade.setIpValidacaoPonto(request.getIpValidacaoPonto());
    unidade.setPingTimeoutMs(request.getPingTimeoutMs());
    unidade.setAtualizadoEm(LocalDateTime.now());
    aplicarEndereco(unidade, request);
    aplicarImagemUnidade(unidade, request);
    aplicarDiretoria(unidade, request);
  }

  public static UnidadeAssistencialResponse toResponse(UnidadeAssistencial unidade) {
    Endereco endereco = unidade.getEndereco();
    ImagemUnidade imagemUnidade = unidade.getImagemUnidade();
    List<DiretoriaUnidadeResponse> diretoria = mapearDiretoria(unidade.getDiretoria());
    return new UnidadeAssistencialResponse(
        unidade.getId(),
        unidade.getNomeFantasia(),
        unidade.getRazaoSocial(),
        unidade.getCnpj(),
        unidade.getEmail(),
        unidade.getSite(),
        unidade.getTelefone(),
        unidade.getHorarioFuncionamento(),
        unidade.getObservacoes(),
        imagemUnidade != null ? imagemUnidade.getLogomarca() : null,
        imagemUnidade != null ? imagemUnidade.getLogomarcaRelatorio() : null,
        diretoria,
        unidade.isUnidadePrincipal(),
        endereco != null ? endereco.getId() : null,
        endereco != null ? endereco.getCep() : null,
        endereco != null ? endereco.getLogradouro() : null,
        endereco != null ? endereco.getNumero() : null,
        endereco != null ? endereco.getComplemento() : null,
        endereco != null ? endereco.getBairro() : null,
        endereco != null ? endereco.getPontoReferencia() : null,
        endereco != null ? endereco.getCidade() : null,
        endereco != null ? endereco.getZona() : null,
        endereco != null ? endereco.getSubzona() : null,
        endereco != null ? endereco.getEstado() : null,
        endereco != null && endereco.getLatitude() != null ? endereco.getLatitude().toPlainString() : null,
        endereco != null && endereco.getLongitude() != null ? endereco.getLongitude().toPlainString() : null,
        unidade.getRaioPontoMetros(),
        unidade.getAccuracyMaxPontoMetros(),
        unidade.getIpValidacaoPonto(),
        unidade.getPingTimeoutMs());
  }

  private static Endereco criarEndereco(UnidadeAssistencialCriacaoRequest request, LocalDateTime agora) {
    if (!possuiDadosEndereco(request)) {
      return null;
    }
    Endereco endereco = new Endereco();
    endereco.setCep(request.getCep());
    endereco.setLogradouro(request.getEndereco());
    endereco.setNumero(request.getNumeroEndereco());
    endereco.setComplemento(request.getComplemento());
    endereco.setBairro(request.getBairro());
    endereco.setPontoReferencia(request.getPontoReferencia());
    endereco.setCidade(request.getCidade());
    endereco.setZona(request.getZona());
    endereco.setSubzona(request.getSubzona());
    endereco.setEstado(request.getEstado());
    endereco.setLatitude(parseCoordenada(request.getLatitude()));
    endereco.setLongitude(parseCoordenada(request.getLongitude()));
    endereco.setCriadoEm(agora);
    endereco.setAtualizadoEm(agora);
    return endereco;
  }

  private static void aplicarEndereco(UnidadeAssistencial unidade, UnidadeAssistencialCriacaoRequest request) {
    if (!possuiDadosEndereco(request)) {
      unidade.setEndereco(null);
      return;
    }

    LocalDateTime agora = LocalDateTime.now();
    Endereco endereco = unidade.getEndereco();

    if (endereco == null) {
      endereco = new Endereco();
      endereco.setCriadoEm(agora);
      unidade.setEndereco(endereco);
    } else if (endereco.getCriadoEm() == null) {
      endereco.setCriadoEm(agora);
    }

    endereco.setCep(request.getCep());
    endereco.setLogradouro(request.getEndereco());
    endereco.setNumero(request.getNumeroEndereco());
    endereco.setComplemento(request.getComplemento());
    endereco.setBairro(request.getBairro());
    endereco.setPontoReferencia(request.getPontoReferencia());
    endereco.setCidade(request.getCidade());
    endereco.setZona(request.getZona());
    endereco.setSubzona(request.getSubzona());
    endereco.setEstado(request.getEstado());
    endereco.setLatitude(parseCoordenada(request.getLatitude()));
    endereco.setLongitude(parseCoordenada(request.getLongitude()));
    endereco.setAtualizadoEm(agora);
  }

  private static List<DiretoriaUnidade> criarDiretoria(
      UnidadeAssistencialCriacaoRequest request, UnidadeAssistencial unidade, LocalDateTime agora) {
    if (request.getDiretoria() == null || request.getDiretoria().isEmpty()) {
      return new ArrayList<>();
    }

    List<DiretoriaUnidade> diretoria = new ArrayList<>();
    for (DiretoriaUnidadeRequest item : request.getDiretoria()) {
      DiretoriaUnidade membro = new DiretoriaUnidade();
      membro.setUnidadeAssistencial(unidade);
      membro.setNomeCompleto(limparTexto(item.getNomeCompleto()));
      membro.setDocumento(limparTexto(item.getDocumento()));
      membro.setFuncao(limparTexto(item.getFuncao()));
      membro.setMandatoInicio(limparTexto(item.getMandatoInicio()));
      membro.setMandatoFim(limparTexto(item.getMandatoFim()));
      membro.setCriadoEm(agora);
      membro.setAtualizadoEm(agora);
      diretoria.add(membro);
    }
    return diretoria;
  }

  private static void aplicarDiretoria(UnidadeAssistencial unidade, UnidadeAssistencialCriacaoRequest request) {
    List<DiretoriaUnidade> diretoriaAtual = unidade.getDiretoria();
    diretoriaAtual.clear();

    if (request.getDiretoria() == null || request.getDiretoria().isEmpty()) {
      return;
    }

    LocalDateTime agora = LocalDateTime.now();
    for (DiretoriaUnidadeRequest item : request.getDiretoria()) {
      DiretoriaUnidade membro = new DiretoriaUnidade();
      membro.setUnidadeAssistencial(unidade);
      membro.setNomeCompleto(limparTexto(item.getNomeCompleto()));
      membro.setDocumento(limparTexto(item.getDocumento()));
      membro.setFuncao(limparTexto(item.getFuncao()));
      membro.setMandatoInicio(limparTexto(item.getMandatoInicio()));
      membro.setMandatoFim(limparTexto(item.getMandatoFim()));
      membro.setCriadoEm(agora);
      membro.setAtualizadoEm(agora);
      diretoriaAtual.add(membro);
    }
  }

  private static ImagemUnidade criarImagemUnidade(
      UnidadeAssistencialCriacaoRequest request, UnidadeAssistencial unidade, LocalDateTime agora) {
    if (!possuiDadosImagem(request)) {
      return null;
    }
    ImagemUnidade imagemUnidade = new ImagemUnidade();
    imagemUnidade.setUnidadeAssistencial(unidade);
    imagemUnidade.setLogomarca(limparTexto(request.getLogomarca()));
    imagemUnidade.setLogomarcaRelatorio(limparTexto(request.getLogomarcaRelatorio()));
    imagemUnidade.setCriadoEm(agora);
    imagemUnidade.setAtualizadoEm(agora);
    return imagemUnidade;
  }

  private static void aplicarImagemUnidade(UnidadeAssistencial unidade, UnidadeAssistencialCriacaoRequest request) {
    if (!possuiDadosImagem(request)) {
      unidade.setImagemUnidade(null);
      return;
    }

    LocalDateTime agora = LocalDateTime.now();
    ImagemUnidade imagemUnidade = unidade.getImagemUnidade();

    if (imagemUnidade == null) {
      imagemUnidade = new ImagemUnidade();
      imagemUnidade.setUnidadeAssistencial(unidade);
      imagemUnidade.setCriadoEm(agora);
      unidade.setImagemUnidade(imagemUnidade);
    } else if (imagemUnidade.getCriadoEm() == null) {
      imagemUnidade.setCriadoEm(agora);
    }

    imagemUnidade.setLogomarca(limparTexto(request.getLogomarca()));
    imagemUnidade.setLogomarcaRelatorio(limparTexto(request.getLogomarcaRelatorio()));
    imagemUnidade.setAtualizadoEm(agora);
  }

  private static boolean possuiDadosEndereco(UnidadeAssistencialCriacaoRequest request) {
    return temValor(request.getCep())
        || temValor(request.getEndereco())
        || temValor(request.getNumeroEndereco())
        || temValor(request.getComplemento())
        || temValor(request.getBairro())
        || temValor(request.getPontoReferencia())
        || temValor(request.getCidade())
        || temValor(request.getZona())
        || temValor(request.getSubzona())
        || temValor(request.getEstado())
        || temValor(request.getLatitude())
        || temValor(request.getLongitude());
  }

  private static BigDecimal parseCoordenada(String valor) {
    if (valor == null || valor.trim().isEmpty()) {
      return null;
    }
    String ajustado = valor.trim().replace(",", ".");
    try {
      return new BigDecimal(ajustado);
    } catch (NumberFormatException ex) {
      return null;
    }
  }

  private static boolean possuiDadosImagem(UnidadeAssistencialCriacaoRequest request) {
    return temValor(request.getLogomarca()) || temValor(request.getLogomarcaRelatorio());
  }

  private static List<DiretoriaUnidadeResponse> mapearDiretoria(List<DiretoriaUnidade> diretoria) {
    if (diretoria == null || diretoria.isEmpty()) {
      return new ArrayList<>();
    }

    return diretoria.stream()
        .map(
            item ->
                new DiretoriaUnidadeResponse(
                    item.getId(),
                    item.getNomeCompleto(),
                    item.getDocumento(),
                    item.getFuncao(),
                    item.getMandatoInicio(),
                    item.getMandatoFim()))
        .collect(Collectors.toList());
  }

  private static String limparTexto(String valor) {
    if (valor == null) {
      return null;
    }
    String trimmed = valor.trim();
    return trimmed.isEmpty() ? null : trimmed;
  }

  private static boolean temValor(String valor) {
    return valor != null && !valor.trim().isEmpty();
  }
}
