package com.lib;

import java.util.Collections;
import java.util.List;

public abstract class AbstractRuntimeExceptionWithDetails
  extends RuntimeException {

  private List<String> details;

  /**
   * @param message Just one line that sums up the error
   * @param details List of details
   */
  public AbstractRuntimeExceptionWithDetails(
    String message,
    List<String> details
  ) {
    super(message);
    this.details = details;
  }

  public List<String> getDetails() {
    return Collections.unmodifiableList(this.details);
  }
}
