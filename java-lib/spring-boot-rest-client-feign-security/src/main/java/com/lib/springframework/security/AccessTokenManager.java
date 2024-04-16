package com.lib.springframework.security;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

public class AccessTokenManager {

  private static final Logger log = LoggerFactory.getLogger(
    AccessTokenManager.class
  );

  private final OAuth2AuthorizedClientManager manager;
  private final Authentication principal;
  private final ClientRegistration clientRegistration;

  public AccessTokenManager(
    OAuth2AuthorizedClientManager manager,
    ClientRegistration clientRegistration
  ) {
    this.manager = manager;
    this.clientRegistration = clientRegistration;
    this.principal =
      new ClientCredentialsPrincipal(clientRegistration.getClientId());
  }

  public String getToken() {
    try {
      var oAuth2AuthorizeRequest = OAuth2AuthorizeRequest
        .withClientRegistrationId(clientRegistration.getRegistrationId())
        .principal(principal)
        .build();

      log.debug(
        "Invoking the GET token endpoint with attributes {}",
        oAuth2AuthorizeRequest.getAttributes()
      );
      var client = manager.authorize(oAuth2AuthorizeRequest);

      if (Objects.isNull(client)) {
        throw new IllegalStateException(
          "client credentials flow on " +
          clientRegistration.getRegistrationId() +
          " failed, client is null"
        );
      } else  {
        log.debug(
          "Got the access token with expiration of {}",
          client.getAccessToken().getExpiresAt()
        );
      }

      // TODO: implement caching of access token based on the expiration

      return client.getAccessToken().getTokenValue();

    } catch (Exception e) {
      log.error("client credentials error " + e.getMessage(), e);
    }

    return null;
  }
}
