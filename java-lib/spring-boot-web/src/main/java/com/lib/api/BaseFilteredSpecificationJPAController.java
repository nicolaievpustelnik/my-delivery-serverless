package com.lib.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.lib.jpa.util.SpecificationParser;
import com.lib.model.IdentifiableEntity;
import com.lib.web.util.RHSColonQueryStringParser;

/**
 * @deprecated Use {@link com.lib.api.crud.jpa.AbstractJpaAdhocProgrammaticCrudSameBodyController}
 */
@Deprecated(forRemoval = true)
public abstract class BaseFilteredSpecificationJPAController<T extends IdentifiableEntity<ID>, ID>
    extends BaseSpecificationJPAController<T, ID> {

  @Autowired
  SpecificationParser<T> specificationParser;

  @Autowired
  RHSColonQueryStringParser rHSColonQueryStringParser;

  @Override
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  protected ResponseEntity<List<T>> getAll(
      @RequestParam(required = false) Map<String, String> params) {

    var specification = (params.isEmpty()) ? getSpecification()
        : getSpecification()
            .and(specificationParser.parse(rHSColonQueryStringParser.parse(params)));

    return new ResponseEntity<>(repository.findAll(specification), HttpStatus.OK);
  }
}
