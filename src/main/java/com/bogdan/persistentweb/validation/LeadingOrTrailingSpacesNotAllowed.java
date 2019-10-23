package com.bogdan.persistentweb.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LeadingOrTrailingSpacesNotAllowedValidator.class)
public @interface LeadingOrTrailingSpacesNotAllowed {

  String message() default ErrorMessages.LEADING_TRAILING_SPACES;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
