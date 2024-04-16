package com.lib.springframework.security;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@Configuration
@ConditionalOnProperty(
  value = "app.security.rbac.enabled",
  havingValue = "true",
  matchIfMissing = false
)
@EnableGlobalMethodSecurity(
  prePostEnabled = true,
  securedEnabled = true,
  jsr250Enabled = true
)
public class RbacConfiguration {

  private static final Logger log = LoggerFactory.getLogger(
    RbacConfiguration.class
  );

  @PostConstruct
  public void init() {
    log.info(
      "RBAC enabled, a.k.a. method level security: prePostEnabled=true, securedEnabled=true, jsr250Enabled=true"
    );
  }
}
