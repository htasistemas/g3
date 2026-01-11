package br.com.g3.emprestimoseventos.mapper;

import br.com.g3.emprestimoseventos.domain.EmprestimoEvento;
import br.com.g3.emprestimoseventos.domain.EmprestimoEventoItem;
import br.com.g3.emprestimoseventos.domain.EmprestimoEventoMovimentacao;
import br.com.g3.emprestimoseventos.domain.EventoEmprestimo;
import br.com.g3.emprestimoseventos.dto.EmprestimoEventoItemRequest;
import br.com.g3.emprestimoseventos.dto.EmprestimoEventoItemResponse;
import br.com.g3.emprestimoseventos.dto.EmprestimoEventoMovimentacaoResponse;
import br.com.g3.emprestimoseventos.dto.EmprestimoEventoResponse;
import br.com.g3.emprestimoseventos.dto.EventoEmprestimoResponse;
import br.com.g3.emprestimoseventos.dto.ResponsavelResumoResponse;
import java.util.ArrayList;
import java.util.List;

public class EmprestimosEventosMapper {
  public EventoEmprestimoResponse paraEventoResposta(EventoEmprestimo evento) {
    EventoEmprestimoResponse resposta = new EventoEmprestimoResponse();
    resposta.setId(evento.getId());
    resposta.setTitulo(evento.getTitulo());
    resposta.setDescricao(evento.getDescricao());
    resposta.setLocal(evento.getLocal());
    resposta.setDataInicio(evento.getDataInicio());
    resposta.setDataFim(evento.getDataFim());
    resposta.setStatus(evento.getStatus());
    return resposta;
  }

  public EmprestimoEventoItemResponse paraItemResposta(EmprestimoEventoItem item) {
    EmprestimoEventoItemResponse resposta = new EmprestimoEventoItemResponse();
    resposta.setId(item.getId());
    resposta.setItemId(item.getItemId());
    resposta.setTipoItem(item.getTipoItem());
    resposta.setQuantidade(item.getQuantidade());
    resposta.setStatusItem(item.getStatusItem());
    resposta.setObservacaoItem(item.getObservacaoItem());
    return resposta;
  }

  public List<EmprestimoEventoItemResponse> paraItensResposta(List<EmprestimoEventoItem> itens) {
    List<EmprestimoEventoItemResponse> respostas = new ArrayList<>();
    for (EmprestimoEventoItem item : itens) {
      respostas.add(paraItemResposta(item));
    }
    return respostas;
  }

  public EmprestimoEventoResponse paraEmprestimoResposta(
      EmprestimoEvento emprestimo,
      EventoEmprestimo evento,
      String responsavelNome,
      List<EmprestimoEventoItemResponse> itens) {
    EmprestimoEventoResponse resposta = new EmprestimoEventoResponse();
    resposta.setId(emprestimo.getId());
    resposta.setEvento(paraEventoResposta(evento));
    resposta.setUnidadeId(emprestimo.getUnidadeId());
    resposta.setResponsavel(
        emprestimo.getResponsavelId() != null
            ? new ResponsavelResumoResponse(emprestimo.getResponsavelId(), responsavelNome)
            : null);
    resposta.setDataRetiradaPrevista(emprestimo.getDataRetiradaPrevista());
    resposta.setDataDevolucaoPrevista(emprestimo.getDataDevolucaoPrevista());
    resposta.setDataRetiradaReal(emprestimo.getDataRetiradaReal());
    resposta.setDataDevolucaoReal(emprestimo.getDataDevolucaoReal());
    resposta.setStatus(emprestimo.getStatus());
    resposta.setObservacoes(emprestimo.getObservacoes());
    resposta.setItens(itens);
    return resposta;
  }

  public EmprestimoEventoMovimentacaoResponse paraMovimentacaoResposta(
      EmprestimoEventoMovimentacao movimentacao) {
    EmprestimoEventoMovimentacaoResponse resposta = new EmprestimoEventoMovimentacaoResponse();
    resposta.setId(movimentacao.getId());
    resposta.setAcao(movimentacao.getAcao());
    resposta.setDescricao(movimentacao.getDescricao());
    resposta.setUsuarioId(movimentacao.getUsuarioId());
    resposta.setCriadoEm(movimentacao.getCriadoEm());
    return resposta;
  }

  public EmprestimoEventoItem paraItemEntidade(
      EmprestimoEvento emprestimo,
      EmprestimoEventoItemRequest requisicao) {
    EmprestimoEventoItem item = new EmprestimoEventoItem();
    item.setEmprestimo(emprestimo);
    item.setItemId(requisicao.getItemId());
    item.setTipoItem(requisicao.getTipoItem());
    item.setQuantidade(requisicao.getQuantidade());
    item.setStatusItem(requisicao.getStatusItem());
    item.setObservacaoItem(requisicao.getObservacaoItem());
    return item;
  }
}
