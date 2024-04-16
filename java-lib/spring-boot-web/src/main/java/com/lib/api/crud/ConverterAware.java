package com.lib.api.crud;

import org.springframework.core.convert.converter.Converter;

public interface ConverterAware<S, T> {
  Converter<S, T> converter();
}
