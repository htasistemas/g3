package br.com.g3.cadastroprofissionais.mapper;

import br.com.g3.cadastroprofissionais.domain.CadastroProfissional;
import br.com.g3.cadastroprofissionais.dto.CadastroProfissionalCriacaoRequest;
import br.com.g3.cadastroprofissionais.dto.CadastroProfissionalResponse;
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
    cadastro.setCriadoEm(agora);
    cadastro.setAtualizadoEm(agora);
    return cadastro;
  }

  public static void aplicarAtualizacao(CadastroProfissional cadastro, CadastroProfissionalCriacaoRequest request) {
    aplicarDados(cadastro, request);
    cadastro.setAtualizadoEm(LocalDateTime.now());
  }

  public static CadastroProfissionalResponse toResponse(CadastroProfissional cadastro) {
    return new CadastroProfissionalResponse(
        cadastro.getId(),
        cadastro.getNomeCompleto(),
        cadastro.getCategoria(),
        cadastro.getRegistroConselho(),
        cadastro.getEspecialidade(),
        cadastro.getEmail(),
        cadastro.getTelefone(),
        cadastro.getUnidade(),
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
    cadastro.setCategoria(limparTexto(request.getCategoria()));
    cadastro.setRegistroConselho(limparTexto(request.getRegistroConselho()));
    cadastro.setEspecialidade(limparTexto(request.getEspecialidade()));
    cadastro.setEmail(limparTexto(request.getEmail()));
    cadastro.setTelefone(limparTexto(request.getTelefone()));
    cadastro.setUnidade(limparTexto(request.getUnidade()));
    cadastro.setCargaHoraria(request.getCargaHoraria());
    cadastro.setDisponibilidade(juntarLista(request.getDisponibilidade()));
    cadastro.setCanaisAtendimento(juntarLista(request.getCanaisAtendimento()));
    cadastro.setStatus(limparTexto(request.getStatus()));
    cadastro.setTags(juntarLista(request.getTags()));
    cadastro.setResumo(limparTexto(request.getResumo()));
    cadastro.setObservacoes(limparTexto(request.getObservacoes()));
  }

  private static String limparTexto(String valor) {
    if (valor == null) return null;
    String limpo = valor.trim();
    return limpo.isEmpty() ? null : limpo;
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
