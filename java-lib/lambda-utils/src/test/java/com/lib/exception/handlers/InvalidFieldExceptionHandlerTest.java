package com.lib.exception.handlers;

import com.lib.exception.custom.InvalidFieldsException;
import com.lib.http.HttpStatus;
import com.lib.http.ProblemDetail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class InvalidFieldExceptionHandlerTest {

  @InjectMocks
  private InvalidFieldExceptionHandler exceptionHandler;

  @Test
  void testHandle() {
    String errorMessage = "Test error message";
    InvalidFieldsException exception = new InvalidFieldsException(errorMessage);

    ProblemDetail expectedProblemDetail = ProblemDetail.forStatusAndDetailAndTitle(
      HttpStatus.BAD_REQUEST, errorMessage, "Invalid Fields");

    ProblemDetail actualProblemDetail = exceptionHandler.handle(exception);

    assertEquals(expectedProblemDetail.getStatus(), actualProblemDetail.getStatus());
    assertEquals(expectedProblemDetail.getDetail(), actualProblemDetail.getDetail());
    assertEquals(expectedProblemDetail.getTitle(), actualProblemDetail.getTitle());
  }
}
