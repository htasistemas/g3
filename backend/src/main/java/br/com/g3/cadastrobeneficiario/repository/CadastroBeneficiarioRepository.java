package br.com.g3.cadastrobeneficiario.repository;

import br.com.g3.cadastrobeneficiario.domain.CadastroBeneficiario;
import java.util.List;
import java.util.Optional;

public interface CadastroBeneficiarioRepository {
  CadastroBeneficiario salvar(CadastroBeneficiario cadastro);

  List<CadastroBeneficiario> listar();

  List<CadastroBeneficiario> buscarPorNome(String nome);

  Optional<CadastroBeneficiario> buscarPorId(Long id);

  void remover(CadastroBeneficiario cadastro);
}
