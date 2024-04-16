package com.lib.http;

public enum HttpHeaders {

  /**
   * The HTTP {@code Content-Encoding} header field name.
   *
   * @see <a href="https://tools.ietf.org/html/rfc7231#section-3.1.2.2">Section 3.1.2.2 of RFC 7231</a>
   */
  CONTENT_ENCODING("Content-Encoding"),
  /**
   * The HTTP {@code User-Agent} header field name.
   *
   * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.5.3">Section 5.5.3 of RFC 7231</a>
   */
  USER_AGENT("User-Agent"),
  /**
   * The HTTP {@code Content-Type} header field name.
   *
   * @see <a href="https://tools.ietf.org/html/rfc7231#section-3.1.1.5">Section 3.1.1.5 of RFC 7231</a>
   */
  CONTENT_TYPE("Content-Type");

  private final String headerName;

  HttpHeaders(String headerName) {
    this.headerName = headerName;
  }

  public String value() {
    return headerName;
  }

  @Override
  public String toString() {
    return headerName;
  }
}
