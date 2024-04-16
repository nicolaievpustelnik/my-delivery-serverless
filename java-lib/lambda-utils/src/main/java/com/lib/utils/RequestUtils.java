package com.lib.utils;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lib.exception.custom.ParameterException;

import java.util.Objects;

/**
 * The {@code RequestUtils} class is a utility class that provides two static methods one for parsing JSON bodies and the other for extracting parameters from an APIGatewayProxyRequestEvent object.
 * It uses the {@code ObjectMapper} class from the Jackson library to convert JSON to Java objects.
 * The {@code RequestUtils} class can be used to extract information from an APIGatewayProxyRequestEvent object and convert it to Java objects.
 */
public final class RequestUtils {

  /**
   * Creates a new instance of the {@code ObjectMapper} to convert JSON to Java objects.
   */
  private static final ObjectMapper objectMapper = MapperUtils.getObjectMapper();

  /**
   * Constructs a new {@code RequestUtils}. This method is private, which means it cannot be instantiated from outside the class.
   * This is because it is a utility class that provides only static methods.
   */
  private RequestUtils() {
  }

  /**
   * This method uses the {@code ObjectMapper} class from the Jackson library to convert the JSON body of the request to a Java object of the specified class.
   * The method readValue() del {@code ObjectMapper} is called to parse the JSON body obtained by calling the getBody() method of the request event object.
   * The type of object to parse is specified by the clazz parameter.
   * If the JSON cannot be parsed properly, a JsonProcessingException is thrown.
   *
   * @param requestEvent APIGatewayProxyRequestEvent
   * @param clazz        Class<T>
   * @return a new instance of that class
   */
  public static <T> T parseJsonBody(APIGatewayProxyRequestEvent requestEvent, Class<T> clazz) throws JsonProcessingException {
    return objectMapper.readValue(requestEvent.getBody(), clazz);
  }

  /**
   * This method takes an APIGatewayProxyRequestEvent object and a parameter name and returns the value of the parameter with the given name from the path parameters of the request event.
   * It calls the getPathParameters().get(parameterName) method of the requestEvent object to retrieve the value of the given parameter name.
   * If the parameter is not found or if an error occurs while trying to get the parameter value, it throws a {@code ParameterException}.
   *
   * @param requestEvent  APIGatewayProxyRequestEvent
   * @param parameterName name of the parameter that is needed
   * @return String the value of the parameter
   */
  public static String extractParameter(APIGatewayProxyRequestEvent requestEvent, String parameterName) {
    try {
      String parameterValue = requestEvent.getPathParameters().get(parameterName);
      if (Objects.isNull(parameterValue)) {
        throw new ParameterException("Parameter not found: " + parameterName);
      }

      return parameterValue;
    } catch (Exception e) {
      throw new ParameterException("Error on try get parameter: " + parameterName);

    }
  }
}
