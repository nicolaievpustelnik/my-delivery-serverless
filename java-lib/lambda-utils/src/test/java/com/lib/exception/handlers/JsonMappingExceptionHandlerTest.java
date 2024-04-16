package com.lib.handlers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.lib.exception.handlers.JsonMappingExceptionHandler;
import com.lib.http.HttpStatus;
import com.lib.http.ProblemDetail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JsonMappingExceptionHandlerTest {

  @Mock
  private JsonMappingException jsonMappingException;

  @InjectMocks
  private JsonMappingExceptionHandler exceptionHandler;

  @Test
  void testHandle() {
    String errorMessage = "Test error message";
    when(jsonMappingException.getMessage()).thenReturn(errorMessage);

    ProblemDetail expectedProblemDetail = ProblemDetail.forStatusAndDetailAndTitle(
      HttpStatus.BAD_REQUEST, errorMessage, "Invalid Fields");

    ProblemDetail actualProblemDetail = exceptionHandler.handle(jsonMappingException);

    assertEquals(expectedProblemDetail.getStatus(), actualProblemDetail.getStatus());
    assertEquals(expectedProblemDetail.getDetail(), actualProblemDetail.getDetail());
    assertEquals(expectedProblemDetail.getTitle(), actualProblemDetail.getTitle());
  }
}
