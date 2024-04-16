package com.lib.support;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lib.api.BaseFilteredJPAController;

@RestController
@RequestMapping("/test")
public class BaseJPAControllerImpl
  extends BaseFilteredJPAController<MyEntity, Integer> {}
