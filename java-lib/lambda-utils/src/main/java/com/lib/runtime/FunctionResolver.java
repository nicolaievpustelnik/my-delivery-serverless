package com.lib.runtime;

import com.lib.exception.custom.LambdaRuntimeException;
import com.lib.exception.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * The FunctionResolver class is responsible for resolving AWS Lambda functions
 * based on the given handler information. It caches the resolved function for
 * better performance.
 */
public class FunctionResolver {
  private static final Logger logger = LoggerFactory.getLogger(FunctionResolver.class);
  private final ApplicationContext applicationContext;
  private final ErrorHandler errorHandler;
  private FunctionInfo functionCache = null;

  public FunctionResolver(ApplicationContext applicationContext, ErrorHandler errorHandler) {
    this.applicationContext = applicationContext;
    this.errorHandler = errorHandler;
  }

  /**
   * Retrieves the AWS Lambda function based on the handler information. If the
   * function is already resolved and cached, the cached function is returned.
   *
   * @return A FunctionInfo object containing the resolved AWS Lambda function
   * and its input and output types.
   * @throws LambdaRuntimeException if any error occurs while resolving the
   *                                function.
   */
  public FunctionInfo recoverFunction() {
    if (functionCache != null) {
      logger.debug("Function {} already cached", Environment.getFunctionName());
      return functionCache;
    }

    Function<Object, Object> function = loadFunctionClass();
    Type[] typeArguments = getTypeArguments(function);

    try {
      functionCache = new FunctionInfo(
        Class.forName(typeArguments[0].getTypeName()),
        Class.forName(typeArguments[1].getTypeName()),
        function,
        function.getClass().getName(),
        errorHandler
      );
    } catch (ClassNotFoundException e) {
      logger.error("Error on load function class, error on get typeArguments", e);
    }


    return functionCache;
  }

  /**
   * Loads the Function class based on the handler information.
   *
   * @return The loaded Function class.
   * @throws LambdaRuntimeException if any error occurs while loading the class
   *                                or if the class does not implement the
   *                                Function interface.
   */
  @SuppressWarnings({"unchecked"})
  private Function<Object, Object> loadFunctionClass() {
    String functionName = Environment.getFunctionName();
    try {
      return applicationContext.getBean(functionName, Function.class);
    } catch (Exception e) {
      throw new LambdaRuntimeException("Error on load function class " + functionName, e);
    }
  }

  /**
   * Retrieves the type arguments (input and output types) of the given Function
   * class.
   *
   * @param function The Function class.
   * @return An array of Type objects representing the input and output types.
   * @throws LambdaRuntimeException if any error occurs while retrieving the type
   *                                arguments.
   */
  private Type[] getTypeArguments(Function<?, ?> function) {
    Type genericType = function.getClass().getGenericInterfaces()[0];
    if (genericType instanceof ParameterizedType parameterizedType) {
      return parameterizedType.getActualTypeArguments();
    }
    throw new LambdaRuntimeException("Error on getting type arguments for function class " + function.getClass().getName());
  }

  public FunctionInfo getFunctionCache() {
    return functionCache;
  }
}
