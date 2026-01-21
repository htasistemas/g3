package br.com.g3.chamadas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/ws/**", "/api/**").permitAll()
            .anyRequest().permitAll())
        .headers(headers -> headers.frameOptions(frame -> frame.disable()));
    return http.build();
  }
}
