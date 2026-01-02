package br.com.g3.shared.serviceimpl;

import br.com.g3.shared.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
  private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

  private final JavaMailSender mailSender;
  private final boolean habilitado;
  private final String remetente;
  private final String nomeRemetente;
  private final String usuarioMail;

  public EmailServiceImpl(
      JavaMailSender mailSender,
      @Value("${app.email.habilitado:false}") boolean habilitado,
      @Value("${app.email.remetente:}") String remetente,
      @Value("${app.email.nome-remetente:HTA Sistemas}") String nomeRemetente,
      @Value("${spring.mail.username:}") String usuarioMail) {
    this.mailSender = mailSender;
    this.habilitado = habilitado;
    this.remetente = remetente;
    this.nomeRemetente = nomeRemetente;
    this.usuarioMail = usuarioMail;
  }

  @Override
  public void enviarRecuperacaoSenha(String destinatario, String nome, String token, String validade) {
    if (!habilitado || destinatario == null || destinatario.trim().isEmpty()) {
      return;
    }

    String from = remetente == null || remetente.trim().isEmpty() ? usuarioMail : remetente;
    if (from == null || from.trim().isEmpty()) {
      return;
    }

    String destinatarioSeguro = destinatario.trim().toLowerCase();
    String nomeSeguro = nome == null || nome.trim().isEmpty() ? "usuario" : nome.trim();

    String corpo =
        "Ola "
            + nomeSeguro
            + ",\n\n"
            + "Recebemos sua solicitacao de recuperacao de senha.\n"
            + "Use o token abaixo para redefinir sua senha:\n\n"
            + token
            + "\n\n"
            + "Validade: "
            + validade
            + "\n\n"
            + "Se voce nao solicitou a recuperacao, ignore este email.\n"
            + "G3";

    try {
      MimeMessageHelper helper =
          new MimeMessageHelper(mailSender.createMimeMessage(), "UTF-8");
      helper.setTo(destinatarioSeguro);
      helper.setFrom(from, nomeRemetente);
      helper.setSubject("Recuperacao de senha - G3");
      helper.setText(corpo, false);
      mailSender.send(helper.getMimeMessage());
    } catch (Exception ex) {
      LOGGER.warn("Falha ao enviar email de recuperacao de senha.", ex);
    }
  }
}
