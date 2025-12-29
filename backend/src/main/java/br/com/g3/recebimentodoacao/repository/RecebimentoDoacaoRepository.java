package br.com.g3.recebimentodoacao.repository;

import br.com.g3.recebimentodoacao.domain.RecebimentoDoacao;
import java.util.List;
import java.util.Optional;

public interface RecebimentoDoacaoRepository {
  RecebimentoDoacao salvar(RecebimentoDoacao recebimento);

  List<RecebimentoDoacao> listar();

  Optional<RecebimentoDoacao> buscarPorId(Long id);
}
