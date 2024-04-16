package com.lib.springframework.web.exception;

import com.lib.BusinessValidationException;
import com.lib.EmptyResultException;
import com.lib.ResourceAlreadyExistsException;
import com.lib.ResourceNotFoundException;
import com.lib.springframework.web.exception.ApiError.ApiErrorBuilder;
import io.opentracing.Tracer;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalErrorHandler extends ResponseEntityExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalErrorHandler.class);

  @Autowired(required = false)
  Tracer tracer;

  private Optional<Tracer> getTracer() {
    return Optional.ofNullable(tracer);
  }

  private ApiErrorBuilder newBody(Throwable cause, HttpStatus status) {
    var builder = ApiError.builder();

    getTracer()
      .map(Tracer::activeSpan)
      .filter(Objects::nonNull)
      .ifPresent(
        span -> {
          builder.withTraceId(span.context().toTraceId());
          builder.withTraceId(span.context().toSpanId());
        }
      );

    builder.withCode(String.valueOf(status.value()));
    builder.withMessage(cause.getMessage());
    builder.withType(cause.getClass().getSimpleName());

    return builder;
  }

  @ExceptionHandler({ IllegalArgumentException.class })
  public ResponseEntity<Object> handleExceptionTo400(
    Exception cause,
    WebRequest request
  ) {
    var status = HttpStatus.BAD_REQUEST;
    var body = newBody(cause, status);

    log.error(cause.getMessage(), cause);

    return handleExceptionInternal(
      cause,
      body.build(),
      new HttpHeaders(),
      status,
      request
    );
  }

  @ExceptionHandler({ BusinessValidationException.class })
  public ResponseEntity<Object> handleExceptionTo422(
    BusinessValidationException cause,
    WebRequest request
  ) {
    var status = HttpStatus.UNPROCESSABLE_ENTITY;
    var body = newBody(cause, status);

    cause.getDetails().forEach(body::addDetail);

    log.error(cause.getMessage(), cause);

    return handleExceptionInternal(
      cause,
      body.build(),
      new HttpHeaders(),
      status,
      request
    );
  }

  @ExceptionHandler({ ResourceNotFoundException.class })
  public ResponseEntity<Object> handleExceptionTo404(
    ResourceNotFoundException cause,
    WebRequest request
  ) {
    var status = HttpStatus.NOT_FOUND;
    var body = newBody(cause, status);

    log.error(cause.getMessage(), cause);

    return handleExceptionInternal(
      cause,
      body.build(),
      new HttpHeaders(),
      status,
      request
    );
  }

  @ExceptionHandler({ EmptyResultException.class })
  public ResponseEntity<Object> handleExceptionTo204(
    EmptyResultException cause,
    WebRequest request
  ) {
    var body = List.of();

    return handleExceptionInternal(
      cause,
      body,
      new HttpHeaders(),
      HttpStatus.NO_CONTENT,
      request
    );
  }

  @ExceptionHandler({ ResourceAlreadyExistsException.class })
  public ResponseEntity<Object> handleExceptionTo409(
    ResourceAlreadyExistsException cause,
    WebRequest request
  ) {
    var status = HttpStatus.CONFLICT;
    var body = newBody(cause, status);

    log.error(cause.getMessage(), cause);

    return handleExceptionInternal(
      cause,
      body.build(),
      new HttpHeaders(),
      status,
      request
    );
  }
}
