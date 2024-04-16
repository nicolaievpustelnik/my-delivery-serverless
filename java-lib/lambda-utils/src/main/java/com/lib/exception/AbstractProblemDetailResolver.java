package com.lib.exception;

import com.lib.http.HttpStatus;
import com.lib.http.ProblemDetail;

import java.net.URI;
import java.util.List;
import java.util.Objects;

/**
 * The {@code AbstractProblemDetailResolver} is a class that provides a base implementation for resolving exceptions and generating problem details in a RESTful API.
 * The {@code AbstractProblemDetailResolver} class is abstract, meaning it cannot be instantiated directly,
 * but must be subclassed and the abstract methods implemented.
 */
public abstract class AbstractProblemDetailResolver {
  private final List<AbstractExceptionHandler<? extends Throwable>> exceptionHandlers;

  /**
   * Defines a constructor for {@code AbstractProblemDetailResolver} class, and takes a List of {@code AbstractExceptionHandler} objects as its input parameter.
   * These exception handlers are objects that implement the {@code AbstractExceptionHandler} interface and
   * provide specialized handling for specific types of exceptions.
   *
   * @param exceptionHandlers List of {@code AbstractExceptionHandler} objects
   */
  protected AbstractProblemDetailResolver(List<AbstractExceptionHandler<? extends Throwable>> exceptionHandlers) {
    this.exceptionHandlers = exceptionHandlers;
  }

  /**
   * This method checks if the {@code Throwable} object has a cause using the "getCause" method.
   * If the cause is null, the method returns the input  {@code Throwable} object itself.
   * If is not null, the method calls itself recursively with the cause of the input  {@code Throwable} object until the cause is null.
   *
   * @param throwable a {@code Throwable} object
   * @return a {@code Throwable} object
   */
  private static Throwable getRootCause(Throwable throwable) {
    if (throwable.getCause() == null) {
      return throwable;
    }
    return getRootCause(throwable.getCause());
  }

  /**
   * This method creates a default {@code ProblemDetail} object setting HttpStatus.INTERNAL_SERVER_ERROR, root cause message obtained from the input and a generic title.
   *
   * @param exception a {@code Throwable} object
   * @return a default {@code ProblemDetail} object
   */
  private static ProblemDetail buildDefaultProblemDetail(Throwable exception) {
    Throwable rootCause = getRootCause(exception);
    return ProblemDetail.forStatusAndDetailAndTitle(HttpStatus.INTERNAL_SERVER_ERROR, rootCause.getMessage(), "Internal Server Error");
  }

  /**
   * This method takes an {@code Throwable} object and a {@code URI} object and returns a {@code ProblemDetail} object
   * that includes information about the exception and the URI where the exception occurred.
   *
   * @param exception {@code Throwable} object that occurred
   * @param uri       URI where the exception occurred
   * @return a {@code ProblemDetail} object
   */
  public ProblemDetail resolveException(Throwable exception, URI uri) {
    ProblemDetail problemDetail = resolveException(exception);
    problemDetail.setInstance(uri);
    return problemDetail;
  }

  /**
   * The method loops through a list of {@code AbstractExceptionHandler} objects and checks if it can handle the input parameter by calling the "canHandle" method on the object.
   * The "canHandle" method takes a {@code Throwable} object as input and returns a boolean.
   *
   * @param exception {@code Throwable} object to handle
   * @return a {@code ProblemDetail} object if the exception is on the list or a default {@code ProblemDetail} if not.
   */
  private ProblemDetail resolveException(Throwable exception) {
    for (AbstractExceptionHandler<? extends Throwable> handler : exceptionHandlers) {
      if (handler.canHandle(exception)) {
        @SuppressWarnings("unchecked")
        //factory problemaDetail
        ProblemDetail handle = ((ExceptionHandler<Throwable>) handler).handle(exception);
        return Objects.requireNonNullElseGet(handle, () -> buildDefaultProblemDetail(exception));
      }
    }
    return buildDefaultProblemDetail(exception);
  }
}
