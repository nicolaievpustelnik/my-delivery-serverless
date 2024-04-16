package com.lib.runtime;

import com.lib.exception.AbstractExceptionHandler;
import com.lib.exception.ErrorHandler;
import com.lib.exception.ProblemDetailResolver;
import com.lib.exception.handlers.InvalidFieldExceptionHandler;
import com.lib.exception.handlers.JsonMappingExceptionHandler;
import com.lib.exception.handlers.ParameterNotFoundExceptionHandler;
import com.lib.validation.FieldValidator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfiguration
public class LambdaUtilAutoConfiguration {

  @Bean
  public FieldValidator fieldValidator() {
    return new FieldValidator();
  }

  @Bean
  public ProblemDetailResolver problemDetailResolver(List<AbstractExceptionHandler<? extends Throwable>> exceptionHandlers) {
    exceptionHandlers.add(new InvalidFieldExceptionHandler());
    exceptionHandlers.add(new JsonMappingExceptionHandler());
    exceptionHandlers.add(new ParameterNotFoundExceptionHandler());
    return new ProblemDetailResolver(exceptionHandlers);
  }

  @Bean
  public ErrorHandler errorHandler(ProblemDetailResolver problemDetailResolver) {
    return new ErrorHandler(problemDetailResolver);
  }

  @Bean
  public FunctionResolver functionResolver(ApplicationContext applicationContext, ErrorHandler errorHandler) {
    return new FunctionResolver(applicationContext, errorHandler);
  }

  @Bean
  public FunctionInfo functionInfo(FunctionResolver functionResolver) {
    return functionResolver.recoverFunction();
  }

  @Bean
  public AwsHttpPropagation awsHttpPropagation() {
    return new AwsHttpPropagation();
  }

  @Bean(name = "lambdaRuntime")
  public SmartLifecycle smartLifecycle(AwsHttpPropagation awsHttpPropagation, FunctionInfo functionInfo) {
    return new LambdaRuntime(awsHttpPropagation, functionInfo);
  }
}
