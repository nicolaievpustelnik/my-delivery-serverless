package com.lib.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.lib.ResourceNotFoundException;
import com.lib.model.IdentifiableEntity;

/**
 * @deprecated Use {@link com.lib.api.crud.jpa.AbstractJpaAdhocProgrammaticCrudSameBodyController}
 */
@Deprecated(forRemoval = true)
public abstract class BaseSpecificationJPAController<T extends IdentifiableEntity<ID>, ID> {

  @Autowired	
  protected JpaRepositoryImplementation<T, ID> repository;

  protected abstract Specification<T> getSpecification();
  
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  protected ResponseEntity<List<T>> getAll(
      @RequestParam(required = false) Map<String, String> params) {

    return new ResponseEntity<>(repository.findAll(getSpecification()), HttpStatus.OK);
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  protected ResponseEntity<T> get(@PathVariable(name = "id", required = true) ID id) {

    Specification<T> specificationById = getSpecification().and((root, query,
        criteriaBuilder) -> criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), id)));

    List<T> result = repository.findAll(getSpecification().and(specificationById));

    if (result.isEmpty()) {
      throw new ResourceNotFoundException();
    } else {
      return ResponseEntity.ok(result.get(0));
    }
  }
}
