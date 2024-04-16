package com.lib;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lib.exception.ErrorHandler;
import com.lib.exception.ProblemDetailResolver;
import com.lib.exception.handlers.InvalidFieldExceptionHandler;
import com.lib.http.HttpStatus;
import com.lib.http.ProblemDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ErrorHandlerTest {

  @Mock
  private ProblemDetailResolver mockResolver = new ProblemDetailResolver(List.of(new InvalidFieldExceptionHandler()));

  @InjectMocks
  private ErrorHandler errorHandler;

  @Mock
  private APIGatewayProxyRequestEvent mockRequestEvent;


  @BeforeEach
  void setUp() {
    errorHandler = new ErrorHandler(mockResolver);
  }

  @Test
  void testResolve_WithExceptionAndRequestEvent_ReturnsResponseEvent() throws JsonProcessingException {
    Throwable exception = new RuntimeException("Test error message");
    ProblemDetail expectedProblemDetail = ProblemDetail.forStatusAndDetailAndTitle(
      HttpStatus.BAD_REQUEST, "Test error message", "Internal Server Error");
    URI mockUri = URI.create("/path");
    APIGatewayProxyResponseEvent expectedResponseEvent = new APIGatewayProxyResponseEvent();
    expectedResponseEvent.setBody(new ObjectMapper().writeValueAsString(expectedProblemDetail));
    expectedResponseEvent.setStatusCode(400);

    when(mockRequestEvent.getPath()).thenReturn("/path");
    when(mockResolver.resolveException(exception, mockUri)).thenReturn(expectedProblemDetail);

    APIGatewayProxyResponseEvent actualResponseEvent = errorHandler.resolve(exception, mockRequestEvent);

    assertEquals(expectedResponseEvent.getBody(), actualResponseEvent.getBody());
    assertEquals(expectedResponseEvent.getStatusCode(), actualResponseEvent.getStatusCode());
  }
}
