package com.lib.shoji;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ShojiPublicKeyException extends RuntimeException {

  public ShojiPublicKeyException(String message) {
    super(message);
  }
}
