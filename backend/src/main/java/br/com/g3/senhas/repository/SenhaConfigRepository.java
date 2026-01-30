package br.com.g3.senhas.repository;

import br.com.g3.senhas.domain.SenhaConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SenhaConfigRepository extends JpaRepository<SenhaConfig, Long> {}
