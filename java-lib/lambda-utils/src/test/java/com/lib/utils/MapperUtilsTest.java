package com.lib.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

class MapperUtilsTest {

  @Test
  void testObjectToJson() throws JsonProcessingException {
    // Given
    Map<String, String> dcmInstance = createDcmInstance();

    // When
    String json = MapperUtils.objectToJson(dcmInstance);

    // Then
    Assertions.assertNotNull(json);
    Assertions.assertFalse(json.isEmpty());
  }

  @Test
  void testJsonToObject() throws JsonProcessingException {
    // Given
    Map<String, String> expectedDcmInstance = createDcmInstance();
    String json = MapperUtils.objectToJson(expectedDcmInstance);

    // When
    Map<String, String> actualDcmInstance = MapperUtils.jsonToObject(json, Map.class);

    // Then
    Assertions.assertEquals(expectedDcmInstance, actualDcmInstance);
  }

  @Test
  void testJsonToObjectWithInputStream() throws IOException {
    // Given
    Map<String, String> expectedDcmInstance = createDcmInstance();
    String json = MapperUtils.objectToJson(expectedDcmInstance);

    InputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

    // When
    Map<String, String> actualDcmInstance = MapperUtils.jsonToObject(inputStream, Map.class);

    // Then
    Assertions.assertEquals(expectedDcmInstance, actualDcmInstance);
  }

  private Map<String, String> createDcmInstance() {
    return Map.of("teste", "teste");
  }
}
