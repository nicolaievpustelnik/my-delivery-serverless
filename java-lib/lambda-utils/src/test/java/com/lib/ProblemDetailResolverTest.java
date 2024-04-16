package com.lib;

import com.lib.exception.ProblemDetailResolver;
import com.lib.exception.custom.ParameterException;
import com.lib.exception.handlers.ParameterNotFoundExceptionHandler;
import com.lib.http.HttpStatus;
import com.lib.http.ProblemDetail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ProblemDetailResolverTest {

  private final URI mockUri = URI.create("http://example.com");

  @InjectMocks
  private ProblemDetailResolver resolver = new ProblemDetailResolver(List.of(new ParameterNotFoundExceptionHandler()));

  @Test
  void testResolveException_WithCustomHandler() {
    String errorMessage = "Test error message";
    Throwable exception = new ParameterException(errorMessage);

    ProblemDetail expectedProblemDetail = ProblemDetail.forStatusAndDetailAndTitle(HttpStatus.BAD_REQUEST,
      exception.getMessage(), "Error on manipulate parameters");
    expectedProblemDetail.setInstance(mockUri);

    ProblemDetail actualProblemDetail = resolver.resolveException(exception, mockUri);

    assertEquals(expectedProblemDetail.getStatus(), actualProblemDetail.getStatus());
    assertEquals(expectedProblemDetail.getDetail(), actualProblemDetail.getDetail());
    assertEquals(expectedProblemDetail.getTitle(), actualProblemDetail.getTitle());
    assertEquals(expectedProblemDetail.getInstance(), actualProblemDetail.getInstance());

  }

  @Test
  void testResolveException_WithDefaultHandler() {
    Throwable exception = new NullPointerException("Test error message");

    ProblemDetail expectedProblemDetail = ProblemDetail.forStatusAndDetailAndTitle(HttpStatus.INTERNAL_SERVER_ERROR,
      exception.getMessage(), "Internal Server Error");
    expectedProblemDetail.setInstance(mockUri);

    ProblemDetail actualProblemDetail = resolver.resolveException(exception, mockUri);


    assertEquals(expectedProblemDetail.getStatus(), actualProblemDetail.getStatus());
    assertEquals(expectedProblemDetail.getDetail(), actualProblemDetail.getDetail());
    assertEquals(expectedProblemDetail.getTitle(), actualProblemDetail.getTitle());
    assertEquals(expectedProblemDetail.getInstance(), actualProblemDetail.getInstance());
  }
}
