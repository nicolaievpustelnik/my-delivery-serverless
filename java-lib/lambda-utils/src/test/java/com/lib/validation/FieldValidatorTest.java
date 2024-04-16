package com.lib.validation;

import com.lib.exception.custom.InvalidFieldsException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FieldValidatorTest {

  @InjectMocks
  private FieldValidator fieldValidator;

  @Test
  void testValidate_ValidObject_NoExceptionThrown() {
    // Given
    Validator validator = mock(Validator.class);
    ReflectionTestUtils.setField(fieldValidator, "validator", validator);
    Object object = new Object();

    // When
    fieldValidator.validate(object);

    // Then
    verify(validator, times(1)).validate(object);
  }

  @Test
  void testValidate_InvalidObject_ThrowsInvalidFieldsException() {
    // Given
    Validator validator = mock(Validator.class);
    ReflectionTestUtils.setField(fieldValidator, "validator", validator);

    Object object = new Object();
    Set<ConstraintViolation<Object>> constraintViolations = mock(Set.class);
    when(constraintViolations.isEmpty()).thenReturn(false);
    when(validator.validate(object)).thenReturn(constraintViolations);

    // When
    assertThrows(InvalidFieldsException.class, () -> fieldValidator.validate(object));

    // Then
    verify(validator, times(1)).validate(object);
  }

  @Test
  void testValidErrors_ValidObject_ReturnsEmptyOptional() {
    // Given
    Validator validator = mock(Validator.class);
    ReflectionTestUtils.setField(fieldValidator, "validator", validator);

    Object object = new Object();
    Set<ConstraintViolation<Object>> constraintViolations = mock(Set.class);
    when(constraintViolations.isEmpty()).thenReturn(true);
    when(validator.validate(object)).thenReturn(constraintViolations);

    // When
    Optional<String> errors = fieldValidator.validErros(object);

    // Then
    assertTrue(errors.isEmpty());
    verify(validator, times(1)).validate(object);
  }

  @Test
  void testValidErrors_InvalidObject_ReturnsErrorMessages() {
    // Given
    Validator validator = mock(Validator.class);
    ReflectionTestUtils.setField(fieldValidator, "validator", validator);
    Object object = new Object();
    Set<ConstraintViolation<Object>> constraintViolations = mock(Set.class);
    when(constraintViolations.isEmpty()).thenReturn(false);
    when(validator.validate(object)).thenReturn(constraintViolations);
    ConstraintViolation<Object> violation1 = mock(ConstraintViolation.class);
    ConstraintViolation<Object> violation2 = mock(ConstraintViolation.class);
    when(violation1.getMessage()).thenReturn("Error 1");
    when(violation2.getMessage()).thenReturn("Error 2");
    when(constraintViolations.stream()).thenReturn(Stream.of(violation1, violation2));

    // When
    Optional<String> errors = fieldValidator.validErros(object);

    // Then
    assertTrue(errors.isPresent());
    assertEquals("Error 1,Error 2", errors.get());
    verify(validator, times(1)).validate(object);
  }
}
