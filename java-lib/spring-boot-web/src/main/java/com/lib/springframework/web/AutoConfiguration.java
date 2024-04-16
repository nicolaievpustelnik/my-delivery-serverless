package com.lib.springframework.web;

import com.lib.jpa.util.SpecificationParser;
import com.lib.shoji.ShojiTokenConfiguration;
import com.lib.springframework.web.exception.GlobalErrorHandler;
import com.lib.web.util.RHSColonQueryStringParser;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@ComponentScan(
  basePackageClasses = {
    GlobalErrorHandler.class,
    RHSColonQueryStringParser.class,
    SpecificationParser.class,
    ShojiTokenConfiguration.class
  }
)
public class AutoConfiguration {

  private static final Logger log = LoggerFactory.getLogger(
    AutoConfiguration.class
  );

  @Value("")
  private Boolean noTlsCheck;

  @Bean
  public CorsFilter corsFiler() {
    log.info("Configuring CORS.");

    var config = new CorsConfiguration();

    config.setAllowCredentials(false);
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    config.addExposedHeader("x-trace-id");

    config.addAllowedMethod("OPTIONS");
    config.addAllowedMethod("GET");
    config.addAllowedMethod("POST");
    config.addAllowedMethod("PATCH");
    config.addAllowedMethod("PUT");
    config.addAllowedMethod("DELETE");

    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter((CorsConfigurationSource) source);
  }

  @ConditionalOnProperty(
    value = "app.security.tls.skip-validation",
    havingValue = "false",
    matchIfMissing = true
  )
  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder.build();
  }

  @ConditionalOnProperty(
    value = "app.security.tls.skip-validation",
    havingValue = "true",
    matchIfMissing = false
  )
  @Bean
  public RestTemplate restTemplateTlsSkipValidation() throws Exception {
    final SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(
      SSLContexts.custom().loadTrustMaterial(TrustAllStrategy.INSTANCE).build()
    );

    final var client = HttpClients
      .custom()
      .setSSLHostnameVerifier(new NoopHostnameVerifier())
      .setSSLSocketFactory(csf)
      .build();

    final var factory = new HttpComponentsClientHttpRequestFactory(client);

    log.warn("*** tls *** skip validation: enabled");

    return new RestTemplate(factory);
  }
}
