package com.bogdan.persistentweb.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidLongValidator.class)
public @interface ValidLong {

  String message() default ErrorMessages.VALID_LONG;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
