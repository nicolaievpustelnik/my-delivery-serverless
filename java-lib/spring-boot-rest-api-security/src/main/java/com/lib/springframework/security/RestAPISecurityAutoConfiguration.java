package com.lib.springframework.security;

import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@EnableWebSecurity
@Import({ RbacConfiguration.class })
public class RestAPISecurityAutoConfiguration {

  private static final Logger log = LoggerFactory.getLogger(
    RestAPISecurityAutoConfiguration.class
  );

  @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
  private String issuer;

  @Value("${app.springdoc.openid-connect-url:https://configure-me.com/.well-known/openid-configuration}")
  private URL openIdConnectURL;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	      .authorizeRequests()
	      .antMatchers("/actuator/**")
	      .permitAll()
	      .antMatchers("/swagger-ui.html")
	      .permitAll()
	      .antMatchers("/swagger-ui/**")
	      .permitAll()
	      .antMatchers("/v3/api-docs/**")
	      .permitAll()
	      .anyRequest()
	      .authenticated()
	      .and()
	      .cors()
	      .configurationSource(corsConfigurationSource())
	      .and()
	      .oauth2ResourceServer()
	      .jwt()
	      .decoder(jwtDecoder());

	    log.info("HTTP security enabled with JWT and OAuth2");
	    
	    return http.build();
	  
  }

  private CorsConfigurationSource corsConfigurationSource() {
    var configuration = new CorsConfiguration();
    configuration.setAllowedMethods(
      List.of(
        HttpMethod.GET.name(),
        HttpMethod.PUT.name(),
        HttpMethod.POST.name(),
        HttpMethod.DELETE.name()
      )
    );

    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration(
      "/**",
      configuration.applyPermitDefaultValues()
    );
    return source;
  }

  private JwtDecoder jwtDecoder() {
    return JwtDecoders.fromOidcIssuerLocation(issuer);

  }

  @Bean
  @ConditionalOnProperty(
    value = "springdoc.swagger-ui.enabled",
    havingValue = "true",
    matchIfMissing = false
  )
  public OpenAPI openAPI() {

    final var scheme = "Bearer JWT Token";
    return new OpenAPI()
    .addSecurityItem(new SecurityRequirement().addList(scheme))
    .components(
        new Components()
        .addSecuritySchemes(
            scheme,
            new SecurityScheme()
              .name(scheme)
              .type(SecurityScheme.Type.OPENIDCONNECT)
              .scheme("bearer")
              .openIdConnectUrl(openIdConnectURL.toString())
          )
      );
  }

}
