package app.esiroi.auth.model.exception;

import static app.esiroi.auth.model.exception.ApiException.ExceptionType.SERVER_EXCEPTION;

public class TooManyRequestException extends ApiException {
  public TooManyRequestException(String message) {
    super(message, SERVER_EXCEPTION);
  }
}
