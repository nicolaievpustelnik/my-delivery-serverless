package com.lib.exception.custom;

import com.lib.runtime.LambdaRuntime;

/**
 * This exception can be used to o {@link LambdaRuntime} don`t can handle the exception.
 */
public class LambdaRuntimeException extends RuntimeException {

  public LambdaRuntimeException(String message) {
    super(message);
  }

  public LambdaRuntimeException(String message, Exception e) {
    super(message, e);
  }
}
