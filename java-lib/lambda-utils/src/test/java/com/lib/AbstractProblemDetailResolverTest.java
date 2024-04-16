package com.lib;

import com.lib.exception.AbstractExceptionHandler;
import com.lib.exception.custom.InvalidFieldsException;
import com.lib.exception.handlers.InvalidFieldExceptionHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class AbstractProblemDetailResolverTest {

  @InjectMocks
  private AbstractExceptionHandler<InvalidFieldsException> handler = new InvalidFieldExceptionHandler();

  @Test
  void canHandle_withNullPointer_exception() {
    Throwable exception = new InvalidFieldsException("");
    assertTrue(handler.canHandle(exception));
  }

  @Test
  void canHandle_withDifferentException() {
    Throwable exception = new IllegalArgumentException();
    assertFalse(handler.canHandle(exception));
  }
}
