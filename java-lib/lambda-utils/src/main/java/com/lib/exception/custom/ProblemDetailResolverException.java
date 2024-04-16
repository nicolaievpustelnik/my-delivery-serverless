package com.lib.exception.custom;

import com.lib.exception.ErrorHandler;

/**
 * This exception can be used to handle situations where o {@link ErrorHandler} don`t can handle the exception.
 */
public class ProblemDetailResolverException extends RuntimeException {

  public ProblemDetailResolverException(String message) {
    super(message);
  }

  public ProblemDetailResolverException(String message, Throwable cause) {
    super(message, cause);
  }
}
