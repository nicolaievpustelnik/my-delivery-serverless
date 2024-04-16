package com.lib.api.crud;

/**
 * @param <TREQ> Request type
 * @param <TRES> Response type
 * @param <E> Entity type
 */
public interface CrudMapper<TREQ, TRES, E> {
  
  E toEntity(TREQ request);

  TRES toResponse(E entity);
}
