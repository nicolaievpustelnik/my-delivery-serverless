package com.lib.api.crud;

import com.lib.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

/**
 * Provide the default implementation to read the resource by id.
 *
 * @param <TREQ> DTO request type
 * @param <TRES> DTO response type
 * @param <ID> DTO id type
 * @param <RT> Repository entity type
 * @param <RID> Repository entity identity type
 */
public interface ReadController<TREQ, TRES, ID, RT, RID>
  extends
    RepositoryAware<RT, RID>,
    ConverterAware<ID, RID>,
    MapperAware<TREQ, TRES, RT> {
  Logger log = LoggerFactory.getLogger(ReadController.class);

  default ResponseEntity<TRES> readById(ID id) {
    log.debug("id to read {}", id);

    var entityId = converter().convert(id);
    log.debug("id converted to entity id: {}", entityId);

    var entity = repository()
      .findById(entityId)
      .orElseThrow(ResourceNotFoundException::new);
    log.debug("found entity {}", entity);

    var response = mapper().toResponse(entity);
    log.debug("mapped response {}", response);

    return ResponseEntity.ok().body(response);
  }
}
