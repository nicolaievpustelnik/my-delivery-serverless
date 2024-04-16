package com.lib.http;

public enum MediaType {

  /**
   * Public constant media type that includes all media ranges (i.e. "&#42;/&#42;").
   */
  ALL("*", "*"),

  /**
   * Public constant media type for {@code application/json}.
   */
  APPLICATION_JSON("application", "json"),

  /**
   * Public constant media type for {@code application/problem+json}.
   *
   * @see <a href="https://tools.ietf.org/html/rfc7807#section-6.1">
   * Problem Details for HTTP APIs, 6.1. application/problem+json</a>
   */
  APPLICATION_PROBLEM_JSON("application", "problem+json"),

  /**
   * Public constant media type for {@code text/plain}.
   */
  TEXT_PLAIN("text", "plain");

  private final String type;
  private final String subtype;

  MediaType(String type, String subtype) {
    this.type = type;
    this.subtype = subtype;
  }

  public String value() {
    return type + "/" + subtype;
  }

  @Override
  public String toString() {
    return type + "/" + subtype;
  }
}
