package com.bogdan.persistentweb.utils;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class GenericTestData {

  public static Stream<Arguments> invalidPayloadData() {
    return Stream.of(
        Arguments.of("empty object", "{}"),
        Arguments.of("empty string", ""),
        Arguments.of("absent title", "{\"id\":1}")
    );
  }

  public static Stream<Arguments> leadingAndTrailingSpaceData() {
    return Stream.of(
        Arguments.of(" leadingSpace"),
        Arguments.of("trailingSpace ")
    );
  }
}
