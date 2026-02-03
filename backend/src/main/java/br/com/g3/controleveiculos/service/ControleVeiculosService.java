package br.com.g3.controleveiculos.service;

import br.com.g3.controleveiculos.dto.DiarioBordoRequest;
import br.com.g3.controleveiculos.dto.DiarioBordoResponse;
import br.com.g3.controleveiculos.dto.MotoristaAutorizadoRequest;
import br.com.g3.controleveiculos.dto.MotoristaAutorizadoResponse;
import br.com.g3.controleveiculos.dto.MotoristaDisponivelResponse;
import br.com.g3.controleveiculos.dto.VeiculoRequest;
import br.com.g3.controleveiculos.dto.VeiculoResponse;
import java.util.List;

public interface ControleVeiculosService {
  List<VeiculoResponse> listarVeiculos();

  VeiculoResponse criarVeiculo(VeiculoRequest request);

  VeiculoResponse atualizarVeiculo(Long id, VeiculoRequest request);

  void excluirVeiculo(Long id);

  List<DiarioBordoResponse> listarDiarios();

  DiarioBordoResponse criarDiario(DiarioBordoRequest request);

  DiarioBordoResponse atualizarDiario(Long id, DiarioBordoRequest request);

  void excluirDiario(Long id);

  List<MotoristaDisponivelResponse> listarMotoristasDisponiveis(String nome);

  List<MotoristaAutorizadoResponse> listarMotoristasAutorizados(Long veiculoId);

  MotoristaAutorizadoResponse criarMotoristaAutorizado(MotoristaAutorizadoRequest request);

  MotoristaAutorizadoResponse atualizarMotoristaAutorizado(Long id, MotoristaAutorizadoRequest request);

  void excluirMotoristaAutorizado(Long id);
}
