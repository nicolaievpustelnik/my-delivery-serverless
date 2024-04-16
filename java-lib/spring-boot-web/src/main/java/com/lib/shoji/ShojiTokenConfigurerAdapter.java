package com.lib.shoji;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ConditionalOnProperty(prefix = "shoji", name = "skip-signature-verification")
@Configuration
@RequiredArgsConstructor
public class ShojiTokenConfigurerAdapter implements WebMvcConfigurer {

  private final ShojiTokenAuthorizationHeaderConverter converter;

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(converter);
  }
}
