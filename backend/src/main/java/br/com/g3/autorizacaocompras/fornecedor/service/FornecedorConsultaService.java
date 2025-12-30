package br.com.g3.autorizacaocompras.fornecedor.service;

import br.com.g3.autorizacaocompras.fornecedor.dto.FornecedorCnpjResponse;

public interface FornecedorConsultaService {
  FornecedorCnpjResponse buscarPorCnpj(String cnpj);
}
