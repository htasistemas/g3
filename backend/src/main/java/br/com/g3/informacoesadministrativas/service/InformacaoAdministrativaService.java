package br.com.g3.informacoesadministrativas.service;

import br.com.g3.informacoesadministrativas.dto.InformacaoAdministrativaRequest;
import br.com.g3.informacoesadministrativas.dto.InformacaoAdministrativaResponse;
import br.com.g3.informacoesadministrativas.dto.InformacaoAdministrativaRevealResponse;
import java.util.List;

public interface InformacaoAdministrativaService {
  List<InformacaoAdministrativaResponse> listar(
      String tipo,
      String categoria,
      String titulo,
      String tags,
      Boolean status,
      Long usuarioId,
      String ip);

  InformacaoAdministrativaResponse buscarPorId(Long id, Long usuarioId, String ip);

  InformacaoAdministrativaResponse criar(InformacaoAdministrativaRequest request, Long usuarioId, String ip);

  InformacaoAdministrativaResponse atualizar(
      Long id,
      InformacaoAdministrativaRequest request,
      Long usuarioId,
      String ip);

  void remover(Long id, Long usuarioId, String ip);

  InformacaoAdministrativaRevealResponse revelar(Long id, Long usuarioId, String ip);

  void registrarCopia(Long id, Long usuarioId, String ip);
}
