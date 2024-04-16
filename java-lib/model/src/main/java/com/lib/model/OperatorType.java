package com.lib.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperatorType {

  eq("equals", "equals"),
  lt("less than", "less-than"),
  lte("less than or equal to", "less-than-or-equal-to"),
  gt("greater than", "greater-than"),
  gte("greater than or equal to", "greate-than-or-equal-to");

  private String textual;
  private String label; 
}
