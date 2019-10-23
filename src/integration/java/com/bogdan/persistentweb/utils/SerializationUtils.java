package com.bogdan.persistentweb.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class SerializationUtils {

  private static ObjectMapper objectMapper = new ObjectMapper()
      .setSerializationInclusion(JsonInclude.Include.NON_ABSENT)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  public static <T> String serialized(T createdProduct) throws JsonProcessingException {
    return objectMapper.writeValueAsString(createdProduct);
  }

  public static <T> T deserialized(String value, Class<T> clazz) throws IOException {
    return objectMapper.readValue(value, clazz);
  }
}
