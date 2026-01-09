package br.com.g3.auditoria.serviceimpl;

import br.com.g3.auditoria.domain.AuditoriaEvento;
import br.com.g3.auditoria.dto.AuditoriaEventoResponse;
import br.com.g3.auditoria.repository.AuditoriaEventoRepository;
import br.com.g3.auditoria.service.AuditoriaService;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class AuditoriaServiceImpl implements AuditoriaService {
  private final AuditoriaEventoRepository repository;
  private final JdbcTemplate jdbcTemplate;

  public AuditoriaServiceImpl(AuditoriaEventoRepository repository, JdbcTemplate jdbcTemplate) {
    this.repository = repository;
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public AuditoriaEventoResponse registrarEvento(
      String acao, String entidade, String entidadeId, String dadosJson, Long usuarioId) {
    AuditoriaEvento evento = new AuditoriaEvento();
    evento.setId(UUID.randomUUID());
    evento.setAcao(acao);
    evento.setEntidade(entidade);
    evento.setEntidadeId(entidadeId);
    evento.setDadosJson(dadosJson);
    evento.setUsuarioId(usuarioId);
    evento.setCriadoEm(LocalDateTime.now());
    AuditoriaEvento salvo = repository.salvar(evento);
    return mapResponse(salvo);
  }

  @Override
  public AuditoriaEventoResponse buscarPorId(UUID id) {
    AuditoriaEvento evento =
        repository
            .buscarPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento nao encontrado."));
    return mapResponse(evento);
  }

  @Override
  public List<AuditoriaEventoResponse> listar(
      LocalDate dataInicio,
      LocalDate dataFim,
      String usuarioId,
      String entidade,
      String texto) {
    StringBuilder sql =
        new StringBuilder(
            "SELECT id, usuario_id, acao, entidade, entidade_id, dados_json, criado_em "
                + "FROM auditoria_evento WHERE 1=1 ");
    List<Object> params = new ArrayList<>();

    if (dataInicio != null) {
      sql.append(" AND criado_em >= ? ");
      params.add(Date.valueOf(dataInicio));
    }
    if (dataFim != null) {
      sql.append(" AND criado_em <= ? ");
      params.add(Date.valueOf(dataFim.plusDays(1)));
    }
    if (usuarioId != null && !usuarioId.trim().isEmpty()) {
      sql.append(" AND CAST(usuario_id AS TEXT) = ? ");
      params.add(usuarioId.trim());
    }
    if (entidade != null && !entidade.trim().isEmpty()) {
      sql.append(" AND LOWER(entidade) LIKE ? ");
      params.add("%" + entidade.trim().toLowerCase() + "%");
    }
    if (texto != null && !texto.trim().isEmpty()) {
      String termo = "%" + texto.trim().toLowerCase() + "%";
      sql.append(" AND (LOWER(acao) LIKE ? OR LOWER(entidade) LIKE ? OR LOWER(entidade_id) LIKE ?) ");
      params.add(termo);
      params.add(termo);
      params.add(termo);
    }

    sql.append(" ORDER BY criado_em DESC ");

    return jdbcTemplate.query(
        sql.toString(),
        (rs, rowNum) -> {
          AuditoriaEventoResponse response = new AuditoriaEventoResponse();
          response.setId(UUID.fromString(rs.getString("id")));
          Object usuario = rs.getObject("usuario_id");
          response.setUsuarioId(usuario == null ? null : ((Number) usuario).longValue());
          response.setAcao(rs.getString("acao"));
          response.setEntidade(rs.getString("entidade"));
          response.setEntidadeId(rs.getString("entidade_id"));
          response.setDadosJson(rs.getString("dados_json"));
          response.setCriadoEm(rs.getTimestamp("criado_em").toLocalDateTime());
          return response;
        },
        params.toArray());
  }

  private AuditoriaEventoResponse mapResponse(AuditoriaEvento evento) {
    AuditoriaEventoResponse response = new AuditoriaEventoResponse();
    response.setId(evento.getId());
    response.setUsuarioId(evento.getUsuarioId());
    response.setAcao(evento.getAcao());
    response.setEntidade(evento.getEntidade());
    response.setEntidadeId(evento.getEntidadeId());
    response.setDadosJson(evento.getDadosJson());
    response.setCriadoEm(evento.getCriadoEm());
    return response;
  }
}
