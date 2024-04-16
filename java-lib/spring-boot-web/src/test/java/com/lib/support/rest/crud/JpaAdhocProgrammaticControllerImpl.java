package com.lib.support.rest.crud;

import com.lib.api.crud.jpa.AbstractJpaAdhocController;
import com.lib.api.crud.jpa.JpaAdhocProgrammaticController;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/crud/adhoc/programmatic")
@RestController
class JpaAdhocProgrammaticControllerImpl
  extends AbstractJpaAdhocController<MyRequest, MyResponse, String, MyCrudEntity, Long>
  implements
    JpaAdhocProgrammaticController<MyRequest, MyResponse, String, MyCrudEntity, Long> {

  @Override
  public Specification<MyCrudEntity> specification() {
    // even ids
    return (root, query, builder) ->
      builder.equal(builder.mod(root.get("id"), 2), 0L);
  }
}
