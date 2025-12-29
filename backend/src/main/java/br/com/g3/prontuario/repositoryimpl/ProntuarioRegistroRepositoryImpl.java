package br.com.g3.prontuario.repositoryimpl;

import br.com.g3.prontuario.domain.ProntuarioRegistro;
import br.com.g3.prontuario.dto.ProntuarioRegistroResponse;
import br.com.g3.prontuario.repository.ProntuarioRegistroConsultaResultado;
import br.com.g3.prontuario.repository.ProntuarioRegistroRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ProntuarioRegistroRepositoryImpl implements ProntuarioRegistroRepository {
  private final ProntuarioRegistroJpaRepository jpaRepository;
  private final JdbcTemplate jdbcTemplate;
  private final ObjectMapper objectMapper;

  public ProntuarioRegistroRepositoryImpl(
      ProntuarioRegistroJpaRepository jpaRepository, JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
    this.jpaRepository = jpaRepository;
    this.jdbcTemplate = jdbcTemplate;
    this.objectMapper = objectMapper;
  }

  @Override
  public ProntuarioRegistro salvar(ProntuarioRegistro registro) {
    return jpaRepository.save(registro);
  }

  @Override
  public Optional<ProntuarioRegistro> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public void remover(ProntuarioRegistro registro) {
    jpaRepository.delete(registro);
  }

  @Override
  public ProntuarioRegistroConsultaResultado listarRegistros(
      Long beneficiarioId,
      String tipo,
      LocalDateTime dataInicio,
      LocalDateTime dataFim,
      Long profissionalId,
      Long unidadeId,
      String status,
      String texto,
      int pagina,
      int tamanhoPagina) {
    StringBuilder sql =
        new StringBuilder(
            "SELECT id, beneficiario_id, tipo, data_registro, profissional_id, unidade_id, titulo, descricao, dados_extra, status, criado_em, criado_por, atualizado_em, atualizado_por "
                + "FROM prontuario_registros WHERE beneficiario_id = ?");
    List<Object> params = new ArrayList<>();
    params.add(beneficiarioId);

    if (tipo != null && !tipo.trim().isEmpty()) {
      sql.append(" AND tipo = ?");
      params.add(tipo);
    }
    if (status != null && !status.trim().isEmpty()) {
      sql.append(" AND status = ?");
      params.add(status);
    }
    if (profissionalId != null) {
      sql.append(" AND profissional_id = ?");
      params.add(profissionalId);
    }
    if (unidadeId != null) {
      sql.append(" AND unidade_id = ?");
      params.add(unidadeId);
    }
    if (dataInicio != null) {
      sql.append(" AND data_registro >= ?");
      params.add(Timestamp.valueOf(dataInicio));
    }
    if (dataFim != null) {
      sql.append(" AND data_registro <= ?");
      params.add(Timestamp.valueOf(dataFim));
    }
    if (texto != null && !texto.trim().isEmpty()) {
      sql.append(" AND (LOWER(descricao) LIKE ? OR LOWER(titulo) LIKE ?)");
      String search = "%" + texto.toLowerCase() + "%";
      params.add(search);
      params.add(search);
    }

    String countSql = "SELECT COUNT(*) FROM (" + sql + ") AS total_registros";
    long total =
        jdbcTemplate.queryForObject(countSql, params.toArray(), (rs, rowNum) -> rs.getLong(1));

    sql.append(" ORDER BY data_registro DESC LIMIT ? OFFSET ?");
    params.add(tamanhoPagina);
    params.add(pagina * tamanhoPagina);

    List<ProntuarioRegistroResponse> registros =
        jdbcTemplate.query(sql.toString(), params.toArray(), new ProntuarioRegistroRowMapper(objectMapper));

    return new ProntuarioRegistroConsultaResultado(registros, total);
  }

  @Override
  public Map<String, Long> contarPorTipo(Long beneficiarioId) {
    String sql =
        "SELECT tipo, COUNT(*) AS total FROM prontuario_registros WHERE beneficiario_id = ? GROUP BY tipo";
    List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, beneficiarioId);
    Map<String, Long> resultado = new HashMap<>();
    for (Map<String, Object> row : rows) {
      resultado.put(String.valueOf(row.get("tipo")), ((Number) row.get("total")).longValue());
    }
    return resultado;
  }

  @Override
  public LocalDateTime buscarUltimaAtualizacao(Long beneficiarioId) {
    String sql =
        "SELECT MAX(atualizado_em) FROM prontuario_registros WHERE beneficiario_id = ?";
    return jdbcTemplate.queryForObject(sql, new Object[] {beneficiarioId}, (rs, rowNum) -> {
      Timestamp timestamp = rs.getTimestamp(1);
      return timestamp != null ? timestamp.toLocalDateTime() : null;
    });
  }

  private static class ProntuarioRegistroRowMapper implements RowMapper<ProntuarioRegistroResponse> {
    private final ObjectMapper objectMapper;

    private ProntuarioRegistroRowMapper(ObjectMapper objectMapper) {
      this.objectMapper = objectMapper;
    }

    @Override
    public ProntuarioRegistroResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
      ProntuarioRegistroResponse response = new ProntuarioRegistroResponse();
      response.setId(rs.getLong("id"));
      response.setBeneficiarioId(rs.getLong("beneficiario_id"));
      response.setTipo(rs.getString("tipo"));
      response.setDataRegistro(rs.getTimestamp("data_registro").toLocalDateTime());
      response.setProfissionalId((Long) rs.getObject("profissional_id"));
      response.setUnidadeId((Long) rs.getObject("unidade_id"));
      response.setTitulo(rs.getString("titulo"));
      response.setDescricao(rs.getString("descricao"));
      String dadosExtra = rs.getString("dados_extra");
      if (dadosExtra != null && !dadosExtra.trim().isEmpty()) {
        try {
          Map<String, Object> map =
              objectMapper.readValue(dadosExtra, new TypeReference<Map<String, Object>>() {});
          response.setDadosExtra(map);
        } catch (Exception ex) {
          response.setDadosExtra(Collections.emptyMap());
        }
      } else {
        response.setDadosExtra(Collections.emptyMap());
      }
      response.setStatus(rs.getString("status"));
      response.setCriadoEm(rs.getTimestamp("criado_em").toLocalDateTime());
      response.setCriadoPor((Long) rs.getObject("criado_por"));
      response.setAtualizadoEm(rs.getTimestamp("atualizado_em").toLocalDateTime());
      response.setAtualizadoPor((Long) rs.getObject("atualizado_por"));
      return response;
    }
  }
}
