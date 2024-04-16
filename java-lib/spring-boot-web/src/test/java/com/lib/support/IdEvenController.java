package com.lib.support;

import com.lib.api.BaseFilteredSpecificationJPAController;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/even")
public class IdEvenController
  extends BaseFilteredSpecificationJPAController<MyEntity, Integer> {

  @Override
  protected Specification<MyEntity> getSpecification() {
    return (root, query, criteriaBuilder) ->
      criteriaBuilder.equal(criteriaBuilder.mod(root.get("id"), 2), 0);
  }
}
