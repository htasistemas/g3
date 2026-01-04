package br.com.g3.fotoseventos.service;

import br.com.g3.fotoseventos.dto.FotoEventoDetalheResponse;
import br.com.g3.fotoseventos.dto.FotoEventoFotoAtualizacaoRequest;
import br.com.g3.fotoseventos.dto.FotoEventoFotoRequest;
import br.com.g3.fotoseventos.dto.FotoEventoFotoResponse;
import br.com.g3.fotoseventos.dto.FotoEventoListaResponse;
import br.com.g3.fotoseventos.dto.FotoEventoRequest;
import br.com.g3.fotoseventos.dto.FotoEventoResponse;
import java.time.LocalDate;
import java.util.List;
import org.springframework.core.io.Resource;

public interface FotoEventoService {
  FotoEventoListaResponse listar(
      String busca,
      LocalDate dataInicio,
      LocalDate dataFim,
      Long unidadeId,
      String status,
      List<String> tags,
      String ordenacao,
      int pagina,
      int tamanho);

  FotoEventoDetalheResponse obter(Long id);

  FotoEventoResponse criar(FotoEventoRequest request);

  FotoEventoResponse atualizar(Long id, FotoEventoRequest request);

  void excluir(Long id);

  FotoEventoFotoResponse adicionarFoto(Long id, FotoEventoFotoRequest request);

  FotoEventoFotoResponse atualizarFoto(
      Long id, Long fotoId, FotoEventoFotoAtualizacaoRequest request);

  void removerFoto(Long id, Long fotoId);

  Resource obterArquivoFoto(Long id, Long fotoId);

  Resource obterFotoPrincipal(Long id);
}
