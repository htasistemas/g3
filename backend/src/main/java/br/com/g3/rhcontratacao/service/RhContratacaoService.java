package br.com.g3.rhcontratacao.service;

import br.com.g3.rhcontratacao.dto.RhAtualizarStatusRequest;
import br.com.g3.rhcontratacao.dto.RhArquivoRequest;
import br.com.g3.rhcontratacao.dto.RhArquivoResponse;
import br.com.g3.rhcontratacao.dto.RhAuditoriaContratacaoResponse;
import br.com.g3.rhcontratacao.dto.RhCartaBancoRequest;
import br.com.g3.rhcontratacao.dto.RhCartaBancoResponse;
import br.com.g3.rhcontratacao.dto.RhCandidatoRequest;
import br.com.g3.rhcontratacao.dto.RhCandidatoResponse;
import br.com.g3.rhcontratacao.dto.RhCandidatoResumoResponse;
import br.com.g3.rhcontratacao.dto.RhDocumentoItemRequest;
import br.com.g3.rhcontratacao.dto.RhDocumentoItemResponse;
import br.com.g3.rhcontratacao.dto.RhEntrevistaRequest;
import br.com.g3.rhcontratacao.dto.RhEntrevistaResponse;
import br.com.g3.rhcontratacao.dto.RhFichaAdmissaoRequest;
import br.com.g3.rhcontratacao.dto.RhFichaAdmissaoResponse;
import br.com.g3.rhcontratacao.dto.RhPpdRequest;
import br.com.g3.rhcontratacao.dto.RhPpdResponse;
import br.com.g3.rhcontratacao.dto.RhProcessoContratacaoResponse;
import br.com.g3.rhcontratacao.dto.RhTermoRequest;
import br.com.g3.rhcontratacao.dto.RhTermoResponse;
import java.util.List;
import org.springframework.core.io.Resource;

public interface RhContratacaoService {
  List<RhCandidatoResumoResponse> listarCandidatos(String termo);
  RhCandidatoResponse buscarCandidato(Long candidatoId);
  RhProcessoContratacaoResponse buscarProcessoPorCandidato(Long candidatoId);
  RhCandidatoResponse criarCandidato(RhCandidatoRequest request, Long usuarioId);
  RhCandidatoResponse atualizarCandidato(Long candidatoId, RhCandidatoRequest request, Long usuarioId);
  void inativarCandidato(Long candidatoId, Long usuarioId);
  RhProcessoContratacaoResponse atualizarStatus(Long processoId, RhAtualizarStatusRequest request, Long usuarioId);

  List<RhEntrevistaResponse> listarEntrevistas(Long processoId);
  RhEntrevistaResponse salvarEntrevista(Long processoId, RhEntrevistaRequest request, Long usuarioId);

  RhFichaAdmissaoResponse buscarFichaAdmissao(Long processoId);
  RhFichaAdmissaoResponse salvarFichaAdmissao(Long processoId, RhFichaAdmissaoRequest request, Long usuarioId);

  List<RhDocumentoItemResponse> listarDocumentos(Long processoId);
  RhDocumentoItemResponse atualizarDocumentoItem(Long itemId, RhDocumentoItemRequest request, Long usuarioId);

  List<RhArquivoResponse> listarArquivos(Long processoId);
  RhArquivoResponse adicionarArquivo(Long processoId, RhArquivoRequest request, Long usuarioId);
  Resource obterArquivo(Long arquivoId);

  List<RhTermoResponse> listarTermos(Long processoId);
  RhTermoResponse salvarTermo(Long processoId, RhTermoRequest request, Long usuarioId);

  RhPpdResponse buscarPpd(Long processoId);
  RhPpdResponse salvarPpd(Long processoId, RhPpdRequest request, Long usuarioId);

  RhCartaBancoResponse buscarCartaBanco(Long processoId);
  RhCartaBancoResponse salvarCartaBanco(Long processoId, RhCartaBancoRequest request, Long usuarioId);

  List<RhAuditoriaContratacaoResponse> listarAuditoria(Long processoId);
}
