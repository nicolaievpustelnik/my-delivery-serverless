package com.lib.api;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
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
 * Controller that extends from {@link CRUDJPAController} by overriding the getAll() method to use
 * filters. The fields to be filtered are reported as request parameters and the RHS notation is
 * used to identify the operator.
 * 
 * @deprecated Use {@link com.lib.api.crud.jpa.AbstractJpaAdhocCrudSameBodyController}
 * 
 * @param <T> Type of entity to be managed. Must implement {@link IdentifiableEntity}
 * @param <ID> Entity Identifier Type
 */
@Deprecated(forRemoval = true)
public abstract class BaseFilteredJPAController<T extends IdentifiableEntity<ID>, ID>
    extends BaseCRUDJPAController<T, ID> {

  @Autowired
  protected JpaRepositoryImplementation<T, ID> repositoryImpl;

  protected SpecificationParser<T> specificationParser = new SpecificationParser<>();

  @Autowired
  protected RHSColonQueryStringParser rHSColonQueryStringParser;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @Override
  protected ResponseEntity<List<T>> getAll(
      @RequestParam(required = false) Map<String, String> params) {

    return new ResponseEntity<>(
        (params.isEmpty()) ? this.repositoryImpl.findAll()
            : this.repositoryImpl
                .findAll(specificationParser.parse(rHSColonQueryStringParser.parse(params))),
        HttpStatus.OK);
  }
}
