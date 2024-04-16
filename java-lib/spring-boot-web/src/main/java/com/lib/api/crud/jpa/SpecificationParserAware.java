package com.lib.api.crud.jpa;

import com.lib.jpa.util.SpecificationParser;

public interface SpecificationParserAware<T> {
  
  SpecificationParser<T> parser();

}
