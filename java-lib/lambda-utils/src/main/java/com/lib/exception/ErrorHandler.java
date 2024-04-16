package com.lib.exception;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.lib.exception.custom.ProblemDetailResolverException;
import com.lib.http.HttpStatus;
import com.lib.http.ProblemDetail;
import com.lib.utils.ResponseUtils;

import java.net.URI;
import java.util.Objects;

/**
 * The {@code ErrorHandler} class handles exceptions and creates a response with a {@code ProblemDetail} object and an HTTP response object.
 */
public class ErrorHandler {
  private final AbstractProblemDetailResolver abstractProblemDetailResolver;

  /**
   * Constructs a new {@code ErrorHandler} taking an {@code AbstractProblemDetailResolver} object as a parameter and assigns it to the abstractProblemDetailResolver previously initialized.
   *
   * @param abstractProblemDetailResolver an AbstractProblemDetailResolver object
   */
  public ErrorHandler(ProblemDetailResolver abstractProblemDetailResolver) {
    this.abstractProblemDetailResolver = abstractProblemDetailResolver;
  }

  /**
   * This method takes a {@code Throwable} object and a {@code APIGatewayProxyRequestEvent} object as parameters and returns a {@code APIGatewayProxyResponseEvent} object.
   * A {@code StopWatch} object is created and started at the beggining of the method, and later stopped to know how long it took to find the handler.
   * The "resolveException" method of the abstractProblemDetailResolver is called with the exception and a URI object created from the requestEvent Path.
   * A response is created using the "createResponse" method of {@code ResponseUtils} with the {@code ProblemDetail} object returned by the "resolveException" method
   * and an HttpStatus object created from the {@code ProblemDetail}´s status.
   *
   * @param exception    a Throwable object,
   * @param requestEvent an APIGatewayProxyResponseEvent object
   * @return an APIGatewayProxyResponseEvent
   */
  public APIGatewayProxyResponseEvent resolve(Throwable exception, APIGatewayProxyRequestEvent requestEvent) {
    ProblemDetail problemDetail = abstractProblemDetailResolver.resolveException(exception,
      URI.create(Objects.requireNonNullElse(requestEvent.getPath(), "")));
    try {
      return ResponseUtils.createResponse(problemDetail, HttpStatus.valueOf(problemDetail.getStatus()));
    } catch (Exception e) {
      throw new ProblemDetailResolverException("Error creating ErrorHandler error handling response", e);
    }
  }
}

