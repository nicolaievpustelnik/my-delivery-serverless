package com.lib.support.rest.crud;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lib.api.crud.AbstractAwareCrudController;
import com.lib.api.crud.ReadAllControllerBindable;

@RequestMapping("/v1/crud")
@RestController
class ReadAllControllerImpl 
  extends AbstractAwareCrudController<MyRequest, MyResponse, String, MyCrudEntity, Long>
  implements ReadAllControllerBindable<MyRequest, MyResponse, MyCrudEntity, Long> {}
