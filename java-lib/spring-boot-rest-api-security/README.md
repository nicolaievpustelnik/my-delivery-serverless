# spring-boot-rest-api-security

SpringBoot conveniences to create protected HTTP Rest API endpoint with OAuth2, JWT and RBAC.

## Features

By default the following URI does not enforce security check:

- `/actuator/**`
- `/swagger-ui.html`
- `/swagger-ui/**`
- `/v3/api-docs/**`

## How to Use

```xml
<dependency>
  <groupId>com.lib.java</groupId>
  <artifactId>spring-boot-rest-api-security</artifactId>
  <version>2.6-1.0.0</version>
</dependency>
```

## Configuration

Configuration can be made using the properties bellow:

```yaml
# To enable method level RBAC security authorization.
# Without this, any valid token will be authorized without role checking.
app:
  security:
    rbac:
      enabled: true | false
  springdoc:
    openid-connect-url: https://moderna.us.auth0.com/.well-known/openid-configuration

# to auto enable the configuration of openid-connect at swagger ui
springdoc:
  swagger-ui:
    enabled: true

spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://configure-me.com/
```

## Usage

Once `app.security.rbac.enabled=true`, you can use these annotations:

- `javax.annotation.security.PermitAll`
- `javax.annotation.security.RolesAllowed`
- `org.springframework.security.access.prepost.PreAuthorize`
- `org.springframework.security.access.prepost.PostAuthorize`
- `org.springframework.security.access.annotation.Secured`

> [See this Baeldung Introduction to Spring Method](https://www.baeldung.com/spring-security-method-security)
