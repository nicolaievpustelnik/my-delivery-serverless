package com.lib.shoji;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ShojiNotIssuedTheTokenException extends RuntimeException {

  public ShojiNotIssuedTheTokenException(String token) {
    super(token);
  }
}
