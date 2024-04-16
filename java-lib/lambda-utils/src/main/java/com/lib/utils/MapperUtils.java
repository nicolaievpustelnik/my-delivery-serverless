package com.lib.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.InputStream;

public final class MapperUtils {

  private static final ObjectMapper objectMapper = new ObjectMapper()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .registerModule(new JavaTimeModule());

  private MapperUtils() {
  }

  public static ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  public static <T> String objectToJson(T object) throws JsonProcessingException {
    return objectMapper.writeValueAsString(object);
  }

  public static <T> T jsonToObject(String json, Class<T> clazz) throws JsonProcessingException {
    return objectMapper.readValue(json, clazz);
  }

  public static <T> T jsonToObject(InputStream json, Class<T> clazz) throws IOException {
    return objectMapper.readValue(json, clazz);
  }

}
