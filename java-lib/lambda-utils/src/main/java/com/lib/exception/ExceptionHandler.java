package com.lib.exception;

import com.lib.http.ProblemDetail;

/**
 * The {@code  ExceptionHandler<T extends Throwable>} is a generic interface and takes a parameter type T that extends {@code Thrpwable}.
 * This interface has two method: "canHandle" and "handle" and it can be implemented by classes that handle specific types of exceptions
 * and provide custom logic to handle those exceptions.
 *
 * @param <T>
 */
interface ExceptionHandler<T extends Throwable> {


  /**
   * Determines whether the implementation of the {@code ExceptionHandler<T extends Throwable>} interface can handle a particular exception.
   *
   * @param exception Throwable object to handle
   * @return boolean true if the exception can be handled
   */
  boolean canHandle(Throwable exception);


  /**
   * It is used to handle the exception and returns a {@code ProblemDetail} object.
   * The ProblemDetail object contains information about the exception that was handled, such as the error message and the HTTP status code.
   *
   * @param exception generic Type T that extends Throwable
   * @return the ProblemDetail {@code ProblemDetail}
   */
  ProblemDetail handle(T exception);
}
