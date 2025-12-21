package app.esiroi.auth.endpoint;

import static org.springframework.http.HttpStatus.*;

import app.esiroi.auth.model.exception.BadRequestException;
import app.esiroi.auth.model.exception.ForbiddenException;
import app.esiroi.auth.model.exception.NotFoundException;
import app.esiroi.auth.model.exception.TooManyRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InternalToRestException {

  @ExceptionHandler(value = {BadRequestException.class, IllegalArgumentException.class})
  public ResponseEntity<app.esiroi.auth.endpoint.rest.model.Exception> handleBadRequestException(
      Exception e) {
    return new ResponseEntity<>(toRestException(e, BAD_REQUEST), BAD_REQUEST);
  }

  @ExceptionHandler(value = NotFoundException.class)
  public ResponseEntity<app.esiroi.auth.endpoint.rest.model.Exception> handleNotFoundException(
      Exception e) {
    return new ResponseEntity<>(toRestException(e, NOT_FOUND), NOT_FOUND);
  }

  @ExceptionHandler(value = ForbiddenException.class)
  public ResponseEntity<app.esiroi.auth.endpoint.rest.model.Exception> handleForbiddenException(
      Exception e) {
    return new ResponseEntity<>(toRestException(e, FORBIDDEN), FORBIDDEN);
  }

  @ExceptionHandler(value = TooManyRequestException.class)
  public ResponseEntity<app.esiroi.auth.endpoint.rest.model.Exception>
      handleTooManyRequestException(Exception e) {
    return new ResponseEntity<>(toRestException(e, TOO_MANY_REQUESTS), TOO_MANY_REQUESTS);
  }

  private app.esiroi.auth.endpoint.rest.model.Exception toRestException(
      Exception e, HttpStatus status) {
    var exception = new app.esiroi.auth.endpoint.rest.model.Exception();
    exception.setMessage(e.getMessage());
    exception.setType(status.toString());
    return exception;
  }
}
