package com.lib.exception.handlers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.lib.exception.AbstractExceptionHandler;
import com.lib.exception.ExceptionHandlerComponent;
import com.lib.http.HttpStatus;
import com.lib.http.ProblemDetail;

/**
 * The {@code JsonMappingExceptionHandler} class extends the {@code AbstractExceptionHandler} class and handles {@code JsonMappingException}.
 * It is annotated with @ExceptionHandlerComponent which indicates that it is a component that handles exceptions.
 */
@ExceptionHandlerComponent
public class JsonMappingExceptionHandler extends AbstractExceptionHandler<JsonMappingException> {

  /**
   * Constructs a new {@code JsonMappingExceptionHandler} initializing the parent class with the {@code JsonMappingException} class.
   */
  public JsonMappingExceptionHandler() {
    super(JsonMappingException.class);
  }

  /**
   * This method takes the {@code JsonMappingException} and returns a {@code ProblemDetail} object.
   * The {@code ProblemDetail} object is created using the "forStatusAndDetailAndTitle" method of the {@code ProblemDetail} class
   * which takes three arguments: an HttpStatus, an error message and a title.
   *
   * @param exception {@code JsonMappingException} to handle
   * @return a {@code ProblemDetail}
   */
  @Override
  public ProblemDetail handle(JsonMappingException exception) {
    return ProblemDetail.forStatusAndDetailAndTitle(HttpStatus.BAD_REQUEST, exception.getMessage(), "Invalid Fields");
  }
}
