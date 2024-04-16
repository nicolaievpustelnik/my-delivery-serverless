package com.lib;

import java.util.List;

public class BusinessValidationException extends AbstractRuntimeExceptionWithDetails {

  /**
   * @param message Just one line that sums up the business error
   * @param details List of details about the error
   */
  public BusinessValidationException(String message, List<String> details) {
    super(message, details);
  }

}
