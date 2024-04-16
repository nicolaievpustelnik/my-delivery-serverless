package com.lib.http;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ProblemDetailTest {

  @Test
  void testForStatus() {
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    assertEquals(HttpStatus.NOT_FOUND.value(), problemDetail.getStatus());
  }

  @Test
  void testForStatusAndDetail() {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid input");
    assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
    assertEquals("Invalid input", problemDetail.getDetail());
  }

  @Test
  void testForStatusAndDetailAndTitle() {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetailAndTitle(HttpStatus.INTERNAL_SERVER_ERROR, "Server error", "Internal Error");
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), problemDetail.getStatus());
    assertEquals("Server error", problemDetail.getDetail());
    assertEquals("Internal Error", problemDetail.getTitle());
  }

  @Test
  void testSettersAndGetters() {
    ProblemDetail problemDetail = new ProblemDetail();
    problemDetail.setStatus(HttpStatus.UNAUTHORIZED);
    problemDetail.setDetail("Unauthorized access");
    problemDetail.setTitle("Access Denied");
    problemDetail.setType(URI.create("https://example.com/problems/unauthorized"));
    problemDetail.setInstance(URI.create("https://example.com/instances/1234"));
    problemDetail.setProperty("customKey", "customValue");

    assertEquals(HttpStatus.UNAUTHORIZED.value(), problemDetail.getStatus());
    assertEquals("Unauthorized access", problemDetail.getDetail());
    assertEquals("Access Denied", problemDetail.getTitle());
    assertEquals(URI.create("https://example.com/problems/unauthorized"), problemDetail.getType());
    assertEquals(URI.create("https://example.com/instances/1234"), problemDetail.getInstance());
    assertEquals("customValue", problemDetail.getProperties().get("customKey"));
  }
}
