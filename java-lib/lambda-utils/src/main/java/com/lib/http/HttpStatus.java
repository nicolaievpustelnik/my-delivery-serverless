package com.lib.http;

public enum HttpStatus {

  OK(200, Series.SUCCESSFUL, "OK"),
  /**
   * {@code 201 Created}.
   *
   * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.3.2">HTTP/1.1: Semantics and Content, section 6.3.2</a>
   */
  CREATED(201, Series.SUCCESSFUL, "Created"),
  /**
   * {@code 202 Accepted}.
   *
   * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.3.3">HTTP/1.1: Semantics and Content, section 6.3.3</a>
   */
  ACCEPTED(202, Series.SUCCESSFUL, "Accepted"),
  /**
   * {@code 203 Non-Authoritative Information}.
   *
   * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.3.4">HTTP/1.1: Semantics and Content, section 6.3.4</a>
   */
  NO_CONTENT(204, Series.SUCCESSFUL, "No Content"),

  /**
   * {@code 400 Bad Request}.
   *
   * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.1">HTTP/1.1: Semantics and Content, section 6.5.1</a>
   */
  BAD_REQUEST(400, Series.CLIENT_ERROR, "Bad Request"),
  /**
   * {@code 401 Unauthorized}.
   *
   * @see <a href="https://tools.ietf.org/html/rfc7235#section-3.1">HTTP/1.1: Authentication, section 3.1</a>
   */
  UNAUTHORIZED(401, Series.CLIENT_ERROR, "Unauthorized"),
  /**
   * {@code 403 Forbidden}.
   *
   * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.3">HTTP/1.1: Semantics and Content, section 6.5.3</a>
   */
  FORBIDDEN(403, Series.CLIENT_ERROR, "Forbidden"),
  /**
   * {@code 404 Not Found}.
   *
   * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.4">HTTP/1.1: Semantics and Content, section 6.5.4</a>
   */
  NOT_FOUND(404, Series.CLIENT_ERROR, "Not Found"),
  /**
   * {@code 500 Internal Server Error}.
   *
   * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.1">HTTP/1.1: Semantics and Content, section 6.6.1</a>
   */
  INTERNAL_SERVER_ERROR(500, Series.SERVER_ERROR, "Internal Server Error"),
  /**
   * {@code 501 Not Implemented}.
   *
   * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.2">HTTP/1.1: Semantics and Content, section 6.6.2</a>
   */
  NOT_IMPLEMENTED(501, Series.SERVER_ERROR, "Not Implemented"),
  /**
   * {@code 502 Bad Gateway}.
   *
   * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.3">HTTP/1.1: Semantics and Content, section 6.6.3</a>
   */
  BAD_GATEWAY(502, Series.SERVER_ERROR, "Bad Gateway"),
  /**
   * {@code 503 Service Unavailable}.
   *
   * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.4">HTTP/1.1: Semantics and Content, section 6.6.4</a>
   */
  SERVICE_UNAVAILABLE(503, Series.SERVER_ERROR, "Service Unavailable");

  private static final HttpStatus[] VALUES;

  static {
    VALUES = values();
  }

  private final int value;

  private final Series series;

  private final String reasonPhrase;

  HttpStatus(int value, Series series, String reasonPhrase) {
    this.value = value;
    this.series = series;
    this.reasonPhrase = reasonPhrase;
  }

  public static HttpStatus valueOf(int statusCode) {
    HttpStatus status = resolve(statusCode);
    if (status == null) {
      throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
    }
    return status;
  }

  public static HttpStatus resolve(int statusCode) {
    // Use cached VALUES instead of values() to prevent array allocation.
    for (HttpStatus status : VALUES) {
      if (status.value == statusCode) {
        return status;
      }
    }
    return null;
  }

  public int value() {
    return this.value;
  }

  public Series series() {
    return this.series;
  }

  public String getReasonPhrase() {
    return this.reasonPhrase;
  }

  public boolean is2xxSuccessful() {
    return (series() == Series.SUCCESSFUL);
  }

  public boolean is4xxClientError() {
    return (series() == Series.CLIENT_ERROR);
  }

  public boolean is5xxServerError() {
    return (series() == Series.SERVER_ERROR);
  }

  public boolean isError() {
    return (is4xxClientError() || is5xxServerError());
  }

  @Override
  public String toString() {
    return this.value + " " + name();
  }

  /**
   * Enumeration of HTTP status series.
   * <p>Retrievable via {@link HttpStatus#series()}.
   */
  public enum Series {

    SUCCESSFUL(2),
    CLIENT_ERROR(4),
    SERVER_ERROR(5);

    private final int value;

    Series(int value) {
      this.value = value;
    }

    /**
     * Return the integer value of this status series. Ranges from 1 to 5.
     */
    public int value() {
      return this.value;
    }

  }
}
