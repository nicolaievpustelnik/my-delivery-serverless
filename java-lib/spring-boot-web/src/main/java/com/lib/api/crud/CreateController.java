package com.lib.api.crud;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Provide default implementation to read the resource by id.
 * 
 * @param <TREQ> DTO request type
 * @param <TRES> DTO response type
 * @param <ID> DTO id type
 * @param <RT> Repository entity type
 * @param <RID> Repository entity identity type
 */
public interface CreateController<TREQ, TRES, ID, RT, RID>
  extends RepositoryAware<RT, RID>, MapperAware<TREQ, TRES, RT> {
  Logger log = LoggerFactory.getLogger(CreateController.class);

  default ResponseEntity<TRES> create(@Valid @RequestBody TREQ req) {
    log.debug("request body {}", req);

    var entity = mapper().toEntity(req);
    log.debug("mapped entity {}", entity);

    entity = repository().save(entity);
    log.debug("saved entity {}", entity);

    var res = mapper().toResponse(entity);
    log.debug("response body {}", res);

    return new ResponseEntity<>(res, HttpStatus.OK);
  }
}
