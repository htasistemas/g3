package br.com.g3.cadastrovoluntario.mapper;

import br.com.g3.cadastrovoluntario.domain.CadastroVoluntario;
import br.com.g3.cadastrovoluntario.dto.CadastroVoluntarioCriacaoRequest;
import br.com.g3.cadastrovoluntario.dto.CadastroVoluntarioResponse;
import br.com.g3.unidadeassistencial.domain.Endereco;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CadastroVoluntarioMapper {
  private CadastroVoluntarioMapper() {}
  private static final String STATUS_PADRAO = "ATIVO";

  public static CadastroVoluntario toDomain(CadastroVoluntarioCriacaoRequest request) {
    CadastroVoluntario cadastro = new CadastroVoluntario();
    aplicarDados(cadastro, request);
    LocalDateTime agora = LocalDateTime.now();
    cadastro.setCriadoEm(agora);
    cadastro.setAtualizadoEm(agora);
    return cadastro;
  }

  public static void aplicarAtualizacao(
      CadastroVoluntario cadastro, CadastroVoluntarioCriacaoRequest request) {
    aplicarDados(cadastro, request);
    cadastro.setAtualizadoEm(LocalDateTime.now());
  }

  public static CadastroVoluntarioResponse toResponse(CadastroVoluntario cadastro) {
    Endereco endereco = cadastro.getEndereco();
    return new CadastroVoluntarioResponse(
        cadastro.getId(),
        cadastro.getProfissional() != null ? cadastro.getProfissional().getId() : null,
        cadastro.getNomeCompleto(),
        cadastro.getCpf(),
        cadastro.getRg(),
        cadastro.getFoto3x4(),
        endereco != null ? endereco.getCep() : null,
        endereco != null ? endereco.getLogradouro() : null,
        endereco != null ? endereco.getNumero() : null,
        endereco != null ? endereco.getComplemento() : null,
        endereco != null ? endereco.getBairro() : null,
        endereco != null ? endereco.getPontoReferencia() : null,
        endereco != null ? endereco.getCidade() : null,
        endereco != null ? endereco.getZona() : null,
        endereco != null ? endereco.getEstado() : null,
        cadastro.getDataNascimento(),
        cadastro.getGenero(),
        cadastro.getProfissao(),
        cadastro.getMotivacao(),
        cadastro.getTelefone(),
        cadastro.getEmail(),
        cadastro.getCidade(),
        cadastro.getEstado(),
        cadastro.getAreaInteresse(),
        cadastro.getHabilidades(),
        cadastro.getIdiomas(),
        cadastro.getLinkedin(),
        cadastro.getStatus(),
        separarLista(cadastro.getDisponibilidadeDias()),
        separarLista(cadastro.getDisponibilidadePeriodos()),
        cadastro.getCargaHorariaSemanal(),
        cadastro.getPresencial(),
        cadastro.getRemoto(),
        cadastro.getInicioPrevisto(),
        cadastro.getObservacoes(),
        cadastro.getDocumentoIdentificacao(),
        cadastro.getComprovanteEndereco(),
        cadastro.isAceiteVoluntariado(),
        cadastro.isAceiteImagem(),
        cadastro.getAssinaturaDigital(),
        cadastro.getCriadoEm(),
        cadastro.getAtualizadoEm());
  }

  public static void aplicarDados(
      CadastroVoluntario cadastro, CadastroVoluntarioCriacaoRequest request) {
    cadastro.setNomeCompleto(limparTexto(request.getNomeCompleto()));
    cadastro.setCpf(limparTexto(request.getCpf()));
    cadastro.setRg(limparTexto(request.getRg()));
    cadastro.setFoto3x4(limparTexto(request.getFoto3x4()));
    aplicarEndereco(cadastro, request);
    cadastro.setDataNascimento(request.getDataNascimento());
    cadastro.setGenero(limparTexto(request.getGenero()));
    cadastro.setProfissao(limparTexto(request.getProfissao()));
    cadastro.setMotivacao(limparTexto(request.getMotivacao()));
    cadastro.setTelefone(limparTexto(request.getTelefone()));
    cadastro.setEmail(limparTexto(request.getEmail()));
    cadastro.setCidade(limparTexto(request.getCidade()));
    cadastro.setEstado(limparTexto(request.getEstado()));
    cadastro.setAreaInteresse(limparTexto(request.getAreaInteresse()));
    cadastro.setHabilidades(limparTexto(request.getHabilidades()));
    cadastro.setIdiomas(limparTexto(request.getIdiomas()));
    cadastro.setLinkedin(limparTexto(request.getLinkedin()));
    cadastro.setStatus(normalizarStatus(request.getStatus()));
    cadastro.setDisponibilidadeDias(juntarLista(request.getDisponibilidadeDias()));
    cadastro.setDisponibilidadePeriodos(juntarLista(request.getDisponibilidadePeriodos()));
    cadastro.setCargaHorariaSemanal(limparTexto(request.getCargaHorariaSemanal()));
    cadastro.setPresencial(request.getPresencial());
    cadastro.setRemoto(request.getRemoto());
    cadastro.setInicioPrevisto(request.getInicioPrevisto());
    cadastro.setObservacoes(limparTexto(request.getObservacoes()));
    cadastro.setDocumentoIdentificacao(limparTexto(request.getDocumentoIdentificacao()));
    cadastro.setComprovanteEndereco(limparTexto(request.getComprovanteEndereco()));
    cadastro.setAceiteVoluntariado(Boolean.TRUE.equals(request.getAceiteVoluntariado()));
    cadastro.setAceiteImagem(Boolean.TRUE.equals(request.getAceiteImagem()));
    cadastro.setAssinaturaDigital(limparTexto(request.getAssinaturaDigital()));
  }

  private static Endereco criarEndereco(CadastroVoluntarioCriacaoRequest request, LocalDateTime agora) {
    if (!possuiDadosEndereco(request)) {
      return null;
    }
    Endereco endereco = new Endereco();
    endereco.setCep(limparTexto(request.getCep()));
    endereco.setLogradouro(limparTexto(request.getLogradouro()));
    endereco.setNumero(limparTexto(request.getNumero()));
    endereco.setComplemento(limparTexto(request.getComplemento()));
    endereco.setBairro(limparTexto(request.getBairro()));
    endereco.setPontoReferencia(limparTexto(request.getPontoReferencia()));
    endereco.setCidade(limparTexto(request.getMunicipio()));
    endereco.setZona(limparTexto(request.getZona()));
    endereco.setEstado(limparTexto(request.getUf()));
    endereco.setCriadoEm(agora);
    endereco.setAtualizadoEm(agora);
    return endereco;
  }

  private static void aplicarEndereco(CadastroVoluntario cadastro, CadastroVoluntarioCriacaoRequest request) {
    if (!possuiDadosEndereco(request)) {
      cadastro.setEndereco(null);
      return;
    }

    LocalDateTime agora = LocalDateTime.now();
    Endereco endereco = cadastro.getEndereco();

    if (endereco == null) {
      endereco = new Endereco();
      endereco.setCriadoEm(agora);
      cadastro.setEndereco(endereco);
    } else if (endereco.getCriadoEm() == null) {
      endereco.setCriadoEm(agora);
    }

    endereco.setCep(limparTexto(request.getCep()));
    endereco.setLogradouro(limparTexto(request.getLogradouro()));
    endereco.setNumero(limparTexto(request.getNumero()));
    endereco.setComplemento(limparTexto(request.getComplemento()));
    endereco.setBairro(limparTexto(request.getBairro()));
    endereco.setPontoReferencia(limparTexto(request.getPontoReferencia()));
    endereco.setCidade(limparTexto(request.getMunicipio()));
    endereco.setZona(limparTexto(request.getZona()));
    endereco.setEstado(limparTexto(request.getUf()));
    endereco.setAtualizadoEm(agora);
  }

  private static boolean possuiDadosEndereco(CadastroVoluntarioCriacaoRequest request) {
    return temValor(request.getCep())
        || temValor(request.getLogradouro())
        || temValor(request.getNumero())
        || temValor(request.getComplemento())
        || temValor(request.getBairro())
        || temValor(request.getPontoReferencia())
        || temValor(request.getMunicipio())
        || temValor(request.getZona())
        || temValor(request.getUf());
  }

  private static String normalizarStatus(String status) {
    String normalized = limparTexto(status);
    return normalized != null ? normalized : STATUS_PADRAO;
  }

  private static String limparTexto(String value) {
    if (value == null) {
      return null;
    }
    String trimmed = value.trim();
    return trimmed.isEmpty() ? null : trimmed;
  }

  private static boolean temValor(String value) {
    return value != null && !value.trim().isEmpty();
  }

  private static String juntarLista(List<String> values) {
    if (values == null || values.isEmpty()) {
      return null;
    }
    return values.stream()
        .map(String::trim)
        .filter((item) -> !item.isEmpty())
        .collect(Collectors.joining(" | "));
  }

  private static List<String> separarLista(String value) {
    if (value == null || value.trim().isEmpty()) {
      return java.util.Collections.emptyList();
    }
    return java.util.Arrays.stream(value.split("\\|"))
      .map(String::trim)
      .filter((item) -> !item.isEmpty())
      .collect(Collectors.toList());
  }
}
