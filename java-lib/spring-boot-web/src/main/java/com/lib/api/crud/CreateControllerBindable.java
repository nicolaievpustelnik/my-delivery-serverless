package com.lib.api.crud;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
public interface CreateControllerBindable<TREQ, TRES, ID, RT, RID>
  extends CreateController<TREQ, TRES, ID, RT, RID> {
  
  @PostMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseStatus(HttpStatus.OK)
  default ResponseEntity<TRES> create(@Valid @RequestBody TREQ req) {
    return CreateController.super.create(req);
  }
}
