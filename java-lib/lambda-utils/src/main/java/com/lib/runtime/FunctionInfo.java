package com.lib.runtime;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.lib.exception.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Function;

/**
 * A record class representing a resolved AWS Lambda function along with its input
 * and output types.
 */
final class FunctionInfo {

  private final Class<?> inputType;
  private final Class<?> outputType;
  private final Function<Object, Object> function;
  private final String name;
  private final ErrorHandler errorHandler;
  Logger log = LoggerFactory.getLogger(FunctionInfo.class);


  FunctionInfo(Class<?> inputType, Class<?> outputType, Function<Object, Object> function, String name, ErrorHandler errorHandler) {
    this.inputType = inputType;
    this.outputType = outputType;
    this.function = function;
    this.name = name;
    this.errorHandler = errorHandler;
  }

  /**
   * Processes the input data using the encapsulated Function instance.
   *
   * @param input The input data to be processed.
   * @return The result of the processing as an Object.
   */
  Object apply(Object input) {
    try {
      log.info("request: {}", input);
      Object response = function.apply(input);
      log.info("response: {}", response);
      return response;
    } catch (Exception e) {
      APIGatewayProxyResponseEvent response = errorHandler.resolve(e, (APIGatewayProxyRequestEvent) input);
      log.info("response error: {}", response);
      return response;
    }
  }

  public Class<?> inputType() {
    return inputType;
  }

  public Class<?> outputType() {
    return outputType;
  }

  public Function<Object, Object> function() {
    return function;
  }

  public String name() {
    return name;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (FunctionInfo) obj;
    return Objects.equals(this.inputType, that.inputType) &&
      Objects.equals(this.outputType, that.outputType) &&
      Objects.equals(this.function, that.function) &&
      Objects.equals(this.name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inputType, outputType, function, name);
  }

  @Override
  public String toString() {
    return "FunctionInfo[" +
      "inputType=" + inputType + ", " +
      "outputType=" + outputType + ", " +
      "function=" + function + ", " +
      "name=" + name + ']';
  }


}
