package com.lib.support.rest.crud;

import com.lib.api.crud.AbstractAwareCrudController;
import com.lib.api.crud.ReadControllerBindable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/crud")
@RestController
class ReadControllerImpl
  extends AbstractAwareCrudController<MyRequest, MyResponse, String, MyCrudEntity, Long>
  implements ReadControllerBindable<MyRequest, MyResponse, String, MyCrudEntity, Long> {}

