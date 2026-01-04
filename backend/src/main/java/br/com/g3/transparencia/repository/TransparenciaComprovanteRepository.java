package br.com.g3.transparencia.repository;

import br.com.g3.transparencia.domain.TransparenciaComprovante;
import java.util.List;

public interface TransparenciaComprovanteRepository {
  TransparenciaComprovante salvar(TransparenciaComprovante comprovante);

  List<TransparenciaComprovante> listarPorTransparencia(Long transparenciaId);

  void removerPorTransparencia(Long transparenciaId);
}
