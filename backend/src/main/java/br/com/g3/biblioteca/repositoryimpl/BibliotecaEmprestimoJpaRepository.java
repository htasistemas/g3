package br.com.g3.biblioteca.repositoryimpl;

import br.com.g3.biblioteca.domain.BibliotecaEmprestimo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BibliotecaEmprestimoJpaRepository extends JpaRepository<BibliotecaEmprestimo, Long> {
  @Query("select e from BibliotecaEmprestimo e join fetch e.livro order by e.dataEmprestimo desc, e.id desc")
  List<BibliotecaEmprestimo> findAllWithLivroOrderByDataEmprestimoDescIdDesc();

  @Query("select e from BibliotecaEmprestimo e join fetch e.livro where e.id = :id")
  Optional<BibliotecaEmprestimo> findByIdWithLivro(@Param("id") Long id);

  @Query("select count(e) from BibliotecaEmprestimo e where e.livro.id = :livroId and e.status in :status")
  long contarPorLivroEStatus(@Param("livroId") Long livroId, @Param("status") List<String> status);
}
