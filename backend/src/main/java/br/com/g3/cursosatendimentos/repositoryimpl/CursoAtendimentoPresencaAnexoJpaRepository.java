package br.com.g3.cursosatendimentos.repositoryimpl;

import br.com.g3.cursosatendimentos.domain.CursoAtendimentoPresencaAnexo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoAtendimentoPresencaAnexoJpaRepository
    extends JpaRepository<CursoAtendimentoPresencaAnexo, Long> {
  List<CursoAtendimentoPresencaAnexo> findAllByPresencaDataIdOrderByDataUploadDescIdDesc(Long presencaDataId);
}
