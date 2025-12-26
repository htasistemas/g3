package br.com.g3.cadastroprofissionais.repository;

import br.com.g3.cadastroprofissionais.domain.CadastroProfissional;
import java.util.List;
import java.util.Optional;

public interface CadastroProfissionalRepository {
  CadastroProfissional salvar(CadastroProfissional cadastro);

  List<CadastroProfissional> listar();

  List<CadastroProfissional> buscarPorNome(String nome);

  Optional<CadastroProfissional> buscarPorId(Long id);

  void remover(CadastroProfissional cadastro);
}
