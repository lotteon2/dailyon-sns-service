package com.dailyon.snsservice.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomSizeValidator implements ConstraintValidator<CustomSize, String> {

  private int min;
  private int max;

  @Override
  public void initialize(CustomSize constraintAnnotation) {
    this.min = constraintAnnotation.min();
    this.max = constraintAnnotation.max();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.trim().isEmpty()) {
      return true;
    }

    int length = value.length();
    return length >= min && length <= max;
  }
}
