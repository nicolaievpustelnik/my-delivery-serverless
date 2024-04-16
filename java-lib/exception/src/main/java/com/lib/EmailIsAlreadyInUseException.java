package com.lib;

public class EmailIsAlreadyInUseException extends ResourceAlreadyExistsException {

  /**
   * @param email E-mail already used.
   */
  public EmailIsAlreadyInUseException(String email){
    super(email);
  }

}
