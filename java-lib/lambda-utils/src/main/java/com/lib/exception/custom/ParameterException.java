package com.lib.exception.custom;

/**
 * The {@code ParameterException} class extends {@code RuntimeException} and is a custom exception class.
 * This exception can be used to handle situations where parameters can not be detected in an application.
 */
public class ParameterException extends RuntimeException {

  /**
   * Constructor that creates a new {@code ParameterException} object taking a String parameter called error message,
   * which is used to set the message for the exception.
   *
   * @param message error message associated with the exception
   */
  public ParameterException(String message) {
    super(message);
  }

}
