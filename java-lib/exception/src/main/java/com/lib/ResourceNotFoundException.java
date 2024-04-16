package com.lib;

public class ResourceNotFoundException extends RuntimeException {
  
  /**
   * @param name Of not fount resource.
   */
  public ResourceNotFoundException(String name) {
    super(name);
  }

  public ResourceNotFoundException() {
    super();
  }
}
