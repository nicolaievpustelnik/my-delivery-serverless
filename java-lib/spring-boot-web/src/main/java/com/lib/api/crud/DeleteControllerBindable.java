package com.lib.api.crud;

import com.lib.model.IdentifiableEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface DeleteControllerBindable<
  TREQ, TRES, ID, RT extends IdentifiableEntity<RID>, RID
>
  extends DeleteController<TREQ, TRES, ID, RT, RID> {
  
  @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  default ResponseEntity<Void> deleteById(
    @PathVariable(name = "id", required = true) ID id
  ) {
    return DeleteController.super.deleteById(id);
  }
}
