package com.lib.support.rest.crud;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lib.api.crud.AbstractAwareCrudController;
import com.lib.api.crud.DeleteControllerBindable;

@RequestMapping("/v1/crud")
@RestController
class DeleteControllerImpl 
  extends AbstractAwareCrudController<MyRequest, MyResponse, String, MyCrudEntity, Long> 
  implements DeleteControllerBindable<MyRequest, MyResponse, String, MyCrudEntity, Long> {}

