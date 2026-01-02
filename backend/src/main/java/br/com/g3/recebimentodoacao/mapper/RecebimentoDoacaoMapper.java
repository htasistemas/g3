package br.com.g3.recebimentodoacao.mapper;

import br.com.g3.recebimentodoacao.domain.Doador;
import br.com.g3.recebimentodoacao.domain.RecebimentoDoacao;
import br.com.g3.recebimentodoacao.dto.DoadorRequest;
import br.com.g3.recebimentodoacao.dto.DoadorResponse;
import br.com.g3.recebimentodoacao.dto.RecebimentoDoacaoRequest;
import br.com.g3.recebimentodoacao.dto.RecebimentoDoacaoResponse;

public class RecebimentoDoacaoMapper {
  public Doador toDoador(DoadorRequest request) {
    Doador doador = new Doador();
    doador.setNome(request.getNome());
    doador.setTipoPessoa(request.getTipoPessoa());
    doador.setDocumento(request.getDocumento());
    doador.setResponsavelEmpresa(request.getResponsavelEmpresa());
    doador.setEmail(request.getEmail());
    doador.setTelefone(request.getTelefone());
    doador.setLogradouro(request.getLogradouro());
    doador.setNumero(request.getNumero());
    doador.setComplemento(request.getComplemento());
    doador.setBairro(request.getBairro());
    doador.setCidade(request.getCidade());
    doador.setUf(request.getUf());
    doador.setCep(request.getCep());
    doador.setObservacoes(request.getObservacoes());
    return doador;
  }

  public DoadorResponse toDoadorResponse(Doador doador) {
    DoadorResponse response = new DoadorResponse();
    response.setId(doador.getId());
    response.setNome(doador.getNome());
    response.setTipoPessoa(doador.getTipoPessoa());
    response.setDocumento(doador.getDocumento());
    response.setResponsavelEmpresa(doador.getResponsavelEmpresa());
    response.setEmail(doador.getEmail());
    response.setTelefone(doador.getTelefone());
    response.setLogradouro(doador.getLogradouro());
    response.setNumero(doador.getNumero());
    response.setComplemento(doador.getComplemento());
    response.setBairro(doador.getBairro());
    response.setCidade(doador.getCidade());
    response.setUf(doador.getUf());
    response.setCep(doador.getCep());
    response.setObservacoes(doador.getObservacoes());
    return response;
  }

  public void applyRecebimento(RecebimentoDoacao recebimento, RecebimentoDoacaoRequest request) {
    recebimento.setTipoDoacao(request.getTipoDoacao());
    recebimento.setDescricao(request.getDescricao());
    recebimento.setQuantidadeItens(request.getQuantidadeItens());
    recebimento.setValorMedio(request.getValorMedio());
    recebimento.setValorTotal(request.getValorTotal());
    recebimento.setValor(request.getValor());
    recebimento.setDataRecebimento(request.getDataRecebimento());
    recebimento.setFormaRecebimento(request.getFormaRecebimento());
    recebimento.setRecorrente(request.isRecorrente());
    recebimento.setPeriodicidade(request.getPeriodicidade());
    recebimento.setProximaCobranca(request.getProximaCobranca());
    recebimento.setStatus(request.getStatus());
    recebimento.setObservacoes(request.getObservacoes());
  }

  public RecebimentoDoacaoResponse toRecebimentoResponse(RecebimentoDoacao recebimento) {
    RecebimentoDoacaoResponse response = new RecebimentoDoacaoResponse();
    response.setId(recebimento.getId());
    if (recebimento.getDoador() != null) {
      response.setDoadorId(recebimento.getDoador().getId());
      response.setDoadorNome(recebimento.getDoador().getNome());
    }
    response.setTipoDoacao(recebimento.getTipoDoacao());
    response.setDescricao(recebimento.getDescricao());
    response.setQuantidadeItens(recebimento.getQuantidadeItens());
    response.setValorMedio(recebimento.getValorMedio());
    response.setValorTotal(recebimento.getValorTotal());
    response.setValor(recebimento.getValor());
    response.setDataRecebimento(recebimento.getDataRecebimento());
    response.setFormaRecebimento(recebimento.getFormaRecebimento());
    response.setRecorrente(recebimento.isRecorrente());
    response.setPeriodicidade(recebimento.getPeriodicidade());
    response.setProximaCobranca(recebimento.getProximaCobranca());
    response.setStatus(recebimento.getStatus());
    response.setObservacoes(recebimento.getObservacoes());
    response.setContabilidadePendente(recebimento.isContabilidadePendente());
    return response;
  }
}
