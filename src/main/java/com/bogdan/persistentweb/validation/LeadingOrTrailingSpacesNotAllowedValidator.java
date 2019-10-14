package com.bogdan.persistentweb.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LeadingOrTrailingSpacesNotAllowedValidator implements ConstraintValidator<LeadingOrTrailingSpacesNotAllowed, String> {
    @Override
    public void initialize(final LeadingOrTrailingSpacesNotAllowed constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        return value == null || !(value.startsWith(" ") || value.endsWith(" "));
    }
}
