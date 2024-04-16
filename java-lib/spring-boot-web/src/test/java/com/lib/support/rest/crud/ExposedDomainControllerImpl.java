package com.lib.support.rest.crud;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lib.api.crud.AbstractCrudExposedDomainControllerBindable;

@RequestMapping("/v1/crud/exposed")
@RestController
class ExposedDomainControllerImpl
  extends AbstractCrudExposedDomainControllerBindable<MyCrudEntity, Long> {}
