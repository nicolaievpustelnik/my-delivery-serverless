package com.lib.validation;

import com.lib.exception.custom.InvalidFieldsException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The {@code FieldValidator} class is responsible for the validations of the fields using a {@code Validator} class object.
 * This class uses a Set of ConstraintViolation to represent errors found for an object.
 * In case of errors, it throws a {@code InvalidFieldsException}
 */
public class FieldValidator {

  /**
   * An instance of the Validator class that later will be initialized by the getValidator() method of the default ValidatorFactory.
   */
  private Validator validator = null;

  private Validator getValidator() {
    if (Objects.nonNull(validator)) {
      return validator;
    }
    try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      this.validator = validatorFactory.getValidator();
    }
    return validator;
  }

  /**
   * This method validates if there were any errors returned by validErros() method by checking if the Optional<String> is present or not.
   * If it is present, throws an InvalidFieldsException with the error message retrieved from the Optional.
   *
   * @param object Object
   */
  public void validate(Object object) {
    validator = getValidator();
    Optional<String> erros = validErros(object);
    if (erros.isPresent()) {
      throw new InvalidFieldsException(erros.get());
    }
  }

  /**
   * This method takes an object as a parameter and returns an Optional<String> containing any validation errors found for that object.
   * First, calls the method validate of the validator instance passing the object and returns a Set of ConstraintViolation objects
   * representing the validation errors found for the object. Later, checks if the ConstraintViolation set is empty.
   * If it is, returns an empty Optional. If not, maps each ConstraintViolation object to its error message using the getMessage() method.
   *
   * @param object Object to validate
   * @return Optional containing the error messages
   */
  Optional<String> validErros(Object object) {
    Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);

    if (constraintViolations.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(constraintViolations.stream()
      .map(ConstraintViolation::getMessage)
      .collect(Collectors.joining(",")));
  }

}
    
