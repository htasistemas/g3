package br.com.g3.manualsistema.repository;

import br.com.g3.manualsistema.domain.ManualSistemaMudanca;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ManualSistemaMudancaRepository {
  ManualSistemaMudanca salvar(ManualSistemaMudanca mudanca);

  List<ManualSistemaMudanca> listarRecentes(int limite);

  Optional<LocalDateTime> buscarUltimaMudanca();
}
