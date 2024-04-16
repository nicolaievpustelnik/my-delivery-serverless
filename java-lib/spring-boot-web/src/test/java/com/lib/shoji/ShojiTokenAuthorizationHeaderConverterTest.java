package com.lib.shoji;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.lib.support.FoobarController;
import com.lib.support.Main;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ActiveProfiles("test")
@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = {
    Main.class,
    ShojiTokenConfiguration.class,
    ShojiTokenAuthorizationHeaderConverter.class,
    ShojiTokenConfigurerAdapter.class,
  }
)
class ShojiTokenAuthorizationHeaderConverterTest {

  @Value("${local.server.port}")
  int port;

  @Autowired
  TestRestTemplate http;

  @Captor
  ArgumentCaptor<ShojiToken> captor;

  @SpyBean
  FoobarController controller;

  @SpyBean
  ShojiTokenConfiguration configuration;

  @Autowired
  ShojiTokenAuthorizationHeaderConverter converter;

  @Test
  void should_result_null_when_no_header() {
    final String url = String.format("http://localhost:%d/test/shoji", port);

    final var headers = new HttpHeaders();

    final var response = http.exchange(
      url,
      HttpMethod.GET,
      new HttpEntity<>(headers),
      String.class
    );
    assertEquals(200, response.getStatusCode().value());

    verify(controller).save(captor.capture());
    final var actual = captor.getValue();

    assertEquals(ShojiTokenAuthorizationHeaderConverter.NO_TOKEN, actual);
  }

  @Test
  void should_result_null_when_empty_header() {
    final String url = String.format("http://localhost:%d/test/shoji", port);

    final var headers = new HttpHeaders();
    headers.add(ShojiToken.HEADER, "  ");

    final var response = http.exchange(
      url,
      HttpMethod.GET,
      new HttpEntity<>(headers),
      String.class
    );
    assertEquals(200, response.getStatusCode().value());

    verify(controller).save(captor.capture());
    final var actual = captor.getValue();

    assertEquals(ShojiTokenAuthorizationHeaderConverter.NO_TOKEN, actual);
  }

  @Test
  void should_result_null_when_no_token() {
    final String url = String.format("http://localhost:%d/test/shoji", port);

    final var headers = new HttpHeaders();
    headers.add(ShojiToken.HEADER, "Bearer ");

    final var response = http.exchange(
      url,
      HttpMethod.GET,
      new HttpEntity<>(headers),
      String.class
    );
    assertEquals(200, response.getStatusCode().value());

    verify(controller).save(captor.capture());
    final var actual = captor.getValue();

    assertEquals(ShojiTokenAuthorizationHeaderConverter.NO_TOKEN, actual);
  }

  void should_result_null_when_invalid_jwt_format() {
    final String url = String.format("http://localhost:%d/test/shoji", port);

    final var token =
      "eyJhbGciOiJSUzUxMiJ9.eyJhdWQiOiJodHRwczovL2V4YW1wbGUuY29tL2FwaS92MS8iLCJzdWIiOiJmam9zZW1vckBudHRkYXRhLmNvbSIsImlhdCI6MTY3MDUxOTkxOCwiZXhwIjoxNjcwNTIwODE4LCJpc3MiOiJodHRwczovL3Nob2ppLWRldm9wcy1kZXYuYXBwcy5vY3B0ZXN0LnRveW90YS5jb20uYXIiLCJncm91cHMiOlsidGxhYy1ia3RnLXByby1hZG0iXSwibmlja25hbWUiOiJmam9zZW1vciIsIm5hbWUiOiJmam9zZW1vckBudHRkYXRhLmNvbSIsInBpY3R1cmUiOiJodHRwczovL3MuZ3JhdmF0YXIuY29tL2F2YXRhci8wZWEyOGVlMTZkMzViNTQ0YjhmMzA0MTIxNDJjNTA4ND9zPTQ4MCZyPXBnJmQ9aHR0cHMlM0ElMkYlMkZjZG4uYXV0aDAuY29tJTJGYXZhdGFycyUyRmZqLnBuZyIsImlkcCI6ImF1dGgwLXN1cHBsaWVyIn0";
    final var headers = new HttpHeaders();
    headers.add(ShojiToken.HEADER, "Bearer " + token);

    final var response = http.exchange(
      url,
      HttpMethod.GET,
      new HttpEntity<>(headers),
      String.class
    );
    assertEquals(200, response.getStatusCode().value());

    verify(controller).save(captor.capture());
    final var actual = captor.getValue();

    assertEquals(ShojiTokenAuthorizationHeaderConverter.NO_TOKEN, actual);
  }

  @Test
  void should_result_http_400_on_invalid_signature() {
    final String url = String.format("http://localhost:%d/test/shoji", port);

    final var token =
      "eyJhbGciOiJSUzUxMiJ9.eyJhdWQiOiJodHRwczovL2V4YW1wbGUuY29tL2FwaS92MS8iLCJzdWIiOiJmam9zZW1vckBudHRkYXRhLmNvbSIsImlhdCI6MTY3MDUxOTkxOCwiZXhwIjoxNjcwNTIwODE4LCJpc3MiOiJodHRwczovL3Nob2ppLWRldm9wcy1kZXYuYXBwcy5vY3B0ZXN0LnRveW90YS5jb20uYXIiLCJncm91cHMiOlsidGxhYy1ia3RnLXByby1hZG0iXSwibmlja25hbWUiOiJmam9zZW1vciIsIm5hbWUiOiJmam9zZW1vckBudHRkYXRhLmNvbSIsInBpY3R1cmUiOiJodHRwczovL3MuZ3JhdmF0YXIuY29tL2F2YXRhci8wZWEyOGVlMTZkMzViNTQ0YjhmMzA0MTIxNDJjNTA4ND9zPTQ4MCZyPXBnJmQ9aHR0cHMlM0ElMkYlMkZjZG4uYXV0aDAuY29tJTJGYXZhdGFycyUyRmZqLnBuZyIsImlkcCI6ImF1dGgwLXN1cHBsaWVyIn0.aYUIbTRrS0dcNEC9xWYFfpXWpEAr7schT4WoLfvFBE1GnZYSvRAKPJbS2j9OSycb9Qqc-7CJoB8gs02PU6F7wLeJQeeFByjUSs2dFqOopsXpCh5IU8hK1iKwt3P5DRinkfrgzplg5IFN15xa8SBRPelIqkRiH-0CnxC70VXclrgIUAhhbvrJ7Ri-EDM3BwqXzV6xN09-DK7whWuJJptp7x2RzCMOsShRurpAOn4p-4gXRhoNJ5kVSLyDDkdLUElBMEQIMW3AQeiBLrH5imCriWj65dghwnAHibcLUnPcSGql4OUNqOeXbBF-W1P6K_MKtiJ0z5owgB6bb-ugbGp_1jLBOZeZtSylsq6ME_bFBL-RyQ_ujNEqQokN4MyocAJ5s3PQMuhXttVornQh-0Yo8EjTO-RTFDtTSR18gZK_H88qc5GwsD8IiICDNppofhQhn6BFUffQZFSRenqgLp71qVrPU_uyzeAIcvJuYeux6wcrLlvv3INdxpfvuBYI-vvnQSRVEqObESrJnL1vR0oE-67KVJVimlhPT8AvoA2BAeelopWbKEsLASyMYQBMWWaOhPW4yRryz59uR-oWZ-trI4b0MOdjEeUbFwfqVhSgD5QTlarQaDpm6jrbKauHgkfrjwYIwWkJ30AZ9OyifKXZ4WnTYcQl8nN";
    final var headers = new HttpHeaders();
    headers.add(ShojiToken.HEADER, "Bearer " + token);

    doReturn(Boolean.FALSE).when(configuration).getSkipSignatureVerification();

    final var response = http.exchange(
      url,
      HttpMethod.GET,
      new HttpEntity<>(headers),
      String.class
    );
    assertEquals(400, response.getStatusCode().value());
  }

  @Test
  void should_result_deserialized_token_when_ok() {
    final String url = String.format("http://localhost:%d/test/shoji", port);

    final var token =
      "eyJhbGciOiJSUzUxMiJ9.eyJhdWQiOiJodHRwczovL2V4YW1wbGUuY29tL2FwaS92MS8iLCJzdWIiOiJmam9zZW1vckBudHRkYXRhLmNvbSIsImlhdCI6MTY3MDUxOTkxOCwiZXhwIjoxNjcwNTIwODE4LCJpc3MiOiJodHRwczovL3Nob2ppLWRldm9wcy1kZXYuYXBwcy5vY3B0ZXN0LnRveW90YS5jb20uYXIiLCJncm91cHMiOlsidGxhYy1ia3RnLXByby1hZG0iXSwibmlja25hbWUiOiJmam9zZW1vciIsIm5hbWUiOiJmam9zZW1vckBudHRkYXRhLmNvbSIsInBpY3R1cmUiOiJodHRwczovL3MuZ3JhdmF0YXIuY29tL2F2YXRhci8wZWEyOGVlMTZkMzViNTQ0YjhmMzA0MTIxNDJjNTA4ND9zPTQ4MCZyPXBnJmQ9aHR0cHMlM0ElMkYlMkZjZG4uYXV0aDAuY29tJTJGYXZhdGFycyUyRmZqLnBuZyIsImlkcCI6ImF1dGgwLXN1cHBsaWVyIn0.aYUIbTRrS0dcNEC9xWYFfpXWpEAr7schT4WoLfvFBE1GnZYSvRAKPJbS2j9OSycb9Qqc-7CJoB8gs02PU6F7wLeJQeeFByjUSs2dFqOopsXpCh5IU8hK1iKwt3P5DRinkfrgzplg5IFN15xa8SBRPelIqkRiH-0CnxC70VXclrgIUAhhbvrJ7Ri-EDM3BwqXzV6xN09-DK7whWuJJptp7x2RzCMOsShRurpAOn4p-4gXRhoNJ5kVSLyDDkdLUElBMEQIMW3AQeiBLrH5imCriWj65dghwnAHibcLUnPcSGql4OUNqOeXbBF-W1P6K_MKtiJ0z5owgB6bb-ugbGp_1jLBOZeZtSylsq6ME_bFBL-RyQ_ujNEqQokN4MyocAJ5s3PQMuhXttVornQh-0Yo8EjTO-RTFDtTSR18gZK_H88qc5GwsD8IiICDNppofhQhn6BFUffQZFSRenqgLp71qVrPU_uyzeAIcvJuYeux6wcrLlvv3INdxpfvuBYI-vvnQSRVEqObESrJnL1vR0oE-67KVJVimlhPT8AvoA2BAeelopWbKEsLASyMYQBMWWaOhPW4yRryz59uR-oWZ-trI4b0MOdjEeUbFwfqVhSgD5QTlarQaDpm6jrbKauHgkfrjwYIwWkJ30AZ9OyifKXZ4WnTYcQl8nN-pp3KTwqXgYE";
    final var headers = new HttpHeaders();
    headers.add(ShojiToken.HEADER, "Bearer " + token);

    final var response = http.exchange(
      url,
      HttpMethod.GET,
      new HttpEntity<>(headers),
      String.class
    );
    assertEquals(200, response.getStatusCode().value());

    verify(controller).save(captor.capture());
    final var actual = captor.getValue();

    assertNotNull(actual);
    assertEquals("https://example.com/api/v1/", actual.audience());
    assertEquals("fjosemor@nttdata.com", actual.email());
    assertEquals(1670519918, actual.issedAt());
    assertEquals(1670520818, actual.expiration());
    assertEquals(
      "https://shoji-devops-dev.apps.ocptest.lib.com.ar",
      actual.issuer()
    );
    assertEquals("tlac-bktg-pro-adm", actual.groups()[0]);
    assertEquals("fjosemor", actual.nickname());
    assertEquals("fjosemor@nttdata.com", actual.name());
    assertEquals("auth0-supplier", actual.idp());
    assertFalse(actual.failover());
  }

  @Test
  void should_result_deserialized_token_signature_when_ok() {
    final String url = String.format("http://localhost:%d/test/shoji", port);

    /*
     {
      "aud": "https://example.com/api/v1/",
      "sub": "fjosemor@nttdata.com",
      "iat": 1670519918,
      "exp": 1670520818,
      "iss": "https://shoji-devops-dev.apps.ocptest.lib.com.ar",
      "groups": [
        "tlac-bktg-pro-adm"
      ],
      "nickname": "fjosemor",
      "name": "fjosemor@nttdata.com",
      "picture": "https://s.gravatar.com/avatar/0ea28ee16d35b544b8f30412142c5084?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Ffj.png",
      "idp": "auth0-supplier"
    }
     */
    final var token =
      "eyJhbGciOiJSUzUxMiJ9.eyJhdWQiOiJodHRwczovL2V4YW1wbGUuY29tL2FwaS92MS8iLCJzdWIiOiJmam9zZW1vckBudHRkYXRhLmNvbSIsImlhdCI6MTY3MDUxOTkxOCwiZXhwIjoxNjcwNTIwODE4LCJpc3MiOiJodHRwczovL3Nob2ppLWRldm9wcy1kZXYuYXBwcy5vY3B0ZXN0LnRveW90YS5jb20uYXIiLCJncm91cHMiOlsidGxhYy1ia3RnLXByby1hZG0iXSwibmlja25hbWUiOiJmam9zZW1vciIsIm5hbWUiOiJmam9zZW1vckBudHRkYXRhLmNvbSIsInBpY3R1cmUiOiJodHRwczovL3MuZ3JhdmF0YXIuY29tL2F2YXRhci8wZWEyOGVlMTZkMzViNTQ0YjhmMzA0MTIxNDJjNTA4ND9zPTQ4MCZyPXBnJmQ9aHR0cHMlM0ElMkYlMkZjZG4uYXV0aDAuY29tJTJGYXZhdGFycyUyRmZqLnBuZyIsImlkcCI6ImF1dGgwLXN1cHBsaWVyIn0.aYUIbTRrS0dcNEC9xWYFfpXWpEAr7schT4WoLfvFBE1GnZYSvRAKPJbS2j9OSycb9Qqc-7CJoB8gs02PU6F7wLeJQeeFByjUSs2dFqOopsXpCh5IU8hK1iKwt3P5DRinkfrgzplg5IFN15xa8SBRPelIqkRiH-0CnxC70VXclrgIUAhhbvrJ7Ri-EDM3BwqXzV6xN09-DK7whWuJJptp7x2RzCMOsShRurpAOn4p-4gXRhoNJ5kVSLyDDkdLUElBMEQIMW3AQeiBLrH5imCriWj65dghwnAHibcLUnPcSGql4OUNqOeXbBF-W1P6K_MKtiJ0z5owgB6bb-ugbGp_1jLBOZeZtSylsq6ME_bFBL-RyQ_ujNEqQokN4MyocAJ5s3PQMuhXttVornQh-0Yo8EjTO-RTFDtTSR18gZK_H88qc5GwsD8IiICDNppofhQhn6BFUffQZFSRenqgLp71qVrPU_uyzeAIcvJuYeux6wcrLlvv3INdxpfvuBYI-vvnQSRVEqObESrJnL1vR0oE-67KVJVimlhPT8AvoA2BAeelopWbKEsLASyMYQBMWWaOhPW4yRryz59uR-oWZ-trI4b0MOdjEeUbFwfqVhSgD5QTlarQaDpm6jrbKauHgkfrjwYIwWkJ30AZ9OyifKXZ4WnTYcQl8nN-pp3KTwqXgYE";
    final var headers = new HttpHeaders();
    headers.add(ShojiToken.HEADER, "Bearer " + token);

    doReturn(Boolean.FALSE).when(configuration).getSkipSignatureVerification();

    final var response = http.exchange(
      url,
      HttpMethod.GET,
      new HttpEntity<>(headers),
      String.class
    );
    assertEquals(200, response.getStatusCode().value());

    verify(controller).save(captor.capture());
    final var actual = captor.getValue();

    assertNotNull(actual);
    assertEquals("https://example.com/api/v1/", actual.audience());
    assertEquals("fjosemor@nttdata.com", actual.email());
    assertEquals(1670519918, actual.issedAt());
    assertEquals(1670520818, actual.expiration());
    assertEquals(
      "https://shoji-devops-dev.apps.ocptest.lib.com.ar",
      actual.issuer()
    );
    assertEquals("tlac-bktg-pro-adm", actual.groups()[0]);
    assertEquals("fjosemor", actual.nickname());
    assertEquals("fjosemor@nttdata.com", actual.name());
    assertEquals("auth0-supplier", actual.idp());
    assertFalse(actual.failover());
  }

  @Test
  void should_result_http_400_when_public_file_not_found() {
    final String url = String.format("http://localhost:%d/test/shoji", port);

    final var token =
      "eyJhbGciOiJSUzUxMiJ9.eyJhdWQiOiJodHRwczovL2V4YW1wbGUuY29tL2FwaS92MS8iLCJzdWIiOiJmam9zZW1vckBudHRkYXRhLmNvbSIsImlhdCI6MTY3MDUxOTkxOCwiZXhwIjoxNjcwNTIwODE4LCJpc3MiOiJodHRwczovL3Nob2ppLWRldm9wcy1kZXYuYXBwcy5vY3B0ZXN0LnRveW90YS5jb20uYXIiLCJncm91cHMiOlsidGxhYy1ia3RnLXByby1hZG0iXSwibmlja25hbWUiOiJmam9zZW1vciIsIm5hbWUiOiJmam9zZW1vckBudHRkYXRhLmNvbSIsInBpY3R1cmUiOiJodHRwczovL3MuZ3JhdmF0YXIuY29tL2F2YXRhci8wZWEyOGVlMTZkMzViNTQ0YjhmMzA0MTIxNDJjNTA4ND9zPTQ4MCZyPXBnJmQ9aHR0cHMlM0ElMkYlMkZjZG4uYXV0aDAuY29tJTJGYXZhdGFycyUyRmZqLnBuZyIsImlkcCI6ImF1dGgwLXN1cHBsaWVyIn0.aYUIbTRrS0dcNEC9xWYFfpXWpEAr7schT4WoLfvFBE1GnZYSvRAKPJbS2j9OSycb9Qqc-7CJoB8gs02PU6F7wLeJQeeFByjUSs2dFqOopsXpCh5IU8hK1iKwt3P5DRinkfrgzplg5IFN15xa8SBRPelIqkRiH-0CnxC70VXclrgIUAhhbvrJ7Ri-EDM3BwqXzV6xN09-DK7whWuJJptp7x2RzCMOsShRurpAOn4p-4gXRhoNJ5kVSLyDDkdLUElBMEQIMW3AQeiBLrH5imCriWj65dghwnAHibcLUnPcSGql4OUNqOeXbBF-W1P6K_MKtiJ0z5owgB6bb-ugbGp_1jLBOZeZtSylsq6ME_bFBL-RyQ_ujNEqQokN4MyocAJ5s3PQMuhXttVornQh-0Yo8EjTO-RTFDtTSR18gZK_H88qc5GwsD8IiICDNppofhQhn6BFUffQZFSRenqgLp71qVrPU_uyzeAIcvJuYeux6wcrLlvv3INdxpfvuBYI-vvnQSRVEqObESrJnL1vR0oE-67KVJVimlhPT8AvoA2BAeelopWbKEsLASyMYQBMWWaOhPW4yRryz59uR-oWZ-trI4b0MOdjEeUbFwfqVhSgD5QTlarQaDpm6jrbKauHgkfrjwYIwWkJ30AZ9OyifKXZ4WnTYcQl8nN-pp3KTwqXgYE";
    final var headers = new HttpHeaders();
    headers.add(ShojiToken.HEADER, "Bearer " + token);

    ReflectionTestUtils.setField(converter, "shojiPublicKey", null);

    doReturn(Boolean.FALSE).when(configuration).getSkipSignatureVerification();
    doReturn(Path.of("/path/not/found.der"))
      .when(configuration)
      .getPublicPkcs8KeyPath();

    final var response = http.exchange(
      url,
      HttpMethod.GET,
      new HttpEntity<>(headers),
      String.class
    );
    assertEquals(400, response.getStatusCode().value());
  }

  @Test
  void should_result_http_400_when_have_not_header() {
    final String url = String.format("http://localhost:%d/test/shoji", port);

    final var token =
      "eyJhdWQiOiJodHRwczovL2V4YW1wbGUuY29tL2FwaS92MS8iLCJzdWIiOiJmam9zZW1vckBudHRkYXRhLmNvbSIsImlhdCI6MTY3MDUxOTkxOCwiZXhwIjoxNjcwNTIwODE4LCJpc3MiOiJodHRwczovL3Nob2ppLWRldm9wcy1kZXYuYXBwcy5vY3B0ZXN0LnRveW90YS5jb20uYXIiLCJncm91cHMiOlsidGxhYy1ia3RnLXByby1hZG0iXSwibmlja25hbWUiOiJmam9zZW1vciIsIm5hbWUiOiJmam9zZW1vckBudHRkYXRhLmNvbSIsInBpY3R1cmUiOiJodHRwczovL3MuZ3JhdmF0YXIuY29tL2F2YXRhci8wZWEyOGVlMTZkMzViNTQ0YjhmMzA0MTIxNDJjNTA4ND9zPTQ4MCZyPXBnJmQ9aHR0cHMlM0ElMkYlMkZjZG4uYXV0aDAuY29tJTJGYXZhdGFycyUyRmZqLnBuZyIsImlkcCI6ImF1dGgwLXN1cHBsaWVyIn0.aYUIbTRrS0dcNEC9xWYFfpXWpEAr7schT4WoLfvFBE1GnZYSvRAKPJbS2j9OSycb9Qqc-7CJoB8gs02PU6F7wLeJQeeFByjUSs2dFqOopsXpCh5IU8hK1iKwt3P5DRinkfrgzplg5IFN15xa8SBRPelIqkRiH-0CnxC70VXclrgIUAhhbvrJ7Ri-EDM3BwqXzV6xN09-DK7whWuJJptp7x2RzCMOsShRurpAOn4p-4gXRhoNJ5kVSLyDDkdLUElBMEQIMW3AQeiBLrH5imCriWj65dghwnAHibcLUnPcSGql4OUNqOeXbBF-W1P6K_MKtiJ0z5owgB6bb-ugbGp_1jLBOZeZtSylsq6ME_bFBL-RyQ_ujNEqQokN4MyocAJ5s3PQMuhXttVornQh-0Yo8EjTO-RTFDtTSR18gZK_H88qc5GwsD8IiICDNppofhQhn6BFUffQZFSRenqgLp71qVrPU_uyzeAIcvJuYeux6wcrLlvv3INdxpfvuBYI-vvnQSRVEqObESrJnL1vR0oE-67KVJVimlhPT8AvoA2BAeelopWbKEsLASyMYQBMWWaOhPW4yRryz59uR-oWZ-trI4b0MOdjEeUbFwfqVhSgD5QTlarQaDpm6jrbKauHgkfrjwYIwWkJ30AZ9OyifKXZ4WnTYcQl8nN-pp3KTwqXgYE";
    final var headers = new HttpHeaders();
    headers.add(ShojiToken.HEADER, "Bearer " + token);

    doReturn(Boolean.FALSE).when(configuration).getSkipSignatureVerification();

    final var response = http.exchange(
      url,
      HttpMethod.GET,
      new HttpEntity<>(headers),
      String.class
    );
    assertEquals(400, response.getStatusCode().value());
  }
}
