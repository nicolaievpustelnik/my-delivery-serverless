package com.lib.runtime;

import com.lib.exception.custom.LambdaRuntimeException;
import com.lib.http.HttpHeaders;
import com.lib.http.HttpMethod;
import com.lib.utils.MapperUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Utility class for propagating errors, executing HTTP requests and responses with AWS Lambda.
 * This class provides methods to handle AWS Lambda specific HTTP interactions and error handling.
 */

public final class AwsHttpPropagation {

  private static final HttpClient httpClient = HttpClient.newHttpClient();

  /**
   * Executes an HTTP request with the specified HttpRequest instance.
   *
   * @param request The HttpRequest instance to be executed.
   * @return An HttpResponse<String> instance containing the response.
   */
  private static HttpResponse<String> executeRequest(HttpRequest request) {
    try {
      return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
      throw new LambdaRuntimeException("InterruptedException while sending error to AWS", ex);
    } catch (Exception ex) {
      throw new LambdaRuntimeException("Error while sending error to AWS", ex);
    }
  }

  /**
   * Propagates an AWS error with the provided requestId and exception.
   *
   * @throws LambdaRuntimeException if there is an error while sending the error to AWS.
   */
  public void propagateAwsError(String errorUrl, Object errorMap) throws LambdaRuntimeException {
    HttpRequest request;

    try {
      request = buildRequest(URI.create(errorUrl), HttpRequest.BodyPublishers.ofString(MapperUtils.objectToJson(errorMap)), HttpMethod.POST);
    } catch (Exception ex) {
      throw new LambdaRuntimeException("Error while sending error to AWS", ex);
    }
    executeRequest(request);
  }

  /**
   * Executes an HTTP request with the specified URI.
   *
   * @param uri The URI for the HTTP request.
   * @return An HttpResponse<String> instance containing the response.
   */
  public HttpResponse<String> executeRequest(String uri) {
    HttpRequest request = buildRequest(URI.create(uri), HttpRequest.BodyPublishers.noBody(), HttpMethod.GET);
    return executeRequest(request);
  }

  /**
   * Executes an HTTP response with the specified URI and response body.
   *
   * @param uri      The URI for the HTTP response.
   * @param response The response body as a JSON string.
   * @return An HttpResponse<String> instance containing the response.
   */
  public HttpResponse<String> executeResponse(String uri, String response) {
    HttpRequest request = buildRequest(URI.create(uri), HttpRequest.BodyPublishers.ofString(response), HttpMethod.POST);
    return executeRequest(request);
  }

  /**
   * Builds a new HttpRequest instance with the specified URI, body publisher, and HTTP method.
   *
   * @param uri           The URI for the request.
   * @param bodyPublisher The HttpRequest.BodyPublisher instance providing the request body content.
   * @param method        The HttpMethod instance specifying the HTTP method to be used.
   * @return A HttpRequest instance with the specified URI, body publisher, and HTTP method.
   */
  private HttpRequest buildRequest(URI uri, HttpRequest.BodyPublisher bodyPublisher, HttpMethod method) {
    HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
      .uri(uri)
      .setHeader(HttpHeaders.USER_AGENT.value(), Environment.USER_AGENT_VALUE);

    if (method == HttpMethod.POST) {
      requestBuilder.POST(bodyPublisher);
    } else if (method == HttpMethod.GET) {
      requestBuilder.GET();
    }

    return requestBuilder.build();
  }
}
