package com.bogdan.persistentweb.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class SerializationUtils {

  private static ObjectMapper objectMapper = new ObjectMapper()
      .setSerializationInclusion(JsonInclude.Include.NON_ABSENT)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  public static <T> String serialized(T entity) {
    try {
      return objectMapper.writeValueAsString(entity);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(String.format("An exception during serialization of '%s'", entity), e);
    }
  }

  public static <T> T deserialized(String value, Class<T> clazz) {
    try {
      return objectMapper.readValue(value, clazz);
    } catch (IOException e) {
      throw new RuntimeException(String.format("An exception during deserialization of '%s' as '%s'", value, clazz), e);
    }
  }

  public static <T> List<T> deserializedList(String value, Class<T> t) {
    try {
      return objectMapper.readValue(value, objectMapper.getTypeFactory().constructCollectionType(List.class, t));
    } catch (IOException e) {
      throw new RuntimeException(String.format("An exception during deserialization of '%s' as list of '%s'", value, t), e);
    }
  }
}
