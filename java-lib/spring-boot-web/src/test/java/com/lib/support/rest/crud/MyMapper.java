package com.lib.support.rest.crud;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.lib.api.crud.CrudMapper;

@Mapper(componentModel = "spring")
public interface MyMapper extends CrudMapper<MyRequest, MyResponse, MyCrudEntity> {

  @Mapping(target = "id", source = "id", qualifiedByName = "asString")
  MyResponse toResponse(MyCrudEntity entity);

  @Named("asString")
  public static String asString(Long value) {
    return String.valueOf(value);
  }
  
  @Named("asLong")
  public static Long asLong(String value){
    return Long.valueOf(value);
  }
}
