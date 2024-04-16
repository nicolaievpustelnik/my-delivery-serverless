package com.lib.support.rest.crud;

import com.lib.api.crud.jpa.AbstractJpaAdhocController;
import com.lib.api.crud.jpa.JpaAdhocController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/crud/adhoc")
@RestController
class JpaAdhocControllerImpl
  extends AbstractJpaAdhocController<MyRequest, MyResponse, String, MyCrudEntity, Long>
  implements
    JpaAdhocController<MyRequest, MyResponse, String, MyCrudEntity, Long> {}
