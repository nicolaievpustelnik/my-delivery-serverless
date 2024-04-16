package com.lib.jpa.util;

import com.lib.model.QueryFilter;

@FunctionalInterface
public interface ValueParser<Y extends Comparable<? super Y>> {

  Y parse(QueryFilter filter, Class<? extends Object> fieldType, String value);

}
