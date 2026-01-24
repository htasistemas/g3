package br.com.g3.biblioteca.repositoryimpl;

import br.com.g3.biblioteca.domain.BibliotecaLivro;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BibliotecaLivroJpaRepository extends JpaRepository<BibliotecaLivro, Long> {
  Optional<BibliotecaLivro> findByCodigo(String codigo);
}
