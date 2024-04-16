# spring-boot-rest-client-security

Spring Boot convenience library for HTTP Client using Client Credentials.  

## How to Use

```xml
<dependency>
  <groupId>com.lib.java</groupId>
  <artifactId>spring-boot-rest-client-security</artifactId>
  <version>2.6-1.0.0</version>
</dependency>
```

## Configuration

Configuration can be made using the properties bellow:

```yaml
http:
  auth:
    client-registration-id: auth0 

spring:
  security:
    oauth2:
      client:
        registration:
          auth0: # Same value of http.auth.client-registration-id
            authorization-grant-type: client_credentials
            client-id: 6PGDnxDLQcvKABSLqgz6QcSUCuaeZpdj
            client-secret: 0YBEJf4hn5njfIa1CG0Jb_ZrU2ZUhRURBFG173bDQ5uJkuCNAr_9jqt1EDr5_utd
        provider:
          auth0: # Same value of http.auth.client-registration-id
            token-uri: https://moderna.us.auth0.com/oauth/token
```

> 🔥__Notice__: the `client-id`, `client-secret` and `token-uri` are valid just for development purposes.
