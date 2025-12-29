package br.com.g3.cadastroprofissionais.mapper;

import br.com.g3.cadastroprofissionais.domain.CadastroProfissional;
import br.com.g3.cadastroprofissionais.dto.CadastroProfissionalCriacaoRequest;
import br.com.g3.cadastroprofissionais.dto.CadastroProfissionalResponse;
import br.com.g3.unidadeassistencial.domain.Endereco;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CadastroProfissionalMapper {
  private CadastroProfissionalMapper() {}

  public static CadastroProfissional toDomain(CadastroProfissionalCriacaoRequest request) {
    CadastroProfissional cadastro = new CadastroProfissional();
    aplicarDados(cadastro, request);
    LocalDateTime agora = LocalDateTime.now();
    cadastro.setEndereco(criarEndereco(request, agora));
    cadastro.setCriadoEm(agora);
    cadastro.setAtualizadoEm(agora);
    return cadastro;
  }

  public static void aplicarAtualizacao(CadastroProfissional cadastro, CadastroProfissionalCriacaoRequest request) {
    aplicarDados(cadastro, request);
    aplicarEndereco(cadastro, request);
    cadastro.setAtualizadoEm(LocalDateTime.now());
  }

  public static CadastroProfissionalResponse toResponse(CadastroProfissional cadastro) {
    Endereco endereco = cadastro.getEndereco();
    return new CadastroProfissionalResponse(
        cadastro.getId(),
        cadastro.getNomeCompleto(),
        cadastro.getCpf(),
        cadastro.getDataNascimento(),
        cadastro.getFoto3x4(),
        cadastro.getSexoBiologico(),
        cadastro.getEstadoCivil(),
        cadastro.getNacionalidade(),
        cadastro.getNaturalidadeCidade(),
        cadastro.getNaturalidadeUf(),
        cadastro.getNomeMae(),
        cadastro.getVinculo(),
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
        cadastro.getCategoria(),
        cadastro.getRegistroConselho(),
        cadastro.getEspecialidade(),
        cadastro.getEmail(),
        cadastro.getTelefone(),
        cadastro.getUnidade(),
        cadastro.getSalaAtendimento(),
        cadastro.getCargaHoraria(),
        separarLista(cadastro.getDisponibilidade()),
        separarLista(cadastro.getCanaisAtendimento()),
        cadastro.getStatus(),
        separarLista(cadastro.getTags()),
        cadastro.getResumo(),
        cadastro.getObservacoes(),
        cadastro.getCriadoEm(),
        cadastro.getAtualizadoEm());
  }

  private static void aplicarDados(CadastroProfissional cadastro, CadastroProfissionalCriacaoRequest request) {
    cadastro.setNomeCompleto(limparTexto(request.getNomeCompleto()));
    cadastro.setCpf(limparTexto(request.getCpf()));
    cadastro.setDataNascimento(request.getDataNascimento());
    cadastro.setFoto3x4(request.getFoto3x4());
    cadastro.setSexoBiologico(limparTexto(request.getSexoBiologico()));
    cadastro.setEstadoCivil(limparTexto(request.getEstadoCivil()));
    cadastro.setNacionalidade(limparTexto(request.getNacionalidade()));
    cadastro.setNaturalidadeCidade(limparTexto(request.getNaturalidadeCidade()));
    cadastro.setNaturalidadeUf(limparTexto(request.getNaturalidadeUf()));
    cadastro.setNomeMae(limparTexto(request.getNomeMae()));
    cadastro.setVinculo(normalizarVinculo(request.getVinculo()));
    cadastro.setCategoria(limparTexto(request.getCategoria()));
    cadastro.setRegistroConselho(limparTexto(request.getRegistroConselho()));
    cadastro.setEspecialidade(limparTexto(request.getEspecialidade()));
    cadastro.setEmail(limparTexto(request.getEmail()));
    cadastro.setTelefone(limparTexto(request.getTelefone()));
    cadastro.setUnidade(limparTexto(request.getUnidade()));
    cadastro.setSalaAtendimento(limparTexto(request.getSalaAtendimento()));
    cadastro.setCargaHoraria(request.getCargaHoraria());
    cadastro.setDisponibilidade(juntarLista(request.getDisponibilidade()));
    cadastro.setCanaisAtendimento(juntarLista(request.getCanaisAtendimento()));
    cadastro.setStatus(limparTexto(request.getStatus()));
    cadastro.setTags(juntarLista(request.getTags()));
    cadastro.setResumo(limparTexto(request.getResumo()));
    cadastro.setObservacoes(limparTexto(request.getObservacoes()));
  }

  private static Endereco criarEndereco(CadastroProfissionalCriacaoRequest request, LocalDateTime agora) {
    if (!possuiDadosEndereco(request)) {
      return null;
    }
    Endereco endereco = new Endereco();
    endereco.setCep(request.getCep());
    endereco.setLogradouro(request.getLogradouro());
    endereco.setNumero(request.getNumero());
    endereco.setComplemento(request.getComplemento());
    endereco.setBairro(request.getBairro());
    endereco.setPontoReferencia(request.getPontoReferencia());
    endereco.setCidade(request.getMunicipio());
    endereco.setZona(request.getZona());
    endereco.setSubzona(request.getSubzona());
    endereco.setEstado(request.getUf());
    endereco.setCriadoEm(agora);
    endereco.setAtualizadoEm(agora);
    return endereco;
  }

  private static void aplicarEndereco(CadastroProfissional cadastro, CadastroProfissionalCriacaoRequest request) {
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

    endereco.setAtualizadoEm(agora);
    endereco.setCep(request.getCep());
    endereco.setLogradouro(request.getLogradouro());
    endereco.setNumero(request.getNumero());
    endereco.setComplemento(request.getComplemento());
    endereco.setBairro(request.getBairro());
    endereco.setPontoReferencia(request.getPontoReferencia());
    endereco.setCidade(request.getMunicipio());
    endereco.setZona(request.getZona());
    endereco.setSubzona(request.getSubzona());
    endereco.setEstado(request.getUf());
  }

  private static boolean possuiDadosEndereco(CadastroProfissionalCriacaoRequest request) {
    return temValor(request.getCep())
        || temValor(request.getLogradouro())
        || temValor(request.getNumero())
        || temValor(request.getComplemento())
        || temValor(request.getBairro())
        || temValor(request.getPontoReferencia())
        || temValor(request.getMunicipio())
        || temValor(request.getZona())
        || temValor(request.getSubzona())
        || temValor(request.getUf());
  }

  private static String limparTexto(String valor) {
    if (valor == null) return null;
    String limpo = valor.trim();
    return limpo.isEmpty() ? null : limpo;
  }

  private static String normalizarVinculo(String vinculo) {
    String limpo = limparTexto(vinculo);
    return limpo != null ? limpo : "VOLUNTARIO";
  }

  private static String juntarLista(List<String> valores) {
    if (valores == null || valores.isEmpty()) {
      return null;
    }
    return valores.stream().filter(Objects::nonNull).map(String::trim).filter(CadastroProfissionalMapper::temValor).collect(Collectors.joining(";"));
  }

  private static List<String> separarLista(String valor) {
    if (!temValor(valor)) {
      return null;
    }
    String[] partes = valor.split(";");
    List<String> lista = new ArrayList<>();
    for (String parte : partes) {
      if (temValor(parte)) {
        lista.add(parte.trim());
      }
    }
    return lista.isEmpty() ? null : lista;
  }

  private static boolean temValor(String valor) {
    return valor != null && !valor.trim().isEmpty();
  }
}
