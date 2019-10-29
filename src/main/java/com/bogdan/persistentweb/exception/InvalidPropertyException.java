package com.bogdan.persistentweb.exception;

public class InvalidPropertyException extends RuntimeException {

  public InvalidPropertyException(final String name, final String value) {
    super("Invalid value '" + value + "' for property '" + name + "'");
  }

}
