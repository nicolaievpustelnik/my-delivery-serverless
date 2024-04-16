package com.lib.api.crud;

import com.lib.ResourceNotFoundException;
import com.lib.model.IdentifiableEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Provide default implementation to delete the resource by id.
 */
public interface DeleteController<
  TREQ, TRES, ID, RT extends IdentifiableEntity<RID>, RID
>
  extends
    RepositoryAware<RT, RID>,
    MapperAware<TREQ, TRES, RT>,
    ConverterAware<ID, RID> {
  Logger log = LoggerFactory.getLogger(DeleteController.class);

  default ResponseEntity<Void> deleteById(
    @PathVariable(name = "id", required = true) ID id
  ) {
    log.debug("id to delete {}", id);

    var entityId = converter().convert(id);
    log.debug("id converted to entity id: {}", entityId);

    return repository()
      .findById(entityId)
      .map(entity -> {
        repository().delete(entity);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
      })
      .orElseThrow(ResourceNotFoundException::new);
  }
}
