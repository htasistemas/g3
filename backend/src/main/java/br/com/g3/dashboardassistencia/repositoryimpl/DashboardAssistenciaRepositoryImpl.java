package br.com.g3.dashboardassistencia.repositoryimpl;

import br.com.g3.dashboardassistencia.repository.DashboardAssistenciaRepository;
import java.sql.Date;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DashboardAssistenciaRepositoryImpl implements DashboardAssistenciaRepository {
  private final JdbcTemplate jdbcTemplate;

  public DashboardAssistenciaRepositoryImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public long contarBeneficiarios() {
    Long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM cadastro_beneficiario", Long.class);
    return total != null ? total : 0L;
  }

  @Override
  public Map<String, Long> contarBeneficiariosPorStatus() {
    String sql =
        "SELECT COALESCE(status, 'EM_ANALISE') AS status, COUNT(*) AS total " +
        "FROM cadastro_beneficiario GROUP BY COALESCE(status, 'EM_ANALISE')";
    return jdbcTemplate.query(
        sql,
        rs -> {
          Map<String, Long> resultado = new LinkedHashMap<>();
          while (rs.next()) {
            resultado.put(rs.getString("status"), rs.getLong("total"));
          }
          return resultado;
        });
  }

  @Override
  public long contarBeneficiariosPeriodo(LocalDate inicio, LocalDate fim) {
    String baseSql = "SELECT COUNT(*) FROM cadastro_beneficiario";
    if (inicio == null && fim == null) {
      Long total = jdbcTemplate.queryForObject(baseSql, Long.class);
      return total != null ? total : 0L;
    }

    if (inicio != null && fim == null) {
      Long total =
          jdbcTemplate.queryForObject(
              baseSql + " WHERE cast(criado_em as date) >= ?",
              Long.class,
              Date.valueOf(inicio));
      return total != null ? total : 0L;
    }

    if (inicio == null) {
      Long total =
          jdbcTemplate.queryForObject(
              baseSql + " WHERE cast(criado_em as date) <= ?",
              Long.class,
              Date.valueOf(fim));
      return total != null ? total : 0L;
    }

    Long total =
        jdbcTemplate.queryForObject(
            baseSql + " WHERE cast(criado_em as date) >= ? AND cast(criado_em as date) <= ?",
            Long.class,
            Date.valueOf(inicio),
            Date.valueOf(fim));
    return total != null ? total : 0L;
  }

  @Override
  public long contarCadastroCompleto() {
    String sql =
        "SELECT COUNT(*) FROM cadastro_beneficiario b " +
        "WHERE b.endereco_id IS NOT NULL " +
        "AND EXISTS (SELECT 1 FROM contato_beneficiario c WHERE c.beneficiario_id = b.id)";
    Long total = jdbcTemplate.queryForObject(sql, Long.class);
    return total != null ? total : 0L;
  }

  @Override
  public List<LocalDate> listarDatasNascimento() {
    String sql = "SELECT data_nascimento FROM cadastro_beneficiario WHERE data_nascimento IS NOT NULL";
    return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getDate("data_nascimento").toLocalDate());
  }

  @Override
  public long contarSituacaoSocialTotal() {
    String sql = "SELECT COUNT(DISTINCT beneficiario_id) FROM situacao_social";
    Long total = jdbcTemplate.queryForObject(sql, Long.class);
    return total != null ? total : 0L;
  }

  @Override
  public Double calcularMediaPessoas() {
    String sql =
        "SELECT AVG(COALESCE(criancas_adolescentes, 0) + COALESCE(idosos, 0)) " +
        "FROM situacao_social";
    Double media = jdbcTemplate.queryForObject(sql, Double.class);
    return media;
  }

  @Override
  public List<String> listarRendasFamiliares() {
    String sql =
        "SELECT renda_mensal FROM escolaridade_beneficiario " +
        "WHERE renda_mensal IS NOT NULL AND TRIM(renda_mensal) <> ''";
    return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("renda_mensal"));
  }

  @Override
  public Map<String, Long> contarVulnerabilidades() {
    Map<String, Long> resultado = new LinkedHashMap<>();
    resultado.put("Acompanhamento CRAS", contarFlag("acompanhamento_cras"));
    resultado.put("Acompanhamento Saude", contarFlag("acompanhamento_saude"));
    resultado.put("Responsavel legal", contarFlag("responsavel_legal"));
    resultado.put("Mora com familia", contarFlag("mora_com_familia"));
    return resultado;
  }

  @Override
  public long contarProfissionais() {
    Long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM cadastro_profissionais", Long.class);
    return total != null ? total : 0L;
  }

  @Override
  public long contarVoluntarios() {
    Long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM cadastro_voluntario", Long.class);
    return total != null ? total : 0L;
  }

  @Override
  public long contarFamilias() {
    Long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM vinculo_familiar", Long.class);
    return total != null ? total : 0L;
  }

  @Override
  public long contarBensPatrimonio() {
    Long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM patrimonio_item", Long.class);
    return total != null ? total : 0L;
  }

  @Override
  public double somarValoresAReceber() {
    String sql =
        "SELECT COALESCE(SUM(valor), 0) FROM lancamento_financeiro " +
        "WHERE tipo = 'receber' AND situacao <> 'pago'";
    BigDecimal total = jdbcTemplate.queryForObject(sql, BigDecimal.class);
    return total != null ? total.doubleValue() : 0.0;
  }

  @Override
  public double somarValoresEmCaixa() {
    String sql = "SELECT COALESCE(SUM(saldo), 0) FROM conta_bancaria WHERE tipo = 'corrente'";
    BigDecimal total = jdbcTemplate.queryForObject(sql, BigDecimal.class);
    return total != null ? total.doubleValue() : 0.0;
  }

  @Override
  public double somarValoresEmBanco() {
    String sql = "SELECT COALESCE(SUM(saldo), 0) FROM conta_bancaria WHERE tipo <> 'corrente'";
    BigDecimal total = jdbcTemplate.queryForObject(sql, BigDecimal.class);
    return total != null ? total.doubleValue() : 0.0;
  }

  private long contarFlag(String coluna) {
    String sql = "SELECT COUNT(*) FROM situacao_social WHERE " + coluna + " = TRUE";
    Long total = jdbcTemplate.queryForObject(sql, Long.class);
    return total != null ? total : 0L;
  }
}
