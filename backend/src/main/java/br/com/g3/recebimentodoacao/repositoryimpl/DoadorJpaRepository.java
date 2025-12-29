package br.com.g3.recebimentodoacao.repositoryimpl;

import br.com.g3.recebimentodoacao.domain.Doador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoadorJpaRepository extends JpaRepository<Doador, Long> {}
