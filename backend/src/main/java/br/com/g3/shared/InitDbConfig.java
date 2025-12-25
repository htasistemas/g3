package br.com.g3.shared;

import java.sql.Connection;
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
        ScriptUtils.executeSqlScript(connection, script);
      }
    };
  }
}
