package com.lib.utils;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lib.http.HttpHeaders;
import com.lib.http.HttpStatus;
import com.lib.http.MediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ResponseUtilsTest {

  @Test
  void testCreateResponse_success() throws JsonProcessingException {
    Object responseBody = Map.of("key", "value");
    HttpStatus status = HttpStatus.OK;
    APIGatewayProxyResponseEvent response = ResponseUtils.createResponse(responseBody, status);

    assertEquals(status.value(), response.getStatusCode());
    Assertions.assertEquals(MediaType.APPLICATION_JSON.value(), response.getHeaders().get(HttpHeaders.CONTENT_TYPE.value()));

    ObjectMapper objectMapper = new ObjectMapper();
    String expectedBody = objectMapper.writeValueAsString(responseBody);
    assertEquals(expectedBody, response.getBody());
  }

  @Test
  void testCreateResponse_error() throws JsonProcessingException {
    Object responseBody = Map.of("error", "Invalid input");
    HttpStatus status = HttpStatus.BAD_REQUEST;
    APIGatewayProxyResponseEvent response = ResponseUtils.createResponse(responseBody, status);

    assertEquals(status.value(), response.getStatusCode());
    assertEquals(MediaType.APPLICATION_PROBLEM_JSON.value(), response.getHeaders().get(HttpHeaders.CONTENT_TYPE.value()));

    ObjectMapper objectMapper = new ObjectMapper();
    String expectedBody = objectMapper.writeValueAsString(responseBody);
    assertEquals(expectedBody, response.getBody());
  }
}
