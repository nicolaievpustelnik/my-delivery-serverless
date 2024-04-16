package com.lib.exception.handlers;

import com.lib.exception.AbstractExceptionHandler;
import com.lib.exception.ExceptionHandlerComponent;
import com.lib.exception.custom.ParameterException;
import com.lib.http.HttpStatus;
import com.lib.http.ProblemDetail;

/**
 * The {@code ParamenterNotFoundExceptionHandler} class extends the {@code AbstractExceptionHandler} class and handles {@code ParameterException}.
 * It is annotated with @ExceptionHandlerComponent which indicates that it is a component that handles exceptions.
 */
@ExceptionHandlerComponent
public class ParameterNotFoundExceptionHandler extends AbstractExceptionHandler<ParameterException> {

  /**
   * Constructs a new {@code ParamenterNotFoundExceptionHandler} initializing the parent class with the {@code ParameterException} class.
   */
  public ParameterNotFoundExceptionHandler() {
    super(ParameterException.class);
  }

  /**
   * This method takes the {@code ParameterException} and returns a {@code ProblemDetail} object.
   * The {@code ProblemDetail} object is created using the "forStatusAndDetailAndTitle" method of the {@code ProblemDetail} class
   * which takes three arguments: an HttpStatus, an error message and a title.
   *
   * @param exception {@code ParameterException} to handle
   * @return a {@code ProblemDetail}
   */
  @Override
  public ProblemDetail handle(ParameterException exception) {
    return ProblemDetail.forStatusAndDetailAndTitle(HttpStatus.BAD_REQUEST, exception.getMessage(), "Error on manipulate parameters");
  }
}

