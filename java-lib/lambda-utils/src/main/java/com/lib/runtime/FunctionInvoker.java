package com.lib.runtime;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.lib.exception.custom.LambdaRuntimeException;
import com.lib.utils.MapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StreamUtils;

import java.io.InputStream;
import java.io.OutputStream;

public class FunctionInvoker implements RequestStreamHandler {

  Logger log = LoggerFactory.getLogger(FunctionInvoker.class);
  private FunctionInfo functionInfo;
  private boolean started = false;

  public FunctionInvoker() {
    start();
  }

  void start() {
    try {
      Class<?> startClass = FunctionClassUtils.getStartClass();

      ConfigurableApplicationContext run = SpringApplication.run(startClass);

      this.functionInfo = run.getBean(FunctionInfo.class);
      started = true;
    } catch (Exception e) {
      throw new LambdaRuntimeException("Error on start", e);
    }
  }

  /**
   * Handles a Lambda Function request
   *
   * @param input   The Lambda Function input stream
   * @param output  The Lambda function output stream
   * @param context The Lambda execution environment context object.
   */
  @Override
  public void handleRequest(InputStream input, OutputStream output, Context context) {
    if (Environment.isCustomRuntime()) {
      log.info("is custom runtime");
      System.exit(0);
      return;
    }
    if (!started) {
      start();
    }
    try {
      Object inputFunction = MapperUtils.jsonToObject(input, functionInfo.inputType());

      Object outputFunction = functionInfo.apply(inputFunction);

      String outputJson = MapperUtils.objectToJson(outputFunction);

      StreamUtils.copy(outputJson.getBytes(), output);
    } catch (Exception e) {
      log.error("Error on handleRequest", e);
    }
  }


}
