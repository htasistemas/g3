package br.com.g3.cursosatendimentos.mapper;

import br.com.g3.cursosatendimentos.domain.CursoAtendimento;
import br.com.g3.cursosatendimentos.domain.CursoAtendimentoFilaEspera;
import br.com.g3.cursosatendimentos.domain.CursoAtendimentoMatricula;
import br.com.g3.cursosatendimentos.domain.CursoAtendimentoStatus;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoFilaEsperaRequest;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoFilaEsperaResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoMatriculaRequest;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoMatriculaResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoRequest;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoStatusResponse;
import br.com.g3.unidadeassistencial.domain.SalaUnidade;
import br.com.g3.unidadeassistencial.dto.SalaUnidadeResponse;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class CursosAtendimentosMapper {
  private static final String DIAS_SEMANA_DELIMITADOR = ";";
  private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

  private CursosAtendimentosMapper() {}

  public static CursoAtendimento toDomain(CursoAtendimentoRequest request, SalaUnidade sala) {
    CursoAtendimento curso = new CursoAtendimento();
    apply(curso, request, sala);
    return curso;
  }

  public static void apply(CursoAtendimento curso, CursoAtendimentoRequest request, SalaUnidade sala) {
    curso.setTipo(request.getTipo());
    curso.setNome(request.getNome());
    curso.setDescricao(request.getDescricao());
    curso.setImagem(request.getImagem());
    curso.setVagasTotais(request.getVagasTotais());
    curso.setCargaHoraria(request.getCargaHoraria());
    curso.setHorarioInicial(parseHorario(request.getHorarioInicial()));
    curso.setDuracaoHoras(request.getDuracaoHoras());
    curso.setDiasSemana(joinDiasSemana(request.getDiasSemana()));
    curso.setRestricoes(request.getRestricoes());
    curso.setProfissional(request.getProfissional());
    curso.setSala(sala);

    if (request.getStatus() != null) {
      curso.setStatus(request.getStatus());
    }

    if (request.getDataTriagem() != null) {
      curso.setDataTriagem(request.getDataTriagem());
    }

    if (request.getDataEncaminhamento() != null) {
      curso.setDataEncaminhamento(request.getDataEncaminhamento());
    }

    if (request.getDataConclusao() != null) {
      curso.setDataConclusao(request.getDataConclusao());
    }

    replaceMatriculas(curso, request.getMatriculas());
    replaceFilaEspera(curso, request.getFilaEspera());
  }

  public static CursoAtendimentoResponse toResponse(CursoAtendimento curso) {
    CursoAtendimentoResponse response = new CursoAtendimentoResponse();
    response.setId(curso.getId() == null ? null : curso.getId().toString());
    response.setTipo(curso.getTipo());
    response.setNome(curso.getNome());
    response.setDescricao(curso.getDescricao());
    response.setImagem(curso.getImagem());
    response.setVagasTotais(curso.getVagasTotais());
    response.setVagasDisponiveis(curso.getVagasDisponiveis());
    response.setCargaHoraria(curso.getCargaHoraria());
    response.setHorarioInicial(formatHorario(curso.getHorarioInicial()));
    response.setDuracaoHoras(curso.getDuracaoHoras());
    response.setDiasSemana(splitDiasSemana(curso.getDiasSemana()));
    response.setRestricoes(curso.getRestricoes());
    response.setProfissional(curso.getProfissional());
    response.setSalaId(curso.getSala() == null ? null : curso.getSala().getId().toString());
    response.setSala(toSalaResponse(curso.getSala()));
    response.setCriadoEm(curso.getCriadoEm());
    response.setAtualizadoEm(curso.getAtualizadoEm());
    response.setStatus(curso.getStatus());
    response.setHistoricoStatus(toHistoricoResponse(curso.getHistoricoStatus()));
    response.setDataTriagem(curso.getDataTriagem());
    response.setDataEncaminhamento(curso.getDataEncaminhamento());
    response.setDataConclusao(curso.getDataConclusao());
    response.setMatriculas(toMatriculasResponse(curso.getMatriculas()));
    response.setFilaEspera(toFilaEsperaResponse(curso.getFilaEspera()));
    return response;
  }

  private static SalaUnidadeResponse toSalaResponse(SalaUnidade sala) {
    if (sala == null) return null;
    Long unidadeId = sala.getUnidadeAssistencial() == null ? null : sala.getUnidadeAssistencial().getId();
    return new SalaUnidadeResponse(sala.getId(), unidadeId, sala.getNome());
  }

  private static List<CursoAtendimentoMatriculaResponse> toMatriculasResponse(
      List<CursoAtendimentoMatricula> matriculas) {
    if (matriculas == null) return Collections.emptyList();
    return matriculas.stream()
        .sorted(Comparator.comparing(CursoAtendimentoMatricula::getDataMatricula).reversed())
        .map(CursosAtendimentosMapper::toMatriculaResponse)
        .collect(Collectors.toList());
  }

  private static CursoAtendimentoMatriculaResponse toMatriculaResponse(
      CursoAtendimentoMatricula matricula) {
    CursoAtendimentoMatriculaResponse response = new CursoAtendimentoMatriculaResponse();
    response.setId(matricula.getId() == null ? null : matricula.getId().toString());
    response.setNomeBeneficiario(matricula.getBeneficiarioNome());
    response.setCpf(matricula.getCpf());
    response.setStatus(matricula.getStatus());
    response.setDataMatricula(matricula.getDataMatricula());
    return response;
  }

  private static List<CursoAtendimentoFilaEsperaResponse> toFilaEsperaResponse(
      List<CursoAtendimentoFilaEspera> filaEspera) {
    if (filaEspera == null) return Collections.emptyList();
    return filaEspera.stream()
        .sorted(Comparator.comparing(CursoAtendimentoFilaEspera::getDataEntrada).reversed())
        .map(CursosAtendimentosMapper::toFilaEsperaResponse)
        .collect(Collectors.toList());
  }

  private static CursoAtendimentoFilaEsperaResponse toFilaEsperaResponse(
      CursoAtendimentoFilaEspera filaEspera) {
    CursoAtendimentoFilaEsperaResponse response = new CursoAtendimentoFilaEsperaResponse();
    response.setId(filaEspera.getId() == null ? null : filaEspera.getId().toString());
    response.setNomeBeneficiario(filaEspera.getBeneficiarioNome());
    response.setCpf(filaEspera.getCpf());
    response.setDataEntrada(filaEspera.getDataEntrada());
    return response;
  }

  private static List<CursoAtendimentoStatusResponse> toHistoricoResponse(
      List<CursoAtendimentoStatus> historico) {
    if (historico == null) return Collections.emptyList();
    return historico.stream()
        .sorted(Comparator.comparing(CursoAtendimentoStatus::getDataAlteracao).reversed())
        .map(CursosAtendimentosMapper::toStatusResponse)
        .collect(Collectors.toList());
  }

  private static CursoAtendimentoStatusResponse toStatusResponse(CursoAtendimentoStatus status) {
    CursoAtendimentoStatusResponse response = new CursoAtendimentoStatusResponse();
    response.setStatus(status.getStatus());
    response.setDataAlteracao(status.getDataAlteracao());
    response.setJustificativa(status.getJustificativa());
    return response;
  }

  private static void replaceMatriculas(
      CursoAtendimento curso, List<CursoAtendimentoMatriculaRequest> matriculas) {
    curso.getMatriculas().clear();
    if (matriculas == null) return;
    for (CursoAtendimentoMatriculaRequest request : matriculas) {
      CursoAtendimentoMatricula matricula = new CursoAtendimentoMatricula();
      matricula.setCursoAtendimento(curso);
      matricula.setBeneficiarioNome(request.getNomeBeneficiario());
      matricula.setCpf(request.getCpf());
      matricula.setStatus(request.getStatus() == null ? "Ativo" : request.getStatus());
      matricula.setDataMatricula(
          request.getDataMatricula() == null ? LocalDateTime.now() : request.getDataMatricula());
      curso.getMatriculas().add(matricula);
    }
  }

  private static void replaceFilaEspera(
      CursoAtendimento curso, List<CursoAtendimentoFilaEsperaRequest> filaEspera) {
    curso.getFilaEspera().clear();
    if (filaEspera == null) return;
    for (CursoAtendimentoFilaEsperaRequest request : filaEspera) {
      CursoAtendimentoFilaEspera entrada = new CursoAtendimentoFilaEspera();
      entrada.setCursoAtendimento(curso);
      entrada.setBeneficiarioNome(request.getNomeBeneficiario());
      entrada.setCpf(request.getCpf());
      entrada.setDataEntrada(
          request.getDataEntrada() == null ? LocalDateTime.now() : request.getDataEntrada());
      curso.getFilaEspera().add(entrada);
    }
  }

  private static LocalTime parseHorario(String horario) {
    if (horario == null || horario.trim().isEmpty()) return null;
    return LocalTime.parse(horario);
  }

  private static String formatHorario(LocalTime horario) {
    if (horario == null) return null;
    return horario.format(FORMATO_HORA);
  }

  private static String joinDiasSemana(List<String> diasSemana) {
    if (diasSemana == null || diasSemana.isEmpty()) return null;
    return diasSemana.stream()
        .filter(Objects::nonNull)
        .map(String::trim)
        .filter((item) -> !item.trim().isEmpty())
        .collect(Collectors.joining(DIAS_SEMANA_DELIMITADOR));
  }

  private static List<String> splitDiasSemana(String diasSemana) {
    if (diasSemana == null || diasSemana.trim().isEmpty()) return Collections.emptyList();
    return Arrays.stream(diasSemana.split(DIAS_SEMANA_DELIMITADOR))
        .map(String::trim)
        .filter((item) -> !item.trim().isEmpty())
        .collect(Collectors.toList());
  }
}
