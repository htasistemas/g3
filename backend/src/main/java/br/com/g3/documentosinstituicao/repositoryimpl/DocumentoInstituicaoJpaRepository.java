package br.com.g3.documentosinstituicao.repositoryimpl;

import br.com.g3.documentosinstituicao.domain.DocumentoInstituicao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentoInstituicaoJpaRepository extends JpaRepository<DocumentoInstituicao, Long> {
  List<DocumentoInstituicao> findAllByOrderByAtualizadoEmDescIdDesc();
}
