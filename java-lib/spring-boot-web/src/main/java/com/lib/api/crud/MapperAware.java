package com.lib.api.crud;

public interface MapperAware<TREQ, TRES, E> {

  CrudMapper<TREQ, TRES, E> mapper();

}
