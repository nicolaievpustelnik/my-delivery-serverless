# Spring Boot Web

Here you find convenience classes to accelerate the HTTP development.

## How to Use

```xml
<dependency>
  <groupId>com.lib.java</groupId>
  <artifactId>spring-boot-web</artifactId>
  <version>{LATEST VERSION}</version>
</dependency>
```

## Features

- Enable CORS
- RestTemplate
- Controller Advise with common exception handling
- AbstractCRUDJPAController: *Abstract controller that implements the basic REST CURD operations using a JPA repository.*
- ShojiToken
- RestTemplate with TLS verification skip
- 📢 CRUD - [See these examples](src/test/java/com/lib/support/rest/crud)
  - customize the suffix of crud controllers
      ```yaml
      app:
        api:
          crud:
            suffix-path: custom-suffix
      # you will have:
      #  GET /api/custom-suffix/{id}
      #  GET /api/custom-suffix?page=0&size=100
      #  POST /api/custom-suffix
      #  PUT /api/custom-suffix/{id}
      #  DELETE /api/custom-suffix/{id}

      ```

### Usage example

#### AbstractCRUDJPAController

This controller implements the basic CRUD operations using a JAPA repository. To use it, the entity to be managed must implement the com.lib.model.IEntity interface. The generic types that the abstract class receives are the entity to manage and the type corresponding to the Id of that entity.

```java
@RestController
@RequestMapping("/v1/operations")
public class OperationController extends AbstractCRUDJPAController<Operation, Long> {
}
```

#### Shoji Token

> 🚨 Disclaimer 🚨 Authorization is done by the API Gateway and the usage of
Shoji token payload does not guarantee any king of access control.

Here you find how to access the Shoji token payload. And to do that you must
have a controller with following signature:

```java
import com.lib.shoji.ShojiToken;

@RestController
class MyController {
  public String myControllerMethod(
    @RequestHeader(name = ShojiToken.HEADER, required = false) ShojiToken token
  ) {

    // Done: the token argument will have the strong typed Shoji token
  }
}
```

And the following properties at the app's `application.yaml`:

```yaml
shoji:
  skip-signature-verification: false
  public-pkcs8-key-path: /etc/app/security/shoji.public.pkcs8.der
```

The `shoji.public.pkcs8.der` is available at Kubernetes and Openshift, just
add the following defintion at app's Deployment manifest: 

```yaml
apiVersion: apps/v1
kind: Deployment
#...
spec:
  #...
  template:
    spec:
      volumes:
        - name: shoji-public-pkcs8
          secret:
            secretName: shoji-public-pkcs8
      #...
      containers:
      - name: container
        #...
        volumeMounts:
          - mountPath: /etc/app/security/shoji.public.pkcs8.der
            name: shoji-public-pkcs8
            subPath: shoji.public.pkcs8.der
            readOnly: true
  #...
```

#### Skip TLS Verification

By default every request to `https://` will check the issuer of
that certificate.

To skip it, you should add the following property:

```yaml
app:
  security:
    tls:
      skip-validation: true
```

## Configuration

Configuration can be made using environment variables:

TODO:
