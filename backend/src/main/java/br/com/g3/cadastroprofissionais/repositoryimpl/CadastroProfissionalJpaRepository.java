package br.com.g3.cadastroprofissionais.repositoryimpl;

import br.com.g3.cadastroprofissionais.domain.CadastroProfissional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CadastroProfissionalJpaRepository extends JpaRepository<CadastroProfissional, Long> {
  List<CadastroProfissional> findByNomeCompletoContainingIgnoreCase(String nomeCompleto);
}
