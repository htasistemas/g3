package br.com.g3.shared;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import org.springframework.core.io.ByteArrayResource;
import javax.sql.DataSource;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

@Configuration
public class InitDbConfig {
  @Bean
  ApplicationRunner executarInitDb(DataSource dataSource) {
    return args -> {
      ClassPathResource script = new ClassPathResource("init.db");
      if (!script.exists()) {
        return;
      }
      try (Connection connection = dataSource.getConnection()) {
        if (ehPostgreSql(connection)) {
          garantirTiposChamado(connection);
          String conteudoScript =
              new String(script.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
          String scriptSemTipos = removerDeclaracoesTiposChamado(conteudoScript);
          ScriptUtils.executeSqlScript(connection, new ByteArrayResource(scriptSemTipos.getBytes(StandardCharsets.UTF_8)));
          return;
        }
        ScriptUtils.executeSqlScript(connection, script);
      }
    };
  }

  private boolean ehPostgreSql(Connection connection) throws SQLException {
    String nomeBanco = connection.getMetaData().getDatabaseProductName();
    return nomeBanco != null && nomeBanco.toLowerCase(Locale.ROOT).contains("postgresql");
  }

  private void garantirTiposChamado(Connection connection) throws SQLException {
    criarTipoSeNecessario(connection, "chamado_tipo", "('ERRO', 'MELHORIA', 'CORRECAO', 'NOVA_IMPLEMENTACAO')");
    criarTipoSeNecessario(
        connection,
        "chamado_status",
        "('ABERTO', 'EM_ANALISE', 'EM_DESENVOLVIMENTO', 'EM_TESTE', 'AGUARDANDO_CLIENTE', 'RESOLVIDO', 'FECHADO', 'REABERTO', 'CANCELADO')");
    criarTipoSeNecessario(connection, "chamado_prioridade", "('BAIXA', 'MEDIA', 'ALTA', 'CRITICA')");
    criarTipoSeNecessario(connection, "chamado_impacto", "('BAIXO', 'MEDIO', 'ALTO')");
    criarTipoSeNecessario(
        connection,
        "chamado_acao_tipo",
        "('CRIACAO', 'COMENTARIO', 'MUDANCA_STATUS', 'ATRIBUICAO', 'ANEXO', 'EDICAO', 'VINCULO', 'REGISTRO_ATIVIDADE')");
    adicionarValorEnumSeNecessario(connection, "chamado_tipo", "CORRECAO");
    adicionarValorEnumSeNecessario(connection, "chamado_tipo", "NOVA_IMPLEMENTACAO");
    adicionarValorEnumSeNecessario(connection, "chamado_status", "REABERTO");
    adicionarValorEnumSeNecessario(connection, "chamado_status", "FECHADO");
  }

  private void criarTipoSeNecessario(Connection connection, String nomeTipo, String valoresEnum)
      throws SQLException {
    if (tipoExiste(connection, nomeTipo)) {
      return;
    }
    String comando = String.format("CREATE TYPE %s AS ENUM %s", nomeTipo, valoresEnum);
    try (PreparedStatement statement = connection.prepareStatement(comando)) {
      statement.execute();
    }
  }

  private boolean tipoExiste(Connection connection, String nomeTipo) throws SQLException {
    String consulta =
        "SELECT 1 FROM pg_type t JOIN pg_namespace n ON n.oid = t.typnamespace WHERE t.typname = ?";
    try (PreparedStatement statement = connection.prepareStatement(consulta)) {
      statement.setString(1, nomeTipo);
      try (ResultSet resultado = statement.executeQuery()) {
        return resultado.next();
      }
    }
  }

  private void adicionarValorEnumSeNecessario(Connection connection, String nomeTipo, String valorEnum)
      throws SQLException {
    if (!tipoExiste(connection, nomeTipo)) {
      return;
    }
    if (valorEnumExiste(connection, nomeTipo, valorEnum)) {
      return;
    }
    String comando = String.format("ALTER TYPE %s ADD VALUE IF NOT EXISTS '%s'", nomeTipo, valorEnum);
    try (PreparedStatement statement = connection.prepareStatement(comando)) {
      statement.execute();
    }
  }

  private boolean valorEnumExiste(Connection connection, String nomeTipo, String valorEnum)
      throws SQLException {
    String consulta =
        "SELECT 1 FROM pg_enum e JOIN pg_type t ON t.oid = e.enumtypid WHERE t.typname = ? AND e.enumlabel = ?";
    try (PreparedStatement statement = connection.prepareStatement(consulta)) {
      statement.setString(1, nomeTipo);
      statement.setString(2, valorEnum);
      try (ResultSet resultado = statement.executeQuery()) {
        return resultado.next();
      }
    }
  }

  private String removerDeclaracoesTiposChamado(String script) {
    String resultado = script;
    resultado =
        resultado.replaceAll(
            "(?is)CREATE\\s+TYPE\\s+IF\\s+NOT\\s+EXISTS\\s+chamado_tipo\\s+AS\\s+ENUM\\s*\\(.*?\\);",
            "");
    resultado =
        resultado.replaceAll(
            "(?is)CREATE\\s+TYPE\\s+IF\\s+NOT\\s+EXISTS\\s+chamado_status\\s+AS\\s+ENUM\\s*\\(.*?\\);",
            "");
    resultado =
        resultado.replaceAll(
            "(?is)CREATE\\s+TYPE\\s+IF\\s+NOT\\s+EXISTS\\s+chamado_prioridade\\s+AS\\s+ENUM\\s*\\(.*?\\);",
            "");
    resultado =
        resultado.replaceAll(
            "(?is)CREATE\\s+TYPE\\s+IF\\s+NOT\\s+EXISTS\\s+chamado_impacto\\s+AS\\s+ENUM\\s*\\(.*?\\);",
            "");
    resultado =
        resultado.replaceAll(
            "(?is)CREATE\\s+TYPE\\s+IF\\s+NOT\\s+EXISTS\\s+chamado_acao_tipo\\s+AS\\s+ENUM\\s*\\(.*?\\);",
            "");
    return resultado;
  }
}
