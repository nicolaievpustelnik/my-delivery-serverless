package com.lib.support.rest.crud;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lib.api.crud.AbstractAwareCrudController;
import com.lib.api.crud.UpdateControllerBindable;

@RequestMapping("/v1/crud")
@RestController
class UpdateControllerImpl 
  extends AbstractAwareCrudController<MyRequest, MyResponse, String, MyCrudEntity, Long>
  implements UpdateControllerBindable<MyRequest, MyResponse, String, MyCrudEntity, Long> {}

