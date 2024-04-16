package com.lib.support.rest.crud;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.lib.model.IdentifiableEntity;

import lombok.Data;

@Data
@Entity
public class MyCrudEntity implements IdentifiableEntity<Long> {

  @Id
  @GeneratedValue
  private Long id;

  @NotBlank
  private String name;

  @NotBlank
  private String email;

  @NotNull
  private LocalDate born;

  private Boolean active;

  private Float score;

}
