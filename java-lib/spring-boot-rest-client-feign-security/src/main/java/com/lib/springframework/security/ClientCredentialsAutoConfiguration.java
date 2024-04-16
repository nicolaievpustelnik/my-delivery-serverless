package com.lib.springframework.security;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Configuration
public class ClientCredentialsAutoConfiguration {

  @Value("${http.auth.client-registration-id}")
  private String clientRegistrationId;

  private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
  private final ClientRegistrationRepository clientRegistrationRepository;

  public ClientCredentialsAutoConfiguration(
    OAuth2AuthorizedClientService oAuth2AuthorizedClientService,
    ClientRegistrationRepository clientRegistrationRepository
  ) {
    this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
    this.clientRegistrationRepository = clientRegistrationRepository;
  }

  @Bean
  public RequestInterceptor requestInterceptor() {
    var clientRegistration = clientRegistrationRepository.findByRegistrationId(
      clientRegistrationId
    );
    
    var clientCredentialsFeignManager = new AccessTokenManager(
      authorizedClientManager(),
      clientRegistration
    );

    return requestTemplate -> 
      requestTemplate.header(
        "Authorization",
        "Bearer " + clientCredentialsFeignManager.getToken()
      );
  }

  @Bean
  public OAuth2AuthorizedClientManager authorizedClientManager() {
    var authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder
      .builder()
      .clientCredentials()
      .build();

    var authorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(
      clientRegistrationRepository,
      oAuth2AuthorizedClientService
    );
    authorizedClientManager.setAuthorizedClientProvider(
      authorizedClientProvider
    );

    return authorizedClientManager;
  }
}
