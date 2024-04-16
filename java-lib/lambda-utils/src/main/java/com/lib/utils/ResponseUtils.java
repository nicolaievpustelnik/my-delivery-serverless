package com.lib.utils;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lib.http.HttpHeaders;
import com.lib.http.HttpStatus;
import com.lib.http.MediaType;

import java.util.Map;

/**
 * The {@code ResponseUtils} class is a utility class that provides a static method for creating an APIGatewayProxyResponseEvent object with an HTTP status code, Http headers, and a response body.
 * It uses the {@code ObjectMapper} class from the Jackson library to convert Java objects to JSON.
 */
public final class ResponseUtils {

  private static final ObjectMapper objectMapper = MapperUtils.getObjectMapper();

  /**
   * Constructs a new {@code ResponseUtils}. This method is private, which means it cannot be instantiated from outside the class.
   * This is because it is a utility class that provides only static methods.
   */
  private ResponseUtils() {
  }

  /**
   * This method creates an APIGatewayProxyResponseEvent object with an HTTP status code for the request, Http headers for the response and the response body.
   * The HTTP headers are set based on the media type of the response body.
   * If the HTTP status code is an error code, the media type is set to APPLICATION_PROBLEM_JSON, otherwise, it is set to APPLICATION_JSON.
   * The response body is serialized to a JSON string using the ObjectMapper instance.
   * If there is an error while serializing the response body, the method returns the empty response object initialized in the static block.
   *
   * @param responseBody the object to be sent
   * @param status       The HTTP status code for the request
   * @return APIGatewayProxyResponseEvent
   */
  public static APIGatewayProxyResponseEvent createResponse(Object responseBody, HttpStatus status) throws JsonProcessingException {
    MediaType mediaType = MediaType.APPLICATION_JSON;
    if (status.isError()) {
      mediaType = MediaType.APPLICATION_PROBLEM_JSON;
    }

    return new APIGatewayProxyResponseEvent()
      .withStatusCode(status.value())
      .withHeaders(Map.of(HttpHeaders.CONTENT_TYPE.value(), mediaType.value()))
      .withBody(objectMapper.writeValueAsString(responseBody));

  }
}
