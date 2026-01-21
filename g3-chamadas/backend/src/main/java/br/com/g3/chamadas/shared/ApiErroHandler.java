package br.com.g3.chamadas.shared;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiErroHandler {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErro> tratarValidacao(MethodArgumentNotValidException ex, HttpServletRequest request) {
    List<String> detalhes = ex.getBindingResult().getFieldErrors().stream()
        .map(this::formatarCampo)
        .collect(Collectors.toList());
    ApiErro erro = new ApiErro("Erro de validacao.", request.getRequestURI(), LocalDateTime.now(), detalhes);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ApiErro> tratarStatus(ResponseStatusException ex, HttpServletRequest request) {
    ApiErro erro = new ApiErro(ex.getReason(), request.getRequestURI(), LocalDateTime.now(), List.of());
    return ResponseEntity.status(ex.getStatusCode()).body(erro);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiErro> tratarArgumento(IllegalArgumentException ex, HttpServletRequest request) {
    ApiErro erro = new ApiErro(ex.getMessage(), request.getRequestURI(), LocalDateTime.now(), List.of());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErro> tratarGenerico(Exception ex, HttpServletRequest request) {
    ApiErro erro = new ApiErro("Erro inesperado.", request.getRequestURI(), LocalDateTime.now(), List.of());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
  }

  private String formatarCampo(FieldError erro) {
    return erro.getField() + ": " + erro.getDefaultMessage();
  }
}
