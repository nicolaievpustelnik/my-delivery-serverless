package com.lib.api.crud;

import com.lib.model.IdentifiableEntity;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface UpdateControllerBindable<
  TREQ, TRES, ID, RT extends IdentifiableEntity<RID>, RID
>
  extends UpdateController<TREQ, TRES, ID, RT, RID> {

  @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  default ResponseEntity<Void> update(
    @PathVariable(name = "id", required = true) ID id,
    @Valid @RequestBody TREQ req
  ) {
    return UpdateController.super.update(id, req);
  }
}
