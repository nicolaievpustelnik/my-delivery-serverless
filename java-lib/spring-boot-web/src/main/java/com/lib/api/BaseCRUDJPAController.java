package com.lib.api;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.lib.ResourceNotFoundException;
import com.lib.model.IdentifiableEntity;

/**
 * Controller that implements the basic operations of CRUD (Create, Read, Update and Remove) of
 * entities using JPA. It is essential that the entity implements the {@link IdentifiableEntity}
 * interface
 * 
 * @deprecated Use {@link com.lib.api.crud.AbstractCrudSameBodyController}
 * 
 * @param <T> Type of entity to be managed. Must implement {@link IdentifiableEntity}
 * @param <ID> Entity Identifier Type
 */
@Deprecated(forRemoval = true)
public abstract class BaseCRUDJPAController<T extends IdentifiableEntity<ID>, ID> {

  @Autowired
  protected JpaRepository<T, ID> repository;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  protected ResponseEntity<List<T>> getAll(
      @RequestParam(required = false) Map<String, String> params) {

    return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  protected ResponseEntity<T> get(@PathVariable(name = "id", required = true) ID id) {

    return repository.findById(id).map(ResponseEntity::ok)
        .orElseThrow(ResourceNotFoundException::new);
  }

  @PostMapping(path = {"", "/"}, produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  protected ResponseEntity<T> post(@Valid @RequestBody T req) throws Exception {
    return new ResponseEntity<>(repository.save(req), HttpStatus.OK);
  }

  @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  protected ResponseEntity<Void> put(@PathVariable(name = "id", required = true) ID id,
      @Valid @RequestBody T req) {

    req.setId(id);
    return repository.findById(id)
        .map(found -> req)
        .map(repository::save)
        .map(safe -> new ResponseEntity<Void>(HttpStatus.NO_CONTENT))
        .orElseThrow(ResourceNotFoundException::new);
  }

  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  protected ResponseEntity<Void> delete(@PathVariable(name = "id", required = true) ID id) {

    return repository.findById(id).map(entity -> {
      repository.delete(entity);
      return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }).orElseThrow(ResourceNotFoundException::new);
  }
}
