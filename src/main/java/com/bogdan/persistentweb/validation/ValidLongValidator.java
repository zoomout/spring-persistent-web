package com.bogdan.persistentweb.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidLongValidator implements ConstraintValidator<ValidLong, String> {

  @Override
  public void initialize(final ValidLong constraintAnnotation) {
  }

  @Override
  public boolean isValid(final String value, final ConstraintValidatorContext context) {
    try {
      long l = Long.parseLong(value);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }
}
