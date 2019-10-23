package com.bogdan.persistentweb.utils;

import java.net.URI;
import java.net.URISyntaxException;

public class UriUtil {

  public static URI createUri(String uri) {
    try {
      return new URI(uri);
    } catch (URISyntaxException e) {
      throw new RuntimeException(String.format("Invalid URI '%s'", uri), e);
    }
  }
}
