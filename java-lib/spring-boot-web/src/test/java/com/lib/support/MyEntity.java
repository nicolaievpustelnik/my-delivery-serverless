package com.lib.support;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import com.lib.model.IdentifiableEntity;

@Entity
public class MyEntity implements IdentifiableEntity<Integer> {

  @Id
  private Integer id;

  @NotBlank
  private String name;

  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public void setId(Integer id) {
    this.id = Objects.requireNonNull(id);
  }

  public String getName(){
    return name;
  }
  
  public void setName(String name){
    this.name = name;
  }

  @Override
  public boolean equals(Object other){
    if(other instanceof MyEntity){
      return this.id.equals(((MyEntity)other).id);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return this.id.hashCode();
  }

  public String toString() {
    return "id=" + id + ", name=" + name;
  }
}
