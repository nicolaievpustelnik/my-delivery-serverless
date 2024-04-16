package com.lib.support.rest.crud;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record MyRequest(
  @NotBlank String name,
  @NotBlank String email,
  @NotNull LocalDate born
) {}
