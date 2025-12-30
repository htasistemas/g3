package br.com.g3.autorizacaocompras.repository;

import br.com.g3.autorizacaocompras.domain.AutorizacaoCompra;
import java.util.List;
import java.util.Optional;

public interface AutorizacaoComprasRepository {
  AutorizacaoCompra salvar(AutorizacaoCompra compra);

  List<AutorizacaoCompra> listar();

  Optional<AutorizacaoCompra> buscarPorId(Long id);

  void remover(AutorizacaoCompra compra);
}
