package com.lib.exception.custom;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class LambdaRuntimeExceptionTest {

  @Test
  void testConstructorWithMessage() {
    String errorMessage = "Test error message";
    LambdaRuntimeException exception = new LambdaRuntimeException(errorMessage);
    assertEquals(errorMessage, exception.getMessage());
  }

  @Test
  void testConstructorWithMessageAndException() {
    String errorMessage = "Test error message";
    LambdaRuntimeException exception = new LambdaRuntimeException(errorMessage, new RuntimeException(errorMessage));
    assertEquals(errorMessage, exception.getMessage());
  }

}
