package com.lib.runtime;

import org.springframework.util.StringUtils;

public final class Environment {

  public static final String USER_AGENT_VALUE = String.format(
    "spring-cloud-function/%s-%s",
    System.getProperty("java.runtime.version"),
    "1.0.0");
  public static final String LAMBDA_VERSION_DATE = "2018-06-01";
  public static final String LAMBDA_ERROR_URL_TEMPLATE = "http://{0}/{1}/runtime/invocation/{2}/error";
  public static final String LAMBDA_RUNTIME_URL_TEMPLATE = "http://{0}/{1}/runtime/invocation/next";
  public static final String LAMBDA_INVOCATION_URL_TEMPLATE = "http://{0}/{1}/runtime/invocation/{2}/response";
  public static final String AWS_LAMBDA_RUNTIME_API = "AWS_LAMBDA_RUNTIME_API";
  public static final String LAMBDA_RUNTIME_AWS_REQUEST_ID = "Lambda-Runtime-Aws-Request-Id";
  public static final String FUNCTION_BEAN_NAME = "FUNCTION_BEAN_NAME";

  private Environment() {
  }

  public static String getFunctionName() {
    String functionName = System.getenv(FUNCTION_BEAN_NAME);
    if (functionName == null) {
      functionName = System.getProperty(FUNCTION_BEAN_NAME);
    }
    return functionName;
  }

  public static String getRuntimeApi() {
    String awsLambdaRuntimeApi = System.getenv(AWS_LAMBDA_RUNTIME_API);
    if (awsLambdaRuntimeApi == null) {
      awsLambdaRuntimeApi = System.getProperty(AWS_LAMBDA_RUNTIME_API);
    }
    return awsLambdaRuntimeApi;
  }

  public static boolean isCustomRuntime() {
    if (!StringUtils.hasText(System.getenv("AWS_EXECUTION_ENV"))) {
      return true;
    }
    return !System.getenv("AWS_EXECUTION_ENV").contains("java");
  }
}
