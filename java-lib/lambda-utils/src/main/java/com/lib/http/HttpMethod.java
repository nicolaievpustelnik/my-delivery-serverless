package com.lib.http;

/**
 * An enumeration of HTTP methods as defined in the Hypertext Transfer Protocol (HTTP/1.1).
 *
 * @see <a href="https://tools.ietf.org/html/rfc7231#section-4">Section 4 of RFC 7231</a>
 */
public enum HttpMethod {
  /**
   * The HTTP {@code GET} method requests a representation of the specified resource.
   *
   * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3.1">Section 4.3.1 of RFC 7231</a>
   */
  GET,

  /**
   * The HTTP {@code POST} method submits an entity to the specified resource, often causing a change in state.
   *
   * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3.3">Section 4.3.3 of RFC 7231</a>
   */
  POST,

  /**
   * The HTTP {@code PUT} method replaces all current representations of the target resource with the request payload.
   *
   * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3.4">Section 4.3.4 of RFC 7231</a>
   */
  PUT,

  /**
   * The HTTP {@code DELETE} method deletes the specified resource.
   *
   * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3.5">Section 4.3.5 of RFC 7231</a>
   */
  DELETE,

  /**
   * The HTTP {@code PATCH} method applies partial modifications to a resource.
   *
   * @see <a href="https://tools.ietf.org/html/rfc5789">RFC 5789</a>
   */
  PATCH,

  /**
   * The HTTP {@code HEAD} method asks for a response identical to that of a GET request, but without the response body.
   *
   * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3.2">Section 4.3.2 of RFC 7231</a>
   */
  HEAD,

  /**
   * The HTTP {@code OPTIONS} method describes the communication options for the target resource.
   *
   * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3.7">Section 4.3.7 of RFC 7231</a>
   */
  OPTIONS;

  /**
   * Returns the HttpMethod from the given string.
   *
   * @param method the method string to be converted to HttpMethod
   * @return HttpMethod corresponding to the given string
   * @throws IllegalArgumentException if the string doesn't match any HttpMethod
   */
  public static HttpMethod fromString(String method) {
    for (HttpMethod httpMethod : values()) {
      if (httpMethod.name().equalsIgnoreCase(method)) {
        return httpMethod;
      }
    }
    throw new IllegalArgumentException("Invalid HttpMethod: " + method);
  }
}
