package com.lib.api.crud;

import com.lib.ResourceNotFoundException;
import com.lib.model.IdentifiableEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface UpdateController<
  TREQ, TRES, ID, RT extends IdentifiableEntity<RID>, RID
>
  extends
    RepositoryAware<RT, RID>,
    MapperAware<TREQ, TRES, RT>,
    ConverterAware<ID, RID> {
  Logger log = LoggerFactory.getLogger(UpdateController.class);

  default ResponseEntity<Void> update(ID id, TREQ req) {
    log.debug("id to update {}", id);
    log.debug("body to update {}", req);

    var entityId = converter().convert(id);
    log.debug("id converted to entity id: {}", entityId);

    return repository()
      .findById(entityId)
      .map(found -> req)
      .map(mapper()::toEntity)
      .map(e -> {
        e.setId(entityId);
        return e;
      })
      .map(repository()::save)
      .map(safe -> new ResponseEntity<Void>(HttpStatus.NO_CONTENT))
      .orElseThrow(ResourceNotFoundException::new);
  }
}
