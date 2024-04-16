package com.lib.api.crud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Provide the default implementation to read the resources with pagination.
 * <br/>
 *
 * Your repository must implements the {@link PagingAndSortingRepository} interface and {@link org.springframework.data.repository.CrudRepository}.
 *
 * @param <TREQ> DTO request type
 * @param <TRES> DTO response type
 * @param <ID> DTO id type
 * @param <RT> Repository entity type
 * @param <RID> Repository entity identity type
 */
public interface ReadAllController<TREQ, TRES, RT, RID>
  extends RepositoryAware<RT, RID>, MapperAware<TREQ, TRES, RT> {
  Logger log = LoggerFactory.getLogger(ReadAllController.class);

  default Page<TRES> readAll(int pageNumber, int pageSize) {
    log.debug("page: {}, size:{}", pageNumber, pageSize);

    return this.<PagingAndSortingRepository<RT, RID>>repository()
      .findAll(PageRequest.of(pageNumber, pageSize))
      .map(mapper()::toResponse);
  }
}
