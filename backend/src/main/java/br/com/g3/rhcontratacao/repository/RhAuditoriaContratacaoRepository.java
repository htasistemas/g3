package br.com.g3.rhcontratacao.repository;

import br.com.g3.rhcontratacao.domain.RhAuditoriaContratacao;
import java.util.List;

public interface RhAuditoriaContratacaoRepository {
  RhAuditoriaContratacao salvar(RhAuditoriaContratacao auditoria);
  List<RhAuditoriaContratacao> listarPorProcesso(Long processoId);
}
