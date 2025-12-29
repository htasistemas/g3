package br.com.g3.doacaorealizada.repository;

import br.com.g3.doacaorealizada.domain.DoacaoRealizada;
import java.util.List;
import java.util.Optional;

public interface DoacaoRealizadaRepository {
  DoacaoRealizada salvar(DoacaoRealizada doacao);

  List<DoacaoRealizada> listar();

  Optional<DoacaoRealizada> buscarPorId(Long id);
}
