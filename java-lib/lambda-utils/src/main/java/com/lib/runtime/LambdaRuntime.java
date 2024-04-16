package com.lib.runtime;

import com.lib.utils.MapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;

import java.net.http.HttpResponse;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * LambdaRuntime is a custom AWS Lambda Runtime for Java applications.
 * It provides an event-driven runtime environment that handles incoming
 * events, executes the appropriate function, and returns the response.
 * This class is designed to be used as a custom runtime within AWS Lambda.
 *
 * @author Watlas
 */
public class LambdaRuntime implements SmartLifecycle {

  private static final Logger logger = LoggerFactory.getLogger(LambdaRuntime.class);
  private final AwsHttpPropagation awsHttpPropagation;
  private final FunctionInfo functionInfo;
  private boolean isRunning = false;


  public LambdaRuntime(AwsHttpPropagation awsHttpPropagation, FunctionInfo functionInfo) {
    this.awsHttpPropagation = awsHttpPropagation;
    this.functionInfo = functionInfo;
  }

  public void run() {
    logger.info("Starting Custom Runtime");
    try {
      eventLoop();
    } catch (Exception e) {
      logger.error("Error on receipt event message", e);
    }
  }

  /**
   * Implements an event loop that constantly polls for new events,
   * executes the appropriate function, and returns the response.
   */
  private void eventLoop() {
    String runtimeApi = Environment.getRuntimeApi();

    String eventUri = MessageFormat.format(Environment.LAMBDA_RUNTIME_URL_TEMPLATE, runtimeApi, Environment.LAMBDA_VERSION_DATE);

    while (isRunning) {
      logger.debug("Polling for next event: {}", eventUri);

      HttpResponse<String> httpResponse = awsHttpPropagation.executeRequest(eventUri);

      String body = httpResponse.body();
      if (body != null) {

        String requestId = httpResponse.headers().firstValue(Environment.LAMBDA_RUNTIME_AWS_REQUEST_ID).orElseThrow(RuntimeException::new);

        try {
          logger.info("Event received: {}, Request ID: {}", body, requestId);


          Object input = MapperUtils.jsonToObject(body, functionInfo.inputType());

          Optional<String> traceId = httpResponse.headers().firstValue("Lambda-Runtime-Trace-Id");
          if (traceId.isPresent() && !traceId.get().isEmpty()) {
            logger.debug("Lambda-Runtime-Trace-Id: {}", traceId);
            System.setProperty("com.amazonaws.xray.traceHeader", traceId.get());
          }

          Object response = functionInfo.apply(input);

          String responseJson = MapperUtils.objectToJson(response);

          String invocationUrl = MessageFormat
            .format(Environment.LAMBDA_INVOCATION_URL_TEMPLATE, runtimeApi, Environment.LAMBDA_VERSION_DATE, requestId);

          HttpResponse<String> awsResponse = awsHttpPropagation.executeResponse(invocationUrl, responseJson);

          logger.debug("Response propagation aws status: {}", awsResponse.statusCode());
        } catch (Exception e) {
          logger.error("Error on execute function", e);
          propagateAwsError(requestId, runtimeApi, e);
        }

      }
    }
  }

  /**
   * Propagates an AWS error by sending the error details to the appropriate Lambda error endpoint.
   *
   * @param requestId  The unique request ID associated with the AWS Lambda invocation.
   * @param runtimeApi The API endpoint for the AWS Lambda runtime environment.
   * @param e          The exception that occurred during the Lambda function execution.
   */
  private void propagateAwsError(String requestId, String runtimeApi, Exception e) {
    String errorUrl = MessageFormat.format(Environment.LAMBDA_ERROR_URL_TEMPLATE, runtimeApi, Environment.LAMBDA_VERSION_DATE, requestId);
    Map<String, String> errorMap = new HashMap<>();
    errorMap.put("errorMessage", e.getMessage());
    errorMap.put("errorType", e.getClass().getName());
    errorMap.put("stackTrace", e.toString());
    awsHttpPropagation.propagateAwsError(errorUrl, errorMap);
  }

  @Override
  public void start() {
    if (Environment.isCustomRuntime()) {
      isRunning = true;
      run();
    } else {
      logger.info("Not running in custom runtime");
    }
  }

  @Override
  public void stop() {
    isRunning = false;
    logger.info("Shutting down Custom Runtime");
  }

  @Override
  public boolean isRunning() {
    return isRunning;
  }
}
