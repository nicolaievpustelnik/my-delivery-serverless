package com.lib.support.rest.crud;

import java.time.LocalDate;

public record MyResponse(
  String id,
  String name,
  String email,
  LocalDate born,
  Boolean active,
  Float score
) {}
