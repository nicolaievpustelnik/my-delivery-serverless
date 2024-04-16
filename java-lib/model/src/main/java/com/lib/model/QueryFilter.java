package com.lib.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class QueryFilter {

  private String field;
  private String value;
  private OperatorType operator;

  public String toString() {
    return field + " " + operator.getTextual() + " " + value;
  }
}
