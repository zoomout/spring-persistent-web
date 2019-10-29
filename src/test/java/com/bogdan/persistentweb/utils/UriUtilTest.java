package com.bogdan.persistentweb.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

class UriUtilTest {

  @Test
  void validUri() {
    URI uri = UriUtil.createUri("/valid/uri");
    assertThat(uri, notNullValue());
  }

  @Test
  void invalidUriThrowsAnException() {
    Assertions.assertThrows(RuntimeException.class, () -> {
      UriUtil.createUri("%invalid");
    });
  }

}
