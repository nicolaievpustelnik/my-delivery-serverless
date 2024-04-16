package com.lib.springframework.web.exception;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiError {

  private Payload error;

  ApiError(Payload error) {
    this.error = error;
  }

  public Payload getError() {
    return this.error;
  }
 
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Payload {

    @JsonProperty String code;
    @JsonProperty String type;
    @JsonProperty String message;
    @JsonProperty String traceId;
    @JsonProperty String spanId;

    @JsonProperty
    @JsonAlias("detail")
    private List<String> details;

  }

  public static ApiErrorBuilder builder() {
    return new ApiErrorBuilder();
  }

  public static class ApiErrorBuilder {

    private String code;
    private String type;
    private String message;
    private String traceId;
    private String spanId;
    private List<String> details;

    private List<String> getDetails() {
      if(null== details){
        this.details = List.of();
      }

      return details;
    }

    public ApiError build() {

      var payload = new Payload();

      payload.code = this.code;
      payload.type = this.type;
      payload.message = this.message;
      payload.traceId = this.traceId;
      payload.spanId = this.spanId;
      payload.details = Collections.unmodifiableList(this.getDetails());

      return new ApiError(payload);
    }

    public ApiErrorBuilder withCode(String code) {
      this.code = code;
      return this;
    }

    public ApiErrorBuilder withType(String type) {
      this.type = type;
      return this;
    }

    public ApiErrorBuilder withMessage(String message) {
      this.message = message;
      return this;
    }

    public ApiErrorBuilder addDetail(String detail) {
      this.getDetails().add(detail);
      return this;
    }

    public ApiErrorBuilder withTraceId(String traceId) {
      this.traceId = traceId;
      return this;
    }

    public ApiErrorBuilder withSpanId(String spanId) {
      this.spanId = spanId;
      return this;
    }
  }
}
