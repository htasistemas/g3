package br.com.g3.fotoseventos.repository;

import br.com.g3.fotoseventos.domain.FotoEvento;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FotoEventoRepository {
  FotoEvento salvar(FotoEvento evento);

  Optional<FotoEvento> buscarPorId(Long id);

  Page<FotoEvento> listarComFiltros(
      String busca, LocalDate dataInicio, LocalDate dataFim, Long unidadeId, Pageable pageable);

  void remover(FotoEvento evento);
}
