package br.com.g3.rhcontratacao.repositoryimpl;

import br.com.g3.rhcontratacao.domain.RhProcessoContratacao;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RhProcessoContratacaoJpaRepository extends JpaRepository<RhProcessoContratacao, Long> {
  Optional<RhProcessoContratacao> findByCandidatoId(Long candidatoId);
  List<RhProcessoContratacao> findAllByOrderByAtualizadoEmDesc();
}
