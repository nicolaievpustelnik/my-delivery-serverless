package com.lib.exception.handlers;

import com.lib.exception.AbstractExceptionHandler;
import com.lib.exception.ExceptionHandlerComponent;
import com.lib.exception.custom.InvalidFieldsException;
import com.lib.http.HttpStatus;
import com.lib.http.ProblemDetail;

/**
 * The {@code InvalidFieldExceptionHandler} class extends the {@code AbstractExceptionHandler} class and handles {@code InvalidFieldsException} objects.
 * It is annotated with @ExceptionHandlerComponent which indicates that it is a component that handles exceptions.
 */
@ExceptionHandlerComponent
public class InvalidFieldExceptionHandler extends AbstractExceptionHandler<InvalidFieldsException> {

  /**
   * Constructs a new {@code InvalidFieldExceptionHandler} calling the constructor of the superclass {@code AbstractExceptionHandler}
   * with the {@code InvalidFieldsException} class as parameter.
   */
  public InvalidFieldExceptionHandler() {
    super(InvalidFieldsException.class);
  }

  /**
   * Handles the InvalidFieldsException {@link InvalidFieldsException} and returns a {@code ProblemDetail} object.
   * Overrides the "handle" method of the superclass {@code AbstractExceptionHandler}.
   *
   * @param exception the exception to handle
   * @return a ProblemDetail {@link ProblemDetail}
   */
  @Override
  public ProblemDetail handle(InvalidFieldsException exception) {
    return ProblemDetail.forStatusAndDetailAndTitle(HttpStatus.BAD_REQUEST, exception.getMessage(), "Invalid Fields");
  }
}
