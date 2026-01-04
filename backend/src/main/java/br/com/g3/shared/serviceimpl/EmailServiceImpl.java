package br.com.g3.shared.serviceimpl;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import br.com.g3.shared.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EmailServiceImpl implements EmailService {
  private static final String CONFIG_EMAIL_ARQUIVO = "configuracao servidor email.txt";
  private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

  private final JavaMailSender mailSender;
  private final boolean habilitado;
  private final String remetente;
  private final String nomeRemetente;
  private final String usuarioMail;
  private final String hostMail;

  public EmailServiceImpl(
      JavaMailSender mailSender,
      @Value("${app.email.habilitado:false}") boolean habilitado,
      @Value("${app.email.remetente:}") String remetente,
      @Value("${app.email.nome-remetente:HTA Sistemas}") String nomeRemetente,
      @Value("${spring.mail.username:}") String usuarioMail,
      @Value("${spring.mail.host:}") String hostMail) {
    this.mailSender = mailSender;
    this.habilitado = habilitado;
    this.remetente = remetente;
    this.nomeRemetente = nomeRemetente;
    this.usuarioMail = usuarioMail;
    this.hostMail = hostMail;
  }

  @Override
  public void enviarRecuperacaoSenha(String destinatario, String nome, String token, String validade) {
    configurarMailSeNecessario();
    if (destinatario == null || destinatario.trim().isEmpty()) {
      LOGGER.warn("Envio de email de recuperacao ignorado: destinatario vazio.");
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Email do destinatario nao informado.");
    }

    boolean envioHabilitado = habilitado;
    String hostConfigurado = hostMail;
    if ((hostConfigurado == null || hostConfigurado.trim().isEmpty()) && mailSender instanceof JavaMailSenderImpl) {
      hostConfigurado = ((JavaMailSenderImpl) mailSender).getHost();
    }
    if (!envioHabilitado && hostConfigurado != null && !hostConfigurado.trim().isEmpty()) {
      envioHabilitado = true;
    }

    if (!envioHabilitado) {
      LOGGER.warn(
          "Envio de email de recuperacao desabilitado. Verifique APP_EMAIL_HABILITADO e MAIL_HOST.");
      throw new ResponseStatusException(
          HttpStatus.SERVICE_UNAVAILABLE, "Envio de email desabilitado.");
    }

    String from = remetente == null || remetente.trim().isEmpty() ? usuarioMail : remetente;
    if ((from == null || from.trim().isEmpty()) && mailSender instanceof JavaMailSenderImpl) {
      String username = ((JavaMailSenderImpl) mailSender).getUsername();
      from = username == null || username.trim().isEmpty() ? from : username;
    }
    if (from == null || from.trim().isEmpty()) {
      LOGGER.warn(
          "Envio de email de recuperacao ignorado: remetente nao configurado. Verifique APP_EMAIL_REMETENTE ou MAIL_USER.");
      throw new ResponseStatusException(
          HttpStatus.SERVICE_UNAVAILABLE, "Remetente nao configurado.");
    }

    String destinatarioSeguro = destinatario.trim().toLowerCase();
    String nomeSeguro = nome == null || nome.trim().isEmpty() ? "usuario" : nome.trim();

    String corpo =
        "Ola "
            + nomeSeguro
            + ",\n\n"
            + "Recebemos sua solicitacao de recuperacao de senha no G3.\n"
            + "Para redefinir sua senha, informe o token abaixo na tela de recuperacao:\n\n"
            + token
            + "\n\n"
            + "Validade do token: "
            + validade
            + "\n\n"
            + "Se voce nao solicitou a recuperacao, ignore este email.\n"
            + "Atenciosamente,\n"
            + "Equipe G3";

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
      throw new ResponseStatusException(
          HttpStatus.SERVICE_UNAVAILABLE, "Falha ao enviar email de recuperacao.");
    }
  }
  private void configurarMailSeNecessario() {
    if (!(mailSender instanceof JavaMailSenderImpl)) {
      return;
    }

    JavaMailSenderImpl sender = (JavaMailSenderImpl) mailSender;
    boolean hostVazio = sender.getHost() == null || sender.getHost().trim().isEmpty();
    boolean usuarioVazio = sender.getUsername() == null || sender.getUsername().trim().isEmpty();
    boolean senhaVazia = sender.getPassword() == null || sender.getPassword().trim().isEmpty();

    if (!hostVazio && !usuarioVazio && !senhaVazia) {
      return;
    }

    Map<String, String> valores = new HashMap<>();
    valores.putAll(System.getenv());
    valores.putAll(lerConfiguracaoEmail());

    if (hostVazio) {
      String host = obterValor(valores, "MAIL_HOST");
      if (host != null) {
        sender.setHost(host);
      }
    }

    if (usuarioVazio) {
      String usuario = obterValor(valores, "MAIL_USER");
      if (usuario != null) {
        sender.setUsername(usuario);
      }
    }

    if (senhaVazia) {
      String senha = obterValor(valores, "MAIL_PASS");
      if (senha != null) {
        sender.setPassword(senha);
      }
    }

    String porta = obterValor(valores, "MAIL_PORT");
    if (porta != null) {
      try {
        sender.setPort(Integer.parseInt(porta));
      } catch (NumberFormatException ex) {
        LOGGER.warn("Porta de email invalida em configuracao: {}", porta);
      }
    }
  }

  private Map<String, String> lerConfiguracaoEmail() {
    Map<String, String> valores = new HashMap<>();
    for (Path caminho : new Path[] {
        Paths.get(CONFIG_EMAIL_ARQUIVO),
        Paths.get("..", CONFIG_EMAIL_ARQUIVO)
    }) {
      if (Files.exists(caminho)) {
        try {
          for (String linha : Files.readAllLines(caminho, StandardCharsets.UTF_8)) {
            String valor = extrairValorEnv(linha);
            if (valor != null) {
              String[] partes = valor.split("=", 2);
              if (partes.length == 2) {
                valores.put(partes[0].trim(), partes[1].trim());
              }
            }
          }
        } catch (Exception ex) {
          LOGGER.warn("Falha ao ler configuracao de email em {}", caminho, ex);
        }
        break;
      }
    }
    return valores;
  }

  private String extrairValorEnv(String linha) {
    if (linha == null) {
      return null;
    }
    String limpa = linha.trim();
    if (!limpa.toLowerCase().startsWith("set ")) {
      return null;
    }
    String valor = limpa.substring(4).trim();
    return valor.isEmpty() ? null : valor;
  }

  private String obterValor(Map<String, String> valores, String chave) {
    if (valores.containsKey(chave)) {
      String valor = valores.get(chave);
      return valor == null || valor.trim().isEmpty() ? null : valor.trim();
    }
    return null;
  }
}
