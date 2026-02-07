package br.com.g3.cadastrobeneficiario.repository;

import br.com.g3.cadastrobeneficiario.domain.CadastroBeneficiario;
import java.util.List;
import java.util.Optional;

public interface CadastroBeneficiarioRepository {
  CadastroBeneficiario salvar(CadastroBeneficiario cadastro);

  List<CadastroBeneficiario> listar();

  List<CadastroBeneficiario> listarPorNomeEStatus(String nome, String status);

  List<CadastroBeneficiario> buscarPorNome(String nome);

  List<CadastroBeneficiario> buscarPorCodigo(List<String> codigos);

  Optional<CadastroBeneficiario> buscarPorId(Long id);

  Integer buscarMaiorCodigo();

  void remover(CadastroBeneficiario cadastro);
}
