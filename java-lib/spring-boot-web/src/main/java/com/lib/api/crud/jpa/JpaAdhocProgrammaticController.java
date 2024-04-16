package com.lib.api.crud.jpa;

import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Adhoc Jpa query using programmatic {@link org.springframework.data.jpa.domain.Specification}.
 * and custom request filters.
 *
 * <p>
 * Warning! Que queries may be slow due the lack of indexes for the attributes.
 * </p>
 * <p>
 * Your repository must implements the {@link JpaRepositoryImplementation} interface.
 * </p>
 *
 * @param <TREQ> DTO request type
 * @param <TRES> DTO response type
 * @param <ID> DTO id type
 * @param <RT> Repository entity type
 * @param <RID> Repository entity identity type
 */
public interface JpaAdhocProgrammaticController<TREQ, TRES, ID, RT, RID>
  extends JpaAdhocController<TREQ, TRES, ID, RT, RID> {
  /**
   * Your custom programmatic filter.
   */
  Specification<RT> specification();

  @Override
  default Page<TRES> readAllByFilter(
    @RequestParam(required = false) Map<String, String> filter,
    @RequestParam(name = "@page", defaultValue = "0") int pageNumber,
    @RequestParam(
      name = "@size",
      defaultValue = "${app.rest.bulk.response.size:100}"
    ) int pageSize
  ) {
    log.debug("page: {}, size:{}", pageNumber, pageSize);

    var page = PageRequest.of(pageNumber, pageSize);

    var query = rhs().sanitize(filter);
    log.debug("adhoc filter {}", query);

    if (query.isEmpty()) {
      log.debug("no filter for adhoc filtering");
      return this.<JpaRepositoryImplementation<RT, RID>>repository()
        .findAll(specification(), page)
        .map(mapper()::toResponse);
    }

    return this.<JpaRepositoryImplementation<RT, RID>>repository()
      .findAll(specification().and(parser().parse(rhs().parse(filter))), page)
      .map(mapper()::toResponse);
  }
}
