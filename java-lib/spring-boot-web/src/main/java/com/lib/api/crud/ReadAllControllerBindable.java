package com.lib.api.crud;

import javax.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Provide the endpoint to read the resources with pagination.
 * <br/>
 *
 * Your repository must implements the {@link PagingAndSortingRepository} interface and {@link org.springframework.data.repository.CrudRepository}.
 *
 * @param <TREQ> DTO request type
 * @param <TRES> DTO response type
 * @param <ID> DTO id type
 * @param <RT> Repository entity type
 * @param <RID> Repository entity identity type
 */
public interface ReadAllControllerBindable<TREQ, TRES, RT, RID>
  extends ReadAllController<TREQ, TRES, RT, RID> {

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  default Page<TRES> readAll(
    @Min(0) @RequestParam(name = "page", defaultValue = "0") int pageNumber,
    @Min(1) @RequestParam(
      name = "size",
      defaultValue = "${app.rest.bulk.response.size:100}"
    ) int pageSize
  ) {
    return ReadAllController.super.readAll(pageNumber, pageSize);
  }
}
