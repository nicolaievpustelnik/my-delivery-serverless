package com.lib.shoji;

import java.nio.file.Path;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@ConditionalOnProperty(prefix = "shoji", name = "skip-signature-verification")
@Component
@ConfigurationProperties("shoji")
@Getter
@Setter
public class ShojiTokenConfiguration {

  Boolean skipSignatureVerification;
  Path publicPkcs8KeyPath;
  String[] uriPrefixToSkip;

}
