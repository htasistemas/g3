package br.com.g3.autenticacao.serviceimpl;

import br.com.g3.autenticacao.service.GoogleTokenService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GoogleTokenServiceImpl implements GoogleTokenService {
  private static final Logger log = LoggerFactory.getLogger(GoogleTokenServiceImpl.class);
  private final GoogleIdTokenVerifier verifier;
  private final String clientId;

  public GoogleTokenServiceImpl(@Value("${app.auth.google.client-id:}") String clientId) {
    this.clientId = clientId == null ? "" : clientId.trim();
    this.verifier =
        new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
            .setAudience(Collections.singletonList(this.clientId))
            .build();
  }

  @Override
  public GoogleUsuarioInfo validarIdToken(String idToken) {
    if (clientId.isBlank()) {
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Login Google indisponivel");
    }
    String token = idToken == null ? "" : idToken.trim();
    if (token.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token invalido");
    }
    try {
      GoogleIdToken tokenVerificado = verifier.verify(token);
      if (tokenVerificado == null) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalido");
      }
      GoogleIdToken.Payload payload = tokenVerificado.getPayload();
      String issuer = payload.getIssuer();
      if (!"accounts.google.com".equals(issuer) && !"https://accounts.google.com".equals(issuer)) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalido");
      }
      Boolean emailVerificado = payload.getEmailVerified();
      if (emailVerificado == null || !emailVerificado) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Email nao verificado");
      }
      String email = payload.getEmail();
      String nome = (String) payload.get("name");
      String fotoUrl = (String) payload.get("picture");
      String googleId = payload.getSubject();
      return new GoogleUsuarioInfo(googleId, email, nome, fotoUrl);
    } catch (GeneralSecurityException | IOException ex) {
      log.warn("Falha ao validar token Google: {}", ex.getMessage());
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalido");
    }
  }
}
