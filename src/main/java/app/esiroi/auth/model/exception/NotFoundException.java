package app.esiroi.auth.model.exception;

import static app.esiroi.auth.model.exception.ApiException.ExceptionType.CLIENT_EXCEPTION;

public class NotFoundException extends ApiException {
  public NotFoundException(String message) {
    super(message, CLIENT_EXCEPTION);
  }
}
