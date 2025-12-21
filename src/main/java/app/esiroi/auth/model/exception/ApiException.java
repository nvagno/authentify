package app.esiroi.auth.model.exception;

import lombok.Getter;

public class ApiException extends RuntimeException {
  @Getter private final ExceptionType type;

  public ApiException(String message, ExceptionType type) {
    super(message);
    this.type = type;
  }

  public enum ExceptionType {
    SERVER_EXCEPTION,
    CLIENT_EXCEPTION,
  }
}
