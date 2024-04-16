package com.lib.exception;

/**
 * The {@code AbstractExceptionHandler<T extends Throwable>} class implements {@code ExceptionHandler<T>} interface provides a generic way to handle exceptions.
 * This class has a method "canHandle" that provides a convenient way to check if the handler can handle a particular type of exception.
 */
public abstract class AbstractExceptionHandler<T extends Throwable> implements ExceptionHandler<T> {

  /**
   * This private field is used to store the exception class that this handler can handle.
   */
  private final Class<T> exceptionClass;

  /**
   * Constructor for the abstract class AbstractExceptionHandler that takes a Class object named "exceptionClass" as input.
   * The purpose of this constructor is to set the exception class that this handler can handle.
   *
   * @param exceptionClass a Class object
   */
  protected AbstractExceptionHandler(Class<T> exceptionClass) {
    this.exceptionClass = exceptionClass;
  }

  /**
   * The method checks if the input object is an instance of the exception class that this handler can handle.
   *
   * @param exception an instance of the exception to handle
   * @return true if it can handle it and false if not.
   */
  @Override
  public boolean canHandle(Throwable exception) {
    return exceptionClass.isInstance(exception);
  }

}
