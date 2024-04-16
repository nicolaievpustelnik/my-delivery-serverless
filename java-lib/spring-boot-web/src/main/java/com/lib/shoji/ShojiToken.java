package com.lib.shoji;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;

public record ShojiToken(
  String[] groups,
  @JsonProperty("sub") String email,
  @JsonProperty("iat") Long issedAt,
  @JsonProperty("exp") Long expiration,
  @JsonProperty("aud") String audience,
  @JsonProperty("scp") String scope,
  String nickname,
  String name,
  String picture,
  String idp,
  @JsonProperty("iss") String issuer,
  Boolean failover
) {
  public static final String HEADER = "Authorization";

  @Override
  public Boolean failover() {
    return Optional.ofNullable(failover).orElseGet(() -> Boolean.FALSE);
  }
}
