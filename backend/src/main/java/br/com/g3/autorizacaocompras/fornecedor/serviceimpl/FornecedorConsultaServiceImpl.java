package br.com.g3.autorizacaocompras.fornecedor.serviceimpl;

import br.com.g3.autorizacaocompras.cotacoes.domain.AutorizacaoCompraCotacao;
import br.com.g3.autorizacaocompras.cotacoes.repository.AutorizacaoCompraCotacaoRepository;
import br.com.g3.autorizacaocompras.fornecedor.dto.FornecedorCnpjResponse;
import br.com.g3.autorizacaocompras.fornecedor.service.FornecedorConsultaService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FornecedorConsultaServiceImpl implements FornecedorConsultaService {
  private final AutorizacaoCompraCotacaoRepository cotacaoRepository;

  public FornecedorConsultaServiceImpl(AutorizacaoCompraCotacaoRepository cotacaoRepository) {
    this.cotacaoRepository = cotacaoRepository;
  }

  @Override
  public FornecedorCnpjResponse buscarPorCnpj(String cnpj) {
    AutorizacaoCompraCotacao cotacao = cotacaoRepository.buscarUltimaPorCnpj(cnpj);
    if (cotacao == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor não encontrado.");
    }
    FornecedorCnpjResponse response = new FornecedorCnpjResponse();
    response.setCnpj(cotacao.getCnpj());
    response.setRazaoSocial(cotacao.getRazaoSocial());
    response.setNomeFantasia(cotacao.getFornecedor());
    return response;
  }
}
