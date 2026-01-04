package br.com.g3.transparencia.repository;

import br.com.g3.transparencia.domain.TransparenciaRecebimento;
import java.util.List;

public interface TransparenciaRecebimentoRepository {
  TransparenciaRecebimento salvar(TransparenciaRecebimento recebimento);

  List<TransparenciaRecebimento> listarPorTransparencia(Long transparenciaId);

  void removerPorTransparencia(Long transparenciaId);
}
