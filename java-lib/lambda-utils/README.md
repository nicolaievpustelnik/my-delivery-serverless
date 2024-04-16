# Exception Handling Library

#### This library provides a comprehensive solution for handling exceptions in Java applications, offering an easy way to create standardized and customized responses for API clients.

## Features

* Support for handling custom and standard Java exceptions
* Automatic generation of standardized responses according to RFC 7807
* Extensible and easy to integrate with frameworks like Spring and others
* Provides ResponseUtils and RequestUtils for easier handling of responses and requests

## How to Use

### To start using the library, follow the steps below:

* Add the library as a dependency in your project.
* Create your own custom exception classes, if necessary.
* Create your own ExceptionHandler classes to handle your custom exceptions.

```
// Your custom exception class
public class MyCustomException extends RuntimeException {
// ...
}

// Your custom exception handler
public class MyCustomExceptionHandler extends AbstractExceptionHandler<MyCustomException> {
// ...
}
```

### ResponseUtils and RequestUtils

* These utility classes can be used to simplify working with responses and requests in your application.

* ResponseUtils provides methods to create standardized Responses objects according to RFC 7807.

Example usage:

```
ProblemDetail problemDetail = // create or obtain a ProblemDetail instance
APIGatewayProxyResponseEvent response = ResponseUtils.createResponse(problemDetail, HttpStatus.BAD_REQUEST);
RequestUtils
```
