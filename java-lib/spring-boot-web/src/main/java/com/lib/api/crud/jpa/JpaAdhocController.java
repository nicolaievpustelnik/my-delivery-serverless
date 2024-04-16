package com.lib.api.crud.jpa;

import com.lib.api.crud.MapperAware;
import com.lib.api.crud.RepositoryAware;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Adhoc Jpa queries using RHS Colon pattern.
 * <p>
 * Warning! Que queries may be slow due the lack of indexes for the attributes.
 * </p>
 * <p>
 * Your repository must implements the {@link JpaRepositoryImplementation} interface.
 * </p>
 * <p>
 * To avoid name collision, the params for pagination are <code>@page</code> for page number and <code>@size</code> for page size.
 * </p>
 *
 * @param <TREQ> DTO request type
 * @param <TRES> DTO response type
 * @param <ID> DTO id type
 * @param <RT> Repository entity type
 * @param <RID> Repository entity identity type
 */
public interface JpaAdhocController<TREQ, TRES, ID, RT, RID>
  extends
    RepositoryAware<RT, RID>,
    SpecificationParserAware<RT>,
    RHSParserAware,
    MapperAware<TREQ, TRES, RT> {
  Logger log = LoggerFactory.getLogger(JpaAdhocController.class);

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  default Page<TRES> readAllByFilter(
    @RequestParam(required = false) final Map<String, String> filter,
    @RequestParam(name = "@page", defaultValue = "0") final int pageNumber,
    @RequestParam(
      name = "@size",
      defaultValue = "${app.rest.bulk.response.size:100}"
    ) final int pageSize
  ) {
    log.debug("page: {}, size:{}", pageNumber, pageSize);

    var page = PageRequest.of(pageNumber, pageSize);

    var query = rhs().sanitize(filter);
    log.debug("adhoc filter {}", query);

    if (query.isEmpty()) {
      log.debug("no filter for adhoc filtering");
      return this.<JpaRepositoryImplementation<RT, RID>>repository()
        .findAll(page)
        .map(mapper()::toResponse);
    }

    return this.<JpaRepositoryImplementation<RT, RID>>repository()
      .findAll(parser().parse(rhs().parse(filter)), page)
      .map(mapper()::toResponse);
  }
}
