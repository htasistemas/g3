package br.com.g3.patrimonio.repository;

import br.com.g3.patrimonio.domain.PatrimonioMovimentacao;

public interface PatrimonioMovimentacaoRepository {
  PatrimonioMovimentacao salvar(PatrimonioMovimentacao movimento);
}
