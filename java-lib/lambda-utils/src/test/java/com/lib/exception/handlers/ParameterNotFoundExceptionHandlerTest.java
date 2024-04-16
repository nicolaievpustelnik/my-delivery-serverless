package com.lib.exception.handlers;

import com.lib.exception.custom.ParameterException;
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
class ParameterNotFoundExceptionHandlerTest {

  @Mock
  private ParameterException parameterException;

  @InjectMocks
  private ParameterNotFoundExceptionHandler exceptionHandler;

  @Test
  void testHandle() {
    String errorMessage = "Test error message";
    when(parameterException.getMessage()).thenReturn(errorMessage);

    ProblemDetail expectedProblemDetail = ProblemDetail.forStatusAndDetailAndTitle(
      HttpStatus.BAD_REQUEST, errorMessage, "Error on manipulate parameters");

    ProblemDetail actualProblemDetail = exceptionHandler.handle(parameterException);

    assertEquals(expectedProblemDetail.getStatus(), actualProblemDetail.getStatus());
    assertEquals(expectedProblemDetail.getDetail(), actualProblemDetail.getDetail());
    assertEquals(expectedProblemDetail.getTitle(), actualProblemDetail.getTitle());
  }
}
