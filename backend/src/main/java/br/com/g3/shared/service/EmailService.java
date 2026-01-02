package br.com.g3.shared.service;

public interface EmailService {
  void enviarRecuperacaoSenha(String destinatario, String nome, String token, String validade);
}
