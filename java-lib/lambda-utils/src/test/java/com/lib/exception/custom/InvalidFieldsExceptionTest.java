package com.lib.exception.custom;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class InvalidFieldsExceptionTest {

  @Test
  void testConstructorWithMessage() {
    String errorMessage = "Test error message";
    InvalidFieldsException exception = new InvalidFieldsException(errorMessage);
    assertEquals(errorMessage, exception.getMessage());
  }
}
