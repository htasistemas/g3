package br.com.g3.transparencia.repository;

import br.com.g3.transparencia.domain.TransparenciaDestinacao;
import java.util.List;

public interface TransparenciaDestinacaoRepository {
  TransparenciaDestinacao salvar(TransparenciaDestinacao destinacao);

  List<TransparenciaDestinacao> listarPorTransparencia(Long transparenciaId);

  void removerPorTransparencia(Long transparenciaId);
}
