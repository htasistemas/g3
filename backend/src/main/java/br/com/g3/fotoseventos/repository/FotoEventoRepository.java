package br.com.g3.fotoseventos.repository;

import br.com.g3.fotoseventos.domain.FotoEvento;
import java.time.LocalDate;
import br.com.g3.fotoseventos.domain.StatusFotoEvento;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FotoEventoRepository {
  FotoEvento salvar(FotoEvento evento);

  Optional<FotoEvento> buscarPorId(Long id);

  Page<FotoEvento> listarComFiltros(
      String busca,
      LocalDate dataInicio,
      LocalDate dataFim,
      Long unidadeId,
      StatusFotoEvento status,
      List<String> tags,
      Pageable pageable);

  Page<FotoEvento> listarComFiltrosOrdenadoPorFotos(
      String busca,
      LocalDate dataInicio,
      LocalDate dataFim,
      Long unidadeId,
      StatusFotoEvento status,
      List<String> tags,
      Pageable pageable);

  void remover(FotoEvento evento);
}
