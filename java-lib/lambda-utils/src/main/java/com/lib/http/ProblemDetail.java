package com.lib.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A class representing a problem detail object as specified by RFC 7807. This object is used to provide standardized error responses.
 */
public class ProblemDetail {

  public static final String HTTP_STATUS_IS_REQUIRED = "HttpStatus is required";
  /**
   * A URI reference that identifies the problem type. When dereferenced, it should provide human-readable documentation for the problem type.
   * Default is "about:blank". Example: URI.create("https://example.com/problems/out-of-stock")
   */
  private static final URI BLANK_TYPE = URI.create("about:blank");
  private URI type = BLANK_TYPE;

  /**
   * A short, human-readable summary of the problem type. It should not change from occurrence to occurrence of the problem, except for purposes of
   * localization. Example: "Out of Stock"
   */
  private String title;

  /**
   * The HTTP status code for this occurrence of the problem. Example: 404
   */
  private int status = 500;

  /**
   * A human-readable explanation specific to this occurrence of the problem. Example: "Item 42 is no longer available"
   */
  private String detail;

  /**
   * A URI reference that identifies the specific occurrence of the problem. It may or may not yield further information if dereferenced. Example:
   * URI.create("https://example.com/orders/42")
   */
  private URI instance;

  /**
   * A Map containing additional, custom properties to be included in the problem detail object. The keys should be unique and the values should be
   * JSON-compatible types. Example: properties.put("itemId", 42); properties.put("available", false);
   */
  @JsonInclude(Include.NON_NULL)
  private Map<String, Object> properties;

  protected ProblemDetail() {
  }

  protected ProblemDetail(int rawStatusCode) {
    this.status = rawStatusCode;
  }

  protected ProblemDetail(ProblemDetail other) {
    this.type = other.type;
    this.title = other.title;
    this.status = other.status;
    this.detail = other.detail;
    this.instance = other.instance;
    this.properties = (other.properties != null ? new LinkedHashMap<>(other.properties) : null);
  }

  public static ProblemDetail forStatus(HttpStatus status) {
    Objects.requireNonNull(status, HTTP_STATUS_IS_REQUIRED);
    return forStatus(status.value());
  }

  public static ProblemDetail forStatus(int status) {
    return new ProblemDetail(status);
  }

  public static ProblemDetail forStatusAndDetail(HttpStatus status, String detail) {
    Objects.requireNonNull(status, HTTP_STATUS_IS_REQUIRED);
    ProblemDetail problemDetail = forStatus(status.value());
    problemDetail.setDetail(detail);
    return problemDetail;
  }

  public static ProblemDetail forStatusAndDetailAndTitle(HttpStatus status, String detail, String title) {
    Objects.requireNonNull(status, HTTP_STATUS_IS_REQUIRED);
    ProblemDetail problemDetail = forStatus(status.value());
    problemDetail.setDetail(detail);
    problemDetail.setTitle(title);
    return problemDetail;
  }

  public URI getType() {
    return this.type;
  }

  public void setType(URI type) {
    this.type = type;
  }

  public String getTitle() {
    if (this.title == null) {
      HttpStatus httpStatus = HttpStatus.resolve(this.status);
      if (httpStatus != null) {
        return httpStatus.getReasonPhrase();
      }
    }
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getStatus() {
    return this.status;
  }

  public void setStatus(HttpStatus httpStatus) {
    this.status = httpStatus.value();
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getDetail() {
    return this.detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public URI getInstance() {
    return this.instance;
  }

  public void setInstance(URI instance) {
    this.instance = instance;
  }

  public void setProperty(String name, Object value) {
    this.properties = (this.properties != null ? this.properties : new LinkedHashMap<>());
    this.properties.put(name, value);
  }

  public Map<String, Object> getProperties() {
    return this.properties;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[" + initToStringContent() + "]";
  }

  protected String initToStringContent() {
    return "type=" + getType() + ", title=" + getTitle() + ", status=" + getStatus() + ", detail=" + getDetail() + ", instance=" + getInstance()
      + ", properties=" + getProperties();
  }
}
