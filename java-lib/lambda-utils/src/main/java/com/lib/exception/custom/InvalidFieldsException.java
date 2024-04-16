package com.lib.exception.custom;

/**
 * The {@code InvalidFieldsException} class extends {@code RuntimeException} and is a custom exception class.
 * This exception can be used to handle situations where invalid input fields are detected in an application.
 */
public class InvalidFieldsException extends RuntimeException {

  /**
   * Constructor that creates an {@code InvalidFieldsException} object taking a String parameter called error message,
   * which is used to set the message for the exception.
   *
   * @param message error message associated with the exception
   */
  public InvalidFieldsException(String message) {
    super(message);
  }
}
