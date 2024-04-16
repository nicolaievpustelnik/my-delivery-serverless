package com.lib.support;

import com.lib.shoji.ShojiToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FoobarController {

  @GetMapping("/test/shoji")
  public String read(
    @RequestHeader(name = ShojiToken.HEADER, required = false) ShojiToken token
  ) {
    save(token);

    return "/test/shoji/";
  }

  // to allow the capture of actual token
  public void save(ShojiToken token) {}
}
