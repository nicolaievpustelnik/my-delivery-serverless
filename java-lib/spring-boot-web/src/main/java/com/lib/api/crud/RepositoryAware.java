package com.lib.api.crud;

import org.springframework.data.repository.CrudRepository;

public interface RepositoryAware<T, ID> {

  <R extends CrudRepository<T, ID>> R repository();

}
