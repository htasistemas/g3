package br.com.g3.emprestimoseventos.repositoryimpl;

import br.com.g3.emprestimoseventos.domain.EmprestimoEvento;
import br.com.g3.emprestimoseventos.repository.EmprestimoEventoRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class EmprestimoEventoRepositoryImpl implements EmprestimoEventoRepository {
  private final EmprestimoEventoJpaRepository jpaRepository;
  private final JdbcTemplate jdbcTemplate;

  public EmprestimoEventoRepositoryImpl(
      EmprestimoEventoJpaRepository jpaRepository, JdbcTemplate jdbcTemplate) {
    this.jpaRepository = jpaRepository;
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public EmprestimoEvento salvar(EmprestimoEvento emprestimo) {
    return jpaRepository.save(emprestimo);
  }

  @Override
  public Optional<EmprestimoEvento> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public List<EmprestimoEvento> listarPorFiltros(
      LocalDateTime inicio,
      LocalDateTime fim,
      String status,
      Long eventoId,
      Long itemId,
      Long unidadeId) {
    StringBuilder consultaSql = new StringBuilder();
    List<Object> parametros = new ArrayList<>();
    consultaSql.append("SELECT DISTINCT e.id, e.data_retirada_prevista ");
    consultaSql.append("FROM emprestimos_eventos e ");
    consultaSql.append("LEFT JOIN emprestimos_eventos_itens i ON i.emprestimo_id = e.id ");
    consultaSql.append("WHERE 1=1 ");

    if (inicio != null) {
      consultaSql.append("AND e.data_retirada_prevista >= ? ");
      parametros.add(Timestamp.valueOf(inicio));
    }
    if (fim != null) {
      consultaSql.append("AND e.data_devolucao_prevista <= ? ");
      parametros.add(Timestamp.valueOf(fim));
    }
    if (status != null && !status.trim().isEmpty()) {
      consultaSql.append("AND e.status = ? ");
      parametros.add(status);
    }
    if (eventoId != null) {
      consultaSql.append("AND e.evento_id = ? ");
      parametros.add(eventoId);
    }
    if (itemId != null) {
      consultaSql.append("AND i.item_id = ? ");
      parametros.add(itemId);
    }
    if (unidadeId != null) {
      consultaSql.append("AND e.unidade_id = ? ");
      parametros.add(unidadeId);
    }

    consultaSql.append("ORDER BY e.data_retirada_prevista DESC");

    List<Long> ids =
        jdbcTemplate.query(consultaSql.toString(),
            (rs, rowNum) -> rs.getLong("id"),
            parametros.toArray());
    if (ids.isEmpty()) {
      return new ArrayList<>();
    }
    return jpaRepository.findAllById(ids);
  }

  @Override
  public List<EmprestimoEventoAgendaRegistro> listarAgendaRegistros(
      LocalDateTime inicio, LocalDateTime fim) {
    String consultaSql =
        "SELECT e.id, e.data_retirada_prevista, e.data_devolucao_prevista, e.status "
            + "FROM emprestimos_eventos e "
            + "WHERE e.status IN ('AGENDADO', 'RETIRADO') "
            + "AND e.data_retirada_prevista < ? AND e.data_devolucao_prevista > ?";

    return jdbcTemplate.query(
        consultaSql,
        new Object[] { Timestamp.valueOf(fim), Timestamp.valueOf(inicio) },
        new AgendaRegistroRowMapper());
  }

  @Override
  public List<EmprestimoEventoAgendaRow> listarAgendaDia(
      LocalDateTime inicioDia, LocalDateTime fimDia) {
    String consultaSql =
        "SELECT e.id AS emprestimo_id, e.status, e.data_retirada_prevista, e.data_devolucao_prevista, "
            + "e.data_retirada_real, e.data_devolucao_real, "
            + "u.id AS responsavel_id, COALESCE(u.nome, u.nome_usuario) AS responsavel_nome, "
            + "ev.id AS evento_id, ev.titulo AS evento_titulo, ev.local AS evento_local, "
            + "ev.data_inicio AS evento_inicio, ev.data_fim AS evento_fim, "
            + "i.item_id, i.tipo_item, i.quantidade, i.status_item, "
            + "p.nome AS patrimonio_nome, p.numero_patrimonio, "
            + "a.descricao AS almox_nome "
            + "FROM emprestimos_eventos e "
            + "JOIN eventos_emprestimos ev ON ev.id = e.evento_id "
            + "LEFT JOIN usuarios u ON u.id = e.responsavel_id "
            + "LEFT JOIN emprestimos_eventos_itens i ON i.emprestimo_id = e.id "
            + "LEFT JOIN patrimonio_item p ON p.id = i.item_id AND i.tipo_item = 'PATRIMONIO' "
            + "LEFT JOIN almoxarifado_item a ON a.id = i.item_id AND i.tipo_item = 'ALMOXARIFADO' "
            + "WHERE e.status IN ('AGENDADO', 'RETIRADO') "
            + "AND e.data_retirada_prevista < ? AND e.data_devolucao_prevista > ? "
            + "ORDER BY e.id";

    return jdbcTemplate.query(
        consultaSql,
        new Object[] { Timestamp.valueOf(fimDia), Timestamp.valueOf(inicioDia) },
        new AgendaDiaRowMapper());
  }

  @Override
  public List<EmprestimoEventoDisponibilidadeResumo> buscarConflitosItem(       
      Long itemId,
      String tipoItem,
      LocalDateTime inicio,
      LocalDateTime fim,
      Long emprestimoIgnorarId) {
    StringBuilder consultaSql = new StringBuilder();
    List<Object> parametros = new ArrayList<>();
    consultaSql.append("SELECT e.id AS emprestimo_id, ev.titulo AS evento_titulo, ");
    consultaSql.append("e.data_retirada_prevista, e.data_devolucao_prevista, e.status, i.quantidade ");
    consultaSql.append("FROM emprestimos_eventos_itens i ");
    consultaSql.append("JOIN emprestimos_eventos e ON e.id = i.emprestimo_id ");
    consultaSql.append("JOIN eventos_emprestimos ev ON ev.id = e.evento_id ");
    consultaSql.append("WHERE i.item_id = ? AND i.tipo_item = ? ");
    consultaSql.append("AND e.status IN ('AGENDADO', 'RETIRADO') ");
    consultaSql.append("AND e.data_retirada_prevista < ? AND e.data_devolucao_prevista > ? ");

    parametros.add(itemId);
    parametros.add(tipoItem);
    parametros.add(Timestamp.valueOf(fim));
    parametros.add(Timestamp.valueOf(inicio));

    if (emprestimoIgnorarId != null) {
      consultaSql.append("AND e.id <> ? ");
      parametros.add(emprestimoIgnorarId);
    }

    return jdbcTemplate.query(
        consultaSql.toString(),
        parametros.toArray(),
        new DisponibilidadeRowMapper());
  }

  @Override
  public Integer obterEstoqueAtual(Long itemId) {
    String consultaSql = "SELECT estoque_atual FROM almoxarifado_item WHERE id = ?";
    List<Integer> resultado =
        jdbcTemplate.query(consultaSql, (rs, rowNum) -> rs.getInt(1), itemId);
    return resultado.isEmpty() ? null : resultado.get(0);
  }

  private static class AgendaRegistroRowMapper
      implements RowMapper<EmprestimoEventoAgendaRegistro> {
    @Override
    public EmprestimoEventoAgendaRegistro mapRow(ResultSet rs, int rowNum) throws SQLException {
      EmprestimoEventoAgendaRegistro registro = new EmprestimoEventoAgendaRegistro();
      registro.setEmprestimoId(rs.getLong("id"));
      registro.setRetiradaPrevista(rs.getTimestamp("data_retirada_prevista").toLocalDateTime());
      registro.setDevolucaoPrevista(rs.getTimestamp("data_devolucao_prevista").toLocalDateTime());
      registro.setStatus(rs.getString("status"));
      return registro;
    }
  }

  private static class AgendaDiaRowMapper implements RowMapper<EmprestimoEventoAgendaRow> {
    @Override
    public EmprestimoEventoAgendaRow mapRow(ResultSet rs, int rowNum) throws SQLException {
      EmprestimoEventoAgendaRow linha = new EmprestimoEventoAgendaRow();
      linha.setEmprestimoId(rs.getLong("emprestimo_id"));
      linha.setStatus(rs.getString("status"));
      linha.setRetiradaPrevista(rs.getTimestamp("data_retirada_prevista").toLocalDateTime());
      linha.setDevolucaoPrevista(rs.getTimestamp("data_devolucao_prevista").toLocalDateTime());
      linha.setRetiradaReal(toLocalDateTime(rs, "data_retirada_real"));
      linha.setDevolucaoReal(toLocalDateTime(rs, "data_devolucao_real"));
      linha.setResponsavelId((Long) rs.getObject("responsavel_id"));
      linha.setResponsavelNome(rs.getString("responsavel_nome"));
      linha.setEventoId(rs.getLong("evento_id"));
      linha.setEventoTitulo(rs.getString("evento_titulo"));
      linha.setEventoLocal(rs.getString("evento_local"));
      linha.setEventoInicio(rs.getTimestamp("evento_inicio").toLocalDateTime());
      linha.setEventoFim(rs.getTimestamp("evento_fim").toLocalDateTime());
      linha.setItemId((Long) rs.getObject("item_id"));
      linha.setTipoItem(rs.getString("tipo_item"));
      linha.setQuantidade((Integer) rs.getObject("quantidade"));
      linha.setStatusItem(rs.getString("status_item"));
      String nomeItem = rs.getString("patrimonio_nome");
      if (nomeItem == null || nomeItem.trim().isEmpty()) {
        nomeItem = rs.getString("almox_nome");
      }
      linha.setNomeItem(nomeItem);
      linha.setNumeroPatrimonio(rs.getString("numero_patrimonio"));
      return linha;
    }

    private LocalDateTime toLocalDateTime(ResultSet rs, String coluna) throws SQLException {
      Timestamp ts = rs.getTimestamp(coluna);
      return ts != null ? ts.toLocalDateTime() : null;
    }
  }

  private static class DisponibilidadeRowMapper
      implements RowMapper<EmprestimoEventoDisponibilidadeResumo> {
    @Override
    public EmprestimoEventoDisponibilidadeResumo mapRow(ResultSet rs, int rowNum) throws SQLException {
      EmprestimoEventoDisponibilidadeResumo resumo = new EmprestimoEventoDisponibilidadeResumo();
      resumo.setEmprestimoId(rs.getLong("emprestimo_id"));
      resumo.setEventoTitulo(rs.getString("evento_titulo"));
      resumo.setInicio(rs.getTimestamp("data_retirada_prevista").toLocalDateTime());
      resumo.setFim(rs.getTimestamp("data_devolucao_prevista").toLocalDateTime());
      resumo.setStatus(rs.getString("status"));
      resumo.setQuantidade((Integer) rs.getObject("quantidade"));
      return resumo;
    }
  }
}

