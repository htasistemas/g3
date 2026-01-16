package br.com.g3.shared.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErroControllerAdvice {

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<RespostaErro> tratarResponseStatusException(
      ResponseStatusException ex, HttpServletRequest request) {
    HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
    HttpStatus statusFinal = status != null ? status : HttpStatus.BAD_REQUEST;
    RespostaErro resposta =
        new RespostaErro(
            OffsetDateTime.now().toString(),
            statusFinal.value(),
            statusFinal.getReasonPhrase(),
            ex.getReason() != null ? ex.getReason() : "Erro nao informado.",
            request.getRequestURI());
    return ResponseEntity.status(statusFinal).body(resposta);
  }

  public record RespostaErro(
      String timestamp,
      int status,
      String erro,
      String mensagem,
      String caminho) {}
}
