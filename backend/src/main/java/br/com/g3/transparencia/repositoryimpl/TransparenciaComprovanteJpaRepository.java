package br.com.g3.transparencia.repositoryimpl;

import br.com.g3.transparencia.domain.TransparenciaComprovante;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface TransparenciaComprovanteJpaRepository
    extends JpaRepository<TransparenciaComprovante, Long> {
  List<TransparenciaComprovante> findAllByTransparenciaId(Long transparenciaId);

  @Modifying
  @Transactional
  void deleteByTransparenciaId(Long transparenciaId);
}
