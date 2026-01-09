package br.com.g3.chamadotecnico.service;

import br.com.g3.chamadotecnico.dto.ChamadoTecnicoAcaoResponse;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoAnexoRequest;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoAnexoResponse;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoAtribuicaoRequest;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoAuditoriaVinculoRequest;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoAuditoriaVinculoResponse;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoComentarioRequest;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoComentarioResponse;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoCriacaoRequest;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoAtualizacaoRequest;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoListaResponse;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoResponse;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoStatusRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ChamadoTecnicoService {
  ChamadoTecnicoResponse criar(ChamadoTecnicoCriacaoRequest request);

  ChamadoTecnicoResponse atualizar(UUID id, ChamadoTecnicoAtualizacaoRequest request);

  ChamadoTecnicoResponse buscarPorId(UUID id);

  ChamadoTecnicoListaResponse listar(
      String status,
      String tipo,
      String prioridade,
      String responsavel,
      String modulo,
      String cliente,
      LocalDate dataInicio,
      LocalDate dataFim,
      String texto,
      int pagina,
      int tamanhoPagina);

  ChamadoTecnicoResponse alterarStatus(UUID id, ChamadoTecnicoStatusRequest request);

  ChamadoTecnicoResponse atribuirResponsavel(UUID id, ChamadoTecnicoAtribuicaoRequest request);

  ChamadoTecnicoComentarioResponse adicionarComentario(
      UUID id, ChamadoTecnicoComentarioRequest request);

  List<ChamadoTecnicoComentarioResponse> listarComentarios(UUID id);

  ChamadoTecnicoAnexoResponse adicionarAnexo(UUID id, ChamadoTecnicoAnexoRequest request);

  List<ChamadoTecnicoAnexoResponse> listarAnexos(UUID id);

  List<ChamadoTecnicoAcaoResponse> listarAcoes(UUID id);

  ChamadoTecnicoAuditoriaVinculoResponse vincularAuditoria(
      UUID id, ChamadoTecnicoAuditoriaVinculoRequest request);

  List<ChamadoTecnicoAuditoriaVinculoResponse> listarAuditoriasVinculadas(UUID id);
}
