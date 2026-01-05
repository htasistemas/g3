package br.com.g3.oficios.repository;

import br.com.g3.oficios.domain.OficioTramite;
import java.util.List;

public interface OficioTramiteRepository {
  OficioTramite salvar(OficioTramite tramite);

  List<OficioTramite> listarPorOficio(Long oficioId);

  void removerPorOficio(Long oficioId);
}
