package br.com.g3.oficios.repository;

import br.com.g3.oficios.domain.OficioImagem;
import java.util.List;

public interface OficioImagemRepository {
  OficioImagem salvar(OficioImagem imagem);

  List<OficioImagem> listarPorOficioId(Long oficioId);

  void removerPorId(Long id);

  void removerPorOficioId(Long oficioId);
}
