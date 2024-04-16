package com.lib.support.rest.crud;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class StringToLongConverter implements Converter<String, Long> {

  @Override
  @Nullable
  public Long convert(String source) {
    return Long.valueOf(source);
  }
  
}
