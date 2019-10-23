package com.bogdan.persistentweb.exception;

public class EntityNotFoundException extends RuntimeException {

  public EntityNotFoundException(String entityName, String id) {
    super(String.format("Entity '%s' with id '%s' not found", entityName, id));
  }
}
