package com.lib.api.crud;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Provide the endpoint to read the resource by id.
 * 
 * @param <TREQ> DTO request type
 * @param <TRES> DTO response type
 * @param <ID> DTO id type
 * @param <RT> Repository entity type
 * @param <RID> Repository entity identity type
 */
public interface ReadControllerBindable<TREQ, TRES, ID, RT, RID>
  extends ReadController<TREQ, TRES, ID, RT, RID> {

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  default ResponseEntity<TRES> readById(
    @PathVariable(name = "id", required = true) ID id
  ) {
    return ReadController.super.readById(id);
  }
}
