package app.esiroi.auth.model.exception;

import static app.esiroi.auth.model.exception.ApiException.ExceptionType.CLIENT_EXCEPTION;

public class BadRequestException extends ApiException {
  public BadRequestException(String message) {
    super(message, CLIENT_EXCEPTION);
  }
}
