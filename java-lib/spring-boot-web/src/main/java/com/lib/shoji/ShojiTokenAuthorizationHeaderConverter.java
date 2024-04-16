package com.lib.shoji;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.jsonwebtoken.io.Decoders;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(prefix = "shoji", name = "skip-signature-verification")
@Component
@Slf4j
class ShojiTokenAuthorizationHeaderConverter
  implements Converter<String, ShojiToken> {

  public static final ShojiToken NO_TOKEN = null;

  @Autowired
  ShojiTokenConfiguration shoji;

  @Autowired
  ObjectMapper json;

  private PublicKey shojiPublicKey;

  private PublicKey getShojiPublicKey() {
    if (null == shojiPublicKey) {
      try {
        final var content = Files.readAllBytes(shoji.getPublicPkcs8KeyPath());
        final var spec = new X509EncodedKeySpec(content);
        this.shojiPublicKey =
          KeyFactory.getInstance("RSA").generatePublic(spec);
      } catch (IOException e) {
        log.error(e.getMessage(), e);
        throw new ShojiPublicKeyException(e.getMessage());
      } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
        log.error(e.getMessage(), e);
        throw new IllegalStateException(e.getMessage(), e);
      }
    }

    return shojiPublicKey;
  }

  private void verify(String headerAndPayload, String signature) {
    final var validator = new DefaultJwtSignatureValidator(
      SignatureAlgorithm.RS512,
      getShojiPublicKey(),
      Decoders.BASE64URL
    );

    if (!validator.isValid(headerAndPayload, signature)) {
      throw new ShojiNotIssuedTheTokenException(
        String.format("%s.%s", headerAndPayload, signature)
      );
    }
  }

  @Override
  @Nullable
  public ShojiToken convert(final String source) {
    log.debug("Authorization header content: {}", source);
    var header = source.trim();

    var tokenOptional = Optional
      .of(header)
      .map(h -> h.split(" "))
      .filter(s -> s.length > 1)
      .map(s -> s[1])
      .map(String::trim);

    if (tokenOptional.isPresent()) {
      var token = tokenOptional.get();

      log.debug("intercepted token: {}", token);
      final var chunks = token.split("\\.");
      if (chunks.length < 3) {
        log.error("token does not have the jwt parts");
        throw new IllegalArgumentException(
          "shoji token have on invalid format"
        );
      } else {
        log.debug("size of token chunks {}", chunks.length);
        log.debug("base64 token header {}", chunks[0]);
        log.debug("base64 token body {}", chunks[1]);
        log.debug("base64 token signature {}", chunks[2]);

        final var decoder = Base64.getUrlDecoder();
        final var headerString = new String(decoder.decode(chunks[0]));
        final var bodyString = new String(decoder.decode(chunks[1]));
        log.debug("json token header {}", headerString);
        log.debug("json token body {}", bodyString);

        if (!shoji.getSkipSignatureVerification()) {
          shoji.getPublicPkcs8KeyPath();
          verify(String.format("%s.%s", chunks[0], chunks[1]), chunks[2]);
        } else {
          log.warn(
            "shoji is configured to skip signature verification. DO NOT USE IT IN PRODUCTION"
          );
        }

        try {
          return json.readValue(bodyString, ShojiToken.class);
        } catch (IOException e) {
          log.error(e.getMessage(), e);
        }
      }
    } else {
      log.warn("Authorization header have no content");
    }

    return NO_TOKEN;
  }
}
