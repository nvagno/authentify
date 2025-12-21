package app.esiroi.auth.model.exception;

import static app.esiroi.auth.model.exception.ApiException.ExceptionType.CLIENT_EXCEPTION;

public class ForbiddenException extends ApiException {
  public ForbiddenException(String message) {
    super(message, CLIENT_EXCEPTION);
  }
}
